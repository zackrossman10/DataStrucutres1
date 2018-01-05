package compression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CurDoublyLinkedList class
 *	@Zack Rossman
 *	@9/2017
 */

public class TestCurDoublyLinkedList{
	CurDoublyLinkedList<Integer> list;

	@Before
	public void setUp() throws Exception {
		list = new CurDoublyLinkedList<Integer>();
	}

	@Test(expected =EmptyListException.class)
	public void testCurDoublyLinkedList() {
		assertNull(list.current);
		assertEquals(0, list.size());		
		assertTrue(!list.isOff());
	}

	@Test
	public void testFirst() {
		// 83 <=> 47
		list.add(47);
		list.add(83);	
		list.first();			
		assertNotNull(list.current);
		assertEquals(83, list.current.value(), 0);
	}

	@Test
	public void testLast() {
		// 83 <=> 47
		list.add(47);
		list.add(83);	
		list.last();

		assertNotNull(list.current);
		assertEquals(47, list.current.value(), 0);
	}


	// Calls next() on an empty list. Replace the "NoSuchElementException"
	// with whatever exception you throw when the precondition is violated
	@Test(expected=EmptyListException.class)
	public void testNext_EmptyList() {
		list.next();
	}

	// Calls next() on a non-empty list where current is off the right side
	// Replace with whatever exception you throw when the precondition is violated 
	@Test(expected=IsOffException.class)	
	public void testNext_OffRightSide() {
		list.addFirst(47);
		list.next(); // this moves current off right
		list.next(); //	this should trigger an exception
	}

	// Calls next() on non-empty list where current is head
	@Test
	public void testNext() {
		// 83 <=> 47
		list.add(47);
		list.add(83);
		list.first();
		assertNotNull(list.current);
		assertEquals(83, list.current.value(), 0);
		list.next();
		assertEquals(47, list.current.value(), 0);
		//push current offRight
		list.next();
		assertTrue(list.isOffRight());
		//return to tail
		list.back();
		assertEquals(47, list.current.value(), 0);
	}

	
	@Test
	public void testBack() {
		list.add(47);
		list.add(83);
		list.first();
		list.next();
		list.back();
		assertEquals(83, list.current.value(), 0);
		//push current offLeft
		list.back();
		assertTrue(list.isOffLeft());
		//return to head
		list.next();
		assertEquals(83, list.current.value(), 0);
	}

	
	@Test
	public void testIsOffRight() {
		list.add(47);
		list.first();
		assertTrue(!list.isOffRight());
		//push current offRight
		list.next();
		assertTrue(list.isOffRight());
	}

	@Test
	public void testIsOffLeft() {
		list.add(47);
		list.first();
		assertTrue(!list.isOffLeft());
		//push current offLeft
		list.back();
		assertTrue(list.isOffLeft());
	}

	@Test
	public void testIsOff() {
		list.add(47);
		//push current offLeft
		list.back();
		assertTrue(list.isOff());
		//push current back to head
		list.next();
		assertTrue(!list.isOff());
		//push current offRight
		list.next();
		assertTrue(list.isOff());
		//return to tail
		list.back();
		assertTrue(!list.isOff());
	}

	// Calls currentValue() on an empty list. Replace the "NoSuchElementException"
	// with whatever exception you throw when the precondition is violated
	@Test(expected=EmptyListException.class)
	public void testCurrentValue_EmptyList() {
		list.currentValue();
	}

	// Calls currentValue() on non-empty list where current is off right.
	// Replace with whatever exception you throw when precondition is violated
	@Test(expected=IsOffException.class)	
	public void testCurrentValue_isOff() {
		list.addFirst(47);
		list.next();  // this moves current off right
		list.currentValue(); // this should trigger an exception
	}
	
	@Test
	public void testCurrentValue() {
		// 134 <=> 84 <=> 47
		list.add(47);
		list.add(84);
		list.add(134);
		
		list.first(); // current points at 134
		assertEquals(134, list.currentValue(), 0);
		
		list.next(); // current points at 84
		assertEquals(84, list.currentValue(), 0);
		
		list.next(); // current points at 47
		assertEquals(47, list.currentValue(), 0);		
	}
	
	@Test
	public void testAddAfterCurrent() {
		// 47
		list.add(47);
		list.first();
		assertEquals(1, list.size());
		assertEquals(47, list.current.value(), 0);
		
		// 47 <=> 83
		list.addAfterCurrent(83);

		assertEquals(2, list.size());
		assertEquals(83, list.current.value(), 0); // current should point to new node		
	}

	
	@Test
	public void testRemoveCurrent() {
		//remove the only value in the list
		list.add(47);
		list.removeCurrent();
		assertTrue(list.current == null);
		//remove the first value
		list.add(47);
		list.add(87);
		list.add(97);
		list.removeCurrent();
		assertEquals(87, list.current.value(), 0);
		//remove a middle element
		list.add(97);
		list.next();
		list.removeCurrent();
		assertEquals(47, list.current.value(), 0);
		//remove the tail
		list.removeCurrent();
		assertNull(list.current);
		//attempt to return to new tail
		list.back();
		assertEquals(97, list.current.value(), 0);
	}

	
	@Test
	public void testAddFirstE() {
		//add to an empty list
		list.addFirst(1);
		assertEquals(1, list.current.value(), 0);
		//add to a non-empty list
		list.addFirst(2);
		assertEquals(2, list.current.value(), 0);
	}

	
	@Test
	public void testRemoveFirst() {
		list.add(1);
		//remove the first element from a 1-element list
		list.removeFirst();
		assertTrue(list.current == null);
		list.add(1);
		list.add(2);
		//remove the first element of a 2+ element list
		list.removeFirst();
		assertEquals(1, list.current.value(), 0);
	}

	
	@Test
	public void testAddLastE() {
		//addLast to an empty list
		list.addLast(1);
		assertEquals(1, list.current.value(), 0);
		//addLast to a non-empty list
		list.addLast(2);
		assertEquals(2, list.current.value(), 0);
	}
	
	@Test
	public void testRemoveLast() {
		list.add(1);
		//removeLast from a 1-element list
		list.removeLast();
		assertTrue(list.current == null);
		list.add(1);
		list.add(2);
		//removeLast from a 2+ elementList
		list.removeLast();
		//get current back to tail
		list.back();
		assertEquals(2, list.current.value(), 0);
	}

	
	@Test
	public void testGetFirst() {
		list.add(1);
		//getFirst from a 1-element list
		assertTrue(list.getFirst() == 1);
		//getFirst from a 2+ element list
		list.add(2);
		assertTrue(list.getFirst() == 2);
	}

	
	@Test
	public void testGetLast() {
		//getLast from a 1-element list
		list.add(1);
		assertTrue(list.getLast() == 1);
		list.add(2);
		//getLast from a 2-element list
		assertTrue(list.getLast() == 1);
	}

	
	@Test(expected = EmptyListException.class)
	public void testClear() {
		list.add(47);
		list.add(83);
		list.clear();		
		assertNull(list.current);
		assertEquals(0, list.size());
		assertTrue(list.isOff());
		// TODO: add assertion to make sure current is not off the right
		// or left side of the list??
		
	}

}
