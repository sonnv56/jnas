package com.fit.object;

import java.util.List;

public class ClassNode extends Node{

	public ClassNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public ClassNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

	
}
