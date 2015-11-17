package com.fit.object;

import java.util.List;

/**
 * File cau hinh
 * @author DucAnh
 *
 */
public class ConfigurationNode extends XmlNode {

	public ConfigurationNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConfigurationNode(int id, String path, Node parent) {
		super(id, path, parent);
		// TODO Auto-generated constructor stub
	}

	public ConfigurationNode(String path, List<Node> callees, List<Node> callers) {
		super(path, callees, callers);
		// TODO Auto-generated constructor stub
	}

}
