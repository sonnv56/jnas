package com.fit.process.jsf;

import java.util.List;

import com.fit.loader.tree.Condition;
import com.fit.loader.tree.Search;
import com.fit.object.Node;

public class JsfUtils {
	/**
	 * Duyet cay de tim kiem nhung Node thoa man dieu kien
	 * 
	 * @param n
	 *            root cay can duyet
	 * @param condition
	 *            dieu kien
	 * @param relativePath
	 *            duong dan tuong doi trong cong nghe JSF cua Node can tim. Cac
	 *            thanh phan phan cach boi dau gach cheo ("/")
	 * 
	 * @return
	 */
	public static List<Node> searchNode(Node n, Condition condition, String relativePath) {
		final String REPLACEMENT = "";

		String url = ParamaterWebConfig.URL_PATTERN.replace("*", "");
		if (url.startsWith("/"))
			url = url.substring(1);

		relativePath = relativePath.replace(url, REPLACEMENT);

		return Search.searchNode(n, condition, relativePath);
	}
}
