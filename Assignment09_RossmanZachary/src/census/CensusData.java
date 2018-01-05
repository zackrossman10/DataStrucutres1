package census;

import java.util.Arrays;

/** 
 *  just a resizing array for holding the input
 *  
 *  note: array may not be full; see data_size field
 *  
 *  @author: Zack Rossman, Ethan Lewis
 */

public class CensusData {
	private static final int INITIAL_SIZE = 100;
	private CensusGroup[] data;
	private int data_size;
	
	public CensusData(CensusGroup[] data){
		this.data = data;
		this.data_size = data.length;
	}
	
	public CensusData() {
		data = new CensusGroup[INITIAL_SIZE];
		data_size = 0;
	}
	
	public void add(int population, float latitude, float longitude) {		
		if(data_size == data.length) { // resize
			CensusGroup[] new_data = new CensusGroup[data.length*2];
			for(int i=0; i < data.length; ++i)
				new_data[i] = data[i];
			data = new_data;
		}
		CensusGroup g = new CensusGroup(population,latitude,longitude); 
		data[data_size++] = g;
	}
	
	/**
	 * 
	 * @param i index of element we want to get from the CensusData Object
	 * @return the ith element from the CensusData Object
	 */
	public CensusGroup get(int i){
		return data[i];
	}
	
	/**
	 * 
	 * @return a new CensusData Object that contains the first half of the original 
	 * CensusData object's data
	 */
	public CensusData firstHalf(){
		CensusGroup[] temp = Arrays.copyOfRange(this.data, 0, (this.data_size)/2);
		return new CensusData(temp);
	}
	
	/**
	 * 
	 * @return a new CensusData Object that contains the second half of the original 
	 * CensusData object's data
	 */
	public CensusData secondHalf(){
		CensusGroup[] temp = Arrays.copyOfRange(this.data, (this.data_size)/2, this.data_size);
		return new CensusData(temp);
	}
	
	public int size(){
		return data_size;
	}
	
	public String toString(){
		return data.toString();
	}
}
