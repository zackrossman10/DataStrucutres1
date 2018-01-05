package hexAPawn;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author zackr
 * @version 10/29/17
 * 
 * This tests the GameTree class
 */

public class GameTreeTest {
	GameTree test;
	GameTree test1;
	GameTree test2;
	GameTree test3;
	
	@Before
	public void setUp(){
		test = new GameTree(new HexBoard(3,3), 'o',2);
		test1 = new GameTree(new HexBoard(3,2), 'o',1);
		test2 = new GameTree(new HexBoard(3,1), '*',2);
		test3 = new GameTree(new HexBoard(3,3), '*',1);		
	}
	
	//Construct Gametrees with boards of different sizes and
	//current players, check that number of nodes is correct
	//and that all instance variables are correct when the 
	//game tree is first constructed
	@Test
	public void construct() {
		assertTrue(test.numberNodes() == 252);
		assertEquals(15,test1.numberNodes());
		assertTrue(test2.numberNodes() == 2);
		assertTrue(test3.numberNodes() == 252);
		assertEquals(3, test.possibleMoves);
		assertEquals(test.moves.size(), test.listChildren.size());
		assertTrue(test.equals(test));
		assertTrue(test.currentPlayer == 'o');		
	}
	
	//Test that computer moves are correctly handled 
	//and the tree updates itself appropriately
	@Test
	public void computerMove(){
		GameTree next = test.makeComputerMove();
		assertTrue(next.currentPlayer == HexBoard.opponent(test.currentPlayer));
	}	
	
	//Test that the tree update method correctly handles
	//an illegal mov
	@Test
	public void humanMoveError(){
		test.updateTree(new HexMove(5,4,3));
		//test.updateTree(new HexMove(4,1,3));
	}
	
	//Test that the tree correctly handles an opponent's move
	@Test
	public void humanMove(){
		System.out.println(test3.moves.toString());
		test3.updateTree(new HexMove(6,3,3));	
		//System.out.print(test3.currentGameTree.board);
	}
	
	//Test that reset works correctly
	@Test
	public void resetPlayAgain(){
		test3.updateTree(new HexMove(6,3,3));
		System.out.print(test3.board.toString());
		System.out.print(test3.board.toString());
		assertTrue(test3.equals(test3));	
	}
	
	
	

}
