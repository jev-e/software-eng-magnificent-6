package ClassStructure;

import GUI.Controller;
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
    int turns;
    LinkedList<Player> turnOrder;
    HashMap<Integer,BoardTile> tiles;// key: iD value: (Property|Effect)
    int taxPot; // subject to change see free parking in spec
    boolean repeat;
    private String version;
    private int timeLimit = 5;
    Timer timer;
    boolean timeUp = false;
    HashMap<String, ArrayList<Pair>> dataStore; //key : playerName value: networths at each turn in game
    private Instant start;
    private Instant finished;
    public long timeElapsed;


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

    public Board() {

    }

    /**
     * Asks user to choose between abridged and full version
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
     * Displays current state of the board in text format
     * Rough version for prototyping
     */
    public void displayAsString() {
        int n = tiles.size();
        int padding = (n-4)/4;
        int row = padding + 2;
        int spacerAmount = 20;
        int currentTile = 0;
        int leftSide = n-1;
        int rightSide = row;
        String title;

        printContainer(spacerAmount, row);
        //print first row
        for(int i = currentTile; i < row;i++) {
            title = tiles.get(i).title;
            System.out.printf("|%s%s|", title,repeat(" ",spacerAmount - title.length()));
        }
        printContainer(spacerAmount, row);
        //If tile is property print it's group otherwise print a blank line
        String group;
        for(int i = 0; i < row;i++) {
            group = tileGroup(i);
            System.out.printf("|%s%s|", group, repeat(" ",spacerAmount - group.length()));
        }
        printContainerS(spacerAmount, row);
        //print the token symbols of the tile
        String onTile;
        for(int i = 0; i < row;i++) {
            onTile = playersOnTile(i);
            System.out.printf("|%s%s|", onTile, repeat(" ", spacerAmount - onTile.length()));
        }
        printContainer(spacerAmount, row);
        //Print the middle section tiles
        for(int i = 0; i < padding; i++) {
            //print title
            title = tiles.get(leftSide).title;
            String title2 = tiles.get(rightSide).title;
            System.out.printf("|%s%s||%s",title, repeat(" ",spacerAmount - title.length()),repeat(" ",(spacerAmount+2)*(padding)-2));
            System.out.printf("||%s%s|",title2,repeat(" ",spacerAmount - title2.length()));
            printMiddleContainer(spacerAmount,row);
            //print group if property
            group = tileGroup(leftSide);
            String group2 = tileGroup(rightSide);
            System.out.printf("|%s%s||%s",group, repeat(" ",spacerAmount - group.length()),repeat(" ",(spacerAmount+2)*(padding)-2));
            System.out.printf("||%s%s|",group2,repeat(" ",spacerAmount - group2.length()));
            printMiddleContainerS(spacerAmount,row);
            //print tokens on tile
            onTile = playersOnTile(leftSide);
            String onTile2 = playersOnTile(rightSide);
            System.out.printf("|%s%s||%s",onTile, repeat(" ",spacerAmount - onTile.length()),repeat(" ",(spacerAmount+2)*(padding)-2));
            System.out.printf("||%s%s|",onTile2,repeat(" ",spacerAmount - onTile2.length()));

            if(i != padding - 1) {
                printMiddleContainer(spacerAmount,row);
            }
            leftSide--;//keep track of tiles printed
            rightSide++;
        }
        printContainer(spacerAmount, row);
        int finalRowStart = leftSide;//store final row start point
        currentTile = finalRowStart;
        //print final row
        for(int i = 0; i < row;i++) {
            title = tiles.get(currentTile).title;//print title of tile
            System.out.printf("|%s%s|", title,repeat(" ",spacerAmount - title.length()));
            currentTile--;//increment to move to next tile
        }
        printContainer(spacerAmount, row);
        currentTile = finalRowStart;//reset to the row start
        for(int i = 0; i < row;i++) {
            group = tileGroup(currentTile);//print group if property
            System.out.printf("|%s%s|", group,repeat(" ",spacerAmount - group.length()));
            currentTile--;//increment to move to next tile
        }

        printContainerS(spacerAmount, row);
        currentTile = finalRowStart;//reset to the row start
        for(int i = 0; i < row;i++) {
            onTile = playersOnTile(currentTile);//print tokens on tile
            System.out.printf("|%s%s|", onTile,repeat(" ",spacerAmount - onTile.length()));
            currentTile--;//increment to move to next tile
        }
        printContainer(spacerAmount, row);
    }

    /**
     * Prints tile container with - between | characters for complete row
     * @param size width of tiles in text
     * @param count number of tiles
     */
    public void printContainer(int size, int count) {
        String dash = "-";
        String container = "|"+repeat(dash,size)+"|";
        System.out.printf("\n%s\n",repeat(container,count));
    }

    /**
     * Version of printContainer with spaces in place of -
     * @param size width of tiles in text
     * @param count number of tiles
     */
    public void printContainerS(int size, int count) {
        String space = " ";
        String container = "|"+repeat(" ",size)+"|";
        System.out.printf("\n%s\n",repeat(container,count));
    }

    /**
     * Container for middle section of the board
     * @param size width of tiles in text
     * @param count number of tiles
     */
    public void printMiddleContainer(int size, int count) {
        String dash = "-";
        String container = "|"+repeat("-",size)+"|";
        int spaceAmount = container.length();
        System.out.printf("\n%s|%s|%s\n",container,repeat(" ",((spaceAmount*(count-2))-2)),container);
    }

    /**
     * Version of printMiddleContainer with spaces in place of -
     * @param size width of tiles in text
     * @param count number of tiles
     */
    public void printMiddleContainerS(int size, int count) {
        String container = "|"+repeat(" ",size)+"|";
        int spaceAmount = container.length();
        System.out.printf("\n%s|%s|%s\n",container,repeat(" ",((spaceAmount*(count-2))-2)),container);
    }

    /**
     * For a given tile on the board return token symbols corresponding to the players on that tile
     * @param tile target tile
     * @return String of player symbols empty string when no players on tile
     */
    public String playersOnTile(int tile) {
        String players = "";
        for(Player  player : turnOrder) {//fetch players on current tile
            if(player.getCurrentPos() == tile) {
                players += (player.getToken().getSymbol() + " ");
            }
        }
        return players;
    }

    /**
     * If a tile is a property return it's group otherwise return empty string
     * @param tile target tile
     * @return String empty or one of property groups
     */
    public String tileGroup(int tile) {
        String group = " ";
        BoardTile currentTile = tiles.get(tile);
        if(currentTile instanceof Property) {
            group = ((Property) currentTile).getGroup().toString();
        }
        return group;
    }

    /**
     * Repeat a string by the amount of times provided
     * @param s string to be repeated
     * @param amount number of times to repeat input string
     * @return string of length s.length()*amount
     */
    public String repeat(String s, int amount) {
        String out = "";
        for(int i = 0; i < amount; i++) {
            out += s;
        }
        return out;
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
                System.out.printf("%s:%s Has Gone To Jail\n", currentPlayer.getName(), currentPlayer.getToken().getSymbol());
                currentPlayer.jailPlayer();//sets players status to jailed
            }
        }
        if (count < 3) {
            result = die1 + die2;
            int position = (currentPlayer.getCurrentPos() + result) % tiles.size();
            System.out.printf("%s:%s Rolled |%d|%d| = %d\n", currentPlayer.getName(), currentPlayer.getToken().getSymbol(), die1, die2, result);
            currentPlayer.setCurrentPos(position);
        }
        return result;
    }

    public void testLoop(Controller controller) {
        int retirePoint = 300;
        start = Instant.now();
        displayAsString();
        Collections.shuffle(turnOrder);
        for( Player p: turnOrder ){
            dataStore.put(p.getName(), new ArrayList<>());
        }
        turns = 0;
        while (turnOrder.size() > 1) {
            turns++;
            for (int i = 0; i < turnOrder.size(); i++) {
                Player currentPlayer = turnOrder.get(i);
                if (currentPlayer == null) {
                    continue;//skip players that have been removed from turn order
                } else if (currentPlayer.isInJail()) {
                    tiles.get(10).activeEffect(currentPlayer);//Activate the jail tile to serve time
                    storeData(currentPlayer, currentPlayer.netWorth());
                    continue;//move to next turn
                }
                int count = 0;

                do {
                    count++;
                    repeat = false;
                    if (!currentPlayer.isAiAgent()) {//get input from player
                        try {//TODO remove this for GUI loop
                            controller.grabRollInput();
                        } catch (Exception e) {
                            System.out.println("Error in press next");
                            e.printStackTrace();
                        }
                    }
                    currentPlayer.setLastRoll(roll(currentPlayer, count));//keep track of player roll
                    currentPlayer.passGo();
                    tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);
                    displayAsString();

                    if (turnOrder.contains(currentPlayer) && !currentPlayer.isInJail()) {
                        if (!currentPlayer.isAiAgent()) {
                            currentPlayer.leaveGame();
                            currentPlayer.unMortgage();
                            trade(currentPlayer);

                        } else {
                            currentPlayer.initiateTrade();
                            if (turns > retirePoint && turnOrder.size() > 4) {
                                retirePoint += 100;
                                currentPlayer.agentRetire();
                            }
                        }
                        currentPlayer.developProperties();
                        if (!repeat) {
                            storeData(currentPlayer, currentPlayer.netWorth());
                        }
                    }

                } while (repeat);
            }
            if (timeUp) {
                System.out.println("Time limit reached");
                System.out.println(version);
                break;
            }
        }
        try {
            gameOver();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    /**
     * Game loop
     */
    public void gameLoop() throws Exception {
        displayAsString();
        //Player pool for use in end game screen to be removed
        Collections.shuffle(turnOrder);//randomises the player turn order
        LinkedList<Player> playerPool = (LinkedList<Player>) turnOrder.clone();

        while (turnOrder.size() != 1) {
            for (Player p : turnOrder) {
                System.out.print(p.getName() + ":" + p.getMoney() + " ");
            }
            System.out.println();
            for (int ii = 0; ii < turnOrder.size(); ii++) {
                Player p = turnOrder.get(ii);
                if (!p.isInJail()) {
                    int count = 0;
                    do {
                        count++;//keep track of number of repeat turns player has had
                        repeat = false;
                        System.out.println("Press Enter to continue");
                        if (!p.isAiAgent()) {//get input from player
                            try {
                                System.in.read();
                            } catch (Exception e) {
                                System.out.println("Error in press next");
                                e.printStackTrace();
                            }
                        }
                        p.setLastRoll(roll(p, count));//keep track of player roll
                        //displayAsString();
                        p.passGo();
                        tiles.get(p.getCurrentPos()).activeEffect(p);
                        if (!turnOrder.contains(p)) {
                            System.out.println("Player not in turn order");
                            break;//end turn if player has been bankrupt
                        }
                        displayAsString();
                    } while (repeat);
                    if (turnOrder.contains(p) && !p.isInJail()) {
                        if (!p.isAiAgent()) {
                            p.leaveGame();
                            p.unMortgage();
                            trade(p);
                        }
                        p.developProperties();
                    }

                } else {
                    if (turnOrder.contains(p) && p.getCurrentPos() == 10) {
                        tiles.get(10).activeEffect(p);//Activate the jail tile to serve time
                    }
                }

            }
            if( timeUp ){
                System.out.println("Time limit reached");
                System.out.println(version);
                break;
            } else {
                turns++;
            }
        }
        try {
            gameOver();
        } catch ( Exception e ){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Processes the ending of the game and declares the winner
     * @throws Exception
     */
    private void gameOver() throws Exception {

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
            System.out.println("Game over");
            winner = turnOrder.peekFirst();
        } else if (version.equals("abridged") && turnOrder.size() == 1) { //abridged game ended like a full game
            System.out.println("Game over before time limit reached");
            winner = turnOrder.peekFirst();
        } else { //no winner
            throw new Exception("illegal end game state");
        }


        assert winner != null;
        System.out.println(winner.getName() + " has won the game with a net worth of " + winner.netWorth());
        storeData( winner, winner.netWorth() );
        if (winner.isAiAgent()) {
            System.out.println(winner.getPersonality());//TODO remove this, trait balancing inspection
        }
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
                confirm = yesNoInput(player.getName() + ", do you agree that " + leavingPlayer.getName() + " can leave the game?");
                if (!confirm) { //votes must be unanimous so if one disagrees, player can't leave
                    canLeave = false;
                    System.out.println("Sorry " + leavingPlayer.getName() + ", you cannot leave the game right now");
                    break;
                }
            }
        }

        return canLeave;
    }

    /**
     * Takes a yes no question and returns a boolean if answer is yes or no, loops until correct input is recieved
     *
     * @param message must have an answer of yes or no
     */
    private boolean yesNoInput( String message ) {
        System.out.println( message );
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        boolean valid = false; //flag for valid input
        boolean userDecision = false;

        //ask if want to purchase
        while(!valid){
            String decision = userInputScanner.nextLine();
            //normalise input
            decision = decision.toLowerCase();

            if( decision.equals( "no" )){
                userDecision = false;
                valid = true;
            }else if( decision.equals( "yes" )){
                userDecision = true;
                valid = true;
            }else {
                System.out.println("Sorry, please try again (you need to type yes OR no)");
            }
        }

        return userDecision;
    }

    /**
     * Player trading method, called at the end of the turn for the player right now, in GUI should only be called once
     * per turn per player
     *
     * @param currentPlayer, the player making the trade
     */
    public void trade( Player currentPlayer ) {

        LinkedList<Object> tradeAssetsGive; //assets to be traded to currentPlayer
        LinkedList<Object> tradeAssetsReceive = null; //assets to be traded to currentPlayer
        Player choice; //container for chosen player
        boolean input = false; //boolean flag for if player would like to trade


        //ask for confirmation
        if(currentPlayer.getAssets().size() != 0){
            input = yesNoInput(currentPlayer.getName() + ", would you like to trade (yes/no)?");
        }

        //select player
        if( input ){
            //print players
            System.out.println("AVAILABLE TRADES:");
            int count = 0;
            for( Player player : turnOrder ){
                if(player.getAssets().size() != 0 && (player != currentPlayer)){
                    System.out.println(player.getName());
                    count++;
                }
            }
            if(count != 0){
                //player selection
                choice = playerSelection();
                //if choice is null, player cancelled
                if( choice != null ) {
                    //select assets to give to chosen player
                    System.out.println(currentPlayer.getName() + ", select assets that you want to give " + choice.getName());
                    tradeAssetsGive = currentPlayer.tradeAssetSelection();
                    if (tradeAssetsGive != null) {
                        //select assets to be received from chosen player
                        System.out.println(currentPlayer.getName() + ", select " + choice.getName() + "'s assets that you want in return");
                        tradeAssetsReceive = choice.tradeAssetSelection();
                    }

                    if (tradeAssetsGive != null && tradeAssetsReceive != null) {
                        //print selections
                        System.out.println("Your Choices:");
                        System.out.println("Give:");
                        System.out.println(tradeAssetsGive.toString());
                        System.out.println("Receive:");
                        System.out.println(tradeAssetsReceive.toString());
                        //get confirmation from the other player
                        boolean confirm = yesNoInput(choice.getName() + ", do you accept the offer (yes/no)?");
                        //perform transfers
                        if (confirm) {
                            for (Object asset : tradeAssetsGive) {
                                currentPlayer.removeAsset(asset);
                                choice.addAsset(asset);
                            }
                            for (Object asset : tradeAssetsReceive) {
                                choice.removeAsset(asset);
                                currentPlayer.addAsset(asset);
                            }
                            System.out.println("Trade Complete");
                            //check for complete set
                            currentPlayer.completeSetProperties();
                            choice.completeSetProperties();
                        } else {
                            System.out.println("Trade Rejected");
                        }
                    }
                }
            }
        }
    }

    /**
     * Text based method for choosing player with assets for trading
     * //TODO convert to GUI compatible method
     *
     * @return Player, a selected player
     */
    private Player playerSelection(){
        boolean valid = false;
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        Player choice = null;

        while(!valid ){
            String decision = null;
            boolean found = false; //flag for if given player found
            System.out.println("Please enter a player name or type cancel: ");
            decision = userInputScanner.nextLine(); //fetch user input
            if( decision.toLowerCase().equals("cancel")){
                choice = null;
                break;
            }else {
                for( Player player : turnOrder) {
                    if(player.getName().toLowerCase().equals(decision.toLowerCase())){
                        found = true; //user input reflects a possible property
                        choice = player; //assign chosen player to allow for trading
                        break;
                    }
                }
            }

            //give response to user
            if( found ){
                System.out.println("You have selected " + choice.getName());
                valid = true;
            }else {
                System.out.println("Sorry, please try again, ID not found");
            }
        }
        return choice;
    }

    public void storeData( Player p, int netWorth ){
        Pair pair = new Pair( turns, netWorth);
        dataStore.get(p.getName()).add(pair);
    }


}