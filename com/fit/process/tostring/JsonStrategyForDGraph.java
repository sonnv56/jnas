package com.fit.process.tostring;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fit.cia.LeafCondition;
import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.DependencyGeneration;

/**
 * Xuat file json de hien thi cay phu thuoc trong
 * http://nylen.tv/d3-process-map/graph.php
 * 
 * @author DucAnh
 *
 */
public class JsonStrategyForDGraph implements IToString {
	private String output_ = "";
	private Node node_;

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);
		DependencyGeneration.parse(projectRootNode);
		// display tree of project
		IToString treeDisplayer = new JsonStrategyForDGraph(projectRootNode);
		System.out.println(treeDisplayer.getString());

	}

	/**
	 * 
	 * @param node
	 *            root cay cau truc vat li cua project
	 */
	public JsonStrategyForDGraph(Node node) {
		this.node_ = node;
		convertToString();
	}

	@Override
	public void convertToString() {
		List<Node> fileNodes = Search.searchNode(node_, new LeafCondition());
		JSONArray output = new JSONArray();
		for (Node file : fileNodes)
			if (file.getCallees().size() > 0 || file.getCallers().size() > 0) {
				JSONObject obj = new JSONObject();
				obj.put("name", file.getNodeName() + "[" + file.getId() + "]");
				obj.put("type", "group0");

				JSONArray depends = new JSONArray();
				for (Node callee : file.getCallees())
					depends.add(callee.getNodeName() + "[" + callee.getId() + "]");

				obj.put("depends", depends);

				output.add(obj);
			}
		output_ = output.toString();
	}

	private String toStr(String str) {
		return "\"" + str + "\"";
	}

	@Override
	public String getString() {
		return output_;
	}
}