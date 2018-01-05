package census;
/**
 * Main class that handles population Queries
 * 
 * @author Your Name Here
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;

/**
 * this class handles queries about populations in the US
 * @author zackr
 *
 */
public class PopulationQuery{
	// next four constants are relevant to parsing
	private static final int TOKENS_PER_LINE  = 7;
	private static final int POPULATION_INDEX = 4; // zero-based indices
	private static final int LATITUDE_INDEX   = 5;
	private static final int LONGITUDE_INDEX  = 6;

	//construct a 2D array of rectangles that comprise a grid of the US
	protected Rectangle[][] US;
	
	// parse the input file into a large array held in a CensusData object
	public static CensusData parse(String filename) {
		CensusData result = new CensusData();
		
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line

            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0)
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
            }

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
	}

	
	// argument 0: file name for input data: pass this to parse
	// argument 1: size of the grid
	// argument 2: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		//get the three command-line arguments
		String file = args[0];
		CensusData inputData = parse(file);
		int size = Integer.parseInt(args[1]);
		Version12 v12 = new Version12(inputData, size);
		
		//variable which holds a shell of a grid that 
		//encompasses the census data's latitudes and longitudes
		Grid grid;
		String version = args[2];
		//if version1, construct the grid sequentially 
		if(version.equals("-v1")){
			grid =	Version12.constructBoundary(inputData);
		}else{
			//if verion2 or version3, construct the grid in parallel
			grid = v12.compute();
		}
		Rectangle[][] US = grid.constructGrid(size);
		
		Version3 v3 = new Version3(size, inputData);
		//if version3, pre-compute
		if(version.equals("-v3")){
			v3.setGridPopulation(US);
			v3.updateGrid(US);
		}
		
		
		System.out.println("Provide a query rectangle with space b/t parameters: ");
		Scanner query = new Scanner(System.in);
		String line = query.nextLine();
		String[] splited = line.split("\\s+");
		//keep answering queries while parameters have 4 integers
		while(splited.length==4){
			int left = Integer.parseInt(splited[0]);
			int bottom = Integer.parseInt(splited[1]);
			int right = Integer.parseInt(splited[2]);
			int top = Integer.parseInt(splited[3]);
			int population;
			
			//determine which version will handle the query
			if(version.equals("-v3")){
				population = v3.handleQuery(left-1,bottom-1,right-1, top-1, US);
			}else{
				SumPopulation sumPop = new SumPopulation(left, bottom, right, top, inputData, size, US);
				if(version.equals("-v2")){
					population = sumPop.compute();
				}else{
					population = sumPop.sumPopulation(US);
				}
			}	
			
			System.out.println("Population in query rectangle: "+population);
			double percentage = (double) population/ (double)285230516*100.0;
			System.out.printf("Percentage of US Pop in query: %.2f%%%n", percentage); 
			System.out.println();
			System.out.println("Provide a query rectangle with space b/t parameters: ");
			line = query.nextLine();
			splited = line.split("\\s+");
		}
		System.out.println("Illegal paramters, goodbye");
	}

	
}
