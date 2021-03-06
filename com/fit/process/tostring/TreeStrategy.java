package com.fit.process.tostring;

import com.fit.object.Node;

/**
 * Mot chien thuat bien doi cay sang xau
 * 
 * @author DucAnh
 *
 */
public class TreeStrategy implements IToString {
	private String output = "";
	private Node node;

	public TreeStrategy(Node node) {
		this.node = node;
		convertToString();
	}

	@Override
	public String getString() {
		return output;
	}

	@Override
	public void convertToString() {
		displayTree(node, 0);
	}

	private void displayTree(Node n, int level) {
		if (n == null || n.getPath() == null)
			return;
		else {
			output += genTab(level) + "[" + n.getId() + "]" + n.getNodeName() + "\n";
			for (Node callee : n.getCallees())
				output += genTab(level + 1) + "[callee id=" + callee.getId() + "]" + callee.getNodeName() + "\n";
			for (Node caller : n.getCallers())
				output += genTab(level + 1) + "[caller id=" + caller.getId() + "]" + caller.getNodeName() + "\n";
		}
		for (Object child : n.getChildren()) {
			displayTree((Node) child, ++level);
			level--;
		}

	}

	private String genTab(int level) {
		String tab = "";
		for (int i = 0; i < level; i++)
			tab += "     ";
		return tab;
	}

}
