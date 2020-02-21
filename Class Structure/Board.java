import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ayman Bensreti
 *	Game board
 */
public class Board {
    Deque<CardEffect> potLuck;
    Deque<CardEffect> opportunityKnocks;
    int turns;
    LinkedList<Player> turnOrder;
    HashMap<Integer,BoardTile> tiles;// key: iD value: (Property|Effect)
    int taxPot; // subject to change see free parking in spec
    boolean repeat;

    /**
     *
     * @param turnOrder linked list of players in their turn order
     * @param tiles board representation as a hash map
     * @param potLuck deque of pot luck cards
     * @param opportunityKnocks deque of opportunity knocks cards
     */
    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks) {
        this.potLuck = potLuck;
        this.opportunityKnocks = opportunityKnocks;
        this.turnOrder = turnOrder;
        this.tiles = tiles;
        turns = 0;
        taxPot = 0;
        repeat =  false;
    }
    /**
     * draw pot luck card from the top of the deque
     * @return Effect
     */
    public CardEffect drawPotLuck() {
        return null;
    }
    /**
     * draw opportunity knocks card from the top of the deque
     * @return Effect
     */
    public  CardEffect drawOpportunityKnocks() {
        return null;
    }
    /**
     * rolls two die and returns the number of places to move
     * @return int
     */
    public int roll() {
        return 0;
    }
    /**
     * Bankrupts player from the game
     * @param target player to removed from turn order
     */
     void bankruptPlayer(Player target) {
         turnOrder.remove(target);
     }
}