package com.fit.cia;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.ProjectNode;
import com.fit.process.DependencyGeneration;
import com.fit.process.tostring.IToString;
import com.fit.process.tostring.JsonStrategyForDGraph;
import com.fit.process.tostring.NodeDescriptionGeneration;
import com.fit.process.tostring.TreeStrategy;
import com.fit.util.Utils;

/**
 * Ham tien xu li cay chuan bi cho qua trinh xu li CIA.
 * 
 * @author DucAnh
 *
 */
public class Preprocess {
	public static void main(String[] args) {
		/** Project tree generation */
		// ProjectNode projectRootNode =
		// ProjectLoader.load("/home/rmrf/builds/dukes-forest");
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.JSF_DUKES_FOREST_PATH);

		/** create dependency between nodes in structure tree here */
		DependencyGeneration.parse(projectRootNode);

		/** Display dependency tree */
		IToString displayer = new TreeStrategy(projectRootNode);
		System.out.println(displayer.getString());

		/** Json generation for dependency graph */
		JsonStrategyForDGraph dGraphJson = new JsonStrategyForDGraph(projectRootNode);
		Utils.writeContentToFile(dGraphJson.getString(), ConfigurationOfAnh.DEFAULT_JSON_PATH_FOR_DEPENDENCY_GRAPH);
		NodeDescriptionGeneration nodeDescription = new NodeDescriptionGeneration(projectRootNode);
		
		/** view dependency graph */
		Utils.openDefaultBrowser("http://localhost/graph.php");
	}
}
