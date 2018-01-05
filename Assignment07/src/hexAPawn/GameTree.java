package hexAPawn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author zackr
 * @version 10/29/17
 * 
 * This class creates a game tree structure. A game tree has a board as the 
 * root, along with an arraylist of possible moves and possible next trees
 * 
 */

public class GameTree {
	protected HexBoard board;	//the current state of the game board, the root of the GameTree
	protected int possibleMoves; //the number possible moves that the player can choose from a node
	protected ArrayList<HexMove> moves = new ArrayList<HexMove>(); //the the possible moves that the player can choose from a node
	protected ArrayList<GameTree> listChildren; //the children of the current board
	protected int nodes = 1; 			//the minimum number of nodes in a game tree, 1=the root
	protected HexMove lastMove; 				//keeps track of the last move made by either player
	protected Random rand = new Random(); //a random object to pick the next move
	
	protected char currentPlayer; //the color of player who gets to move next;
	protected char player1; //the player who moves first
	protected char player2; //the player who moves second 
	protected int computerTurn; //either holds 1 or 2, meaning the computer makes the 1st
								//or second turn
	protected boolean found; //whether the last move was a legal move
	
	/**
	 *  
	 * @param newBoard a new HexBoard that will act as the root of
	 * the game tree
	 * @param player the color of player who gets to move first
	 * @param turn whether the human opponent wants to play first(1) or second(2)
	 * 
	 * @pre turn =1 or turn =2
	 */
	public GameTree(HexBoard newBoard, char player, int turn) throws IllegalArgumentException{
		this(newBoard, player);
		if(turn != 1 && turn!= 2) throw new IllegalArgumentException();
		//assign the computer a turn, 1 if turn =2 and 2 if turn =1
		computerTurn = (turn+1)%2;
		//if the computer plays first, they are player
		if(computerTurn ==1){
			player1 = player;
			player2 = HexBoard.opponent(player1);
			currentPlayer = player1;
		}else{
			//if the computer plays second, they are the player's opponent
			player1 = HexBoard.opponent(player);
			player2 = player;
			currentPlayer = player2;
		} 
	}
	
	/**
	 * 
	 * @param newBoard a new HexBoard that will act as the root of
	 * the game tree
	 * @param player the color of player who gets to move first
	 * 
	 */
	public GameTree(HexBoard newBoard, char player){
		board = newBoard;
		player1 = player;
		currentPlayer = player1;
		//determine the possible moves can be made by player with the given board
		possibleMoves = board.moves(player1).size();
		moves = board.moves(player1);
		//construct an array to hold the permutations of different boards
		//possible based on the current situation
		listChildren = new ArrayList<GameTree>(possibleMoves); 
		//construct the GameTree
		construct();
		//set the current pointer to this game tree
		//currentGameTree=this;
	}
	
	/**
	 * constructs a GameTree based on ArrayLists. The children, who themselves are GameTrees;
	 * are added to an ArrayList of GameTrees. The method is recursive, base case occurs
	 * when the board passed into the new GameTree is a winning board for the opponent (this 
	 * is a leaf).
	 */
	public void construct(){
		//if the opponent hasn't won, keep creating a new ArrayList 
		//to represent children of this GameTree
		if(!board.win(HexBoard.opponent(currentPlayer))){
			//keeps track of how many children we have added to the current node
			int movesTested = 0;
			while(movesTested<possibleMoves){
				//create a new HexBoard with one possible move from moves
				HexBoard test = new HexBoard(board, moves.get(movesTested));
				//create a new GameTree
				GameTree recurse = new GameTree(test, HexBoard.opponent(currentPlayer));
				listChildren.add(recurse);
				movesTested++;
				nodes+= recurse.nodes;
			}
		}		
	}
	
	/**
	 * 
	 * @return a game tree that results from making a computer move
	 * @post: select a move from the arraylist moves and find the board that 
	 * results from making that move
	 */
	public GameTree makeComputerMove(){
		//generate an integer between 0 and (possibleMoves)
		int indexlastMove = rand.nextInt(board.moves(currentPlayer).size());
		//construct a move 
		lastMove = board.moves(currentPlayer).get(indexlastMove);
		//print out the computer's move
		System.out.println("Computer: "+lastMove.toString());
		//update the tree after making this move
		return updateTree(lastMove);
	}
	
	/**
	 * return the tree that results from making a move on the current board
	 */
	public GameTree updateTree(HexMove move)throws IllegalArgumentException{
		//check that move is a possible move
		int index = 0;
		found =false;
		while(!found && index<possibleMoves){
			if(moves.get(index).equals(move)){
				found = true;
				break;
			}
			index++;
		}
		
		//if the move was not found, it's not a valid move.
		//don't update the tree and return the current tree
		if(!found){
			System.out.print("Move not possible");
			return this;
		}
		//determine the last moves for player1/2
		/*if(currentPlayer == player1){
			lastPlayer1Move = lastMove;
		}else{
			lastPlayer2Move = lastMove;
		}*/
		//the node that the Game Tree needs to traverse to
		//FIX FOR THE AI
		lastMove = move;
		GameTree next = listChildren.get(index);
		//change the current player after they make a move
		next.currentPlayer = HexBoard.opponent(currentPlayer);
		//update current so that we traverse the game tree
		return next;
	}
	
	/**
	 * @return the number of possible moves for the current player 
	 * given the current board
	 */
	public int childrenSize(){
		return listChildren.size();
	}	
	
	/**
	 * @return the number of nodes in this GameTree
	 */
	public int numberNodes(){
		return nodes;
	}
	
	/**
	 * @return the string representation of the GameTree
	 */
	public String toString(){
		return board.toString();
	}
}
