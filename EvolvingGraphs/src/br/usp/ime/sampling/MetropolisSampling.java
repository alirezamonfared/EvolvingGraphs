package br.usp.ime.sampling;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
	private HashSet<Integer> sampledNodes;	
	double [] OriginalReportValues;
	double [] SampleReportValues;
	
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


	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	
	public HashSet<Integer> getSampledNodes() {
		return sampledNodes;
	}


	public void setSampledNodes(HashSet<Integer> sampledNodes) {
		this.sampledNodes = sampledNodes;
	}


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
		} catch (Exception e) {
			fail("MaxTime not initialized2.");
		}
		this.sigma = 0;
		this.sampleSize = sampleSize;
		this.original = originalReporter.getGraph();
		this.sample = randomSample(T);
		this.sampleReporter.setGraph(this.sample);
		this.sampledNodes = new HashSet<Integer>();
	}

	public void statusReport(){
		System.out.println("Original graph has " + original.getNumOfNodes()
				+ " Nodes");
		System.out.println("Original graph has " + original.getNumOfEdges()
				+ " Edges");
		System.out.println("Sampled graph has " + sample.getNumOfNodes()
				+ " Nodes");
		System.out.println("Sampled graph has " + sample.getNumOfEdges()
				+ " Edges");
	}

	/**
	 * Creates a random sample from the original graph with "sampleSize" nodes.
	 * @return
	 */
	public EvolvingGraph randomSample(int T){
		EvolvingGraph sample = new EvolvingGraph(sampleSize);
		int numOfSamples = 0;
		Random genersator = new Random();
		int newSample;
		int NNodes = original.getNumOfNodes();
		sampledNodes = new HashSet<Integer>();
		// Get the indices for the samples
		while(numOfSamples < sampleSize){
			newSample = genersator.nextInt(NNodes);
			if (!sampledNodes.contains(newSample)){
				sampledNodes.add(newSample);
				numOfSamples++;
			}
		}
		sample.setMaxTime(T);
		
		Integer[] tmp = (Integer[])sampledNodes.toArray(new Integer[sampledNodes.size()]);
		Edge uv, vu;
		Schedule sched, schedc;
		for(int i = 0; i < tmp.length ; i++){
			for (int j = i+1 ; j < tmp.length ; j++){
				uv = null;
				vu = null;
				sched = new Schedule();
				schedc = new Schedule();
				uv = original.getEdge(tmp[i].intValue(), tmp[j].intValue());
				vu = original.getEdge(tmp[j].intValue(), tmp[i].intValue());
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
	
	public void updateSigma(){

		//double [] DiffResult = null;
		try {
			this.OriginalReportValues = originalReporter.journeyUnreachableReport();
			this.SampleReportValues = sampleReporter.journeyUnreachableReport();
			//DiffResult = OriginalResult - SampleResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public EvolvingGraph sample() {
		// TODO Auto-generated method stub
		return null;
	}

}
