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
import com.fit.process.jsf.object.MbNodeContainer;

/**
 * Lay danh sach ten cac managed bean trong project duoc dinh nghia trong cac
 * file java
 * 
 * @author DucAnh
 *
 */
public class CollectMbFromJavaFile {
	private List<MbNodeContainer> mbNodes_ = new ArrayList<MbNodeContainer>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		List<MbNodeContainer> output = new CollectMbFromJavaFile(projectRootNode).getMbNodes();
		for (MbNodeContainer n : output) {
			System.out.println(n.getClassNode().getPath());
		}
	}

	public CollectMbFromJavaFile(Node projectNode) {
		mbNodes_ = getManagedBeans(projectNode);
	}

	public List<MbNodeContainer> getMbNodes() {
		return mbNodes_;
	}

	private List<MbNodeContainer> getManagedBeans(Node projectNode) {
		List<MbNodeContainer> mbNodes = new ArrayList<MbNodeContainer>();

		List<Node> nodes = Search.searchNode(projectNode, new ManagedBeanCondition());
		for (Node node : nodes) {
			MbNodeContainer managedBean = new MbNodeContainer((ClassNode) node);
			String name = getManagedBeanName(node);
			managedBean.setName(name);
			mbNodes.add(managedBean);
		}
		return mbNodes;
	}

	/**
	 * Lay managed name cua managedBean file
	 * 
	 * @param managedNode
	 * @return
	 */
	private String getManagedBeanName(Node managedNode) {
		String managedName = "";
		ClassFileParser classParser = new ClassFileParser(managedNode);

		for (ASTNode m : classParser.getListAnnotation()) {
			String managedBeanAnnotation = m.toString();

			for (String mbAnnotation : MANAGED_BEAN_ANNOTATIONS)
				if ((m.toString()+"\r").contains(mbAnnotation)) {
					managedName = parseMbAnnotation(managedBeanAnnotation);

					/** managed bean file khong dinh nghia ten */
					if (managedName.length() == 0) {
						managedName = managedNode.getNodeName().replace(".java", "");
						
						char c[] = managedName.toCharArray();
						c[0] = Character.toLowerCase(c[0]);
						managedName = new String(c);
					}
					break;
				}
		}

		return managedName;
	}

	/**
	 * Lay ten managed bean
	 * 
	 * @param mbAnnotation
	 *            Annotation khai bao mot class la managed bean
	 * @return
	 */
	private String parseMbAnnotation(String mbAnnotation) {
		String managedName = "";
		for (String mbDeclaration : MANAGED_BEAN_NAME_DECLARATION_EXPRESSIONS) {
			Pattern p = Pattern.compile(mbDeclaration);
			Matcher m = p.matcher(mbAnnotation);

			if (m.find()) {
				managedName = m.group(1);
			}
			if (managedName.length() > 0)
				break;
		}
		return managedName;
	}

	private static final String[] MANAGED_BEAN_ANNOTATIONS = new String[] { "@ManagedBean(", "@Named(", "@Named\r" };
	private static final String[] MANAGED_BEAN_NAME_DECLARATION_EXPRESSIONS = new String[] { "name\\s*=\\s*\"(\\w+)\"",
			"value\\s*=\\s*\"(\\w+)\"", "\\(\\\"(\\w+)\\\"\\)" };
}
