package com.fit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.ClassCondition;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

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
	public static List<Node> getSubProjectsList(Node projectRootNode) {
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
			if (child !=null && node.getPath()!=null && node.getPath().equals(child.getPath())) {
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

	public static void main(String[] args) {
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);
		String test = "C:\\Users\\DucAnh\\Dropbox\\Workspace\\Download project\\DEMO J2EE 2\\dukes-forest\\dukes-forest\\dukes-shipment\\src\\java\\com\\forest\\entity\\Person.java";
		Node node = new ClassNode();
		node.setPath(test);
		System.out.println(findNodeByPath(projectRootNode, node));
	}
}
