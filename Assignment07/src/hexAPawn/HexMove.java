package hexAPawn;


// A class for representing a HexAPawn move
// (c) 2000, 2001 duane a. bailey

public class HexMove {
	protected int from;
	protected int to;
	protected int cols;

  /**
	 * @post: construct a new move from position f to position t
	 *       on a board with c cols
   */
	public HexMove(int f, int t, int c) {
		from = f;
		to = t;
		cols = c;
	}

  /**
	 * @post: get from position
   */
	public int from() {
		return from;
	}

  /**
	 * @post: get to position
   */
	public int to()	{
		return to;
	}

  /**
	 * @post: generates printable representation of move
   */
	public String toString() {
		return "Move from ["
			+ (1 + (from / cols))
			+ ","
			+ (1 + (from % cols))
			+ "] "
			+ "to ["
			+ (1 + (to / cols))
			+ ","
			+ (1 + (to % cols))
			+ "].";
	}
	
	public boolean equals(HexMove other){
		if(other.to() == to() && other.from() == from() 
				&& other.cols == cols){
			return true;
		}else{
			return false;
		}
	}
}
