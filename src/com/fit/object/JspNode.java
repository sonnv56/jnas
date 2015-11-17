package com.fit.object;

import java.util.List;

public class JspNode extends XmlNode {

	public JspNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JspNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public JspNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

}
