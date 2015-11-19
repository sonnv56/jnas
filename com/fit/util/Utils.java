package com.fit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.fit.config.Configuration;
import com.fit.loader.tree.ClassCondition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;

/**
 * Bo thu vien cac tinh nang
 */
public class Utils {
	/**
	 * Doc noi dung file java hoac xhtml, xml, jsp
	 * 
	 * @param filePath
	 *            duong dan tuyet doi file
	 * @return noi dung file
	 */
	public static String readFileContent(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(3000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static String getParentOfANode(Node node) {
		String parentName = "";
		try {
			String fileContent = Utils.readFileContent(node.getPath());
			int template = fileContent.indexOf("class");
			int index1 = fileContent.indexOf("extends", template) + "extends".length();
			if (index1 == "extends".length() - 1) {
				index1 = fileContent.indexOf("implements", template) + "implements".length();
			}
			int index2 = fileContent.indexOf("{", index1);
			parentName = fileContent.substring(index1, index2).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parentName;
	}

	/**
	 * Kiem tra mot project co chua cac project nho hon hay khong
	 * 
	 * @param projectNode
	 * @return true neu project chua cac project con<br/>
	 *         false neu la project con
	 */
	public static boolean containManyProject(Node projectNode) {
		final String BUILD_FOLDER_NAME = "build";
		for (Node child : projectNode.getChildren()) {
			if (child.getNodeName().equals(BUILD_FOLDER_NAME))
				return false;
		}
		return true;
	}

	/**
	 * Lay danh sach cac project con tu project dau vao
	 * 
	 * @param projectRootNode
	 */
	public static List<Node> getProjects(Node projectRootNode) {
		List<Node> projects = new ArrayList<Node>();
		if (Utils.containManyProject(projectRootNode))
			for (Node subProjectItem : projectRootNode.getChildren())
				projects.add(subProjectItem);
		else
			projects.add(projectRootNode);
		return projects;
	}

	/***/
	public static Node findNodeByPath(Node projectNode, Node node) {
		for (Node child : Search.searchNode(projectNode, new ClassCondition())) {
			if (child != null && node.getPath() != null && node.getPath().equals(child.getPath())) {
				return child;
			}
		}
		return null;
	}

	public static String getFileExtension(String path) {
		String[] pathSegments = path.split(File.separator);
		String fileName = pathSegments[pathSegments.length - 1];

		int i = fileName.lastIndexOf('.');
		if (i == -1) {
			return "";
		} else {
			return fileName.substring(i + 1);
		}
	}

	public static boolean fileEndsWith(String path, String ext) {
		return getFileExtension(path).toLowerCase().equals(ext.toLowerCase());
	}

	/**
	 * Lay cau truc DOM cua file xml
	 * @param path
	 * @return
	 */
	public static Document getDOM(String path) {
		try {
			File inputFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
