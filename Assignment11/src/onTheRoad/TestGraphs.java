package onTheRoad;
/**
 * Testing code for graph algorithms
 * @author Kim Bruce
 * @date 11/27/2017
 */

import java.util.Iterator;

import structure5.Association;
import structure5.Edge;
import structure5.Graph;
import structure5.GraphListDirected;

import java.util.ArrayList;

public class TestGraphs {
	/**
	 * Testing code for graph algorithms
	 * @param args  ignored
	 */
	public static void main(String[] args) {
		// Build a test graph
		Graph<String, Double> testGraph = new GraphListDirected<String, Double>();
		testGraph.add("a");
		testGraph.add("b");
		testGraph.add("c");
		testGraph.add("d");
		testGraph.addEdge("a", "b", 1.0);
		testGraph.addEdge("a", "c", 1.0);
		testGraph.addEdge("b", "d", 3.0);
		testGraph.addEdge("c", "d", 3.0);
		// rev is graph with reversed edges
		Graph<String, Double> rev = GraphAlgorithms.graphEdgeReversal(testGraph);
		Iterator<Edge<String, Double>> it = rev.edges();
		// print out edges in reversed graph
		while (it.hasNext()) {
			Edge<String, Double> edge = it.next();
			System.out.println(edge + " " + edge.label());
		}
		
		// Run breadth-first search and see if everything visited
		GraphAlgorithms.breadthFirstSearch(testGraph, "a");
		for (String vertex : testGraph) {
			System.out.println(vertex + " is visited: "
					+ testGraph.isVisited(vertex));
		}
		// Check to see if testGraph is strongly-connected.
		if (GraphAlgorithms.isStronglyConnected(testGraph)) {
			System.out.println("Strongly connected");
		} else {
			System.out.println("not strongly connected");
		}
		// Add another vertex and some edges
		testGraph.addEdge("d", "a", 3.0);
		testGraph.add("e");
		testGraph.addEdge("d", "e", 3.0);
		testGraph.addEdge("e", "b", 3.0);
		// See if it is now strongly connected
		if (GraphAlgorithms.isStronglyConnected(testGraph)) {
			System.out.println("Strongly connected");
		} else {
			System.out.println("not strongly connected");
		}
		// Find the shortest path from "a" to "e" and print it.
		Association<Double, ArrayList<Edge<String, Double>>> result = 
				GraphAlgorithms.getShortestPath(testGraph, "a", "e");
		System.out.println("Shortest path from a to e is");
		GraphAlgorithms.printShortestPathDistance(result);
	}

/*
The output from this program should be:
<Edge: b -> a> 1.0
<Edge: c -> a> 1.0
<Edge: d -> b> 3.0
<Edge: d -> c> 3.0
a is visited: true
b is visited: true
c is visited: true
d is visited: true
not strongly connected
Strongly connected
Shortest path from a to e is
   Begin at a
   Continue to b (1.0 miles)
   Continue to d (3.0 miles)
   Continue to e (3.0 miles)
Total distance: 7.0 miles.
*/
}
