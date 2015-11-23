package com.fit.jdtParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.fit.object.Node;
import com.fit.util.Utils;

/**
 * Phan tich file .java lay thong tin can thiet
 * 
 * @author DucAnh
 *
 */
public class ClassFileParser implements IJdtParser {

	private List<MethodDeclaration> listMethodDeclaration = new ArrayList<MethodDeclaration>();
	private List<ImportDeclaration> listImportDeclaration = new ArrayList<ImportDeclaration>();
	private List<FieldDeclaration> listFieldDeclaration = new ArrayList<FieldDeclaration>();
	private List<ASTNode> listAnnotation = new ArrayList<ASTNode>();
	private Type extendClass;
	private List<TypeDeclaration> interfaces = new ArrayList<TypeDeclaration>();

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
			public boolean visit(ImportDeclaration node) {
				listImportDeclaration.add(node);
				return true;
			}

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

			public boolean visit(TypeDeclaration classDeclaration) {
				interfaces = classDeclaration.superInterfaceTypes();
				extendClass = classDeclaration.getSuperclassType();
				return true;
			}
		});
	}

	public List<ASTNode> getListAnnotation() {
		return listAnnotation;
	}

	public List<FieldDeclaration> getListFieldDeclaration() {
		return listFieldDeclaration;
	}

	public List<MethodDeclaration> getListMethodDeclaration() {
		return listMethodDeclaration;
	}

	public List<TypeDeclaration> getInterfaces() {
		return interfaces;
	}

	public Type getExtendClass() {
		return extendClass;
	}

	public List<ImportDeclaration> getListImportDeclaration() {
		return listImportDeclaration;
	}
}
