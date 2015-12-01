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
				if (containMbAnnotation(fileContent)) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean containMbAnnotation(String fileContent) {
		for (String mbAnnotation : MANAGED_BEAN_ANNOTATIONS)
			if (fileContent.contains(mbAnnotation))
				return true;
		return false;
	}

	private static final String[] MANAGED_BEAN_ANNOTATIONS = new String[] { "@ManagedBean(", "@Named(", "@Named\n" };
}
