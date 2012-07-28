/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.usp.ime.algorithmslist.AlgorithmList;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

public class MiddleAlgorithm implements Algorithm {
	
	private char type;
	AlgorithmList algList;
	
	public MiddleAlgorithm(char type, AlgorithmList list){
		super();
		this.type = type;
		algList = list;
	}

	public List<Journey> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}
  
  public List<Journey> calculate(List<List<Journey>> pareto, char typ) {
    switch (typ) {
      case 'a':
        return middleHop (pareto);
      case 'b':
        return middleArrival (pareto);
      case 'c':
        return random (pareto);
      case 'd':
        return quarterHop (pareto);
      case 'e':
        return threeQuarterHop (pareto);
      case 'f':
        return middleTransitTime (pareto);
      case 'g':
        return middleArrival (pareto);
      case 'h':
        return random (pareto);
      case 'i':
        return quarterTransitTime (pareto);
      case 'j':
        return threeQuarterTransitTime (pareto);
      default:
        return null;
    }
  }
  
	public List<Journey> calculate(EvolvingGraph graph, Node s, int initTime) {
		List<List<Journey>> pareto = algList.calculate(graph, s, initTime);
		return calculate(pareto,type);
	}
	
	public List<Journey> middleHop (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (toV.get(0).getSize() + 
							 toV.get(toV.size()-1).getSize())/2;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getSize() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getSize() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getSize() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> middleTransitTime (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (toV.get(0).getTransitTime() + 
							 toV.get(toV.size()-1).getTransitTime())/2;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getTransitTime() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getTransitTime() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getTransitTime() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> middleArrival (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (toV.get(0).getArrivalDate() + 
						 	 toV.get(toV.size()-1).getArrivalDate())/2;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getArrivalDate() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getArrivalDate() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getArrivalDate() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> quarterHop (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (3*toV.get(0).getSize() + 
							 toV.get(toV.size()-1).getSize())/4;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getSize() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getSize() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getSize() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> threeQuarterHop (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (toV.get(0).getSize() + 
							 3*toV.get(toV.size()-1).getSize())/4;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getSize() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getSize() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getSize() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> quarterTransitTime (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (3*toV.get(0).getTransitTime() + 
							 toV.get(toV.size()-1).getTransitTime())/4;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getTransitTime() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getTransitTime() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getTransitTime() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> threeQuarterTransitTime (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  int ideal = (toV.get(0).getTransitTime() + 
							 3*toV.get(toV.size()-1).getTransitTime())/4;
				Journey best = toV.get(0);
				int bestValue = Math.abs(best.getTransitTime() - ideal);
				for (Journey actual : toV){
					if (Math.abs(actual.getTransitTime() - ideal) < bestValue){
						best = actual;
						bestValue = Math.abs(actual.getTransitTime() - ideal);
					}
				}
				middle.add(best);
			}
		}
		return middle;
	}
	
	public List<Journey> random (List<List<Journey>> pareto) {
	  List<Journey> middle = new ArrayList<Journey>();
	  for (List<Journey> toV : pareto){
	    if (toV.isEmpty()){
				middle.add(new Journey());
			}
			else {
			  Random generator = new Random();
				int pos = generator.nextInt(toV.size());
				middle.add(toV.get(pos));
			}
		}
		return middle;
	}
	
	/*The optimization in this journey is not a single parameter.
	 * Got to find a way to represent this in case you want to report on middle journeys.
	 */
	public int getOptimizedParameter (Journey j) {
		return -1;
	}
	
	public int getType() {
	  if (type > 'j')
	    return -1;
		return (type - 'a' + 4);
	}
}
