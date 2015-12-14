package com.fit.cia.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fit.object.Node;
import com.fit.util.Utils;

/**
 * Phân tích file json Dependency.json để tạo đồ thị phụ thuộc.
 * 
 * @author DucAnh
 *
 */
public class JsonParser implements IJsonParser {
	private String dependency_ = "";
	private String changeSet_ = "";
	private List<Node> nodes_ = new ArrayList<>();
	private List<Node> changeSetNodes_ = new ArrayList<>();

	public static void main(String[] args) {
		String dependencyPath = "C:\\Dependency.json";
		String changeSetPath = "C:\\ChangeSet.json";
		new JsonParser(dependencyPath, changeSetPath).display();
	}

	public JsonParser(String dependency, String changeSet) {
		dependency_ = dependency;
		changeSet_ = changeSet_.replace(" ", "");
		parse();
	}

	@Override
	public void parse() {
		JSONParser parser = new JSONParser();
		try {
			JSONArray arr = (JSONArray) parser.parse(dependency_);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject item = (JSONObject) arr.get(i);

				Integer id = Integer.parseInt((item.get("id") + ""));
				Node caller = getNode(id);
				if (caller == null) {
					caller = new IdNode();
					caller.setId(id);

					nodes_.add(caller);
				}

				JSONArray depends = (JSONArray) item.get("depends");
				for (int j = 0; j < depends.size(); j++) {
					Integer idCallee = Integer.parseInt(depends.get(j) + "");

					Node callee = getNode(idCallee);
					if (callee == null) {
						callee = new IdNode();
						callee.setId(idCallee);

						nodes_.add(callee);
					}

					caller.getCallees().add(callee);
					callee.getCallers().add(caller);
				}
			}

			// Lấy danh sách change Set
			changeSetNodes_ = createChangeSet(nodes_, changeSet_);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Node getNode(int searchedId) {
		for (Node n : changeSetNodes_)
			if (n.getId() == searchedId)
				return n;
		return null;
	}

	/**
	 * 
	 * @param nodes
	 *            Danh sách node
	 * @param changeSet
	 *            Danh sách changeSet dạng xâu. Mỗi một phần tử phân cách bởi
	 *            dấu phẩy.
	 * @return
	 */
	private List<Node> createChangeSet(List<Node> nodes, String changeSet) {
		List<Node> changeSetNodes = new ArrayList<Node>();

		String[] changeSetItems = changeSet.split(",");
		for (String changeSetItem : changeSetItems) {
			int idItem = Integer.parseInt(changeSetItem);
			Node changeSetNode = getNode(nodes, idItem);
			if (changeSetNode != null) {
				changeSetNodes.add(changeSetNode);
			}
		}
		return changeSetNodes;
	}

	public void display() {
		for (Node n : nodes_) {
			System.out.print(n.getId() + ":");
			for (Node callee : n.getCallees())
				System.out.print(callee.getId() + ", ");
			System.out.println();
		}
		System.out.println("change set: " + changeSetNodes_.size() + "--------------------------------");
		for (Node n : changeSetNodes_) {
			System.out.print(n.getId() + ":");
			for (Node callee : n.getCallees())
				System.out.print(callee.getId() + ", ");
			System.out.println();
		}
	}

	/**
	 * 
	 * @param nodes
	 *            Danh sách Node
	 * @param id
	 *            Id cần lấy
	 * @return Node chứa ID
	 */
	private Node getNode(List<Node> nodes, int id) {
		for (Node n : nodes)
			if (n.getId() == id)
				return n;
		return null;
	}

	/**
	 * Lấy danh sách Node
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return changeSetNodes_;
	}
}
