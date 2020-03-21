package ClassStructure;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Test Space for demoing visual elements of the game
 */
public class TestSpace {
    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();

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
        Random rand = new Random();
        for(int i = 0; i < 10; i++) {
            if(rand.nextInt(4) == 0) {
                opp.addFirst(new MoveBack3());
            }else{
                opp.addFirst(new BankPaysPlayer("Lucky you here's £20",20));
            }
            pot.addFirst(new PlayerPaysBank("Butter fingers loose £20",20));
        }
        //construction of a test board
        BoardTile temp;

        for (int i = 0; i < 8; i++) {
            if(i == 5) {
                temp = new Property(i, "Example Street", Group.GREEN, 10, 10, null, 10, ayman);
            }else if(i == 7) {
                temp = new Property(i, "Test crescent", Group.DEEP_BLUE, 10, 10, null, 10, calvin);
            }else if(i == 0) {
                    temp = new Go();
            }else{
                if(rand.nextInt(2) == 0) {
                    temp = new PotLuck(i);
                }else{
                    temp = new OpportunityKnocks(i);
                }
            }
            board.put(i, temp);
        }
        Board b = new Board(order, board, pot, opp);
        b.demo();//run demo method shown in sprint 1 meeting
    }
}

/**
 * Dummy tile effect class for testing
 */
class tImp extends TileEffect {

    public tImp(int iD, String title, boolean canPurchase, String text) {
        this.iD = iD;
        this.title = title;
        this.canPurchase = canPurchase;
        this.text = text;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        System.out.println("The current player is " + currentPlayer.getName() + " their token is " + currentPlayer.getToken().getSymbol() + "\n" + text);
    }
}
