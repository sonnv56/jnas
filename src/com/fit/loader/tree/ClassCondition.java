package com.fit.loader.tree;

import com.fit.object.ClassNode;
import com.fit.object.Node;

/**
 * Kiem tra mot node co phai class hay khong
 * 
 * @author DucAnh
 *
 */
public class ClassCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode)
			return true;
		return false;
	}
}
