package com.fit.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fit.config.Configuration;
import com.fit.loader.tree.ManagedBeanCondition;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.ComponentNode;
import com.fit.object.JspNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.object.XhtmlNode;
import com.fit.object.XmlNode;

/**
 * Load 1 project
 */
public class ProjectLoader {
	public static void main(String[] args) {
		String projectRootPath = "C:\\Users\\DucAnh\\Dropbox\\Project\\J2EE\\DEMO J2EE 2\\dukes-forest\\dukes-forest";
		// System.out.println(ProjectLoader.load(projectRootPath).getChildren().get(0).toString());
		List<Node> listManagedBeanFile = Search.searchNode(ProjectLoader.load(projectRootPath),
				new ManagedBeanCondition());
		for (Node item : listManagedBeanFile) {
			System.out.println(item.getPath());
		}
	}

	/**
	 * Load 1 project Input: Java EE Project Output: ProjectNode
	 * 
	 * @param projectRootPath
	 *            duong dan toi thu muc goc cua project
	 * @return node kieu project
	 */
	public static ProjectNode load(String projectRootPath) {
		ProjectNode projectNode = new ProjectNode();
		projectNode.setPath(projectRootPath);
		parseSrcFolder(projectNode, projectNode.getPath());
		return projectNode;
	}

	private static void parseSrcFolder(Node parent, String path) {

		ArrayList<String> children = getChildren(path);

		for (String pathItem : children)
			switch (getTypeOfPath(pathItem)) {
			case Configuration.CLASS_FILE:
				ClassNode classNode = new ClassNode();
				classNode.setPath(pathItem);
				parent.getChildren().add(classNode);
				
//				System.out.println(pathItem);
				break;
			case Configuration.JSP_FILE:
				JspNode jspFile = new JspNode();
				jspFile.setPath(pathItem);
				parent.getChildren().add(jspFile);
				break;
			case Configuration.XHTML_FILE:
				XhtmlNode xhtmlNode = new XhtmlNode();
				xhtmlNode.setPath(pathItem);
				parent.getChildren().add(xhtmlNode);
				break;
			case Configuration.XML_FILE:
				XmlNode xmlNode = new XmlNode();
				xmlNode.setPath(pathItem);
				parent.getChildren().add(xmlNode);
				break;
			case Configuration.COMPONENT:
				Node componentNode = new ComponentNode();
				componentNode.setPath(pathItem);

				parent.getChildren().add(componentNode);
				componentNode.setParent(parent);

				parseSrcFolder(componentNode, pathItem);
				break;
			}
	}

	/**
	 * 
	 * @param pathItem
	 *            Duong dan tuyet doi cua doi tuong
	 * @return Kieu doi tuong
	 */
	private static int getTypeOfPath(String pathItem) {
		if (pathItem.contains(CLASS_SYMBOL))
			return Configuration.CLASS_FILE;
		if (pathItem.contains(JSP_SYMBOL))
			return Configuration.JSP_FILE;
		if (pathItem.contains(XHTML_SYMBOL))
			return Configuration.XHTML_FILE;
		if (pathItem.contains(XML_SYMBOL))
			return Configuration.XML_FILE;

		// check whether is folder
		File file = new File(pathItem);
		if (file.isDirectory())
			return Configuration.COMPONENT;

		return Configuration.UNDEFINED_COMPONENT;
	}

	/**
	 * 
	 * @param path
	 *            Duong dan tuyet doi
	 * @return Cac duong dan tuyet doi cua cac thanh phan ben trong
	 */
	public static ArrayList<String> getChildren(String path) {
		ArrayList<String> pathOfChildren = new ArrayList<String>();
		File file = new File(path);
		String[] names = file.list();

		for (String name : names) {
			pathOfChildren.add(path + "\\" + name);

			// System.out.println(path + "\\" + name);
		}

		return pathOfChildren;
	}

	private static final String CLASS_SYMBOL = ".java";
	private static final String XHTML_SYMBOL = ".xhtml";
	private static final String JSP_SYMBOL = ".jsp";
	private static final String XML_SYMBOL = ".xml";

}
