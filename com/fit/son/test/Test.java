package com.fit.son.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.jdtParser.ClassFileParser;

public class Test {
	public static void main(String[] args) {
		String classPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
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

		// display all interfaces
		for (ASTNode _interface : classFileParser.getInterfaces()) {
			System.out.println(_interface.toString());
		}
		// display extend class
		System.out.println(classFileParser.getExtendClass().toString());
	}
}
