package ClassStructure;

import java.util.Scanner;

/**
 * @author Ayman Bensreti, Calvin Boreham
 * Structure for property objects
 */
public class Property extends BoardTile{
    private Group group;//enum colour group the tile belongs to
    private int cost;//amount to be paid to purchase property. This is also the sell value
    private int rent;//default rent of a property which developments are added to
    private int[] buildingRents; //4 int length array, amounts to increase rent by per house [1 House,2 Houses,3 Houses, 4 Houses]
    private int housesNo;
    private int hotelNo;
    private int hotelRent;//A given property may have only one hotel this functions the same as building rent
    private Player owner;
    private boolean mortgaged;//if property is mortgaged no rent is deducted. Can be restored by paying half the cost to the bank
    private boolean completedSet;//If a single player owns all properties in group this is true and false otherwise
    private boolean rentDoubled; //Flag representing whether the rent on this property has been doubled
    private boolean developed; //flag for if property improved

    /**
     * Default constructor for Jackson
     */
    public Property() {
        canPurchase = true;
        mortgaged = false;
        completedSet = false;
        rentDoubled = false;
        developed = false;
        owner = null;
    }

    /**
     * Creation of new property
     * @param iD tile ID
     * @param title Property title
     * @param group colour group
     * @param cost tile cost
     * @param rent tile rent
     * @param buildingRents building rents array
     * @param owner player that owns the property
     */
    public Property(int iD, String title,Group group, int cost, int rent, int[] buildingRents,int hotelRent, Player owner) {
        this.iD = iD;
        this.title = title;
        this.group = group;
        this.cost = cost;
        this.rent = rent;
        this.buildingRents = buildingRents;
        this.hotelRent = hotelRent;
        this.owner = owner;
        this.canPurchase = true;
        this.mortgaged = false;
        this.completedSet = false;
        this.rentDoubled = false;
        this.housesNo = 0;
        this.hotelNo = 0;
    }

    /**
     * classStructure.Property activation functionality, both unowned and owned
     * @param currentPlayer land on player, either able to purchase or subject to rent
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        /*Involves checks for hotels and houses
         * if there is a full set
         * if the property is mortgaged
         * purchase functionality if not owned
         */
        //is there an owner?
        if( owner != null && owner != currentPlayer && !mortgaged && !owner.isInJail()){
            //there is an owner that is allowed to collect rent,so collect rent
            collectRent( currentPlayer );
        } else if( owner != currentPlayer ){
            //no owner, can possibly purchase property
            if( currentPlayer.CanBuy()){
                purchase( currentPlayer );
            }
        }
    }

    /**
     * Getter for number of houses on property
     * @return number of houses
     */
    public int getHousesNo() {
        return housesNo;
    }

    /**
     * Getter for number of hotels on property
     * @return number of hotels
     */
    public int getHotelNo() {
        return hotelNo;
    }

    /**
     * Getter for owner of property
     * @return owner player owner of property
     */
    public Player getOwner() { return owner; }

    /**
     * Getter for if property has been developed or not
     * @return boolean true if property developed, false otherwise
     */
    public boolean getDeveloped() { return developed; }


    /**
     * Function to sell houses and hotels on a property.
     * @return Either the cost of the building or 0 if their is no buildings to sell.
     */
    public int sellHouseOrHotel(){
        if( hotelNo == 1 ){
            hotelNo = 0;
            housesNo = 4;
            rent -= hotelRent;
            return( group.getBuildingCost() );
        } else if( housesNo > 0 ){
            rent -= buildingRents[housesNo -1];
            housesNo -= 1;
            if( housesNo == 0 ){
                developed = false;
            }
            return( group.getBuildingCost() );
        } else{
            return( 0 );
        }
    }

    /**
     * Function to allow for the ability to mortgage the property.
     * Will set the property to being mortgaged to stop rent being acquired
     * @return Half the value of the property.
     */
    public int mortgageProperty() {
        if ( developed ) {
            return (0);
        } else if( mortgaged ){
            return (0);
        } else {
            mortgaged = true;
            return( cost/2 );
        }
    }

    /**
     * method to un-mortgage the property
     */
    public void unmortgageProperty() {
        if( mortgaged && owner.getMoney() >= (cost/2)){
            owner.deductAmount(cost/2); //pay un mortgaging fee
            mortgaged = false; //update flag
        } else if( mortgaged && (owner.getMoney() < (cost/2))) {
            System.out.println("Sorry, you cannot afford to do this");
        } else{
            System.out.println("Property not currently mortgaged");
        }
    }

    /**
     * Function to sell the property. Will not be able to sell if property is developed, and will only sell at
     * half price when property is mortgaged.
     * @ 0 for developed properties, half of property cost if mortgaged and full property cost if not mortgaged.
     */
    public int sellProperty(){
        if ( developed ) {
            return (0);
        } else if( mortgaged ){
            owner.getAssets().remove( this );
            owner = null;
            mortgaged = false;
            return(this.cost/2);
        } else {
            owner.getAssets().remove( this );
            owner = null;
            return (this.cost);
        }
    }

    /**
     * Runs an auction for the players to purchase the property. Each player makes an (optional) bid and the highest
     * bidding player purchases the property
     */
    private void auction( Player currentPlayer ) {
        System.out.println("==========================================================================================");
        System.out.println("==================================AUCTION BEGINS==========================================");
        System.out.println("==========================================================================================");
        Player highestBidder = null;
        int highestBid = 0;
        //for each player that isn't the current player
        for( Player bidder: board.turnOrder ) {
            if( bidder != currentPlayer && bidder.CanBuy() && !bidder.isInJail() ){
                int bid = 0;
                boolean wishToBid = false;
                boolean valid = false;

                //ask if want to purchase
                String question = bidder.getName() + ", do you want to make a bid (yes/no)?";
                wishToBid = yesNoInput( question );
                if( wishToBid ) {

                    Scanner userInputScanner = new Scanner( System.in );

                    while(!valid){

                        System.out.println("Please make a bid:");
                        bid = userInputScanner.nextInt();
                        if( bid > bidder.getMoney() ) {
                            System.out.println("Sorry, you have don't have that much money. Please bid again or enter 0 to cancel bid");
                        } else {
                            valid = true;
                        }

                    }

                    if( bid > highestBid ){
                        highestBidder = bidder;
                        highestBid = bid;
                    }
                }
            }
        }

        //auction complete, if we have any valid bids need to make the purchase
        if( highestBidder != null ){
            System.out.println( highestBidder.getName() + " has won the auction for " + title + " with a bid of £" + highestBid );
            highestBidder.deductAmount( highestBid );
            owner = highestBidder;
            highestBidder.addAsset(this);
            highestBidder.completeSetProperties(); //purchase made, update complete set flags

        } else {
            System.out.println( "No bids were made");
        }

        System.out.println("==========================================================================================");
        System.out.println("====================================AUCTION ENDS==========================================");
        System.out.println("==========================================================================================");
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
            decision.toLowerCase(); //normalise

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
     * Asks current player is they wish to purchase property, if so the ownership is transferred and purchase amount
     * deducted from player, if not an auction is triggered
     *
     * @param currentPlayer the player currently on property tile
     */
    private void purchase( Player currentPlayer) {

        boolean wishToPurchase = false; //flag for purchase choice

        String message = currentPlayer.getName() + ", do you want to make a purchase (yes/no)?";

        wishToPurchase = yesNoInput( message );
        System.out.println(wishToPurchase);
        if( wishToPurchase ){
            if( cost > currentPlayer.getMoney() ){
                //no purchase can be made, trigger auction
                System.out.println("Sorry, you can't afford this");
                auction(currentPlayer);
                System.out.println("Auction ended, can;t afford");
            } else {
                //deduct purchase cost from player
                currentPlayer.deductAmount( cost );
                //transfer ownership
                owner = currentPlayer;
                currentPlayer.addAsset(this);
                currentPlayer.completeSetProperties(); //purchase has been made, update complete set flags
                System.out.println("You have purchased " + title + " for £" + cost);
            }

        } else {
            //trigger auction
            auction( currentPlayer );
        }

    }

    /**
     * Doubles rent if the board is part of a complete set and has no improvements, halves rent if board is no longer
     * in a complete set
     */
    public void updateRent() {
        if( housesNo == 0 && hotelNo == 0 && completedSet ){
            rent *= 2;
            rentDoubled = true;
            System.out.println("Rent has been doubled");
        } else if( rentDoubled && !completedSet ){
            rent /= 2;
            rentDoubled = false;
            System.out.println("Rent has been reset");
        } else if ( rentDoubled && housesNo == 1 ){
            rent /= 2;
            rentDoubled = false;
            System.out.println("Rent has been reset");
        }
    }

    /**
     * Deducts rent amount for property from currentPlayer and pays rent amount to property owner
     *
     * @param currentPlayer the player currently on property tile
     */
    private void collectRent( Player currentPlayer) {
        System.out.println( currentPlayer.getName() + " pays rent to " + owner.getName());
        //deduct rent from current player
        int amountPayed = currentPlayer.deductAmount( rent ); //deduct amount from player
        //give owner rent
        owner.payPlayerAmount( amountPayed ); //gives owner amount renter was able to pay (e.g. if bankrupt)
    }

    /**
     * Increments housesNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds, updates rent amount
     *
     */
    public void purchaseHouse() {
        if( housesNo <= 4) {
            housesNo++;
            if( housesNo == 1 ){
                developed = true;
                updateRent();
            }
            owner.deductAmount( group.getBuildingCost() );
            rent += buildingRents[housesNo - 1];
            System.out.println("House purchased for " + title  + ", the new rent is: £" + rent);
        } else {
            System.out.println("house limit reached");
        }
    }

    /**
     * Increments hotelNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds
     */
    public void purchaseHotel() {
        if( housesNo == 4) {
            //'sell' 4 houses
            hotelNo = 1;
            housesNo = 0;
            owner.deductAmount( group.getBuildingCost() );
            rent += hotelRent;
            System.out.println("Hotel purchased for " + title + ", the new rent is: £" + rent);
        } else if (hotelNo == 1){
            System.out.println("hotel limit reached");
        } else {
            System.out.println("not enough houses");
        }
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int[] getBuildingRents() {
        return buildingRents;
    }

    public void setBuildingRents(int[] buildingRents) {
        this.buildingRents = buildingRents;
    }

    public void setHousesNo(int housesNo) {
        this.housesNo = housesNo;
    }

    public void setHotelNo(int hotelNo) {
        this.hotelNo = hotelNo;
    }

    public int getHotelRent() {
        return hotelRent;
    }

    public void setHotelRent(int hotelRent) {
        this.hotelRent = hotelRent;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }

    public boolean isCompletedSet() {
        return completedSet;
    }

    public void setCompletedSet(boolean completedSet) {
        this.completedSet = completedSet;
    }

    public boolean isRentDoubled() {
        return rentDoubled;
    }

    public void setRentDoubled(boolean rentDoubled) {
        this.rentDoubled = rentDoubled;
    }

    public boolean isDeveloped() {
        return developed;
    }

    public void setDeveloped(boolean developed) {
        this.developed = developed;
    }
}