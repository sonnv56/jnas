package com.fit.cia;

import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.JsfConnectionGeneration;

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

		// create connection between .xhtml/.jsp and managed bean file
		new JsfConnectionGeneration(projectRootNode);

		// Lay tat ca cac node la
		List<Node> leafList = Search.searchNode(projectRootNode, new LeafCondition());
		for (Node n : leafList)
			System.out.println(n.getPath());
	}
}
