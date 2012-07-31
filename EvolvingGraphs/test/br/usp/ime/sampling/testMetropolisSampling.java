package br.usp.ime.sampling;

import java.io.IOException;

import br.usp.ime.evolvinggraph.EvolvingGraph;

public class testMetropolisSampling {
	public static void main(String[] args) throws IOException {
		MetropolisSampling mtp = new MetropolisSampling("./input/CSV4OriginalLinks.one", 10);
		mtp.updateSigma();
		mtp.statusReport();
	}
}
