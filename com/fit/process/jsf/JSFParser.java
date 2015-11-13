package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.JspCondition;
import com.fit.process.jsf.condition.XHtmlCondition;
import com.fit.process.jsf.connection.JSFConfigToWebConfig;
import com.fit.process.jsf.connection.WebPageToWebPage;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNode;
import com.fit.util.Utils;

/**
 * Phan tich project dau vao de tao lien ket cac thanh phan trong JSFComponent
 * 
 * @author DucAnh
 *
 */
public class JSFParser implements IParser {
	private Node projectRootNode;

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);
		new JSFParser(projectRootNode);
	}

	public JSFParser(Node projectRootNode) {
		this.projectRootNode = projectRootNode;
		parse();
	}

	@Override
	public void parse() {
		List<Node> projects = Utils.getSubProjectsList(projectRootNode);

		/** Duy tat ca project con de tao cac lien ket */
		for (Node subProjectItem : projects) {
			List<Dependency> dependenciesList = new ArrayList<Dependency>();
			List<ManagedBeanNode> managedBeanNodeList = new ArrayList<>();

			/** Lay danh sach managed bean duoc dinh nghia trong file java */
			managedBeanNodeList.addAll(new CollectManagedBeanFromJavaFile(subProjectItem).getListManagedBeanNodes());

			/** Lay danh sach file cau hinh duoc dinh nghia trong web.xml */
			List<Node> listConfigurationFiles = new JSFConfigToWebConfig(subProjectItem).getListConfigJSFNode();

			for (Node configItem : listConfigurationFiles) {
				dependenciesList.addAll(WebPageToWebPage.getDependenciesList(configItem));
				//managedBeanNodeList.addAll(new JSFConfigToClass(configPath).collectManageBean(configItem));
			}

			/** Lay lien ket giua file giao dien va cac file managed bean */
			List<Node> listWebPagesList = getWebPagesList(subProjectItem);
			dependenciesList
					.addAll(TreeUpdater.getDependency(subProjectItem, managedBeanNodeList, listWebPagesList));
					/** Lay lien ket giua file giao dien va CSS */

			/** Tao lien ket giua cac file giao dien */
			TreeUpdater.createConnection(subProjectItem, dependenciesList);
		}
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
}
