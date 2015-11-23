package com.fit.process;

import com.fit.object.ProjectNode;

public abstract class Processor {
	/** Node project */
	protected ProjectNode projectNode;
	public abstract void process();
	
	public void setProjectNode(ProjectNode projectNode) {
		this.projectNode = projectNode;
	}
	public ProjectNode getProjectNode() {
		return projectNode;
	}
}
