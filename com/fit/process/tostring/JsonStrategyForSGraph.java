package com.fit.process.tostring;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

/**
 * Xuat file json de hien thi cay cau truc trong
 * http://mbostock.github.io/d3/talk/20111018/tree.html
 * 
 * @author DucAnh
 *
 */
public class JsonStrategyForSGraph implements ITreeReducer, IToString {
	private String output_ = "";
	private Node node_;

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.MULTIPLE_CONFIG_JSF);

		// display tree of project
		IToString treeDisplayer = new JsonStrategyForSGraph(projectRootNode);
		System.out.println(treeDisplayer.getString());

	}

	/**
	 * 
	 * @param node
	 *            root cay cau truc vat li cua project
	 */
	public JsonStrategyForSGraph(Node node) {
		this.node_ = node;
		reduce();
		convertToString();
	}

	@Override
	public void convertToString() {
		output_ = traverse(node_);
	}

	private String toStr(String str) {
		return "\"" + str + "\"";
	}

	private String traverse(Node n) {
		String output = "";
		//
		String jsonKey = toStr("name") + ":" + toStr(n.getNodeName());
		String jsonValue = toStr("children") + ":[";

		for (Node child : n.getChildren()) {
			jsonValue += traverse(child);
			if (n.getChildren().indexOf(child) < n.getChildren().size() - 1)
				jsonValue += ",";
		}
		jsonValue += "]";

		// merge all
		output += "{" + jsonKey + "," + jsonValue + "}";
		return output;
	}

	@Override
	public void reduce() {
		new TreeReducerforClass(node_);
	}

	@Override
	public String getString() {
		return output_;
	}
}