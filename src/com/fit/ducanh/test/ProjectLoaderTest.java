package com.fit.ducanh.test;

import com.fit.ducanh.test.displayTree.TreeStrategy;
import com.fit.loader.ProjectLoader;
import com.fit.object.ProjectNode;

/**
 * Test ProjectLoad class
 * 
 * @author DucAnh
 *
 */
public class ProjectLoaderTest {
	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);

		// display tree of project
		TreeStrategy treeDisplayer = new TreeStrategy(projectRootNode);
		System.out.println(treeDisplayer.getString());
	}
}
