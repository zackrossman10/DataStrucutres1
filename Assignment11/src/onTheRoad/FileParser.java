package onTheRoad;
/**
 * Class to read in and parse the input data that can then be used to
 * build the graph used in finding shortest paths
 * Written 11/24/2018
 * @author Kim Bruce
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import structure5.Graph;
import structure5.GraphListDirected;

public class FileParser {
	// list of all vertices & edges in graph being build
	private List<String> vertices = new ArrayList<String>();
	private List<Segment> segments = new ArrayList<Segment>();
	
	// List of all trips that should be calculated
	private List<TripRequest> trips = new ArrayList<TripRequest>();

	/**
	 * Parse input to obtain lists of vertices, edges, and trip requests.
	 * @param fileName
	 * 		file containing information on road network
	 */
	public FileParser(String fileName) {
		try {
			// file with input data on transportation network
			BufferedReader input = new BufferedReader(new FileReader(fileName));

			// get intersections
			String line = getDataLine(input);
			int numLocations = Integer.parseInt(line);

			for (int count = 0; count < numLocations; count++) {
				line = getDataLine(input);
				vertices.add(line.trim());
			}

			// get road segments
			line = getDataLine(input);
			int numSegments = Integer.parseInt(line);

			for (int count = 0; count < numSegments; count++) {
				line = getDataLine(input);
				Segment temp = new Segment(line);
				//Make sure all segments are legal, i.e., startIndex
				// and endIndex are legal for vertices.
				if(temp.getStart() < 0 || temp.getStart() >= numLocations || 
						temp.getEnd()<0 || temp.getEnd()>=numLocations){
					System.out.print("Illegal segment: "+temp.toString());
				}else{
					segments.add(temp);
				}
			}

			// get trip requests
			line = getDataLine(input);
			int numTrips = Integer.parseInt(line);

			for (int count = 0; count < numTrips; count++) {
				line = getDataLine(input);
				TripRequest temp = new TripRequest(line, vertices);
				//if trip request was illegal, don't add it to the arraylist of trips
				if(temp.getStart().equals("") || temp.getEnd().equals("")){
					System.out.println("Illegal trip request: "+temp.toString());
					System.out.println();
				}else{
					trips.add(temp);
				}
				
			}

			input.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @param input
	 *            file from which data is read
	 * @return next valid line of input, skips blank lines and lines starting
	 *         with #
	 * @throws IOException
	 */
	private String getDataLine(BufferedReader input) throws IOException {
		String line = input.readLine();
		while (line.trim().equals("") || line.charAt(0) == ('#')) {
			line = input.readLine();
		}

		return line;
	}

	/**
	 * 
	 * @return list of vertices (locations) from the input
	 */
	public List<String> getVertices() {
		return vertices;
	}

	/**
	 * 
	 * @return list of segments (edges) from the input
	 */
	public List<Segment> getSegments() {
		return segments;
	}

	/**
	 * 
	 * @return list of trip requests from input.
	 */
	public List<TripRequest> getTrips() {
		return trips;
	}

	/**
	 * Build graph from input file
	 * @param isDistance Whether to make graph with edges for distance or for time.
	 * @return  Graph representing file read in
	 */
	public Graph<String, Double> makeGraph(boolean isDistance) {
		// roadNetwork is graph to be returned
		Graph<String, Double> roadNetwork = new GraphListDirected<String, Double>();
		for (String vertex : vertices) {
			roadNetwork.add(vertex);
		}
		
		// add edges to roadNetwork
		for (Segment seg : segments) {
			int startIndex = seg.getStart();
			int endIndex = seg.getEnd();
			double cost;
			if (isDistance) {  // edges have distances
				cost = seg.getDistance();
			} else {           // edges represent time to traverse
				cost = seg.getDistance() / seg.getSpeed();
			}
			roadNetwork.addEdge(vertices.get(startIndex),
					vertices.get(endIndex), cost);
		}
		return roadNetwork;
	}

	// testing code for file parser
	public static void main(String[] args) {
		FileParser fp = new FileParser("C:/Users/AJ Rossman/Desktop/assignment11/data/sample.txt");

		Graph<String, Double> roadNetworkDistance = fp.makeGraph(true);
		Graph<String, Double> roadNetworkTime = fp.makeGraph(false);

		System.out.println("Distance graph:\n" + roadNetworkDistance);
		System.out.println("Time graph:\n" + roadNetworkTime);
	}
}
