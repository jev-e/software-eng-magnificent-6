package classStructure;

import java.util.*;

/**
 * @author Ayman Bensreti
 *	Game board and logic
 */
public class Board {
    Deque<CardEffect> potLuck;
    Deque<CardEffect> opportunityKnocks;
    int turns;
    LinkedList<Player> turnOrder;
    HashMap<Integer,BoardTile> tiles;// key: iD value: (classStructure.Property|Effect)
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
            if(b instanceof TileEffect) {
                ((TileEffect) b).setBoard(this);
            }
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
                System.out.printf("%s:%s Has Gone To Jail", currentPlayer.getName(), currentPlayer.getToken().getSymbol());
                currentPlayer.jailPlayer();//sets players status to jailed
                currentPlayer.setCurrentPos(0);//Currently sends player to classStructure.Go however must be changed to jail position
                //In future versions the player method jailPlayer will move the player to the jail tile
            }
        }
        if(count < 3) {
            result = die1 + die2;
            int position = (currentPlayer.getCurrentPos() + result) % tiles.size();
            System.out.printf("%s:%s Rolled |%d|%d| = %d", currentPlayer.getName(), currentPlayer.getToken().getSymbol(), die1, die2, result);
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
        for(Player p: turnOrder) {
            System.out.println(p.getName() + " Money:" + p.getMoney());
        }


        while (turns < 10) {
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
                    displayAsString();
                    p.passGo();
                    tiles.get(p.getCurrentPos()).activeEffect(p);
                    displayAsString();
                }while (repeat);
                propertyImprovement(p);
            }

            turns++;
        }
    }

    /**
     * Bankrupts player from the game
     * @param target player to removed from turn order
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
        System.out.println("Properties available for improvement:");
        if(currentPlayer.getAssets().size() != 0){
            //update complete set flags for all assets of player
            completeSetProperties( currentPlayer );
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
            decision.toLowerCase();

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
     * Calculates if the property is a complete set and updates boolean flag
     * 
     * @param currentPlayer
     */
    private void completeSetProperties( Player currentPlayer ) {
        HashMap<Group, Integer> count = new HashMap<>();

        for(Object asset : currentPlayer.getAssets()){
            //pull out all properties owned and form a count
            if( asset instanceof Property){
                if (count.get(((Property) asset).group) == null) {
                    count.put(((Property) asset).group, 1);
                } else {
                    int previousCount = count.get(((Property) asset).group);
                    count.put(((Property) asset).group, previousCount++ );
                }
            }
        }
        for(Group key : count.keySet()){
            for( Object asset: currentPlayer.getAssets()){
                if( (asset instanceof Property) && count.get(key) == key.getMemberCount() ) {
                    ((Property) asset).completedSet = true;
                }
            }
        }
    }
}