/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.priorityqueueelement;

/**
 * Used by the priority queue from ForemostAlgorithm.
 * Mantains for each journey the arrival time.
 */
public class ForemostElement implements Comparable<ForemostElement>{
	private Object object;
	/* Value is a positive number, or -1 in case of infinity. */
	private int value;
	
	public ForemostElement(Object object, int value) {
		this.object = object;
		this.value = value;
	}

	public int compareTo(ForemostElement o) {
		if (this.value == -1) return 1;
		if (o.value == -1) return -1;
		return this.value - o.getValue();
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
