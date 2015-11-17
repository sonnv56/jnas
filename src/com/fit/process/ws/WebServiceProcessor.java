package com.fit.process.ws;

import java.util.List;
import java.util.Vector;

import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Condition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.util.Utils;

public class WebServiceProcessor {
	private ProjectNode root;
	
	public WebServiceProcessor(ProjectNode node) {
		root = node;
	}

	public static void main(String[] args) {
		String projectRootPath = "/home/rmrf/builds/dukes-forest";
		ProjectNode node = ProjectLoader.load(projectRootPath);
		
		WebServiceProcessor wsp = new WebServiceProcessor(node);
		wsp.process();
	}
	
	public void process() {
		List<Node> wsdlNodes = Search.searchNode(root, new Condition() {
			@Override
			public boolean isStatisfiabe(Node n) {
				return Utils.fileEndsWith(n.getPath(), "wsdl");
			}
		});
		
		for (Node n: wsdlNodes) {
			System.out.println(n.getPath());
		}
	}
}
