package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import com.fit.process.jsf.object.Dependency;

/**
 * Lop truu truong su dung de tim kiem cac su phu thuoc
 * 
 * @author DucAnh
 *
 */
public abstract class DependenciesGeneration {
	protected List<Dependency> dependenciesList = new ArrayList<Dependency>();

	public abstract List<Dependency> findDependencies();

	public List<Dependency> getDependenciesList() {
		return dependenciesList;
	}
}
