package com.fit.process.jsf.object;

import com.fit.object.ClassNode;

/**
 * Dai dien mot thanh phan managed bean
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanNode {
	private ClassNode classNode;

	public ManagedBeanNode(ClassNode classNode) {
		this.classNode = classNode;
	}

	/** Ten managed bean su dung de tiem */
	private String name = "";

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
}
