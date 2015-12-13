package com.fit.process.totree;

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
public class DependencyGeneration implements IJsonParser {
	private String content_ = "";
	private List<Node> nodes_ = new ArrayList<>();

	public static void main(String[] args) {
		String path = "C:\\Dependency.json";
		new DependencyGeneration(path).display();
	}

	public DependencyGeneration(String dependencyPath) {
		try {
			content_ = Utils.readFileContent(dependencyPath);
			parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse() {
		JSONParser parser = new JSONParser();
		try {
			JSONArray arr = (JSONArray) parser.parse(content_);
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
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Node getNode(int searchedId) {
		for (Node n : nodes_)
			if (n.getId() == searchedId)
				return n;
		return null;
	}

	public void display() {
		for (Node n : nodes_) {
			System.out.print(n.getId() + ":");
			for (Node callee : n.getCallees())
				System.out.print(callee.getId() + ", ");
			System.out.println();
		}
	}

	/**
	 * Lấy danh sách Node
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return nodes_;
	}
}
