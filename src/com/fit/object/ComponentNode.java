/**
 * 
 */
package com.fit.object;

import java.util.List;

/**
 * @author son
 *
 */
public class ComponentNode extends Node{

	public ComponentNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComponentNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public ComponentNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

}
