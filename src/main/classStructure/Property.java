package classStructure;

import java.util.Scanner;

/**
 * @author Ayman Bensreti, Calvin Boreham
 * Structure for property objects
 * @version 2.0
 * @u
 */
public class Property extends BoardTile{
    Group group;//enum colour group the tile belongs to
    int cost;//amount to be paid to purchase property. This is also the sell value
    int rent;//default rent of a property which developments are added to
    int[] buildingRents; //4 int length array, amounts to increase rent by per house [1 House,2 Houses,3 Houses, 4 Houses]
    int housesNo;
    int hotelNo;
    int hotelRent;//A given property may have only one hotel this functions the same as building rent
    Player owner;
    boolean mortgaged;//if property is mortgaged no rent is deducted. Can be restored by paying half the cost to the bank
    boolean completedSet;//If a single player owns all properties in group this is true and false otherwise
    boolean rentDoubled = false; //Flag representing whether the rent on this property has been doubled

    /**
     * Creation of new property
     * @param iD tile ID
     * @param title classStructure.Property title
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
        housesNo = 0;
        hotelNo = 0;
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
        if( owner != null && owner != currentPlayer ){
            //there is an owner, collect rent
            collectRent( currentPlayer );
        } else if( owner != currentPlayer ){
            //no owner, can possibly purchase property
            if( currentPlayer.CanBuy()){
                purchase( currentPlayer );
            }
        }
    }

    public int getHousesNo() {
        return housesNo;
    }

    public int getHotelNo() {
        return hotelNo;
    }

    /**
     * Clears the owner
     */
    public void returnToBank() {
        owner = null;
    }

    /**
     * Runs an auction for the players to purchase the property. Each player makes an (optional) bid and the highest
     * bidding player purchases the property
     *
     */
    private void auction( Player currentPlayer ) {
        System.out.println("==========================================================================================");
        System.out.println("==================================AUCTION BEGINS==========================================");
        System.out.println("==========================================================================================");
        Player highestBidder = null;
        int highestBid = 0;

        //for each player that isn't the current player
        for( Player bidder: super.board.turnOrder ) {
            if( bidder != currentPlayer && bidder.CanBuy() ){
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
            highestBidder.deductAmount( cost );
            owner = highestBidder;
            highestBidder.addAsset(this);
            board.completeSetProperties(highestBidder); //purchase made, update complete set flags

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
     * Asks current player is they wish to purchase property, if so the ownership is transferred and purchase amount
     * deducted from player, if not an auction is triggered
     *
     * @param currentPlayer the player currently on property tile
     */
    private void purchase( Player currentPlayer) {

        boolean wishToPurchase = false; //flag for purchase choice

        String message = currentPlayer.getName() + ", do you want to make a purchase (yes/no)?";

        wishToPurchase = yesNoInput( message );

        if( wishToPurchase ){
            if( cost > currentPlayer.getMoney() ){
                //no purchase can be made, trigger auction
                System.out.println("Sorry, you can't afford this");
                auction( currentPlayer );
            }
            //deduct purchase cost from player
            currentPlayer.deductAmount( cost );
            //transfer ownership
            owner = currentPlayer;
            currentPlayer.addAsset(this);
            board.completeSetProperties( currentPlayer ); //purchase has been made, update complete set flags
            System.out.println("You have purchased " + title + " for £" + cost);
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
        } else if (rentDoubled && completedSet && housesNo == 1){
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
        currentPlayer.deductAmount( rent );
        //give owner rent
        owner.payPlayerAmount( rent );
    }

    /**
     * Increments housesNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds, updates rent amount
     *
     * @param currentPlayer, the player purchasing the house
     */
    public void purchaseHouse( Player currentPlayer) {
        if( housesNo <= 4) {
            housesNo++;
            currentPlayer.deductAmount( group.getBuildingCost() );
            rent += buildingRents[housesNo - 1];
            System.out.println("House purchased for " + title  + ", the new rent is: £" + rent);
        } else {
            System.out.println("house limit reached");
        }
    }

    /**
     * Increments hotelNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds
     *
     * @param currentPlayer, the player purchasing the house
     */
    public void purchaseHotel( Player currentPlayer ) {
        if( housesNo == 4) {
            //'sell' 4 houses
            hotelNo = 1;
            housesNo = 0;
            currentPlayer.deductAmount( group.getBuildingCost() );
            rent += hotelRent;
            System.out.println("Hotel purchased for " + title + ", the new rent is: £" + rent);
        } else if (hotelNo == 1){
            System.out.println("hotel limit reached");
        } else {
            System.out.println("not enough houses");
        }
    }




}