package com.fit.cia.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.object.ClassNode;
import com.fit.object.ComponentNode;
import com.fit.object.ConfigurationNode;
import com.fit.object.CssNode;
import com.fit.object.JavascriptNode;
import com.fit.object.Node;
import com.fit.util.Utils;

/**
 * Sinh cay phu thuoc (Thuc chat la mot danh sach)
 * @author DucAnh
 *
 */
public class TreeGeneration implements IToTree {
	private String jsonContent_;
	private List<Node> nodes_ = new ArrayList<>();

	public static void main(String[] args) {
		List<Node> nodes = new TreeGeneration(ConfigurationOfAnh.JSON_DUKES_FOREST_PATH).getNodes();
		for (Node n : nodes) {
			String output = "[id=" + n.getId() + "]" + n.getPath();
			for (Node callee : n.getCallees())
				output += "\n\t[callee id=" + callee.getId() + "]" + callee.getPath();
			for (Node caller : n.getCallers())
				output += "\n\t[caller id=" + caller.getId() + "]" + caller.getPath();

			System.out.println(output);
		}
	}

	public TreeGeneration(String jsonPath) {
		try {
			jsonContent_ = Utils.readFileContent(jsonPath);
			convertToTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void convertToTree() {
		JSONParser parser = new JSONParser();
		try {
			final JSONObject jsonInput = (JSONObject) parser.parse(jsonContent_);

			/** Lay danh sach file */
			final String FILES_KEY = "files";
			JSONArray jsonFiles = (JSONArray) parser.parse(jsonInput.get(FILES_KEY).toString());
			nodes_ = generateNodes(jsonFiles);

			/** Lay danh sach call */
			final String CALL_KEY = "calls";
			JSONArray jsonCalls = (JSONArray) parser.parse(jsonInput.get(CALL_KEY).toString());
			generateCallDependency(nodes_, jsonCalls);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private List<Node> generateNodes(JSONArray jsonFiles) {
		List<Node> nodes = new ArrayList<>();
		Node projectRootNode_ = new ComponentNode();
		try {
			for (int i = 0; i < jsonFiles.size(); i++) {
				JSONParser parser = new JSONParser();
				final JSONObject jsonItem = (JSONObject) parser.parse(jsonFiles.get(i).toString());
				/** Lay thong tin */
				final String ID_KEY = "id";
				final String PATH_KEY = "path";
				Integer id = Integer.parseInt(jsonItem.get(ID_KEY).toString());
				String path = jsonItem.get(PATH_KEY).toString();

				/** Tao node moi */
				Node n;
				if (path.endsWith(".java")) {
					n = new ClassNode(id, path, projectRootNode_);
					nodes.add(n);
				} else if (path.endsWith(".xml")) {
					n = new ConfigurationNode(id, path, projectRootNode_);
					nodes.add(n);
				} else if (path.endsWith(".xhtml")) {
					n = new ClassNode(id, path, projectRootNode_);
					nodes.add(n);
				} else if (path.endsWith(".jsp")) {
					n = new ClassNode(id, path, projectRootNode_);
					nodes.add(n);
				} else if (path.endsWith(".js")) {
					n = new JavascriptNode(id, path, projectRootNode_);
					nodes.add(n);
				} else if (path.endsWith(".css")) {
					n = new CssNode(id, path, projectRootNode_);
					nodes.add(n);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	private void generateCallDependency(List<Node> nodes, JSONArray jsonCalls) {
		try {
			for (int i = 0; i < jsonCalls.size(); i++) {
				JSONParser parser = new JSONParser();
				final JSONObject jsonItem = (JSONObject) parser.parse(jsonCalls.get(i).toString());
				/** Lay thong tin */
				final String FIELD_ID = "fileID";
				final String CALLEE_ID = "calleeID";
				Integer fileID = Integer.parseInt(jsonItem.get(FIELD_ID).toString());
				Integer calleeID = Integer.parseInt(jsonItem.get(CALLEE_ID).toString() + "");

				/** Tao quan he */
				Node gayPhuThuoc = getNode(nodes, fileID);
				Node biPhuThuoc = getNode(nodes, calleeID);
				if (gayPhuThuoc != null && biPhuThuoc != null) {
					gayPhuThuoc.getCallees().add(biPhuThuoc);
					biPhuThuoc.getCallers().add(gayPhuThuoc);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Node getNode(List<Node> nodes, int id) {
		for (Node n : nodes)
			if (n.getId() == id)
				return n;
		return null;
	}

	public List<Node> getNodes() {
		return nodes_;
	}
}
