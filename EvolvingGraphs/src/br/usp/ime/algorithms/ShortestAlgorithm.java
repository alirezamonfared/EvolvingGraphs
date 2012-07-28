/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

/**
 * Implements the Shortest Journey algorithm, which returns the journeys with the least number of hops.
 */
public class ShortestAlgorithm implements Algorithm {

	Vector<Edge> eMin;
	Vector<Integer> tMin;
	
	/**
	 * Given the journeys with k hops, calculates journeys with k+1 hops
	 * @param graph
	 * @param tLBD Lower Bound on Departure from each node
	 * @return
	 */
	private boolean edgeAndScheduleSelection(EvolvingGraph graph, Vector<Integer> tLBD){
		Vector<Integer> arrivalTime = new Vector<Integer>();
		boolean improved = false;
		
		eMin = new Vector<Edge>(graph.getNumOfNodes());
		tMin = new Vector<Integer>(graph.getNumOfNodes());
		
		for (int i = 0; i < graph.getNumOfNodes(); i++){
			eMin.add(i, null);
			tMin.add(i, Integer.MAX_VALUE);
			arrivalTime.add(i, tLBD.get(i));
		}
		for (Node node : graph.getNodes()){
			if (tLBD.get(node.getId()) != Integer.MAX_VALUE){
				for (Edge edge : node.getAdj()){
					Integer newTime = edge.getNextDeparture(tLBD.get(node.getId()));
					if (newTime != null) {
						Node v = edge.getV();
						if (newTime + edge.getCost() < arrivalTime.get(v.getId())){
							eMin.set(v.getId(), edge);
							tMin.set(v.getId(), newTime);
							arrivalTime.set(v.getId(), newTime + edge.getCost());
							improved = true;
						}
					}
				}
			}
		}
		return improved;
	}
	
	//@Override
	public List<Journey> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}
	
	/**
	 * Calculates Shortest Journey from s to all other nodes.
	 * 
	 * @param graph Evolving graph
	 * @param s Initial node
	 * @param initTime Initial time
	 */
	public List<Journey> calculate(EvolvingGraph graph, Node s, int initTime) {
		List<Journey> shortestJourney = new ArrayList<Journey>();
		List<Journey> currentJourney = new ArrayList<Journey>();
		Vector<Integer> tLBD = new Vector<Integer>(graph.getNumOfNodes());
		
		for (int i = 0; i < graph.getNumOfNodes(); i++){
			tLBD.add(i, Integer.MAX_VALUE);
			currentJourney.add(i, new Journey());
			shortestJourney.add(i, null);
		}
		tLBD.set(s.getId(), initTime);
		currentJourney.set(s.getId(), new Journey(initTime));
		shortestJourney.set(s.getId(), new Journey(initTime));
		
		int numSteps = 0;
		int unreachableNodes = graph.getNumOfNodes() - 1;
		
		while (numSteps < graph.getNumOfNodes() && unreachableNodes > 0){
			List<Journey> lastJourney = new ArrayList<Journey>();
			if (edgeAndScheduleSelection(graph, tLBD) == false)
				break;
			
			for (int i = 0; i < graph.getNumOfNodes(); i++){
				lastJourney.addAll(currentJourney);
			}
			
			for (Node node : graph.getNodes()){
				Edge edge = eMin.get(node.getId());
				if (edge != null){
					int uId = edge.getU().getId();
					int vId = edge.getV().getId();
					int t = tMin.get(vId);
					Journey journey = new Journey(lastJourney.get(uId), edge, t);
					journey.setStartTime(initTime);
					currentJourney.set(vId, journey);
					if (tLBD.get(vId) == Integer.MAX_VALUE){
						shortestJourney.set(vId, journey);
						unreachableNodes--;
					}
					tLBD.set(vId, t + edge.getCost());
				}
			}
			numSteps = numSteps + 1;
		}
		
		return shortestJourney;
	}

	public int getOptimizedParameter (Journey j) {
		return j.getSize();
	}
	
	public int getType() {
		return 2;
	}
}
