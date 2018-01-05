package census;

import java.util.Scanner;

public class TestParallelism {
	
	/**
	 * Test the time it takes version 1/2/3 to process queries
	 * @param args command-line arguments
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
		
		//version 1
		
		int population = 0;
		Rectangle[][] US = null;
		Grid grid =	Version12.constructBoundary(inputData);
		long startTime1 = System.currentTimeMillis();
		for(int i=1; i<=30; i++){
			US = grid.constructGrid(size);
			SumPopulation sumPop = new SumPopulation(left, bottom, right, top, inputData, size, US);
			population = sumPop.sumPopulation(US);
		}
		
		long elapsedTime1 = System.currentTimeMillis() - startTime1;
		System.out.println("Version 1: "+population);
		System.out.println("V1 time: "+elapsedTime1/(long)30);
		System.out.println();
		
		//version 2
		int pop1 = -1;
		long startTime2;
		long elapsedTime2;
		long accumulator = 0;
		grid = v12.compute();
		for(int i=1; i<=30; i++){
			startTime2 = System.currentTimeMillis();
			US = grid.constructGrid(size);
			SumPopulation sumPop1 = new SumPopulation(left, bottom, right, top, inputData, size, US);
			pop1 = sumPop1.compute();
			elapsedTime2 = System.currentTimeMillis() - startTime2;
			if(i>20){
				accumulator+=elapsedTime2;
			}
		}
		
		System.out.println("Version 2 population: "+ pop1);
		System.out.println("V2 time: "+ accumulator/(long)10);
		System.out.println();
		
		

		//version3
		Version3 v3 = new Version3(size, inputData);
		int pop3 = -1;
		long startTime3;
		long elapsedTime3;
		long accumulator3 = 0;
		v3.setGridPopulation(US);
		v3.updateGrid(US);
		for(int i=1; i<=30; i++){
			startTime3 = System.currentTimeMillis();
			pop3 = v3.handleQuery(left-1,bottom-1,right-1, top-1, US);
			elapsedTime3 = System.currentTimeMillis() - startTime3;
			if(i>20){
				accumulator3+=elapsedTime3;
			}
		}
		
		System.out.println("Version 3 population: "+ pop3);
		System.out.println("V3 time: "+ accumulator3/(long)10);
		
		
		
		

	}
	
	
	
}
