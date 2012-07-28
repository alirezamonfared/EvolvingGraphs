package br.usp.ime.graphreport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import br.usp.ime.graphreader.DieselNetReader;
import br.usp.ime.graphreader.DefaultGraphReader;
import br.usp.ime.graphreader.GraphReader;


public class GraphReporterTest {

	@Test
	public void testReadGraph () {
		GraphReporter reporter = new GraphReporter ();
		GraphReader reader = new DefaultGraphReader("input/teste1.in");
		try {
			reporter.readGraph (reader);
		} catch (Exception e) {
			fail("MaxTime not initialized.");
		}
		assertNotNull (reporter.getGraph());
	}
	
	@Test
	public void testFileExists () {
		GraphReporter reporter = new GraphReporter ();
		assertTrue(reporter.fileExists("input/teste1.in"));
		assertFalse(reporter.fileExists("arquivoquenaoexiste.txt"));
	}
	
	@Test
	public void testReportMessages1 () {
		GraphReporter reporter = new GraphReporter ();
		reporter.setAuthor("Bozo");
		reporter.setTraceName("Teste de Mensagens");
		GraphReader reader = new DefaultGraphReader("input/teste3.in");
		File f = new File ("reports/testreport1.txt");
		f.delete();
		try {
			reporter.readGraph (reader);
			reporter.report ("input/messageinput2.in", "reports/testreport1.txt");
		} catch (Exception e) {
			fail("MaxTime not initialized.");
		}
		f = new File("reports/testreport1.txt");
		try {
			Scanner sc = new Scanner(f);
			assertTrue(sc.nextLine().equals("#This report is for the trace: Teste de Mensagens"));
			assertTrue(sc.nextLine().equals("#Uploaded to DTNTES by: Bozo"));
			assertTrue(sc.nextLine().equals("#No link to the trace was provided"));
			assertTrue(sc.next().equals("Message"));
			assertTrue(sc.next().equals("EAD"));
			assertTrue(sc.next().equals("MinHop"));
			assertTrue(sc.next().equals("MinTrs"));
			
			assertEquals(1, sc.nextInt());
			assertEquals(1, sc.nextInt());
			assertEquals(1, sc.nextInt());
			assertEquals(0, sc.nextInt());
			assertEquals(2, sc.nextInt());
			assertEquals(1, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertEquals(0, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertEquals(2, sc.nextInt());
			assertEquals(1, sc.nextInt());
			assertEquals(0, sc.nextInt());
		} catch (FileNotFoundException e) {
			fail("File not found");
		}
	}
	
	@Test
	public void testReportMessages2 () {
		GraphReporter reporter = new GraphReporter ();
		reporter.setAuthor("Bozo");
		reporter.setTraceName("Teste de Mensagens");
		GraphReader reader = new DefaultGraphReader("input/teste4.in");
		File f = new File ("reports/testreport2.txt");
		f.delete();
		try {
			reporter.readGraph (reader);
			reporter.report ("input/messageinput3.in", "reports/testreport2.txt");
		} catch (Exception e) {
			fail("MaxTime not initialized.");
		}
		f = new File("reports/testreport2.txt");
		try {
			Scanner sc = new Scanner(f);
			assertTrue(sc.nextLine().equals("#This report is for the trace: Teste de Mensagens"));
			assertTrue(sc.nextLine().equals("#Uploaded to DTNTES by: Bozo"));
			assertTrue(sc.nextLine().equals("#No link to the trace was provided"));
			assertTrue(sc.next().equals("Message"));
			assertTrue(sc.next().equals("EAD"));
			assertTrue(sc.next().equals("MinHop"));
			assertTrue(sc.next().equals("MinTrs"));
			
			assertEquals(1, sc.nextInt());
			assertEquals(10, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertEquals(2, sc.nextInt());
			assertEquals(6, sc.nextInt());
			assertEquals(2, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertEquals(3, sc.nextInt());
			assertTrue(sc.next().equals("-"));
			assertTrue(sc.next().equals("-"));
			assertTrue(sc.next().equals("-"));
		} catch (FileNotFoundException e) {
			fail("File not found");
		}
	}
	
//	@Test
//	public void testReportMessages3 () {
//		GraphReporter reporter = new GraphReporter ();
//		GraphReader reader = new ONEGraphReader("input/testeONE5.in", 240, 5000);
//		try {
//			reporter.readGraph (reader);
//			reporter.report ("input/messageinput4.in", "testreport3.txt");
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("MaxTime not initialized.");
//		}
//	}
	
	@Test
	public void testJourneyReport () {
		GraphReporter reporter = new GraphReporter ();
		reporter.setAuthor("Bozo");
		reporter.setLink("www.com.br");
		reporter.setTraceName("Teste de Jornadas");
		GraphReader reader = new DefaultGraphReader("input/teste3.in");
		File f = new File ("reports/teststats.txt");
		f.delete();
		f = new File ("reports/testjourneys.txt");
		f.delete();
		try {
			reporter.readGraph (reader);
			reporter.journeyReport("reports/teststats.txt", "reports/testjourneys.txt", 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
	
	@Test
	public void testConnectivityReport () {
		GraphReporter reporter = new GraphReporter ();
		reporter.setAuthor("Boça");
		reporter.setLink("www.org");
		reporter.setTraceName("Teste de Conectividade");
		File f = new File ("reports/testconn.txt");
		f.delete();
		GraphReader reader = new DefaultGraphReader("input/teste4.in");
		try {
			reporter.readGraph (reader);
			reporter.connectivityReport("reports/testconn.txt");
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
	
	@Test
	public void testDieselNet () {
		GraphReader reader = new DieselNetReader("input/testdiesel.in");
		try {
			System.out.println(reader.readEvolvingGraph());
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
	
	@Test
	public void testDieselNet2 () {
		GraphReader reader = new DieselNetReader("input/testdiesel2.in");
		try {
			System.out.println(reader.readEvolvingGraph());
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
	
	@Test
	public void testPeriodicityReport () {
		GraphReporter reporter = new GraphReporter ();
		File f = new File ("reports/testperi.txt");
		f.delete();
		reporter.setAuthor("Bozo");
		reporter.setLink("www.com.br");
		reporter.setTraceName("Teste de Periodicidade");
		GraphReader reader = new DefaultGraphReader("input/teste4.in");
		try {
			reporter.readGraph (reader);
			reporter.periodicityReport("reports/testperi.txt");
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
	
	@Test
	public void testPeriodicityReport2 () {
		GraphReporter reporter = new GraphReporter ();
		File f = new File ("reports/testperi2.txt");
		f.delete();
		GraphReader reader = new DefaultGraphReader("input/testeperi.in");
		try {
			reporter.readGraph (reader);
			reporter.periodicityReport("reports/testperi2.txt");
		} catch (Exception e) {
			e.printStackTrace();
			fail("MaxTime not initialized.");
		}
	}
}
