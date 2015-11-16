package com.fit.process.jsf.condition;

import com.fit.loader.tree.Condition;
import com.fit.object.Node;
import com.fit.object.XmlNode;

public class ConfigurationCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof XmlNode)
			return true;
		return false;
	}
}