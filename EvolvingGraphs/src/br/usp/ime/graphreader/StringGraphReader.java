package br.usp.ime.graphreader;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Schedule;

public class StringGraphReader implements GraphReader {

	private int [][] EvolvingGraphLog;
	private int numOfNodes;
	private int simTime;
	
	public StringGraphReader (int [][] EvolvingGraphLog, int numOfNodes, int simTime) {
		this.EvolvingGraphLog = EvolvingGraphLog;
		this.numOfNodes = numOfNodes;
		this.simTime = simTime;
	}
	

	/**
	 * Reads an evolving graph from a given string in the form "<time> <node u> <node v> <UP(1)/DOWN(0)>"
	 */
	public EvolvingGraph readEvolvingGraph() throws FileNotFoundException {
		System.out.println("StringGraphReader is reading your graph...");
		EvolvingGraph eg = new EvolvingGraph(numOfNodes);
		HashSet<Edge> openConnections = new HashSet<Edge> ();
		eg.setMaxTime(simTime);
		
		int NEvents = EvolvingGraphLog.length;
		int time , u , v, status;
		Schedule sched, schedc;
		for (int i = 0 ; i < NEvents ; i++){
			time = EvolvingGraphLog[i][0];
			u = EvolvingGraphLog[i][1];
			v = EvolvingGraphLog[i][2];
			status = EvolvingGraphLog[i][3];
			//System.out.println("Iteration i == " + i + " started with u=="+u+ " ,v==" + v + " ,t==" + time);
			Edge uv = null, vu = null;
			uv = eg.getEdge(u, v);
			
			if (uv == null) {
				sched = new Schedule();
				sched.add(time, time+1);
				schedc = new Schedule();
				schedc.add(time, time+1);
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
				if (status == 1) {
					//System.out.println("uv exists for Iteration " + i + " with status up");
					sched.add(time, time+1);
					schedc.add(time, time+1);
					//System.out.println("Sched done");
					openConnections.add(uv);
					openConnections.add(vu);
					//System.out.println("OpenConnections Done");
				}
				else {
					//System.out.println("uv exists for Iteration " + i + " with status down");
					int ini = sched.last();
					openConnections.remove(uv);
					openConnections.remove(vu);
					sched.add(ini, time);
					schedc.add(ini,time);
				}
			}
		}
		for (Iterator<Edge> iter = openConnections.iterator(); iter.hasNext();) {
			Edge element = (Edge) iter.next();
			int ini = element.getSchedule().last();
			// Add remaining edges
			element.getSchedule().add(ini, simTime);
		}
		System.out.println("StringGraphReader has read your graph...");
		return eg;
	}

}
