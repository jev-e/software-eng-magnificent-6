package ClassStructure;

import GUI.Controller;
import javafx.util.Pair;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;

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

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    private Instant start;
    private Instant finished;
    public long timeElapsed;
    private CountDownLatch latch = new CountDownLatch(1);


    /**
     * Board constructor that fetches version choice from user input
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

        dataStore = new HashMap<>();



        //fetch version choice from user
        this.version = chooseVersion();
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

        if( version.equals( "abridged")){
            timer = new Timer();
            TimerTask endGame = new TimerTask() {
                @Override
                public void run() {
                    timeUp = true;
                }
            };
            timer.schedule(endGame, (timeLimit * 60) * 1000);
        }
    }

    /**
     * Board constructor with specified version
     * @param turnOrder linked list of players in their turn order
     * @param tiles board representation as a hash map
     * @param potLuck deque of pot luck cards
     * @param opportunityKnocks deque of opportunity knocks cards
     * @param version one of "full" or "abridged"
     */
    public Board(LinkedList<Player> turnOrder, HashMap<Integer,BoardTile> tiles, Deque<CardEffect> potLuck, Deque<CardEffect> opportunityKnocks, String version) {

        if( version.equals("full") || version.equals("abridged") ){
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

        if( version.equals("abridged")){
            timer = new Timer();
            TimerTask endGame = new TimerTask() {
                @Override
                public void run() {
                    timeUp = true;
                }
            };
            timer.schedule(endGame, (timeLimit * 60) * 1000);
        }
    }



    /**
     * Asks user to choose between abridged and full version
     * for text version and debugging only
     *
     * @return decision one of "full" or "abridged"
     */
    private String chooseVersion() {
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        boolean valid = false;
        String  decision = null;
        int timeLimit = 0;

        while(!valid){
            System.out.println("Abridged or full version?");
            decision = userInputScanner.nextLine();
            decision = decision.toLowerCase();
            if( decision.equals("abridged") ){
                System.out.println("Please give a time limit in whole minutes:");
                timeLimit = userInputScanner.nextInt();
                this.timeLimit = timeLimit;
                valid = true;
            } else if (decision.equals("full")){
                valid = true;
            } else {
                System.out.println("Please pick one of full or abridged");
            }
        }

        return decision;
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
        if(!(card instanceof GetOutOfJail)) {//if card is not get out of jail free place it at the bottom of the deque
            opportunityKnocks.addLast(card);
        }
        return card;
    }
    /**
     * rolls two die and returns the number of places to move
     * @return int combination of die results
     */
    public int roll(Player currentPlayer, int count) {
        int result = 0;
        Random rand = new Random();
        int die1 = rand.nextInt(6)+1;
        int die2 = rand.nextInt(6)+1;
        if (die1 == die2) {
            repeat = true;//tracks double rolled for player to go again
            if (count >= 3) {
                result = die1 + die2;
                repeat = false;
                currentPlayer.jailPlayer();//sets players status to jailed
            }
        }
        if (count < 3) {
            result = die1 + die2;
            int position = (currentPlayer.getCurrentPos() + result) % tiles.size();
            currentPlayer.setCurrentPos(position);
        }
        return result;
    }

    public void unblockCall(){
        latch.countDown();
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
        //TODO GUI Results screen here and remove prints
        System.out.println(winner.getName() + " has won the game with a net worth of " + winner.netWorth());
        storeData( winner, winner.netWorth() );
        for( String key: dataStore.keySet() ){
            System.out.println(dataStore.get(key).size() + " " + key + " " + dataStore.get(key).toString());
        }



    }

    /**
     * Asks all players if it is okay for one player to leave the game
     *
     * @param leavingPlayer the player who wants to leave
     * @return boolean true if all players agree to the leaving player leaving, false other
     */
    public boolean getLeaveVotes( Player leavingPlayer ) {

        boolean confirm;
        boolean canLeave = true;

        for (Player player : turnOrder) {
            if (player != leavingPlayer && !player.isAiAgent()) {//ignore AI agents
                confirm = true;//TODO change to get user decision from GUI
                if (!confirm) { //votes must be unanimous so if one disagrees, player can't leave
                    canLeave = false;
                    break;
                }
            }
        }

        return canLeave;
    }


    public void storeData( Player p, int netWorth ){
        Pair pair = new Pair( turns, netWorth);
        dataStore.get(p.getName()).add(pair);
    }
}