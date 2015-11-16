package com.fit.process.jsf.condition;

import com.fit.loader.tree.Condition;
import com.fit.object.JspNode;
import com.fit.object.Node;

public class JspCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof JspNode && !n.getPath().contains("\\build\\web\\"))
			return true;
		return false;
	}
}
