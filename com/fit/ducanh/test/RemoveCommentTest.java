package com.fit.ducanh.test;

import java.io.IOException;

import com.fit.util.Utils;

public class RemoveCommentTest {
	
	public static void main(String[] args) {
//		String filePath = "C:\\Users\\DucAnh\\Dropbox\\Workspace\\Eclipse project\\Jnas\\Remove Comment Test\\test1.txt";
		String filePath = "C:\\Users\\DucAnh\\Dropbox\\Workspace\\Eclipse project\\Jnas\\Remove Comment Test\\test2.java";
		try {
			String fileContent = Utils.readFileContent(filePath);
			fileContent = Utils.removeAllComments(fileContent);
			System.out.println(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
