/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

import java.util.ArrayList;

/**
 * A journey is a path with an crossing time for each
 * of its edges
 */
public class Journey {
	private ArrayList<Edge> edge;
	private ArrayList<Integer> time;
	private int size;
	private int startTime;
	
	/**
	 * Empty Journey
	 */
	public Journey() {
		edge = new ArrayList<Edge>();
		time = new ArrayList<Integer>();
		size = 0;
		startTime = -1;
	}
	
	/**
	 * Empty Journey with start time
	 */
	public Journey (int startTime) {
		edge = new ArrayList<Edge>();
		time = new ArrayList<Integer>();
		size = 0;
		this.startTime = startTime;
	}
	
	/**
	 * A copy of j
	 * @param j
	 */
	public Journey(Journey j) {
		edge = new ArrayList<Edge>(j.edge);
		time = new ArrayList<Integer>(j.time);
		size = j.size;
	}
	
	/**
	 * Copy of j, appending edge e at time t to it.
	 * @param j
	 * @param e
	 * @param t
	 */
	public Journey(Journey j, Edge e, int t){
		edge = new ArrayList<Edge>(j.edge);
		time = new ArrayList<Integer>(j.time);
		size = j.size;
		append(e, t);
	}
	
	/**
	 * Copy of j, appending edge e at time t to the start of it.
	 * @param j
	 * @param e
	 * @param t
	 */
	public Journey(Edge e, int t, Journey j){
		edge = new ArrayList<Edge>(j.edge);
		time = new ArrayList<Integer>(j.time);
		size = j.size;
		push(e, t);
	}

	/**
	 * Pushes Edge e in time t to the start of the journey
	 * @param e
	 * @param t
	 */
	private void push(Edge e, int t) {
		edge.add(0,e);
		time.add(0,t);
		size++;
	}

	public boolean isEmpty () {
		return (startTime == -1);
	}
	
	/**
	 * Appends Edge e in time t to the end of the journey
	 * @param e
	 * @param t
	 */
	public void append(Edge e, int t){
		edge.add(e);
		time.add(t);
		size++;
	}
	
	public String toString(){
		String str = "";
		int i;
		for (i = 0; i < size; i++){
			Edge e = edge.get(i);
			str += e.getU().getName() + " [" + time.get(i) + "] ";
		}
		i--;
		if (i >= 0)
			str += edge.get(i).getV().getName();
		return str;
	}

	/**
	 * @return Time the message arrives at the destination
	 */
	public Integer getArrivalDate() {
		if (startTime == -1) return -1;
		else if (size == 0) return startTime;
		return time.get(time.size()-1) + edge.get(edge.size()-1).getCost();
	}
	
	/**
	 * @return Time elapsed between departure and arrival
	 */
	public Integer getTransitTime(){
		if (time.size() == 0) return -1;
		return getArrivalDate() - time.get(0);
	}
	
	/**
	 * @return Number of hops in the journey
	 */
	public Integer getSize(){
		return size;
	}
	
	public ArrayList<Edge> getEdges () {
		return edge;
	}
	
	public Edge getEdge(int i){
		return edge.get(i);
	}
	
	public ArrayList<Integer> getTimes () {
		return time;
	}
	
	public Integer getTime(int i){
		return time.get(i);
	}

	/**
	 * @return Traversal time of the first edge
	 */
	public Integer getDeparture() {
		if (time.size() == 0) return 0;
		return time.get(0);
	}

	/**
	 * Tries to reduce the transit time by delaying all edges (except the last one)
	 * @return true if journey was delayed
	 */
	public boolean tryToDelay() {
		boolean delayed = false;
		for (int i = edge.size()-2; i >= 0; i--){
			 Integer newTime = edge.get(i).lastDepartureBefore(time.get(i+1));
			 if (newTime > time.get(i)){
				 time.set(i, newTime);
				 delayed = true;
			 }
		}
		return delayed;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getStartTime() {
		return startTime;
	}
}
