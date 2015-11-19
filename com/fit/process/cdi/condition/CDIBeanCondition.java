package com.fit.process.cdi.condition;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fit.loader.tree.Condition;
import com.fit.object.Node;
import com.fit.process.cdi.CDIConst;

public class CDIBeanCondition extends Condition{
	public boolean isStatisfiabe(Node n) {
		Path p = Paths.get(n.getPath());
		String file = p.getFileName().toString();
//		System.out.println(file);
		return (file.equals(CDIConst.CDI_BEAN_XML_FILE));
	}

}
