package com.fit.process.jsf.object;

import com.fit.object.Node;

/**
 * Dai dien mot the <mangaged-bean> trong JSF config
 * 
 * @author DucAnh
 *
 */
public class ManagedBean {
	String content;
	Node JSFConfig;

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
