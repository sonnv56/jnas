package com.fit.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
	 * Xoa tat ca moi comment trong file Java
	 * 
	 * @param fileContent
	 *            noi dung file Java
	 * @return
	 */
	public static String removeAllComments(String fileContent) {
		final String REPLACEMENT = "";
		final String COMMENT_REGEX = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";

		fileContent = fileContent.replaceAll(COMMENT_REGEX, REPLACEMENT);
		return fileContent;
	}
}
