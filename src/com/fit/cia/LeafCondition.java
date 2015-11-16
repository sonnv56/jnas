package com.fit.cia;

import com.fit.loader.tree.Condition;
import com.fit.object.ComponentNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

/**
 * Tim kiem tat ca node la trong cay.
 * 
 * @author DucAnh
 *
 */
public class LeafCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (!(n instanceof ComponentNode || n instanceof ProjectNode)) {
			return true;
		}
		return false;
	}
}
