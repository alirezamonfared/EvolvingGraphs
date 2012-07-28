/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
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
 * Implements the Foremost algorithm, which returns the journeys with the minimum arrival time
 */
public class ForemostAlgorithm implements Algorithm{
	
	private final Integer INFINITY = Integer.MAX_VALUE;
	private List<ForemostElement> heapElements;
	
	public List<Journey> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}
	
	/**
	 * Calculates Foremost Journey from s to all other nodes.
	 * 
	 * @param graph Evolving graph
	 * @param s Initial node
	 * @param initTime Initial time
	 */
	public List<Journey> calculate(EvolvingGraph graph, Node s, int initTime) {
		int n = graph.getNumOfNodes();
		List<Journey> foremostJourneys = new ArrayList<Journey>(n);
		PriorityQueue<ForemostElement> heap = new PriorityQueue<ForemostElement>();
		heapElements = new ArrayList<ForemostElement>();
		
		for (int i = 0; i < n; i++){
			ForemostElement element = new ForemostElement(graph.getNode(i), INFINITY);
			heap.add(element);
			heapElements.add(element);
			foremostJourneys.add(new Journey());
		}
		
		ForemostElement elementS = getForemostElement(s);
		heap.remove(elementS);
		elementS.setValue(initTime);
		heap.add(elementS);
		foremostJourneys.get(s.getId()).setStartTime(initTime);
		
		while (heap.isEmpty() == false){
			ForemostElement elementU = heap.remove();
			
			if (elementU.getValue() == INFINITY) break;
			Node u = (Node) elementU.getObject();
			
			for(Edge edge : u.getAdj()){
				Node v = edge.getV();
				ForemostElement elementV = getForemostElement(v);
				Integer t = edge.getNextDeparture(elementU.getValue());
				if (t != null) {
					if (t + edge.getCost() < elementV.getValue()) {
						heap.remove(elementV);
						elementV.setValue(t + edge.getCost());
						Journey journey = new Journey(foremostJourneys.get(u.getId()), edge, t);
						foremostJourneys.remove(v.getId());
						foremostJourneys.add(v.getId(), journey);
						foremostJourneys.get(v.getId()).setStartTime(initTime);
						heap.add(elementV);
					}
				}
			}
		}
		
		return foremostJourneys;
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
