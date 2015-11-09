package com.fit.ducanh.test.displayTree;

import com.fit.object.ClassNode;
import com.fit.object.ComponentNode;
import com.fit.object.JspNode;
import com.fit.object.Node;
import com.fit.object.XhtmlNode;
import com.fit.object.XmlNode;

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
		if (n == null)
			return;
		else {
			if (n instanceof ComponentNode) {
				output += genTab(level) + n.getNodeName() + "\n";

			} else if (n instanceof ClassNode) {
				output += genTab(level) + n.getNodeName() + "\n";
				for (Node caller : n.getCallers())
					output += genTab(level + 1) + "[caller]" + caller.getNodeName() + "\n";

			} else if (n instanceof JspNode) {
				output += genTab(level) + n.getNodeName() + "\n";
				for (Node callee : n.getCallees())
					output += genTab(level + 1) + callee.getNodeName() + "\n";

			} else if (n instanceof XhtmlNode) {
				output += genTab(level) + n.getNodeName() + "\n";
				for (Node callee : n.getCallees())
					output += genTab(level + 1) + "[callee]" + callee.getNodeName() + "\n";

			} else if (n instanceof XmlNode) {
				output += genTab(level) + n.getNodeName() + "\n";
			}
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
