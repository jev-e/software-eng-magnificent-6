import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author Ayman Bensreti
 *	Interface for the game board
 */
public interface Board {
	Deque<Effect> potLuck = null;
	Deque<Effect> opportunityKnocks = null;
	int turns = 0;
	LinkedList<Player> turnOrder = null;
	HashMap<Integer,BoardTile> tiles = null;// key: iD value: (Property|Effect)
	int taxPot = 0; // subject to change see free parking in spec
	boolean repeat = false;
	
	/**
	 * draw pot luck card on the top of the deque
	 * @return Effect  
	 */
	public Effect drawPotLuck();
	/**
	 * draw opportunity knocks card on the top of the deque
	 * @return Effect
	 */
	public Effect drawOpportunityKnocks();
	/**
	 * rolls two die and returns the number of places to move
	 * @return int
	 */
	public int roll();
	/**
	 * Bankrupts player from the game
	 * @param player target player to removed from turn order
	 */
	void bankruptPlayer(Player player);
}
