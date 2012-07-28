/* 
 * Copyright 2011, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.graphreader;

import java.io.FileNotFoundException;

import br.usp.ime.evolvinggraph.EvolvingGraph;

/**
 * Interface to be implemented by classes that read evolving graphs from files.
 * @author Paulo Henrique Floriano
 *
 */
public interface GraphReader {
	public EvolvingGraph readEvolvingGraph () throws FileNotFoundException;

}
