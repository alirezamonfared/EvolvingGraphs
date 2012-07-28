package br.usp.ime.graphreport;

import static org.junit.Assert.fail;

import java.awt.image.RescaleOp;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.List;

//import br.usp.ime.algorithms.Algorithm;
//import br.usp.ime.algorithms.ShortestAlgorithm;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Node;
//import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.graphreader.ONEGraphReader;
import br.usp.ime.graphreader.StringGraphReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class TestReportsAlireza {


	public void testReadONEFile(){
		/*
		//working with pwd
		 System.out.println("Changing working directory...");
		 System.setProperty("user.dir",
				 "/home/alireza/workspace/MobilityProject/MATLAB/Sampling/Results/");
		 File directory = new File (".");
		 try {
			 System.out.println ("Current directory's canonical path: " 
					 + directory.getCanonicalPath()); 
			 System.out.println ("Current directory's absolute  path: " 
					 + directory.getAbsolutePath());
		 }
		 catch(Exception e) {
			 System.out.println("Exceptione is ="+e.getMessage());
		  }
		  File[] listOfFiles = directory.listFiles();
		  String files;
		  for(int i=0;i<listOfFiles.length;i++){
			  files = listOfFiles[i].getName();
			  System.out.println(files);
		  }
		 */
		
		GraphReporter reporter = new GraphReporter ();
		//ONEGraphReader reader = new ONEGraphReader("input/CSV4Original.one",51,1000);
		//ONEGraphReader reader = new ONEGraphReader("input/pmtr_linksPrep.one",50,1632979);
		//ONEGraphReader reader = new ONEGraphReader("input/ConnectedModel.one",50,1000);
		//ONEGraphReader reader = new ONEGraphReader("input/testeONE1.in",6,17);
		//ONEGraphReader reader = new ONEGraphReader("input/testeONE6.in",3,3);
		//ONEGraphReader reader = new ONEGraphReader("input/MessageFerry.one",21,100);
		//ONEGraphReader reader = new ONEGraphReader("../../../MATLAB/Sampling/Results/CSV4OriginalSampled10phi.one",10,1000);
		//ONEGraphReader reader = new ONEGraphReader("../../../MATLAB/Sampling/Results/CSV4OriginalSampled20phi.one",11,1000);
		//Sampled and Original
		//ONEGraphReader reader = new ONEGraphReader("../../../MATLAB/Sampling/Results/CSV4OriginalLinks.one",51,1000);
		//ONEGraphReader reader = new ONEGraphReader("./CSV4OriginalJSampled20phi.one",10,990);
		ONEGraphReader reader = new ONEGraphReader("/home/alireza/workspace/MobilityProject/MATLAB/Sampling/Results/CSV4OriginalJSampled20phi.one",9,990);
		reader.setStartsFromOne(true);
		
		String OutputFileName = "/home/alireza/workspace/MobilityProject/MATLAB/Sampling/Results/CSV4JourneyClassReport.txt";
		
		EvolvingGraph gp = new EvolvingGraph();
		
		/* Graph Reading Using Reporter*/
		try {
			reporter.readGraph (reader);
			// These lines make sure that a new file is written
			File f = new File (OutputFileName);
			System.out.println("done");
			//f.delete();
			//f = new File ("reports/FT2.txt");
			//f.delete();
			//f = new File ("reports/FT3.txt");
			//f.delete();
			//f = new File ("reports/FT4.txt");
			//f.delete();
			//f = new File ("reports/FT5.txt");
			//f.delete();
			//f = new File (OutputFileName);
			f.delete();
			//reporter.journeyReport("reports/FT1.txt", "reports/FT2.txt", 0);
			reporter.journeyClassifier(OutputFileName);
			//reporter.connectivityReport("reports/FT3.txt");
			//reporter.periodicityReport("reports/FT4.txt");
		} catch (Exception e) {
			//System.out.println("Error");
			fail("MaxTime not initialized2.");
		}
		gp = reporter.getGraph(); //N
		for(Node nn:gp.getNodes()){
			System.out.println(nn.getId());
		}
		/* Test Algorithms Separately
		Algorithm algorithm = new ShortestAlgorithm();
		List<Journey> emptyJourneys = algorithm.calculate(gp, gp.getNode(0));
		System.out.print(emptyJourneys.size());
		System.out.print("\n DONE \n");
		reporter.setAuthor("Alireza");
		reporter.setTraceName("FirstTrace");
		File f = new File ("reports/FT1.txt");
		f.delete();
		 */
		
		/* Direct Graph Reading 
		try{
			gp = reader.readEvolvingGraph();
			Algorithm algorithm = new ShortestAlgorithm();
			List<Journey> emptyJourneys = algorithm.calculate(gp, gp.getNode(0));
			System.out.print(emptyJourneys.size());
			System.out.print("\n DONE \n");
		}  catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
		*/
		
		//System.out.print(gp.getNumOfNodes()); //N
		//System.out.print("\n");
		//System.out.print(gp.getNumOfEdges());

	}
	
	public double [] TestJourneyUnreachableReport(String[] args) throws Exception {
		double [] Result = null;
		if (args.length != 4)
			fail("Invalid Number of Arguments");
	    else{
			BufferedReader CSVFile = 
					new BufferedReader(new FileReader(args[0]));
			Integer NNodes = Integer.parseInt(args[2]);
			Integer MaxTime = Integer.parseInt(args[3]);
			CSVFile.close();
			GraphReporter reporter = new GraphReporter ();
			ONEGraphReader reader = new ONEGraphReader(args[0],NNodes+1,MaxTime);
//			System.out.println("Max time is: " + MaxTime + " and NNodes is: "
//					+ NNodes);
			//EvolvingGraph gp = new EvolvingGraph();
			try {
				reporter.readGraph (reader);
//				File f = new File (args[1]);
//				String fname = f.getName();
//				int mid = fname.lastIndexOf(".");
//				String name = fname.substring(0,mid);
//				String ext = fname.substring(mid+1,fname.length());
//				String dir = f.getParent();
//				String Out1 = "";
//				String Out2 = "";
//				System.out.println(dir);
//				if (dir == null || dir.isEmpty()){
//					System.out.println("No Parent Dir");
//					Out1 = name + "1." + ext;
//					Out2 = name + "2." + ext;
//				}
//				else{
//					System.out.println("There is a Parent Dir");
//					Out1 = dir + "/" + name + "1." + ext;
//					Out2 = dir + "/" + name + "2." + ext;
//				}
//				System.out.println(Out2);
//				f.delete();
//				File f1 = new File (Out1);
//				File f2 = new File (Out2);
//				f1.delete();
//				f2.delete();
//				reporter.journeyReport(Out1, Out2, 0);
				Result = reporter.journeyUnreachableReport();
//				for(int i=0; i<Result.length;i++)
//					System.out.println(Result[i]);
				//reporter.journeyClassifier(args[1]);
			} catch (Exception e) {
				fail("MaxTime not initialized2.");
			}
			//gp = reporter.getGraph();		
			//System.out.print(gp.getNumOfNodes());
			//System.out.println(gp.getMaxTime());
			//System.out.print("\n");
			//System.out.print(gp.getNumOfEdges());
		}
		return Result;
	}
	
	
	public double [] TestJourneyUnreachableReportString(int[][] args, int n, int T) throws Exception {
		double [] Result = null;
		if (args.length == 0)
			fail("No information given!");
	    else{
	    	System.out.println("number of nodes is " + n + "time is " + T);
			GraphReporter reporter = new GraphReporter ();
			StringGraphReader reader = new StringGraphReader(args, n, T);
			try {
				reporter.readGraph (reader);
				System.out.println("Graph is Read");
				Result = reporter.journeyUnreachableReport();
			} catch (Exception e) {
				fail("MaxTime not initialized2.");
			}
		}
		return Result;
	}
	
//	public static void main(String[] args) {
//		String[] NewArgs = new String[4];
//		NewArgs[0] = args[0];
//		NewArgs[1] = args[1];
//		
//	}
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2)
			System.out.println("There is an Error");
		else{
			BufferedReader CSVFile = 
					new BufferedReader(new FileReader(args[0]));
			System.out.println("File " + args[0] + " is read");
			ArrayList<Double> ls1 = new ArrayList<Double>();
			Set<Integer> ls2 = new HashSet<Integer>();
			
			
			
			String dataRow = CSVFile.readLine(); 
			while (dataRow != null){
				   String[] dataArray = dataRow.split(" ");
				   ls1.add(Double.valueOf(dataArray[0]));
				   ls2.add(Integer.valueOf(dataArray[2]));
				   System.out.println(); // Print the data line.
				   dataRow = CSVFile.readLine(); // Read next line of data.
				  }
				  // Close the file once all data has been read.
			System.out.println(ls1);
			Integer NNodes = ls2.size();
			Integer MaxTimeInt = Collections.max(ls1).intValue();
			CSVFile.close();
			GraphReporter reporter = new GraphReporter ();
			ONEGraphReader reader = new ONEGraphReader(args[0],NNodes+1,MaxTimeInt);
			System.out.println("Max time is: " + MaxTimeInt + " and NNodes is: "
					+ NNodes);
			EvolvingGraph gp = new EvolvingGraph();
			try {
				reporter.readGraph (reader);
				File f = new File (args[1]);
				String fname = f.getName();
				int mid = fname.lastIndexOf(".");
				String name = fname.substring(0,mid);
				String ext = fname.substring(mid+1,fname.length());
				String dir = f.getParent();
				String Out1 = "";
				String Out2 = "";
				System.out.println(dir);
				if (dir == null || dir.isEmpty()){
					System.out.println("No Parent Dir");
					Out1 = name + "1." + ext;
					Out2 = name + "2." + ext;
				}
				else{
					System.out.println("There is a Parent Dir");
					Out1 = dir + "/" + name + "1." + ext;
					Out2 = dir + "/" + name + "2." + ext;
				}
				System.out.println(Out2);
				f.delete();
				File f1 = new File (Out1);
				File f2 = new File (Out2);
				f1.delete();
				f2.delete();
				reporter.journeyReport(Out1, Out2, 0);
				double[] Result = reporter.journeyUnreachableReport();
				//for(int i=0; i<Result.length;i++)
					//System.out.println(Result[i]);
				//reporter.journeyClassifier(args[1]);
			} catch (Exception e) {
				fail("MaxTime not initialized2.");
			}
			gp = reporter.getGraph();		
			System.out.print(gp.getNumOfNodes());
			System.out.print("\n");
			System.out.print(gp.getNumOfEdges());
		}
	}
	
}
