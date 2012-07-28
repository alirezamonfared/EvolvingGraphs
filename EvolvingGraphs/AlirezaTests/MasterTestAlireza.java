import static org.junit.Assert.fail;


import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.graphreader.ONEGraphReader;
import br.usp.ime.graphreport.GraphReporter;
import br.usp.ime.graphreport.TestReportsAlireza;


public class MasterTestAlireza {
	
	public static void main(String [] args){
		TestReportsAlireza obj = new TestReportsAlireza();
		//obj.testEGWithOneNodeShouldReturnEmptyJourney();
		//obj.testReadGraph();
		//System.out.print("Hello");
		obj.testReadONEFile();
	}
	
	/*
	public static void main(String [] args){
		GraphReporter reporter = new GraphReporter ();
		ONEGraphReader reader = new ONEGraphReader("../../../MATLAB/Sampling/Results/CSV4OriginalLinks.one",51,1000);		
		//EvolvingGraph gp = new EvolvingGraph();
		double[] Result;
		try {			
			reporter.readGraph (reader);
			//Result = reporter.journeyTimeReport();
			Result = reporter.journeyUnreachableReport();
			for(int i=0; i<Result.length;i++)
				System.out.println(Result[i]);
		} catch (Exception e) {
			fail("MaxTime not initialized2.");
		}
		//gp = reporter.getGraph();	
		//System.out.print(gp.getNumOfNodes());
		//System.out.print("\n");
	}
	*/
}
