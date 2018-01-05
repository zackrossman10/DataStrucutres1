package onTheRoad;

import java.util.ArrayList;
import java.util.List;

import structure5.Association;
import structure5.Edge;
import structure5.Graph;

/**
 * Class whose main method reads in description of graph and trip requests,
 * and then returns shortest paths (according to distance or time) from one
 * given vertex to another.  The input file is given by a command line argument.
 * @author zrossman and elewis
 * @date 11/30/17
 */

public class Optimizer {
	public static void main(String[] args) {
		FileParser parse = new FileParser(args[0]);
		List<TripRequest> requests = parse.getTrips();
		//construct a graph for processing distance requests
		Graph<String, Double> graphDistance = parse.makeGraph(true);
		//construct a graph for processing time requests
		Graph<String, Double> graphTime = parse.makeGraph(false);
		//process each trip in requests
		for(TripRequest trip : requests){
			
			//use the distance graph to find and print the shortest path in terms of distance if
			//specified by the trip
			if(trip.isDistance()){
				System.out.println("Shortest distance from "+trip.getStart()+" to "+trip.getEnd());
				Association<Double, ArrayList<Edge<String, Double>>> result = 
						GraphAlgorithms.getShortestPath(graphDistance, trip.getStart(), trip.getEnd());
				GraphAlgorithms.printShortestPathDistance(result);
			}else{
				System.out.println("Shortest time from "+trip.getStart()+" to "+trip.getEnd());
				//use the time graph to find and print the shortest path in terms of time if
				//specified by the trip
				Association<Double, ArrayList<Edge<String, Double>>> result = 
						GraphAlgorithms.getShortestPath(graphTime, trip.getStart(), trip.getEnd());
				GraphAlgorithms.printShortestPathTime(result);
			}
			System.out.println();
		}
	}
}
