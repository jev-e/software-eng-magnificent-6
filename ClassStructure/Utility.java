package ClassStructure;

import java.util.LinkedList;
import java.util.Scanner;

public class Utility extends TileEffect {

    private int cost;
    private Player owner;

    /**
     * Default constructor for Jackson
     */
    public Utility() {
        canPurchase = true;
        owner = null;
    }

    public Utility(int iD, String title) {
        this.owner = null;
        this.cost = 150;
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        if(owner != null && owner != currentPlayer && !owner.isInJail()) {
            String multiplier;
            int utilityCount = 0;
            int amount;//amount to be paid
            LinkedList<Object> assets = owner.getAssets();
            for (Object item : assets) {
                if (item instanceof Utility) {
                    utilityCount++;//fetches number of utilities owned by this tiles owner
                }
            }
            if (utilityCount == 1) {
                amount = 4 * (currentPlayer.getLastRoll());
                multiplier = "4";
            } else {
                amount = 10 * (currentPlayer.getLastRoll());
                multiplier = "10";
            }
            int payment = currentPlayer.deductAmount(amount);
            owner.payPlayerAmount(payment);//give owner the amount
            this.text = "You pay £" + amount + " resulting from roll |" + currentPlayer.getLastRoll() + "|" + "times multiplier "
                    + multiplier;
            System.out.println(text);//temp for text version
        }else{
            if(owner == null) {
                purchase(currentPlayer);
            }
        }
    }

    /**
     * "sells" the asset, sets owner to null and removes asset from owners asset tree
     * @return cost
     */
    public int sellUtility() {
        owner.removeAsset(this);
        return cost;
    }

    /**
     * Runs an auction for the players to purchase the property. Each player makes an (optional) bid and the highest
     * bidding player purchases the property
     *
     */
    private void auction( Player currentPlayer ) {
        System.out.println(title);
        System.out.println("==========================================================================================");
        System.out.println("==================================AUCTION BEGINS==========================================");
        System.out.println("==========================================================================================");
        Player highestBidder = null;
        int highestBid = 0;

        //for each player that isn't the current player
        for( Player bidder: super.board.turnOrder ) {
            if (bidder != currentPlayer && bidder.CanBuy()) {
                int bid = 0;
                boolean wishToBid = false;
                boolean valid = false;

                //ask if want to purchase
                String question = bidder.getName() + ", do you want to make a bid (yes/no)?";
                if (bidder.isAiAgent()) {
                    wishToBid = false;//TODO temp for testing to be removed
                    System.out.println("no");
                } else {
                    wishToBid = yesNoInput(question);
                }
                if (wishToBid) {

                    Scanner userInputScanner = new Scanner(System.in);

                    while (!valid) {

                        System.out.println("Please make a bid:");
                        bid = userInputScanner.nextInt();
                        if (bid > bidder.getMoney()) {
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

        boolean wishToPurchase; //flag for purchase choice

        String message = currentPlayer.getName() + ", do you want to make a purchase (yes/no)?";

        if (!currentPlayer.isAiAgent()) {
            wishToPurchase = yesNoInput(message);
        } else {
            wishToPurchase = currentPlayer.decide(this);
        }
        if (wishToPurchase) {
            if (cost > currentPlayer.getMoney()) {
                //no purchase can be made, trigger auction
                System.out.println("Sorry, you can't afford this");
                auction(currentPlayer);
            } else {
                //deduct purchase cost from player
                currentPlayer.deductAmount(cost);
                //transfer ownership
                owner = currentPlayer;
                currentPlayer.addAsset(this);
                System.out.println("You have purchased " + title + " for £" + cost);
            }
        } else {
            //trigger auction
            auction( currentPlayer );
        }

    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
