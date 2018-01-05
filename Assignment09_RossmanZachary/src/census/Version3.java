package census;

/**
 * Class that implements version 3 by pre-processing the array so that 
 * populations in a given qquery can be accessed in O(1) time
 * @author zackr
 *
 */
public class Version3 {
	protected int divide;
	protected CensusData data;
	
	/**
	 * 
	 * @param divide the number of rows and columns in the grid
	 * @param data the CensusGroup data
	 */
	public Version3(int divide, CensusData data){
		this.divide = divide;
		this.data = data;
	}
	
	/**
	 * Assigns a population to each rectangle of the grid based on the 
	 * census data
	 * @param USPop a grid representing the US
	 */
	public void setGridPopulation(Rectangle[][] USPop){	
		for(int i=0; i<data.size(); i++){
			CensusGroup inspect = data.get(i);
			searchLoop:
			//find the census group's grid position
			for(int lat = 0; lat< divide; lat++){
				for(int log = 0; log<divide; log++){
					//if we found the rectangle that contains the census group,
					//determine whether it is in the query rectangle
					if(USPop[lat][log].contains(inspect)){
						int newPop = inspect.getPopulation() + USPop[lat][log].getPopulation();
						USPop[lat][log].setPopulation(newPop);
						break searchLoop;
					}
				}
			}
		}
		
	}
	
	/**
	 * Updates each rectangle's population to reflect the population of 
	 * this rectangle and all rectanges to the west or south. Thus, at 
	 * USPop[0][0], the population doesn't change, but the population at
	 * UsPop[0][1] will be updated to (USPop[0][0]+USPop[0][1])
	 * The population in rectange USPop[divide][divide] will equal the 
	 * entire US population
	 * @param USPop a grid with rectangles reflecting the population only 
	 * contained in that particular rectangle
	 */
	public void updateGrid(Rectangle[][] USPop){
	int dimen = USPop.length;
	for (int row = 0; row < dimen; row++){
		for (int col = 0; col < dimen; col++){
			if (col == 0){
				if (row != 0){
					USPop[row][col].setPopulation(USPop[row][col].getPopulation() + 
							USPop[row -1][col].getPopulation());
				}
			}else if (row == 0){
				if (col != 0){
					USPop[row][col].setPopulation(USPop[row][col].getPopulation() + 
							USPop[row][col - 1].getPopulation());
				}	
			}else{
				USPop[row][col].setPopulation((USPop[row][col].getPopulation()) + 
						(USPop[row -1][col].getPopulation()) + (USPop[row][col-1].getPopulation()) - 
						(USPop[row -1][col -1].getPopulation()));
				}
			}
		}
	}
	
	/**
	 * @pre: query must be a rectangle contained within the grid
	 * Dimensions of the query rectangle:
	 * @param left
	 * @param bottom
	 * @param right
	 * @param top
	 * 
	 * @param USPop a grid with rectangles reflecting the population contained in 
	 * all rectangles to the west and south
	 * @return the population in the query rectangle
	 */ 
	public int handleQuery(int left, int bottom, int right, int top, Rectangle[][] US)
		throws IllegalArgumentException{
		// preconditions
		if(right<left || right>=divide || left<0 || left>=divide ||top <bottom || 
				top>=divide || bottom<0 || bottom>=divide)throw new IllegalArgumentException();
		int topRight = US[top][right].getPopulation();
		int leftBotLeft;
		int botBotRight;
		int add;
		boolean offLeft = false;
		boolean offTop = false;
		//handle border conditions
		//if the query contains the leftmost column, don't subtract anything from the left
		if(left== 0 ){
			leftBotLeft=0;
			offLeft = true;
		}else{
			leftBotLeft = US[top][left-1].getPopulation();
		}
		//if the query contains the lowest row, don't subtract anything from the bottom
		if(bottom==0){
			botBotRight = 0;
			offTop = true;
		}else{
			botBotRight = US[bottom-1][right].getPopulation();
		}
		//add back the territory that was subtracted twice (only if offLeft and offRight = false)
		if(offTop || offLeft){
			add = 0;
		}else{
			add = US[bottom-1][left-1].getPopulation();
		}
		
		//return the sum of these elements which reflects the population in the query rectangle
		return (topRight -leftBotLeft -botBotRight +add);
	}

	//test that version 3 works correctly by comparing to sequential algorithms
	public static void main(String args[]){
		CensusData data = PopulationQuery.parse("C:\\Users\\zackr\\workspace\\Assignment09\\src\\census\\blkgrp_pop_centroid_withname.txt");
		Version12 test1 = new Version12(data, 2);
		Grid threads = test1.compute();
		Rectangle[][] US = threads.constructGrid(2);
		Version3 v3 = new Version3(2, data);
		v3.setGridPopulation(US);	
		System.out.println("SEPARATE POPS");
		System.out.println("Top Left USPOP:"+US[1][0].getPopulation()+"| Top Right USPOP:"+US[1][1].getPopulation());	
		System.out.println("Bottom Left USPOP:" +US[0][0].getPopulation()+"| Bottom Right USPOP:" +US[0][1].getPopulation());
		System.out.println();
		System.out.println("CUMULATIVE POPS");
		v3.updateGrid(US);
		System.out.println("Top Left USPOP:"+US[1][0].getPopulation()+"| Top Right USPOP:"+US[1][1].getPopulation());	
		System.out.println("Bottom Left USPOP:" +US[0][0].getPopulation()+"| Bottom Right USPOP:" +US[0][1].getPopulation());
		
		int v33 = v3.handleQuery(0,0,1,1, US);
		System.out.println("Total Pop:" +v33);
		v33 = v3.handleQuery(0,0,0,0, US);
		System.out.println("Total Pop first space:" +v33);
		v33 = v3.handleQuery(0,0,0,1, US);
		System.out.println("Total Pop first colum:" +v33);
		v33 = v3.handleQuery(0,0,1,0, US);
		System.out.println("Total Pop first row :" +v33);
		v33 = v3.handleQuery(0,1,1,1, US);
		System.out.println("Total Pop second row:" +v33);
		v33 = v3.handleQuery(1,1,1,1, US);
		System.out.println("Total Pop last space:" +v33);
		

		
		
	}
}
