package com.fit.ducanh.test;

import com.fit.loader.ProjectLoader;
import com.fit.object.ProjectNode;
import com.fit.process.tostring.TreeStrategy;

/**
 * Test ProjectLoad class
 * 
 * @author DucAnh
 *
 */
public class ProjectLoaderTest {
	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		// display tree of project
		TreeStrategy treeDisplayer = new TreeStrategy(projectRootNode);
		System.out.println(treeDisplayer.getString());
	}
}
