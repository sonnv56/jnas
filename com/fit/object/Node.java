package com.fit.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author son Thanh phan cua project noi chung
 */
public abstract class Node {
	/** Duong dan tuyet doi */
	private String path;
	/** Thanh phan <b>duoc goi</b> */
	private List<Node> callees;
	/** Thanh phan <b>goi</b> */
	private List<Node> callers;
	/** Cac thanh phan ben trong thanh phan hien tai */
	private List<Node> children;
	/** Thanh phan chua thanh phan hien tai */
	private Node parent;
	/** ID duy nhat cua thanh phan hien tai */
	private int id;

	public Node() {
		this.callees = new ArrayList<>();
		this.callers = new ArrayList<>();
		this.children = new ArrayList<>();
	}

	public Node(String path, List<Node> callees, List<Node> callers) {
		super();
		this.path = path;
		this.callees = callees;
		this.callers = callers;
		this.children = new ArrayList<>();
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the callees
	 */
	public List<Node> getCallees() {
		return callees;
	}

	/**
	 * @param callees
	 *            the callees to set
	 */
	public void setCallees(List<Node> callees) {
		this.callees = callees;
	}

	/**
	 * @return the callers
	 */
	public List<Node> getCallers() {
		return callers;
	}

	/**
	 * @param callers
	 *            the callers to set
	 */
	public void setCallers(List<Node> callers) {
		this.callers = callers;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @return the callers
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Phan tich duong dan tuyet doi de lay ten file
	 * 
	 * @param path
	 * @return
	 */
	public String getNodeName() {
		final String DELIMITER_BETWEEN_COMPOPNENT_TYPE1 = "\\";
		final String DELIMITER_BETWEEN_COMPOPNENT_TYPE2 = "/";
		String tmpPath = path.replace(DELIMITER_BETWEEN_COMPOPNENT_TYPE1, DELIMITER_BETWEEN_COMPOPNENT_TYPE2);
		return tmpPath.substring(tmpPath.lastIndexOf(DELIMITER_BETWEEN_COMPOPNENT_TYPE2) + 1, tmpPath.length());
	}
}
