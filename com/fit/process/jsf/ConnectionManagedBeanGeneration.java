package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;
import com.fit.object.ProjectNode;

/**
 * Tao lien ket tu jsp/xhtml to class managed bean
 * 
 * @author DucAnh
 *
 */
public class ConnectionManagedBeanGeneration {
	public ConnectionManagedBeanGeneration(ProjectNode projectRootPath) {
		List<Node> listManagedBeanNodes = getAllManagedBeanNodes(projectRootPath);
		List<Node> listXHtmlNodes = getAllXhtmlNodes(projectRootPath);
		List<Node> listJspNodes = getAllJspNodes(projectRootPath);
		
		for (Node jspNode : listJspNodes) {
			JspFileParser jspFileParser = new JspFileParser(jspNode);
			jspFileParser.parse();
		}
		
	}

	private List<Node> getAllManagedBeanNodes(ProjectNode projectRootPath) {
		List<Node> listNode = new ArrayList<>();
		// do something here
		return listNode;
	}

	private List<Node> getAllXhtmlNodes(ProjectNode projectRootPath) {
		List<Node> listNode = new ArrayList<>();
		// do something here
		return listNode;
	}

	private List<Node> getAllJspNodes(ProjectNode projectRootPath) {
		List<Node> listNode = new ArrayList<>();
		// do something here
		return listNode;
	}
}
