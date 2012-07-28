/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.graphreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.MatchResult;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Schedule;

/**
 * Reader that uses One input. For information on the input format check 
 * http://grenoble.ime.usp.br/~paulo/dtn/formats.html
 * @author Paulo Henrique Floriano
 *
 */
public class ONEGraphReader implements GraphReader{

	private String filename;
	private int numOfNodes;
	private int simTime;
	private boolean startsFromOne;

	public ONEGraphReader (String filename, int numOfNodes, int simTime) {
		this.filename = filename;
		this.numOfNodes = numOfNodes;
		this.simTime = simTime;
		this.startsFromOne = false; //If this is set to true, node numbers in the ONE file are interpreted as 1,2...,N and are handled correctly in the reader with a -1 offset
	}

	/**
	 * Processes a line of the input, generating
	 * @param s
	 * @param eg
	 * @param openConnections
	 */
	
	private void processLine(Scanner s, EvolvingGraph eg, HashSet<Edge> openConnections) {
		Schedule sched, schedc;
		//s.findInLine("(\\d+).0 CONN (\\d+) (\\d+) (\\w+)");
		s.findInLine("(\\d+).00 CONN (\\d+) (\\d+) (\\w+)");
		MatchResult result = s.match();
		int time = Integer.parseInt((result.group(1)));
		int u = Integer.parseInt((result.group(2)));
		int v = Integer.parseInt((result.group(3)));
		if (this.startsFromOne){
			u--;
			v--;
		}
		String status = result.group(4);
		Edge uv = null, vu = null;
		uv = eg.getEdge(u, v);
		if (uv == null) {
			sched = new Schedule();
			sched.add(time, time+1);
			schedc = new Schedule();
			schedc.add(time, time+1);
			//Edge e1 = eg.createEdge(u, v, sched, 2);
			//Edge e2 = eg.createEdge(v, u, schedc, 2);
			Edge e1 = eg.createEdge(u, v, sched, 0);
			Edge e2 = eg.createEdge(v, u, schedc, 0);
			e1.setInv(e2);
			e2.setInv(e1);
			openConnections.add(eg.getEdge(u, v));
			openConnections.add(eg.getEdge(v, u));
		}
		else {
			vu = eg.getEdge(v, u);
			sched = uv.getSchedule();
			schedc = vu.getSchedule();
			if (status.equals("up")) {
				sched.add(time, time+1);
				schedc.add(time, time+1);
				openConnections.add(uv);
				openConnections.add(vu);
			}
			else {
				int ini = sched.last();
				openConnections.remove(uv);
				openConnections.remove(vu);
				sched.add(ini, time);
				schedc.add(ini,time);
			}
		}

	}

	public void setStartsFromOne(boolean startsFromOne) {
		this.startsFromOne = startsFromOne;
	}

	public EvolvingGraph readEvolvingGraph () throws FileNotFoundException {
		EvolvingGraph graph = new EvolvingGraph(numOfNodes);
		HashSet<Edge> openConnections = new HashSet<Edge> ();
		File f = new File (filename);
		Scanner sc;
		graph.setMaxTime(simTime);

		sc = new Scanner (f);
		while (sc.hasNextLine ()) {
			processLine(sc, graph, openConnections);
			sc.nextLine();
		}
		for (Iterator<Edge> iter = openConnections.iterator(); iter.hasNext();) {
			Edge element = (Edge) iter.next();
			int ini = element.getSchedule().last();
			// Add remaining edges
			element.getSchedule().add(ini, simTime);
		}
		return graph;
	}

}
