package com.fit.process.tostring;

import java.util.List;

import org.json.simple.JSONObject;

import com.fit.cia.LeafCondition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;

/**
 * Xuat cay cau truc sang dang JSON gom hai thanh phan: danh sach files va danh
 * sach quan he phu thuoc
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
			JSONObject obj = new JSONObject();

			obj.put("id", n.getId());
			obj.put("path", n.getPath().replace(node_.getPath(), ""));

			files += "\t\t" + obj.toString() + ",\n";
		}
		files = files.substring(0, files.length() - 2);
		files = "\t" +

		toStr("files") + ":[\n" + files + "\n\t]";

		return files;
	}

	private String getCallDependency(List<Node> leafs) {
		String calls = "";
		for (Node n : leafs) {
			for (Node callee : n.getCallees()) {
				JSONObject obj = new JSONObject();

				obj.put("fileID", n.getId());
				obj.put("calleeID", callee.getId());

				calls += "\t\t" + obj.toString() + ",\n";
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
		return output_;
	}
}
