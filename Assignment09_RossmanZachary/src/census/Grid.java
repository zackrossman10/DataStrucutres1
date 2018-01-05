package census;

/**
 * this class constructs a grid of rectangles (represented as a 2D array)
 * given the boundaries of the grid and the number of columns/rows
 * to divide it
 * @author zackr elewis
 *
 */
public class Grid {
	protected Rectangle grid;
	protected float right;
	protected float left;
	protected float top;
	protected float bottom;
	
	
	/**
	 * Construct a grid given these boundaries
	 * @param top
	 * @param right
	 * @param bottom
	 * @param left
	 */
	public Grid(float left, float right, float top, float bottom){
		grid = new Rectangle(left, right, top, bottom);
		this.right = right;
		this.left = left;
		this.top = top;
		this.bottom = bottom;
	}
	
	/**
	 * 
	 * @param divide the number of rows and columns to divide the grid into
	 * @return a 2D array of rectangles that divide the grid into (divide) columns
	 * and (divide) rows
	 */
	public Rectangle[][] constructGrid(int divide){
		Rectangle[][] gridList = new Rectangle[divide][divide];
		
		float latIncrement = (top - bottom)/divide;
		float longIncrement = (right-left)/divide;
		float leftBoundary = left;
		float rightBoundary = left+longIncrement;
		float topBoundary = bottom+latIncrement;
		float bottomBoundary = bottom;
		for(int lat = 0; lat< divide; lat++){
			for(int log = 0; log<divide; log++){
				gridList[lat][log] = new Rectangle(leftBoundary, rightBoundary, topBoundary, bottomBoundary);
				//move the left and right boundaries to the right
				leftBoundary = rightBoundary;
				rightBoundary+=longIncrement;
			}
			//reset the left and right boundaries
			leftBoundary = left;
			rightBoundary = left+longIncrement;
			//move the top and bottom boudaries down
			bottomBoundary = topBoundary;
			topBoundary += latIncrement;
		}
		return gridList;
	}
	
	public String toString() {
		return "[left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + "]";
	}
	
	
}

