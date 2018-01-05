
package compression;

/**
 *  TwoDTable.java
 *  Interface representing a 2-dimensional table
 * @author kim
 * @version 3/98, revised 2/2011
 *
 * @param <ValueType>
 */

interface TwoDTable<ValueType> {

	/**
	 * @pre: (row,col) is legal position in table @post: value in table for
	 * (row,col) is newInfo
	 * 
	 * @param row
	 *            row of element to be updated
	 * @param col
	 *            column of element to be update
	 * @param newInfo
	 *            new value to place in slot (row, col)
	 */
	public void updateInfo(int row, int col, ValueType newInfo);

	/**
	 * @pre: (row,col) is legal position in table
	 * 
	 * @param row
	 *            a row of the table
	 * @param col
	 *            a column of the table
	 * @return value stored in (row,col) entry of table
	 */
	public ValueType getInfo(int row, int col);

}
