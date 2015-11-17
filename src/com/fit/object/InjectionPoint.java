package com.fit.object;

import org.eclipse.jdt.core.dom.ASTNode;

public class InjectionPoint {
	/**Node tiem*/
	private Node injector;
	/**Node bi tiem*/
	private Node injectee;
	/**Cau lenh inject*/
	private ASTNode statement;
	
	public InjectionPoint() {
	}
	public Node getInjectee() {
		return injectee;
	}
	public Node getInjector() {
		return injector;
	}
	public void setInjectee(Node injectee) {
		this.injectee = injectee;
	}
	public void setInjector(Node injector) {
		this.injector = injector;
	}
	public void setStatement(ASTNode statement) {
		this.statement = statement;
	}
	public ASTNode getStatement() {
		return statement;
	}
}
