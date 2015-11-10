package com.fit.cdi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.fit.cdi.condition.CDICustomQualifierCondition;
import com.fit.cdi.condition.CDIDefaultCondition;
import com.fit.cdi.condition.CDINamedCondition;
import com.fit.cdi.condition.CDIQualifierCondition;
import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.util.Utils;

/**
 * @author son Bo xu ly CDI trong project J2EE
 * */
public class CDIProcessor {
	private static final String INJECT_ANOTATION = "@Inject";
	private static final String DECLARATION_DEFAULT_CASE_PATTERN = "(@Inject )(?!@).*?(;)";
	private static final String USAGE_DEFAULT_CASE_PATTERN = "(@)(Inject)( )(?!@).*?(\\().*?(\\)).*";
	/** Node goc project */
	private ProjectNode projectNode;

	public static void main(String[] args) {
		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor processor = new CDIProcessor();
		processor.setProjectNode(projectNode);

		processor.process();

	}

	public void process() {
		List<Node> namedNodes = Search.searchNode(projectNode,
				new CDINamedCondition());
		List<Node> qualifiers = Search.searchNode(projectNode,
				new CDIQualifierCondition());
		List<Node> defaultNodes = Search.searchNode(projectNode,
				new CDIDefaultCondition());
		// Xu ly cach quy dinh 1
		applyCDIContextWithDefault(namedNodes, defaultNodes);
		// Xy ly cach quy dinh 2
		List<String> qulifiersAnotation = extractAnotationFromQualifiers(qualifiers);
		applyCDIContextWithQualifiers(namedNodes, qulifiersAnotation);

		for (Node node : namedNodes) {
			applyCDIContext(node);
		}
	}

	private void applyCDIContextWithQualifiers(List<Node> namedNodes,
			List<String> qulifiersAnotation) {
		for (String qualifier : qulifiersAnotation) {
			// Xac dinh candidate
			List<Node> qualifiers = Search.searchNode(projectNode,
					new CDICustomQualifierCondition(qualifier));
			for (Node node : qualifiers) {
				// Xac dinh injection point
				List<Node> whoUseParent = findQualifierCase(qualifier,
						namedNodes);
				for (Node node2 : whoUseParent) {
					createAConnectInProjectTree(node, node2);
				}
			}
		}
	}

	/**
	 * Xu ly truong hop su dung qualifier
	 * */
	private List<Node> findQualifierCase(String qualifier, List<Node> namedNodes) {
		List<Node> nodes = new ArrayList<Node>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(
					node.getPath());
			boolean isAUser = false;
			// Kiem tra cac field
			for (FieldDeclaration field : classFileParser
					.getListFieldDeclaration()) {
				String cdiAnotation = INJECT_ANOTATION;
				if (!qualifier.isEmpty())
					cdiAnotation += (" " + "@" + qualifier);

				if (field.toString().indexOf(cdiAnotation) != -1) {
					System.out.println("Declaration");
					isAUser = true;
				}
			}
			// Kiem tra cac method
			for (MethodDeclaration method : classFileParser
					.getListMethodDeclaration()) {
				if (method.toString().indexOf(INJECT_ANOTATION) != -1) {
					List<SingleVariableDeclaration> parameters = method
							.parameters();
					for (SingleVariableDeclaration p : parameters) {
						if (p.toString().equals("@" + qualifier)) {
							System.out.println("Use");
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
	 * Lay anotation tu qualifier
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
	 * */
	private void applyCDIContextWithDefault(List<Node> namedNodes,
			List<Node> defaultNodes) {
		if (defaultNodes.size() > 0) {
			for (Node node : defaultNodes) {
				String parent = Utils.getParentOfANode(node);
				List<Node> whoUseParent = findDefaultCase("", parent,
						namedNodes);
				for (Node w : whoUseParent) {
					createAConnectInProjectTree(w, node);
				}
			}
		}
	}

	private void createAConnectInProjectTree(Node w, Node node) {
		System.out.println(w.getPath());
		System.out.println(node.getPath());
	}

	private List<Node> findDefaultCase(String anotation, String parent,
			List<Node> namedNodes) {

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
						System.out.println("Declaration");
						isAUser = true;
					}
				}
			}
			// Kiem tra cac method
			pa = Pattern.compile(USAGE_DEFAULT_CASE_PATTERN);
			for (MethodDeclaration method : classFileParser.getListMethodDeclaration()) {
				m = pa.matcher(method.toString().trim().replace("\n", "").replace("\r", ""));
				if (m.matches()) {
					List<SingleVariableDeclaration> parameters = method.parameters();
					for (SingleVariableDeclaration p : parameters) {
						if (p.getType().toString().equals(parent)) {
							System.out.println("Use");
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

	private void applyCDIContext(Node node) {

	}

	public void setProjectNode(ProjectNode projectNode) {
		this.projectNode = projectNode;
	}
}
