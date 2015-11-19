package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.ConfigurationNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.JsfUtils;
import com.fit.process.jsf.condition.ConfigurationCondition;
import com.fit.process.jsf.condition.WebPageCondition;
import com.fit.process.jsf.object.Dependency;
import com.fit.util.Utils;

/**
 * 
 * Input: Mot project con <br/>
 * Output: Danh sach su phu thuoc (web.xml, JSFConfigfiles), danh sach JSFConfig
 * files, uri pattern
 * 
 * @author DucAnh
 *
 */
public class WebConfigParser extends DependenciesGeneration {
	private List<Node> jsfConfigNodes_ = new ArrayList<>();
	private Node webConfig_ = new ConfigurationNode();
	private Node projectRootNode_;
	private String urlPattern_;
	private List<Node> welcomeNodes_;

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.MULTIPLE_CONFIG_JSF1);

		WebConfigParser parser = new WebConfigParser(projectRootNode);

		System.out.println("web.xml path: " + parser.getWebConfig().getPath());

		System.out.println("JSF configs:");
		for (Node n : parser.getListConfigJSFNode()) {
			System.out.println("\t" + n.getPath());
		}

		System.out.println("Dependencies List:");
		for (Dependency n : parser.getDependencies()) {
			System.out.println(n.toString());
		}

		System.out.println("URL pattern: ");
		System.out.println(parser.getUriPattern());

		System.out.println("Welcome files: ");
		System.out.println(parser.getWelcomeNodes());
	}

	/**
	 * 
	 * @param projectNode
	 *            project con
	 */
	public WebConfigParser(Node projectNode) {
		this.projectRootNode_ = projectNode;

		webConfig_ = findWebConfigNode(projectNode);

		Document dom = Utils.getDOM(webConfig_.getPath());
		if (dom != null) {
			welcomeNodes_ = findWelcomeNodes(dom);
			urlPattern_ = findUrlPattern(dom);
			if (urlPattern_ != null) {
				jsfConfigNodes_ = findJsfConfigNodes(dom, projectNode);
				dependencies = findDependencies();
			}
		}
	}

	/**
	 * Tim danh sach welcome file nodes duoc dinh nghia
	 * 
	 * @param dom
	 * @return
	 */
	private List<Node> findWelcomeNodes(Document dom) {
		List<Node> output = new ArrayList<>();

		final String WELCOME_FILE_TAG = "welcome-file";

		final NodeList welcomeFileNodes = findTag(dom, WELCOME_FILE_TAG);
		for (int temp = 0; temp < welcomeFileNodes.getLength(); temp++) {
			final org.w3c.dom.Node nTmpNode = welcomeFileNodes.item(temp);
			String relativeWelcomeFile = normalize(nTmpNode.getTextContent());

			final List<Node> welcomeNodes = JsfUtils.searchNode(projectRootNode_, new WebPageCondition(),
					relativeWelcomeFile);
			output.addAll(welcomeNodes);
		}
		return output;
	}

	/**
	 * Tim kiem Node web.xml
	 * 
	 * @param projectNode
	 * @return
	 */
	private Node findWebConfigNode(Node projectNode) {
		final List<Node> webConfig = JsfUtils.searchNode(projectNode, new ConfigurationCondition(), WEB_CONFIG_PATH);
		if (webConfig.size() != 0) {
			final Node selectedWebConfig = webConfig.get(0);
			return selectedWebConfig;
		}
		return null;
	}

	/**
	 * Tim url-pattern cuar JSF
	 * 
	 * @param doc
	 * @return
	 */
	private String findUrlPattern(Document doc) {
		String uriPattern = null;
		NodeList tag = findTag(doc, URL_PATTERN);
		uriPattern = tag.item(0).getTextContent();
		return uriPattern;
	}

	/**
	 * Danh sach Node tuong ung voi JSFConfig
	 * 
	 * @param projectNode
	 * @return
	 */
	private List<Node> findJsfConfigNodes(Document doc, Node projectNode) {
		List<Node> listConfigJSFNode = new ArrayList<>();

		// Lay duong dan tuyet doi file face-config.xml
		final List<Node> faces_config = JsfUtils.searchNode(projectNode, new ConfigurationCondition(),
				DEFAULT_JSF_CONFIG_NAME);
		if (faces_config.size() == 1) {
			listConfigJSFNode.add(faces_config.get(0));
		}

		// Lay duong dan tuyet doi cac file config trong web.xml
		final String[] configsFile = getRelativePathOfJSFConfig(doc);
		if (configsFile != null)
			for (String config : configsFile) {

				// Lay node tuong ung voi duong dan tuong doi file cau hinh
				List<Node> configList = JsfUtils.searchNode(projectNode, new ConfigurationCondition(), config);
				if (configList.size() == 1) {
					final Node configPath = configList.get(0);
					listConfigJSFNode.add(configPath);
				}
			}
		return listConfigJSFNode;
	}

	@Override
	protected List<Dependency> findDependencies() {
		List<Dependency> dependencies = new ArrayList<>();
		for (Node JSFConfig : jsfConfigNodes_) {
			final Dependency d = new Dependency();
			d.setBiPhuThuoc(JSFConfig);
			d.setGayPhuThuoc(webConfig_);

			dependencies.add(d);
		}

		for (Node welcomeFileNode : welcomeNodes_) {
			final Dependency d = new Dependency();
			d.setBiPhuThuoc(welcomeFileNode);
			d.setGayPhuThuoc(webConfig_);

			dependencies.add(d);
		}
		return dependencies;
	}

	// -------------------------------------------------------------------------------------------------------
	/**
	 * Lay duong dan tuong doi cac file cau hinh JSF
	 * 
	 * @return
	 */
	private String[] getRelativePathOfJSFConfig(Document doc) {
		try {
			final NodeList nList = findTag(doc, CONTEXT_PARAM_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				final org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					final Element eElement = (Element) nNode;
					final String paramName = findTag(eElement, PARAM_NAME_TAG).item(0).getTextContent();

					if (paramName != null && paramName.equals(CONFIG_FILES_TAG)) {
						String paramNameList = findTag(eElement, PARAM_VALUE_TAG).item(0).getTextContent();
						if (paramNameList != null) {
							// Loai bo cac ki tu thua
							paramNameList = normalize(paramNameList);
							return paramNameList.split(",");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private NodeList findTag(Document doc, String tag) {
		return doc.getElementsByTagName(tag);
	}

	private String normalize(String str) {
		return str.replace("\n", "").replace(" ", "").replace("\r", "");
	}

	private NodeList findTag(Element e, String tag) {
		return e.getElementsByTagName(tag);
	}

	public List<Node> getListConfigJSFNode() {
		return jsfConfigNodes_;
	}

	public Node getWebConfig() {
		return webConfig_;
	}

	public String getUriPattern() {
		return urlPattern_;
	}

	public List<Node> getWelcomeNodes() {
		return welcomeNodes_;
	}

	private static final String CONFIG_FILES_TAG = "javax.faces.CONFIG_FILES";
	private static final String CONTEXT_PARAM_TAG = "context-param";
	private static final String PARAM_NAME_TAG = "param-name";
	private static final String PARAM_VALUE_TAG = "param-value";
	private static final String URL_PATTERN = "url-pattern";
	private static final String WEB_CONFIG_PATH = "\\web\\WEB-INF\\web.xml";
	private static final String DEFAULT_JSF_CONFIG_NAME = "faces-config.xml";
}
