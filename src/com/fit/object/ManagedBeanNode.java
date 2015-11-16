package com.fit.object;

/**
 * Dai dien mot thanh phan managed bean
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanNode extends ClassNode {
	/** Ten managed bean su dung de tiem */
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
