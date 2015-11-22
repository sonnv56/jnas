package com.fit.process.tostring;

import java.util.List;

import com.fit.cia.LeafCondition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;

/**
 * Xuat cay cau truc sang dang JSON
 * 
 * @author Duc Anh
 *
 */
public class JsonStrategy implements IToString {
	private String output_ = "";
	private Node node_;

	/**
	 * 
	 * @param node
	 *            root cua project
	 */
	public JsonStrategy(Node node) {
		this.node_ = node;
		convertToString();
	}

	@Override
	public void convertToString() {
		List<Node> leafs = Search.searchNode(node_, new LeafCondition());
		output_ = "{\n" + getFiles(leafs) + ",\n" + getCallDependency(leafs) + "\n}";
	}

	private String getFiles(List<Node> leafList) {
		String files = "";

		for (Node n : leafList) {
			String id = toStr("id") + ":" + n.getId();
			String path = toStr("path") + ":" + toStr(n.getPath().replace(node_.getPath(), "").replace("\\", "/"));

			String object = "{\n" + genTab(4) + toStr(n.getNodeName()) + ":{\n" + genTab(5) + id + ",\n" + genTab(5)
					+ path + "\n" + genTab(5) + "\n" + genTab(4) + "}" + "\n" + genTab(3) + "}";

			files += "\t\t" + object + ",\n";
		}
		files = files.substring(0, files.length() - 2);
		files = "\t" +

		toStr("files") + ":[\n" + files + "\n\t]";

		return files;
	}

	private String genTab(int level) {
		String tab = "";
		for (int i = 0; i < level; i++)
			tab += "     ";
		return tab;
	}

	private String getCallDependency(List<Node> leafs) {
		String calls = "";
		for (Node n : leafs) {
			for (Node callee : n.getCallers()) {
				String object = "\t\t{" + toStr(n.getId() + "") + ":" + callee.getId() + "}";
				calls += object + ",\n";
			}
		}
		calls = calls.substring(0, calls.length() - 2);
		calls = "\t" + toStr("calls") + ":[\n" + calls + "\n\t]";
		return calls;
	}

	private String toStr(String str) {
		return "\"" + str + "\"";
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return output_;
	}
}
