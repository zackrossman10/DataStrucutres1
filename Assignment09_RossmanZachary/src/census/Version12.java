package census;


import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Version12 extends RecursiveTask<Grid>{
	//censusData
	protected CensusData data;
	//grid dimensions
	protected int divide;
	//a grid with the correct maxLong, minLong, maxLat, minLat
	protected Grid finalGrid;
	
	/**
	 * @param data the census data that the is contained in the grid
	 * @param divide the grid divisions
	 */
	public Version12(CensusData data, int divide)throws IllegalArgumentException{
		this.data = data;
		this.divide = divide;
	}
	
	/**
	 * Build a shell of a grid that encompasses the census data's latitudes and longitudes
	 * @param filename the file containing census data
	 * @param divide is the integer passed into args[1]
	 * @return a grid that encompasses this census data's latitudes and longitudes
	 */
	public static Grid constructBoundary(CensusData results){
		float maxLong = results.get(0).getLongitude();
		float minLong = results.get(0).getLongitude();
		float maxLat = results.get(0).getLatitude();
		float minLat = results.get(0).getLatitude();
		for(int i=1; i<results.size(); i++){
			//determine the max longitude and min longitude of all
			//datapoints added to data in o(n)
				if(results.get(i).getLongitude() < maxLong){
					if(results.get(i).getLongitude() < minLong){
						minLong = results.get(i).getLongitude();
					}
				}else{
					maxLong = results.get(i).getLongitude();
				}
				
				//determine the max latitude and min latitude of all 
				//datapoints added to data in o(n)
				if(results.get(i).getLatitude() < maxLat){
					if(results.get(i).getLatitude() < minLat){
						minLat = results.get(i).getLatitude();
					}
				}else{
					maxLat = results.get(i).getLatitude();
				}
		}
		return new Grid(minLong, maxLong, maxLat, minLat);
	}
	
	public Grid getFinalGrid(){
		return finalGrid;
	}
	

	@Override
	protected Grid compute() {
		if(data.size()<=2000){
			return constructBoundary(data);
		}else{
			Version12 firstHalf = new Version12(data.firstHalf(), divide);
			Version12 secondHalf = new Version12(data.secondHalf(), divide);
			
			firstHalf.fork();
			Grid second = secondHalf.compute();
			Grid first = firstHalf.join();
			float minLong = Math.min(first.left, second.left);
			float maxLong = Math.max(first.right, second.right);
			float minLat = Math.min(first.bottom, second.bottom);
			float maxLat = Math.max(first.top, second.top);
			finalGrid = new Grid(minLong, maxLong, maxLat, minLat);
			return finalGrid;
		}
	}
}
