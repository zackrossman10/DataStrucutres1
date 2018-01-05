package hexAPawn;

import java.util.Scanner;

/**
 * 
 * @author zackr
 * @version 10/29/17
 * 
 * This class allows for a human player to imput moves on a console 
 * and play against other human, random, or comp players
 */

public class HumanPlayer implements Player{
	protected char color; //the color of this player (white or black)
	protected char opponentColor; //the color of the opponent
	protected HexMove lastMove;
	protected boolean newTree = true;
	
	/**
	 * construct a human player
	 * @param col the color of human player (black or white)
	 */
	public HumanPlayer(char col){
		color = col;
		opponentColor = HexBoard.opponent(color);
	}
	
	/**
	 * @param node the game tree whose root represents the current board
	 * @param opponent the opposing player
	 * @post if the current board doesn't represent a winning situation for the 
	 * opponent, prompt the user to input 2 digits representing player's move
	 * Once a move is made, pass the updated gametree to the opponent and await their
	 * response.
	 */
	public Player play(GameTree node, Player opponent){
		//if the board configuration is a win for the opponent, return opponent
		//and print the winning board
		if(node.board.win(opponentColor)){
			System.out.print(node.board.toString());
			return opponent;
		}else{
			//if tree is a new tree and opponent is a comp player, set
			//the comp player's trimmed tree to node
			if(newTree && opponent instanceof CompPlayer){
				((CompPlayer) opponent).trimmed = node;
				((CompPlayer) opponent).newTree = false;
			}
			//otherwise, print out the current board and as the player what move
			//they would like to make
			System.out.println("Current Board:");
			System.out.print(node.board.toString());
			System.out.println("Make a move, player "+node.currentPlayer);
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			//take in 2 integers from user input
			System.out.print("Move from: ");
			int from = scanner.nextInt();
			System.out.print("Move to: ");
			int to = scanner.nextInt();
			//construct a new move from this input
			lastMove = new HexMove(from, to, node.board.cols);
			//update the tree after making this move
			GameTree next = node.updateTree(lastMove);		
			//the tree will not update for an illegal move
			//if this is the case, prompt user for another move
			while(!node.found){
				System.out.println("Illegal: "+lastMove.toString());
				System.out.println("Please enter a new move");
				//take in 2 integers from user input
				System.out.print("Move from: ");
				from = scanner.nextInt();
				System.out.println();
				System.out.print("Move to: ");
				to = scanner.nextInt();
				System.out.println();
				//construct a new move from this input
				lastMove = new HexMove(from, to, node.board.cols);
				//update the tree after making this move
				next = node.updateTree(lastMove);		
			}
			//prompt the opponent to make a move on the new board,
			//eventually resulting in a winning player
			return opponent.play(next, this);
			
		}
	}
	
	public static void main(String[] args){
		new AutograderCompTest();
		//construct an empty game board
		HexBoard game = new HexBoard();
		//construct a GameTree for this hexboard, white moving first
		GameTree optionTree = new GameTree(game, 'o');
		//create a new human player, player1, who is white (and therefore moves first)
		HumanPlayer player1 = new HumanPlayer('o');
		//create a new human player, player2, who is black (and therefore moves second)
		HumanPlayer player2 = new HumanPlayer('*');
		
		//play a game, print out the winning player
		HumanPlayer winner = (HumanPlayer) player1.play(optionTree, player2);
		System.out.println("winner is "+winner.color+" player");
		System.out.println("Play again? y/n");
		Scanner scanner3 = new Scanner(System.in);
		//repeat the game while the player selects "y"
		while(scanner3.nextLine().toLowerCase().equals("y")){
			//reset the option tree
			winner = (HumanPlayer) player1.play(optionTree, player2);
			System.out.println("winner is "+winner.color+" player");
			System.out.println("Play again? y/n");
		}
		System.out.println("GAME OVER, Adios");
		scanner3.close();
	}
}
