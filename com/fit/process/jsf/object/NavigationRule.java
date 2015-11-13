package com.fit.process.jsf.object;

import com.fit.object.Node;

public class NavigationRule {
	Node JSFConfig;
	String content;

	public String getContent() {
		return content;
	}

	public Node getJSFConfig() {
		return JSFConfig;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setJSFConfig(Node jSFConfig) {
		JSFConfig = jSFConfig;
	}
}
