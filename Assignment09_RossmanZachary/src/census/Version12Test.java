package census;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test that the version 1 and version 2 methods work correctly
 * @author zackr elewis
 *
 */
public class Version12Test {
	
	@Test
	public void construct2dArray(){
		CensusData data = PopulationQuery.parse("C:\\Users\\zackr\\workspace\\Assignment09\\src\\census\\blkgrp_pop_centroid_withname.txt");
		int accumulator = 0;
		for(int i=0; i<data.size(); i++){
			accumulator += data.get(i).getPopulation();
		}
		assertTrue(data.size()<211111);
		//determine the approximate population in the bottom-right quadrant of the US
		//should be close to 285,000,000
		Version12 test1 = new Version12(data, 50);
		//Grid gridOne = Version12.constructBoundary(data);
		//use paralellism to create a grid
		Grid threads = test1.compute();
		//determine the approximate population in the bottom-left quadrant of the US
		//should be much smaller than 
		Rectangle[][] US = threads.constructGrid(50);
		SumPopulation sumTestFive = new SumPopulation(26, 1, 50, 25, data, 50, US);
		int sequentialPop = sumTestFive.sumPopulation(US);
		System.out.println("Bot Right Sequential Population: "+sequentialPop);
		int population = sumTestFive.compute();
		assertEquals(population, sequentialPop);
		System.out.println("Bot Right Parallel population: "+population);
		//determine the approximate population in the bottom-left quadrant of the US
		//should be much smaller than 
		//the bottom left of US
		SumPopulation sumTestOne = new SumPopulation(1, 1, 25, 25, data, 50, US);
		int botLeft = sumTestOne.sumPopulation(US);
		System.out.println("Bottom Left: "+botLeft);
		//the top left of US
		SumPopulation sumTestTwo = new SumPopulation(1, 26, 25, 50, data, 50, US);
		int topLeft = sumTestTwo.sumPopulation(US);
		System.out.println("Top Left: "+topLeft);
		//the bottom right of US
		SumPopulation sumTestThree = new SumPopulation(26, 1, 50, 25, data, 50, US);
		int botRight = sumTestThree.sumPopulation(US);
		System.out.println("Bottom Right: "+botRight);
		//the top right of US
		SumPopulation sumTestFour = new SumPopulation(26, 26, 50, 50, data, 50, US);
		int topRight = sumTestFour.sumPopulation(US);
		System.out.println("Top Right: "+topRight);
		
		
		//adding up the population of the 4 quadrants should represent the entire population
		assertEquals((botLeft+botRight+topLeft+topRight), accumulator);
		//bottom right quadrant should have the most population
		assertTrue(botRight> topRight && botRight>botLeft && botRight>topLeft);
		//top right should =0 population
		assertTrue(topRight == 0); 
		System.out.println("Total of 4 quadrants: "+(botLeft+botRight+topLeft+topRight));
		System.out.println("Total in file: " +accumulator);
		System.out.println();	
	}
}
