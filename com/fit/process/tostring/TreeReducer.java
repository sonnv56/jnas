package com.fit.process.tostring;

import java.util.ArrayList;
import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.ClassNode;
import com.fit.object.ComponentNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

public class TreeReducer implements ITreeReducer {
	private Node root_;
	/** fsdfs */
	private List<ComponentNode> packageNodes = new ArrayList<>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		TreeReducer reducer = new TreeReducer(projectRootNode);
		// display tree of project
		TreeStrategy treeDisplayer = new TreeStrategy(projectRootNode);
		 System.out.println(treeDisplayer.getString());

	}

	public TreeReducer(Node root) {
		root_ = root;
		reduce();

		// removeUnnecessaryNode(root);
	}

	@Override
	public void reduce() {
		traverse(root_);
	}

	private boolean isStartOfPackage(Node n) {
		final String[] PACKAGE_SYMBOL = new String[] { "src", "generated-sources", "generated" };
		for (String packageSymbol : PACKAGE_SYMBOL)
			if (n.getPath().endsWith(packageSymbol))
				return true;
		return false;
	}

	private void traverse(Node n) {
		for (Node child : n.getChildren()) {
			if (child instanceof ComponentNode && isStartOfPackage(child)) {
				Node src = child;
				List<Node> packageNodes = new ArrayList<Node>();
				
				traverseSrc(child, src, packageNodes);
				
				src.getChildren().clear();
				src.getChildren().addAll(packageNodes);
			} else {
				traverse(child);
			}
		}
	}

	private String getRelativePath(Node n, Node src) {
		String rootPath = src.getPath();

		String path = n.getPath();
		path = path.replace("\\" + n.getNodeName(), "");

		String relativePath = path.replace(rootPath + "\\", "");
		relativePath = relativePath.replace("\\", ".");
		return relativePath;
	}

	private void traverseSrc(Node n, Node src, List<Node> packageNodes) {
		for (Node child : n.getChildren()) {

			if (child instanceof ClassNode) {
				String relativePath = getRelativePath(child, src);
				Node packageNode = findPackageNode(packageNodes, relativePath);
				if (packageNode == null) {
					packageNode = new ComponentNode();
					packageNode.setParent(src);
					packageNode.setPath(relativePath);
					packageNodes.add(packageNode);
				}

				packageNode.addChild(child);
				child.setParent(packageNode);

				
			} else
				traverseSrc(child, src, packageNodes);
		}
	}

	private Node findPackageNode(List<Node> packageNodes, String path) {
		for (Node packageNode : packageNodes) {
			if (packageNode.getPath().equals(path))
				return packageNode;
		}
		return null;
	}

}
