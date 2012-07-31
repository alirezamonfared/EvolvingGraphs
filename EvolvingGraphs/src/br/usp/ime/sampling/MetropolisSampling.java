package br.usp.ime.sampling;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Node;
import br.usp.ime.evolvinggraph.Schedule;
import br.usp.ime.graphreader.ONEGraphReader;
import br.usp.ime.graphreport.GraphReporter;

public class MetropolisSampling implements Sampling {

	private EvolvingGraph original;
	private EvolvingGraph sample;
	private GraphReporter originalReporter;
	private GraphReporter sampleReporter;
	private int sampleSize;
	private double sigma; // the function that evaluates the difference between the original and the sampled charachteristic
	private int numOfIterations;
	private ArrayList<Integer> sampledNodes;
	private double [] originalReportValues;
	private double [] sampleReportValues;
	
	// METHODS
	public EvolvingGraph getOriginal() {
		return original;
	}


	public void setOriginal(EvolvingGraph original) {
		this.original = original;
	}


	public EvolvingGraph getSample() {
		return sample;
	}


	public void setSample(EvolvingGraph sample) {
		this.sample = sample;
	}


	public int getSampleSize() {
		return sampleSize;
	}


	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}


	public double getSigma() {
		return sigma;
	}
	
	
	public ArrayList<Integer> getSampledNodes() {
		return sampledNodes;
	}


	public void setNumOfIterations(int numOfIterations) {
		this.numOfIterations = numOfIterations;
	}


	/**
	 * Constructor for the sampling class 
	 * @param originalGraphFileName : the name of the ONE file containing the links of the original Evolving graph
	 * @param sampleSize : Number of samples to be taken using this class
	 * @throws IOException : Exception if the file does not exist
	 */
	public MetropolisSampling(String originalGraphFileName, int sampleSize) throws IOException {
		//ONEGraphReader reader = new ONEGraphReader(originalGraphFileName, n, T);
		ONEGraphReader reader = new ONEGraphReader(originalGraphFileName);
		int T = reader.getMaxTime();
		this.originalReporter = new GraphReporter();
		this.sampleReporter = new GraphReporter();
		this.sample = new EvolvingGraph();
		this.original = new EvolvingGraph();
		try {
			reader.setStartsFromOne(true);
			this.originalReporter.readGraph (reader);
			this.original = originalReporter.getGraph();
			this.originalReportValues = originalReporter.journeyUnreachableReport();
		} catch (Exception e) {
			fail("MaxTime not initialized2.");
		}
		this.sigma = 0;
		this.sampleSize = sampleSize;
		this.sampledNodes = new ArrayList<Integer>();
		this.sample = randomSample();
		this.sampleReporter.setGraph(this.sample);
		this.numOfIterations = 10000;
		System.out.println("Constructor is Done");
	}

	/**
	 * A Report of all the important values of the class in printed form
	 */
	public void statusReport(){
		System.out.println("Original graph has " + original.getNumOfNodes()
				+ " Nodes");
		System.out.println("Original graph has " + original.getNumOfEdges()
				+ " Edges");
		System.out.println("Sampled graph has " + sample.getNumOfNodes()
				+ " Nodes");
		System.out.println("Sampled graph has " + sample.getNumOfEdges()
				+ " Edges");
		System.out.println("Sampled nodes are " + sampledNodes);
		System.out.println("Value of Sigma is currently " + sigma);
	}

	/**
	 * Creates a random sample from the original graph with "sampleSize" nodes.
	 * @return
	 */
	public EvolvingGraph randomSample(){
		EvolvingGraph sample = new EvolvingGraph(this.sampleSize);
		int T = this.original.getMaxTime();
		int numOfSamples = 0;
		Random generator = new Random();
		int newSample;
		int NNodes = this.original.getNumOfNodes();
		this.sampledNodes = new ArrayList<Integer>();
		// Get the indices for the samples
		while(numOfSamples < sampleSize){
			newSample = generator.nextInt(NNodes);
			if (this.sampledNodes.indexOf(newSample) == -1){
				this.sampledNodes.add(newSample);
				numOfSamples++;
			}
		}
		sample.setMaxTime(T);
		//Integer[] tmp = (Integer[])sampledNodes.toArray(new Integer[sampledNodes.size()]); /convert set to array
		Edge uv, vu;
		Schedule sched, schedc;
		for(int i = 0; i < sampledNodes.size() ; i++){
			for (int j = i+1 ; j < sampledNodes.size() ; j++){
				uv = null;
				vu = null;
				sched = new Schedule();
				schedc = new Schedule();
				uv = original.getEdge(sampledNodes.get(i).intValue(), sampledNodes.get(j).intValue());
				vu = original.getEdge(sampledNodes.get(j).intValue(), sampledNodes.get(i).intValue());
				if (uv != null && vu != null) {
					sched = uv.getSchedule();
					schedc = vu.getSchedule();
					sample.createEdge(i, j, sched, 0);
					sample.createEdge(j, i, schedc, 0);
				}
			}
		}

		return sample;
	}
	
	/**
	 * Takes a random node number from the sampled nodes
	 * @return
	 */
	public int randomNode(){
		Random generator = new Random();
		int index = generator.nextInt(sampledNodes.size());
		int n = sampledNodes.get(index);
		return n;
	}
	
	
	/**
	 * Takes a random (node) number from the given ArrayList
	 * @param list Input list from which the random number is taken out
	 * @return
	 */
	public int randomNode(ArrayList<Integer> list){
		Random generator = new Random();
		int index = generator.nextInt(list.size());
		int n = list.get(index);
		return n;
	}
	
	
	/**
	 * Replaces the node nOut with the node nIn in the sampled graph
	 * IDs are given between 0...NNodes-1 of the original graph
	 * 
	 * @param nOut Node to be taken out of the sampled graph
	 * @param nIn Node to be added to the sampled graph
	 */
	public EvolvingGraph replaceNode(int nOut, int nIn){
		EvolvingGraph graphIn = new EvolvingGraph(sample);
		if(nIn > original.getNumOfNodes() || nOut > original.getNumOfNodes())
			fail("Node numbers given should be between 0...NNodes-1");
		if(sampledNodes.indexOf(nOut) == -1)
			fail("The node to be taken out does not exist in the sampled graph");
		int nodeID = sampledNodes.indexOf(nOut);
		//Remove all edges that have one side in the node to be taken out
		Edge uv, vu;
		//int nRemoved = 0;
		for(int i = 0; i < sampledNodes.size() ; i++){
			uv = graphIn.getEdge(i, nodeID);
			vu = graphIn.getEdge(nodeID, i);
			if (uv != null && vu != null) {
				graphIn.removeEdge(i, nodeID, uv);
				graphIn.removeEdge(nodeID, i, vu);
				uv = null;
				vu = null;
				//nRemoved++;
			}
		}
		//System.out.println(nRemoved + " Edges Removed");
		//nRemoved = 0;
		// Add new node
		//sampledNodes.set(nodeID, nIn);
		// Update Edges
		Schedule sched, schedc;
		for(int i = 0; i < sampledNodes.size() ; i++){
				uv = null;
				vu = null;
				sched = new Schedule();
				schedc = new Schedule();
				uv = original.getEdge(sampledNodes.get(i).intValue(), nIn);
				vu = original.getEdge(nIn, sampledNodes.get(i).intValue());
				if (uv != null && vu != null) {
					sched = uv.getSchedule();
					schedc = vu.getSchedule();
					graphIn.createEdge(i, nodeID, sched, 0);
					graphIn.createEdge(nodeID, i, schedc, 0);
					//nRemoved++;
				}
		}
		//System.out.println(nRemoved + " Edges Added");
		return graphIn;
	}
	
	
	/**
	 * Automatically updates the value of the sigma (difference between the original and sample graph metric)
	 */
	public void updateSigma(){
		this.sampleReportValues = null;
		this.sigma = 0;
		double [] DiffResult = new double[this.originalReportValues.length];
		try {			
			this.sampleReportValues = sampleReporter.journeyUnreachableReport();
			//DiffResult = OriginalResult - SampleResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.originalReportValues.length != this.sampleReportValues.length)
			fail("Sample should have the same length as the original graph");
		for(int i = 0; i < this.originalReportValues.length ; i++){
			DiffResult[i] = Math.abs(this.originalReportValues[i]-this.sampleReportValues[i]);
			this.sigma += DiffResult[i];
		}
		this.sigma = this.sigma / DiffResult.length;
	}
	
	
	/**
	 * Finds the distance metric between a given EvolvingGraph newSample, and the original EvolvingGraph of the class (this.original)
	 * @param newSample
	 * @return
	 */
	public double getSigma(EvolvingGraph newSample){
		double newSigma = 0;
		double[] newSampleReportValues = null;
		double [] DiffResult = new double[this.originalReportValues.length];
		GraphReporter newSampleReporter = new GraphReporter();
		newSampleReporter.setGraph(newSample);
		try {			
			newSampleReportValues = newSampleReporter.journeyUnreachableReport();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.originalReportValues.length != newSampleReportValues.length)
			fail("Sample should have the same length as the original graph");
		for(int i = 0; i < this.originalReportValues.length ; i++){
			DiffResult[i] = Math.abs(this.originalReportValues[i]-newSampleReportValues[i]);
			newSigma += DiffResult[i];
		}
		newSigma = newSigma / DiffResult.length;
		return newSigma;
	}

	
	/**
	 * Sample the Evolving Graph using the Metropolis-Hasting Algorithm
	 * This assumes that an Object of type MetropolisSampling is already created using the constructor.
	 * @return 
	 */
	public void sample() {
		System.out.println("Sigma Entering Sample is " + sigma);
		EvolvingGraph sCurrent = new EvolvingGraph(this.sample);
		EvolvingGraph sNew = new EvolvingGraph();
		Random generator = new Random();
		double alpha;
		int id, id2;
		// sBest is the same as this.sample
		// since the constructor is called, sBest is already a randomSample of the given size
		Integer v,w;
		ArrayList<Integer> nonSampled = new ArrayList<Integer>();
		for(int i = 0 ; i < original.getNumOfNodes() ; i++){
			if(!sampledNodes.contains(i))
				nonSampled.add(i);
		}
		for(int i = 0; i < numOfIterations ; i++){
			v = new Integer(randomNode());
			nonSampled.add(v);
			w = new Integer(randomNode(nonSampled));
			sNew = replaceNode(v.intValue(), w.intValue());
			alpha = generator.nextDouble();
			if(alpha < getSigma(sCurrent)/getSigma(sNew)){
				sCurrent = new EvolvingGraph(sNew);
				//replace v for w in the currentSampledNodes, and w for v in nonSampled
				id = sampledNodes.indexOf(v);
				id2 = nonSampled.indexOf(w);
				sampledNodes.set(id, w);
				nonSampled.set(id2, v);
				if(getSigma(sCurrent) < getSigma()){
					sample = new EvolvingGraph(sCurrent);
				}
			}
			// remove the last element of nonSampled (the added v)
			nonSampled.remove(nonSampled.size()-1);
		}
		System.out.println("Sigma Exiting Sample is " + sigma);
	}

}
