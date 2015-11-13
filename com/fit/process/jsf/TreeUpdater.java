package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNode;

/**
 * Phan tich cac su phu thuoc va cap nhat vao cay cau truc
 * 
 * @author DucAnh
 *
 */
public class TreeUpdater {
	public static void createConnection(Node projectNode, List<Dependency> dependenciesList) {

	}

	public static List<Dependency> getDependency(Node subProjectItem, List<ManagedBeanNode> managedBeanNodeList,
			List<Node> listXHtmlNodes) {
		List<Dependency> output = new ArrayList<Dependency>();
		// do something here
		return output;
	}

}
