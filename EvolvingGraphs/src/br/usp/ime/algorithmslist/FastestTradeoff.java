/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithmslist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.usp.ime.algorithms.Algorithm;
import br.usp.ime.algorithms.ForemostAlgorithm;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

/**
 * Implements the Fastest tradeoff algorithm, which returns all optimal journeys between 
 * the fastest and foremost.
 *
 */
public class FastestTradeoff implements AlgorithmList {

	final Integer INFINITY = Integer.MAX_VALUE;
	List<List<Journey>> pareto = new LinkedList<List<Journey>> ();
	
	public List<List<Journey>> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}

	public List<List<Journey>> calculate(EvolvingGraph graph, Node s, int initTime) {
		List<Journey> fastest = new ArrayList<Journey>();
		int departure = initTime;
		int n = graph.getNumOfNodes();
		List <Integer> td = new ArrayList<Integer>(n);
		List <Integer> time = new ArrayList<Integer>(n);
		
		for (int i = 0; i < n; i++){
			td.add(INFINITY);
			time.add(INFINITY);
			pareto.add(i, new LinkedList<Journey>());
		}
		//The journey from s to s has transit time 0
		td.set(s.getId(), 0);
		time.set(s.getId(), 0);
		
		while (departure >= 0 && departure < INFINITY){
		  System.out.println (departure);
			//Delta begins with the next event in s
			int delta = s.nextConnection(departure) - departure;
			Algorithm foremost = new ForemostAlgorithm();
			List<Journey> journeys = foremost.calculate(graph, s, departure);
			
			/* Adds the journeys to the pareto list if they belong there*/
			for(int i = 0; i < n; i++) {
			  Journey j = journeys.get(i);
			  LinkedList<Journey> remove = new LinkedList<Journey> ();
			  boolean add = true;
			  
			  if (j.getSize() == 0)
			    continue;
			  
			  for(Journey p: pareto.get(i)) {
			    if (j.getArrivalDate() == p.getArrivalDate()) {
			      if (j.getTransitTime() < p.getTransitTime())
			        remove.add(p);
			      else
			        add = false;
			    }
			    if (j.getTransitTime() == p.getTransitTime()) {
			      if (j.getArrivalDate() < p.getArrivalDate())
			        remove.add(p);
			      else
			        add = false;
			    }
			  }
			  if (add)
			    pareto.get(i).add(j);
			  for(Journey p: remove)
			    pareto.get(i).remove(p);
			}
			
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
			
		return pareto;
	}

	public List<List<Journey>> getPareto() {
		return pareto;
	}

	public void setPareto(List<List<Journey>> pareto) {
		this.pareto = pareto;
	}
	
}
