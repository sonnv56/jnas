package com.fit.process.jsf.condition;

import com.fit.loader.tree.Condition;
import com.fit.object.Node;
import com.fit.object.XhtmlNode;

public class XHtmlCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof XhtmlNode && !n.getPath().contains("\\build\\web\\"))
			return true;
		return false;
	}
}
