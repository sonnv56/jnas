package com.fit.jdtParser;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import com.fit.object.Node;
import com.fit.util.Utils;

public class ClassFileParser implements IJdtParser {

	private ArrayList<MethodDeclaration> listMethodDeclaration = new ArrayList<MethodDeclaration>();
	private ArrayList<FieldDeclaration> listFieldDeclaration = new ArrayList<FieldDeclaration>();
	private ArrayList<ASTNode> listAnnotation = new ArrayList<ASTNode>();

	public ClassFileParser(Node classNode) {
		try {
			String fileContent = Utils.readFileContent(classNode.getPath());
			parse(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClassFileParser(String classNodePath) {
		try {
			String fileContent = Utils.readFileContent(classNodePath);
			parse(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse(String fileContent) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(fileContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {
			// visit a method
			public boolean visit(MethodDeclaration node) {
				listMethodDeclaration.add(node);
				return true;
			}

			// visit a field
			public boolean visit(FieldDeclaration node) {
				listFieldDeclaration.add(node);
				return true;
			}

			public boolean visit(AnnotationTypeMemberDeclaration annotation) {
				listAnnotation.add(annotation);
				return true;
			}

			public boolean visit(AnnotationTypeDeclaration annotation) {
				listAnnotation.add(annotation);
				return true;
			}

			public boolean visit(MarkerAnnotation annotation) {
				listAnnotation.add(annotation);
				return true;
			}

			public boolean visit(NormalAnnotation annotation) {
				listAnnotation.add(annotation);
				return true;
			}

			public boolean visit(SingleMemberAnnotation annotation) {
				listAnnotation.add(annotation);
				return true;
			}
		});
	}

	public ArrayList<ASTNode> getListAnnotation() {
		return listAnnotation;
	}

	public ArrayList<FieldDeclaration> getListFieldDeclaration() {
		return listFieldDeclaration;
	}

	public ArrayList<MethodDeclaration> getListMethodDeclaration() {
		return listMethodDeclaration;
	}
}
