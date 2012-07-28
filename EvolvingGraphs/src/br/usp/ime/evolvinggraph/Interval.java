/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

/**
 * Integer interval for use in Schedule.
 * Range of interval: [start,end[
 */
public class Interval implements Comparable<Interval>{
	Integer start;
	Integer end;
	
	public Interval(Integer start, Integer end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean isDisjoint(Interval interval){
		return (this.end < interval.start || interval.end < this.start);
	}
	
	public boolean isEmpty(){
		return (start >= end); 
	}
	
	/**
	 * Intersection overrides current interval
	 * @param interval
	 */
	public void intersection(Interval interval){
		if (this.start < interval.start)
			this.start = interval.start;
		if (this.end > interval.end);
			this.end = interval.end;
	}
	
	/**
	 * Assumes the intervals are NOT disjoint
	 * @param interval
	 */
	public void union(Interval interval){
		if (this.start > interval.start)
			this.start = interval.start;
		if (this.end < interval.end)
			this.end = interval.end;
	}
	
	/**
	 * Subtracts the given interval from this interval.
	 * If the subtraction results in two disjoint ranges, one of them 
	 * will be returned. Else, an empty interval will be returned. 
	 * @param interval
	 * @return The second resultant interval. (or an empty one)
	 */
	public Interval subtract(Interval interval){
		Interval second = new Interval(0,-1);
		
		if (isDisjoint(interval) == false){
			if (this.start >= interval.start){
				this.start = interval.end;
			}
			else {
				second = new Interval(interval.end, this.end);
				this.end = interval.start;
			}
		}
		return second;
	}

	public int compareTo(Interval arg0) {
		return this.start - arg0.start;
	}

	public boolean contains(int time) {
		return (start <= time && time < end);
	}

	public Integer getEnd() {
		return end;
	}

	public Integer getStart() {
		return start;
	}
}
