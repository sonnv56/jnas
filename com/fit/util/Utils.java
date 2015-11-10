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
<<<<<<< HEAD
<<<<<<< HEAD
	
	public static String getParentOfANode(Node node) {
		String parentName = "";
		try {
			String fileContent = Utils.readFileContent(node.getPath());
			int template = fileContent.indexOf("class");
			int index1 = fileContent.indexOf("extends", template) + "extends".length();
			if(index1 =="extends".length()-1){
				index1 = fileContent.indexOf("implements", template)+"implements".length();
			}
			int index2 = fileContent.indexOf("{", index1);
			parentName = fileContent.substring(index1, index2).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parentName;
	}

}
=======
}
>>>>>>> 98aeb8eeef2c57ec8d8e628c49c73764dc9b3698
=======
}
>>>>>>> 98aeb8eeef2c57ec8d8e628c49c73764dc9b3698
