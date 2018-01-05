package onTheRoad;

/** Common algorithms for Graphs.  All assume working with a directed graph.
 * Written ????
 * @author ???? (based on algorithms in Bailey, Java Structures)
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import structure5.Association;
import structure5.ComparableAssociation;
import structure5.Map;
import structure5.MapList;
import structure5.QueueList;
import structure5.Graph;
import structure5.GraphListDirected;
import structure5.Edge;

public class GraphAlgorithms {

	/**
	 * @param g
	 *            directed graph
	 * @return graph like g except all edges are reversed
	 */
	public static <V, E> Graph<V, E> graphEdgeReversal(Graph<V, E> g) {		
		//TODO create a new directed graph that is stored as an adjacency list
		Graph<V,E> newGraph = new GraphListDirected<V, E>();		
		
		//copy the vertices from the original graph g
		Iterator<V> iterate =  g.iterator();
		
		//iterates through vertices in the orginal graph copying them to the new graph and stores a temp copy
		//of all the vertices
		while (iterate.hasNext() ) {
			newGraph.add((V) iterate.next());
		}
		
		// iterate through the edges of g and add a new edge of reversed direction in the new graph
		Iterator<Edge<V,E>> edgeIterate = g.edges();
		
		while(edgeIterate.hasNext()){
			
			Edge<V,E> temp = edgeIterate.next();
			newGraph.addEdge(temp.there(), temp.here() , temp.label());
		}		
		return newGraph;
	}

	/**
	 * Perform breadTH-first search of g from vertex start. At end, can ask
	 * vertices in if they were visited
	 * 
	 * @param g
	 *            directed graph
	 * @param start
	 *            starting vertex for search
	 */
	public static <V, E> void breadthFirstSearch(Graph<V, E> g, V start) {
		//make all the vertices unvisited
		g.reset();
		
		//TODO follow the BFS algorithm described in slides using a queue
		
		//store the values in the order we're seeing them
		@SuppressWarnings("unchecked")
		structure5.Queue<V> q = new QueueList<V>();

		V first = start;
		q.enqueue(first);
		//while the iterate has a vertex - want to look at every vertex
		while (!q.isEmpty()) {
			
			//capture that vertex
			V temp = q.dequeue();
			
			//if the vertex is unvisited
			if (!g.isVisited(temp)){
				
				//visit it
				g.visit(temp);
				
				//create an iterator of its neighbor vertices
				Iterator<V> neighbor =  g.neighbors(temp);
				
				//iterate through the neighbor vertices 
				while(neighbor.hasNext()){
					
					//capture the neighbor
					V temp1 = neighbor.next();
					
					//check if the neighbor we're inspecting isn't visited then enqueue it
					if(!(g.isVisited(temp1))){
						q.enqueue(temp1);
					}
						
				}				
			}
			
		}	
		
	}

	/**
	 * @param g
	 *            directed graph
	 * @return whether graph g is strongly connected.
	 */
	public static <V, E> boolean isStronglyConnected(Graph<V, E> g) {
		Iterator<V> getRandom = g.iterator();
		//get a random vertice in g
		V random = getRandom.next();
		//perform a BFS from this vertice
		GraphAlgorithms.breadthFirstSearch(g, random);
		//if original directed graph isn't connected, can't be 
		//strongly connected
		if(!GraphAlgorithms.allVisited(g)){
			return false;
		}
		//reverse the graph's edges
		Graph<V,E> rev = GraphAlgorithms.graphEdgeReversal(g);
		//perform a BFS from the same node as before on the reversed graph
		GraphAlgorithms.breadthFirstSearch(rev, random);
		//if reverse-directed graph isn't connected, can't be 
		//strongly connected
		if(!GraphAlgorithms.allVisited(rev)){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param g a directed graph that we want to inspect 
	 * @return whether all vertices of g are visited
	 */
	public static <V, E> boolean allVisited(Graph<V, E> g){
		for(V vertex : g){
			if(!g.isVisited(vertex)) return false;
		}
		return true;
		
	}

	/**
	 * Perform Dijkstra's algorithm on graph g from vertex start.
	 * 
	 * @param g
	 * @param start
	 * @return map taking each vertex to cost to get there from start and the
	 *         last edge in a shortest path to the vertex
	 */
	public static Map<String, ComparableAssociation<Double, Edge<String, Double>>> dijkstra(
			Graph<String, Double> g, String start) {
		
		//create a new priority queue which associates vertices with 
		//1) the cost from the start node to the given node,
		//2) the last edge in the shortest path to the given node
		Queue<ComparableAssociation<Double, Edge<String, Double>>> dij = 
				new PriorityQueue<ComparableAssociation<Double, Edge<String, Double>>>();
		
		//declare a map to accumulate the associations of a vertex with a comparable association
		Map<String, ComparableAssociation<Double, Edge<String, Double>>> map = new
				MapList<String, ComparableAssociation<Double, Edge<String, Double>>>();	
		
		//for each vertex in g, add a new association with a cost of (basically) infinity to the priority queue
		for(String vertex : g){
			ComparableAssociation<Double, Edge<String, Double>> cost = 
					new ComparableAssociation<Double, Edge<String, Double>> (10000.0, new Edge<String, Double>(null, vertex, null, true));
			dij.add(cost);
		}
		
		//create a new association for the start vertex with a cost of 0
		//add to the priority queue
		ComparableAssociation<Double, Edge<String, Double>> start1 =
				new ComparableAssociation<Double, Edge<String, Double>>(0.0, 
						new Edge<String, Double>(null, start, null, true));
		dij.add(start1);
		
		//for each element of the priority queue...
		while(!dij.isEmpty()){
			
			//get the highest priority association (the one with the lowest cost)
			ComparableAssociation<Double, Edge<String, Double>> top = dij.remove();
			//get the that association's vertice
			String vertex = top.getValue().there();
			
			//create association between the min vertex and this association in the map, 
			//as long as the vertex doesn't already exist in the map
			if(map.containsKey(vertex)) continue;
			map.put(vertex, top);
			
			//ceate an iterator which gets the vertices adjacent to temp
			Iterator<String> adjacent = g.neighbors(vertex);
			
			//access all the adjacent vertices of vertex
			while(adjacent.hasNext()){
				//get the edge that links these adjacent vertices
				Edge<String, Double> edge= g.getEdge(vertex, (String) adjacent.next());
				//determine the tenative cost of travelling to start to adjacent.next()
				double tentative = edge.label() + top.getKey();
				
				//create a new ComparableAssociation with an updated key to the priority queue,
				//which will sort the associations by key
				dij.add(new ComparableAssociation<Double, Edge<String, Double>>(tentative, edge));
			}
		}
		//return the completed map
		return map;
	}

	/**
	 * Compute shortest path from start to end using Dijkstra's algorithm
	 * 
	 * @param g
	 *            directed graph
	 * @param start
	 *            starting node in search for shortest path
	 * @param end
	 *            ending node in search for shortest path
	 * @return pair of the total cost from start to end in shortest path as well
	 *         as a list of edges in that shortest path
	 */
	public static Association<Double, ArrayList<Edge<String, Double>>> getShortestPath(
			Graph<String, Double> g, String start, String end) {
		//make a map of associations of shortest paths using dijkstra's alg
		Map<String, ComparableAssociation<Double, Edge<String, Double>>> map = GraphAlgorithms.dijkstra(g, start);
		//get the cost of travelling to the end vertex from map
		Double cost = map.get(end).getKey();		
		
		//create an arraylist to hold the edges that represent shortest path from start to end
		ArrayList<Edge<String, Double>> path = new ArrayList<Edge<String, Double>>();
		
		//get the ending edge in the correct path to end
		Edge<String, Double> previousEdge= map.get(end).getValue( );
		//get the parent of the end node
		String parent = previousEdge.here();
		
		while(parent != null){
			//add the previous edge to the array
			path.add(previousEdge);
			previousEdge= map.get(parent).getValue();
			//get the parent of the end node
			parent = previousEdge.here();
		}
		
		return new Association<Double, ArrayList<Edge<String, Double>>>(cost, path);
	}

	/**
	 * Using the output from Dijkstra, print the shortest path (according to
	 * distance) between two nodes
	 * 
	 * @param pathInfo
	 *            Cost and path information from run of Djikstra
	 */
	public static void printShortestPathDistance(
			Association<Double, ArrayList<Edge<String, Double>>> pathInfo) {
		ArrayList<Edge<String, Double>> array = pathInfo.getValue();
		//print the starting vertex of the first edge
		System.out.println("Begin at "+array.get(array.size()-1).here());
		Edge<String, Double> currentEdge;
		for(int i=array.size()-1; i>=0; i--){
			//get the edge from index i
			currentEdge = array.get(i);
			//print the ending vertex of each edge
			System.out.println("Continue to "+currentEdge.there()+" ("+currentEdge.label()+" miles)");
		}
		System.out.println("Total distance: "+pathInfo.getKey()+" miles.");
	}

	/**
	 * Using the output from Dijkstra, print the shortest path (according to
	 * time) between two nodes
	 * 
	 * @param pathInfo
	 *            Pair consisting of total time and shortest times to each node
	 */
	public static void printShortestPathTime(
			Association<Double, ArrayList<Edge<String, Double>>> pathInfo) {
		ArrayList<Edge<String, Double>> array = pathInfo.getValue();
		//print the starting vertex of the first edge
		System.out.println("Begin at "+array.get(array.size()-1).here());
		Edge<String, Double> currentEdge;
		for(int i=array.size()-1; i>=0; i--){
			//get the edge from index i
			currentEdge = array.get(i);
			//print the ending vertex of each edge
			System.out.println("Continue to "+currentEdge.there()+" ("+GraphAlgorithms.hoursToHMS(currentEdge.label())+")");
		}
		System.out.println("Total time: "+GraphAlgorithms.hoursToHMS(pathInfo.getKey()));
		System.out.println();
	}
	
	/**
	 * Convert hours (in decimal) to
	 * 
	 * @param rawhours
	 *            time elapsed
	 * @return Equivalent of rawhours in hours, minutes, and seconds (to
	 *         nearest 10th of a second)
	 */
	private static String hoursToHMS(double rawhours) {
		int numHours = (int) rawhours;
		double leftover = rawhours-numHours;
		int tenthSeconds = (int)Math.round(leftover * 36000);
		int minutes = tenthSeconds/600;
		int tenthSecondsLeft = tenthSeconds - 600 * minutes;
		int seconds = tenthSecondsLeft/10;
		
		String print = "";
		
		if(numHours == 0){
			if(minutes == 0){
			}else{
				print += minutes+" mins ";
			}
		}else{
			print += numHours+" hrs " + minutes+" mins ";
		}
		return print + seconds+"."+tenthSeconds/1000+" secs";
	}
}
