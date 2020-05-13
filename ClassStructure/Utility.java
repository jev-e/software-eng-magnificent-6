package ClassStructure;

import javafx.scene.control.Label;

import java.util.LinkedList;

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
        currentPlayer.addAction("Landed on " + title);
        if(owner != null && owner != currentPlayer && !owner.isInJail()) {
            String multiplier;
            int roll = currentPlayer.getLastRoll1() + currentPlayer.getLastRoll2();//combined roll amount
            int utilityCount = 0;
            int amount;//amount to be paid
            LinkedList<Object> assets = owner.getAssets();
            for (Object item : assets) {
                if (item instanceof Utility) {
                    utilityCount++;//fetches number of utilities owned by this tiles owner
                }
            }
            if (utilityCount == 1) {
                amount = 4 * (roll);//multiplier*roll
                multiplier = "4";
            } else {
                amount = 10 * (roll);//multiplier*roll
                multiplier = "10";
            }
            int payment = currentPlayer.deductAmount(amount);
            owner.payPlayerAmount(payment);//give owner the amount
            this.text = "You pay £" + amount + " resulting from roll |" + currentPlayer.getLastRoll1() + "|" + currentPlayer.getLastRoll2() + "|times multiplier "
                    + multiplier;
            currentPlayer.addAction(text);//add tile text to action log
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
     * Asks current player is they wish to purchase station, if so the ownership is transferred and purchase amount
     * deducted from player, if not an auction is triggered
     *
     * @param currentPlayer the player currently on the tile
     */
    private void purchase(Player currentPlayer) {

        boolean wishToPurchase; //flag for purchase choice

        if (!currentPlayer.isAiAgent()) {
            wishToPurchase = super.board.getPurchase(title, Integer.toString(cost));
        } else {
            wishToPurchase = currentPlayer.decide(this);
        }
        if (wishToPurchase) {
            if (cost > currentPlayer.getMoney()) {
                //TODO GUI auction activated here double check
                // No purchase can be made, trigger auction
                super.board.callAuctionSetupScene(currentPlayer, this);
            } else {
                //deduct purchase cost from player
                currentPlayer.deductAmount(cost);
                //transfer ownership
                owner = currentPlayer;
                currentPlayer.addAsset(this);
                currentPlayer.addAction("Purchased " + title + " for £" + cost);
            }
        } else {
            //TODO GUI auction activated here double check
            // Trigger auction
            super.board.callAuctionSetupScene(currentPlayer, this);
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

    /**
     * init the gui elements
     */
    @Override
    public void initGuiElements() {
        super.tileName = new Label();
    }
}