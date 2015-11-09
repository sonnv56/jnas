package com.fit.son.test;

import java.util.List;

import com.fit.cdi.condition.CDINamedCondition;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

public class Test {
	public static void main(String[] args) {
		// String projectRootPath =
		// "C:\\Users\\DucAnh\\Dropbox\\Project\\J2EE\\DEMO J2EE 2\\dukes-forest\\dukes-forest";
		String projectRootPath = "C://Users//son//Desktop//Workspace//CIA//dukes-forest";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		List<Node> listManagedBeanFile = Search.searchNode(projectNode, new CDINamedCondition());
		for (Node item : listManagedBeanFile) {
			System.out.println(item.getPath());
		}
	}

}
