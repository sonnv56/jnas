package com.fit.ducanhTest;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.fit.jdtParser.ClassFileParser;


public class JdtParserTest {
	public static void main(String[] args) {
		String classPath = "C://Users//Chicky//Desktop//Workspace//Project//CIA//dukes-forest//dukes-store//src//java//com//forest//handlers//DeliveryHandler.java";
		ClassFileParser classFileParser = new ClassFileParser(classPath);

		// display all field
//		for (FieldDeclaration annotation : classFileParser.getListFieldDeclaration()) {
//			System.out.println(annotation.getType());
//			System.out.println(annotation);
//		}
		// display all method
		for (MethodDeclaration annotation : classFileParser.getListMethodDeclaration()) {
			List<SingleVariableDeclaration> parameters= annotation.parameters();
			for (SingleVariableDeclaration singleVariableDeclaration : parameters) {
				System.out.println(singleVariableDeclaration.getType());
			}
		}
	}
}
