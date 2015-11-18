package com.fit.process.jsf;

import com.fit.loader.tree.Condition;
import com.fit.object.Node;

public class NodeCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n.getPath().contains("\\build"))
			return false;
		return true;
	}
}
