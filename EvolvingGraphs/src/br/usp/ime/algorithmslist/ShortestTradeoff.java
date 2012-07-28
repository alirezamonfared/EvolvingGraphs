/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithmslist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;
import br.usp.ime.graphreader.DefaultGraphReader;
import br.usp.ime.graphreader.GraphReader;

/**
 * Implements the Shortest tradeoff algorithm, which returns all optimal journeys between 
 * the shortest and foremost.
 *
 */
public class ShortestTradeoff implements AlgorithmList {
	
	Vector<Edge> eMin;
	Vector<Integer> tMin;
	
	public List<List <Journey> > pareto = new LinkedList < List < Journey> >();
	
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
						if ( newTime + edge.getCost() < arrivalTime.get(v.getId())){
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
	

	public List<List<Journey>> calculate(EvolvingGraph graph, Node s) {
		return calculate(graph, s, 0);
	}
	
	public List<List<Journey>> calculate(EvolvingGraph graph, Node s, int initTime) {
		List<Journey> currentJourney = new ArrayList<Journey>();
		Vector<Integer> tLBD = new Vector<Integer>(graph.getNumOfNodes());
		
		for (int i = 0; i < graph.getNumOfNodes(); i++){
			tLBD.add(i, Integer.MAX_VALUE);
			currentJourney.add(i, new Journey());
			pareto.add(i, new LinkedList<Journey>());
		}
		tLBD.set(s.getId(), initTime);
		currentJourney.set(s.getId(), new Journey());
		
		int numSteps = 0;
		
		while (numSteps < graph.getNumOfNodes()){
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
					pareto.get(vId).add(journey);			
					currentJourney.set(vId, journey);
					tLBD.set(vId, t + edge.getCost());
				}
			}
			numSteps = numSteps + 1;
		}
		
		/*int sum = 0, dif = 0, fm = 0, sh = 0, num = 0;

		System.out.println (s.getId() + ":");
		
		for (List<Journey> j: pareto) {
		  if (j.size() != 0) {
		    dif += j.get(0).getSize() - j.get(j.size()-1).getSize();
		    sh += j.get(0).getSize();
		    fm += j.get(j.size()-1).getSize();
		    sum += j.size();
		    num++;
		  }
		}
		System.out.println ("media: " + (1.0*sum)/(1.0*num) + " fm: " + (1.0*fm)/(1.0*num) + " sh: " + (1.0*sh)/(1.0*num) + " dif: " + (1.0*dif)/(1.0*num));*/
		return pareto;
	}

	public List<List<Journey>> getPareto() {
		return pareto;
	}

	public void setPareto(List<List<Journey>> pareto) {
		this.pareto = pareto;
	}
	
	public static void main(String args[]) throws FileNotFoundException{
		File shortest = new File("pontos.txt");
		GraphReader reader = new DefaultGraphReader("input/teste9.in");
		EvolvingGraph eg = reader.readEvolvingGraph();

		PrintStream saida = null;
		try {
			saida = new PrintStream(shortest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setOut(saida);
		AlgorithmList algorithm = new ShortestTradeoff();
		algorithm.calculate(eg,eg.getNode(0));
		List<List<Journey>> pareto = algorithm.getPareto();
		int j = 1;
		for(int i = 0; i < pareto.get(j).size(); i++){
			System.out.println(pareto.get(j).get(i).getSize() + " " + pareto.get(j).get(i).getArrivalDate());
		}
		
		System.out.println("Saída no arquivo pontos.txt");
		
	}

}
