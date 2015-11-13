package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBean;
import com.fit.process.jsf.object.ManagedBeanNode;

/**
 * Phan tich mot the managed bean d/n trong <managed-bean> de tao su phu thuoc
 * (JSF Config, Java class). Dong thoi, danh sach class la managed bean duoc
 * trich xuat
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanParser extends DependenciesGeneration {
	private List<ManagedBeanNode> managedBean = new ArrayList<>();

	public ManagedBeanParser(ManagedBean mb, List<Node> javaFiles) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Dependency> findDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ManagedBeanNode> getManagedBean() {
		return managedBean;
	}
}
