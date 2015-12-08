package com.fit.cia.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import com.fit.object.Node;





public class CallGraph {
	public static class MyEdge extends DefaultWeightedEdge {
	    @Override
	    public String toString() {
	        return "";
	    }
	}
	
	private ArrayList<Node> nodes;
	private ArrayList<Node> changeSetInNode;
	private ArrayList<String> changeSet;
	private ArrayList<String> core;
	private ArrayList<String> impactSet;
	
	private int depth = 1;
	public ListenableDirectedGraph<String, MyEdge> graph;
	

	public CallGraph() {
		nodes = new ArrayList<Node>();
		changeSet = new ArrayList<String>();
		changeSetInNode = new ArrayList<Node>();
		core = new ArrayList<String>();
		impactSet = new ArrayList<String>();
		
		graph = new ListenableDirectedGraph<String, MyEdge>(MyEdge.class);

		genarateGraph();
	}

	public CallGraph(List<Node> _nodes,List<Node> _changeSet) {
		nodes = (ArrayList<Node>)_nodes;
		changeSetInNode = (ArrayList<Node>)_changeSet;
		changeSet = new ArrayList<String>();
		for(Node node : changeSetInNode){
			changeSet.add(node.getId()+"");
		}
		core = new ArrayList<String>();
		impactSet = new ArrayList<String>();
		
		graph = new ListenableDirectedGraph<String, MyEdge>(MyEdge.class);

		genarateGraph();

		System.out.println("num of Nodes: " + graph.vertexSet().size());
		System.out.println("num of call: " + graph.edgeSet().size());
	}

	private void genarateGraph(){
		String [] nodestring = new String[nodes.size()];
		for (int i=0;i<nodes.size();i++){
			nodestring[i] = nodes.get(i).getId()+"";
			graph.addVertex(nodestring[i]);
		}
		for (int i=0;i<nodes.size();i++){
			if (nodes.get(i).getCallees().size() > 0){
				for (Node callee : nodes.get(i).getCallees()){
					for(int j=0;j<nodes.size();j++){
						if(nodestring[j].equals(callee.getId()+"")){
							graph.addEdge(nodestring[i], nodestring[j]);
						}
					}
				}
			}
		}
	}
	
	public int distance(Node n1, Node n2){
		AsUndirectedGraph<String, MyEdge> unDirectedGraph = new AsUndirectedGraph<String, MyEdge>(graph);
		List<MyEdge> path = DijkstraShortestPath.findPathBetween(unDirectedGraph, n1.getId()+"", n2.getId()+"");
		return path.size();
	}
	public int distance(String n1, String n2){
		AsUndirectedGraph<String, MyEdge> unDirectedGraph = new AsUndirectedGraph<String, MyEdge>(graph);
		List<MyEdge> path = DijkstraShortestPath.findPathBetween(unDirectedGraph, n1, n2);
		if(path != null)
			return path.size();
		return 10000;
	}
	
	public ArrayList<String> getNeighborSet(String vertex){
		ArrayList<String> neighbor = new ArrayList<String>();
		for(String v : graph.vertexSet()){
			if(distance(v, vertex) <= depth){
				neighbor.add(v);
			}
		}
		return neighbor;
	}
	public void caculateCore(){
		core.clear();
		ArrayList<String> tempSet = new ArrayList<String>();
		for(String v: graph.vertexSet()){
			if(!changeSet.contains(v)){
				ArrayList<String> neighborSet =  getNeighborSet(v);
				ArrayList<String> neighborCSSet = intersection(neighborSet, changeSet);
				if(neighborCSSet.size() > 1){
					tempSet.add(v);
				}
			}
		}
		core.addAll(changeSet);
		core.addAll(tempSet);
	}
	
	public void calculateImpactSet(){
		impactSet.clear();
		caculateCore();
		ArrayList<String> tempSet = new ArrayList<String>();
		impactSet.addAll(core);
		for(String m : graph.vertexSet()){
			if(!impactSet.contains(m)){
		    	ArrayList<String> callerSet = getCallerSet(m);
		    	ArrayList<String> calleeSet = getCalleeSet(m);
		    	if( intersection(impactSet, calleeSet).size() > 0 && intersection(impactSet, callerSet).size() > 0){
		    		tempSet.add(m);
		    	}
			}
		}
		impactSet.addAll(tempSet);
	}
	
	
    private <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
    	ArrayList<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    private Node getMethodNode(String label){
		for(Node m : nodes){
			if(label.equals(m.getId()+"")){
				return m;
			}
		}
		return null;
    }
    private ArrayList<String> getCallerSet(String method) {
    	ArrayList<String> callerSet = new ArrayList<String>();
    	Node node = getMethodNode(method);
    	ArrayList<Node> callerNode= new ArrayList<Node>();
    	callerNode.addAll(node.getCallers());
    	for(Node caller : callerNode){
    		callerSet.add(caller.getId()+"");
    	}
    	return callerSet;
    }
    private ArrayList<String> getCalleeSet(String method) {
    	ArrayList<String> calleeSet = new ArrayList<String>();
    	Node node = getMethodNode(method);
    	ArrayList<Node> calleeNode= new ArrayList<Node>();
    	calleeNode.addAll(node.getCallees());
    	for(Node callee : calleeNode){
    		calleeSet.add(callee.getId()+"");
    	}
    	return calleeSet;
    }
    public String changeSetToString(){
    	String ret = new String();
    	ret += changeSet.size() + " nodes in Change Set: \n";
    	for(String n : changeSet){
    		ret += (n + "\n") ;
    	}
    	return ret;
    }    
    public String impactSetToString(){
    	String ret = new String();
    	ret += impactSet.size() + " nodes in Impact Set: \n";
    	for(String n : impactSet){
    		ret += (n + "\n") ;
    	}
    	return ret;
    }
    public ArrayList<String> getImpactSet() {
		return impactSet;
	}
    public String coreToString(){
    	String ret = new String();
    	ret += core.size() + " nodes in Core: \n";
    	for(String n : core){
    		ret += (n + "\n") ;
    	}
    	return ret;
    }
    
    public void addToChangeSet(int nodeId){
    	for(Node node : nodes){
    		if(node.getId() == nodeId){
    	    	changeSet.add(node.getId()+"");
    	    	return;
    		}
    	}
    }

    public void createRandomChangeSet(){
		ArrayList<String> vertexes = new ArrayList<String>();
		vertexes.addAll(graph.vertexSet());
		int numOfMethod = vertexes.size();
		System.out.println("numOfMethod: " + numOfMethod );
//		addToChangeSet(430);
//		addToChangeSet(411);

    }
}
