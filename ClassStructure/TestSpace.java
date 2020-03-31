package ClassStructure;

import javax.smartcardio.Card;
import java.io.IOException;
import java.util.*;

/**
 * Test Space for demoing visual elements of the game
 */
public class TestSpace {
    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> oppourtunityKnocksPack;
    private static List<CardEffect> potLuckPack;

    public static void main(String args[]) {
        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND);
        Player danny = new Player("Danny", Token.CAT);
        Player jacob = new Player("Jacob", Token.BOOT);
        Player calvin = new Player("Calvin", Token.SMARTPHONE);
        Player callum = new Player("Callum", Token.SPOON);
        Player tom = new Player("Tom", Token.GOBLET);
        //load players into turn order
        order.add(ayman);
        order.add(danny);
        order.add(jacob);
        order.add(calvin);
        order.add(callum);
        order.add(tom);

        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp = new ArrayDeque<CardEffect>();
        //add cards to decks

        try{
            board = Json.fromJsonToTileSet("BoardTileData.json");
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            oppourtunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        }catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(oppourtunityKnocksPack);//shuffles order
        Collections.shuffle(potLuckPack);

        pot = new ArrayDeque<>(potLuckPack);//Load shuffled pack into decks
        opp = new ArrayDeque<>(oppourtunityKnocksPack);

        Board b = new Board(order, board, pot, opp);
        b.demo();//run demo method shown in sprint 1 meeting
    }
}

