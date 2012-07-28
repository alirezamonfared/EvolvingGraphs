/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.evolvinggraph;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Ordered list of intervals the edges are active.
 */
public class Schedule {
	private SortedSet<Interval> sched;

	public Schedule(){
		sched = new TreeSet<Interval>();
	}
	
	/**
	 * Adds an interval to the end of this schedule
	 * 
	 * @param start Interval start
	 * @param end Interval end
	 */
	public void add(Integer start, Integer end){
		Interval newInterval = new Interval(start,end);
		List<Interval> affected = new LinkedList<Interval>();
		
		for (Interval currInterval : sched){
			if (!currInterval.isDisjoint(newInterval))
				affected.add(currInterval);
			if (currInterval.start > newInterval.end)
				break;
		}
		
		for (Interval interval : affected){
			sched.remove(interval);
			newInterval.union(interval);
		}
		sched.add(newInterval);
	}
	
	/**
	 * Removes an interval from this schedule
	 * 
	 * @param start Interval start
	 * @param end Interval end
	 */
	public void remove(Integer start, Integer end){
		Interval toRemove = new Interval(start,end);
		List<Interval> affected = new LinkedList<Interval>();
		
		for (Interval currInterval : sched){
			if (!currInterval.isDisjoint(toRemove))
				affected.add(currInterval);
			if (currInterval.start > toRemove.end)
				break;
		}
		
		for (Interval interval : affected){
			sched.remove(interval);
			Interval aux = interval.subtract(toRemove);
			if (interval.isEmpty() == false){
				sched.add(interval);
				if (aux.isEmpty() == false)
					sched.add(aux);
			}
		}
	}
	
	/** 
	 * @param time
	 * @return next instant after 'time' in which the connection is active.
	 */
	public Integer nextTime(int time){
		for (Interval interval : sched){
			if (interval.contains(time))
				return time;
			if (interval.getStart() > time)
				return interval.start;
		}
		return null;
	}
	
	/** 
	 * @param time
	 * @return previous instant before 'time' in which the connection is active.
	 */
	public Integer prevTime(int time) {
		Object [] sc =  sched.toArray();
		
		for(int i = sc.length - 1; i >= 0; i--) {
			Interval interval = (Interval) sc[i];
			if (interval.getEnd() <= time)
				return interval.getEnd() - 1;
			if (interval.getStart() < time)
				return time - 1;
		}
		return null;
		
	}
	
	/**
	 * @param time
	 * @return last instant before 'time' in which the connection is active.
	 */
	public Integer lastTime(int time){
		Integer last = null; 
		for (Interval interval : sched){
			if (interval.contains(time))
				return time;
			if (interval.start > time)
				return last;
			last = interval.end - 1;
		}
		return last;
	}
	
	public boolean isEmpty(){
		return sched.isEmpty();
	}

	/**
	 * @return Number of disjoint intervals.
	 */
	public Integer size(){
		return sched.size();
	}
	
	/**
	 * @return last connection instant.
	 */
	public Integer last(){
		return sched.last().end;
	}
	
	/**
	 * 
	 * @param time
	 * @return The interval in this schedule that contains the given time instant
	 */
	public Interval getIntervalContaining(Integer time){
		for (Interval interval : sched){
			if (interval.contains(time))
				return interval;
		}
		return null;
	}
	
	public String toString(){
		String str = "";
		for (Interval interval : sched){
			str += " [" + interval.start + "," + interval.end + "["; 
		}
		return str;
	}

	/**
	 * @param t
	 * @return true if the connection is active in t.
	 */
	public boolean contains(int t) {
		Interval i = getIntervalContaining(t);
		if (i == null)
			return false;
		return true;
	}

	/**
	 * @param arrival
	 * @return connection/disconnection after arrival
	 */
	public int getNextEvent(int arrival) {
		for (Interval i : sched){
			if (arrival < i.start) return i.start;
			if (arrival < i.end) return i.end;
		}
		return Integer.MAX_VALUE;
	}
	
	public SortedSet<Interval> getIntervals(){
		return sched;
	}

	/**
	 * 
	 * @param t Time instant
	 * @return The next time the edge is disconnected after the time instant given
	 */
	public int getNextDisconnection(int t) {
		for (Interval i : sched){
			if (t < i.end)
				return i.end;
		}
		return Integer.MAX_VALUE;
	}
}
