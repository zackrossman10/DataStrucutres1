package compression;

import structure5.DoublyLinkedNode;
import structure5.DoublyLinkedList;

/**
 *  Implementation of lists, using doubly linked elements and keeping track of
 *  current reference.
 *  
 *  @author kim
 *  @version 2/2011
 */

public class CurDoublyLinkedList<E> extends DoublyLinkedList<E> {
	protected DoublyLinkedNode<E> current; // special designated node, site of actions
	protected int indexCurrent; //keep track of index of current, index=1 at first node

	/**
	 * @post: constructs an empty list
	 */
	public CurDoublyLinkedList() {
		//construct a doubleLinkedList<E> using super class
		super();
		//indexCurrent = -1 when list is empty or has one element
		indexCurrent = -1;
	}

	/**
	 * @pre: list is non-empty 
	 * @post: current set to first node of list
	 * used when current is offLeft()
	 */
	public void first() {
		if(head == null) throw new EmptyListException("List is empty");
		current = head;
		indexCurrent = 1;
	}

	/**
	 *
	 * @pre: list is non-empty 
	 * @post: current set to last node of list
	 * used when current is offRight
	 */
	public void last() {
		if(head == null) throw new EmptyListException("List is empty");
		current = tail;	
		//set index current equal to list.size()-1
		indexCurrent = this.size();
	}

	/**
	 * @pre: list is non-empty && current is not off right side of list 
	 * @post: if is off left then make first elt the current elt, else reset current elt
	 * to be the next element of list.
	 */
	public void next() {
		if(head == null) throw new EmptyListException("List is empty");
		//next() cannot be called when current is offRight
		if(this.isOffRight()) throw new IsOffException("Current is offRight");
		//if current is offLeft, move to first
		if(this.isOffLeft()){
			this.first();
		}else if(current == tail){
			//move current so that it's offRight
			current = null;
			//set indexCurrent = size() to indicate that current is offRight after this method call
			indexCurrent = this.size()+1;
		}else {
			current=current.next();
			//increment the index
			indexCurrent++;
		}
	}

	/**
	 * @pre: list is non-empty && is not off left side of list
	 * @post: if is off right side of list then make 
	 * tail the current elt and no longer off right, if current
	 * is head then set current to null and remember off left side of list, else
	 * set current elt to be previous element of list.
	 */
	public void back() {
		if(head == null) throw new EmptyListException("List is empty");
		//back() cannot be called when current is offLeft
		if(this.isOffLeft()) throw new IsOffException("Current is offLeft");
		//if current is offRight, set current to last
		if(this.isOffRight()){
			this.last();
		}else if(current == head){
			//move current so that it's offLeft
			current = null;
			//set indexCurrent=0<1 to indicate that current is offLeft
			indexCurrent=-1;
		}else{
			current=current.previous();
			//decrement indexCurrent
			indexCurrent--;
		}
	}

	/**
	 * @pre: list is non-empty
	 * 
	 * @return whether current is off right side of list
	 */
	public boolean isOffRight() {
		if(head == null) throw new EmptyListException("List is empty");
		//current is offRight if index> the list size
		return indexCurrent>this.size();
	}

	/**
	 * @pre: list is non-empty
	 * 
	 * @return whether current is off left side of list
	 */
	public boolean isOffLeft() {
		if(head == null) throw new EmptyListException("List is empty");
		//current is offLeft if index <1
		return indexCurrent<0;
	}

	/**
	 * @pre: list is non-empty
	 * 
	 * @return whether current is off right or left side of list
	 */
	public boolean isOff() {
		if(head == null) throw new EmptyListException("List is empty");
		//determine if current is offRight or offLeft
		return (this.isOffLeft() || this.isOffRight());
	}

	/**
	 * @pre: List is not empty & current not off list
	 * 
	 * @return value in current node
	 */
	public E currentValue() {
		if(head == null) throw new EmptyListException("List is empty");
		if(this.isOff()) throw new IsOffException("Current is off the list");
		return current.value();
	}

	/**
	 * @pre: value is not null, List non-empty & current is not off list 
	 * @post: adds element after current with given value. New elt is set to be
	 * current.
	 * 
	 * @param value
	 *            new value for node inserted after current
	 */
	public void addAfterCurrent(E value) {
		if(head == null) throw new EmptyListException("List is empty");
		if(this.isOff()) throw new IsOffException("Current is off the list");
		if(value == null) throw new NullValueException("Value of parameter is null");
		//add a value after current, which is at inidexCurrent
		super.add(indexCurrent, value);
	}

	/**
	 * @pre: list is non-empty & !isOff()
	 * @post: Current element is deleted, successor is new current elt 
	 * If deleted tail, then current is null and is off right
	 */
	public void removeCurrent() {
		if(head == null) throw new EmptyListException("List is empty");
		if(this.isOff()) throw new IsOffException("Current is off the list");
		//If the current element is the first element, call removeFirst()
		if(current==head){
			this.removeFirst();
		}else if(current==tail){
			//if the current element is the last element, call removeLast()
			this.removeLast();	
		}else{
			//set current.previous()'s forward pointer to current.next()
			current.previous().setNext(current.next());
			//set currrent.next()'s backwards pointer to current.previous()
			current.next().setPrevious(current.previous());
			//set current to current.next???
			//don't adjust index b/c element was removed from list??
			current = current.next();
		}
	}

	/**
	 * @pre: value is not null 
	 * @post: adds element to head of list with value
	 * newFirst, and make it the current element
	 * 
	 * @param newFirst
	 *            value of new first element of list
	 */
	// 
	public void addFirst(E newFirst) {
		if(newFirst == null) throw new EmptyListException("List is empty");
		super.addFirst(newFirst);
		current = head;
		//adjust indexCurrent to 1
		indexCurrent = 1;
	}

	/**
	 * @pre: list is not empty 
	 * @post: removes first value from list, successor is current
	 * 
	 * @return value of element formerly first i list
	 */
	public E removeFirst() {
		if(head == null) throw new EmptyListException("List is empty");
		//remove the first element, store in temp
		E temp = super.removeFirst();
		//successor is current??
		current = head;
		//adjust indexCurrent
		indexCurrent =1;
		//return the value of the element that was removed
		return temp;
	}

	/**
	 * @pre: value is not null 
	 * @post: adds new value to tail of list and make it current
	 * 
	 * @param newLast
	 *            value of new last element of list
	 */
	public void addLast(E newLast) {
		if(newLast == null) throw new NullValueException("Value of parameter is null");
		super.addLast(newLast);
		current = tail;
		//adjust the indexCurrent
		indexCurrent = this.size();
	}

	/**
	 * @pre: list is not empty 
	 * @post: removes value from tail of list, and current is
	 * set to null and off right side of list
	 * 
	 * @return value formerly in last element of list
	 */
	public E removeLast() {
		if(head == null) throw new EmptyListException("List is empty");
		//remove the last element and store in temp
		E temp = super.removeLast();
		//set current equal to null
		current = null;
		//set the indexCurrent to size +1
		indexCurrent = this.size()+1;
		//return the value of the temp node
		return temp;
	}

	/**
	 * @pre: list is not empty
	 * @post: sets current to first element of list
	 * 
	 * @return value of first element in list
	 */
	public E getFirst() {
		if(head == null) throw new EmptyListException("List is empty");
		current = head;
		indexCurrent =1;
		return head.value();
	}

	/**
	 * @pre: list is not empty 
	 * @post: sets current to last element of list
	 * 
	 * @return value of last element in list
	 */
	public E getLast() {
		if(head == null) throw new EmptyListException("List is empty");
		current = tail;
		indexCurrent = this.size();
		return tail.value();
	}

	/**
	 * @post: removes all the elements from the list current set to null
	 */
	public void clear() {
		super.clear();
		current = null;
		indexCurrent = -1;
	}

	/**
	 * @return readable representation of table Shows contents of underlying
	 *         list rather than all elements of the table.
	 */
	public String toString() { // do not change
		return super.toString() + "\nCurrent is " + current;
	}

	/**
	 * This method does not change current
	 * @return a string representation of the list, with 
	 * each element on a new line.
	 */
	public String otherString(){ // do not change
	    DoublyLinkedNode<E> finger = head;
	    StringBuilder ans = new StringBuilder("CurDoublyLinkedList:\n");
	    while (finger != null) {
		ans.append(finger+"\n");
		finger = finger.next();
	    }
	    return ans.toString();
	}

}

