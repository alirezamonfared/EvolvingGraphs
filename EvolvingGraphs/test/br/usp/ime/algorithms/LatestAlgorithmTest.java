package br.usp.ime.algorithms;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.graphreader.DefaultGraphReader;
import br.usp.ime.graphreader.GraphReader;


public class LatestAlgorithmTest {
	@Test
	public void testInputTeste4(){
		GraphReader reader = new DefaultGraphReader("input/teste4.in");
		EvolvingGraph eg;
		try {
			eg = reader.readEvolvingGraph();
			Algorithm algorithm = new LatestAlgorithm();
			List<Journey> journeys = algorithm.calculate(eg, eg.getNode("7"));
			assertTrue (journeys.get(eg.getNode("0").getId()).toString().equals("0 [7] 4 [10] 2 [11] 5 [12] 6 [13] 3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("1").getId()).toString().equals("1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("2").getId()).toString().equals("2 [11] 5 [12] 6 [13] 3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("3").getId()).toString().equals("3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("4").getId()).toString().equals("4 [10] 2 [11] 5 [12] 6 [13] 3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("5").getId()).toString().equals("5 [12] 6 [13] 3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("6").getId()).toString().equals("6 [13] 3 [14] 1 [15] 7"));
			assertTrue (journeys.get(eg.getNode("7").getId()).toString().equals(""));
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
}
