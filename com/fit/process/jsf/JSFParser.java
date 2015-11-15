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
import com.fit.process.jsf.dependenciesgeneration.XhtmlWebPageParser;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNodeContainer;
import com.fit.process.jsf.object.ManagedBeanTag;
import com.fit.process.jsf.object.NavigationRuleTag;
import com.fit.util.Utils;

/**
 * Phan tich project dau vao de tao lien ket cac thanh phan trong JSFComponent
 * 
 * @author DucAnh
 *
 */
public class JSFParser implements IProjectParser {
	private final Node projectRootNode;
	private List<Dependency> dependenciesList = new ArrayList<Dependency>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH2);
		List<Dependency> dependenciesList = new JSFParser(projectRootNode).getDependenciesList();
		for (Dependency d : dependenciesList)
			System.out.println(d.toString());
	}

	public JSFParser(Node projectRootNode) {
		this.projectRootNode = projectRootNode;
		parse();
	}

	@Override
	public void parse() {
		final List<Node> subProjectNodeList = Utils.getSubProjectsList(projectRootNode);

		for (Node subProjectNode : subProjectNodeList)
			if (Search.searchNode(subProjectNode, new ConfigurationCondition(),
					subProjectNode.getNodeName() + "\\web\\WEB-INF\\web.xml").size() > 0) {
				/** Tim va phan tich web.xml */
				final WebConfigParser webConfigParser = new WebConfigParser(subProjectNode);
				final List<Node> listConfigJSFNode = webConfigParser.getListConfigJSFNode();

				dependenciesList.addAll(webConfigParser.getDependenciesList());

				/**
				 * Lay danh sach managed bean duoc dinh nghia trong file java va
				 * JSF config
				 */
				// Lay danh sach managed bean duoc dinh nghia trong file
				final List<ManagedBeanNodeContainer> managedBeansList = new CollectManagedBeanFromJavaFile(
						subProjectNode).getListManagedBeanNodes();

				// luu tat ca cac node managed bean trong config
				for (Node configJSFNode : listConfigJSFNode) {
					List<ManagedBeanTag> managedBeanTagList = getManagedBeanTag(configJSFNode);
					for (ManagedBeanTag managedBeanTag : managedBeanTagList) {
						final ManagedBeanTagParser managedBeanTagParser = new ManagedBeanTagParser(managedBeanTag,
								subProjectNode);
						final List<ManagedBeanNodeContainer> mbNodeContainerList = managedBeanTagParser
								.getManagedBean();
						managedBeansList.addAll(mbNodeContainerList);

						// get dependency
						dependenciesList.addAll(managedBeanTagParser.getDependenciesList());
					}
				}

				/** Tim va phan tich navigation rule trong JSF config */
				final List<Node> webPagesList = getWebPagesList(subProjectNode);
				for (Node configJSFNode : listConfigJSFNode) {
					List<NavigationRuleTag> navigationRuleList = getNavigationRule(configJSFNode);
					
					for (NavigationRuleTag navigationRule : navigationRuleList) {
						final NavigationRuleParser parser = new NavigationRuleParser(navigationRule, subProjectNode,
								webPagesList, managedBeansList);

						// get dependency
						dependenciesList.addAll(parser.getDependenciesList());
					}
				}

				/** Tim va phan tich web pages */
				for (Node webPage : webPagesList) {
					final XhtmlWebPageParser xhtmlWebPageParser = new XhtmlWebPageParser(webPage, subProjectNode,
							managedBeansList);

					// get dependency
					dependenciesList.addAll(xhtmlWebPageParser.getDependenciesList());
				}
			}
	}

	/**
	 * Lay danh sach managed bean tag trong file config JSF
	 * 
	 * @param webConfig
	 * @return
	 */
	private List<ManagedBeanTag> getManagedBeanTag(Node confjgJSFNode) {
		List<ManagedBeanTag> managedBeanList = new ArrayList<>();
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

				managedBeanList.add(mbTag);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return managedBeanList;
	}

	/**
	 * Lay danh sach navigation rule trong file config JSF
	 * 
	 * @param webConfig
	 * @return
	 */
	private List<NavigationRuleTag> getNavigationRule(Node confjgJSFNode) {
		List<NavigationRuleTag> navigationRuleList = new ArrayList<>();
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

				navigationRuleList.add(nrTag);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return navigationRuleList;
	}

	/**
	 * Lay danh sach cac file giao dien
	 * 
	 * @param projectNode
	 *            project con
	 * @return
	 */
	private List<Node> getWebPagesList(Node projectNode) {
		List<Node> listWebPagesList = Search.searchNode(projectNode, new XHtmlCondition());
		List<Node> listJspNodes = Search.searchNode(projectNode, new JspCondition());
		listWebPagesList.addAll(listJspNodes);
		return listWebPagesList;
	}

	public List<Dependency> getDependenciesList() {
		return dependenciesList;
	}
}
