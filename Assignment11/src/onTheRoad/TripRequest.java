package onTheRoad;
/**
 * Class representing a trip request with start, end, and whether it
 * is optimizing distance or time.
 * Written 11/25/2107
 * @author Kim Bruce
 */
import java.util.List;

public class TripRequest {
	
	// Starting point of trip
	private String start = "";
	
	// Ending point of trip
	private String end = "";
	
	// Whether trip should optimize distance (instead of time)
	private boolean isDistance;
	
	/**
	 * Create trip request
	 * @param req 
	 *    String representing a textual request formatted with
	 *    the number of the start and end nodes, and a "D" if
	 *    we are to optimize the trip by distance, "T" if by time
	 * @param vertices
	 * 	A list of vertices of the graph so can look up label of nodes.
	 */
	public TripRequest(String req, List<String> vertices) {
		// TO DO: Check input to make sure it has requisite number of
		// pieces, that the indices are legal, and tag letter is "D" or "T"
		String[] reqPieces = req.split(" ");
		
		
		// index and label of start node
		int startIndex = Integer.parseInt(reqPieces[0]);
		//if start index is illegal, skip this trip request
		if(startIndex  < 0 || startIndex >= vertices.size()){
			return;
		}else{
			start = vertices.get(startIndex);
		}
		
		// index and label of end node
		int endIndex = Integer.parseInt(reqPieces[1]);
		//if end index is illegal, skip this trip request
		if(endIndex  < 0 || endIndex >= vertices.size()){
			return;
		}else{
			end = vertices.get(endIndex);
		}
	
		
		// true iff optimize by distance
		isDistance = reqPieces[2].equals("D");
	}
	
	/**
	 * @return label of starting node for trip
	 */
	public String getStart() {
		return start;
	}
	
	/** 
	 * @return label of ending node for trip
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @return whether to optimize by distance (instead of time)
	 */
	public boolean isDistance() {
		return isDistance;
	}
	
	/**
	 * @return representation of trip as a string
	 */
	public String toString() {
		return "Request going from "+start+" to "+end+" by "
				+(isDistance ?"distance":"time");
	}
}
