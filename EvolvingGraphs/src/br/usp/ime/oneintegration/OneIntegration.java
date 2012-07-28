/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.oneintegration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import br.usp.ime.algorithms.Algorithm;
import br.usp.ime.algorithms.FastestAlgorithm;
import br.usp.ime.algorithms.ForemostAlgorithm;
import br.usp.ime.algorithms.MiddleAlgorithm;
import br.usp.ime.algorithms.ShortestAlgorithm;
import br.usp.ime.algorithmslist.FastestTradeoff;
import br.usp.ime.algorithmslist.ShortestTradeoff;
import br.usp.ime.evolvinggraph.Edge;
import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.graphreader.GraphReader;
import br.usp.ime.graphreader.ONEGraphReader;

/**
 * Class that performs the integration with the ONE simulator's output reports. For a given report, this class
 * can generate the ONE router classes for the foremost, shortest, fastest and several middle optimal journeys.
 * The report files are taken directly from the ONE directory and it is assumed this directory is in the same folder
 * as the EvolvingGraphs directory.
 * @author Cesar Gamboa Machado
 * @author Paulo Henrique Floriano
 *
 */
public class OneIntegration {

	private static final String ONE_DIR = "../one_1.3.0/";
	private static final String FOREMOST_HASH_CLASS = "ForemostJourneyHash";
	private static final String SHORTEST_HASH_CLASS = "ShortestJourneyHash";
	private static final String FASTEST_HASH_CLASS = "FastestJourneyHash";

	private static final String MIDDLEH_HASH_CLASS = "MiddleHJourneyHash";
	private static final String MIDDLEA_HASH_CLASS = "MiddleAJourneyHash";
	private static final String MIDDLER_HASH_CLASS = "MiddleRJourneyHash";
	private static final String QUARTERH_HASH_CLASS = "QuarterHJourneyHash";
	private static final String TQUARTERH_HASH_CLASS = "TQuarterHJourneyHash";

	private static final String MIDDLETT_HASH_CLASS = "MiddleTTJourneyHash";
	private static final String FMIDDLEA_HASH_CLASS = "FMiddleAJourneyHash";
	private static final String FMIDDLER_HASH_CLASS = "FMiddleRJourneyHash";
	private static final String QUARTERTT_HASH_CLASS = "QuarterTTJourneyHash";
	private static final String TQUARTERTT_HASH_CLASS = "TQuarterTTJourneyHash";

	private static final String foremostOutputFile = ONE_DIR + "eg/" + FOREMOST_HASH_CLASS + ".java";
	private static final String shortestOutputFile = ONE_DIR + "eg/" + SHORTEST_HASH_CLASS + ".java";
	private static final String fastestOutputFile = ONE_DIR + "eg/" + FASTEST_HASH_CLASS + ".java";

	private static final String middlehOutputFile = ONE_DIR + "eg/" + MIDDLEH_HASH_CLASS + ".java";
	private static final String middleaOutputFile = ONE_DIR + "eg/" + MIDDLEA_HASH_CLASS + ".java";
	private static final String middlerOutputFile = ONE_DIR + "eg/" + MIDDLER_HASH_CLASS + ".java";
	private static final String quarterhOutputFile = ONE_DIR + "eg/" + QUARTERH_HASH_CLASS + ".java";
	private static final String tquarterhOutputFile = ONE_DIR + "eg/" + TQUARTERH_HASH_CLASS + ".java";

	private static final String middlettOutputFile = ONE_DIR + "eg/" + MIDDLETT_HASH_CLASS + ".java";
	private static final String fmiddleaOutputFile = ONE_DIR + "eg/" + FMIDDLEA_HASH_CLASS + ".java";
	private static final String fmiddlerOutputFile = ONE_DIR + "eg/" + FMIDDLER_HASH_CLASS + ".java";
	private static final String quarterttOutputFile = ONE_DIR + "eg/" + QUARTERTT_HASH_CLASS + ".java";
	private static final String tquarterttOutputFile = ONE_DIR + "eg/" + TQUARTERTT_HASH_CLASS + ".java";

	public static void main(String[] args) {
		if (args.length< 3) {
			System.out.println ("Too few arguments. Use: ant OneIntegration -Dargs\"[nodes] [simTime] [scenario]\"\n");
			return;
		}
		int nodesNum = Integer.parseInt(readArg(args, 0, "-1"));
		int maxTime = Integer.parseInt(readArg(args, 1, "-1"));
		String routerClass = readArg(args, 2, "");

		String connFile = ONE_DIR + "reports/" + routerClass +
		"_ConnectivityONEReport.txt";
		String messFile = ONE_DIR + "reports/" + routerClass +
		"_MessageCreationReport.txt";
		boolean [] toRun = new boolean [14];

		if (routerClass.equals("eg")) {
			for (int i = 3; i < args.length; i++) {
				String alg = readArg(args, i, "-1");
				if (alg.equals("fm"))
					toRun[1] = true;
				else if (alg.equals("sh"))
					toRun[2] = true;
				else if (alg.equals("fs"))
					toRun[3] = true;
				else if (alg.equals("mh"))
					toRun[4] = true;
				else if (alg.equals("ma"))
					toRun[5] = true;
				else if (alg.equals("mrh"))
					toRun[6] = true;
				else if (alg.equals("qh"))
					toRun[7] = true;
				else if (alg.equals("tqh"))
					toRun[8] = true;
				else if (alg.equals("fmtt"))
					toRun[9] = true;
				else if (alg.equals("fma"))
					toRun[10] = true;
				else if (alg.equals("fmr"))
					toRun[11] = true;
				else if (alg.equals("fqtt"))
					toRun[12] = true;
				else if (alg.equals("ftqtt"))
					toRun[13] = true;
			}
		}

		OneIntegration one = new OneIntegration();
		GraphReader reader = new ONEGraphReader (connFile, nodesNum, maxTime);

		if (routerClass.equals("foremost") || toRun[1]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("Foremost Algorithm:");
				Algorithm fm = new ForemostAlgorithm();
				one.parse(eg, messFile, foremostOutputFile, fm, FOREMOST_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("shortest") || toRun[2]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("Shortest Algorithm:");
				Algorithm sh = new ShortestAlgorithm();
				one.parse(eg, messFile, shortestOutputFile, sh, SHORTEST_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("fastest") || toRun[3]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("Fastest Algorithm:");
				Algorithm fs = new FastestAlgorithm();
				one.parse(eg, messFile, fastestOutputFile, fs, FASTEST_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("middlehop") || toRun[4]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("MiddleHop Algorithm:");
				Algorithm middle = new MiddleAlgorithm('a', new ShortestTradeoff ());
				one.parse(eg, messFile, middlehOutputFile, middle, MIDDLEH_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("middlearr") || toRun[5]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("MiddleArr Algorithm:");
				Algorithm middle = new MiddleAlgorithm('b', new ShortestTradeoff ());
				one.parse(eg, messFile, middleaOutputFile, middle, MIDDLEA_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("middlernd") || toRun[6]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("MiddleRnd Algorithm:");
				Algorithm middle = new MiddleAlgorithm('c', new ShortestTradeoff ());
				one.parse(eg, messFile, middlerOutputFile, middle, MIDDLER_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("quarterhop") || toRun[7]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("QuarterHop Algorithm:");
				Algorithm middle = new MiddleAlgorithm('d', new ShortestTradeoff ());
				one.parse(eg, messFile, quarterhOutputFile, middle, QUARTERH_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("3quarterhop") || toRun[8]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("3QuarterHop Algorithm:");
				Algorithm middle = new MiddleAlgorithm('e', new ShortestTradeoff ());
				one.parse(eg, messFile, tquarterhOutputFile, middle, TQUARTERH_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("middlett") || toRun[9]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("MiddleTT Algorithm:");
				Algorithm middle = new MiddleAlgorithm('f', new FastestTradeoff ());
				one.parse(eg, messFile, middlettOutputFile, middle, MIDDLETT_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("fmiddlearr") || toRun[10]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("FMiddleA Algorithm:");
				Algorithm middle = new MiddleAlgorithm('g', new FastestTradeoff ());
				one.parse(eg, messFile, fmiddleaOutputFile, middle, FMIDDLEA_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("fmiddlernd") || toRun[11]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("FMiddleR Algorithm:");
				Algorithm middle = new MiddleAlgorithm('h', new FastestTradeoff ());
				one.parse(eg, messFile, fmiddlerOutputFile, middle, FMIDDLER_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("quartertt") || toRun[12]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("QuarterTT Algorithm:");
				Algorithm middle = new MiddleAlgorithm('i', new FastestTradeoff ());
				one.parse(eg, messFile, quarterttOutputFile, middle, QUARTERTT_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (routerClass.equals("tquartertt") || toRun[13]){
			EvolvingGraph eg;
			try {
				eg = reader.readEvolvingGraph();
				System.out.println("TQuarterTT Algorithm:");
				Algorithm middle = new MiddleAlgorithm('j', new FastestTradeoff ());
				one.parse(eg, messFile, tquarterttOutputFile, middle, TQUARTERTT_HASH_CLASS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static String readArg(String[] args, int pos, String standard) {
		if (args.length > pos) return args[pos];
		return standard;
	}

	public void parse(EvolvingGraph eg, String messageReport, String routeFile, Algorithm alg, String hashClass) {
		File f = new File(messageReport);
		int numMessages = 0;
		try {
			Scanner input = new Scanner(f);
			FileWriter output = new FileWriter(routeFile);
			writeHeader (output, hashClass);
			while (input.hasNextLine()) {
				if (processLine(eg, input, output, alg))
					numMessages ++;
				input.nextLine();
			}
			writeTrailer(output);
			output.close();
		} catch (Exception e) {
			System.out.println("Parser Error:");
			e.printStackTrace();
		}
		System.out.println("Routes found for " + numMessages + " messages.");
	}

	public boolean processLine(EvolvingGraph eg, Scanner input, FileWriter output, 
			Algorithm alg) throws Exception {
		input.findInLine("(\\d+).0000 (\\w+) (\\d+) (\\d+)");
		MatchResult result = input.match();

		int time = Integer.parseInt(result.group(1));
		String name = result.group(2);
		int orig = Integer.parseInt(result.group(3));
		int dest = Integer.parseInt(result.group(4));

		Journey j = findJourney(eg, alg, time, orig, dest);
		if (j == null || j.getEdges().isEmpty())
			return false;
		outputJourneyToFile(output, name, j);
		eg.removeJourney(j);
		return true;
	}

	private Journey findJourney(EvolvingGraph eg, Algorithm alg, int time, int orig, int dest) {
		List<Journey> journeys = alg.calculate(eg, eg.getNode(orig), time);
		Journey j = journeys.get(dest);
		return j;
	}



	private void outputJourneyToFile(FileWriter output, String name, Journey j) throws IOException {
		ArrayList<Edge> e = j.getEdges();
		ArrayList<Integer> t = j.getTimes();
		for (int i = 0; i < e.size(); i++) {
			output.write("		messages.put(\"" + name + 
					"N" + e.get(i).getU().getId() + 
					"T" + t.get(i) + "\", " + e.get(i).getV().getId() + ");\n");
		}
	}

	private void writeHeader(FileWriter fw, String className) throws IOException {
		fw.write("package eg;\n" +
				"import java.util.HashMap;\n" +
				"\n" +
				"public class "+ className + " {\n" +
				"	static HashMap<String, Integer> messages = " +
				"new HashMap<String, Integer>();\n\n" +
		"	static{\n");
	}

	private void writeTrailer(FileWriter fw) throws IOException {
		fw.write("	}\n\n" +
				"	public HashMap<String, Integer> getMessages() {\n" +
				"		return messages;\n" +
				"	}\n" +
		"}");
	}


}
