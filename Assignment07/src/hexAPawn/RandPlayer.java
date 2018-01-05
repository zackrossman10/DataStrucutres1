package hexAPawn;

import java.util.Scanner;

/**
 * 
 * @author zackr
 * @version 10/29/17
 * 
 * This class creates a ranodm player which randomly selects valid moves
 * and competes against other human, rand, or comp players
 */

public class RandPlayer implements Player {
	protected char color; //the color of this player (white or black)
	protected char opponentColor; //the color of the opponent
	protected boolean newTree = true;
	/**
	 * 
	 * @param col the color assigned to the random player
	 */
	public RandPlayer(char col){
		color = col;
		opponentColor = HexBoard.opponent(color);
	}
	
	/**
	 * @param node the game tree whose root represents the current board
	 * @param opponent the opposing player
	 * @post makes a random move based on moves possible for the current game
	 * tree, interacts with opponent until the game is over
	 */
	public Player play(GameTree node, Player opponent){
		System.out.print(node.board.toString());
		//if current board is a winning situation for the opponendt,
		//end the game
		if(node.board.win(opponentColor)){
			return opponent;
		}else{
			//if tree is a new tree and opponent is a comp player, set
			//the comp player's trimmed tree to node
			if(opponent instanceof CompPlayer && newTree){
				((CompPlayer) opponent).trimmed = node;
				((CompPlayer) opponent).newTree = false;
			}
			//random move by the computer and makeComputerMove()
			//this method automatically updates the tree
			GameTree next = node.makeComputerMove();
			//recurse until a winning board is achieved
			return opponent.play(next, this);
		}
	}
	
	public static void main(String[] args){
		//construct an empty game board
		HexBoard game = new HexBoard();
		//construct a GameTree for this hexboard, white moving first
		GameTree optionTree = new GameTree(game, 'o');
		//create a new human player, player1, who is white (and therefore moves first)
		HumanPlayer player1 = new HumanPlayer('o');
		//create a new human player, player2, who is black (and therefore moves second)
		RandPlayer player2 = new RandPlayer('*');
		
		//play a game, print out the winning player
		Player winner = player1.play(optionTree, player2);
		if(winner instanceof RandPlayer){
			System.out.println("winner is "+player2.color+" player");
		}else{
			System.out.println("winner is "+player1.color+" player");
		}
		System.out.println("Play again? y/n");
		Scanner scanner3 = new Scanner(System.in);
		//repeat the game while the player selects "y"
		while(scanner3.nextLine().toLowerCase().equals("y")){
			winner = player1.play(optionTree, player2);
			if(winner instanceof RandPlayer){
				System.out.println("winner is "+player2.color+" player");
			}else{
				System.out.println("winner is "+player1.color+" player");
			}
			
			System.out.println("Play again? y/n");
		}
		System.out.println("GAME OVER, Adios");
		scanner3.close();
	}
}
