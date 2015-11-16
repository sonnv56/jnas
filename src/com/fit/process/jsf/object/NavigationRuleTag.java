package com.fit.process.jsf.object;

import org.w3c.dom.Element;

import com.fit.object.Node;

/**
 * Luu tru mot the navigation duoc dinh nghia trong JSF configs
 * 
 * @author DucAnh
 *
 */
public class NavigationRuleTag {
	Node JSFConfig;
	Element content;

	public Element getContent() {
		return content;
	}

	public Node getJSFConfig() {
		return JSFConfig;
	}

	public void setContent(Element content) {
		preprocess(content);
		this.content = content;
	}

	public void setJSFConfig(Node jSFConfig) {
		JSFConfig = jSFConfig;
	}

	private void preprocess(Element content) {
	}
}
