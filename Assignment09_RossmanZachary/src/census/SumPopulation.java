package census;

import java.util.concurrent.RecursiveTask;

/**
 * Sum the US population in a given area 
 * @author zackr
 *
 */
public class SumPopulation extends RecursiveTask<Integer>{
	//censusData
	protected CensusData data;
	//query dimensions
	protected int left;
	protected int bottom;
	protected int right;
	protected int top;
	//grid dimensions
	protected int divide;
	protected Rectangle[][] grid;
	protected final int CUT_OFF = 8000;
	
	/**
	 * 
	 * @param left the leftmost column of our query rectangle
	 * @param bottom the lowest row of our query rectangle
	 * @param right the rightmost column of our query rectangle
	 * @param top the top row of our query rectangle
	 * @param data the census data that the query is concerned about
	 * @param divide the grid divisions
	 * @throws IllegalArgumentException
	 */
	public SumPopulation(int left, int bottom, int right, int top,
			CensusData data, int divide, Rectangle[][] grid)throws IllegalArgumentException{
		this.data = data;
		//preconditions
		if(right<left || right>divide || left<1 || left>divide ||top <bottom || 
				top>divide || bottom<1 || bottom>divide)throw new IllegalArgumentException();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.divide = divide;
		this.grid = grid;
	}
	
	
	/**
	 * 
	 * @param USPop a 2D array representing the US 
	 * @return the total US population within the query boundaries
	 */
	public int sumPopulation(Rectangle[][] USPop){	
		//accumulator for the population within the query boundaries
		int accumulator = 0;
		for(int i=0; i<data.size(); i++){
			CensusGroup inspect = data.get(i);
			searchLoop:
			//find the census group's grid position
			for(int lat = 0; lat< divide; lat++){
				for(int log = 0; log<divide; log++){
					//if we found the rectangle that contains the census group,
					//determine whether it is in the query rectangle
					if(USPop[lat][log].contains(inspect)){
						//if inside the query rectangle, add the census group's population
						//to an accumulator
						if(log+1>=left && log+1<=right && lat+1<=top && lat+1>=bottom){
							accumulator+=inspect.getPopulation();
						}
						break searchLoop;
					}
				}
			}
		}
		return accumulator;
	}
	
	/**
	 * Compute the population in a given area using parallel processing. 
	 * Parse the data into 2 SumPopulation objects until the data we are processing
	 * has less than (Cut_Off) elements
	 */
	@Override
	protected Integer compute() {
		if(data.size()<=CUT_OFF){
			return sumPopulation(grid);
		}else{
			SumPopulation firstHalf = new SumPopulation(left, bottom, right, top, data.firstHalf(), divide, grid);
			SumPopulation secondHalf = new SumPopulation(left, bottom, right, top, data.secondHalf(), divide, grid);
			
			firstHalf.fork();
			int second = secondHalf.compute();
			int first = firstHalf.join();
			return first+second;
		}
	}
}
