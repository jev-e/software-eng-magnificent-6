package ClassStructure;


import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static Board board;
    private static HashMap<Integer, BoardTile> tileSet = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
    private static Deque<CardEffect> opp = new ArrayDeque<CardEffect>();


    public void jsonDataBoard() throws IOException {
        tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        List<CardEffect> potPack = Json.fromJsonToList("PotLuckCardData.json");
        List<CardEffect> oppPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
        //Collections.shuffle(potPack);//shuffle decks
        //Collections.shuffle(oppPack);
        pot = new ArrayDeque<CardEffect>(potPack);
        opp = new ArrayDeque<CardEffect>(oppPack);
        board = new Board(order, tileSet, pot, opp, "full");
        for (Player p : order) {
            p.setBoard(board);//assign the board to the players
        }
    }

    @BeforeEach
    public void setup() {

        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND, null);
        Player danny = new Player("Danny", Token.CAT, null);
        Player jacob = new Player("Jacob", Token.BOOT, null);
        Player calvin = new Player("Calvin", Token.SMARTPHONE, null);
        Player callum = new Player("Callum", Token.SPOON, null);
        Player tom = new Player("Tom", Token.GOBLET, null);
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
        board = new Board(order, tileSet, pot, opp, "full");

        for( Player pp : board.turnOrder ){
            pp.setBoard( board );
        }


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
        GetOutOfJail jailCard = new GetOutOfJail("Eh lad yahrr free t'go");
        jailCard.setDeck("opportunity");
        opp.add(jailCard);
        jailCard = new GetOutOfJail("ur out feller");
        jailCard.setDeck("pot");
        pot.add(jailCard);
        pot.add(new MoveBack3());
        pot.add(new PlayerPaysBank("Test1",20));
        pot.add(new Birthday());


        board = new Board(order, tileSet, pot, opp, "full");//create board with current card setup
        CardEffect firstCard = board.drawOpportunityKnocks();
        assertEquals(firstCard,board.opportunityKnocks.peekLast());
        CardEffect secondCard = board.drawOpportunityKnocks();
        assertEquals(secondCard,board.opportunityKnocks.peekLast());
        CardEffect thirdCard = board.drawOpportunityKnocks();
        assert(thirdCard instanceof Birthday);//Check that cards are being drawn in order they are inserted
        CardEffect fourthCard = board.drawOpportunityKnocks();
        fourthCard.effect(board.turnOrder.getFirst());
        assertNotEquals(fourthCard,board.opportunityKnocks.peekLast());//checks classStructure.GetOutOfJail card has not been added to deck
        ((GetOutOfJail)fourthCard).returnCard();
        assertTrue(board.opportunityKnocks.peekLast() instanceof GetOutOfJail);//Checks that once the card is played
        assertEquals(board.opportunityKnocks.peekLast(),fourthCard);//It is returned to the bottom of deck

        CardEffect potFirst = board.drawPotLuck();
        assertTrue(potFirst instanceof GetOutOfJail);//check card retrieved correctly
        ((GetOutOfJail)potFirst).returnCard();
        assertEquals(board.potLuck.peekLast(), potFirst);//check card retrieved correctly
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
        board = new Board(order, tileSet, pot, opp, "full");
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
        pot.add(new BankPaysPlayer("Money for player", 100));
        board = new Board(order, tileSet, pot, opp, "full");
        assertEquals(1500, board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1550, board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1650, board.turnOrder.getFirst().getMoney());
        board.drawPotLuck().effect(board.turnOrder.getFirst());
        assertEquals(1700, board.turnOrder.getFirst().getMoney());
    }

    /**
     * Testing the move player cards work
     * Move No classStructure.Go check and Move with classStructure.Go check
     * Move back 3
     * Additionally tests that player correctly receives money when they pass go
     */
    @Test
    public void movePlayer() throws IOException {
        tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        MovePlayerNoPassGo c1 = new MovePlayerNoPassGo("Go here",2);//move player to trigger pot luck
        pot.add(c1);
        pot.add(new MoveBack3());
        pot.add(new MovePlayerPassGo("go to income Tax",4));
        pot.add(new MovePlayerNoPassGo("go to pot luck",2));
        pot.add(new Birthday());
        opp.add(new MoveBack3());
        opp.add(new Birthday());

        board = new Board(order, tileSet, pot, opp, "full");
        Player currentPlayer = board.turnOrder.getFirst();
        assertEquals(0,currentPlayer.getCurrentPos());
        c1.effect(currentPlayer);//move to pot luck and draws move back 3
        assertEquals(39,currentPlayer.getCurrentPos());//check player has been moved correctly
        assertEquals(currentPlayer.getMoney(),1500);
        board.drawPotLuck().effect(currentPlayer);
        assertEquals(currentPlayer.getMoney(),1500);//player is given 200 for passing go but loses 200 from income tax
        assertEquals(4,currentPlayer.getCurrentPos());//check player is on income tax
        board.drawPotLuck().effect(currentPlayer);
        assertEquals(2,currentPlayer.getCurrentPos());//check player is on pot luck
        assertEquals(currentPlayer.getMoney(),1550);//check player did not receive money for passing go but received birthday money
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
        board = new Board(order, tileSet, pot, opp, "full");//Build board for this test
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
        assertEquals(1490, currentPlayer.getMoney()); //has paid rent
        assertEquals(1510, calvin.getMoney()); //has received rent
        //move current player to property with no owner
        currentPlayer.setCurrentPos(5);
        //activate effect, no rent should be paid
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);
        assertEquals(1490, currentPlayer.getMoney()); //has not paid rent

    }

    /**
     * Player bankrupt function and returning assets
     */
    @Test
    public void bankruptTest() throws IOException {
        jsonDataBoard();//Reads in full game data for test
        for (Player p : board.turnOrder) {
            if (p.getName() != "Ayman") {//Sets all players money apart from Ayman's to £10
                p.setMoney(10);
            }
        }
        Player currentPlayer = board.turnOrder.get(1);//gets Danny from turn order
        currentPlayer.setCurrentPos(4);//Moves Danny to income tax
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);//activate tile
        //Danny cannot pay £200 fine
        assertFalse(board.turnOrder.contains(currentPlayer));//Check danny has been removed
        currentPlayer = board.turnOrder.get(0);//gets Ayman from turn order
        currentPlayer.setCurrentPos(4);//Moves Ayman to income tax
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);//activate tile
        //Ayman can pay £200 fine
        assertTrue(board.turnOrder.contains(currentPlayer));//Check Ayman has not been removed

        currentPlayer = board.turnOrder.get(1);//get Jacob
        ((Property) board.tiles.get(1)).setOwner(currentPlayer);//give jacob crapper street
        currentPlayer.addAsset(board.tiles.get(1)); //add to asset list
        //jacob net worth  £10 + crapper street = £10 + £60 = £70
        assertEquals(currentPlayer, ((Property) board.tiles.get(1)).getOwner());//ownership check
        currentPlayer.setCurrentPos(4);//Moves Ayman to income tax
        board.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);//activate tile
        //Jacob cannot pay £200 fine, crapper street returned to bank
        assertNull(((Property) board.tiles.get(1)).getOwner());//ownership check
    }

    /**
     * Station rent amount checks
     *
     * @throws IOException
     */
    @Test
    public void stationRentTest() throws IOException {
        jsonDataBoard();//load data
        Player a = board.turnOrder.get(0);// get Ayman
        Player d = board.turnOrder.get(1);// get Danny

        d.addAsset(board.tiles.get(5));//give station to Danny
        ((Station) board.tiles.get(5)).setOwner(d);
        a.setCurrentPos(5);//move Ayman to station
        assertEquals(1500, a.getMoney());//check money before
        assertEquals(1500, d.getMoney());
        board.tiles.get(5).activeEffect(a);//activate tile
        assertEquals(1475, a.getMoney());//check money before
        assertEquals(1525, d.getMoney());

        d.addAsset(board.tiles.get(15));//give station to Danny
        ((Station) board.tiles.get(15)).setOwner(d);
        a.setCurrentPos(15);//move Ayman to station
        board.tiles.get(15).activeEffect(a);//activate tile
        assertEquals(1425, a.getMoney());//check money before
        assertEquals(1575, d.getMoney());

        d.addAsset(board.tiles.get(25));//give station to Danny
        ((Station) board.tiles.get(25)).setOwner(d);
        a.setCurrentPos(25);//move Ayman to station
        board.tiles.get(25).activeEffect(a);//activate tile
        assertEquals(1325, a.getMoney());//check money before
        assertEquals(1675, d.getMoney());

        d.addAsset(board.tiles.get(35));//give station to Danny
        ((Station) board.tiles.get(35)).setOwner(d);
        a.setCurrentPos(35);//move Ayman to station
        board.tiles.get(35).activeEffect(a);//activate tile
        assertEquals(1125, a.getMoney());//check money before
        assertEquals(1875, d.getMoney());
    }

    /**
     * Test of utility payment based on number owned
     *
     * @throws IOException
     */
    @Test
    public void testUtility() throws IOException {
        jsonDataBoard();//read in data
        Player a = board.turnOrder.get(0);// get Ayman
        Player d = board.turnOrder.get(1);// get Danny

        a.setLastRoll(10);//treat player as if they just rolled 10
        d.addAsset(board.tiles.get(12));//give utility to Danny
        ((Utility) board.tiles.get(12)).setOwner(d);
        a.setCurrentPos(12);
        board.tiles.get(12).activeEffect(a);//activate tile
        assertEquals(1460, a.getMoney());
        assertEquals(1540, d.getMoney());

        a.setLastRoll(6);//treat player as if they just rolled 10
        d.addAsset(board.tiles.get(28));//give utility to Danny
        ((Utility) board.tiles.get(28)).setOwner(d);
        a.setCurrentPos(28);
        board.tiles.get(28).activeEffect(a);//activate tile
        assertEquals(1400, a.getMoney());
        assertEquals(1600, d.getMoney());
    }

    /**
     * Jail serving time and using get out of jail card
     *
     * @throws IOException
     */
    @Test
    public void jailTest() throws IOException {
        jsonDataBoard();//read data in
        String userIn = "3";
        Player currentPlayer = board.turnOrder.getFirst();
        GetOutOfJail jailCard = ((GetOutOfJail) board.opportunityKnocks.getLast());
        jailCard.effect(currentPlayer);
        ByteArrayInputStream in = new ByteArrayInputStream(userIn.getBytes());
        System.setIn(in);//set user in 3 use card
        currentPlayer.jailPlayer();
        assertFalse(currentPlayer.isInJail());
        in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);//set user input 1 serve time
        currentPlayer.setCurrentPos(0);//move player to go
        currentPlayer.jailPlayer();
        //check player has been moved to jail
        assertEquals(10, currentPlayer.getCurrentPos());
        assertTrue(currentPlayer.isInJail());//check player is serving time
        board.tiles.get(10).activeEffect(currentPlayer);//serve time
        assertTrue(currentPlayer.isInJail());
        board.tiles.get(10).activeEffect(currentPlayer);//serve time
        assertTrue(currentPlayer.isInJail());
        board.tiles.get(10).activeEffect(currentPlayer);//serve time
        // check player has been released
        assertFalse(currentPlayer.isInJail());
    }

    /**
     * Housing repair card amounts check
     *
     * @throws IOException file error
     */
    @Test
    public void housingRepairsTest() throws IOException {
        jsonDataBoard();//read in data
        CardEffect house = null;
        while (!(house instanceof HousingRepairs)) {//get house repair card
            house = board.opportunityKnocks.removeFirst();
        }
        Player currentPlayer = board.turnOrder.getFirst();//get player
        currentPlayer.addAsset(board.tiles.get(1));
        ((Property) board.tiles.get(1)).setOwner(currentPlayer);
        currentPlayer.addAsset(board.tiles.get(3));
        ((Property) board.tiles.get(3)).setOwner(currentPlayer);
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(3)).purchaseHouse();
        (house).effect(currentPlayer);
        assertEquals(1320, currentPlayer.getMoney());
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHotel();
        assertEquals(1120, currentPlayer.getMoney());
        (house).effect(currentPlayer);
        assertEquals(965, currentPlayer.getMoney());
    }

    /**
     * Set updating
     *
     * @throws IOException
     */
    @Test
    public void setUpdateTest() throws IOException {
        jsonDataBoard();//read data
        Player firstPlayer = board.turnOrder.getFirst();//get player
        Player secondPlayer = board.turnOrder.get(1);//get second player
        firstPlayer.addAsset(board.tiles.get(1));
        ((Property) board.tiles.get(1)).setOwner(firstPlayer);//give player crapper street
        firstPlayer.completeSetProperties();
        assertFalse(((Property) board.tiles.get(1)).isCompletedSet());//check that set is in complete
        firstPlayer.addAsset(board.tiles.get(3));
        ((Property) board.tiles.get(3)).setOwner(firstPlayer);//give player gangsters paradise
        firstPlayer.completeSetProperties();
        assertTrue(((Property) board.tiles.get(1)).isCompletedSet());//check set is now complete
        ((Property) board.tiles.get(1)).sellProperty();//sell property
        assertNull(((Property) board.tiles.get(1)).getOwner());
        firstPlayer.completeSetProperties();
        assertFalse(((Property) board.tiles.get(3)).isCompletedSet());//check gangsters paradise is not set
    }

    /**
     * Test that player receives no rent when property is mortgaged and pays half the cost to un-mortgage it
     *
     * @throws IOException
     */
    @Test
    public void mortgagingTest() throws IOException {
        jsonDataBoard();//read data
        Player firstPlayer = board.turnOrder.getFirst();//get player
        Player secondPlayer = board.turnOrder.get(1);//get second player
        firstPlayer.addAsset(board.tiles.get(1));
        ((Property) board.tiles.get(1)).setOwner(firstPlayer);
        ((Property) board.tiles.get(1)).mortgageProperty();
        assertTrue(((Property) board.tiles.get(1)).isMortgaged());
        secondPlayer.setCurrentPos(1);//move player to crapper street
        board.tiles.get(1).activeEffect(secondPlayer);//activate tile effect
        assertEquals(1500, firstPlayer.getMoney());//check player received no rent
        assertEquals(1500, secondPlayer.getMoney());//check player had no money deducted

        ((Property) board.tiles.get(1)).unmortgageProperty();//un-mortgage pay £30
        assertFalse(((Property) board.tiles.get(1)).isMortgaged());
        board.tiles.get(1).activeEffect(secondPlayer);//activate tile effect
        assertEquals(1472, firstPlayer.getMoney());//check player received no rent
        assertEquals(1498, secondPlayer.getMoney());//check player had no money deducted
    }

    /**
     * Test of trade function, insures maintains complete set flags
     * current bug with ownership
     * Test to be complete after user input redesign
     *
     * @throws IOException file error
     */
    @Test
    public void tradeTest() throws IOException {
//        jsonDataBoard();//read data
//        String input = "yes" + System.getProperty("line.separator") + "Danny" + System.getProperty("line.separator");
//        Player firstPlayer = board.turnOrder.getFirst();//get player
//        Player secondPlayer = board.turnOrder.get(1);//get second player
//        ((Property)board.tiles.get(1)).setOwner(firstPlayer);
//        firstPlayer.addAsset(board.tiles.get(1));
//        ((Property)board.tiles.get(3)).setOwner(secondPlayer);
//        secondPlayer.addAsset(board.tiles.get(3));
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        board.trade(firstPlayer);
    }

    /**
     * Test of auction asset handling and logic
     * To be tested after user  input redesign
     *
     * @throws IOException
     */
    @Test
    public void auctionTest() throws IOException {
        jsonDataBoard();//read data
    }

    /**
     * Test assets are sold correctly when a player can't pay
     * Additional check that asset selling does not occur when the player does not have net worth to pay
     *
     * @throws IOException
     */
    @Test
    public void sellAssetsTest() throws IOException {
        jsonDataBoard();//read data
    }

    @Test
    public void buildingBuyingTest() throws IOException {
        jsonDataBoard();//read data
        Player firstPlayer = board.turnOrder.getFirst();//get player
        ((Property) board.tiles.get(1)).setOwner(firstPlayer);
        firstPlayer.addAsset(board.tiles.get(1));
        firstPlayer.completeSetProperties();
        ((Property) board.tiles.get(3)).setOwner(firstPlayer);
        firstPlayer.addAsset(board.tiles.get(3));
        firstPlayer.completeSetProperties();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHotel();
        assertEquals(0, ((Property) board.tiles.get(1)).getHousesNo());
        assertEquals(1, ((Property) board.tiles.get(1)).getHotelNo());
    }
}