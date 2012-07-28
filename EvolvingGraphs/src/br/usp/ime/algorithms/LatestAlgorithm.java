/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;
import br.usp.ime.priorityqueueelement.ForemostElement;

/**
 * Implements the Latest Algorithm, which returns the journeys with the maximum departure time.
 * IMPORTANT: all other algorithms return a list of journeys from s to all other nodes. This algorithm
 * returns a list of journeys from all other nodes to s.
 */
public class LatestAlgorithm implements Algorithm{
	
	private final Integer INFINITY = Integer.MIN_VALUE + 1;
	private List<ForemostElement> heapElements;
	
	public List<Journey> calculate(EvolvingGraph graph, Node s) {
		int n = graph.getNumOfNodes();
		List<Journey> foremostJourneys = new ArrayList<Journey>(n);
		PriorityQueue<ForemostElement> heap = new PriorityQueue<ForemostElement>();
		heapElements = new ArrayList<ForemostElement>();
		
		for (int i = 0; i < n; i++){
			ForemostElement element = new ForemostElement(graph.getNode(i), 0);
			heap.add(element);
			heapElements.add(element);
			foremostJourneys.add(new Journey());
		}
		
		ForemostElement elementS = getForemostElement(s);
		heap.remove(elementS);
		elementS.setValue(INFINITY);
		heap.add(elementS);
		foremostJourneys.get(s.getId()).setStartTime(0);
		
		while (heap.isEmpty() == false){
			ForemostElement elementU = heap.remove();
			
			if (elementU.getValue() == 0) break;
			Node u = (Node) elementU.getObject();
			
			for(Edge edge : u.getAdj()){
				Node v = edge.getV();
				ForemostElement elementV = getForemostElement(v);
				Integer t = edge.getPreviousDeparture(-1*elementU.getValue());
				if (t != null) {
					if (-t < elementV.getValue()) {
						heap.remove(elementV);
						elementV.setValue(-t);
						Journey journey = new Journey(edge.getInv(), t, foremostJourneys.get(u.getId()));
						foremostJourneys.remove(v.getId());
						foremostJourneys.add(v.getId(), journey);
						foremostJourneys.get(v.getId()).setStartTime(0);
						heap.add(elementV);
					}
				}
			}
		}
		
		return foremostJourneys;
	}
	
	/**
	 * Calculates Latest Journey from all other nodes to s.
	 */
	public List<Journey> calculate(EvolvingGraph graph, Node s, int initTime) {
		return calculate(graph, s);
	}

	private ForemostElement getForemostElement(Node node) {
		ForemostElement e = heapElements.get(node.getId());
		return e;
	}

	public int getOptimizedParameter (Journey j) {
		return j.getArrivalDate();
	}
	
	public int getType() {
		return 1;
	}
	
}
