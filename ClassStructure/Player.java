package ClassStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Ayman Bensreti
 *	Player information
 */
public class Player {
    private int currentPos;
    private int previousPos;
    private String name;
    private Token token;
    private int money;
    private LinkedList<Object> assets;
    private boolean canBuy;
    private boolean inJail;//indicator to differ between players who are jailed or just visiting
    private int jailTime;//counter to indicate how many turns a player has spent in jail

    /**
     * Sets players name and token and initialises the players starting assets
     * @param name player name
     * @param token one of token enum
     */
    public Player(String name, Token token) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 1500;//All references to money is in £'s
        assets = new LinkedList<>();
        canBuy = false;
        inJail = false;
        this.jailTime = 0;
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
     * if assets will not cover payment the player is bankrupt
     * @param amount amount to be deducted
     * @return the amount of money the player was able to pay
     */
    public int deductAmount(int amount) {
        if(money > amount) {
            payPlayerAmount(-amount);
            return amount;
        }else{
            //check for ability to sell assets
            if(money > amount) {
                return amount;
            }
        }
        return money;//returns what the player can pay
    }

    /**
     * Adds an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card) to be added
     */
    public void addAsset(Object item) {
        assets.add(item);
    }

    /**
     * Removes an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card)
     */
    public void removeAsset(Object item) {
        assets.remove(item);
    }

    public String getName() {
        return name;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getMoney() {
        return money;
    }


    /**
     * Fetches the players assets list
     * @return a list of items(Property|Station|Utility|Card)
     */
    public LinkedList<Object> getAssets() {
        return assets;
    }

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
        setCurrentPos(0);//to be changed to position of jail
        for(Object item: assets) {//looks for a get out of jail free card
            if(item instanceof GetOutOfJail) {
                getOutOfJail = (GetOutOfJail) item;//if found getOutOfJail is set to this card
            }//If the player has two the first one is used
        }
        //Player choice here
        Scanner userIn = new Scanner(System.in);
        int choice;
        boolean selected = false;
        while (!selected) {//Repeat until an option is selected
            System.out.println("Type the number of the option you would like to select");
            System.out.println("1. Serve Jail time\n2. Pay £50 bail");
            if(getOutOfJail != null) {
                System.out.println("3. Use get out of jail free card");//only display option is player has card
            }
            choice = userIn.nextInt();
            switch (choice) {
                case 1:
                    //Do nothing player serves time
                    break;
                case 2:
                    if(money > 50) {//If the player has the funds take the amount and remove them from jail
                        deductAmount(50);
                        leaveJail();
                        selected = true;//note a selection has been made
                    }else{
                        System.out.println("Insufficient funds");
                    }
                    break;
                case 3:
                    if(getOutOfJail != null) {//check player has card for text version, will be impossible in GUI
                        getOutOfJail.playCard();//play get out of jail free card
                        selected = true;
                    }
                    break;
                default:
                    System.out.println("Invalid Input");
            }
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

    /**
     * Updates flags for all player's properties, updates base rent amount if complete set with no properties. Should
     * be called whenever a change to property ownership has occurred - e.g in purchase, sale, trading etc
     *
     */
    public void completeSetProperties() {
        HashMap<Group, Integer> count = new HashMap<>();

        for(Object asset : assets){
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
            for( Object asset: assets){
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
     * Generates a text based interface to initiate the choosing of assets for trading
     */
    public LinkedList<Object> tradeAssetSelection() {
        LinkedList<Object> tradeAssets = new LinkedList<>();
        Scanner userInputScanner = new Scanner( System.in ); //scanner for user input
        String assetChoice = null;
        boolean goAgain = true;
        boolean valid = false;
        boolean cancel = false;

        for( Object asset: assets ) {
            if( asset instanceof Property ){
                if( !((Property) asset).developed ){
                    System.out.println(((Property) asset).title);
                }
            } else if( !(asset instanceof GetOutOfJail) ){
                System.out.println(((BoardTile)asset).title);
            }
        }


        while( goAgain ) {
            while (!valid) {
                boolean found = false; //flag for if given property found
                System.out.println("Please enter an asset name or type cancel: ");
                assetChoice = userInputScanner.nextLine(); //fetch user input

                if( !(assetChoice.toLowerCase().equals("cancel"))){
                    for (Object asset : assets ) {
                        if (((BoardTile)asset).title.toLowerCase().equals(assetChoice.toLowerCase())) {
                            System.out.println("You have selected property: " + ((BoardTile) asset).title);
                            tradeAssets.add( asset );
                            found = true; //user input reflects a possible property
                        }
                    }
                } else {
                    cancel = true;
                }

                if( cancel ){
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
     * Drives the interactive menu for property improvement and contains logic
     * occurs at the end of the turn for the player
     *
     */
    public void propertyImprovement() {

        //ArrayList<Property> improvableProperties = new ArrayList<>();
        Scanner userInputScanner = new Scanner( System.in );
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

                    //init group store
                    ArrayList<Property> group = new ArrayList<>();
                    //find all houses in same group
                    for( Object asset : assets ) {
                        if (asset instanceof Property) {
                            if (((Property) asset).group == toBeImproved.group) {
                                group.add((Property) asset);
                            }
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
                        if( validPurchase && (money > toBeImproved.group.getBuildingCost())){
                            improve = yesNoInput("Do you want to purchase a hotel for £" + toBeImproved.group.getBuildingCost() + " (yes/no)?");
                            if( improve) {
                                toBeImproved.purchaseHotel(); //manages transaction
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
                        if( validPurchase && (money > toBeImproved.group.getBuildingCost())){
                            improve = yesNoInput("Do you want to purchase a house £" + toBeImproved.group.getBuildingCost() + " (yes/no)?");
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
                            if( ((Property) asset).hotelNo == 0  && ((Property) asset).completedSet){
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
                }
            }
        }
    }

    /**
     * Text based method for displaying improvable property choices to the user
     * //TODO convert into GUI method to only show improvable properties to user
     */
    private int improvableProperties() {
        int count = 0;
        System.out.println("Properties available for improvement:");
        //loop through assets, finding all properties
        for(Object asset : assets) {
            if(asset instanceof Property) {
                //if potentially improvable (part of complete set and doesn't have a hotel built and isn't currently mortgaged)
                if( ((Property) asset).completedSet  && ((Property) asset).hotelNo != 1 && !((Property) asset).mortgaged) {
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
}