package com.fit.cia;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.ProjectNode;
import com.fit.process.DependencyGeneration;
import com.fit.process.tostring.IToString;
import com.fit.process.tostring.JsonStrategyForHung;
import com.fit.process.tostring.TreeStrategy;

/**
 * Ham tien xu li cay chuan bi cho qua trinh xu li CIA
 * 
 * @author DucAnh
 *
 */
public class Preprocess {
	public static void main(String[] args) {
		// Project tree generation
//		ProjectNode projectRootNode = ProjectLoader.load("/home/rmrf/builds/dukes-forest");
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		// create dependency between nodes in structure tree here (do later)
		DependencyGeneration.parse(projectRootNode);
		
		IToString displayer = new TreeStrategy(projectRootNode);
		System.out.println(displayer.getString());

		// Lay tat ca cac node la
		// List<Node> leafList = Search.searchNode(projectRootNode, new
		// LeafCondition());
		// for (Node n : leafList)
		// System.out.println(n.getPath());
	}
}
