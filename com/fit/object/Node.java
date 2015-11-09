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
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}
}
