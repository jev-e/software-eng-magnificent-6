package ClassStructure;


import GUI.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static Board board;
    private static HashMap<Integer, BoardTile> tileSet = new HashMap<>();
    private static LinkedList<Player> order = new LinkedList<>();
    private static Deque<CardEffect> pot = new ArrayDeque<>();
    private static Deque<CardEffect> opp = new ArrayDeque<>();

    /**
     * Game board data read in from data files
     *
     * @throws IOException file read in issue
     */
    public void jsonDataBoard() throws IOException {
        tileSet = Json.fromJsonToTileSet("BoardTileData.json");
        List<CardEffect> potPack = Json.fromJsonToList("PotLuckCardData.json");
        List<CardEffect> oppPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
        //Collections.shuffle(potPack);//shuffle decks
        //Collections.shuffle(oppPack);
        pot = new ArrayDeque<>(potPack);
        opp = new ArrayDeque<>(oppPack);
        board = new Board(order, tileSet, pot, opp, "full");
        for (Player p : order) {
            p.setBoard(board);//assign the board to the players
        }
    }

    /**
     * Setup for non full game data tests
     */
    @BeforeEach
    public void setup() {

        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND, null, false);
        Player danny = new Player("Danny", Token.CAT, null, false);
        Player jacob = new Player("Jacob", Token.BOOT, null, false);
        Player calvin = new Player("Calvin", Token.SMARTPHONE, null, false);
        Player callum = new Player("Callum", Token.SPOON, null, false);
        Player tom = new Player("Tom", Token.GOBLET, null, false);
        //load players into turn order
        order = new LinkedList<>();
        order.add(ayman);
        order.add(danny);
        order.add(jacob);
        order.add(calvin);
        order.add(callum);
        order.add(tom);

        //add default set of cards to decks
        Random rand = new Random();
        opp = new ArrayDeque<>();
        pot = new ArrayDeque<>();
        //construction of a test board
        BoardTile temp;

        for (int i = 0; i < 8; i++) {
            if (i == 5) {
                temp = new Property(i, "Example Street", Group.GREEN, 10, 10, null, 10, null);
            } else if (i == 7) {
                temp = new Property(i, "Test crescent", Group.DEEP_BLUE, 10, 10, null, 10, calvin);
            } else if (i == 0) {
                temp = new Go();
            } else {
                if (rand.nextInt(2) == 0) {
                    temp = new PotLuck(i);
                } else {
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
     * Tests the correct creation of the board, in particular testing member variables are
     * instantiated correctly
     */
    @Test
    public void boardCreationTest() {
        assertEquals(board.turnOrder, order);
        assertEquals(board.tiles, tileSet);
        assertEquals(board.potLuck, pot);
        assertEquals(board.opportunityKnocks, opp);
        assertEquals(board.getVersion(), "full");
    }

    /**
     * Tests an exception is thrown when an illegal version is given to the board constructor
     */
    @Test
    public void boardIllegalVersionTest() {
        Main guiTest = new Main();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Board(order, tileSet, pot, opp, "jhvjv",1, guiTest)
        );

        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> new Board(order, tileSet, pot, opp, "full", 1, guiTest)
        );

        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> new Board(order, tileSet, pot, opp, "abridged")
        );
    }

    @Test
    public void rollDiceTest() {
        //check return is within given range
        for( int ii = 0; ii < 100; ii++){
            Player p = board.turnOrder.getFirst();
            board.roll(p, 0);
            int roll = p.getLastRoll1() + p.getLastRoll2();
            assertTrue(roll >= 2 && roll <= 12);
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
        opp = new ArrayDeque<>();
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
        tileSet = new HashMap<>();
        pot.add(new PlayerPaysTax("Bob variable tax", 10));
        pot.add(new PlayerPaysTax("Bob variable tax", 10));
        pot.add(new PlayerPaysTax("Bob variable tax", 10));
        //construct tax test board
        tileSet.put(0, new Go());
        for (int i = 1; i < 7; i++) {
            tileSet.put(i, new PotLuck(i));//Add pot luck draw tiles
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
            if (!p.getName().equals("Ayman")) {//Sets all players money apart from Ayman's to £10
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
     * Test for asset list management insuring that owner is maintained
     * sell asset check
     *
     * @throws IOException file read error
     */
    @Test
    public void assetManagementTest() throws IOException {
        jsonDataBoard();
        Player player = board.turnOrder.peekFirst();
        Property crapper = (Property) tileSet.get(1);
        player.addAsset(crapper);
        assertEquals(crapper.getOwner(), player);
        player.removeAsset(crapper);
        assertNull(crapper.getOwner());
        player.addAsset(crapper);
        assertTrue(player.getAssets().contains(crapper));
        assertEquals(crapper.getOwner(), player);
        crapper.sellProperty();
        assertFalse(player.getAssets().contains(crapper));
        assertTrue(player.getAssets().size() != 1);
        assertNull(crapper.getOwner());
    }

    /**
     * Station rent amount checks
     *
     * @throws IOException file read error
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
     * @throws IOException file read error
     */
    @Test
    public void testUtility() throws IOException {
        jsonDataBoard();//read in data
        Player a = board.turnOrder.get(0);// get Ayman
        Player d = board.turnOrder.get(1);// get Danny

        a.setLastRoll1(5);//treat player as if they just rolled 10
        a.setLastRoll2(5);
        d.addAsset(board.tiles.get(12));//give utility to Danny
        ((Utility) board.tiles.get(12)).setOwner(d);
        a.setCurrentPos(12);
        board.tiles.get(12).activeEffect(a);//activate tile
        assertEquals(1460, a.getMoney());
        assertEquals(1540, d.getMoney());

        a.setLastRoll1(3);//treat player as if they just rolled 10
        a.setLastRoll2(3);
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
     * @throws IOException file read error
     */
    @Test
    public void jailTest() throws IOException {
        jsonDataBoard();//read data in
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setAiAgent(true);//Set player to be AI
        currentPlayer.getPersonality().setPatient(true);//AI will serve time if get out of jail card not available
        currentPlayer.getPersonality().setCautious(true);//AI will likely pay bail based on cautious judgement
        currentPlayer.jailPlayer();
        assertTrue(currentPlayer.isInJail());//Player should not be in jail as they are patient
        CardEffect jailCard = board.drawPotLuck();
        while (!(jailCard instanceof GetOutOfJail)) {//retrieve GetOutOfJail card from deck
            jailCard = board.drawPotLuck();
        }
        jailCard.effect(currentPlayer);//Player will get this card from the effect
        assertTrue(currentPlayer.getAssets().contains(jailCard));
        currentPlayer.jailPlayer();//player will use card
        assertFalse(currentPlayer.isInJail());//check player has left jail
        currentPlayer.getPersonality().setPatient(false);//player should pay bail as they have enough money
        int count = 0;
        int correct = 0;
        currentPlayer.setMoney(10000);//make sure player has ample funds for repeated trials
        while (count < 40) {//check the interaction multiple times due to probabilistic element
            if (!currentPlayer.isInJail()) {
                correct++;
            }
            count++;
        }
        assertTrue(correct > 38);//insure that expected outcome occurs the majority of the time
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
        ((Property) board.tiles.get(1)).purchaseHouse();//1 House
        ((Property) board.tiles.get(3)).purchaseHouse();
        (house).effect(currentPlayer);
        assertEquals(1320, currentPlayer.getMoney());
        ((Property) board.tiles.get(1)).purchaseHouse();//2 houses
        ((Property) board.tiles.get(3)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();//3 houses
        ((Property) board.tiles.get(3)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();//3 houses
        ((Property) board.tiles.get(3)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHotel();
        assertEquals(970, currentPlayer.getMoney());
        (house).effect(currentPlayer);
        assertEquals(695, currentPlayer.getMoney());// after 4 houses and 1 hotel housing charge
        ((Property) board.tiles.get(3)).purchaseHotel();
        (house).effect(currentPlayer);
        assertEquals(415, currentPlayer.getMoney());//after 2 hotels housing charge
    }

    /**
     * Set updating
     *
     * @throws IOException file read error
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
     * @throws IOException file read error
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
     * @throws IOException file error
     */
    @Test
    public void tradeTest() throws IOException {
        jsonDataBoard();//read data
        Player firstPlayer = board.turnOrder.get(0);
        Player secondPlayer = board.turnOrder.get(1);//get players
        firstPlayer.setAiAgent(true);//set player to be AI
        secondPlayer.setAiAgent(true);
        firstPlayer.getPersonality().setTrainAffinity(true);//likes trains
        firstPlayer.getPersonality().setTwoSetAffinity(false);//doesn't prefer brown and deep blue tiles
        firstPlayer.getPersonality().setPlanner(false);
        firstPlayer.getPersonality().setWildCard(false);
        secondPlayer.getPersonality().setTwoSetAffinity(true);//reverse of other player
        secondPlayer.getPersonality().setTrainAffinity(false);
        secondPlayer.getPersonality().setPlanner(false);
        secondPlayer.getPersonality().setWildCard(false);
        Property crapper = (Property) board.tiles.get(1);
        Station falmer = (Station) board.tiles.get(25);
        firstPlayer.addAsset(crapper);
        secondPlayer.addAsset(falmer);
        firstPlayer.initiateTrade();//this trade will be successful
        assertSame(falmer.getOwner(), firstPlayer);
        assertSame(crapper.getOwner(), secondPlayer);
        Property angel = (Property) board.tiles.get(6);
        Property potts = (Property) board.tiles.get(9);
        Property gangsters = (Property) board.tiles.get(3);
        firstPlayer.addAsset(angel);
        secondPlayer.addAsset(potts);
        firstPlayer.addAsset(gangsters);
        firstPlayer.completeSetProperties();
        assertEquals(gangsters.getOwner(), firstPlayer);
        assertFalse(gangsters.isCompletedSet());//gangsters and crapper owned by different players
        secondPlayer.initiateTrade();//second player offers potts for gangsters
        assertEquals(gangsters.getOwner(), secondPlayer);
        assertEquals(potts.getOwner(), firstPlayer);
        assertTrue(gangsters.isCompletedSet());//gangsters and crapper owned by same player after trade
    }


    /**
     * Insure that simple building buying of buildings functions
     * Handle building violations and do not buy houses when not valid
     *
     * @throws IOException file read error
     */
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
        assertEquals(firstPlayer.getMoney(), 1500);
        ((Property) board.tiles.get(1)).purchaseHouse();//buy house on crapper street
        ((Property) board.tiles.get(3)).purchaseHouse();//buy house on gangsters paradise
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(3)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(3)).purchaseHouse();
        ((Property) board.tiles.get(1)).purchaseHouse();
        ((Property) board.tiles.get(3)).purchaseHouse();
        assertFalse(((Property) board.tiles.get(3)).purchaseHouse());//check that the build was not successful
        ((Property) board.tiles.get(1)).purchaseHotel();//buy hotel on crapper street
        ((Property) board.tiles.get(3)).purchaseHotel();//buy hotel gangsters paradise
        assertFalse(((Property) board.tiles.get(3)).purchaseHotel());//check that the build was not successful
        assertEquals(0, ((Property) board.tiles.get(1)).getHousesNo());
        assertEquals(1, ((Property) board.tiles.get(1)).getHotelNo());
        assertEquals(firstPlayer.getMoney(), 1000);//money after paying 5 building charges on each property
    }

    /**
     * Insures selling obeys building regulations
     * @throws IOException file read error
     */
    @Test
    public void agentBuildingSellingTest() throws IOException {
        jsonDataBoard();
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setAiAgent(true);
        currentPlayer.getPersonality().setInvestor(true);
        currentPlayer.addAsset(tileSet.get(6));//weeping
        currentPlayer.addAsset(tileSet.get(8));//potts
        currentPlayer.addAsset(tileSet.get(9));//Nardole
        assertEquals(currentPlayer.getMoney(), 1500);
        currentPlayer.completeSetProperties();
        currentPlayer.agentDevelopProperties();
        assertEquals(currentPlayer.getMoney(), 750);//money after fully developing
        ((Property) tileSet.get(9)).sellHouseOrHotel();//hotel sells house count = 4
        ((Property) tileSet.get(9)).sellHouseOrHotel();//house sells house count = 3
        ((Property) tileSet.get(9)).sellHouseOrHotel();//Sale will fail as it would violate regulations
        ((Property) tileSet.get(9)).sellHouseOrHotel();//house count = 3
        assertTrue(((Property) tileSet.get(9)).isDeveloped());//will still be developed as last two sells will fail
        ((Property) tileSet.get(6)).sellHouseOrHotel();//hotel sell house count = 4
        ((Property) tileSet.get(8)).sellHouseOrHotel();//houses = 4
        ((Property) tileSet.get(9)).sellHouseOrHotel();//houses = 4
        //sell building on each tile
        ((Property) tileSet.get(6)).sellHouseOrHotel();//houses = 3
        ((Property) tileSet.get(8)).sellHouseOrHotel();//houses = 3
        ((Property) tileSet.get(9)).sellHouseOrHotel();//houses = 2
        //sell building on each tile
        ((Property) tileSet.get(6)).sellHouseOrHotel();//houses = 2
        ((Property) tileSet.get(8)).sellHouseOrHotel();//houses = 2
        ((Property) tileSet.get(9)).sellHouseOrHotel();//houses = 1
        //sell building on each tile
        ((Property) tileSet.get(6)).sellHouseOrHotel();//houses = 1
        ((Property) tileSet.get(8)).sellHouseOrHotel();//houses = 1
        ((Property) tileSet.get(9)).sellHouseOrHotel();//houses = 0
        //sell remaining 2 buildings
        ((Property) tileSet.get(6)).sellHouseOrHotel();//houses = 0
        ((Property) tileSet.get(8)).sellHouseOrHotel();//houses = 0
        assertFalse(((Property) tileSet.get(6)).isDeveloped());//Check developed is maintained
        assertFalse(((Property) tileSet.get(8)).isDeveloped());
        assertFalse(((Property) tileSet.get(9)).isDeveloped());
    }

    /**
     * Simple check of agent ability to purchase
     * @throws IOException file read error
     */
    @Test
    public void agentPurchaseTest() throws IOException {
        jsonDataBoard();
        Player player = board.turnOrder.peekFirst();
        player.setAiAgent(true);//player is now AI
        player.setCurrentPos(39);
        player.setCurrentPos(1);
        player.passGo();//check to switch canPurchase true
        player.getPersonality().setTwoSetAffinity(true);//give player two set affinity will buy Brown tiles if possible
        Property crapper = (Property) board.tiles.get(1);//brown tile
        crapper.activeEffect(player);//AI will buy tile
        assertSame(crapper.getOwner(), player);
        assertTrue(player.getAssets().contains(crapper));
        for (Object item : player.getAssets()) {
            if (item instanceof Property) {
                System.out.println(((Property) item).title);
            } else if (item instanceof Station) {
                System.out.println(((Station) item).title);
            } else if (item instanceof Utility) {
                System.out.println(((Utility) item).title);
            } else {
                System.out.println("Card");
            }
        }
    }

    /**
     * Test of cautious buying strategy
     * Runs 100 times and asserts the expected outcome occurs 80% or more of the time
     * may fail in very rare cases due to extreme chance
     * @throws IOException file read error
     */
    @Test
    public void cautiousPurchaseTest() throws IOException {
        jsonDataBoard();
        Player player = board.turnOrder.peekFirst();
        player.setAiAgent(true);//player is not AI
        player.setCurrentPos(39);
        player.setCurrentPos(1);
        player.passGo();//check to switch canPurchase true
        player.getPersonality().setCautious(true);
        player.getPersonality().setPlanner(false);
        player.getPersonality().setTwoSetAffinity(false);
        Property crapper = (Property) board.tiles.get(1);//brown tile
        crapper.activeEffect(player);//AI will buy tile
        assertSame(crapper.getOwner(), player);
        assertTrue(player.getAssets().contains(crapper));
        Property gangsters = (Property) board.tiles.get(3);
        int count = 0;
        int success = 0;
        while (count < 100) {
            player.removeAsset(gangsters);
            player.setMoney(70);
            gangsters.activeEffect(player);
            if (!(player.getAssets().contains(gangsters))) {
                success++;
            }
            count++;
        }
        double result = success / 100.0;
        System.out.println("Success rate:" + result + "%");
        assertTrue(result >= 0.8);
    }

    /**
     * basic asset selling
     *
     * @throws IOException file read error
     */
    @Test
    public void agentAssetSelling() throws IOException {
        jsonDataBoard();
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setAiAgent(true);
        currentPlayer.getPersonality().setInvestor(true);
        currentPlayer.setMoney(1000);
        currentPlayer.addAsset(tileSet.get(1));//give crapper street
        currentPlayer.addAsset(tileSet.get(3));
        currentPlayer.addAsset(tileSet.get(31));
        currentPlayer.addAsset(tileSet.get(5));
        currentPlayer.addAsset(tileSet.get(6));
        currentPlayer.addAsset(tileSet.get(8));
        currentPlayer.addAsset(tileSet.get(9));
        currentPlayer.addAsset(new GetOutOfJail());//check card causes no issues
        currentPlayer.completeSetProperties();
        currentPlayer.agentDevelopProperties();
        System.out.println("Net worth: £" + currentPlayer.netWorth());
        currentPlayer.deductAmount(1800);//trigger asset selling
        for (Object item : currentPlayer.getAssets()) {
            if (item instanceof Property) {
                assertFalse(((Property) item).isDeveloped());
                assertFalse(((Property) item).isCompletedSet());
            }
        }
    }

    /**
     * Testing the case where agent has multiple developed properties
     *
     * @throws IOException file read error
     */
    @Test
    public void assetSellingManyDeveloped() throws IOException {
        jsonDataBoard();
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setMoney(2300);
        currentPlayer.setAiAgent(true);
        currentPlayer.getPersonality().setInvestor(true);
        currentPlayer.addAsset(tileSet.get(1));//crapper
        currentPlayer.addAsset(tileSet.get(6));//weeping
        currentPlayer.addAsset(tileSet.get(8));//potts
        currentPlayer.addAsset(tileSet.get(9));//Nardole
        currentPlayer.addAsset(tileSet.get(16));//cooper
        currentPlayer.addAsset(tileSet.get(18));//wolowitz
        currentPlayer.addAsset(tileSet.get(19));//penny
        currentPlayer.completeSetProperties();
        currentPlayer.agentDevelopProperties();
        System.out.println(currentPlayer.netWorth());
        currentPlayer.deductAmount(3100);
    }

    /**
     * Tests that when an abridged game version is selected that a timer is created, runs and triggers the correct
     * event
     */
    @Test
    public void abridgedTest() throws InterruptedException {
        Main guiTest = new Main();
        board = new Board(order, tileSet, pot, opp, "abridged", 1, guiTest);
        board.startGameTimer();
        assertNotEquals(board.timer, null);
        TimeUnit.MINUTES.sleep(2);
        assertTrue(board.timeUp);
    }

    /**
     * Test that action log correctly stores players actions
     * Maintain that the log is cleared when accessed
     *
     * @throws IOException json file read error
     */
    @Test
    public void actionLogTest() throws IOException {
        jsonDataBoard();
        String expectedString;
        Player currentPlayer = board.turnOrder.getFirst();
        currentPlayer.setAiAgent(true);//set player to be AI
        currentPlayer.getPersonality().setWildCard(false);
        currentPlayer.getPersonality().setTrainAffinity(true);//will buy stations
        currentPlayer.getPersonality().setTwoSetAffinity(true);//will buy Brown and Deep Blue tiles
        board.roll(currentPlayer, 0);
        System.out.println(currentPlayer.getActionLog());//visual test
        assertEquals(currentPlayer.getActionLog(), "");//check that action log is cleared
        Station hove = (Station) board.tiles.get(15);
        hove.activeEffect(currentPlayer);//player should buy station
        expectedString = "Landed on Hove Station\n" +
                "Purchased Hove Station for £200\n";
        assertEquals(expectedString, currentPlayer.getActionLog());
        BoardTile potLuck = board.tiles.get(2);
        potLuck.activeEffect(currentPlayer);//draw potluck card first card bank pays player
        expectedString = "Draw Pot Luck card\n" +
                "You inherit £100\n";
        assertEquals(expectedString, currentPlayer.getActionLog());
        currentPlayer.setMoney(100);//set player money low in oder to bankrupt
        BoardTile tax = board.tiles.get(4);//super tax
        tax.activeEffect(currentPlayer);//charge player 200 station will be sold
        //AI has 100 but is charged 200, 100 will be raised
        tax.activeEffect(currentPlayer);//charge another 200 AI can't pay
        expectedString = "You pay £200 Income tax\n" +
                "Sell assets to raise £100\n" +
                "You pay £200 Income tax\n" +
                "Bankrupt\n";
        assertEquals(expectedString, currentPlayer.getActionLog());
    }
}