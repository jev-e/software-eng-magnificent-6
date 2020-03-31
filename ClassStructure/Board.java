package ClassStructure;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * @author Ayman Bensreti
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
            group = ((Property) currentTile).group.toString();
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
        if(die1 == die2) {
            repeat = true;//tracks double rolled for player to go again
            if (count >= 3) {
                result = die1 + die2;
                System.out.printf("%s:%s Has Gone To Jail\n", currentPlayer.getName(), currentPlayer.getToken().getSymbol());
                currentPlayer.jailPlayer();//sets players status to jailed
                currentPlayer.setCurrentPos(0);//Currently sends player to Go however must be changed to jail position
                //In future versions the player method jailPlayer will move the player to the jail tile
            }
        }
        if(count < 3) {
            result = die1 + die2;
            int position = (currentPlayer.getCurrentPos() + result) % tiles.size();
            System.out.printf("%s:%s Rolled |%d|%d| = %d\n", currentPlayer.getName(), currentPlayer.getToken().getSymbol(), die1, die2, result);
            currentPlayer.setCurrentPos(position);
        }
        return result;
    }

    /**
     * Displays the board and rolls the die for 10 complete turns
     */
    public void demo() {
        displayAsString();
        String tempIn;
        LinkedList<Player> playerPool = (LinkedList<Player>) turnOrder.clone();
        for(Player p: turnOrder) {
            System.out.println(p.getName() + " Money:" + p.getMoney());
        }


        while (turns < 5) {
            for(Player p : turnOrder) {
                int count = 0;
                do{
                    count++;//keep track of number of repeat turns player has had
                    repeat = false;
                    System.out.println("Press Enter to continue");
                    try {
                        System.in.read();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    roll(p,count);
                    //displayAsString();
                    p.passGo();
                    tiles.get(p.getCurrentPos()).activeEffect(p);
                    displayAsString();
                }while (repeat);
                propertyImprovement(p);
                trade(p);
            }

            turns++;
        }
        for(Player player : playerPool) {
            System.out.println(player.getName() + " ended with £" + player.getMoney());
        }

    }

    /**
     * Bankrupts player from the game
     * @param target player to removed from turn order
     * To be added handling of assets
     */
    void bankruptPlayer(Player target) {
        turnOrder.remove(target);
    }

    /**
     * Drives the interactive menu for property improvement and contains logic
     * occurs at the end of the turn for the player
     *
     * @param currentPlayer, the player looking to make some improvements
     */
    private void propertyImprovement( Player currentPlayer ) {
        ArrayList<Property> improvableProperties = new ArrayList<>();
        Scanner userInputScanner = new Scanner( System.in );

        //find all improvable properties
        if(currentPlayer.getAssets().size() != 0){
            System.out.println("Properties available for improvement:");
            //update complete set flags for all assets of player
            //completeSetProperties( currentPlayer ); //TODO is this necessary
            //loop through assets, finding all properties
            for(Object asset : currentPlayer.getAssets()) {
                if(asset instanceof Property) {
                    //if potentially improvable (part of complete set and doesn't have a hotel built and isn't currently mortgaged)
                    if( ((Property) asset).completedSet  && ((Property) asset).hotelNo != 1 && !((Property) asset).mortgaged) {
                        System.out.println(((Property) asset).iD + " " + ((Property) asset).title); //print for selection
                        improvableProperties.add((Property) asset); //add improvable properties for ease of search
                    }
                }
            }
        }
        //check if we have any properties as this contains a lot of looping
        if(improvableProperties.size() != 0){
            boolean valid = false; //flag for valid input
            boolean goAgain = true; //flag for repeating loop
            boolean improve; //flag for if improvements desired
            int decision = 0;

            //fetch confirmation
            improve = yesNoInput("Do you want to make an improvement? (yes/no)");

            if(improve) {
                while( goAgain ) {
                    while(!valid){
                        boolean found = false; //flag for if given property found
                        System.out.println("Please enter a property ID: ");
                        decision = userInputScanner.nextInt(); //fetch user input

                        for( Property pp : improvableProperties){
                            if(pp.iD == decision){
                                found = true; //user input reflects a possible property
                            }
                        }

                        //give response to user
                        if( found ){
                            System.out.println("You have selected property: " + decision);
                            valid = true;
                        }else {
                            System.out.println("Sorry, please try again, ID not found");
                        }
                    }
                    //reset flag
                    valid = false;
                    //fetch property chosen by user
                    Property toBeImproved = (Property) tiles.get(decision);

                    //init group store
                    ArrayList<Property> group = new ArrayList<>();
                    //find all houses in same group
                    for( Property pp : improvableProperties){
                        if(pp.group == toBeImproved.group){
                            group.add(pp);
                        }
                    }

                    //flag for if improvement is possible
                    boolean validPurchase = true;


                    //if hotel needs to be bought
                    if((toBeImproved.getHousesNo() == 4) && (toBeImproved.hotelNo == 0)){
                        //all other properties in group must have 4 houses as well
                        for( Property pp: group ){
                            if(pp.housesNo != 4){
                                validPurchase = false;
                                break;
                            }
                        }
                        //if purchase possible and if player can afford it
                        if( validPurchase && (currentPlayer.getMoney() > toBeImproved.group.getBuildingCost())){
                            improve = yesNoInput("Do you want to purchase a hotel for £" + toBeImproved.group.getBuildingCost() + " (yes/no)?");
                            if( improve) {
                                toBeImproved.purchaseHotel( currentPlayer ); //manages transaction
                                improvableProperties.remove( toBeImproved );
                            }
                        } else {
                            System.out.println("Sorry, the property is not currently improvable");
                        }

                    } else if((toBeImproved.getHousesNo() < 4) && (toBeImproved.getHotelNo() == 0 )) { //only a house can be bought
                        //all other properties must have the same or more than the number of houses on the property
                        for( Property pp: group ){
                            if( pp.housesNo < toBeImproved.getHousesNo() ){
                                validPurchase = false;
                                break;
                            }
                        }
                        //if purchase possible and if player can afford it
                        if( validPurchase && (currentPlayer.getMoney() > toBeImproved.group.getBuildingCost())){
                            improve = yesNoInput("Do you want to purchase a house £" + toBeImproved.group.getBuildingCost() + " (yes/no)?");
                            if( improve) {
                                toBeImproved.purchaseHouse( currentPlayer ); //manages transaction
                            }
                        } else {
                            System.out.println("Sorry, the property is not currently improvable");
                        }

                    }
                    //find if any improvable properties left
                    if(improvableProperties.size() == 0){
                        System.out.println("No properties left to improve");
                        break;
                    }
                    goAgain = yesNoInput("Do you want to go again? (yes/no)");
                }
            }



        }




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
     * Updates flags for all player's properties, updates base rent amount if complete set with no properties. Should
     * be called whenever a change to property ownership has occurred - e.g in purchase, sale, trading etc
     *
     * @param currentPlayer player to check status of their property sets
     */
    public void completeSetProperties( Player currentPlayer ) {
        HashMap<Group, Integer> count = new HashMap<>();

        for(Object asset : currentPlayer.getAssets()){
            //pull out all properties owned and form a count
            if( asset instanceof Property){
                int prev = 0;
                if (count.get(((Property) asset).group) != null) {
                    prev = count.get(((Property) asset).group);
                }
                count.put( ((Property) asset).group, prev + 1);

            }
        }
        System.out.println(count.toString());
        for(Group key : count.keySet()){
            for( Object asset: currentPlayer.getAssets()){
                if( (asset instanceof Property) && count.get(key) == key.getMemberCount() && !((Property) asset).completedSet ) {
                    System.out.println(((Property) asset).title + " complete set updated");
                    ((Property) asset).completedSet = true;
                    ((Property) asset).updateRent();
                } else if((asset instanceof Property) && count.get(key) == key.getMemberCount() && ((Property) asset).completedSet ){
                    System.out.println(((Property) asset).title + " complete set removed");
                    ((Property) asset).completedSet = false;
                    ((Property) asset).updateRent();
                }
            }
        }
    }

    /**
     * Player trading method, called at the end of the turn for the player right now, in GUI should only be called once
     * per turn per player
     *
     * @param currentPlayer
     */
    public void trade( Player currentPlayer ) {

        LinkedList<Object> tradeableAssetsReceive = new LinkedList<>(); //assets to be traded from currentPlayer
        LinkedList<Object> tradeableAssetsGive = new LinkedList<>(); //assets to be traded to currentPlayer
        LinkedList<Object> tradeAssetsGive = new LinkedList<>(); //assets to be traded to currentPlayer
        LinkedList<Object> tradeAssetsReceive = new LinkedList<>(); //assets to be traded to currentPlayer

        Player choice = null; //container for chosen player
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        boolean valid = false; //flag for valid input
        boolean goAgain = true; //flag for repeating input
        boolean input = false;
        boolean cancel = false;

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
            //loop until valid choice
            if(count != 0){

            }
            while(!valid ){
                String decision = null;
                boolean found = false; //flag for if given player found
                System.out.println("Please enter a player name or type cancel: ");
                decision = userInputScanner.nextLine(); //fetch user input
                if( decision.toLowerCase().equals("cancel")){
                    cancel = true;
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

            if( !cancel ){
                valid = false;

                //find tradeable assets for both players

                //select assets to give to chosen player
                System.out.println(currentPlayer + ", select assets that you want to give " + choice.getName());

                //current player
                for( Object asset: currentPlayer.getAssets() ) {
                    if( asset instanceof Property ){
                        if( !((Property) asset).developed ){
                            System.out.println(((Property) asset).title);
                            tradeableAssetsGive.add(asset);
                        }
                    } else if( !(asset instanceof GetOutOfJail) ){
                        System.out.println(((BoardTile)asset).title);
                        tradeableAssetsGive.add(asset);
                    }
                }

                String assetChoice = null;
                while( goAgain ) {
                    while (!valid) {
                        boolean found = false; //flag for if given property found
                        System.out.println("Please enter an asset name: ");
                        assetChoice = userInputScanner.nextLine(); //fetch user input


                        for (Object asset : tradeableAssetsGive ) {
                            if (((BoardTile)asset).title.toLowerCase().equals(assetChoice.toLowerCase())) {
                                System.out.println("You have selected property: " + ((BoardTile) asset).title);
                                tradeAssetsGive.add( asset );
                                found = true; //user input reflects a possible property
                            }
                        }

                        //give response to user
                        if (found) {

                            valid = true;
                        } else {
                            System.out.println("Sorry, please try again, asset not found");
                        }
                    }

                    //reset flag
                    valid = false;
                    //find if any improvable properties left
                    if(tradeableAssetsGive.size() == tradeAssetsGive.size()){
                        System.out.println("No assets left to choose");
                        break;
                    }
                    goAgain = yesNoInput("Do you want to go again? (yes/no)");
                }

                System.out.println(currentPlayer.getName() + ", select " + choice.getName() + "'s assets that you want in return");
                //player being traded with
                for( Object asset: choice.getAssets() ) {
                    if( asset instanceof Property ){
                        if( !((Property) asset).developed ){
                            System.out.println(((Property) asset).title);
                            tradeableAssetsReceive.add(asset);
                        }
                    } else if( !(asset instanceof GetOutOfJail) ){
                        System.out.println(((BoardTile)asset).title);
                        tradeableAssetsReceive.add(asset);
                    }
                }

                assetChoice = null;
                while( goAgain ) {
                    while (!valid) {
                        boolean found = false; //flag for if given property found
                        System.out.println("Please enter an asset name: ");
                        assetChoice = userInputScanner.nextLine(); //fetch user input


                        for (Object asset : tradeableAssetsReceive ) {
                            if (((BoardTile)asset).title.toLowerCase().equals(assetChoice.toLowerCase())) {
                                System.out.println("You have selected property: " + ((BoardTile) asset).title);
                                tradeAssetsReceive.add( asset );
                                found = true; //user input reflects a possible property
                            }
                        }

                        //give response to user
                        if (found) {
                            valid = true;
                        } else {
                            System.out.println("Sorry, please try again, asset not found");
                        }
                    }

                    //reset flag
                    valid = false;
                    //find if any improvable properties left
                    if(tradeableAssetsReceive.size() == tradeAssetsReceive.size()){
                        System.out.println("No assets left to choose");
                        break;
                    }
                    goAgain = yesNoInput("Do you want to go again? (yes/no)");
                }
                //print selections
                System.out.println("Your Choices:");
                System.out.println("Give:");
                System.out.println(tradeAssetsGive.toString());
                System.out.println("Receive:");
                System.out.println(tradeAssetsReceive.toString());
                //get confirmation from the other player
                boolean confirm = yesNoInput( choice.getName() + ", do you accept the offer (yes/no)?");
                //perform transfers
                if(confirm) {
                    for( Object asset : tradeAssetsGive ){
                        currentPlayer.getAssets().remove( asset );
                        choice.getAssets().add( asset );
                    }
                    for( Object asset : tradeAssetsReceive ){
                        choice.getAssets().remove( asset );
                        currentPlayer.getAssets().add( asset );
                    }
                    System.out.println("Trade Complete");
                    //check for complete set
                    completeSetProperties( currentPlayer );
                    completeSetProperties( choice );
                } else {
                    System.out.println("Trade Rejected");
                }

            }
        }
    }

}