package ClassStructure;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static Board board;
    private  static HashMap<Integer, BoardTile> tileSet = new HashMap<Integer, BoardTile>();
    private  static LinkedList<Player> order = new LinkedList<Player>();
    private static Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
    private static Deque<CardEffect> opp = new ArrayDeque<CardEffect>();

    @BeforeEach
    public  void setup() {
        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND);
        Player danny = new Player("Danny", Token.CAT);
        Player jacob = new Player("Jacob", Token.BOOT);
        Player calvin = new Player("Calvin", Token.SMARTPHONE);
        Player callum = new Player("Callum", Token.SPOON);
        Player tom = new Player("Tom", Token.GOBLET);
        //load players into turn order
        order = new LinkedList<Player>();
        order.add(ayman);
        order.add(danny);
        order.add(jacob);
        order.add(calvin);
        order.add(callum);
        order.add(tom);

        //add default set of cards to decks
        Random rand = new Random();
        opp = new ArrayDeque<CardEffect>();
        pot = new ArrayDeque<CardEffect>();
        //construction of a test board
        BoardTile temp;

        for (int i = 0; i < 8; i++) {
            if(i == 5) {
                temp = new Property(i, "Example Street", Group.GREEN, 10, 10, null, 10, null);
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
            tileSet.put(i, temp);
        }
        board = new Board(order, tileSet, pot, opp);
    }

    /**
     * Card drawing test to insure insertion att bottom of deck after draw
     * checking the card order is preserved when a get out of jail free card is drawn
     */
    @Test
    public void cardDrawTest() {
        opp.add(new MoveBack3());//firstCard
        opp.add(new PlayerPaysBank("Test1",20));//secondCard
        opp.add(new Birthday());//thirdCard
        opp.add(new GetOutOfJail("Eh lad yahrr free t'go"));
        pot.add(new MoveBack3());
        pot.add(new PlayerPaysBank("Test1",20));
        pot.add(new Birthday());

        board = new Board(order, tileSet, pot, opp);//create board with current card setup
        CardEffect firstCard = board.drawOpportunityKnocks();
        assertEquals(firstCard,board.opportunityKnocks.peekLast());
        CardEffect secondCard = board.drawOpportunityKnocks();
        assertEquals(secondCard,board.opportunityKnocks.peekLast());
        CardEffect thirdCard = board.drawOpportunityKnocks();
        assert(thirdCard instanceof Birthday);//Check that cards are being drawn in order they are inserted
        CardEffect fourthCard = board.drawOpportunityKnocks();
        fourthCard.effect(board.turnOrder.getFirst());
        assertNotEquals(fourthCard,board.opportunityKnocks.peekLast());//checks classStructure.GetOutOfJail card has not been added to deck
        ((GetOutOfJail)fourthCard).playCard();
        assertTrue(board.opportunityKnocks.peekLast() instanceof GetOutOfJail);//Checks that once the card is played
        assertEquals(board.opportunityKnocks.peekLast(),fourthCard);//It is returned to the bottom of deck
    }

    /**
     * Test that the player paying the amount set by the card
     * test that a player is bankrupt when they cannot pay
     */
    @Test
    public void playerPays() {
        opp = new ArrayDeque<CardEffect>();
        CardEffect card = new PlayerPaysBank("Hahahahah",100);
        CardEffect card2 = new PlayerPaysBank("Hahahahah",1600);
        opp.add(card);
        opp.add(card2);
        board = new Board(order,tileSet,pot,opp);
        //assertEquals(board.turnOrder.getFirst().getMoney(),1500);//Checks initial money
        board.drawOpportunityKnocks().effect(board.turnOrder.getFirst());
        assertEquals(1400,board.turnOrder.getFirst().getMoney());
        assertEquals(6,board.turnOrder.size());//checks numbers of players in game hasn't changed
        board.drawOpportunityKnocks().effect(board.turnOrder.getFirst());
        assertEquals(5,board.turnOrder.size());//check player has been bankrupted
    }

    /**
     * Testing player correctly receives money from a from currently implemented sources
     */
    @Test
    public void playerReceivesMoney() {
        pot.add(new Birthday());
        pot.add(new BankPaysPlayer("Money for player",100));
        board = new Board(order,tileSet,pot,opp);
        assertEquals(1500,board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1550,board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1650,board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1700,board.turnOrder.getFirst().getMoney());
    }

    /**
     * Testing the move player cards work
     * Move No classStructure.Go check and Move with classStructure.Go check
     * Move back 3
     * Additionally tests that player correctly receives money when they pass go
     */
    @Test
    public void movePlayer() {
        pot.add(new MovePlayerNoPassGo("classStructure.Go here",2));
        pot.add(new MoveBack3());
        pot.add(new MovePlayerPassGo("Move here",2));
        board = new Board(order,tileSet,pot,opp);
        assertEquals(0,board.turnOrder.getFirst().getCurrentPos());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(2,board.turnOrder.getFirst().getCurrentPos());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(7,board.turnOrder.getFirst().getCurrentPos());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(2,board.turnOrder.getFirst().getCurrentPos());
        assertEquals(1700,board.turnOrder.getFirst().getMoney());//checks that player received money for passGO
    }

    /**
     * Test of tax paying when card is activated
     * Awarding tax when a player lands on free parking
     */
    @Test
    public void taxPaying() {
        tileSet = new HashMap<Integer, BoardTile>();
        pot.add(new PlayerPaysTax("Bob variable tax",10));
        pot.add(new PlayerPaysTax("Bob variable tax",10));
        pot.add(new PlayerPaysTax("Bob variable tax",10));
        //construct tax test board
        tileSet.put(0,new Go());
        for( int i = 1; i < 7;i++) {
            tileSet.put(i,new PotLuck(i));//Add pot luck draw tiles
        }
        tileSet.put(7, new FreeParking(7));
        board = new Board(order,tileSet,pot,opp);//Build board for this test
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setCurrentPos(1);//move player to first tile
        assertEquals(1500, currentPlayer.getMoney());
        assertEquals(0,board.taxPot);//check the tax pot is empty
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);//take 10 from player and add to pot
        assertEquals(1490,currentPlayer.getMoney());//check players money has been deducted
        assertEquals(10,board.taxPot);//check that correct amount has been added to the pot
        currentPlayer.setCurrentPos(7);//Move player to free parking
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);//activate tile
        assertEquals(1500,currentPlayer.getMoney());//check player received tax pot
        assertEquals(0,board.taxPot);//check the tax pot has been emptied
    }

    /**
     * Test of rent paying when property is landed on
     */
    @Test
    public void rentTest() {

        Player calvin = order.get(3); //owner of test property
        Player currentPlayer = order.peekFirst(); //ayman

        //move player to property
        currentPlayer.setCurrentPos(7);
        //activate effect of tile
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);
        assertEquals(1490,currentPlayer.getMoney()); //has paid rent
        assertEquals( 1510, calvin.getMoney()); //has received rent
        //move current player to property with no owner
        currentPlayer.setCurrentPos(5);
        //activate effect, no rent should be paid
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);
        assertEquals(1490,currentPlayer.getMoney()); //has not paid rent

    }
}