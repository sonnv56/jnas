package com.fit.ducanh.test;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.jdtParser.ClassFileParser;

/**
 * Test JdtParser
 * 
 * @author DucAnh
 *
 */
public class JdtParserTest {
	public static void main(String[] args) {
		String classPath = "C:\\Users\\DucAnh\\Dropbox\\Workspace\\Download project\\DEMO J2EE 2\\dukes-forest\\dukes-forest\\dukes-payment\\src\\java\\com\\forest\\payment\\services\\Payment.java";
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
		if (classFileParser.getExtendClass() != null)
			System.out.println(classFileParser.getExtendClass().toString());
	}
}
