package com.fit.ducanh.test;

import java.io.IOException;

import com.fit.util.Utils;

public class RemoveCommentTest {

	public static void main(String[] args) {
		try {
			String fileContent = Utils.readFileContent(ConfigurationOfAnh.REMOVE_COMMENT_TEST2);
			fileContent = Utils.removeAllComments(fileContent);
			System.out.println(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
