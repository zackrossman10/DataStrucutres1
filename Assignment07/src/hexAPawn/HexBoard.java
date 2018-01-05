package hexAPawn;

/**
 *  A class for representing a HexAPawn board.<p>
 * (c) 2000, 2001 duane a. bailey<p>
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This class represents the start of the HexAPawn board.
 * Note that there is a constructor that creates a board
 * given an original board configuration and a HexMove.  This
 * will be handy while building the game tree.
 *
 * The public methods in this class are sufficient to 
 * complete the assignment- you should not need to change
 * or add to the class in any way.
 *
 * Here is an example of how to use the features of the HexBoard:
 * <PRE>
 *   public static void main(String[] args) {
 *       HexBoard b = new HexBoard(3,3); // change me!
 *       ArrayList<HexMove> moves;
 *       ReadStream r = new ReadStream();
 *       int yourMove;
 *       int myMove;
 *       Random gen = new Random();
 *       System.out.println(b);
 *
 *       do {
 *           // white to play (human): print moves
 *           moves = b.moves(WHITE);
 *           Iterator i = moves.iterator();
 *           int j = 0;
 *           while (i.hasNext()) {
 *                System.out.println(j+". "+i.next());
 *                j++;
 *           }
 *           // read move from keyboard
 *           yourMove = r.readInt();
 *
 *           // construct a new board, based on move
 *           b = new HexBoard(b,(HexMove)moves.elementAt(yourMove));
 *           System.out.println(b);
 *
 *           // if WHITE won, claim the victory and leave.
 *           if (b.win(WHITE)) { System.out.println("You win!"); break; }
 *
 *           // black's move (compute): move randomly, but legally
 *           // get moves
 *           moves = b.moves(BLACK);
 *           // pick one
 *           myMove = Math.abs(gen.nextInt()) % moves.size();
 *           // construct new board
 *           b = new HexBoard(b,(HexMove)moves.elementAt(myMove));
 *           System.out.println("I "+moves.elementAt(myMove));
 *           System.out.println(b);
 *           // claim victory, if true
 *           if (b.win(BLACK)) { System.out.println("I win!"); break; }
 *       } while (true);
 *   }
 * </PRE>
 *
 *
 */

public class HexBoard {
	protected int board[];
	protected int rows, cols;
	protected ArrayList<HexMove> whiteMoves, blackMoves;
	public final static char WHITE = 'o';
	public final static char BLACK = '*';
	public final static char SPACE = ' ';

	/**
	 * Creates a standard 3x3 board, with white pieces in row 1 and black pieces
	 * in row 3. @post constructs a standard hexapawn board
	 */
	public HexBoard() {
		this(3, 3);
	}

	/**
	 * Creates a board with the given number of rows and columns. @pre r, c >= 3
	 * (shape of board) @post constructs a hexapawn board, white on top, black
	 * on bottom
	 */
	public HexBoard(int r, int c) {
		rows = r;
		cols = c;
		board = new int[rows * cols];
		for (int pos = 0; pos < rows * cols; pos++) {
			if (pos < cols)
				board[pos] = WHITE;
			else if (pos >= (rows - 1) * cols)
				board[pos] = BLACK;
			else
				board[pos] = SPACE;
		}
		whiteMoves = blackMoves = null;
	}

	/**
	 * Construct a board given an original board configuration and a HexMove.
	 * This will be handy while building the game tree. 
	 * @pre orig is valid, move m is meaningful 
	 * @post constructs a new hexapawn board derived from orig after m
	 */
	public HexBoard(HexBoard orig, HexMove m) {
		rows = orig.rows;
		cols = orig.cols;
		whiteMoves = blackMoves = null;
		board = new int[rows * cols];
		for (int i = 0; i < rows * cols; i++)
			board[i] = orig.board[i];
		board[m.to()] = board[m.from()];
		board[m.from()] = SPACE;
	}

	/**
	 * Returns the opposite player color. 
	 * @pre m is WHITE or BLACK 
	 * @post returns opponent
	 */
	public static char opponent(char m) {
		if (m == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}

	/**
	 * Returns true if the board is a winning board for the player with 
	 * playerColor after that player has moved. 
	 * @pre m is WHITE or BLACK 
	 * @post returns opponent
	 */
	public boolean win(char playerColor) {
		if (playerColor == WHITE) {
			for (int pos = (rows - 1) * cols; pos < rows * cols; pos++) {
				if (board[pos] == WHITE)
					return true;
			}
			return 0 == moves(BLACK).size();
		} else {
			for (int pos = 0; pos < cols; pos++) {
				if (board[pos] == BLACK)
					return true;
			}
			return 0 == moves(WHITE).size();
		}
	}

	/**
	 * Returns a ArrayList<HexMove> of HexMoves that player playerColor could make on 
	 * the current board. 
	 * @pre playerColor is BLACK or WHITE 
	 * @post returns an ArrayList of possible hexapawn moves from this 
	 * position, provided playerColor moves next. 
	 */
	public ArrayList<HexMove> moves(char playerColor) {
		if (playerColor == WHITE) {
			if (whiteMoves != null)
				return whiteMoves;
			whiteMoves = new ArrayList<HexMove>();
			for (int pos = 0; pos < (rows - 1) * cols; pos++) {
				if (board[pos] == WHITE) {
					if (board[pos + cols] == SPACE) {
						whiteMoves.add(
							new HexMove(pos, pos + cols, cols));
					}
					if ((pos % cols) != 0
						&& board[pos + (cols - 1)] == BLACK) {
						whiteMoves.add(
							new HexMove(pos, pos + cols - 1, cols));
					}
					if ((pos % cols) != (cols - 1)
						&& board[pos + cols + 1] == BLACK) {
						whiteMoves.add(
							new HexMove(pos, pos + cols + 1, cols));
					}
				}
			}
			return whiteMoves;
		} else {
			if (blackMoves != null)
				return blackMoves;
			blackMoves = new ArrayList<HexMove>();
			for (int pos = cols; pos < rows * cols; pos++) {
				if (board[pos] == BLACK) {
					if (board[pos - cols] == SPACE) {
						blackMoves.add(
							new HexMove(pos, pos - cols, cols));
					}
					if ((pos % cols) != 0 && board[pos - cols - 1] == WHITE) {
						blackMoves.add(
							new HexMove(pos, pos - cols - 1, cols));
					}
					if ((pos % cols) != cols - 1
						&& board[pos - cols + 1] == WHITE) {
						blackMoves.add(
							new HexMove(pos, pos - cols + 1, cols));
					}
				}
			}
			return blackMoves;
		}
	}

	/**
	 * Returns a String representation of the board. 
	 * @post returns a printable version of the board.
	 */
	public String toString() {
		String result = "-";
		for (int pos = 0; pos < cols; pos++)
			result += "--";
		result += "\n";
		for (int pos = 0; pos < rows * cols; pos++) {
			result += " " + ((char) board[pos]);
			if ((cols - 1) == (pos % cols))
				result += '\n';
		}
		for (int pos = 0; pos < cols; pos++)
			result += "--";
		result += "-\n\n";
		return result;
	}

	/**
	 * An example of how to use this class: play a game of hexapawn. Use this to
	 * become familiar with game.
	 */
	public static void main(String[] args) {
		HexBoard b = new HexBoard(3, 3); // change me!
		ArrayList<HexMove> moves;
		Scanner r = new Scanner(System.in);
		int yourMove;
		int myMove;
		Random gen = new Random();
		System.out.println(b);

		do {
			// white to play (human): print moves
			moves = b.moves(WHITE);
			Iterator<HexMove> i = moves.iterator();
			int j = 0;
			while (i.hasNext()) {
				System.out.println(j + ". " + i.next());
				j++;
			}
			// read move from keyboard
			yourMove = r.nextInt();

			// construct a new board, based on move
			b = new HexBoard(b, moves.get(yourMove));
			System.out.println(b);

			// if WHITE won, claim the victory and leave.
			if (b.win(WHITE)) {
				System.out.println("You win!");
				break;
			}

			// black's move (compute): move randomly, but legally
			// get moves
			moves = b.moves(BLACK);
			// pick one
			myMove = Math.abs(gen.nextInt(moves.size()));
			// construct new board
			b = new HexBoard(b, moves.get(myMove));
			System.out.println("I " + moves.get(myMove));
			System.out.println(b);
			// claim victory, if true
			if (b.win(BLACK)) {
				System.out.println("I win!");
				break;
			}
		}
		while (true);
	}
}
