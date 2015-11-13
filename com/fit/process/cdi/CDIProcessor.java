package com.fit.process.cdi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.condition.CDICustomQualifierCondition;
import com.fit.process.cdi.condition.CDIDefaultCondition;
import com.fit.process.cdi.condition.CDINamedCondition;
import com.fit.process.cdi.condition.CDIQualifierCondition;
import com.fit.util.Utils;

/**
 * @author son Bo xu ly CDI trong project J2EE
 * */
public class CDIProcessor {
	/** RX cho injection point khai bao mac dinh */
	private static final String DECLARATION_DEFAULT_CASE_PATTERN = "(@Inject )(?!@).*?(;)";
	/** RX cho injection point khai bao mac dinh */
	private static final String USAGE_DEFAULT_CASE_PATTERN = "(@(Inject) ).*?(\\(.*)(?!@).*?(\\)).*";
	/** RX cho injection point khai bao mac dinh */
	private static final String PROCEDUCES_DEFAULT_CASE_PATTERN = "(@)(Produces)( ).*?(\\().*?(\\)).*";
	/** Node goc project */
	private ProjectNode projectNode;

	public static void main(String[] args) {
//		String projectRootPath = "C://Users//son//Google Drive//Share//CIASample";
		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor processor = new CDIProcessor();
		processor.setProjectNode(projectNode);

		processor.process();

	}

	/**
	 * Phuong thuc xu ly chinh
	 * */
	public void process() {
		List<Node> namedNodes = Search.searchNode(projectNode, new CDINamedCondition());
		if(namedNodes.size() > 0){
			List<Node> qualifiers = Search.searchNode(projectNode, new CDIQualifierCondition());
			List<Node> defaultNodes = Search.searchNode(projectNode, new CDIDefaultCondition());
			
			if(defaultNodes.size() > 0){
				// Xu ly cach quy dinh 1 @Inject @Alternative @Default
				applyCDIContextWithDefault(namedNodes, defaultNodes);
			}
			List<String> qulifiersAnotation = extractAnotationFromQualifiers(qualifiers);
			if(qualifiers.size() > 0){
				// Xy ly cach quy dinh 2 @Qualifier
				applyCDIContextWithQualifiers(namedNodes, qulifiersAnotation);
				// Xy ly cach quy dinh 3
				applyCDIContextWithProduces(namedNodes, qulifiersAnotation);
			}
		}
	}
	/**
	 * Xu li cac truong hop dung @Produces
	 * */
	private void applyCDIContextWithProduces(List<Node> namedNodes, List<String> qulifiersAnotation) {
		for (String qualifier : qulifiersAnotation) {
			// Xac dinh candidate
			List<Node> qualifiers = Search.searchNode(projectNode,new CDICustomQualifierCondition(qualifier));
			for (Node node : qualifiers) {
				// Xac dinh injection point
				List<Node> injectionPoints = findQualifierCase(qualifier,namedNodes);
				for (Node node2 : injectionPoints){
					createAConnectInProjectTree(node, node2);
				}
			}
		}
	}

	/**
	 * Xu ly truong hop su dung qualifiers
	 * @param namedNodes
	 * @param qulifiersAnotation
	 * */
	private void applyCDIContextWithQualifiers(List<Node> namedNodes, List<String> qulifiersAnotation) {
		for (String qualifier : qulifiersAnotation) {
			// Xac dinh candidate
			List<Node> qualifiers = Search.searchNode(projectNode,new CDICustomQualifierCondition(qualifier));
			for (Node node : qualifiers) {
				// Xac dinh injection point
				List<Node> injectionPoints = findQualifierCase(qualifier,namedNodes);
				for (Node node2 : injectionPoints){
					createAConnectInProjectTree(node, node2);
				}
			}
		}
	}

	/**
	 * Xu ly truong hop su dung qualifier
	 * @param qualifier
	 * @param namedNodes
	 * */
	private List<Node> findQualifierCase(String qualifier, List<Node> namedNodes) {
		String qualifierPattern1 = DECLARATION_DEFAULT_CASE_PATTERN.replace("(?!@)", "(@" + qualifier + " )");
		String qualifierPattern2 = USAGE_DEFAULT_CASE_PATTERN;
		List<Node> nodes = new ArrayList<Node>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			boolean isAUser = false;
			// Kiem tra cac field
			Pattern pa = Pattern.compile(qualifierPattern1);
			Matcher m = null;
			for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
				m = pa.matcher(field.toString().trim().replace("\n", "").replace("\r", ""));
				if (m.matches()) {
					System.out.println("Qualifier Declaration");
					isAUser = true;
				}
			}
			// Kiem tra cac method
			pa = Pattern.compile(qualifierPattern2);
			for (MethodDeclaration method : classFileParser.getListMethodDeclaration()) {
				m = pa.matcher(method.toString().trim().replace("\n", "").replace("\r", ""));
				if (m.matches()) {
					List<SingleVariableDeclaration> parameters = method.parameters();
					for (SingleVariableDeclaration p : parameters) {
						if (p.toString().indexOf("@"+qualifier)!=-1) {
							isAUser = true;
							System.out.println("Qualifier Use");
						}
					}
					
				}
			}

			if (isAUser) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Lay anotation tu qualifier
	 * @param qualifiers
	 * */
	private List<String> extractAnotationFromQualifiers(List<Node> qualifiers) {
		List<String> result = new ArrayList<String>();
		for (Node node : qualifiers) {
			String fileName = new File(node.getPath()).getName();
			int start = fileName.lastIndexOf(".java");
			String name = fileName.substring(0, start);
			result.add(name);
		}
		return result;
	}

	/**
	 * Ap dung truong hop mac dinh
	 * @param namedNodes
	 * @param defaultNodes
	 * */
	private void applyCDIContextWithDefault(List<Node> namedNodes, List<Node> defaultNodes) {
		if (defaultNodes.size() > 0) {
			for (Node node : defaultNodes) {
				String parent = Utils.getParentOfANode(node);
				List<Node> whoUseParent = findDefaultCase(parent, namedNodes);
				for (Node w : whoUseParent) {
					createAConnectInProjectTree(w, node);
				}
			}
		}
	}

	/**
	 * Tao 1 connection giua cac node
	 * @param w
	 * @param node
	 * */
	private void createAConnectInProjectTree(Node w, Node node) {
		
	}

	/**
	 * Tim injection point su dung cach mac dinh
	 * @param anotation
	 * @param parent
	 * @param namedNodes
	 * */
	private List<Node> findDefaultCase(String parent, List<Node> namedNodes) {
		List<Node> nodes = new ArrayList<Node>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			boolean isAUser = false;
			// Kiem tra cac field
			Pattern pa = Pattern.compile(DECLARATION_DEFAULT_CASE_PATTERN);
			Matcher m = null;
			for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
				m = pa.matcher(field.toString().trim());
				if (m.matches()) {// Kiem tra su xuat hien cua @inject
					if (field.getType().toString().equals(parent)) {
						System.out.println("Default Declaration");
						isAUser = true;
					}
				}
			}
			// Kiem tra cac method
			pa = Pattern.compile(USAGE_DEFAULT_CASE_PATTERN);
			for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
				m = pa.matcher(method.toString().trim().replace("\n", "") .replace("\r", ""));
				if (m.matches()) {
					List<SingleVariableDeclaration> parameters = method .parameters();
					for (SingleVariableDeclaration p : parameters) {
						if (p.getType().toString().equals(parent) && p.toString().indexOf("@")==-1) {
							System.out.println("Default Use");
							isAUser = true;
						}
					}
				}
			}
			if (isAUser) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Set projectNode value
	 * @param projectNode
	 * */
	public void setProjectNode(ProjectNode projectNode) {
		this.projectNode = projectNode;
	}
}
