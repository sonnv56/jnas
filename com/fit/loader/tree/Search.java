package com.fit.loader.tree;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;

public class Search {
	/**
	 * Duyet cay de tim kiem nhung Node thoa man dieu kien
	 * 
	 * @param n
	 *            root cay can duyet
	 * @param condition
	 *            dieu kien
	 * @return
	 */
	public static List<Node> searchNode(Node n, Condition condition) {
		List<Node> listNode = new ArrayList<Node>();

		for (Node child : n.getChildren()) {
			if (condition.isStatisfiabe(child)) {
				listNode.add(child);
			}
			listNode.addAll(searchNode(child, condition));
		}
		return listNode;
	}

	/**
	 * Duyet cay de tim kiem nhung Node thoa man dieu kien
	 * 
	 * @param n
	 *            root cay can duyet
	 * @param condition
	 *            dieu kien
	 * @param relativePath
	 *            duong dan tuong doi cua Node can tim. Cac thanh phan phan cach
	 *            boi dau gach cheo ("/")
	 * @return
	 */
	public static List<Node> searchNode(Node n, Condition condition, String relativePath) {
		List<Node> output = new ArrayList<Node>();
		List<Node> listNode = searchNode(n, condition);

		for (Node item : listNode) {
			final String DELIMITER_IN_RELATIVE_PATH = "/";
			final String DELIMITER_IN_ABSOLUTE_PATH = "\\";
			relativePath = relativePath.replace(DELIMITER_IN_RELATIVE_PATH, DELIMITER_IN_ABSOLUTE_PATH);

			String absolutePath = item.getPath();
			if (absolutePath.contains(relativePath))
				output.add(item);

		}
		return output;
	}
}
