/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.graphreader.DefaultGraphReader;
import br.usp.ime.graphreader.GraphReader;
import br.usp.ime.graphreader.ONEGraphReader;


public class ForemostAlgorithmTest {
	@Test
	public void testEGWithOneNodeShouldReturnEmptyJourney(){
		EvolvingGraph graph = new EvolvingGraph(1);
		Algorithm algorithm = new ForemostAlgorithm();
		List<Journey> emptyJourneys = algorithm.calculate(graph, graph.getNode(0));
		
		assertEquals(1, emptyJourneys.size());
		assertEquals((Integer) 0, emptyJourneys.get(0).getSize());
	}
	
	@Test
	public void testInputTeste1(){
		GraphReader reader = new DefaultGraphReader("input/teste1.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new ForemostAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			
			Journey from0to0 = journeys.get(eg.getNode("0").getId());
			Journey from0to1 = journeys.get(eg.getNode("1").getId());
			
			assertEquals((Integer) 0, from0to0.getArrivalDate());
			assertEquals((Integer) 2, from0to1.getArrivalDate());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}	
	@Test
	public void testInputTeste2(){
		GraphReader reader = new DefaultGraphReader("input/teste2.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new ForemostAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 8, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("5").getId());
			assertEquals((Integer) 2, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("6").getId());
			assertEquals((Integer) 5, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("7").getId());
			assertEquals((Integer) 4, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("8").getId());
			assertEquals((Integer) 4, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("9").getId());
			assertEquals((Integer) 5, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("10").getId());
			assertEquals((Integer) 7, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("11").getId());
			assertEquals((Integer) 5, journey.getArrivalDate());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}	
	@Test
	public void testInputTeste3(){
		GraphReader reader = new DefaultGraphReader("input/teste3.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new ForemostAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 1, journey.getArrivalDate());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testInputTeste4(){
		GraphReader reader = new DefaultGraphReader("input/teste4.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			System.out.println(eg);
			Algorithm algorithm = new ForemostAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 3, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 2, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 4, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 4, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("5").getId());
			assertEquals((Integer) 5, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("6").getId());
			assertEquals((Integer) 10, journey.getArrivalDate());
			journey = journeys.get(eg.getNode("7").getId());
			assertEquals((Integer) 7, journey.getArrivalDate());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testInputFromONE3(){
		GraphReader reader = new ONEGraphReader ("input/testeONE3.in", 126, 5000);
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new ForemostAlgorithm();
			@SuppressWarnings("unused")
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode(0));
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
}
