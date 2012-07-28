/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

import java.util.ArrayList;

/**
 * Structure for an Edge with presence schedule. 
 */
public class Edge {
	Node u;
	Node v;
	Schedule schedule;
	Edge inv;
	int cost;
	
	/**
	 * @param u Origin Node
	 * @param v Destination Node
	 * @param schedule Presence schedule of Edge
	 * @param cost Time needed to cross the edge
	 */
	public Edge(Node u, Node v, Schedule schedule, int cost) {
		this.u = u;
		this.v = v;
		this.schedule = schedule;
		this.cost = cost;
	}
	
	/**
	 * @param time
	 * @return Next instant after 'time' the edge is active. 
	 */
	public Integer getNextDeparture(int time) {
		return schedule.nextTime(time);
	}
	
	/**
	 * @param time
	 * @return Previous instant before 'time' the edge is active. 
	 */
	public Integer getPreviousDeparture(int time) {
		return schedule.prevTime(time);
	}
	
	/**
	 * @param time
	 * @return Interval containing first instant after 'time' 
	 * the edge is active. 
	 */
	public Interval getNextDepartureInterval(int time){
		Integer depart = getNextDeparture(time);
		if (depart == null) return null;
		Interval inter = schedule.getIntervalContaining(depart);
		return new Interval(depart, inter.end-1);
	}

	public Node getU() {
		return u;
	}

	public void setU(Node u) {
		this.u = u;
	}

	public Node getV() {
		return v;
	}

	public void setV(Node v) {
		this.v = v;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	/**
	 * @return inv Edge from v to u
	 */
	public Edge getInv() {
		return inv;
	}

	/**
	 * @param inv Edge from v to u
	 */
	public void setInv(Edge inv) {
		this.inv = inv;
	}
	
	public String toString(){
		String str = u.getId() + "-" + v.getId() + " " + schedule;
		return str;
	}

	/**
	 * @param arrival
	 * @return Next connection or disconnection after arrival 
	 */
	public int getNextEvent(int arrival) {
		return schedule.getNextEvent(arrival);
	}

	/**
	 * @param endTime
	 * @return last time before endTime the edge can be crossed.
	 */
	public Integer lastDepartureBefore(Integer endTime) {
		Integer departure; 
		endTime -= this.cost;
		departure = schedule.lastTime(endTime);
		return departure;
	}

	/**
	 * @param t
	 * @return first disconnection after time t
	 */
	public int getNextDisconnection(int t) {
		return schedule.getNextDisconnection(t);
	}
	
	/**
	 * Builds the connection sequence and calls periodLength (ignoredConnections, connSequence)
	 * 
	 * @param ignoredConnections Number of connections to be treated as warmup
	 * @return The edge's period length when ignoring the first ignoredConnections connections
	 */
	public int periodLength (int ignoredConnections) {
		ArrayList<Integer> sequence = buildSequence ();
		return periodLength(ignoredConnections, sequence);
	}

	/**
	 * This method returns the loop period of the edge when ignoring the first
	 * ignoredConnections encounters. The loop period is an integer t
	 * such that every connection repeats after t seconds. The algorithm is quadratic,
	 * using naive pattern searching. Maybe adapting an efficient pattern searching
	 * algorithm may generate a linear solution.
	 * 
	 * @param ignoredConnections Number of connections to be treated as warmup
	 * @param connSequence The edge's connection sequence returned by buildSequence
	 * @return The edge's period length when ignoring the first ignoredConnections connections
	 */
	public int periodLength (int ignoredConnections, ArrayList<Integer> connSequence) {
		int first = 2*ignoredConnections;
		int i, middle = (connSequence.size() - first)/2 + first - 1;
		for (i = middle; i > first; i--) {
			if (i%2 == 0)
				continue;
			int j, k = first;
			for (j = i + 1; j < connSequence.size(); j++) {
				if (connSequence.get(j) != connSequence.get(k))
					break;
				k = (k - first + 1)%(i - first + 1) + first;
			}
			if (j == connSequence.size() && k == first)
				break;
		}
		int sum = 0;
		if (i > first) {
			sum = 0;
			for (int j = first; j <= i; j++)
				sum += connSequence.get(j);
		}
		return sum;
	}
	
	/**
	 * Builds an array containing the sizes of all the up times and down times of the edge
	 * in chronological order
	 * @return the ArrayList containing the sequence
	 */
	public ArrayList<Integer> buildSequence() {
		ArrayList<Integer> sequence = new ArrayList<Integer> ();
		int lastTime = 0;
		for (Interval i: schedule.getIntervals()) {
			sequence.add(i.getStart() - lastTime);
			sequence.add(i.getEnd() - i.getStart());
			lastTime = i.getEnd();
		}
		return sequence;
	}
	
}
