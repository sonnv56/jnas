package com.fit.process.cdi.condition;

import java.io.IOException;

import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.process.cdi.CDIConst;
import com.fit.util.Utils;
/**
 * @author son
 * Dieu kien kiem tra CDI anotation Name
 * */
public class CDINamedCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			try {
				//Doc file java
				String fileContent = Utils.readFileContent(n.getPath());
				//Lay vi tri cua anotation Named
				int index = fileContent.indexOf(CDIConst.NAMED_ANNOTATION);
				//Vi tri ngay gan anotation Named
				char nextToAnotation = fileContent.charAt(index+CDIConst.NAMED_ANNOTATION.length());
				//Kiem tra 
				if (index!=-1 && !Character.isAlphabetic(nextToAnotation) && !Character.isDigit(nextToAnotation)){
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
