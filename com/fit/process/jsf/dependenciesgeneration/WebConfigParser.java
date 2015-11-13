package com.fit.process.jsf.dependenciesgeneration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.ConfigurationNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.ConfigurationCondition;
import com.fit.process.jsf.object.Dependency;

/**
 * 
 * Input: Mot project con <br/>
 * Output: Danh sach su phu thuoc (web.xml, JSFConfigfiles), danh sach JSFConfig
 * files
 * 
 * @author DucAnh
 *
 */
public class WebConfigParser extends DependenciesGeneration {
	private List<Node> listConfigJSFNode = new ArrayList<>();
	private Node webConfig = new ConfigurationNode();

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
		for (Dependency n : parser.getDependenciesList()) {
			System.out.println(n.toString());
		}
	}

	/**
	 * 
	 * @param projectNode
	 *            project con
	 */
	public WebConfigParser(Node projectNode) {
		webConfig = findWebConfig(projectNode);
		listConfigJSFNode = findListConfigJSFNode(projectNode, webConfig);
		dependenciesList = findDependencies();
	}

	private Node findWebConfig(Node projectNode) {
		// Tim kiem Node web.xml
		List<Node> webConfig = Search.searchNode(projectNode, new ConfigurationCondition(),
				projectNode.getNodeName() + "\\web\\WEB-INF\\web.xml");
		if (webConfig == null || webConfig.size() == 0)
			return null;
		Node selectedWebConfig = webConfig.get(0);
		return selectedWebConfig;
	}

	/**
	 * Danh sach Node tuong ung voi JSFConfig
	 * 
	 * @param projectNode
	 * @return
	 */
	private List<Node> findListConfigJSFNode(Node projectNode, Node nWebConfig) {
		List<Node> listConfigJSFNode = new ArrayList<>();

		// Lay duong dan tuyet doi cac file config trong web.xml
		String[] configsFile = parseConfigFile(nWebConfig);
		if (configsFile != null)
			for (String config : configsFile) {

				// Lay node tuong ung voi duong dan tuong doi file cau hinh
				List<Node> configList = Search.searchNode(projectNode, new ConfigurationCondition(),
						projectNode.getNodeName() + "\\web" + config);

				if (configList == null || configList.size() == 0) {
					// de phong truong hop xay ra
				} else if (configList.size() == 1) {
					// hien nhien dung
					Node configPath = configList.get(0);
					listConfigJSFNode.add(configPath);
				}
			}

		return listConfigJSFNode;
	}

	/**
	 * Lay duong dan tuong doi cac file cau hinh JSF
	 * 
	 * @param selectedWebConfig
	 * @return
	 */
	private String[] parseConfigFile(Node selectedWebConfig) {
		try {
			// default code
			File inputFile = new File(selectedWebConfig.getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// our code
			NodeList nList = doc.getElementsByTagName(CONTEXT_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String paramName = eElement.getElementsByTagName(PARAM_NAME_TAG).item(0).getTextContent();
					if (paramName.equals(CONFIG_FILES_TAG)) {
						String paramNameList = eElement.getElementsByTagName(PARAM_VALUE_TAG).item(0).getTextContent();

						// Loai bo cac ki tu thua
						paramNameList = paramNameList.replace("\n", "").replace(" ", "").replace("\r", "");
						return paramNameList.split(",");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Node> getListConfigJSFNode() {
		return listConfigJSFNode;
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> dependenciesList = new ArrayList<>();
		for (Node JSFConfig : listConfigJSFNode) {
			Dependency d = new Dependency();
			d.setBiPhuThuoc(JSFConfig);
			d.setGayPhuThuoc(webConfig);
			dependenciesList.add(d);
		}
		return dependenciesList;
	}

	public Node getWebConfig() {
		return webConfig;
	}

	private static final String CONFIG_FILES_TAG = "javax.faces.CONFIG_FILES";
	private static final String CONTEXT_TAG = "context-param";
	private static final String PARAM_NAME_TAG = "param-name";
	private static final String PARAM_VALUE_TAG = "param-value";
}
