package com.fit.loader.tree;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;

public class Search {

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
}
