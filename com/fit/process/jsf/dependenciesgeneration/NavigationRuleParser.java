package com.fit.process.jsf.dependenciesgeneration;

import java.util.List;

import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNode;
import com.fit.process.jsf.object.NavigationRule;

/**
 * Phan tich mot navigation rule de tao danh sach phu thuoc (JSF Config, managed
 * beans), (JSF Config, web pages)
 * 
 * @author DucAnh
 *
 */
public class NavigationRuleParser extends DependenciesGeneration {
	public NavigationRuleParser(NavigationRule navigationRule, List<Node> webPagesList,
			List<ManagedBeanNode> managedBeansList) {

	}

	@Override
	public List<Dependency> findDependencies() {
		// TODO Auto-generated method stub
		return null;
	}
}
