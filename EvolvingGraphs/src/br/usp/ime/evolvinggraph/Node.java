/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Node structure
 */
public class Node {
	int id;
	String name;
	LinkedList<Edge> adj;
	LinkedList<Integer> nodeSchedule;

	/**
	 * @param id index of the node in the EG node list.
	 */
	public Node(int id) {
		this.id = id;
		this.name = "" + id;
		this.adj = new LinkedList<Edge>();
		this.nodeSchedule = new LinkedList<Integer>();
	}
	
	/**
	 * Creates a new node with a name and an id
	 * @param name Name of the new node
	 * @param id Id of the new node
	 */
	public Node(String name, int id) {
		this.id = id;
		this.name = name;
		this.adj = new LinkedList<Edge>();
		this.nodeSchedule = new LinkedList<Integer>();
	}
	
	/**
	 * Adds a new edge to this node's adjacency list
	 * 
	 * @param v Destination node
	 * @param sched Edge's presence schedule
	 * @param cost  Edge cost
	 * @return New Edge
	 */
	public Edge add(Node v, Schedule sched, int cost){
		Edge e = new Edge(this, v,sched,cost);
		this.adj.add(e);
		return e;
	}
	
	/**
	 * Adds a new connection interval to an edge of this node's adjacency list. If the
	 * edge does not exist, creates it.
	 * 
	 * @param v Destination node
	 * @param beg Connection interval start time
	 * @param end Connection interval end time
	 * @param cost Edge cost
	 * @return New Edge
	 */
	public Edge add(Node v, int beg, int end, int cost) {
		for (Edge e: adj) {
			if (e.getV().getId() == v.getId()) {
				e.getSchedule().add(beg, end);
				return e;
			}
		}
		Schedule s = new Schedule ();
		s.add(beg, end);
		return add(v, s, cost);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getName () {
		return name;
	}

	/**
	 * @return list of edges departing from this node
	 */
	public LinkedList<Edge> getAdj() {
		return adj;
	}

	/**
	 * Removes all edges leaving this node at time t.
	 * Used to avoid conflict in simulations by OneIntegration.
	 * @param v
	 * @param t
	 */
	public void removeTime(Node v, int t) {
		ArrayList<Edge> toRemove = new ArrayList<Edge>();
		for (Edge edge : adj){
			Schedule s = edge.getSchedule();
			if (edge.getV() == v)
				s.remove(t, t+1);
			else 
				s.remove(t-1,t+2);
			if (s.isEmpty())
				toRemove.add(edge);
			
			Edge inv = edge.getInv();
			if (inv != null){
				s = inv.getSchedule();
				s.remove(t-1, t+2);
				if (s.isEmpty()){
					inv.getU().getAdj().remove(inv);
				}
			}
		}
		for (Edge edge : toRemove){
			adj.remove(edge);
		}
	}

	/**
	 * @param arrival
	 * @return next connection after arrival.
	 */
	public int nextConnection(int arrival) {
		int conn = Integer.MAX_VALUE;
		for (Edge edge : adj){
			int dep = edge.getNextEvent(arrival);
			if (conn > dep)
				conn = dep;
		}
		return conn;
	}
}
