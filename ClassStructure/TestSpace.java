package ClassStructure;

import java.io.IOException;
import java.util.*;

/**
 * Test Space for demoing visual elements of the game
 */
public class TestSpace {
    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> opportunityKnocksPack;
    private static List<CardEffect> potLuckPack;

    public static void main(String args[]) {


        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp = new ArrayDeque<CardEffect>();
        //add cards to decks

        try{
            board = Json.fromJsonToTileSet("BoardTileData.json");
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        }catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(opportunityKnocksPack);//shuffles order
        Collections.shuffle(potLuckPack);

        pot = new ArrayDeque<>(potLuckPack);//Load shuffled pack into decks
        opp = new ArrayDeque<>(opportunityKnocksPack);

        Board b = new Board(order, board, pot, opp);
        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND, b);
        Player danny = new Player("Danny", Token.CAT, b);
        Player jacob = new Player("Jacob", Token.BOOT, b);
        Player calvin = new Player("Calvin", Token.SMARTPHONE, b);
        Player callum = new Player("Callum", Token.SPOON, b);
        Player tom = new Player("Tom", Token.GOBLET, b);
        //load players into turn order
        order.add(ayman);
        order.add(danny);
        order.add(jacob);
        order.add(calvin);
        order.add(callum);
        order.add(tom);
        try {
            b.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred in game execution");
        }
    }
}

