package com.fit.process.cdi.condition;

import java.io.IOException;

import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.util.Utils;

/**
 * @author son Dieu kien kiem tra CDI anotation Name
 * */
public class CDICustomQualifierCondition extends Condition {
	private static String ANOTATION_PREFIX = "@";
	private static final String INJECT_ANOTATION = "@Inject";
	private static final String DECLARE_CLASS = "public class";
	/** Anotation Named */
	private String name;
	
	public CDICustomQualifierCondition(String name) {
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			try {
				// Doc file java
				String fileContent = Utils.readFileContent(n.getPath());
				// Lay vi tri cua anotation Named
				int index = fileContent.indexOf(ANOTATION_PREFIX+name);
				int index2 = fileContent.indexOf(INJECT_ANOTATION);
				int index3 = fileContent.indexOf(DECLARE_CLASS, index);
				// Kiem tra
				if (index2==-1 && index != -1 && index3 !=-1) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
