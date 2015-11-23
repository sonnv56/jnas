package com.fit.process.jsf;

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
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.ConfigurationCondition;
import com.fit.process.jsf.condition.JspCondition;
import com.fit.process.jsf.condition.XHtmlCondition;
import com.fit.process.jsf.dependenciesgeneration.ManagedBeanTagParser;
import com.fit.process.jsf.dependenciesgeneration.NavigationRuleParser;
import com.fit.process.jsf.dependenciesgeneration.WebConfigParser;
import com.fit.process.jsf.dependenciesgeneration.XhtmlPageParser;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanTag;
import com.fit.process.jsf.object.MbNodeContainer;
import com.fit.process.jsf.object.NavigationRuleTag;
import com.fit.util.Utils;

/**
 * Phan tich project dau vao de tao lien ket cac thanh phan trong JSFComponent
 * 
 * @author DucAnh
 *
 */
public class JSFParser implements IProjectParser {
	private final Node projectRootNode_;
	private List<Dependency> dependencies_ = new ArrayList<Dependency>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH2);
		List<Dependency> dependencies = new JSFParser(projectRootNode).getDependenciesList();
		for (Dependency dependency : dependencies)
			System.out.println(dependency.toString());
	}

	public JSFParser(Node projectRootNode) {
		this.projectRootNode_ = projectRootNode;
		parse();
	}

	@Override
	public void parse() {
		final List<Node> projectNodes = Utils.getProjects(projectRootNode_);

		for (Node projectNode : projectNodes)
			if (useJsfTechnology(projectNode)) {
				/** Tim va phan tich web.xml */
				final WebConfigParser webConfigParser = new WebConfigParser(projectNode);
				final List<Node> jsfConfigNodes = webConfigParser.getListConfigJSFNode();
				final List<Dependency> dependencies = webConfigParser.getDependencies();
				
				ParamaterWebConfig.URL_PATTERN = webConfigParser.getUriPattern();
				dependencies_.addAll(dependencies);

				/** Lay danh sach managed bean duoc dinh nghia trong file */
				List<MbNodeContainer> mbNodes = new CollectMbFromJavaFile(projectNode).getMbNodes();

				/** Luu tat ca cac node managed bean trong config */
				for (Node jsfConfigNode : jsfConfigNodes) {
					final List<ManagedBeanTag> mbTags = getManagedBeanTagList(jsfConfigNode);

					for (ManagedBeanTag mbTag : mbTags) {
						final ManagedBeanTagParser mBTagParser = new ManagedBeanTagParser(mbTag, projectNode);

						final List<MbNodeContainer> mbNodeContainers = mBTagParser.getManagedBeanContainers();
						mbNodes.addAll(mbNodeContainers);

						final List<Dependency> dependencies2 = mBTagParser.getDependencies();
						dependencies_.addAll(dependencies2);
					}
				}

				/** Tim va phan tich navigation rule trong JSF config */
				final List<Node> webPageNodes = getWebPagesList(projectNode);
				for (Node configJSFNode : jsfConfigNodes) {
					List<NavigationRuleTag> navigationRules = getNavigationRule(configJSFNode);

					for (NavigationRuleTag navigationRule : navigationRules) {
						final NavigationRuleParser parser = new NavigationRuleParser(navigationRule, projectNode,
								webPageNodes, mbNodes);

						final List<Dependency> dependencies2 = parser.getDependencies();
						dependencies_.addAll(dependencies2);
					}
				}

				/** Tim va phan tich web pages */
				for (Node webPage : webPageNodes) {
					final XhtmlPageParser xhtmlWebPageParser = new XhtmlPageParser(webPage, projectNode, mbNodes);

					final List<Dependency> dependencies2 = xhtmlWebPageParser.getDependencies();
					dependencies_.addAll(dependencies2);
				}
			}
	}

	/**
	 * Lay danh sach managed bean tag trong file config JSF
	 * 
	 * @param webConfig
	 * @return
	 */
	private List<ManagedBeanTag> getManagedBeanTagList(Node confjgJSFNode) {
		List<ManagedBeanTag> mbTags = new ArrayList<>();
		final String MANAGED_BEAN_TAG = "managed-bean";
		try {
			// default code
			File inputFile = new File(confjgJSFNode.getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// my code
			NodeList nList = doc.getElementsByTagName(MANAGED_BEAN_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Element nTmpNode = (Element) nList.item(temp);

				ManagedBeanTag mbTag = new ManagedBeanTag();
				mbTag.setContent(nTmpNode);
				mbTag.setJSFConfig(confjgJSFNode);

				mbTags.add(mbTag);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mbTags;
	}

	/**
	 * Lay danh sach navigation rule trong file config JSF
	 * 
	 * @param webConfig
	 * @return
	 */
	private List<NavigationRuleTag> getNavigationRule(Node confjgJSFNode) {
		List<NavigationRuleTag> navigationRules = new ArrayList<>();
		final String NAVIGATION_RULE_TAG = "navigation-rule";
		try {
			// default code
			File inputFile = new File(confjgJSFNode.getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// my code
			NodeList nList = doc.getElementsByTagName(NAVIGATION_RULE_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Element nTmpNode = (Element) nList.item(temp);

				NavigationRuleTag nrTag = new NavigationRuleTag();
				nrTag.setContent(nTmpNode);
				nrTag.setJSFConfig(confjgJSFNode);

				navigationRules.add(nrTag);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return navigationRules;
	}

	/**
	 * Lay danh sach cac file giao dien
	 * 
	 * @param projectNode
	 *            project con
	 * @return
	 */
	private List<Node> getWebPagesList(Node projectNode) {
		List<Node> xhtmlNodes = Search.searchNode(projectNode, new XHtmlCondition());
		List<Node> jspNodes = Search.searchNode(projectNode, new JspCondition());
		xhtmlNodes.addAll(jspNodes);
		return xhtmlNodes;
	}

	/**
	 * Kiem tra project co su dung cong nghe JSF khong
	 * 
	 * @param project
	 * @return
	 */
	private boolean useJsfTechnology(Node project) {
		final List<Node> webConfigNodes = JsfUtils.searchNode(project, new ConfigurationCondition(), "\\web.xml");

		if (webConfigNodes.size() > 0) {
			final Document doc = Utils.getDOM(webConfigNodes.get(0).getPath());

			final String SERVLET_CLASS_TAG = "servlet-class";
			final NodeList nList = doc.getElementsByTagName(SERVLET_CLASS_TAG);

			for (int temp = 0; temp < nList.getLength(); temp++) {
				final org.w3c.dom.Node nTmpNode = nList.item(temp);
				final String content = nTmpNode.getTextContent();

				final String JSF_SYMBOL = "javax.faces.webapp.FacesServlet";
				if (content != null && content.equals(JSF_SYMBOL))
					return true;
			}
		}
		return false;
	}

	public List<Dependency> getDependenciesList() {
		return dependencies_;
	}
}
