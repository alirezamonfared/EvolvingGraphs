package br.usp.ime.sampling;

import java.io.IOException;
import java.util.ArrayList;

import br.usp.ime.evolvinggraph.EvolvingGraph;

public class testMetropolisSampling {
	public static void main(String[] args) throws IOException {
		MetropolisSampling mtp = new MetropolisSampling("./input/CSV4OriginalLinks.one", 10);
		mtp.updateSigma();
		//mtp.statusReport();
		mtp.setNumOfIterations(1000);
		//ArrayList<Integer> sp = mtp.getSampledNodes();
		//EvolvingGraph eg = mtp.replaceNode(sp.get(0), 27);
		//System.out
		//		.println("Altered has " +eg.getNumOfNodes() + " nodes and " + eg.getNumOfEdges() + " edges" );
		//mtp.statusReport();
		mtp.sample();
		//mtp.statusReport();
	}
}
