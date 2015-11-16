package com.fit.loader.tree;

import java.io.IOException;

import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.util.Utils;

public class ManagedBeanCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			try {
				String fileContent = Utils.readFileContent(n.getPath());
				if (fileContent.indexOf("@ManagedBean")!=-1){
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
