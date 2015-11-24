package com.fit.process.ws;

import java.util.Enumeration;
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
	private Hashtable<Node, WebServiceClientInfo> wscriMap;

	private static class Helper {
		private static String getValueOf(NormalAnnotation na, String name) {
			for (Object p: na.values()) {
				MemberValuePair pair = (MemberValuePair) p;
				if (pair.getName().getFullyQualifiedName().equals(name)) {
					String valueWithQuote = pair.getValue().toString();
					return valueWithQuote.substring(1, valueWithQuote.length() - 1);
				}
			}
			
			return "";
		}
		
		private static String getNodeClassName(Node n) {
			String name = n.getNodeName();
			return name.substring(0, name.lastIndexOf('.'));
		}
	}
	
	private class WebServiceInfo {
		String name;
		String targetNamespace;
		
		public WebServiceInfo(String name, String targetNamespace) {
			this.name = name;
			this.targetNamespace = targetNamespace;
		}
		
		public boolean match(WebServiceClientInfo clientInfo) {
			return this.name.equals(clientInfo.getServiceName()) && this.targetNamespace.equals(clientInfo.targetNamespace);
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
		
		public String getServiceName() {
			return name.substring(0, name.lastIndexOf("Service"));
		}

		@Override
		public String toString() {
			return String.format("%s(%s,%s,%s)", this.getClass().getSimpleName(), name, targetNamespace, wsdlLocation);
		}
	}
	
	public WebServiceProcessor(ProjectNode node) {
		root = node;
		wsiMap = new Hashtable<>();
		wsciMap = new Hashtable<>();
		wscriMap = new Hashtable<>();
	}

	public static void main(String[] args) {
		String projectRootPath = "/home/rmrf/builds/dukes-forest";
		ProjectNode node = ProjectLoader.load(projectRootPath);
		
		WebServiceProcessor wsp = new WebServiceProcessor(node);
		wsp.process();
	}
	
	private void mapClientNode() {
		Enumeration<Node> nKeys = wscriMap.keys();
		while (nKeys.hasMoreElements()) {
			Node nInfo = nKeys.nextElement();
			WebServiceClientInfo ref = wscriMap.get(nInfo);
			
			Enumeration<Node> mKeys = wsciMap.keys();
			while (mKeys.hasMoreElements()) {
				Node mInfo = mKeys.nextElement();
				WebServiceClientInfo client = wsciMap.get(mInfo);

				if (ref.wsdlLocation.equals(client.wsdlLocation)) {
					ref.name = client.name;
					ref.targetNamespace = client.targetNamespace;
				}
			}
			
		}
		
	}
	
	
	public void process() {
		List<Node> wsClients = getWebServiceClients();
		
		mapClientNode();
		
		List<Node> ws = getWebServices();
		
		Enumeration<Node> serviceNodes = wsiMap.keys();
		
		while (serviceNodes.hasMoreElements()) {
			Node snode = serviceNodes.nextElement();
			WebServiceInfo wsi = wsiMap.get(snode);
			
			Enumeration<Node> clientNodes = wscriMap.keys();
			while (clientNodes.hasMoreElements()) {
				Node cnode = clientNodes.nextElement();
				WebServiceClientInfo wsci = wscriMap.get(cnode);
				
				if (wsi.match(wsci)) {
					snode.getCallers().add(cnode);
					cnode.getCallees().add(snode);
				}
				
			}
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
							if (!n.getPath().contains("build/generated")) {
								String target = Helper.getValueOf(na, "targetNamespace");
								String name = Helper.getValueOf(na, "name");
								
								if (name.isEmpty()) {
									name = Helper.getNodeClassName(n);
								}
								
								wsiMap.put(n, new WebServiceInfo(name, target));
								return true;
							}
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
							String wsdlLocation = Helper.getValueOf(na, "wsdlLocation");
							String name = Helper.getValueOf(na, "name");
							String target = Helper.getValueOf(na, "targetNamespace");
							
							wscriMap.put(n, new WebServiceClientInfo(name, target, wsdlLocation));
							return true;
						} else if (na.getTypeName().getFullyQualifiedName().equals("WebServiceClient")) {
							if (n.getPath().contains("build/generated/")) {
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
