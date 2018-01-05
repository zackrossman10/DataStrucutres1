package hexAPawn;

import java.util.Scanner;

/**
 * 
 * @author zackr
 * @version 10/29/17
 * 
 * This class constructs a compPlayer which picks moves from a gametree
 * that gets "trimmed" each time the compPlayer loses. At first, the comp
 * player does no better than the random player, but it will eventually 
 * choose the move that gives it the greatest likelihood of success. 
 */

public class CompPlayer implements Player {
	protected char color; //the color of this player (white or black)
	protected char opponentColor; //the color of the opponent
	protected GameTree trimmed; //the game tree that compplayer trims after each loss
	
	//pointers to keep track of previous moves and trees for access
	//during trimming
	protected GameTree lastGameTree;
	protected GameTree currentGameTree;
	protected HexMove lastMove;
	protected boolean newTree = true; //determine whether to set trimmed to node
	
	/**
	 * construct a human player
	 * @param col the color of human player (black or white)
	 */
	public CompPlayer(char col){
		color = col;
		opponentColor = HexBoard.opponent(color);	
	}
	
	/**
	 * @param node the current game tree representing the game
	 * @param opponent the opposing player 
	 * @post acts like a random player, but each time this player loses,
	 * it eliminates the last move and resulting board from last turn's game
	 * tree. Thus, the player will not choose moves that result in a high 
	 * probability of loss. 
	 */
	public Player play(GameTree node, Player opponent){
		System.out.print(node.board.toString());
		if(newTree){
			trimmed = node;
			if(opponent instanceof HumanPlayer){
				((HumanPlayer) opponent).newTree = false;
			}else{
				((RandPlayer) opponent).newTree = false;
			}
		}
		//if current board is a winning situation for the opponent,
		//end the game and trim the last game tree (which led to the loss)
		//from the second to last game tree
		if(node.board.win(opponentColor)){
			GameTree badTree = lastGameTree.updateTree(lastMove);
			lastGameTree.listChildren.remove(badTree);
			lastGameTree.moves.remove(lastMove);
			lastGameTree.possibleMoves--;
			return opponent;
		}else{
			//update the pointers
			lastGameTree = node;
			//random move from the trimmed tree by the computer
			currentGameTree = node.makeComputerMove();
			newTree = false;
			lastMove = node.lastMove;
			//pass the updated tree back to the opponent
			//note that the opponent doesn't get the advantage of the 
			//AI because we are only trimming this tree at places
			//that the comp color chooses
			return opponent.play(currentGameTree, this);
		}
	}
	
	public static void main(String[] args){
		//new AutograderCompTest();
		HexBoard board1 = new HexBoard();
		GameTree tree = new GameTree(board1, 'o');
		//create a new comp player who is white and moves first
		CompPlayer best = new CompPlayer('o');
		//create a new human player who is black (and therefore moves second)
		HumanPlayer player2 = new HumanPlayer('*');
		//play a game, print out the winning player
		Player winner = best.play(tree, player2);
		if(winner instanceof HumanPlayer){
			System.out.println("winner is "+player2.color+" player");
		}else{
			System.out.println("winner is "+best.color+" player");
		}
		System.out.println("Play again? y/n");
		Scanner scanner3 = new Scanner(System.in);
		//repeat the game while the player selects "y"
		while(scanner3.nextLine().toLowerCase().equals("y")){
			winner = best.play(best.trimmed, player2);
			if(winner instanceof HumanPlayer){
				System.out.println("winner is "+player2.color+" player");
			}else{
				System.out.println("winner is "+best.color+" player");
			}
			
			System.out.println("Play again? y/n");
		}
		System.out.println("GAME OVER, Adios");
		System.out.println();
		System.out.println("NEW GAME");
		//switch colors/turns, play the game again
		GameTree tree2 = new GameTree(board1, 'o');
		//create a new comp player who is white and moves first
		CompPlayer best2 = new CompPlayer('*');
		//create a new human player who is black (and therefore moves second)
		HumanPlayer player3 = new HumanPlayer('o');
		//play a game, print out the winning player
		Player winner2 = player3.play(tree2, best2);
		if(winner instanceof HumanPlayer){
			System.out.println("winner is "+player3.color+" player");
		}else{
			System.out.println("winner is "+best2.color+" player");
		}
		System.out.println("Play again? y/n");
		//repeat the game while the player selects "y"
		while(scanner3.nextLine().toLowerCase().equals("y")){
			winner = player3.play(best2.trimmed, best2);
			if(winner instanceof HumanPlayer){
				System.out.println("winner is "+player3.color+" player");
			}else{
				System.out.println("winner is "+best2.color+" player");
			}
			
			System.out.println("Play again? y/n");
		}
		System.out.println("GAME OVER, Adios");
		scanner3.close();
	}

}
