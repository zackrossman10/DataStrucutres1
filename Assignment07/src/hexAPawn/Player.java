package hexAPawn;

// An interface for a player of hexapawn.
// (c) 2000, 2001 duane a. bailey

public interface Player
{
    // make sure your constructor accepts a char (HexBoard.WHITE or
    // HexBoard.BLACK) to play with.  This should be remembered.

    // include the following line in any of the main method to test
    // compatibility with autograder
    // 
	
    /**
     * @pre: node is a non-null game tree node
     *      opponent is the player to play after this player
     * @post: game is played from this node on; winning player is returned
     */
    public Player play(GameTree node, Player opponent);
}
 
