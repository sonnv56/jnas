package com.fit.cia;

import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

/**
 * Ham tien xu li cay chuan bi cho qua trinh xu li CIA
 * 
 * @author DucAnh
 *
 */
public class Proprocess {
	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);

		// create dependency between nodes in structure tree here (do later)

		// Lay tat ca cac node la
		List<Node> leafList = Search.searchNode(projectRootNode, new LeafCondition());
		for (Node n : leafList)
			System.out.println(n.getPath());
	}
}
