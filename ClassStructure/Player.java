package ClassStructure;
import java.util.*;

/**
 * @author Ayman Bensreti, Calvin Boreham
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
    private Board board;

    /**
     * Sets players name and token and initialises the players starting assets
     * @param name player name
     * @param token one of token enum
     */
    public Player(String name, Token token, Board board) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 10;//All references to money is in £'s
        assets = new LinkedList<>();
        canBuy = false;
        inJail = false;
        this.jailTime = 0;
        this.board = board;
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
     * it is imperative that it is checked that the player has enough money to purchase items such as houses before
     * this method is called
     * @param amount amount to be deducted
     * @return the amount of money the player was able to pay
     */
    public int deductAmount(int amount) {
        System.out.print(name + ":" + money + ":" + amount);
        int payableAmount = 0;
        if(money >= amount) {
            payPlayerAmount(-amount);
            payableAmount = amount;
        }else{
            //find player net worth
            int netWorth = netWorth();
            if( netWorth >= amount){
                //can pay after asset selling, trigger it
                //owner money will be updated
                assetSelling( amount - money );
                //pay amount
                payPlayerAmount(-amount);
                payableAmount = amount;
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

    public Board getBoard() { return board; }

    public void setBoard(Board board) { this.board = board; }


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
        setCurrentPos(10);
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
            System.out.println(name + " " + token.getSymbol());
            System.out.println("Type the number of the option you would like to select");
            System.out.println("1. Serve Jail time\n2. Pay £50 bail");
            if(getOutOfJail != null) {
                System.out.println("3. Use get out of jail free card");//only display option is player has card
            }
            choice = userIn.nextInt();
            switch (choice) {
                case 1:
                    //Do nothing player serves time
                    selected = true;
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
                    //find all houses in same group
                    ArrayList<Property> group = findGroups( toBeImproved );

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
     * User input driven method to allow for a property to be unmortgaged
     */
    public void unMortgage() {
        boolean mortgaged = false;
        for( Object asset : assets ){
            if( asset instanceof Property ){
                if( ((Property) asset).getDeveloped() ){
                    mortgaged = true;
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
                if( money >= (tempProperty.getCost()/2) ){
                    System.out.println(tempProperty.title + " has been un-mortgaged for " + tempProperty.getCost()/2);
                    tempProperty.unmortgageProperty();
                } else {
                    System.out.println("Sorry, you can't afford to do that");
                }
                goAgain = yesNoInput("Un-mortgage any other properties?");

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
                if( ((Property) asset).mortgaged ){
                    netWorth += (((Property) asset).cost/2);
                } else {
                    if( ((Property) asset).developed ){
                        //add resell value of all properties
                        if( ((Property) asset).housesNo >= 1){
                            netWorth += (((Property) asset).housesNo * ((Property) asset).group.getBuildingCost());
                        } else if( ((Property) asset).hotelNo == 1){
                            netWorth += (((Property) asset).group.getBuildingCost() * 5);
                        }
                    }
                    //property worth
                    netWorth += ((Property) asset).cost;
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
        System.out.println(name);
        //remove player from turnorder
        System.out.println("Before removal");
        board.turnOrder.remove( this );
        System.out.println("After removal");
        //transfer all assets to bank ownership
        for( Object asset : assets ){
            System.out.println("Assets removed");
            if( asset instanceof GetOutOfJail ){
                ((GetOutOfJail) asset).returnCard();
            } else if( asset instanceof Property){
                ((Property) asset).setOwner( null );
            } else if( asset instanceof Station){
                ((Station) asset).setOwner( null );
            } else if( asset instanceof Utility){
                ((Utility) asset).setOwner( null );
            }
            assets.remove( asset );
            System.out.println("Assets removed success");
        }
        System.out.println("After assets");
    }

    /**
     * loops through assets giving options to sell assets until amount is reached
     * it is assumed amount can be reached
     *
     * @param amount, the money amount that must be raised
     */
    private void assetSelling( int amount ) {
        int choice;
        Property tempProperty;
        boolean confirm;
        boolean restart = true;
        int sale;
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
                        int count = printUnmortgagedProperties();
                        if( count > 0 ) {
                            System.out.println("Choose a property to mortgage");
                            tempProperty = selectProperty();
                            //sell improvements on chosen property
                            sale += mortgageProperty( mortgageIds, tempProperty );
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
                            tempProperty = selectProperty();
                            //sell improvements on chosen property
                            sale += sellImprovement( houseSaleIds, hotelSaleIds, tempProperty);
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
                        sale += saleIds.get(choice);
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
                if( !((Property) asset).getDeveloped() && !((Property) asset).mortgaged ){
                    System.out.println( ((Property) asset).iD + " " + ((Property) asset).title);
                    count++;
                }
            }
        }

        return count;
    }



    private int mortgageProperty( HashMap<Integer, Integer> mortgageIds, Property property ) {

        int cost = 0;

        if( !mortgageIds.containsKey(property.iD) && !property.developed){
            mortgageIds.put(property.iD, (property.cost/2));
            cost = property.cost/2;
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
        if( !property.getDeveloped()){
            throw new IllegalArgumentException();
        }
        int cost = 0;
        //find property group
        ArrayList<Property> group = findGroups( property );

        boolean houseSalePossible = true;
        int houseNo =  property.housesNo - houseSaleIds.get(property.iD);
        int hotelNo = property.hotelNo - hotelSaleIds.get(property.iD);

        if( houseNo != 0){
            //is house sale possible?
            for( Property pp : group){
                //find house number for properties in group
                if( (pp.housesNo - houseSaleIds.get(pp.iD)) > houseNo ){
                    //a property in the group has a theoretical higher house number than the current property
                    //improvements cannot be sold as this would create an imbalance
                    houseSalePossible = false;
                    break;
                }
            }
        }

        if(houseSalePossible){
            Integer tempCount = houseSaleIds.get( property.iD );
            if (tempCount == null) {
                houseSaleIds.put(property.iD, 1);
            } else {
                houseSaleIds.put(property.iD, tempCount++);
            }
            cost = property.group.getBuildingCost();
        } else if( hotelNo == 1){
            hotelSaleIds.put(property.iD, 1);
            cost = property.group.getBuildingCost();
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
                if(((Property) asset).group == property.group){
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
                    if( ((Property) asset).housesNo != houseSaleIds.get(((Property) asset).iD) && hotelSaleIds.get(((Property) asset).iD) != ((Property) asset).hotelNo){
                        System.out.println(((Property) asset).iD + " " + ((Property) asset).title + " houses:" + ((Property) asset).housesNo + " hotel:" + ((Property) asset).hotelNo);
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
                        if( !((Property) asset).getDeveloped() && !mortgageIds.containsKey(((Property) asset).iD)){ //can only sell properties with no improvements and that haven't been selected for mortgagein
                            System.out.println( ((BoardTile) asset).iD + " " + ((BoardTile) asset).title);
                        } else {
                            if( houseSaleIds.get(((Property) asset).iD) == ((Property) asset).housesNo || houseSaleIds.get(((Property) asset).iD) == 5){
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
}