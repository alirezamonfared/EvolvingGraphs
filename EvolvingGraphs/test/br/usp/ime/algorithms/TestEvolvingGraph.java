/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Schedule;
import br.usp.ime.graphreader.GraphReader;
import br.usp.ime.graphreader.ONEGraphReader;

public class TestEvolvingGraph {

	@Test
	public void testGetEdge() {
		EvolvingGraph eg = new EvolvingGraph (3);
		eg.createEdge(0, 1, new Schedule(), 0);
		eg.createEdge(0, 2, new Schedule(), 0);
		eg.createEdge(1, 0, new Schedule(), 0);
		eg.createEdge(2, 0, new Schedule(), 0);
		eg.createEdge(1, 2, new Schedule(), 0);
		eg.createEdge(2, 1, new Schedule(), 0);
		Edge e = eg.getEdge(1, 0);
		
		assertEquals(1, e.getU().getId());
		assertEquals(0, e.getV().getId());
	}
	
	@Test
	public void testPeriodLength1() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 7, 9, 0);
		assertEquals(3, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength2() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 7, 9, 0);
		eg.createEdge(0, 1, 10, 12, 0);
		eg.createEdge(0, 1, 13, 15, 0);
		assertEquals(3, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength3() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 7, 9, 0);
		eg.createEdge(0, 1, 10, 12, 0);
		eg.createEdge(0, 1, 13, 15, 0);
		eg.createEdge(0, 1, 16, 18, 0);
		assertEquals(9, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength4() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 7, 9, 0);
		eg.createEdge(0, 1, 10, 12, 0);
		eg.createEdge(0, 1, 13, 15, 0);
		eg.createEdge(0, 1, 16, 17, 0);
		assertEquals(0, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength5() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 9, 10, 0);
		eg.createEdge(0, 1, 11, 13, 0);
		eg.createEdge(0, 1, 14, 16, 0);
		eg.createEdge(0, 1, 19, 20, 0);
		assertEquals(10, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength6() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		eg.createEdge(0, 1, 4, 6, 0);
		eg.createEdge(0, 1, 9, 10, 0);
		eg.createEdge(0, 1, 11, 14, 0);
		eg.createEdge(0, 1, 15, 17, 0);
		eg.createEdge(0, 1, 18, 20, 0);
		eg.createEdge(0, 1, 23, 24, 0);
		eg.createEdge(0, 1, 25, 28, 0);
		eg.createEdge(0, 1, 29, 31, 0);
		eg.createEdge(0, 1, 32, 34, 0);
		eg.createEdge(0, 1, 37, 38, 0);
		eg.createEdge(0, 1, 39, 42, 0);
		assertEquals(14, eg.getEdge(0, 1).periodLength(0));
	}
	
	
	@Test
	public void testPeriodLength7() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 1, 3, 0);
		assertEquals(0, eg.getEdge(0, 1).periodLength(0));
	}
	
	@Test
	public void testPeriodLength8() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 5, 8, 0);
		eg.createEdge(0, 1, 9, 11, 0);
		eg.createEdge(0, 1, 12, 14, 0);
		assertEquals(3, eg.getEdge(0, 1).periodLength(1));
	}
	
	@Test
	public void testPeriodLength9() {
		EvolvingGraph eg = new EvolvingGraph ();
		eg.addNode("0");
		eg.addNode("1");
		eg.createEdge(0, 1, 2, 5, 0);
		eg.createEdge(0, 1, 10, 18, 0);
		eg.createEdge(0, 1, 20, 21, 0);
		eg.createEdge(0, 1, 22, 24, 0);
		eg.createEdge(0, 1, 27, 28, 0);
		eg.createEdge(0, 1, 29, 31, 0);
		eg.createEdge(0, 1, 34, 35, 0);
		assertEquals(7, eg.getEdge(0, 1).periodLength(3));
	}
	
	@Test
	public void testRemoveJourney () {
		GraphReader r = new ONEGraphReader ("input/testeONE1.in", 6, 20);
		EvolvingGraph eg;
		try {
			eg = r.readEvolvingGraph();
			Algorithm alg = new ForemostAlgorithm();
			List<Journey> journeys = alg.calculate(eg, eg.getNode(0));
			Journey before = journeys.get(4);
			
			eg.removeJourney(before);
			journeys = alg.calculate(eg, eg.getNode(0));
			assertTrue(before.getArrivalDate() != journeys.get(5).getArrivalDate());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInputRead1 () {
		GraphReader r = new ONEGraphReader ("input/testeONE1.in", 6, 20);
		EvolvingGraph eg;
		try {
			eg = r.readEvolvingGraph();
			assertEquals(6, eg.getNumOfNodes());
			assertEquals(10, eg.getNumOfEdges());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInputRead2 () {
		GraphReader r = new ONEGraphReader ("input/testeONE2.in", 126, 20000);
		EvolvingGraph eg;
		try {
			eg = r.readEvolvingGraph();
			assertEquals(126, eg.getNumOfNodes());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
		
	}
	
//	@Test
//	public void testConnected1 () {
//		GraphReader r = new DefaultGraphReader ("input/teste1.in");
//		EvolvingGraph eg;
//		try {
//			eg = r.readEvolvingGraph();
//			assertEquals(-1, eg.maximumConnectedTime());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testConnected2 () {
//		GraphReader r = new DefaultGraphReader ("input/teste2.in");
//		EvolvingGraph eg;
//		try {
//			eg = r.readEvolvingGraph();
//			assertEquals(-1, eg.maximumConnectedTime());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testConnected3 () {
//		GraphReader r = new DefaultGraphReader ("input/teste3.in");
//		EvolvingGraph eg;
//		try {
//			eg = r.readEvolvingGraph();
//			assertEquals(-1, eg.maximumConnectedTime());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void testConnected4 () {
//		GraphReader r = new DefaultGraphReader ("input/teste4.in");
//		EvolvingGraph eg;
//		try {
//			eg = r.readEvolvingGraph();
//			assertEquals(-1, eg.maximumConnectedTime());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testConnected5 () {
//		GraphReader r = new DefaultGraphReader ("input/teste6.in");
//		EvolvingGraph eg;
//		try {
//			eg = r.readEvolvingGraph();
//			assertEquals(3, eg.maximumConnectedTime());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
