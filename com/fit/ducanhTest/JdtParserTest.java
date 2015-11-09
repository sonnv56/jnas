package com.fit.ducanhTest;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.jdtParser.ClassFileParser;

public class JdtParserTest {
	public static void main(String[] args) {
		String classPath = "C:\\Users\\DucAnh\\Dropbox\\Project\\J2EE\\DEMO J2EE 2\\dukes-forest\\dukes-forest\\dukes-payment\\src\\java\\com\\forest\\payment\\services\\Payment.java";
		ClassFileParser classFileParser = new ClassFileParser(classPath);

		// display all annotation
		for (ASTNode annotation : classFileParser.getListAnnotation()) {
			System.out.println(annotation.toString());
		}

		// display all field
		for (ASTNode annotation : classFileParser.getListFieldDeclaration()) {
			System.out.println(annotation.toString());
		}
		// display all method
		for (ASTNode annotation : classFileParser.getListMethodDeclaration()) {
			System.out.println(annotation.toString());
		}
	}
}
