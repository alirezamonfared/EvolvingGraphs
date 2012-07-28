/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import br.usp.ime.algorithms.Algorithm;
import br.usp.ime.algorithms.FastestAlgorithm;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.graphreader.DefaultGraphReader;
import br.usp.ime.graphreader.GraphReader;

public class FastestAlgorithmTest {

	@Test
	public void testInputTeste1(){
		GraphReader reader = new DefaultGraphReader("input/teste1.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new FastestAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			
			Journey from0to0 = journeys.get(eg.getNode("0").getId());
			Journey from0to1 = journeys.get(eg.getNode("1").getId());
			
			assertNotNull(from0to0);
			assertNotNull(from0to1);
			assertEquals((Integer) (-1), from0to0.getTransitTime());
			assertEquals((Integer) 0, from0to1.getTransitTime());
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
			Algorithm algorithm = new FastestAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("5").getId());
			assertEquals((Integer) 1, journey.getTransitTime());
			journey = journeys.get(eg.getNode("6").getId());
			assertEquals((Integer) 4, journey.getTransitTime());
			journey = journeys.get(eg.getNode("7").getId());
			assertEquals((Integer) 3, journey.getTransitTime());
			journey = journeys.get(eg.getNode("8").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("9").getId());
			assertEquals((Integer) 4, journey.getTransitTime());
			journey = journeys.get(eg.getNode("10").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 1, journey.getTransitTime());
			journey = journeys.get(eg.getNode("11").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 1, journey.getTransitTime());
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
			Algorithm algorithm = new FastestAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 0, journey.getTransitTime());
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
			Algorithm algorithm = new FastestAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("0"));
			
			Journey journey = journeys.get(eg.getNode("1").getId());
			assertEquals((Integer) 1, journey.getTransitTime());
			journey = journeys.get(eg.getNode("2").getId());
			assertEquals((Integer) 1, journey.getTransitTime());
			journey = journeys.get(eg.getNode("3").getId());
			assertEquals((Integer) 3, journey.getTransitTime());
			journey = journeys.get(eg.getNode("4").getId());
			assertEquals((Integer) 1, journey.getTransitTime());
			journey = journeys.get(eg.getNode("5").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 2, journey.getTransitTime());
			journey = journeys.get(eg.getNode("6").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 3, journey.getTransitTime());
			journey = journeys.get(eg.getNode("7").getId());
			//Jornada nao foremost:
			assertEquals((Integer) 4, journey.getTransitTime());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

}
