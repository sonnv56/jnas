package com.fit.process.ws;

import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Condition;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.util.Utils;

public class WebServiceProcessor {
	private ProjectNode root;
	
	private static class Helper {
		private static String getValueOf(NormalAnnotation na, String name) {
			for (Object p: na.values()) {
				MemberValuePair pair = (MemberValuePair) p;
				if (pair.getName().getFullyQualifiedName().equals(name)) {
					return pair.getValue().toString();
				}
			}
			
			return "";
		}
	}
	
	public WebServiceProcessor(ProjectNode node) {
		root = node;
	}

	public static void main(String[] args) {
		String projectRootPath = "/home/rmrf/builds/dukes-forest";
		ProjectNode node = ProjectLoader.load(projectRootPath);
		
		WebServiceProcessor wsp = new WebServiceProcessor(node);
		wsp.process();
	}
	
	public void process() {
		List<Node> wsClients = getWebServiceClients();
		List<Node> ws = getWebServices();
		List<Node> wsdlNodes = Search.searchNode(root, new Condition() {
			@Override
			public boolean isStatisfiabe(Node n) {
				return Utils.fileEndsWith(n.getPath(), "wsdl");
			}
		});
		
		for (Node n: wsdlNodes) {
			System.out.println(n.getPath());
		}
	}

	private List<Node> getWebServices() {
		return Search.searchNode(root, new Condition() {
			@Override
			public boolean isStatisfiabe(Node n) {
				if (!(n instanceof ClassNode))
					return false;
				
				ClassFileParser classFileParser = new ClassFileParser(n.getPath());
				List<ASTNode> annotations = classFileParser.getListAnnotation();
				for (ASTNode a: annotations) {
					if (a instanceof NormalAnnotation) {
						NormalAnnotation na = (NormalAnnotation) a;
						
						if (na.getTypeName().getFullyQualifiedName().equals("WebService")) {
							System.out.println(na);
							String target = Helper.getValueOf(na, "targetNamespace");
							System.out.println(target);
							
							return true;
						}
					}

				}
				return false;
			}
		});
	}

	private List<Node> getWebServiceClients() {
		return Search.searchNode(root, new Condition() {
			@Override
			public boolean isStatisfiabe(Node n) {
				if (!(n instanceof ClassNode))
					return false;
				
				ClassFileParser classFileParser = new ClassFileParser(n.getPath());
				List<ASTNode> annotations = classFileParser.getListAnnotation();
				for (ASTNode a: annotations) {
					if (a instanceof NormalAnnotation) {
						NormalAnnotation na = (NormalAnnotation) a;
						if (na.getTypeName().getFullyQualifiedName().equals("WebServiceRef")) {
							String value = Helper.getValueOf(na, "wsdlLocation");
							System.out.println(value);
							
							return true;							
						}
					}

				}
				return false;
			}
		});
	}
}
