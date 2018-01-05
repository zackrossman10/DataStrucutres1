/**
 * Frequency List of word/# of occurrence pairs
 * @author Kim Bruce
 * @version 2/08
 *  revised: added package --kpc 2/13
 */
package assignment02.src.wordsGeneric;

import java.util.ArrayList;
import java.util.List;

import structure5.Association;

public class FreqList {
    // list of associations holding words and their frequencies
    private List<Association<String, Integer>> flist;
    
    /**
     * Constructor for Frequency List
     */
    public FreqList(){
    	flist = new ArrayList<Association<String, Integer>> ();
    }

    private int numWordsAdded=0; //keeps track of how many words were added to the frequency list
    						     //should be equal to the number of elements in the frequency list
    
    /** 
     * @param word the string token read in from the text file which we want to 
     * mark in the frequency list and keep track of its occurrences
     * post: search the existing frequency list for the word. If the word already 
     * exists as a key of an association in the flist, increment the value of the 
     * association. If the word doesn't exists as the key of an association,
     * create a new association with key=word and value = 1. flist.size() should equal 
     * incremented value at all times.
     *
     */ 
    public void add(String word) {
    	if(flist.size() == 0){ //populate the first element of the frequency list
    		flist.add(new Association<String,Integer>(word, 1));
    		numWordsAdded++;
    	}else{
    		boolean operationCompleted = false;
    		for(int i=0; i<flist.size(); i++){ //check all indices of flist for word
        		if(flist.get(i).getKey().equals(word)){ //if the key of an association equals word, increment the value
        			int incrementedValue = flist.get(i).getValue()+1; //set newValue = oldValue+1
        			flist.get(i).setValue(incrementedValue); //set value to newValue
        			operationCompleted = true;
        		}
        	}
    		if(!operationCompleted){										//if the word doesn't exist as a key
    			flist.add(new Association<String,Integer>(word, 1));		//add a new association with word as the key
    			numWordsAdded++; 	//increment the number of words in flist
    		}
    	}
    	
    	assert (numWordsAdded == flist.size()): "numWordsAdded != flist.size()";
    }

    /**
     * 
     * @param p an random double between 0.0 and 1.0 which will be used to pick the next word based on probability
     * @return the word which the program selects to complete the phrase based on basic AI
     * @throws IllegalArgumentException when p<0 or p>1
     */
    public String get(double p) throws IllegalArgumentException {
    	if(p<0.0 || p> 1.0){ //test that p is a valid number 
    		throw new IllegalArgumentException();
    	}else{
    		int totalWords = 0;	//accumulates the total words, including duplicates, in flist
        	for(int i=0; i<flist.size(); i++){
        		totalWords += flist.get(i).getValue();
        	}
        	int upperBound; //the acceptable ceiling for p
        	upperBound = flist.get(0).getValue(); //sets acceptable range for p if the 1st element of flist is to be returned
        	int inspectingIndex = 0;	//keeps track of what element of flist is being considered for return
        	if(p == 0.0)				//in rare case p = 0.0, return the first element
        		return flist.get(0).getKey();
        	while(p>((double)upperBound/(double)totalWords)){ //enter loop while p is not in an acceptable range
        		inspectingIndex++; 	//check next element
        		upperBound += flist.get(inspectingIndex).getValue(); //increment the upper bound by the $ occurrences
        															 //of the next element       		
        	}
        	return flist.get(inspectingIndex).getKey(); //return the element of flist whose upper-bound was greatest && p<upperBound
    	}
    }

    /**
     * post: print the array list in the format "[<Association: key1=value1>, ...]"
     */
    public String toString() {
        return flist.toString();
    }
    
    // static method to test the class
    public static void main(String args[]) {
        FreqList list = new FreqList();
        list.add("yes");
        list.add("maybe");
        list.add("no");
        String test = "";
        test += list.get(1.0);
        System.out.println(test);
        System.out.print(list.toString());
        // Add code to test this class
    }
}
