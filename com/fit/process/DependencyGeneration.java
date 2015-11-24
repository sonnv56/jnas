package com.fit.process;

import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.tostring.IToString;
import com.fit.process.tostring.JsonStrategyForHung;
import com.fit.process.tostring.TreeStrategy;
import com.fit.process.ws.WebServiceProcessor;

public class DependencyGeneration {
	public static void parse(Node projectRootNode) {
		
		/** JSF parser here */
		
//		List<Dependency> dependenciesList = new JSFParser(projectRootNode).getDependenciesList();
//		TreeUpdater.createConnection(dependenciesList);
//		/** CDI parser here */
//		CDIProcessor processor = new CDIProcessor();
//		processor.setProjectNode((ProjectNode) projectRootNode);

//		processor.process();
		/** Web service parser here */
		
		WebServiceProcessor wsp = new WebServiceProcessor((ProjectNode) projectRootNode);
		wsp.process();
		IToString displayer = new TreeStrategy(projectRootNode);
		System.out.println(displayer.getString());

	}
}
