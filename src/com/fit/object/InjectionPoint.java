package com.fit.object;

public class InjectionPoint {
	/**Node tiem*/
	private Node injector;
	/**Node bi tiem*/
	private Node injectee;
	/**Cau lenh inject*/
	private String statement;
	
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
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public String getStatement() {
		return statement;
	}
}
