/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.graphreport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.usp.ime.algorithms.Algorithm;
import br.usp.ime.algorithms.FastestAlgorithm;
import br.usp.ime.algorithms.ForemostAlgorithm;
import br.usp.ime.algorithms.LatestAlgorithm;
import br.usp.ime.algorithms.ShortestAlgorithm;
import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;
import br.usp.ime.graphreader.GraphReader;
import br.usp.ime.messagereader.Message;
import br.usp.ime.messagereader.MessageReader;

/**
 * Generates several reports on an evolving graph. For more information on the reports, check
 * http://grenoble.ime.usp.br/~paulo/dtn/services.html
 * @author Paulo Henrique Floriano
 *
 */
public class GraphReporter {
	private EvolvingGraph graph;
	private String author, link = null, traceName;
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setTraceName(String traceName) {
		this.traceName = traceName;
	}

	public GraphReporter () {
	}
	
	public GraphReporter (GraphReader gr) {
		try {
			readGraph(gr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setGraph(EvolvingGraph graph) {
		this.graph = graph;
	}

	public EvolvingGraph getGraph() {
		return graph;
	}
	
	/**
	 * Reads evolving graph using the provided graph reader.
	 * 
	 * TODO Exception?
	 * @param reader
	 * @throws Exception
	 */
	public void readGraph (GraphReader reader) throws Exception{
		Exception e = new Exception ();
		graph = reader.readEvolvingGraph();
		if (graph.getMaxTime() == -1){
			System.out.println("Max Time Error in readGraph");
			throw e;
		}
	}
	
	/**
	 * 
	 * @param fileName File name to be tested
	 * @return true if the file exists or false otherwise
	 */
	public boolean fileExists (String fileName) {
		File tempFile = new File(fileName);
		try {
			if (tempFile.createNewFile()) {
				tempFile.delete();
				return false;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
	/**
	 * Writes meta data to the start of a report file.
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void putMetaData (FileWriter output) throws IOException {
		output.write("#This report is for the trace: " + traceName + "\n");
		output.write("#Uploaded to DTNTES by: " + author + "\n");
		if (link == null)
			output.write("#No link to the trace was provided\n\n");
		else
			output.write("#Link to the trace: " + link + "\n\n");
	}
	
	/**
	 * Generates the connectivity report for the evolving graph read.
	 * 
	 * @param outputName Name of the output file
	 * @throws IOException
	 */
	public void connectivityReport (String outputName) throws IOException{
		Algorithm lt = new LatestAlgorithm ();
		List<List<Journey>> ltlist = new ArrayList<List<Journey>> (graph.getNumOfNodes());
		int maxConnTime = Integer.MAX_VALUE;

		for (Node n: graph.getNodes()) 
			ltlist.add(n.getId(), lt.calculate(graph, n));

		if (!fileExists(outputName)) {
			FileWriter output = new FileWriter (outputName);
			output.write("#This is the connectivity report\n\n");
			putMetaData(output);
			for(int i = 0; i < graph.getNumOfNodes(); i++) {
				int nodeMaxConnTime = Integer.MAX_VALUE, nodeLastConnectionTime = -1;
				output.write("Node " + graph.getNode(i).getName() + ":\n");
				output.write("Can communicate with: ");
				for (int j = 0; j < graph.getNumOfNodes(); j++) {
					if (i == j)
						continue;
					if (ltlist.get(j).get(i) != null && !ltlist.get(j).get(i).isEmpty()) {
						output.write(graph.getNode(j).getName() + " ");
						int departureTime = ltlist.get(j).get(i).getTime(0);
						if (ltlist.get(j).size() == graph.getNumOfNodes() - 1 && departureTime < maxConnTime)
							maxConnTime = departureTime;
						if (departureTime < nodeMaxConnTime)
							nodeMaxConnTime = departureTime;
						if (departureTime > nodeLastConnectionTime)
							nodeLastConnectionTime = departureTime;
					}
				}
				output.write("\n");
				output.write("Node's maximum connected time: " + nodeMaxConnTime + "\n");
				output.write("Node's latest connection time: " + nodeLastConnectionTime + "\n\n");
			}
			if (maxConnTime == Integer.MAX_VALUE)
				maxConnTime = -1;
			output.write("The network's maximum connected time is: " + maxConnTime + "\n");
			output.close();
		}
	}
	
	/**
	 * Generates the periodicity report for the evolving graph read.
	 * 
	 * @param outputName Name of the output file
	 * @throws IOException
	 */
	public void periodicityReport (String outputName) throws IOException {
		if (!fileExists(outputName)) {
			FileWriter output = new FileWriter (outputName);
			output.write("#This is the periodicity report\n\n");
			putMetaData(output);
			for (Node n: graph.getNodes()) {
				for (Edge e: n.getAdj()) {
					output.write("Edge " + n.getName() + " " + e.getV().getName() + "\n");
					ArrayList<Integer> seq = e.buildSequence();
					int p = 0, seconds = 0;
					for (int i = 0; i < seq.size()/2; i++) {
						if (i > 0)
							seconds += seq.get(2*i - 1) + seq.get(2*i - 2);
						p = e.periodLength(i, seq);
						if (p > 0) {
							output.write("Periodic after " + seconds + " seconds, every " + p + " seconds.\n");
							break;
						}
					}
					if (p == 0)
						output.write("Non periodic.\n");
				}
			}
			output.close();
		}
	}
	
	/**
	 * Generates the journey reports for the evolving graph read.
	 * 
	 * @param statsOutputName Name of the output file for the journey stats
	 * @param journeysOutputName Name of the output file for the full journeys
	 * @throws IOException
	 */
	public void journeyReport (String statsOutputName, String journeysOutputName, int initialTime) throws IOException{
		Algorithm fm = new ForemostAlgorithm();
		Algorithm sh = new ShortestAlgorithm();
		List<List<Journey>> fmlist = new ArrayList<List<Journey>> (graph.getNumOfNodes());
		List<List<Journey>> shlist = new ArrayList<List<Journey>> (graph.getNumOfNodes());

		for (Node n : graph.getNodes()) {
			fmlist.add(n.getId(), fm.calculate(graph, n, initialTime));
			shlist.add(n.getId(), sh.calculate(graph, n, initialTime));
		}

		if (!fileExists(statsOutputName)) {
			FileWriter output = new FileWriter (statsOutputName);
			output.write("#This is the journey stats report starting at time " + initialTime + "\n");
			output.write("#For every pair of vertices, the earliest arrival date and minimum hopcount are displayed\n\n");
			putMetaData(output);

			for(int i = 0; i < graph.getNumOfNodes(); i++) {
				for (int j = 0; j < graph.getNumOfNodes(); j++) {
					if (i == j)
						continue;
					output.write("Node " + graph.getNode(i).getName() + " to node " + graph.getNode(j).getName() + "\n");
					if (fmlist.get(i).get(j).getArrivalDate() == -1)
						output.write ("Earliest Arrival Date: Unreachable\n");
					else
						output.write("Earliest Arrival Date: " + fmlist.get(i).get(j).getArrivalDate() + "\n");
					if (shlist.get(i).get(j) == null)
						output.write("Minimum hopcount: Unreachable\n\n");
					else
						output.write("Minimum hopcount: " + shlist.get(i).get(j).getSize() + "\n\n");
				}
			}
			output.close();
		}

		if (!fileExists(journeysOutputName)) {
			FileWriter output = new FileWriter (journeysOutputName);
			output.write("#This is the full journey report starting at time " + initialTime + "\n");
			output.write("#For every pair of vertices, the foremost and shortest journeys are displayed\n");
			output.write("#Each edge is in the form (originNode[traversalTime]destinationNode)\n\n");
			putMetaData(output);
			for(int i = 0; i < graph.getNumOfNodes(); i++) {
				for (int j = 0; j < graph.getNumOfNodes(); j++) {
					if (i == j)
						continue;
					output.write("Node " + graph.getNode(i).getName() + " to node " + graph.getNode(j).getName() + "\n");
					if (fmlist.get(i).get(j).getArrivalDate() == -1)
						output.write ("Foremost Journey: Unreachable\n");
					else
						output.write("Foremost Journey: " + fmlist.get(i).get(j) + "\n");
					if (shlist.get(i).get(j) == null)
						output.write("Shortest Journey: Unreachable\n\n");
					else
						output.write("Shortest Journey: " + shlist.get(i).get(j) + "\n\n");
				}
			}
			output.close();
		}
	}
	
	public double [] journeyTimeReport() throws IOException{
		Algorithm fm = new ForemostAlgorithm();
		ArrayList<ArrayList<List<Journey>>> FMLIST = new ArrayList<ArrayList<List<Journey>>> (graph.getMaxTime());

		for(int l = 0; l < graph.getMaxTime(); l++){
			FMLIST.add(new ArrayList<List<Journey>>(graph.getNumOfNodes()));
		}
		for (int currentTime = 0; currentTime < graph.getMaxTime(); currentTime++){
			for (Node n : graph.getNodes()) {
				FMLIST.get(currentTime).add(n.getId(), fm.calculate(graph, n, currentTime));
			}
		}
		double[] Result = new double[graph.getMaxTime()];
		double[] Count = new double[graph.getMaxTime()];
		
		for(int t = 0 ; t < graph.getMaxTime() ; t++){
			Result[t] = 0;
			Count[t] = 0;
		}
		
		for(int t = 0 ; t < graph.getMaxTime(); t++){
			for(int i = 0; i < graph.getNumOfNodes(); i++) {
				for (int j = 0; j < graph.getNumOfNodes(); j++) {
					if (i == j || FMLIST.get(t).get(i).get(j).getArrivalDate() == -1){
						// To-Do : Handle Unreachables 
						continue;
					}
					else{
						Result[t] += FMLIST.get(t).get(i).get(j).getTransitTime().doubleValue();
						Count[t]++;
					}
				}
			}
		}
		
		for(int t = 0 ; t < graph.getMaxTime() ; t++){
			Result[t] /= Count[t];
		}
		
		return Result;		
	}
	
	public double [] journeyUnreachableReport() throws IOException{
		Algorithm fm = new ForemostAlgorithm();
		ArrayList<ArrayList<List<Journey>>> FMLIST = new ArrayList<ArrayList<List<Journey>>> (graph.getMaxTime());

		for(int l = 0; l < graph.getMaxTime(); l++){
			FMLIST.add(new ArrayList<List<Journey>>(graph.getNumOfNodes()));
		}
		for (int currentTime = 0; currentTime < graph.getMaxTime(); currentTime++){
			for (Node n : graph.getNodes()) {
				FMLIST.get(currentTime).add(n.getId(), fm.calculate(graph, n, currentTime));
			}
		}
		double[] Result = new double[graph.getMaxTime()];
		double NNodes = (double)graph.getNumOfNodes();
		
		for(int t = 0 ; t < graph.getMaxTime() ; t++){
			Result[t] = 0;
		}
		
		for(int t = 0 ; t < graph.getMaxTime(); t++){
			for(int i = 0; i < graph.getNumOfNodes(); i++) {
				for (int j = 0; j < graph.getNumOfNodes(); j++) {
					if (i != j && FMLIST.get(t).get(i).get(j).getArrivalDate() == -1)
						Result[t]++;
					else
						continue;
				}
			}
		}
		
		for(int t = 0 ; t < graph.getMaxTime() ; t++){
			Result[t] /= (NNodes*(NNodes-1));
		}
		//System.out.println("Number of Nodes is : " + NNodes + "\n");
		return Result;		
	}
	
	public void journeyClassifier(String statsOutputName) throws IOException{
		Algorithm fm = new ForemostAlgorithm();
		Algorithm sh = new ShortestAlgorithm();
		ArrayList<ArrayList<List<Journey>>> FMLIST = new ArrayList<ArrayList<List<Journey>>> (graph.getMaxTime());
		ArrayList<ArrayList<List<Journey>>> SHLIST = new ArrayList<ArrayList<List<Journey>>> (graph.getMaxTime());
		
		for(int l = 0; l < graph.getMaxTime(); l++){
			FMLIST.add(new ArrayList<List<Journey>>(graph.getNumOfNodes()));
			SHLIST.add(new ArrayList<List<Journey>>(graph.getNumOfNodes()));
		}
		for (int currentTime = 0; currentTime < graph.getMaxTime(); currentTime++){
			for (Node n : graph.getNodes()) {
				//FMLIST[currentTime];
				FMLIST.get(currentTime).add(n.getId(), fm.calculate(graph, n, currentTime));
				SHLIST.get(currentTime).add(n.getId(), sh.calculate(graph, n, currentTime));
			}
		}
		if (!fileExists(statsOutputName)) {
			FileWriter output = new FileWriter (statsOutputName);
			//output.write("Time NodeID1 NodeID2 EarliestArrivalDate MininmumHopCount\n");
			int EAD = 0;
			int MHC = 0;
			int NodepairID;
			for(int t = 0 ; t < graph.getMaxTime(); t++){
				NodepairID = 0;
				for(int i = 0; i < graph.getNumOfNodes(); i++) {
					for (int j = 0; j < graph.getNumOfNodes(); j++) {
						if (i == j)
							continue;
						//output.write("Node " +  + " to node " +  + "\n");
						if (FMLIST.get(t).get(i).get(j).getArrivalDate() == -1)
							EAD  =-1;
						else
							//EAD = FMLIST.get(t).get(i).get(j).getArrivalDate();
							EAD = FMLIST.get(t).get(i).get(j).getTransitTime();
						if (SHLIST.get(t).get(i).get(j) == null)
							MHC = -1;
						else
							MHC = SHLIST.get(t).get(i).get(j).getSize();
						//System.out.printf("%d %d %d \n", t,i,j);
						//output.write((float)(t)+" "+graph.getNode(i).getName()+" "+graph.getNode(j).getName()+" "+EAD+" "+MHC+"\n");
						output.write((float)(t)+" "+NodepairID+" "+EAD+" "+MHC+"\n");
						NodepairID++;
					}
				}
			}
			output.close();
		}
	
	}
	
	
	public void report (Algorithm a, String outputName) {
		String location = "reports/" + outputName;
		try {
			FileWriter output = new FileWriter (location);
			for (int i = 0; i < graph.getMaxTime(); i++) {
				output.write(i + "\n");
				for (Node n : graph.getNodes()) {
					List<Journey> journeys = a.calculate(graph, n, i);
					for (Node m : graph.getNodes()) {
						output.write(n.getId() + " " + m.getId() + " " + a.getOptimizedParameter(journeys.get(m.getId())) + "\n"); 
					}
				}
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void report (String inputName, String outputName) throws IOException{
		File messFile = new File (inputName);
		report (messFile, outputName);
	}
	
	/**
	 * Generates the message log report on the evolving graph read
	 * 
	 * @param messFile File containing the message creation data
	 * @param outputName Name of the output file
	 * @throws IOException
	 */
	public void report (File messFile, String outputName) throws IOException{
		MessageReader reader = new MessageReader (messFile);
		List<Message> messages = reader.readMessages();
		FileWriter output = new FileWriter (outputName);
		putMetaData(output);
		output.write("Message\tEAD\tMinHop\tMinTrs\n");
		int i = 1;
		for (Message m: messages) {
			outputMessageStats(output, i, m);
			i++;
		}
		output.close();
	}

	private void outputMessageStats(FileWriter output, int i, Message m)
			throws IOException {
		//System.out.println("Message " + i);
		List<Journey> journeys;
		ForemostAlgorithm fm = new ForemostAlgorithm ();
		journeys = fm.calculate(graph, graph.getNode(m.getOrigin()), m.getCreationTime());
		int ead = journeys.get(graph.getNode(m.getDestination()).getId()).getArrivalDate();
		//System.out.println(journeys.get(m.getDestinationId()));
		ShortestAlgorithm sh = new ShortestAlgorithm ();
		journeys = sh.calculate(graph, graph.getNode(m.getOrigin()), m.getCreationTime());
		int mhp;
		if (journeys.get(graph.getNode(m.getDestination()).getId()) == null)
			mhp = -1;
		else
			mhp = journeys.get(graph.getNode(m.getDestination()).getId()).getSize();
		//System.out.println(journeys.get(m.getDestinationId()));
		FastestAlgorithm fs = new FastestAlgorithm ();
		journeys = fs.calculate(graph, graph.getNode(m.getOrigin()), m.getCreationTime());
		int mtt = journeys.get(graph.getNode(m.getDestination()).getId()).getTransitTime();
		//System.out.println(journeys.get(m.getDestinationId()));
		output.write(i + "\t");
		if (ead != -1)
			output.write(ead + "\t");
		else
			output.write("-\t");
		if (mhp != -1)
			output.write(mhp + "\t");
		else
			output.write("-\t");
		if (mtt != -1)
			output.write(mtt + "\n");
		else
			output.write("-\n");
	}

}
