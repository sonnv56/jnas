package com.fit.object;

import java.util.ArrayList;
import java.util.List;
/**
 * @author son
 * Thanh phan cua project noi chung
 * */
public abstract class Node {
	/**Duong dan tuyet doi*/
	private String path;
	/**Thanh phan <b>duoc goi</b>*/
	private List<Node> callees;
	/**Thanh phan <b>goi</b>*/
	private List<Node> callers;
	
	public Node() {
		this.callees = new ArrayList<>();
		this.callers = new ArrayList<>();
	}
	public Node(String path, List<Node> callees, List<Node> callers) {
		super();
		this.path = path;
		this.callees = callees;
		this.callers = callers;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
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
	 * @param callees the callees to set
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
	 * @param callers the callers to set
	 */
	public void setCallers(List<Node> callers) {
		this.callers = callers;
	}
	
	
}
