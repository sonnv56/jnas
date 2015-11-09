package com.fit.process.jsf;

import java.util.ArrayList;
import java.util.List;

import com.fit.object.Node;

/**
 * Phan tich faces-config.xml de lay Managed Bean Node
 * 
 * @author DucAnh
 *
 */
public class FacesConfigParser implements IFacesConfigParser {
	private String configPath;
	private List<Node> getListManagedBeanNodes;

	public static void main(String[] args) {
		String configPath = "C://Users//Nga//Documents//FacesServletTest//web//WEB-INF//faces-config.xml";
		FacesConfigParser parser = new FacesConfigParser(configPath);

	}

	public FacesConfigParser(String configPath) {
		this.configPath = configPath;
		getListManagedBeanNodes = new ArrayList<Node>();

		parse();
	}

	@Override
	public void parse() {
		
	}

	public List<Node> getGetListManagedBeanNodes() {
		return getListManagedBeanNodes;
	}
}
