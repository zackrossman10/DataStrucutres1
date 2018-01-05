package census;

public class TestCutOff {
		
	/**
	 * Experiment on the efficiency of version 2 depending on different
	 * cut-off levels for the SumPopulation compute() method
	 * @param args
	 */
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
	
		Rectangle[][] US = null;
		Grid grid =	Version12.constructBoundary(inputData);
		
		int pop1 = 0;
		long startTime2;
		long elapsedTime2;
		long accumulator = 0;
		grid = v12.compute();
		System.out.print("Done");
		
		
		accumulator = 0;
		for(int i=1; i<=60; i++){
			startTime2 = System.currentTimeMillis();
			US = grid.constructGrid(size);
			SumPopulation sumPop1 = new SumPopulation(left, bottom, right, top, inputData, size, US);
			pop1 = sumPop1.compute();
			elapsedTime2 = System.currentTimeMillis() - startTime2;
			if(i>20){
				accumulator+=elapsedTime2;
			}
		}
				
		System.out.println("V2 time "+ accumulator/(long)40);
		System.out.println();
	}
}
