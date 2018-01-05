package census;

/**
 * 
 * @author zackr elewis
 * 
 * Experiment on the efficiencies of versions 1 and 3 depending on 
 * the number of queries it runs
 *
 */
public class TestQueries {
	
	public static void main(String args[]){
		int left = 1;
		int bottom = 1;
		int right = 50;
		int top = 50;
		
		//get the three command-line arguments
		String file = args[0];
		CensusData inputData = PopulationQuery.parse(file);
		int size = Integer.parseInt(args[1]);
		Version12 v12 = new Version12(inputData, size);
		
		//version 1
		
		int population = 0;
		Rectangle[][] US = null;
		Grid grid =	Version12.constructBoundary(inputData);
		US = grid.constructGrid(size);
		long startTime1 = System.currentTimeMillis();
		
		for(int i=1; i<30; i++){
			SumPopulation sumPop = new SumPopulation(left, bottom, right, top, inputData, size, US);
			for(int j=0; j<16; j++){
				population = sumPop.sumPopulation(US);
			}
		}
		
		long elapsedTime1 = System.currentTimeMillis() - startTime1;
		System.out.println("V1 time: "+elapsedTime1/(long)30);
		System.out.println();
		
		//version3
		Version3 v3 = new Version3(size, inputData);
		long startTime3;
		long elapsedTime3;
		long accumulator3 = 0;
		startTime3 = System.currentTimeMillis();
		v3.setGridPopulation(US);
		v3.updateGrid(US);
		for(int i=1; i<=30; i++){
			for(int j=1; j<16; j++){
				v3.handleQuery(left-1,bottom-1,right-1, top-1, US);
			}
			elapsedTime3 = System.currentTimeMillis() - startTime3;
			if(i>20){
				accumulator3+=elapsedTime3;
			}
		}
		
		System.out.println("V3 time: "+ accumulator3/(long)10);
	}
}
	
