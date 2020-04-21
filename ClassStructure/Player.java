package ClassStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

//import sun.plugin.javascript.navig.Link;

/**
 * @author Ayman Bensreti, Calvin Boreham
 *	Player information
 */
public class Player {
    private int currentPos; //ID of player position on board
    private int previousPos; //ID of player's previous position on board
    private String name; //name of player
    private Token token; //token representation of player on board
    private int money; //money held by player
    private LinkedList<Object> assets; //assets of player (utilities, station, property, get out of jail card)
    private boolean canBuy; //flag for if player is eligible for purchasing property
    private boolean inJail; //indicator to differ between players who are jailed or just visiting
    private int jailTime; //counter to indicate how many turns a player has spent in jail
    private Board board; //board the player is playing on
    private int lastRoll;//For use in utility functions
    private boolean aiAgent;//flag for control method (userInput|Agent response)
    private Trait personality;

    /**
     * Sets players name and token and initialises the players starting assets
     *
     * @param name  player name
     * @param token one of token enum
     * @param board the current board being played
     */
    public Player(String name, Token token, Board board, boolean aiAgent) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 1500;//All references to money is in £'s
        assets = new LinkedList<>();
        canBuy = false;
        inJail = false;
        this.aiAgent = aiAgent; //by default assumed human player
        this.jailTime = 0;
        this.board = board;
        if (aiAgent) {
            personality = new Trait();//generate personality
        }
    }

    /**
     * Adds amount to players balance
     * @param amount integer amount to modify the balance by
     */
    public void payPlayerAmount(int amount) {
        money += amount;
    }

    /**
     * Deducts amount from players money if they can pay returning the amount
     * if they cannot pay assets can be sold
     * if assets will not cover payment the player is bankrupt, return the net worth of the player
     * it is imperative that it is checked that the player has enough money to purchase items such as houses before
     * this method is called
     * @param amount amount that was payed
     * @return the amount of money the player was able to pay
     */
    public int deductAmount(int amount) {

        System.out.println(name + ":" + money + ":" + amount); //debug comment
        int payableAmount = 0;

        if(money >= amount) { //player can pay, so pay full amount
            payPlayerAmount(-amount); //deduct money from player
            payableAmount = amount; //update payable amount to full amount
        }else{
            //find player net worth
            int netWorth = netWorth();
            if (netWorth >= amount) {
                //can pay after asset selling, trigger it
                //owner money will be updated
                if (!isAiAgent()) {
                    assetSelling(amount - money);
                } else {
                    agentSellAssets(amount - money);
                }
                //pay amount
                payPlayerAmount(-amount);
                payableAmount = amount; //update payable amount to full amount
            }else{
                //no point asset selling, bankrupt player
                System.out.println("BANKRUPT");
                bankrupt();
                //return the amount they could have played
                payableAmount = netWorth;
            }
        }
        return payableAmount;//returns what the player can pay
    }

    /**
     * Adds an item(Property|Station|Utility|Card) to players assets
     * updates owner for non card assets
     *
     * @param item one of (Property|Station|Utility|Card) to be added
     */
    public void addAsset(Object item) {
        assets.add(item);
        if (item instanceof Property) {
            ((Property) item).setOwner(this);
        } else if (item instanceof Station) {
            ((Station) item).setOwner(this);
        } else if (item instanceof Utility) {
            ((Utility) item).setOwner(this);
        }
    }

    /**
     * Removes an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card)
     */
    public void removeAsset(Object item) {
        if (item instanceof Property) {
            ((Property) item).setOwner(null);
        } else if (item instanceof Station) {
            ((Station) item).setOwner(null);
        } else if (item instanceof Utility) {
            ((Utility) item).setOwner(null);
        }
        assets.remove(item);
    }

    /**
     * Getter for player name
     * @return name player name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for player current position
     * @return currentPos players position on the board
     */
    public int getCurrentPos() {
        return currentPos;
    }

    /**
     * Sets the position on the board that the player is on and updates previous position
     * @param currentPos new position for player to be moved to
     */
    public void setCurrentPos(int currentPos) {
        this.previousPos = this.currentPos;//maintains previous position before change
        this.currentPos = currentPos;
    }

    public int getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(int previousPos) {
        this.previousPos = previousPos;
    }

    /**
     * Setter for player name
     * @param name the new player name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Getter for player money
     *
     * @return money the amount in player bank account
     */
    public int getMoney() {
        return money;
    }

    /**
     * Setter for player money for use in testing
     *
     * @param amount the amount to set player money to
     */
    public void setMoney(int amount) {
        money = amount;
    }

    /**
     * Getter for player board
     *
     * @return board player currently on
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Setter for player board
     *
     * @param board the board currently in play
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isAiAgent() {
        return aiAgent;
    }

    public void setAiAgent(boolean aiAgent) {
        this.aiAgent = aiAgent;
        if (isAiAgent()) {
            personality = new Trait();//generate traits
        }
    }

    /**
     * Fetches the players assets list
     *
     * @return a list of items(Property|Station|Utility|Card)
     */
    public LinkedList<Object> getAssets() {
        return assets;
    }

    /**
     * Fetches whether the player can buy a property or not
     * @return canBuy boolean representation of player's ability to purchase assets
     */
    public boolean CanBuy() {
        return canBuy;
    }

    /**
     * Pass go check and money
     */
    public void passGo() {
        if(currentPos < previousPos) {
            payPlayerAmount(200);//collect £200
        }
        if(!canBuy) {//Checks if the player can buy and set it to true if false
            canBuy = true;
        }
    }

    /**
     * Getter for players token enum
     * @return enum consisting of the token name and image path
     */
    public Token getToken() {
        return token;
    }

    /**
     * sets player to be in jail
     * to be added to alo move player to jail location instead of Go as currently set
     * Gives the player the option to pay or use get out of jail free card
     */
    public void jailPlayer() {
        inJail = true;//sets player in jail
        GetOutOfJail getOutOfJail = null;
        setCurrentPos(10);
        for (Object item : assets) {//looks for a get out of jail free card
            if (item instanceof GetOutOfJail) {
                getOutOfJail = (GetOutOfJail) item;//if found getOutOfJail is set to this card
            }//If the player has two the first one is used
        }
        //Player choice here
        if (!isAiAgent()) {//if human player
            Scanner userIn = new Scanner(System.in);
            int choice;
            boolean selected = false;
            while (!selected) {//Repeat until an option is selected
                System.out.println(name + " " + token.getSymbol());
                System.out.println("Type the number of the option you would like to select");
                System.out.println("1. Serve Jail time\n2. Pay £50 bail");
                if (getOutOfJail != null) {
                    System.out.println("3. Use get out of jail free card");//only display option is player has card
                }
                choice = userIn.nextInt();
                switch (choice) {
                    case 1:
                        //Do nothing player serves time
                        selected = true;
                        break;
                    case 2:
                        if (money > 50) {//If the player has the funds take the amount and remove them from jail
                            deductAmount(50);
                            leaveJail();
                            selected = true;//note a selection has been made
                        } else {
                            System.out.println("Insufficient funds");
                        }
                        break;
                    case 3:
                        if (getOutOfJail != null) {//check player has card for text version, will be impossible in GUI
                            getOutOfJail.playCard();//play get out of jail free card
                            selected = true;
                        }
                        break;
                    default:
                        System.out.println("Invalid Input");
                        break;
                }
            }
        } else {
            if (getOutOfJail != null) {
                getOutOfJail.playCard();//AI uses card
            } else if (!personality.isPatient()) {//AI is not patient
                if (personality.isCautious()) {
                    if (cautiousWillBuy(50)) {//checks if AI will pay bail
                        deductAmount(50);
                        leaveJail();
                    }
                } else {
                    if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip
                        deductAmount(50);
                        leaveJail();//pay bail based on coin flip
                    }
                }
            }
            //otherwise do nothing and serve time
        }
    }

    /**
     * Increments time spent in jail by one turn
     */
    public void serveJailTime() {
        jailTime++;
    }

    /**
     * Time player has been in jail getter
     * @return int number of turns player has been jailed for
     */
    public int getJailTime() {
        return jailTime;
    }

    /**
     * Used to remove a player from jail by any means
     * Pay to get out/ spend turns in jail/ get out of jail card
     * maintains jailed boolean and jail count
     */
    public void leaveJail() {
        inJail = false;
        jailTime = 0;
    }

    /**
     * Check to see if the player is in jail for jail tile and rent checks
     */
    public boolean isInJail() {
        return inJail;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    /**
     * Updates flags for all player's properties, updates base rent amount if complete set with no properties. Should
     * be called whenever a change to property ownership has occurred - e.g in purchase, sale, trading etc
     */
    public void completeSetProperties() {
        HashMap<Group, Integer> count = new HashMap<>(); //frequency distribution

        for (Object asset : assets) { //for each asset owned by player
            //pull out all properties owned and form a count
            if (asset instanceof Property) {
                int prev = 0;
                if (count.get(((Property) asset).getGroup()) != null) {
                    prev = count.get(((Property) asset).getGroup());
                }
                count.put(((Property) asset).getGroup(), prev + 1);
            }
        }
        System.out.println(count.toString()); //debug

        for(Group key : count.keySet()){ //for each key in frequency distribution
            for( Object asset: assets){ //for each asset player owns
                if ((asset instanceof Property) && count.get(key) == key.getMemberCount() &&
                        !((Property) asset).isCompletedSet() && ((Property) asset).getGroup() == key) {
                    //if asset is property and the property is part of a group where the number owned matches
                    //member count in group enum and property is not already marked as a complete set
                    System.out.println(((Property) asset).title + " complete set updated"); //debug
                    ((Property) asset).setCompletedSet(true);
                    ((Property) asset).updateRent();
                } else if ((asset instanceof Property) && count.get(key) != key.getMemberCount() &&
                        ((Property) asset).isCompletedSet() && ((Property) asset).getGroup() == key) {
                    //if asset is property and the group count is incorrect for property's group's member count
                    //and property is marked as complete set
                    System.out.println(((Property) asset).title + " complete set removed"); //debug
                    ((Property) asset).setCompletedSet(false);
                    ((Property) asset).updateRent();
                }
            }
        }
    }

    /**
     * Generates a text based interface to initiate the choosing of assets for trading
     * @return tradeAssets, the assets selected for trading
     */
    public LinkedList<Object> tradeAssetSelection() {
        LinkedList<Object> tradeAssets = new LinkedList<>(); //selection store
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        String assetChoice; //user selection
        boolean goAgain = true;
        boolean valid = false;
        boolean cancel = false;

        //display viable choices
        for( Object asset: assets ) { //for each asset owned by player
            if( asset instanceof Property ){
                if( !((Property) asset).isDeveloped() ){
                    System.out.println(((Property) asset).title); //only non developed properties can be traded
                }
            } else if( !(asset instanceof GetOutOfJail) ){
                System.out.println(((BoardTile)asset).title); //cards cannot be traded
            }
        }


        while( goAgain ) {
            while (!valid) {
                boolean found = false; //flag for if given property found
                System.out.println("Please enter an asset name or type cancel: ");
                assetChoice = userInputScanner.nextLine(); //fetch user input

                if (!(assetChoice.toLowerCase().equals("cancel"))) {
                    for (Object asset : assets) {
                        if (asset instanceof GetOutOfJail) {
                            continue;//skip cards
                        }
                        if (((BoardTile) asset).title.toLowerCase().equals(assetChoice.toLowerCase())) {
                            System.out.println("You have selected property: " + ((BoardTile) asset).title); //normalise
                            tradeAssets.add(asset); //temporary store asset
                            found = true; //user input reflects a possible property
                        }
                    }
                } else {
                    cancel = true;
                }

                if (cancel) {
                    break;
                } else if (found) {
                    valid = true;
                } else {
                    System.out.println("Sorry, please try again, asset not found");
                }
            }

            //reset flag
            if( !cancel ){
                valid = false;
                goAgain = yesNoInput("Do you want to go again? (yes/no)");
            } else {
                tradeAssets = null;
                break;
            }
        }

        return tradeAssets;

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

            if (decision.equals("no")) {
                userDecision = false;
                valid = true;
            } else if (decision.equals("yes")) {
                userDecision = true;
                valid = true;
            } else {
                System.out.println("Sorry, please try again (you need to type yes OR no)");
            }
        }

        return userDecision;
    }

    public void developProperties() {
        Scanner userIn = new Scanner(System.in);
        boolean develop;
        boolean goAgain = true;
        if (improvableProperties() == 0) {
            return;//no properties to develop
        }
        if (!isAiAgent()) {
            develop = yesNoInput("Do you want to make an improvement? (yes/no)");
            if (develop) {
                while (goAgain) {
                    improvableProperties();
                    Property toBeImproved = selectProperty();
                    System.out.println("1. Buy house | 2. Buy Hotel");
                    int choice = userIn.nextInt();
                    while (choice != 1 && choice != 2) {
                        System.out.println("Invalid selection");
                        choice = userIn.nextInt();
                    }
                    if (choice == 1) {
                        if (money > toBeImproved.getGroup().getBuildingCost()) {
                            if (!toBeImproved.purchaseHouse()) {
                                System.out.println("building violation");
                            }
                        } else {
                            System.out.println("insufficient funds");
                            goAgain = false;
                        }
                    } else {
                        if (money > toBeImproved.getGroup().getBuildingCost()) {
                            if (!toBeImproved.purchaseHotel()) {
                                System.out.println("building violation");
                            }
                        } else {
                            System.out.println("insufficient funds");
                            goAgain = false;
                            continue;//end loop here
                        }
                    }
                    goAgain = yesNoInput("Go again?");
                }
            }
        } else {
            //AI decision
            if (decideDevelop()) {
                agentDevelopProperties();//AI developing logic
            }
        }
    }

    /**
     * Drives the interactive menu for property improvement and contains logic
     * occurs at the end of the turn for the player
     */
    public void propertyImprovement() {

        //ArrayList<Property> improvableProperties = new ArrayList<>();
        Scanner userInputScanner = new Scanner(System.in);
        int count = improvableProperties();

        //check if we have any properties as this contains a lot of looping
        if(count != 0){
            boolean valid = false; //flag for valid input
            boolean goAgain = true; //flag for repeating loop
            boolean improve; //flag for if improvements desired
            int decision = 0;

            //fetch confirmation
            improve = yesNoInput("Do you want to make an improvement? (yes/no)");

            if(improve) {
                while( goAgain ) {
                    //property selection
                    Property toBeImproved = selectProperty();
                    if( toBeImproved.isCompletedSet() ){
                        //find all houses in same group
                        ArrayList<Property> group = findGroups( toBeImproved );

                        //flag for if improvement is possible
                        boolean validPurchase = true;

                        //if hotel needs to be bought
                        if((toBeImproved.getHousesNo() == 4) && (toBeImproved.getHotelNo() == 0)){
                            //all other properties in group must have 4 houses as well
                            for( Property pp: group ){
                                if (pp.getHousesNo() != 4 || pp.getHotelNo() == 1) {
                                    validPurchase = false;
                                    break;
                                }
                            }
                            //if purchase possible and if player can afford it
                            if( validPurchase && (money > toBeImproved.getGroup().getBuildingCost())){
                                improve = yesNoInput("Do you want to purchase a hotel for £" + toBeImproved.getGroup().getBuildingCost() + " (yes/no)?");
                                if( improve) {
                                    toBeImproved.purchaseHotel(); //manages transaction
                                }
                            } else {
                                System.out.println("Sorry, the property is not currently improvable");
                            }

                        } else if((toBeImproved.getHousesNo() < 4) && (toBeImproved.getHotelNo() == 0 )) { //only a house can be bought
                            //all other properties must have the same or more than the number of houses on the property
                            for( Property pp: group ){
                                if( pp.getHousesNo() < toBeImproved.getHousesNo() ){
                                    validPurchase = false;
                                    break;
                                }
                            }
                            //if purchase possible and if player can afford it
                            if( validPurchase && (money > toBeImproved.getGroup().getBuildingCost())){
                                improve = yesNoInput("Do you want to purchase a house £" + toBeImproved.getGroup().getBuildingCost() + " (yes/no)?");
                                if( improve) {
                                    toBeImproved.purchaseHouse(); //manages transaction
                                }
                            } else {
                                System.out.println("Sorry, the property is not currently improvable");
                            }

                        }

                        boolean possible = false;
                        //check if any more improvements possible
                        for( Object asset : assets ){
                            if( asset instanceof Property ){
                                if( ((Property) asset).getHotelNo() == 0  && ((Property) asset).isCompletedSet() ){
                                    possible = true;
                                    break;
                                }
                            }
                        }

                        if(possible){
                            goAgain = yesNoInput("Do you want to go again? (yes/no)");
                        } else {
                            System.out.println("No properties left to improve");
                            break;
                        }
                    } else {
                        System.out.println("Sorry, property not improvable");
                        goAgain = yesNoInput("Do you want to go again? (yes/no)");
                    }
                }
            }
        }
    }

    /**
     * User input driven method to allow for a property to be unmortgaged
     */
    public void unMortgage() {
        boolean mortgaged = false;

        for( Object asset : assets ){ //for each asset
            if( asset instanceof Property ){
                if( ((Property) asset).getDeveloped() ){
                    mortgaged = true; //update flag as a mortgaged property is present
                    break;
                }
            }
        }

        boolean confirm = false;

        if(mortgaged){
            confirm = yesNoInput("Do you want to un-mortgage a property?");
        }

        if(confirm){

            Property tempProperty;
            boolean goAgain = true;

            while( goAgain ){

                //print mortgaged properties
                for( Object asset : assets ){
                    if( asset instanceof Property ){
                        if( ((Property) asset).getDeveloped() ){
                            System.out.println(((Property) asset).iD + " " + ((Property) asset).title);
                        }
                    }
                }

                //choose a property
                System.out.println("Choose a property to un-mortgage");
                tempProperty = selectProperty();

                if( money >= (tempProperty.getCost()/2) && tempProperty.isMortgaged() ){ //if player can afford to un mortgage
                    System.out.println(tempProperty.title + " has been un-mortgaged for " + tempProperty.getCost()/2);
                    tempProperty.unmortgageProperty();
                } else {
                    System.out.println("Sorry, you can't do that");
                }
                goAgain = yesNoInput("Un-mortgage any other properties?");

            }
        }

    }

    /**
     * Text based method for displaying improvable property choices to the user
     * //TODO convert into GUI method to only show improvable properties to user
     * @return count the number of properties available to improve
     */
    private int improvableProperties() {
        int count = 0;
        System.out.println("Properties available for improvement:");
        //loop through assets, finding all properties
        for(Object asset : assets) {
            if(asset instanceof Property) {
                //if potentially improvable (part of complete set and doesn't have a hotel built and isn't currently mortgaged)
                if( ((Property) asset).isCompletedSet()  && ((Property) asset).getHotelNo() != 1 && !((Property) asset).isMortgaged() ) {
                    System.out.println(((Property) asset).iD + " " + ((Property) asset).title); //print for selection
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Method to allow a user to select a property from the asset list. It is assumed the user has already been shown
     * a list of appropriate options
     * //TODO convert into GUI method
     * @return the user property choice
     */
    private Property selectProperty() {
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        Property choice = null;
        boolean valid = false;
        int decision;

        while(!valid){
            boolean found = false; //flag for if given property found
            System.out.println("Please enter a property ID: ");
            decision = userInputScanner.nextInt(); //fetch user input

            for( Object asset : assets ){
                if(asset instanceof  Property){
                    if( ((Property) asset).iD == decision){
                        found = true; //user input reflects a possible property
                        choice = (Property) asset;
                    }
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

        return choice;
    }

    /**
     * Finds the net worth of the player by adding resell value of all assets to current money
     *
     * @return net worth
     */
    public int netWorth() {
        int netWorth = 0;
        //loop through assets
        for( Object asset : assets ){
            //property
            if( asset instanceof Property ){
                //mortgaged?
                if( ((Property) asset).isMortgaged() ){
                    netWorth += ( (((Property) asset).getCost())/2 );
                } else {
                    if( ((Property) asset).isDeveloped() ){
                        //add resell value of all properties
                        if( ((Property) asset).getHousesNo() >= 1){
                            netWorth += (((Property) asset).getHousesNo() * ((Property) asset).getGroup().getBuildingCost());
                        } else if( ((Property) asset).getHotelNo() == 1){
                            netWorth += (((Property) asset).getGroup().getBuildingCost() * 5);
                        }
                    }
                    //property worth
                    netWorth += ((Property) asset).getCost();
                }
            } else if( asset instanceof Station ){
                netWorth += ((Station) asset).getCost();
            } else if( asset instanceof Utility ){
                netWorth += ((Utility) asset).getCost();
            }
        }

        return netWorth + money;
    }

    /**
     * Removes player from turnorder and transfers all assets to bank ownership
     */
    private void bankrupt() {
        //remove player from turnorder
        System.out.println(name + " has been removed from the game");
        board.turnOrder.remove(this);
        board.repeat = false;//TODO test change to prevent repeat turn after bankrupt
        //transfer all assets to bank ownership
        for (Object asset : assets) {
            if (asset instanceof GetOutOfJail) {
                ((GetOutOfJail) asset).returnCard();
            } else if (asset instanceof Property) {
                ((Property) asset).setOwner(null);
                System.out.print(((Property) asset).title + " ");
            } else if (asset instanceof Station) {
                ((Station) asset).setOwner(null);
                System.out.print(((Station) asset).title + " ");
            } else if (asset instanceof Utility) {
                ((Utility) asset).setOwner(null);
                System.out.print(((Utility) asset).title + " ");
            }
        }
        System.out.println();
        assets = new LinkedList<>();//resets asset list
        System.out.println("All assets of " + name + " have been returned to the bank's ownership");
        System.out.println("Players left in the game:");
        for (Player pp : board.turnOrder) {
            System.out.println(pp.getName());
        }
    }

    /**
     * loops through assets giving options to sell assets until amount is reached
     * it is assumed amount can be reached
     *
     * @param amount, the money amount that must be raised
     */
    private void assetSelling( int amount ) {
        int choice; //user choice
        Property tempProperty; //temporary container for user choice
        boolean confirm; //user input
        boolean restart = true; //user input
        int sale; //amount raised via asset selling
        HashMap<Integer, Integer> mortgageIds = new HashMap<>(); //id of property mortgaged, mortgage cost
        HashMap<Integer, Integer> saleIds = new HashMap<>(); //id of property sold, property cost
        HashMap<Integer, Integer> houseSaleIds = new HashMap<>(); //id of property, houses sold
        HashMap<Integer, Integer> hotelSaleIds = new HashMap<>(); //id of property, houses sold

        while( restart ){
            sale = 0;
            boolean yesNo;
            while( sale < amount ) {
                System.out.println("You need to raise: " + (amount - sale));

                boolean mortgaging = yesNoInput("Mortgage properties?");
                if(mortgaging){
                    boolean goAgain = true;
                    while( goAgain ){
                        //select property
                        int count = printUnmortgagedProperties(); //prints all valid choices
                        if( count > 0 ) {
                            System.out.println("Choose a property to mortgage");
                            tempProperty = selectProperty();
                            //mortgage property temporarily
                            sale += mortgageProperty( mortgageIds, tempProperty ); //update sale total
                            goAgain = yesNoInput("Select more properties to mortgage? (yes/no)?");
                        } else {
                            System.out.println("No developed properties");
                            goAgain = false;
                        }
                    }
                }

                boolean improvements = yesNoInput("Sell improvements?");
                if(improvements){
                    boolean goAgain = true;
                    while( goAgain ){
                        //select property
                        int count = printDevelopedProperties(houseSaleIds, hotelSaleIds);
                        if( count > 0 ) {
                            System.out.println("Choose a property to sell improvements on");
                            tempProperty = selectProperty(); //prints all valid choices
                            //sell improvements on chosen property
                            sale += sellImprovement( houseSaleIds, hotelSaleIds, tempProperty); //update sale total
                            goAgain = yesNoInput("Select more improvements (yes/no)?");
                        } else {
                            System.out.println("No developed properties");
                            goAgain = false;
                        }
                    }
                }

                boolean tiles = yesNoInput("Sell tiles?");
                if(tiles){
                    //sell properties
                    System.out.println("Choose an asset to sell:");
                    choice = printChooseAsset(saleIds, houseSaleIds, mortgageIds);
                    confirm = yesNoInput("Are you sure (yes/no)?");
                    if( confirm ){
                        sale += saleIds.get(choice); //update sale total
                    }
                }
            }
            System.out.println("Selected improvements to sell:");
            System.out.println("Hotels:");
            System.out.println(hotelSaleIds.toString());
            System.out.println("Houses");
            System.out.println(houseSaleIds.toString());
            System.out.println("Board Tiles");
            System.out.println(saleIds.toString());
            yesNo = yesNoInput("Finalise selection? (no will start process again)");
            if( yesNo ){
                //selection finalised
                //mortgage properties
                for(int ii = 0; ii < assets.size(); ii++){
                    Object asset = assets.get(ii);
                    if( asset instanceof Property ){
                        if( mortgageIds.containsKey(((Property) asset).iD)){
                            payPlayerAmount(((Property) asset).mortgageProperty());
                        }
                    }
                }
                //sell improvement
                for(int ii = 0; ii < assets.size(); ii++){
                    Object asset = assets.get(ii);
                    if( asset instanceof Property){
                        if( hotelSaleIds.containsKey(((Property) asset).iD)) {
                            payPlayerAmount(((Property) asset).sellHouseOrHotel());
                        }
                        if( houseSaleIds.containsKey(((Property)asset).iD) ) {
                            //sell appropriate number of houses
                            for( int jj = 0; jj < houseSaleIds.get(((Property) asset).iD); jj++){
                                payPlayerAmount(((Property) asset).sellHouseOrHotel());
                            }
                            payPlayerAmount(((Property) asset).sellHouseOrHotel());
                        }

                    }
                }
                //sell assets and improvements
                for(int ii = 0; ii < assets.size(); ii++){
                    Object asset = assets.get(ii);
                    if( !( asset instanceof GetOutOfJail)){
                        if( saleIds.containsKey(((BoardTile)asset).iD) ) {
                            if (asset instanceof Property) {
                                payPlayerAmount(((Property) asset).sellProperty());
                            } else if (asset instanceof Station) {
                                payPlayerAmount(((Station) asset).sellStation());
                            } else if (asset instanceof Utility) {
                                payPlayerAmount(((Utility) asset).sellUtility());
                            }
                        }
                    }
                }
                //update flags and rent values
                completeSetProperties();
                restart = false;
            }
        }
    }

    /**
     * prints properties that can mortgaged
     *
     * @return count of un-mortgaged properties
     */
    private int printUnmortgagedProperties() {
        int count = 0;

        for( Object asset: assets ){
            if( asset instanceof Property ){
                if( !((Property) asset).getDeveloped() && !((Property) asset).isMortgaged() ){
                    System.out.println( ((Property) asset).iD + " " + ((Property) asset).title);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Temporarily mortgages a property
     *
     * @param mortgageIds store of properties temporarily mortgaged
     * @param property property requested to be mortgaged
     * @return return money generated from mortgaging property
     */
    private int mortgageProperty( HashMap<Integer, Integer> mortgageIds, Property property ) {

        int cost = 0;

        if( !mortgageIds.containsKey(property.iD) && !property.isDeveloped()){
            mortgageIds.put(property.iD, ((property.getCost())/2));
            cost = (property.getCost())/2;
        }

        return cost;
    }


    /**
     * A method to sell a house or hotel in such a way as to maintain the balance of houses and hotels across a
     * property group
     * This method should be passed a property where sales are potentially possible
     *
     * @param houseSaleIds, temporary sale store
     * @param property, a developed property
     * @return cost the integer sale price of the improvement selected to be sold
     */
    private int sellImprovement( HashMap<Integer, Integer> houseSaleIds, HashMap<Integer, Integer> hotelSaleIds, Property property ) {
        if (!property.getDeveloped()) {
            System.out.println("Error in argument");
            throw new IllegalArgumentException();
        }
        int cost = 0;
        //find property group
        ArrayList<Property> group = findGroups(property);

        boolean houseSalePossible = true;
        int houseNo = property.getHousesNo() - houseSaleIds.get(property.iD);
        int hotelNo = property.getHotelNo() - hotelSaleIds.get(property.iD);

        if( houseNo != 0){
            //is house sale possible?
            for( Property pp : group){
                //find house number for properties in group
                if( (pp.getHousesNo() - houseSaleIds.get(pp.iD)) > houseNo ){
                    //a property in the group has a theoretical higher house number than the current property
                    //improvements cannot be sold as this would create an imbalance
                    houseSalePossible = false;
                    break;
                }
            }
        }

        if(houseSalePossible){
            //update count of houses sold
            Integer tempCount = houseSaleIds.get( property.iD );
            if (tempCount == null) {
                houseSaleIds.put(property.iD, 1);
            } else {
                houseSaleIds.put(property.iD, tempCount++);
            }
            cost = property.getGroup().getBuildingCost();
        } else if( hotelNo == 1){
            //update count of hotels sold
            hotelSaleIds.put(property.iD, 1);
            cost = property.getGroup().getBuildingCost();
        } else {
            System.out.println("Improvement cannot be sold on this property");
        }

        return cost;
    }

    /**
     * Finds all properties with the same group as the passed property in the player's asset list
     *
     * @param property, a property tile on the board
     */
    private ArrayList<Property> findGroups( Property property ) {
        ArrayList<Property> group = new ArrayList<>();
        //find property group
        for( Object asset: assets){
            if(asset instanceof Property){
                if(((Property) asset).getGroup() == property.getGroup()){
                    group.add((Property) asset);
                }
            }
        }

        return group;
    }


    /**
     * Text based method for displaying developed property choices to the user where it is possible to sell more houses
     * or hotels
     *
     * @param houseSaleIds, temporary store of all houses sold on assets
     * @param hotelSaleIds, temporary store of all hotels sold on assets
     */
    private int printDevelopedProperties( HashMap<Integer, Integer> houseSaleIds, HashMap<Integer, Integer> hotelSaleIds ) {
        int count = 0;
        System.out.println("Developed Properties:");
        //loop through assets, finding all properties that are developed
        for(Object asset : assets) {
            if(asset instanceof Property) {
                if( ((Property) asset).getDeveloped()){
                    if( ((Property) asset).getHousesNo() != houseSaleIds.get(((Property) asset).iD) &&
                            hotelSaleIds.get(((Property) asset).iD) != ((Property) asset).getHotelNo()){
                        System.out.println(((Property) asset).iD + " " + ((Property) asset).title + " houses:" +
                                ((Property) asset).getHousesNo() + " hotel:" + ((Property) asset).getHotelNo());
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Prints all assets that can be sold and allows the user to select one
     *
     * @param saleIds, temporary store of already selected assets
     * @param houseSaleIds, temporary store of already selected assets where improvements have been sold
     * @return id of chosen asset
     */
    private int printChooseAsset(HashMap<Integer, Integer> saleIds, HashMap<Integer, Integer> houseSaleIds, HashMap<Integer,Integer> mortgageIds) {

        System.out.println("Saleable Assets");
        for( Object asset : assets ){
            if(!( asset instanceof GetOutOfJail)){
                if( !saleIds.containsKey(((BoardTile) asset).getiD()) ){
                    if( asset instanceof Property ){
                        if( !((Property) asset).getDeveloped() && !mortgageIds.containsKey(((Property) asset).iD)){
                            //can only sell properties with no improvements and that haven't been selected for mortgage
                            System.out.println( ((BoardTile) asset).iD + " " + ((BoardTile) asset).title);
                        } else {
                            if( houseSaleIds.get(((Property) asset).iD) == ((Property) asset).getHousesNo() ||
                                    houseSaleIds.get(((Property) asset).iD) == 5){
                                System.out.println( ((BoardTile) asset).iD + " " + ((BoardTile) asset).title);
                            }
                        }
                    } else{
                        System.out.println( ((BoardTile) asset).iD + " " + ((BoardTile) asset).title);
                    }
                }
            }
        }
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        boolean valid = false;
        int decision = 0;

        while(!valid){
            boolean found = false; //flag for if given property found
            System.out.println("Please enter an asset ID: ");
            decision = userInputScanner.nextInt(); //fetch user input

            for( Object asset : assets ){
                if(!( asset instanceof GetOutOfJail)){
                    if( decision == ((BoardTile) asset).iD ){
                        if( asset instanceof  Property){
                            saleIds.put( decision, ((Property) asset).getCost());
                        }else if( asset instanceof  Station){
                            saleIds.put( decision, ((Station) asset).getCost());
                        }else if( asset instanceof Utility){
                            saleIds.put( decision, ((Utility) asset).getCost());
                        }
                        found = true;
                    }
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

        return decision;

    }

    /**
     * Processes player request to leave the game
     */
    public void leaveGame() {
        //ask user
        if (!isAiAgent()) {//ask human players only
            boolean confirm = yesNoInput("Do you want to leave the game?");
            boolean possible;

            //get user votes
            if (confirm) {
                possible = board.getLeaveVotes(this);
                if (possible) {
                    bankrupt(); //remove from turn order and transfer all assets
                }
            }
        }
    }

    /*
     ---------Artificial Agent Methods---------
     For jail decisions see jail method
     */

    public Trait getPersonality() {
        return personality;
    }

    /**
     * Cautious personality cost verses network assessment
     *
     * @param cost amount of money to decide about
     * @return true|false spend|don't spend
     */
    public boolean cautiousWillBuy(int cost) {
        double threshold = cost;
        double cash = money;
        threshold = 1 - (cost / cash);//cost/cash = 1 when cost=cash
        double roll = ThreadLocalRandom.current().nextDouble(0, 1);
        if (roll < threshold) {
            //When net worth is high and cost is low threshold will be almost 1 and random number will likely be lower
            //System.out.println("threshold = " + threshold + " roll = " + roll);
            return true;
        }
        //System.out.println("threshold = " + threshold + " roll = " + roll);
        return false;//When cost is high and net worth is low threshold is low and random number will likely be higher
    }

    /**
     * Property purchase decision for AI
     *
     * @param property property to decide based on
     * @return true|false buy|do not buy
     */
    public boolean decide(Property property) {
        if (money < property.getCost()) {
            return false;//AI cannot afford the property
        } else if (money > 3000) {//TODO tune this value, requires testing
            return true;//player has a lot of assets always buy
        } else if (personality.hasTwoSetAffinity()) {

            if (property.getGroup() == Group.BROWN || property.getGroup() == Group.DEEP_BLUE) {
                return true;//buy this property AI has affinity for it
            }
        } else if (personality.isPlanner()) {
            if (personality.getGroupA() == null) {
                personality.setGroupA(property.getGroup());//assign purchased property group to one of groups to track
                return true; //purchase property
            } else if (personality.getGroupB() == null) {
                personality.setGroupB(property.getGroup());//assign purchased property group to one of groups to track
                return true; //purchase property

            } else if (personality.getGroupC() == null) {
                personality.setGroupC(property.getGroup());//assign purchased property group to one of groups to track
                return true; //purchase property
            } else {
                Group propGroup = property.getGroup();
                if (propGroup == personality.getGroupA() || propGroup == personality.getGroupB() || propGroup == personality.getGroupC()) {
                    return true;//property is a member of AIs plan groups so buy tile
                }
            }
        } else if (personality.isCautious()) {
            return cautiousWillBuy(property.getCost());//performs cost against net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;//player has money buy
        }
        return false;
    }

    /**
     * AI purchase decision on train stations
     *
     * @param station
     * @return
     */
    public boolean decide(Station station) {
        if (money < station.getCost()) {
            return false;//cannot afford tile
        } else if (personality.hasTrainAffinity()) {
            return true;//player has affinity for stations
        } else if (personality.isCautious()) {
            return cautiousWillBuy(station.getCost());//cost net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;//player has money buy
        }
    }

    /**
     * AI purchase decision on train stations
     *
     * @param utility
     * @return
     */
    public boolean decide(Utility utility) {
        if (money < utility.getCost()) {
            return false;//cannot afford tile
        } else if (personality.hasUtilityAffinity()) {
            return true;//player has affinity for utilities
        } else if (personality.isCautious()) {
            return cautiousWillBuy(utility.getCost());//cost net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;//player has money buy
        }
    }

    /**
     * Trade decision making
     *
     * @param give    what the AI will give
     * @param receive what the AI will receive
     * @return true|false accept|decline
     */
    public boolean decide(LinkedList<Object> give, LinkedList<Object> receive) {
        return false;//TODO currently rejects all trades
    }

    /**
     * AI decision to pay £10 tax or draw opportunity knocks
     *
     * @return tax = true, draw = false
     */
    public boolean payTaxOrDraw() {
        if (money < 10) {
            return false;//Can't afford to pay draw a card
        } else if (personality.isCautious()) {
            return cautiousWillBuy(10);//based on net worth cost ratio
        } else if (personality.isWildCard()) {
            return false;//wild cards take chances
        }
        if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip for remaining personalities
            return true;
        } else {
            return false;
        }
    }

    /**
     * AI development decision
     *
     * @return
     */
    public boolean decideDevelop() {
        if (personality.isInvestor()) {
            return true;//investors will always attempt to develop
        } else if (personality.isCautious()) {
            return cautiousWillBuy(125);//cost verses net worth using average building cost
        } else {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip for remaining personalities
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Property developing procedure for AI
     */
    public void agentDevelopProperties() {
        LinkedList<Property> properties = new LinkedList<>();
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).isCompletedSet() && ((Property) item).getHotelNo() != 1) {
                    properties.add((Property) item);//add development ready tiles
                }
            }
        }
        boolean loop;
        if (personality.isInvestor()) {
            loop = true;
            boolean housePurchased;
            boolean hotelPurchased;
            while (loop && money > properties.peekFirst().getGroup().getBuildingCost()) {//while can develop
                hotelPurchased = false;
                housePurchased = false;
                for (Property p : properties) {
                    if (p.purchaseHouse()) {
                        housePurchased = true;
                        break;//building purchased exit loop
                    } else if (p.purchaseHotel()) {
                        hotelPurchased = true;
                        break;//building purchased exit loop
                    }
                }
                if (!hotelPurchased && !housePurchased) {
                    loop = false;//failed to buy house or hotel end development
                } else if (money < 100) {
                    loop = false;
                }
            }
        } else if (personality.isCautious()) {
            while (cautiousWillBuy(properties.peekFirst().getGroup().getBuildingCost())) {//while willing to spend
                for (Property p : properties) {
                    if (p.purchaseHouse()) {
                        break;//building successfully purchased end for loop
                    } else if (p.purchaseHotel()) {
                        break;//building successfully purchased end for loop
                    }
                }
            }
        } else {
            //wild card and no trait random actions
            while (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5 && money > properties.peekFirst().getGroup().getBuildingCost()) {//while coin flip successful
                for (Property p : properties) {//attempt to build 1 building
                    if (p.purchaseHouse()) {
                        break;//building successfully purchased end for loop
                    } else if (p.purchaseHotel()) {
                        break;//building successfully purchased end for loop
                    }
                }
            }
        }
    }

    /**
     * Sell building for AI agent.
     * When provided a developed property group buildings will be sold within building restrictions
     *
     * @param group property group with buildings to sell
     * @return the amount from the sale
     */
    public int agentSellBuilding(Group group) {
        int houseCount = 0;
        Property hasMostHouses = null;
        for (Object item : assets) {
            if (item instanceof Property && ((Property) item).getGroup() == group) {//property of desired
                if (((Property) item).getHotelNo() == 1) {
                    return ((Property) item).sellHouseOrHotel();//sell hotel
                } else {
                    if (houseCount < ((Property) item).getHousesNo()) {
                        houseCount = ((Property) item).getHousesNo();
                        hasMostHouses = (Property) item;
                    }
                }
            }
        }
        if (hasMostHouses != null) {
            return hasMostHouses.sellHouseOrHotel();
        } else {
            return 0;//nothing was sold
        }
    }

    /**
     * //TODO add priority order based on traits
     * Sell a single item to raise funds
     *
     * @return amount raised from sale
     */
    public int agentSellItem() {
        //Sell utilities
        for (Object item : assets) {
            if (item instanceof Utility) {
                return ((Utility) item).sellUtility();
            }
        }
        //sell stations
        for (Object item : assets) {
            if (item instanceof Station) {
                return ((Station) item).sellStation();
            }
        }
        //sell non set properties
        for (Object item : assets) {
            if (item instanceof Property) {
                if (!((Property) item).isCompletedSet()) {
                    if (((Property) item).getOwner() == null) {
                        System.out.println(((Property) item).title);
                    }
                    return ((Property) item).sellProperty();
                }
            }
        }
        //sell buildings
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).isCompletedSet()) {
                    int buildingSaleAmount = agentSellBuilding(((Property) item).getGroup());//try to sell building
                    if (buildingSaleAmount != 0) {
                        return buildingSaleAmount;//building sold return amount
                    }
                }
            }
        }
        //sell set properties
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).isCompletedSet()) {
                    int propertyAmount = ((Property) item).sellProperty();//sell property
                    completeSetProperties();
                    return propertyAmount;
                }
            }
        }
        return 0;//Could not sell item
    }

    public void agentSellAssets(int amount) {
        System.out.println("Raise £" + amount + " funds");
        int amountRaised = 0;//amount from selling assets
        int count = 0;
        while (amountRaised < amount) {
            count++;
            amountRaised += agentSellItem();
            if (count > 200) {//TODO remove after asset selling bug found
                for (Object item : assets) {
                    if (item instanceof Property) {
                        System.out.println(((Property) item).getTitle());
                    } else if (item instanceof Station) {
                        System.out.println(((Station) item).getTitle());
                    } else if (item instanceof Utility) {
                        System.out.println(((Utility) item).getTitle());
                    }
                }
                System.exit(10);//Asset selling issue
            }
        }
        payPlayerAmount(amountRaised);//give AI agent amount raised
        completeSetProperties();
        System.out.println("Amount raised £" + amountRaised);
    }
}