package com.fit.ducanh.test;

import com.fit.ducanh.test.displayTree.TreeStrategy;
import com.fit.loader.ProjectLoader;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.JsfConnectionGeneration;

/**
 * Test JsfConnectionGeneration class
 * 
 * @author DucAnh
 *
 */
public class JsfConnectionGenerationTest {
	public static void main(String[] args) {
		// Project tree generation
		String projectRootPath = "C:\\Users\\DucAnh\\Dropbox\\Workspace\\Download project\\DEMO J2EE 2\\dukes-forest\\dukes-forest";
		ProjectNode projectRootNode = ProjectLoader.load(projectRootPath);

		// create connection between .xhtml/.jsp and managed bean file
		new JsfConnectionGeneration(projectRootNode);

		// display tree of project
		TreeStrategy treeDisplayer = new TreeStrategy(projectRootNode);
		System.out.println(treeDisplayer.getString());
	}
}
