package com.fit.process.jsf;

import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.JspCondition;
import com.fit.process.jsf.condition.XHtmlCondition;

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
