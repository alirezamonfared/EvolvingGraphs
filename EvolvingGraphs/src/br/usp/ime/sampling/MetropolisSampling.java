package br.usp.ime.sampling;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Node;
import br.usp.ime.graphreader.ONEGraphReader;
import br.usp.ime.graphreport.GraphReporter;

public class MetropolisSampling implements Sampling {

	private EvolvingGraph original;
	private EvolvingGraph sample;
	private int sampleSize;
	private double sigma; // the function that evaluates the difference between the original and the sampled charachteristic
	private HashSet<Integer> sampledNodes;
	
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


	public MetropolisSampling(String originalGraphFileName, int sampleSize, int n, int T) {
		GraphReporter reporter = new GraphReporter ();
		ONEGraphReader reader = new ONEGraphReader(originalGraphFileName, n, T);
		try {
			reader.setStartsFromOne(true);
			reporter.readGraph (reader);
		} catch (Exception e) {
			fail("MaxTime not initialized2.");
		}
		this.sigma = 0;
		this.sampleSize = sampleSize;
		this.original = reporter.getGraph();
		this.sample = randomSample();
		this.sampledNodes = new HashSet<Integer>();
	}


	/**
	 * Creates a random sample from the original graph with "sampleSize" nodes.
	 * @return
	 */
	public EvolvingGraph randomSample(){
		EvolvingGraph sample = new EvolvingGraph(sampleSize);
		int numOfSamples = 0;
		Random genersator = new Random();
		int newSample;
		int NNodes = original.getNumOfNodes();
		// Get the indices for the samples
		while(numOfSamples < sampleSize){
			newSample = genersator.nextInt(NNodes) + 1;
			if (!sampledNodes.contains(newSample)){
				sampledNodes.add(newSample);
				numOfSamples++;
			}
		}
		return sample;
	}
	@Override
	public EvolvingGraph sample() {
		// TODO Auto-generated method stub
		return null;
	}

}
