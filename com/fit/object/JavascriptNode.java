package com.fit.object;

import java.util.List;

public class JavascriptNode extends Node {
	public JavascriptNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JavascriptNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public JavascriptNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

}
