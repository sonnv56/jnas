package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NodeList;

import com.fit.object.Node;
import com.fit.process.jsf.JsfUtils;
import com.fit.process.jsf.condition.NodeCondition;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.MbNodeContainer;
import com.fit.process.jsf.object.NavigationRuleTag;

/**
 * Phan tich mot navigation rule de tao danh sach phu thuoc (JSF Config, managed
 * beans), (JSF Config, web pages)
 * 
 * @author DucAnh
 *
 */
public class NavigationRuleParser extends DependenciesGeneration {
	NavigationRuleTag navigationRule_;
	List<Node> webPages_;
	List<MbNodeContainer> mbNodeContainers_;
	Node projectNode;

	public NavigationRuleParser(NavigationRuleTag navigationRule, Node projectNode, List<Node> webPagesList,
			List<MbNodeContainer> managedBeansList) {
		this.navigationRule_ = navigationRule;
		this.webPages_ = webPagesList;
		this.mbNodeContainers_ = managedBeansList;
		this.projectNode = projectNode;

		dependencies = findDependencies();
	}

	/**
	 * Tao su phu thuoc giua jsf confjg va class
	 * 
	 * @param navigationRule
	 * @param mbNodeContainers
	 * @return
	 */
	private List<Dependency> linkToManagedBeans(NavigationRuleTag navigationRule,
			List<MbNodeContainer> mbNodeContainers) {
		List<Dependency> output = new ArrayList<>();
		try {
			/** mot navigation co nhieu from-action */
			final List<Node> dependencyMBClasses = new ArrayList<>();
			final NodeList nList = navigationRule.getContent().getElementsByTagName(FROM_ACTION);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				final org.w3c.dom.Node nTmpNode = nList.item(temp);
				final String command = nTmpNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
				for (MbNodeContainer mbNode : mbNodeContainers)
					if (command.contains("#{" + mbNode.getName() + "."))
						dependencyMBClasses.add(mbNode.getClassNode());
			}
			/** Tao lien ket */
			for (Node classNode : dependencyMBClasses) {
				final Dependency d = new Dependency();
				d.setBiPhuThuoc(classNode);
				d.setGayPhuThuoc(navigationRule.getJSFConfig());

				output.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Tao su phu thuoc giua jsf config va web page
	 * 
	 * @param navigationRule
	 * @param webPages
	 * @return
	 */
	private List<Dependency> linkToWebPages(NavigationRuleTag navigationRule, List<Node> webPages) {
		List<Dependency> output = new ArrayList<>();
		try {
			String relativeWebPath;
			List<Node> dependencyWebPages = new ArrayList<>();
			/** mot navigation chi co mot from-view-id */
			final org.w3c.dom.Node nNode = navigationRule.getContent().getElementsByTagName(FROM_WIEW_ID).item(0);

			// Neu nevigation-rule dinh nghia from-view-id
			if (nNode != null) {
				relativeWebPath = nNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
				dependencyWebPages = JsfUtils.searchNode(projectNode, new NodeCondition(), relativeWebPath);
			}

			/** mot navigation co nhieu to-view-id */
			NodeList nList = navigationRule.getContent().getElementsByTagName(TO_WIEW_ID);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				final org.w3c.dom.Node nTmpNode = nList.item(temp);
				relativeWebPath = nTmpNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
				dependencyWebPages.addAll(JsfUtils.searchNode(projectNode, new NodeCondition(), relativeWebPath));
			}

			/** Tao lien ket */
			for (Node n : dependencyWebPages) {
				final Dependency d = new Dependency();
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
		output.addAll(linkToManagedBeans(navigationRule_, mbNodeContainers_));
		output.addAll(linkToWebPages(navigationRule_, webPages_));
		return output;
	}

	private static final String FROM_WIEW_ID = "from-view-id";
	private static final String TO_WIEW_ID = "to-view-id";
	private static final String FROM_ACTION = "from-action";
}
