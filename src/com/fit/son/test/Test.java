package com.fit.son.test;

import java.io.File;
import java.io.IOException;
import org.w3c.dom.Element;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.condition.CDIBeanCondition;

public class Test {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		// default code
//		
		
		
		
//		
	}
}
