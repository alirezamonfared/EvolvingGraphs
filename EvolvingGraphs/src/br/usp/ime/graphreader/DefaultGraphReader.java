/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.graphreader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.MatchResult;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Schedule;


/**
 * Reader that uses default input. For information on the input format check 
 * http://grenoble.ime.usp.br/~paulo/dtn/formats.html
 * @author Paulo Henrique Floriano
 *
 */
public class DefaultGraphReader implements GraphReader {

	private String filename;

	public DefaultGraphReader (String filename) {
		this.filename = filename;
	}

	/**
	 * Read Evolving Graph from file in the Default format.
	 * @throws FileNotFoundException 
	 */
	public EvolvingGraph readEvolvingGraph () throws FileNotFoundException {
		Schedule sched;
		String u, v;
		int source, dest, cost, numsched;
		int aux = 0;
		File f = new File (filename);
		EvolvingGraph graph = new EvolvingGraph(0);
		graph.setMaxTime(0);

		Scanner sc = new Scanner (f);
		sc.nextInt();
		int edges = sc.nextInt();
		graph = new EvolvingGraph();
		while(edges > 0) {
			u = sc.next();
			v = sc.next();
			if (!graph.hasNode(u))
				graph.addNode(u);
			source = graph.getNode(u).getId();
			if (!graph.hasNode(v))
				graph.addNode(v);
			dest = graph.getNode(v).getId();
			cost = sc.nextInt();
			numsched = sc.nextInt();
			sched = new Schedule();
			while(numsched > 0) {
				if (sc.hasNext("\\[(\\d*),(\\d*)\\[")) {
					MatchResult m = sc.match();
					int begin = Integer.parseInt(m.group(1));
					int end = Integer.parseInt(m.group(2));
					sched.add(begin, end);
					sc.next();
					numsched--;
				}
				else if (sc.hasNextInt()) {
					aux = sc.nextInt();
					sched.add(aux, aux+1);
					numsched--;
				}
			}
			if (aux > graph.getMaxTime())
				graph.setMaxTime(aux);
			graph.createEdge(source, dest, sched, cost);
			edges--;
		}
		return graph;
	}


}
