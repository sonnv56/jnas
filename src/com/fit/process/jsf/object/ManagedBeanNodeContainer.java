package com.fit.process.jsf.object;

import com.fit.object.ClassNode;

/**
 * Dai dien mot thanh phan chua ManagedBeanNode
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanNodeContainer {
	private ClassNode classNode;
	/** Ten managed bean su dung de tiem */
	private String name = "";

	public ManagedBeanNodeContainer(ClassNode classNode) {
		this.classNode = classNode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ClassNode getClassNode() {
		return classNode;
	}

	public void setClassNode(ClassNode classNode) {
		this.classNode = classNode;
	}

	@Override
	public String toString() {
		return classNode.toString() + " | " + name;
	}
}
