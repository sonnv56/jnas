package com.fit.object;

import java.util.List;

/**
 * Dai dien nhung file bieu dien dang XML
 * 
 * @author DucAnh
 *
 */
public abstract class XmlNode extends Node {

	public XmlNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XmlNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public XmlNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

}
