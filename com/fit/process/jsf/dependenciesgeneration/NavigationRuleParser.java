package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.process.jsf.condition.XHtmlCondition;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNodeContainer;
import com.fit.process.jsf.object.NavigationRuleTag;

/**
 * Phan tich mot navigation rule de tao danh sach phu thuoc (JSF Config, managed
 * beans), (JSF Config, web pages)
 * 
 * @author DucAnh
 *
 */
public class NavigationRuleParser extends DependenciesGeneration {
	NavigationRuleTag navigationRule;
	List<Node> webPagesList;
	List<ManagedBeanNodeContainer> managedBeansList;
	Node projectNode;

	public NavigationRuleParser(NavigationRuleTag navigationRule, Node projectNode, List<Node> webPagesList,
			List<ManagedBeanNodeContainer> managedBeansList) {
		this.navigationRule = navigationRule;
		this.webPagesList = webPagesList;
		this.managedBeansList = managedBeansList;
		this.projectNode = projectNode;

		dependenciesList = findDependencies();
	}

	/**
	 * 
	 * @param navigationRule
	 * @param managedBeansList
	 * @return
	 */
	private List<Dependency> linkToManagedBeans(NavigationRuleTag navigationRule,
			List<ManagedBeanNodeContainer> managedBeansList) {
		List<Dependency> output = new ArrayList<>();
		try {
			/** mot navigation co nhieu from-action */
			List<Node> dependencyMBClass = new ArrayList<>();
			NodeList nList = navigationRule.getContent().getElementsByTagName(FROM_ACTION);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				org.w3c.dom.Node nTmpNode = nList.item(temp);
				String command = nTmpNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
				for (ManagedBeanNodeContainer mbNode : managedBeansList)
					if (command.contains("#{" + mbNode.getName() + "."))
						dependencyMBClass.add(mbNode.getClassNode());
			}
			/** Tao lien ket */
			for (Node n : dependencyMBClass) {
				Dependency d = new Dependency();
				d.setBiPhuThuoc(n);
				d.setGayPhuThuoc(navigationRule.getJSFConfig());
				output.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	private List<Dependency> linkToWebPages(NavigationRuleTag navigationRule, List<Node> webPagesList) {
		List<Dependency> output = new ArrayList<>();
		try {
			/** mot navigation chi co mot from-view-id */
			org.w3c.dom.Node nNode = navigationRule.getContent().getElementsByTagName(FROM_WIEW_ID).item(0);
			String relativeWebsPath = nNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
			List<Node> dependencyWebPages = Search.searchNode(projectNode, new XHtmlCondition(), relativeWebsPath);

			/** mot navigation co nhieu to-view-id */
			NodeList nList = navigationRule.getContent().getElementsByTagName(TO_WIEW_ID);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				org.w3c.dom.Node nTmpNode = nList.item(temp);
				relativeWebsPath = nTmpNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
				dependencyWebPages.addAll(Search.searchNode(projectNode, new XHtmlCondition(), relativeWebsPath));
			}

			/** Tao lien ket */
			for (Node n : dependencyWebPages) {
				Dependency d = new Dependency();
				d.setBiPhuThuoc(n);
				d.setGayPhuThuoc(navigationRule.getJSFConfig());
				output.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> output = new ArrayList<>();
		output.addAll(linkToManagedBeans(navigationRule, managedBeansList));
		output.addAll(linkToWebPages(navigationRule, webPagesList));
		return output;
	}

	private static final String FROM_WIEW_ID = "from-view-id";
	private static final String TO_WIEW_ID = "to-view-id";
	private static final String FROM_ACTION = "from-action";
}
