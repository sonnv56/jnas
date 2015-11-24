package com.fit.process.ws;

import java.util.Hashtable;
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
	private Hashtable<Node, WebServiceInfo> wsiMap;
	private Hashtable<Node, WebServiceClientInfo> wsciMap;

	
	
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
	
	private class WebServiceInfo {
		String name;
		String targetNamespace;
		
		public WebServiceInfo(String name, String targetNamespace) {
			this.name = name;
			this.targetNamespace = targetNamespace;
		}
	}
	
	private class WebServiceClientInfo {
		String name;
		String targetNamespace;
		String wsdlLocation;
		
		public WebServiceClientInfo(String name, String targetNamespace, String wsdlLocation) {
			this.name = name;
			this.targetNamespace = targetNamespace;
			this.wsdlLocation = wsdlLocation;
		}
		

	}
	
	public WebServiceProcessor(ProjectNode node) {
		root = node;
		wsiMap = new Hashtable<>();
		wsciMap = new Hashtable<>();
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
							if (n.getPath().contains("build/generated")) {
								if (n.getPath().contains("build/generated/")) {
									System.out.println(na);
									String target = Helper.getValueOf(na, "targetNamespace");
									String name = Helper.getValueOf(na, "name");
									
									wsiMap.put(n, new WebServiceInfo(name, target));
								}
							} else
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
							System.out.println(na);							
							return true;
						} else if (na.getTypeName().getFullyQualifiedName().equals("WebServiceClient")) {
							if (n.getPath().contains("build/generated/")) {
								System.out.println(na);
								String wsdlLocation = Helper.getValueOf(na, "wsdlLocation");
								String name = Helper.getValueOf(na, "name");
								String target = Helper.getValueOf(na, "targetNamespace");
								
								wsciMap.put(n, new WebServiceClientInfo(name, target, wsdlLocation));
							}
						}
					}

				}
				return false;
			}
		});
	}
}
