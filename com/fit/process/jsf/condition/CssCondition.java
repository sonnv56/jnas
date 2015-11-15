package com.fit.process.jsf.condition;

import com.fit.loader.tree.Condition;
import com.fit.object.CssNode;
import com.fit.object.Node;

public class CssCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof CssNode)
			return true;
		return false;
	}
}
