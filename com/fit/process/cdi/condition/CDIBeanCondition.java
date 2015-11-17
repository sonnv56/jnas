package com.fit.process.cdi.condition;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fit.loader.tree.Condition;
import com.fit.object.Node;

public class CDIBeanCondition extends Condition{
	private static final String CDI_BEAN_XML_FILE = "beans.xml";
	public boolean isStatisfiabe(Node n) {
		Path p = Paths.get(n.getPath());
		String file = p.getFileName().toString();
//		System.out.println(file);
		return (file.equals(CDI_BEAN_XML_FILE));
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
