package com.fit.object;

import java.util.List;

public class CssNode extends Node {
	public CssNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CssNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public CssNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}
}
