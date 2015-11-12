package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

public class main {
	public main() {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.DUKES_FOREST_PATH);

		// visit all sub projects
		for (Node subProjectItem : projectRootNode.getChildren()) {
			List<Dependency> dependenciesList = new ArrayList<Dependency>();
			List<Node> managedBeanNodeList = new ArrayList<>();

			List<Node> listConfigurationFiles = WebXmlParser.getListConfigurationFilePaths(subProjectItem);

			for (Node configItem : listConfigurationFiles) {
				// Tao lien ket giua cac file giao dien
				dependenciesList.addAll(NavigationParser.getDependenciesList(configItem));
				ConnectionGeneration.createConnection(subProjectItem, dependenciesList);

				// Tao lien ket giua file giao dien va cac file managed bean
				managedBeanNodeList.addAll(ManageBeanParser.collectManageBean(configItem));
				managedBeanNodeList.addAll(CollectManagedBeanFromJavaFile.getManagedBeansList(subProjectItem));
				
				// 
				ConnectionGeneration.createConnection(subProjectItem, dependenciesList);
			}
		}
	}
}
