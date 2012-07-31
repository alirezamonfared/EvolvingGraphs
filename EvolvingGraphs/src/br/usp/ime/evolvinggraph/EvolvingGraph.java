/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Main structure for Evolving Graph.
 * @author Cesar Gamboa Machado
 * @author Paulo Henrique Floriano 
 */
public class EvolvingGraph {
	private List<Node> nodes;
	private int edgesnum;
	private int maxTime;
	private HashMap<String, Integer> names;
	
	/**
	 * Sets a new evolving graph with the desired number of nodes
	 * 
	 * @param numOfNodes Number of nodes in the evolving graph
	 */
	public EvolvingGraph(int numOfNodes){
		names = new HashMap<String, Integer> ();
		nodes = new ArrayList<Node>();
		edgesnum = 0;
		maxTime = -1;
		for(int i = 0; i < numOfNodes; i++){
			nodes.add(new Node(i));
		}
	}
	
	
	/**
	 * Copy constructor
	 * @param eg 
	 */
	public EvolvingGraph(EvolvingGraph eg){
		nodes = new ArrayList<Node>();
		for (int i = 0 ;  i < eg.nodes.size() ; i++){
			Node N = new Node(eg.nodes.get(i));
			this.nodes.add(N);
		}
		this.edgesnum = eg.edgesnum;
		this.maxTime = eg.maxTime;
		names = new HashMap<String, Integer>(eg.names);
	}
	
	/**
	 * Builds an evolving graph, but doesn't add any nodes
	 */
	public EvolvingGraph(){
		names = new HashMap<String, Integer> ();
		nodes = new ArrayList<Node>();
		edgesnum = 0;
		maxTime = -1;
	}

	/**
	 * Adds a node with a given name to the evolving graph
	 * 
	 * @param name The name of the added node (must be unique)
	 * @return The added node's unique id
	 */
	public int addNode (String name) {
		int id = nodes.size();
		nodes.add(new Node (name, id));
		names.put(name, id);
		return id;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public Node getNode(int id){
		return nodes.get(id);
	}
	
	/**
	 * 
	 * @param name Name of the searched node
	 * @return The Node with the given name
	 */
	public Node getNode(String name) {
		Integer node = names.get(name);
		if (node == null)
			return null;
		return nodes.get(node);
	}
	
	/**
	 * 
	 * @param name Name of the searched node
	 * @return true if a node with the given name exists. false otherwise
	 */
	public boolean hasNode (String name) {
		Integer node = names.get(name);
		if (node == null)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @return The number of nodes in the evolving graph
	 */
	public int getNumOfNodes() {
		return nodes.size();
	}
	
	/**
	 * Creates a new edge
	 * 
	 * @param u Origin Node
	 * @param v Destination Node
	 * @param sched Edge's Presence Schedule
	 * @param cost Edge cost
	 * @return New Edge
	 */
	public Edge createEdge(int u, int v, Schedule sched, int cost) {
		edgesnum++;
		Edge e = nodes.get(u).add(this.nodes.get(v), sched, cost);
		for(Edge edge: nodes.get(v).getAdj()) {
			if (edge.getV().getId() == u) {
				e.setInv(edge);
				edge.setInv(e);
			}
		}
		return e;
	}
	
	/**
	 * Creates a new connection in the given time interval
	 * 
	 * @param u Origin Node
	 * @param v Destination Node
	 * @param beg Starting time
	 * @param end End time
	 * @param cost Edge cost
	 * @return New Edge
	 */
	public Edge createEdge(int u, int v, int beg, int end, int cost) {
		Edge t = getEdge(u,v);
		if (t == null)
			edgesnum++;
		Edge e = nodes.get(u).add(nodes.get(v), beg, end, cost);
		for(Edge edge: nodes.get(v).getAdj()) {
			if (edge.getV().getId() == u) {
				e.setInv(edge);
				edge.setInv(e);
			}
		}
		return e;
	}
	
	
	public void removeEdge(int u, int v, Edge e) {
		edgesnum--;
		//Edge e = nodes.get(u).add(this.nodes.get(v), sched, cost);
		nodes.get(u).remove(e);
		nodes.get(v).remove(e);
	}
	
	/**
	 * @param u Origin Node
	 * @param v Destination Node
	 * @return Edge from u to v
	 */
	public Edge getEdge (int u, int v) {
		Edge e = null;
		for (Iterator<Edge> iter = this.getNode(u).getAdj().iterator(); iter.hasNext();) {
			Edge element = (Edge) iter.next();
			if (element.getV().getId() == v) {
				e = element;
				break;
			}
		}
		return e;
	}
	
	/**
	 * 
	 * @return Number of edges in the evolving graph
	 */
	public int getNumOfEdges () {
		return this.edgesnum;
	}
	
	public String toString(){
		String str = this.getNumOfNodes() + " " + edgesnum + "\n" ;
		for (int i = 0; i < nodes.size(); i++){
			Node u = nodes.get(i);
			for (int j = 0; j < u.adj.size(); j++){
				Edge e = u.adj.get(j);
				str += u.getName() + " " + e.getV().getName() + " " + e.getCost() + " ";
				str +=  e.getSchedule().size() + e.getSchedule().toString();
				str += "\n";
			}
		}
		return str;
	}
	
	/**
	 * If e is from u to v, returns the edge from v to u
	 * 
	 * @param e Edge to get inverse
	 * @return Inverse edge
	 */
	public Edge getInverseEdge (Edge e) {
		return e.getInv();
	}
	
	/**
	 * Removes all edges used by journey j at the traversal
	 * time from the EG. (this is done in order to avoid conflicts in the 
	 * ONE simulator)  
	 * @param j Journey
	 */
	public void removeJourney(Journey j) {
		ArrayList<Edge> edges = j.getEdges();
		ArrayList<Integer> times = j.getTimes();
		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			int t = times.get(i);
			
			e.getU().removeTime(e.getV(),t);
			e.getV().removeTime(e.getU(),t);
		}
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getMaxTime() {
		return maxTime;
	}
	
//	/* TODO Deprecated */
//	public int maximumConnectedTime () {
//		ForemostAlgorithm fm = new ForemostAlgorithm ();
//		for (int t = 0; ; t++) {
//			for (Node n: nodes) {
//				List<Journey> result = fm.calculate (this, n, t);
//				for (Node m: nodes) {
//					if (result.get(m.getId()).isEmpty())
//						return (t - 1);
//				}
//			}
//		}
//	}

}
