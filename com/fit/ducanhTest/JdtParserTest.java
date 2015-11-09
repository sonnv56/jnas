package com.fit.ducanhTest;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.jdtParser.ClassFileParser;

public class JdtParserTest {
	public static void main(String[] args) {
		String classPath = "C://Users//son//Desktop//Workspace//CIA//dukes-forest//dukes-store//src//java//com//forest//web//CustomerController.java";
		ClassFileParser classFileParser = new ClassFileParser(classPath);

		// display all annotation
//		for (ASTNode annotation : classFileParser.getListAnnotation()) {
//			System.out.println(annotation.toString());
//		}

		// display all field
		for (ASTNode annotation : classFileParser.getListFieldDeclaration()) {
			System.out.println(annotation.toString());
		}
//		// display all method
//		for (ASTNode annotation : classFileParser.getListMethodDeclaration()) {
//			System.out.println(annotation.toString());
//		}
	}
}
