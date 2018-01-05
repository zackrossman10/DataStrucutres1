package compression;

import structure5.*;

/** CompressedTable.java
 * Class representing efficient implementation of 2-dimensional table 
 * when lots of repeated entries. Idea is to record entry only when a 
 * value changes in the table as scan from left to right through 
 * successive rows.
 * @author Kim Bruce, 
 * @version 3/1/98, Revised for Java 5, 2/2007, revised for standard graphics 2/12
 * @param <ValueType> type of value stored in the table
 */

class CompressedTable<ValueType> implements TwoDTable<ValueType> {
	// List holding table entries - do not change
	protected CurDoublyLinkedList<Association<RowOrderedPosn, ValueType>> tableInfo; 
	//initialize variables to store table dimensions
	protected final int rows1;
	protected final int cols1;
	protected final ValueType default1;
	// more instance variables

	/**
	 * Constructor for table of size rows x cols, all of whose values are
	 * initially defaultValue
	 * 
	 * @param rows
	 *            # of rows in table
	 * @param cols
	 *            # of columns in table
	 * @param defaultValue
	 *            initial value of all entries in table
	 */
	public CompressedTable(int rows, int cols, ValueType defaultValue) {
		tableInfo = new CurDoublyLinkedList<Association<RowOrderedPosn, ValueType>> ();
		//set final number of rows
		rows1 = rows;
		//set final number of columns
		cols1 = cols;
		//set the default value
		default1 = defaultValue;
		tableInfo.add(new Association<RowOrderedPosn, ValueType>(new RowOrderedPosn(0,0,rows,cols), defaultValue));
	}

	/**
	 * @post: returns with current value of list being either findPos or the last
	 * elt in the list representation which comes before that entry
	 * 
	 * @param findPos
	 */
	private void find(RowOrderedPosn findPos) {
		tableInfo.first();
		Association<RowOrderedPosn, ValueType> entry = tableInfo.currentValue();
		RowOrderedPosn pos = entry.getKey();
		while (!findPos.less(pos)) {
			// search through list until pass elt looking for
			tableInfo.next();
			if (tableInfo.isOff()) {
				break;
			}
			entry = tableInfo.currentValue();
			pos = entry.getKey();
		}
		tableInfo.back(); // Since passed desired entry, go back to it.
	}

	/**
	 * @pre: (row,col) is legal position in table
   * @post: value in table for * (row,col) is newInfo
	 * 
	 * @param row
	 *            row of element to be updated
	 * @param col
	 *            column of element to be update
	 * @param newInfo
	 *            new value to place in slot (row, col)
	 */
	public void updateInfo(int row, int col, ValueType newInfo) {
		if(row>rows1 || row<0 || col>cols1 ||col<0) 
			throw new IllegalTablePositionException("dimensions are not acceptable");
		//create a RowOrderedPosn Object that represents the position we are looking to update
		RowOrderedPosn lookup = new RowOrderedPosn(row,col,rows1,cols1);
		//find this position
		this.find(lookup);	
		//if current value is equal to newInfo, nothing needs to be done
		if(newInfo == tableInfo.currentValue().getValue()){
			return;
		}
		//lookup and store currentRow and currentCol
		int currentRow = tableInfo.currentValue().getKey().getRow();
		int currentCol = tableInfo.currentValue().getKey().getCol();
		//if currentRow = row and currentCol = col, update the value of current node
		if(row == currentRow && col == currentCol){
			tableInfo.currentValue().setValue(newInfo);
		}else{
			//if current row!= row or currentCol!= col
			//create a new node
			//find next rowOrderedPos for the new node
			tableInfo.addAfterCurrent(new Association<RowOrderedPosn, ValueType>(lookup, newInfo));
		}
		//create a RowOrderedPosn for the next spot in the table
		RowOrderedPosn checkNode = lookup.next();
		//move current to the next spot in the table
		this.find(checkNode);
		//determine 
		int currentRow1 = tableInfo.currentValue().getKey().getRow();
		int currentCol1 = tableInfo.currentValue().getKey().getCol();
		//if current = spot,then there is a node @ the next spot in table
		//no new node with default value has to be added
		if(checkNode.getRow() == currentRow1 && checkNode.getCol() == currentCol1){
			return;
		}else{
			//if current != spot, then there is no node @ the next spot in table
			//add a new node with default value after current
			tableInfo.addAfterCurrent(new Association<RowOrderedPosn, ValueType>(checkNode, default1));
		}
	}

	/**
	 *  @pre: (row,col) is legal position in table???
	 *  @param row
	 *  		a row of the table
	 *  @param col
	 *  		a column of the table
	 *  @return value stored in (row,col) entry of table
	 */
	public ValueType getInfo(int row, int col) {
		if(row>rows1 || row<0 || col>cols1 ||col<0) 
			throw new IllegalTablePositionException("Not a legal position");
		//create a RowOrderedPosn object to look
		RowOrderedPosn lookup = new RowOrderedPosn(row,col,rows1,cols1);
		//move current to the association we are looking for
		this.find(lookup);
		//return the value in the association
		return tableInfo.currentValue().getValue();
	}

	/**
	 *  @return
	 *  		 succinct description of contents of table
	 */
	public String toString() { // do not change
	    return tableInfo.otherString();
	}

	/**
	 * program to test implementation of CompressedTable
	 * @param args
	 * 			ignored, as not used in main
	 */
	public static void main(String[] args) {
		
		// add your own tests to make sure your implementation is correct!!
		CompressedTable<String> table = new CompressedTable<String>(5, 6, "0");
		//System.out.println(table.tableInfo.indexCurrent);
		System.out.println("table is " + table);
		table.updateInfo(0,0,"1");
		table.updateInfo(0,1,"1");
		System.out.println("table is " + table);
		System.out.println(table.getInfo(2,1));
		
		//uncomment below for compatibility check with auto-grader
		AutograderCompTest a = new AutograderCompTest();
		a.testCurDoublyLinkedList();
		a.testCompressedTable();

	}

}
