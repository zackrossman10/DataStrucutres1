/**
 * Class to artificially generate sentences
 * 
 * @author Zachary Rossman
 * @version 1.0
 */
package assignment02.src.wordsGeneric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import structure5.Association;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TextGenerator {

    // List of Association of letter pairs and frequency lists
    protected List<Association<StringPair, FreqList>> letPairList;
    
    Random table = new Random (); //create a Random object for random number generator
   
    /**
     * Constructor for a Text Generator
     */
    public TextGenerator(){
    	letPairList = new ArrayList<Association<StringPair, FreqList>> ();
    }
    
    /** 
     * 
     * @param first the first word of the triplet, will be the first element of the string pair
     * @param second the second word of the triplet, will be the second element of the string pair
     * @param third the third word of the triplet, will be put into a frequency list
     */ 
    public void enter(String first, String second, String third) {	
    	StringPair stringPair1 = new StringPair(first.toLowerCase(), second.toLowerCase()); //create a string pair for first 2 words of triplet
    	int index1 = correctIndex(stringPair1); 	//search for the StringPair in letPairList
    	if(index1<0){ 								//if the StringPair doesn't already exist, create a new association
        	FreqList freqList1 = new FreqList();	//construct a frequency list to hold info about possible third words
        	freqList1.add(third);					//add the third word to the frequency list
        	letPairList.add(new Association<StringPair, FreqList>(stringPair1, freqList1)); //add this association to the arrayList 
    	}else{ 											   //if the StringPair already exists
    		letPairList.get(index1).getValue().add(third); //add the third string to the frequency list that's
    													   //associated with the correct StringPair
    	}
  	}

    /**
     * 
     * @param first the first string in the stringPair we're looking to find a word to follow
     * @param second the second string in the stringPair we're looking to find a word to follow
     * @return the word which is picked to follow the first and second words, based on our
     * randomly generated p value and the getNExtWord function in the FreqList class
     */
    public String getNextWord(String first, String second) {
        double p = table.nextDouble(); 	//generate a p that will choose the next word based on probability
        StringPair stringPair2 = new StringPair (first.toLowerCase(), second.toLowerCase()); //construct StringPair for first 2 words
        if(correctIndex(stringPair2)>=0){ 	//while we can find a StringPair <frist,second>
        	return letPairList.get(correctIndex(stringPair2)).getValue().get(p); //get the next value given the string pair and random p
        }else{ 		//otherwise the program is finished because there is no association at the end.
        	return "";
        }
    }

    /**
     * 
     * @param stringPair3 the StringPair that we are looking to math in an association in letPairList
     * @return the index of letPairList that contains an association between the correct StringPair 
     * and frequency list
     * 
     * Note: I can't use the indexOf(obj) method because I'm looking for the StringPairs within
     * associations, which the indexOf method cannot do
     */
    public int correctIndex(StringPair stringPair3){
    	for(int i=0; i<letPairList.size(); i++){
    		if(letPairList.get(i).getKey().equals(stringPair3)){ //check if the element of letPairList contains the correct string pair
    			return i; //if we found the correct index, set this equal to indexCorrectStringPair
    		}
    	}
    	return -1; //if we didn't find the correct index, return a -1
    }
    
    /**
     * 
     * @return the number of associations in the text generator
     */
    public int size(){
    	return (letPairList.size());
    }
    
    /**
     * @return a string representation of all the associations in letPairList
     */
    public String toString(){
    	String accumulator = ""; //accumulates the associations of each element in letPairList
    	for(int i=0; i<(this.size()); i++){ //print each association in the TextGenerator object on a new line
    		accumulator = accumulator + letPairList.get(i)+"\n";
    	}
    	return accumulator;
    }
    
  
    public static void main(String args[]) {

        WordStream ws = new WordStream();
        JFileChooser dialog = new JFileChooser(".");

        // Display the dialog box and make sure the user did not cancel.
        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            // Find out which file the user selected.
            File file = dialog.getSelectedFile();
            try {
                // Open the file.
                BufferedReader input = new BufferedReader(new FileReader(file));

                // Fill up the editing area with the contents of the file being
                // read.
                String line = input.readLine();
                while (line != null) {
                    ws.addLexItems(line.toLowerCase());
                    System.out.println(line);
                    line = input.readLine();
                }
                System.out.println("Finished reading data");
                // Close the file
                input.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Can't load file "
                        + e.getMessage());
            }

            // insert code to print table & generate new text using
            // random number generator
        } else {
            System.out.println("User cancelled file chooser");
        }
        
        TextGenerator table = new TextGenerator(); //construct a TextGenerator
        
        String firstData = ws.nextToken(); 
        String secondData = ws.nextToken();
        String thirdData = ws.nextToken();
        table.enter(firstData, secondData, thirdData); //populate the first element of table
        											   //using the first 3 tokens of ws
        
        while (ws.hasMoreTokens()){ //while there are more tokens in ws, continue adding elements to generator 1
        	firstData = secondData;
        	secondData = thirdData;
        	thirdData = ws.nextToken();
        	table.enter(firstData, secondData, thirdData);
        }
        
        System.out.println("The table size is "+table.size()); 
        System.out.println(table.toString()); //print out the frequency list
        
        Scanner userInput = new Scanner(System.in); //construct a scanner object to read input from console
        String firstWord = userInput.next(); 		//take in first word
        String secondWord = userInput.next(); 		//take in second word
        String safety1 = firstWord; 			//saves first input in case we have to repeat
        String safety2 = secondWord; 			//saves second input in case we have to repeat
        for(int i=0; i<400; i++){ 				//generate the next 400 words using AI
        	String thirdWord = table.getNextWord(firstWord, secondWord); //generate the third word based on the first 2
        	if(thirdWord.equals("")){ 			//if the program has run out of associations before 400 strings are printed, start over
        		firstWord = safety1; 			//use the first original input string as the first word
            	secondWord = safety2; 			//use the second original input string as the second word
            	System.out.print(firstWord); 	//print the first original inputs
            	System.out.print(secondWord);
        		thirdWord = table.getNextWord(safety1, safety2); //generate the third word based on the original 2 input strings
          	}
        	if(thirdWord.equals("'") || thirdWord.equals(",") || thirdWord.equals("?") ||
        			thirdWord.equals(".")|| secondWord.equals("'") ||
        			thirdWord.equals("-") || secondWord.equals("-")){
        		System.out.print(thirdWord); 	//print the commas & question marks without a space before them, 
        										//print apostrophes and dashes without a space before or after
        	}else{ 
        		System.out.print(" "+thirdWord); //otherwise print tokens with a space before them
        	}
        	firstWord = secondWord; //move on to next String
        	secondWord = thirdWord;   
        	if((i+2)%20 ==0){ 		//print a new line every 20 words
        		System.out.println();
        	}
        }
        
    }
}
