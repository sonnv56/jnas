package com.fit.loader;

import java.io.File;
import java.util.ArrayList;

import com.fit.config.Configuration;
import com.fit.ducanh.test.displayTree.TreeStrategy;
import com.fit.object.ClassNode;
import com.fit.object.ComponentNode;
import com.fit.object.JspNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.object.XhtmlNode;
import com.fit.util.Utils;
import com.fit.object.ConfigurationNode;

/**
 * Load 1 project
 */
public class ProjectLoader {
	public static void main(String[] args) {
		// Project tree generation
		String projectRootPath = "/home/rmrf/builds/dukes-forest";
		ProjectNode projectRootNode = ProjectLoader.load(projectRootPath);

		// display tree of project
		TreeStrategy treeDisplayer = new TreeStrategy(projectRootNode);
		System.out.println(treeDisplayer.getString());
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
		projectNode.setId(NUMBER_OF_NODES++);
		parseSrcFolder(projectNode, projectNode.getPath());
		return projectNode;
	}

	private static void parseSrcFolder(Node parent, String path) {

		ArrayList<String> children = getChildren(path);

		for (String pathItem : children)
			switch (getTypeOfPath(pathItem)) {
			case Configuration.CLASS_FILE:
				ClassNode classNode = new ClassNode(NUMBER_OF_NODES++, pathItem, parent);
				break;
			case Configuration.JSP_FILE:
				JspNode jspFile = new JspNode(NUMBER_OF_NODES++, pathItem, parent);

				break;
			case Configuration.XHTML_FILE:
				XhtmlNode xhtmlNode = new XhtmlNode(NUMBER_OF_NODES++, pathItem, parent);
				break;
			case Configuration.XML_FILE:
				ConfigurationNode xmlNode = new ConfigurationNode(NUMBER_OF_NODES++, pathItem, parent);
				break;
			case Configuration.COMPONENT:
				Node componentNode = new ComponentNode(NUMBER_OF_NODES++, pathItem, parent);

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
		if (Utils.fileEndsWith(pathItem, CLASS_EXTENSION))
			return Configuration.CLASS_FILE;
		if (Utils.fileEndsWith(pathItem, JSP_EXTENSION))
			return Configuration.JSP_FILE;
		if (Utils.fileEndsWith(pathItem, XHTML_EXTENSION))
			return Configuration.XHTML_FILE;
		if (Utils.fileEndsWith(pathItem, XML_EXTENSION) || Utils.fileEndsWith(pathItem, WSDL_EXTENSION))
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
			pathOfChildren.add(path + File.separator + name);
		}

		return pathOfChildren;
	}

	private static final String CLASS_EXTENSION = "java";
	private static final String XHTML_EXTENSION = "xhtml";
	private static final String JSP_EXTENSION = "jsp";
	private static final String XML_EXTENSION = "xml";
	private static final String WSDL_EXTENSION = "wsdl";

	private static int NUMBER_OF_NODES = 0;
}
