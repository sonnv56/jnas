package com.fit.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		
		// delete comments
		if (filePath.endsWith(".java"))
			return Utils.removeAllCommentsInJavaFile(fileData.toString());
		else if (filePath.endsWith(".xml") || filePath.endsWith(".xhtml") || filePath.endsWith(".jsp"))
			return Utils.removeAllCommentsInXmlFile(fileData.toString());
		else
			return fileData.toString();
	}

	/**
	 * Xoa tat ca moi comment trong file .xml
	 * 
	 * @param fileContent
	 *            noi dung file .xml
	 * @return
	 */
	private static String removeAllCommentsInXmlFile(String fileContent) {
		// do something here
		return fileContent;
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
	 * Xoa tat ca moi comment trong file Java
	 * 
	 * @param fileContent
	 *            noi dung file Java
	 * @return
	 */
	public static String removeAllCommentsInJavaFile(String fileContent) {
		final String REPLACEMENT = "";
		final String COMMENT_REGEX = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";

		fileContent = fileContent.replaceAll(COMMENT_REGEX, REPLACEMENT);
		return fileContent;
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
	public static List<Node> getSubProjectsList(Node projectRootNode) {
		List<Node> projects = new ArrayList<Node>();
		if (Utils.containManyProject(projectRootNode))
			for (Node subProjectItem : projectRootNode.getChildren())
				projects.add(subProjectItem);
		else
			projects.add(projectRootNode);
		return projects;
	}
}
