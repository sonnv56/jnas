package com.fit.process.tostring;

import java.util.List;

import com.fit.cia.LeafCondition;
import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.DependencyGeneration;
import com.fit.util.Utils;

public class NodeDescriptionGeneration implements IDescription {
	private Node node_;

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);
		DependencyGeneration.parse(projectRootNode);
		// display tree of project
		IDescription nodeDescription = new NodeDescriptionGeneration(projectRootNode);
		System.out.println(nodeDescription.getString());

	}

	public NodeDescriptionGeneration(Node node) {
		this.node_ = node;
		describe();
	}

	@Override
	public void describe() {
		List<Node> leafNodes = Search.searchNode(node_, new LeafCondition());
		for (Node leafNode : leafNodes) {
			Utils.writeContentToFile(leafNode.getPath(),
					ConfigurationOfAnh.DEFAULT_NODE_DATA_PATH_FOR_DEPENDENCY_GRAPH + "\\" + createExtension(leafNode));
		}
	}

	private String createExtension(Node leafNode) {
		final String DELIMITER_BETWEEN_EXTENSION_AND_NAME = ".";
		final String DATA_FILE_EXTENSION = ".mkdn";

		String nameNode = leafNode.getNodeName();
//		nameNode = nameNode.substring(0, nameNode.lastIndexOf(DELIMITER_BETWEEN_EXTENSION_AND_NAME));
		nameNode = nameNode + "[" + leafNode.getId() + "]" + DATA_FILE_EXTENSION;
		return nameNode;
	}

	@Override
	public String getString() {
		return "";
	}

}
