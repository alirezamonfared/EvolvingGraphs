/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import java.util.ArrayList;
import java.util.List;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

/**
 * Implements the Fastest algorithm, which returns the journeys with the minimum transit time.
 */
public class FastestAlgorithm implements Algorithm {

	final Integer INFINITY = Integer.MAX_VALUE;
	
	public List<Journey> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}

	/**
	 * Calculates Fastest Journey from s to all other nodes.
	 * 
	 * @param graph Evolving graph
	 * @param s Initial node
	 * @param initTime Initial time
	 */
	public List<Journey> calculate(EvolvingGraph graph, Node s, int initTime) {
		List<Journey> fastest = new ArrayList<Journey>();
		int departure = initTime;
		int n = graph.getNumOfNodes();
		List <Integer> td = new ArrayList<Integer>(n);
		List <Integer> time = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++){
			td.add(INFINITY);
			time.add(INFINITY);
		}
		//The journey from s to s has transit time 0
		td.set(s.getId(), 0);
		time.set(s.getId(), 0);
		
		while (departure >= 0 && departure < INFINITY){
			//Delta begins with the next event in s
			int delta = s.nextConnection(departure) - departure;
			Algorithm foremost = new ForemostAlgorithm();
			List<Journey> journeys = foremost.calculate(graph, s, departure);
			for (int i = 0; i < n; i++){
				int localDelta;
				int arrival = journeys.get(i).getArrivalDate();
				if (journeys.get(i).getSize() == 0)
					continue;
				journeys.get(i).tryToDelay();
				localDelta = journeys.get(i).getDeparture() - departure;

				if (journeys.get(i).getTransitTime() < time.get(i)){
					time.set(i, journeys.get(i).getTransitTime());
					td.set(i, journeys.get(i).getDeparture());
				}
				int t = graph.getNode(i).nextConnection(arrival);
				if (t != INFINITY && delta > t - arrival + localDelta)
					delta = t - arrival + localDelta;
				
			}
			departure += delta;
		}
		
		for (int i = 0; i < n; i++){
			Algorithm foremost = new ForemostAlgorithm();
			Journey journey = foremost.calculate(graph, s, td.get(i)).get(i);
			fastest.add(journey);
		}
			
		return fastest;
	}

	public int getType() {
		return 3;
	}
	
	public int getOptimizedParameter (Journey j) {
		return j.getTransitTime();
	}
	
}
