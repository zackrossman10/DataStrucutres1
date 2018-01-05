package census;
/** A class to represent a Rectangle
 *  You do not have to use this, but it's quite convenient
 *  
 *  @author Your Name Here
 */
 
public class Rectangle {
        // invariant: right >= left and top >= bottom (i.e., numbers get bigger as you move up/right)
        // note in our census data longitude "West" is a negative number which nicely matches bigger-to-the-right
	private float left;
	private float right;
	private float top;
	private float bottom;
	protected int population;
	
	public Rectangle(float l, float r, float t, float b) {
		left   = l;
		right  = r;
		top    = t;
		bottom = b;
		population = 0;
	}
	
	// a functional operation: returns a new Rectangle that is the smallest rectangle
	// containing this and that
	public Rectangle encompass(Rectangle that) {
		return new Rectangle(Math.min(this.left,   that.left),
						     Math.max(this.right,  that.right),             
						     Math.max(this.top,    that.top),
				             Math.min(this.bottom, that.bottom));
	}
	
	public float getBottom(){
		return bottom;
	}
	
	public float getTop(){
		return top;
	}
	
	public float getLeft(){
		return left;
	}
	
	public float getRight(){
		return right;
	}
	
	/**
	 * @pre datapoint is non-null
	 * @param datapoint a CensusGroup datapoint, we want to know if it's inside grid
	 * @return whether the datapoint is inside this grid
	 */
	public boolean contains(CensusGroup datapoint)throws IllegalArgumentException{
		if(datapoint == null)throw new IllegalArgumentException();
		if(datapoint.getLatitude() >= getBottom() && datapoint.getLatitude() <=getTop()){
			if(datapoint.getLongitude() >= getLeft() && datapoint.getLongitude() <= getRight()){
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "[left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + "]";
	}
	
	/**
	 * set the population associated with this rectangle to a certain value
	 * @param set the new value of the population
	 */
	public void setPopulation(int set){
		this.population = set;
	}
	
	
	public int getPopulation(){
		return population;
	}
}
