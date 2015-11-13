package com.fit.process.jsf.dependenciesgeneration;

import java.util.List;

import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNode;

/**
 * Phan tich web .xhtml de lay su phu thuoc (.xhtml page, CSS file), (.xhtml
 * page, MB file)
 * 
 * @author DucAnh
 *
 */
public class HTMLWebPageParser extends DependenciesGeneration {
	public HTMLWebPageParser(List<Node> cssFiles, List<ManagedBeanNode> managedBeanList) {
	}

	@Override
	public List<Dependency> findDependencies() {
		// TODO Auto-generated method stub
		return null;
	}
}
