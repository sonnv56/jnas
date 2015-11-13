package com.fit.process.jsf.connection;

import java.util.ArrayList;
import java.util.List;

import com.fit.process.jsf.object.Dependency;

/**
 * Lop truu truong su dung de tim kiem cac su phu thuoc
 * @author DucAnh
 *
 */
public abstract class ConnectionGeneration {
	private List<Dependency> dependenciesList = new ArrayList<Dependency>();

	abstract void findDependencies();

	public List<Dependency> getDependenciesList() {
		return dependenciesList;
	}
}
