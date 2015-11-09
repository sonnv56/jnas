package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;

abstract class WebFileParser {
	private Node webFileNode;
	private List<Node> listManagedNodes;

	public WebFileParser(Node webFileNode) {
		this.webFileNode = webFileNode;
		listManagedNodes = new ArrayList<Node>();
	}

	public void parse() {

	}
}