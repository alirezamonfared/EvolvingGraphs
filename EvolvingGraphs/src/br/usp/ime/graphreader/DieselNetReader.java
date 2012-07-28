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

/**
 * Reader that uses DieselNet input. For information on the input format check 
 * http://grenoble.ime.usp.br/~paulo/dtn/formats.html
 * @author Paulo Henrique Floriano
 *
 */
public class DieselNetReader implements GraphReader {
	String fileName;
	
	public DieselNetReader (String fileName) {
		this.fileName = fileName;
	}
	
	public EvolvingGraph readEvolvingGraph() throws FileNotFoundException {
		EvolvingGraph graph = new EvolvingGraph();
		//HashMap<String, Integer> names = new HashMap<String, Integer>();
		File f = new File (fileName);
		Scanner sc = new Scanner (f);
		
		while (sc.hasNextLine ()) {
			processLine(sc, graph);
			sc.nextLine();
		}
		return graph;
	}

	private void processLine(Scanner sc, EvolvingGraph graph) {
		sc.findInLine("(\\S+) (\\S+) (\\d+):(\\d+):(\\d+) \\w+ (\\d+)(\\.\\d+)? \\S+ \\S+");
		MatchResult result = sc.match();
		if (result.group(1).equals("null") || result.group(2).equals("null"))
			return;
		String orig = result.group(1);
		String dest = result.group(2);
		int connBegin = Integer.parseInt(result.group(3))*3600 + Integer.parseInt(result.group(4))*60 +
						Integer.parseInt(result.group(5));
		int connDuration = Integer.parseInt(result.group(6));
		if (!graph.hasNode(orig))
			graph.addNode(orig);
		int u = graph.getNode(orig).getId();
		if (!graph.hasNode(dest))
			graph.addNode(dest);
		int v = graph.getNode(dest).getId();
		graph.createEdge(u, v, connBegin, connBegin + connDuration, 0);
		graph.createEdge(v, u, connBegin, connBegin + connDuration, 0);
		graph.setMaxTime(connBegin + connDuration);
	}

}
