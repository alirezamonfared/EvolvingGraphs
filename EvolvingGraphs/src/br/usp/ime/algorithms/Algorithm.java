/* 
 * Copyright 2010, Adriano Tabarelli, Caio Cestari Silva, Cassia Garcia Ferreira, Cesar Gamboa Machado, Paulo Henrique Floriano
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package br.usp.ime.algorithms;

import java.util.List;

import br.usp.ime.evolvinggraph.EvolvingGraph;
import br.usp.ime.evolvinggraph.Journey;
import br.usp.ime.evolvinggraph.Node;

/**
 * Interface for a journey algorithm in an evolving graph.
 *
 */
public interface Algorithm {
	List<Journey> calculate(EvolvingGraph graph, Node s);
	List<Journey> calculate(EvolvingGraph graph, Node s, int initTime);
	int getOptimizedParameter(Journey j);
	int getType();
}
