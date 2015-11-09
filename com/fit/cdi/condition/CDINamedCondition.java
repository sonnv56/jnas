package com.fit.cdi.condition;

import java.io.IOException;

import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.util.Utils;
/**
 * @author son
 * Dieu kien kiem tra CDI anotation Name
 * */
public class CDINamedCondition extends Condition {
	/**Anotation Named*/
	private static String NAMED_ANOTATION = "@Named";
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			try {
				//Doc file java
				String fileContent = Utils.readFileContent(n.getPath());
				//Lay vi tri cua anotation Named
				int index = fileContent.indexOf(NAMED_ANOTATION);
				//Vi tri ngay gan anotation Named
				char nextToAnotation = fileContent.charAt(index+NAMED_ANOTATION.length());
				//Kiem tra 
				if (index!=-1 && !Character.isAlphabetic(nextToAnotation) 
						&& !Character.isDigit(nextToAnotation)){
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
