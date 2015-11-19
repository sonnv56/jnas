package com.fit.ducanh.test;

import java.util.List;

import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.ClassCondition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.XHtmlCondition;

/**
 * Ham test file search
 * 
 * @author DucAnh
 *
 */
public class SearchTest {
	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		// tim kiem cac class thoa man
		String relativePath = "com/forest/entity/Administrator.java";
		List<Node> output = Search.searchNode(projectRootNode, new ClassCondition(), relativePath);

		for (Node n : output)
			System.out.println(n.getPath());

		// tim kiem cac file index thoa man
		String relativePath2 = "web/admin/index.xhtml";
		List<Node> output2 = Search.searchNode(projectRootNode, new XHtmlCondition(), relativePath2);

		for (Node n : output2)
			System.out.println(n.getPath());

	}
}
