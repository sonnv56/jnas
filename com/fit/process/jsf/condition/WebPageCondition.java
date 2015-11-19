package com.fit.process.jsf.condition;

import com.fit.loader.tree.Condition;
import com.fit.object.JspNode;
import com.fit.object.Node;
import com.fit.object.XhtmlNode;

public class WebPageCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if ((n instanceof XhtmlNode || n instanceof JspNode) && !n.getPath().contains("\\build\\"))
			return true;
		return false;
	}
}