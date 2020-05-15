package ClassStructure;

import GUI.Main;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.util.Pair;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author Ayman Bensreti, Calvin Boreham
 *	Game board and logic
 */
public class Board {
    Deque<CardEffect> potLuck;
    Deque<CardEffect> opportunityKnocks;
    public int turns;
    public LinkedList<Player> turnOrder;
    public HashMap<Integer,BoardTile> tiles;// key: iD value: (Property|Effect)
    int taxPot; // subject to change see free parking in spec
    public boolean repeat;
    private String version;
    private int timeLimit = 5;
    Timer timer;
    public boolean timeUp = false;
    public HashMap<String, ArrayList<Pair>> dataStore; //key : playerName value: networths at each turn in game

    private Instant start;
    private Instant finished;
    public long timeElapsed;
    @JsonIgnore
    private Main guiMain;

    /**
     * Board constructor with specified version
     * @param turnOrder linked list of players in their turn order
     * @param tiles board representation as a hash map
     * @param potLuck deque of pot luck cards
     * @param opportunityKnocks deque of opportunity knocks cards
     * @param version one of "full" or "abridged"
     */
    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks, String version, Main guiMain) {

        if( version.equals("full") ){
            this.version = version;
        } else {
            System.out.println("Error in version");
            throw new IllegalArgumentException();
        }

        this.potLuck = potLuck;
        this.opportunityKnocks = opportunityKnocks;
        this.turnOrder = turnOrder;
        this.tiles = tiles;
        turns = 0;
        taxPot = 0;
        repeat =  false;
        dataStore = new HashMap<>();
        this.guiMain = guiMain;
        //Set board references for activation methods in tiles and cards
        for (CardEffect c : potLuck) {
            c.setBoard(this);
        }
        for (CardEffect c : opportunityKnocks) {
            c.setBoard(this);
        }
        for (BoardTile b : tiles.values()) {
            b.setBoard(this);
        }

    }

    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks, String version) {

        if( version.equals("full") ){
            this.version = version;
        } else {
            System.out.println("Error in version");
            throw new IllegalArgumentException();
        }

        this.potLuck = potLuck;
        this.opportunityKnocks = opportunityKnocks;
        this.turnOrder = turnOrder;
        this.tiles = tiles;
        turns = 0;
        taxPot = 0;
        repeat =  false;
        dataStore = new HashMap<>();
        //Set board references for activation methods in tiles and cards
        for (CardEffect c : potLuck) {
            c.setBoard(this);
        }
        for (CardEffect c : opportunityKnocks) {
            c.setBoard(this);
        }
        for (BoardTile b : tiles.values()) {
            b.setBoard(this);
        }
    }

    /**
     * Board constructor with specified version
     * @param turnOrder linked list of players in their turn order
     * @param tiles board representation as a hash map
     * @param potLuck deque of pot luck cards
     * @param opportunityKnocks deque of opportunity knocks cards
     * @param version one of "full" or "abridged"
     * @param guiMain instance of the GUI
     */
    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks, String version, int timeLimit, Main guiMain) {

        if( version.equals("abridged") ){
            this.version = version;
        } else {
            System.out.println("Error in version");
            throw new IllegalArgumentException();
        }

        this.timeLimit = timeLimit;
        this.potLuck = potLuck;
        this.opportunityKnocks = opportunityKnocks;
        this.turnOrder = turnOrder;
        this.tiles = tiles;
        turns = 0;
        taxPot = 0;
        repeat =  false;
        dataStore = new HashMap<>();
        this.guiMain = guiMain;
        //Set board references for activation methods in tiles and cards
        for (CardEffect c : potLuck) {
            c.setBoard(this);
        }
        for (CardEffect c : opportunityKnocks) {
            c.setBoard(this);
        }
        for (BoardTile b : tiles.values()) {
            b.setBoard(this);
        }
    }

    /**
     * Board constructor with specified version
     * @param turnOrder linked list of players in their turn order
     * @param tiles board representation as a hash map
     * @param potLuck deque of pot luck cards
     * @param opportunityKnocks deque of opportunity knocks cards
     * @param version one of "full" or "abridged"
     * @param timeLimit time limit
     */
    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks, String version, int timeLimit) {

        if( version.equals("abridged") ){
            this.version = version;
        } else {
            System.out.println("Error in version");
            throw new IllegalArgumentException();
        }

        this.timeLimit = timeLimit;
        this.potLuck = potLuck;
        this.opportunityKnocks = opportunityKnocks;
        this.turnOrder = turnOrder;
        this.tiles = tiles;
        turns = 0;
        taxPot = 0;
        repeat =  false;
        dataStore = new HashMap<>();
        this.guiMain = guiMain;
        //Set board references for activation methods in tiles and cards
        for (CardEffect c : potLuck) {
            c.setBoard(this);
        }
        for (CardEffect c : opportunityKnocks) {
            c.setBoard(this);
        }
        for (BoardTile b : tiles.values()) {
            b.setBoard(this);
        }
    }

    /***
     * Call the front-end function to create the Scene for jail decision
     * @param currentPlayer The current player
     * @param jailCard A parameter to see if they have a get out of jail card they can activate
     */
    public void callJailSetupScene(Player currentPlayer, GetOutOfJail jailCard){
        guiMain.sentToJailSetupScene(currentPlayer, jailCard);
    }

    /***
     * Call the front-end function to create the Scene for auctioning
     * @param currentPlayer the current player
     * @param asset The asset (station, property, utility) that is up for auction
     */
    public void callAuctionSetupScene(Player currentPlayer, BoardTile asset){
        guiMain.auctionPlayerSetup(currentPlayer, asset);
    }

    /***
     * Call the front-end function to create the Scene for asset selling Management
     * @param currentPlayer the current player
     * @param fundNeeded The amount that the current player has to rise to
     */
    public void callAssetSellingScene(Player currentPlayer, int fundNeeded){
        guiMain.assetSellingManagementSetupScene(currentPlayer, fundNeeded);
    }
    /**
     * Middle man function to grab player's decision to leave ethe game
     * @param currentPlayer the player asking to leave the game
     * @returnplayer's decision to leave the game or not
     */
    public boolean leaveConfirmation(Player currentPlayer){
        // TODO add merge
        return(guiMain.leaveConfirmationAlert(currentPlayer));
    }

    /**
     * Starts the game timer, must be called before starting main game loop
     */
    public void startGameTimer(){
        if( version == "abridged"){
            timer = new Timer();
            TimerTask endGame = new TimerTask() {
                @Override
                public void run() {
                    timeUp = true; System.out.println("timeUp" + timeUp);
                }
            };
            timer.schedule(endGame, (timeLimit * 60) * 1000);
        }
        start = Instant.now();
    }

    /**
     * draw pot luck card from the top of the deque
     * @return Effect
     */
    public CardEffect drawPotLuck() {
        CardEffect card = potLuck.removeFirst();
        if(!(card instanceof GetOutOfJail)) {//if card is not get out of jail free place it at the bottom of the deque
            potLuck.addLast(card);
        }
        return card;
    }

    /**
     * draw opportunity knocks card from the top of the deque
     * @return Effect
     */
    public  CardEffect drawOpportunityKnocks() {
        CardEffect card = opportunityKnocks.removeFirst();
        if (!(card instanceof GetOutOfJail)) {//if card is not get out of jail free place it at the bottom of the deque
            opportunityKnocks.addLast(card);
        }
        return card;
    }

    /**
     * rolls two die and moves player to new position
     * die 1 and 2 result stored in player
     */
    public void roll(Player currentPlayer, int count) {
        System.out.println("Roll Function Called " + currentPlayer.getName());
        int result;
        Random rand = new Random();
        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        currentPlayer.setLastRoll1(die1);//maintain last roll ints
        currentPlayer.setLastRoll2(die2);
        if (currentPlayer.isAiAgent()) {//if AI record roll in action log
            currentPlayer.addAction("Rolled |" + die1 + "||" + die2 + "|");
        }
        currentPlayer.passGo();
        if (die1 == die2) {
            repeat = true;//tracks double rolled for player to go again
            if (count >= 3) {
                repeat = false;
                currentPlayer.jailPlayer();//sets players status to jailed
            }
        }

        if (count < 3) {
            result = die1 + die2;
            int position = (currentPlayer.getCurrentPos() + result) % tiles.size();
            currentPlayer.setCurrentPos(position);
        }
    }

    /**
     * Processes the ending of the game and declares the winner
     *
     * @throws Exception illegal game end
     */
    public void gameOver() throws Exception {

        finished = Instant.now();
        timeElapsed = Duration.between(start, finished).toMillis();
        Player winner = null;

        if (version.equals("abridged") && timeUp) { //time limit up on abridged game
            int maxNetWorth = 0;
            for (Player player : turnOrder) {
                if (player.netWorth() > maxNetWorth) {
                    maxNetWorth = player.netWorth(); //find player with max net worth
                    winner = player;
                }
            }


        } else if (version.equals("full") && turnOrder.size() == 1) { //full game end
            //System.out.println("Game over");
            winner = turnOrder.peekFirst();
        } else if (version.equals("abridged") && turnOrder.size() == 1) { //abridged game ended like a full game
            //System.out.println("Game over before time limit reached");
            winner = turnOrder.peekFirst();
        } else { //no winner
            throw new Exception("illegal end game state");
        }

        assert winner != null;
        guiMain.winnerSetupScene(winner);
        storeData( winner, winner.netWorth() );
    }

    /**
     * Asks all players if it is okay for one player to leave the game
     * @param leavingPlayer the player who wants to leave
     * @return boolean true if all players agree to the leaving player leaving, false other
     */
    public boolean getLeaveVotes( Player leavingPlayer ) {

        boolean confirm;
        boolean canLeave = true;

        for (Player player : turnOrder) {
            if (player != leavingPlayer && !player.isAiAgent()) {//ignore AI agents
                confirm = guiMain.getLeavePermission(player, leavingPlayer);
                if (!confirm) { //votes must be unanimous so if one disagrees, player can't leave
                    canLeave = false;
                    break;
                }
            }
        }
        return canLeave;
    }

    /**
     * Stores data for a player in the board data store
     * data stored is pair (turns, networth)
     * @param p player
     * @param netWorth current networth of player
     */
    public void storeData( Player p, int netWorth ){
        Pair pair = new Pair( turns, netWorth);
        dataStore.get(p.getName()).add(pair);
    }

    /**
     * Getter for finish time
     * return time game finished
     */
    public Instant getFinished() {
        return finished;
    }

    /**
     * Getter for time elapsed
     * @return time taken for game to be played
     */
    public Long getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Getter for start time
     * @return time game started
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Setter for start
     * @param start
     */
    public void setStart(Instant start) {
        this.start = start;
    }

    /**
     * Getter for game version
     * @return version of game, either abridged or full
     */
    public String getVersion() {
        return version;
    }

    // -- "Middle Man" Functions --

    /**
     * Middle man function to get current player's decision on whether he wants to buy an asset or not
     * @param title The name of the asset
     * @param cost How much the asset cost to buy
     * @return Current player's decision to buy purchase or not
     */
    public boolean getPurchase(String title, String cost) {
        return (guiMain.assetBuyingScene(title,cost));
    }

    /**
     * Middle man function to grab player's decision on whether to pay tax or draw opportunity knock card
     * @return player's decision
     */
    public boolean payTaxOrDrawOpKnock(){
        return(guiMain.taxOrDrawScreen());
    }

    /**
     * Called by Player in .setCurrentPos() to update the player's token position
     *
     * @param currentPlayer current player who's token needs to be moved
     */
    public void movePlayerToken(Player currentPlayer) {
        guiMain.displayTokens(currentPlayer);
    }

    /**
     * Called by Player's add and remove assset functions to update on screen assets owned
     *
     * @param currentPlayer current player who's assets need to be update
     * @param item The asset that has been added or removed
     * @param use The case in which being called, "add" or "remove"
     */
    public void updatePlayerAssets(Player currentPlayer, Object item, String use) {
        guiMain.displayPlayerAssets(currentPlayer, item, use);
    }
}