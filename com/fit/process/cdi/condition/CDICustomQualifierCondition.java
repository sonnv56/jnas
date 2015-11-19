package com.fit.process.cdi.condition;

import java.io.IOException;

import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.process.cdi.CDIConst;
import com.fit.util.Utils;

/**
 * @author son Dieu kien kiem tra CDI anotation Name
 * */
public class CDICustomQualifierCondition extends Condition {
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
				String anotation = name;
				if(anotation.indexOf(CDIConst.ANNOTATION_PREFIX) == -1){
					anotation = CDIConst.ANNOTATION_PREFIX + name;
				}
				int index = fileContent.indexOf(anotation);
				int index2 = fileContent.indexOf(CDIConst.INJECT_ANNOTATION);
				int index3 = fileContent.indexOf(CDIConst.DECLARE_CLASS, index);
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
