package com.fit.cia;

import com.fit.loader.tree.Condition;
import com.fit.object.ComponentNode;
import com.fit.object.Node;
import com.fit.object.ProjectNode;

/**
 * Tim tat ca node la trong cay thoa man: nhung node la nay gay phu thuoc/bi phu
 * thuoc vao node khac
 * 
 * @author DucAnh
 *
 */
public class WaveLeafCondition extends Condition {
	@Override
	public boolean isStatisfiabe(Node n) {
		if (!(n instanceof ComponentNode || n instanceof ProjectNode))
			if (n.getCallees().size() > 0 || n.getCallers().size() > 0) {
				return true;
			}
		return false;
	}
}
