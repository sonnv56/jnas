package com.fit.process.jsf.object;

import org.w3c.dom.Element;

import com.fit.object.Node;

/**
 * Dai dien mot the mangaged-bean trong JSF config
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanTag {
	Element content;
	Node JSFConfig;

	public Element getContent() {
		return content;
	}

	public Node getJSFConfig() {
		return JSFConfig;
	}

	public void setContent(Element content) {
		this.content = content;
	}

	public void setJSFConfig(Node jSFConfig) {
		JSFConfig = jSFConfig;
	}
}
