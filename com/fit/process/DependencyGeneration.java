package com.fit.process;

import java.util.List;

import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.CDIProcessor2;
import com.fit.process.jsf.JSFParser;
import com.fit.process.jsf.TreeUpdater;
import com.fit.process.jsf.object.Dependency;

public class DependencyGeneration {
	public static void parse(Node projectRootNode) {
		/** JSF parser here */
		List<Dependency> dependenciesList = new JSFParser(projectRootNode).getDependenciesList();
		TreeUpdater.createConnection(dependenciesList);
		/** CDI parser here */
		CDIProcessor2 processor = new CDIProcessor2();
		processor.setProjectNode((ProjectNode) projectRootNode);

		processor.process();
		/** Web service parser here */
	}
}
