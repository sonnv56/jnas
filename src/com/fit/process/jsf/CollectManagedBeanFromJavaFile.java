package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.ManagedBeanCondition;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.object.ManagedBeanNodeContainer;

/**
 * Lay danh sach ten cac managed bean trong project duoc dinh nghia trong cac
 * file java
 * 
 * @author DucAnh
 *
 */
public class CollectManagedBeanFromJavaFile {
	private List<ManagedBeanNodeContainer> listManagedBeanNodes = new ArrayList<ManagedBeanNodeContainer>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);

		List<ManagedBeanNodeContainer> output = new CollectManagedBeanFromJavaFile(projectRootNode)
				.getListManagedBeanNodes();
		for (ManagedBeanNodeContainer n : output) {
			System.out.println(n.getClassNode().getPath());
		}
	}

	public CollectManagedBeanFromJavaFile(Node projectNode) {
		listManagedBeanNodes = getManagedBeansList(projectNode);
	}

	public List<ManagedBeanNodeContainer> getListManagedBeanNodes() {
		return listManagedBeanNodes;
	}

	private List<ManagedBeanNodeContainer> getManagedBeansList(Node projectNode) {
		List<ManagedBeanNodeContainer> listManagedBeanNodes = new ArrayList<ManagedBeanNodeContainer>();

		List<Node> listNode = Search.searchNode(projectNode, new ManagedBeanCondition());
		for (Node n : listNode) {
			ManagedBeanNodeContainer managedBean = new ManagedBeanNodeContainer((ClassNode) n);
			String name = getManagedName(n);
			managedBean.setName(name);
			listManagedBeanNodes.add(managedBean);
		}
		return listManagedBeanNodes;
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
					managedName = managedNode.getNodeName().replace(".java", "");
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

		if (m.find()) {
			managedName = m.group(1);
		}
		return managedName;
	}

	private static final String MANAGED_BEAN_ANNOTATION = "@ManagedBean";
	private static final String MANAGED_BEAN_NAME_DECLARATION_EXPRESSION = "name\\s*=\\s*\"(\\w+)\"";
}
