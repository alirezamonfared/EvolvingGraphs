/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithmslist;

import java.util.List;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

/**
 * Interface for the algorithms that return a list of lists of journeys. It is used by the tradeoff
 * algorithms
 *
 */
public interface AlgorithmList {
	List<List<Journey>> calculate(EvolvingGraph graph, Node s);
	List<List<Journey>> calculate(EvolvingGraph graph, Node s, int initTime);
	List<List<Journey>> getPareto();
}
