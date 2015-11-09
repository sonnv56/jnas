package com.fit.process.jsf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.ducanh.test.displayTree.TreeStrategy;
import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.ManagedBeanCondition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.JspCondition;
import com.fit.process.jsf.condition.XHtmlCondition;
import com.fit.util.Utils;

/**
 * Tao lien ket tu jsp/xhtml toi class managed bean (callees) va nguoc lai
 * (callers)
 * 
 * @author DucAnh do later: duyet cay project can ap dung pham vi duyet
 */
public class JsfConnectionGeneration {
	public static void main(String[] args) {
		// String projectRootPath =
		// "C:\\Users\\DucAnh\\Dropbox\\Project\\J2EE\\DEMO J2EE
		// 2\\dukes-forest\\dukes-forest";
		String projectRootPath = "G:\\Dropbox\\NetBeansProjects\\dukes-forest";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);

		JsfConnectionGeneration gen = new JsfConnectionGeneration(projectNode);

		TreeStrategy displayer = new TreeStrategy(projectNode);
		System.out.println(displayer.getString());
	}

	public JsfConnectionGeneration(ProjectNode projectNode) {
		Map<Node, String> managedBeanNodesMap = getManagedBeanNodesMap(projectNode);
		List<Node> listXHtmlNodes = Search.searchNode(projectNode, new XHtmlCondition());
		List<Node> listJspNodes = Search.searchNode(projectNode, new JspCondition());

		listXHtmlNodes.addAll(listJspNodes);
		createConnectionToManagedFile(listXHtmlNodes, managedBeanNodesMap);
	}

	/**
	 * 
	 * @param projectNode
	 * @return
	 */
	private Map<Node, String> getManagedBeanNodesMap(ProjectNode projectNode) {
		List<Node> listManagedBeanNodes = Search.searchNode(projectNode, new ManagedBeanCondition());
		Map<Node, String> managedBeanNodesMap = new HashMap<>();
		for (Node managedBean : listManagedBeanNodes) {
			managedBeanNodesMap.put(managedBean, getManagedName(managedBean));
		}
		return managedBeanNodesMap;
	}

	/**
	 * Tao lien ket giua xhtml/jsp file va managed bean file
	 * 
	 * @param webNode
	 *            danh sach xhtml/jsp node
	 * @param listManagedBeanNodes
	 *            danh sach managed bean node
	 */
	private void createConnectionToManagedFile(List<Node> listWebFileNode, Map<Node, String> managedBeanNodesMap) {
		// Duyet tat ca moi file xhtml trong project
		for (Node webNode : listWebFileNode) {
			try {
				String fileContent = Utils.readFileContent(webNode.getPath());

				if (fileContent.contains(INTERMEDIATE_EXPRESSION_LANGUAGE_START))
					// Duyet tat ca moi managedBean file trong project
					for (Node managedNode : managedBeanNodesMap.keySet()) {

						// Lay managedBean name
						String managedName = managedBeanNodesMap.get(managedNode);

						if (fileContent.contains(setIndentifierForManagedBean(managedName))) {
							webNode.getCallees().add(managedNode);
							managedNode.getCallers().add(webNode);
						}
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param managedName
	 *            Ten mot managed bean
	 * @return dinh danh de xac dinh managed bean do trong xhtml/jsp file
	 */
	private String setIndentifierForManagedBean(String managedName) {
		return INTERMEDIATE_EXPRESSION_LANGUAGE_START + managedName + ".";
	}

	/**
	 * Lay managed name cua managedBean file
	 * 
	 * @param managedNode
	 * @return
	 */
	private String getManagedName(Node managedNode) {
		String managedName = "";
		ClassFileParser classParser = new ClassFileParser(managedNode);

		for (ASTNode m : classParser.getListAnnotation()) {
			String managedBeanAnnotation = m.toString();

			if (m.toString().contains(MANAGED_BEAN_ANNOTATION)) {
				managedName = parseManageBeanAnnotation(managedBeanAnnotation);
				// managed bean file khong duoc dinh nghia ten
				if (managedName.length() == 0) {
					managedName = managedNode.getNodeName();
				}
				break;
			}
		}

		return managedName;
	}

	/**
	 * Lay ten managed bean
	 * 
	 * @param managedBeanAnnotation
	 *            Annotation khai bao mot class la managed bean
	 * @return
	 */
	private String parseManageBeanAnnotation(String managedBeanAnnotation) {
		String managedName = "";

		Pattern p = Pattern.compile(MANAGED_BEAN_NAME_DECLARATION_EXPRESSION);
		Matcher m = p.matcher(managedBeanAnnotation);
		StringBuffer sb = new StringBuffer();

		if (m.find()) {
			managedName = m.group(1);
		}
		return managedName;
	}

	private static final String MANAGED_BEAN_ANNOTATION = "@ManagedBean";
	private static final String MANAGED_BEAN_NAME_DECLARATION_EXPRESSION = "name\\s*=\\s*\"(\\w+)\"";
	private static final String INTERMEDIATE_EXPRESSION_LANGUAGE_START = "#{";
}
