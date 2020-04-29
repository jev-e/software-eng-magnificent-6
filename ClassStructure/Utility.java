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
            //System.out.println(text);//temp for text version
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

        boolean wishToPurchase = false; //flag for purchase choice

        if (!currentPlayer.isAiAgent()) {
            wishToPurchase = false;//TODO Change this to get player choice from GUI
        } else {
            wishToPurchase = currentPlayer.decide(this);
        }
        if (wishToPurchase) {
            if (cost > currentPlayer.getMoney()) {
                //no purchase can be made, trigger auction
                //TODO GUI auction activated here
            } else {
                //deduct purchase cost from player
                currentPlayer.deductAmount(cost);
                //transfer ownership
                owner = currentPlayer;
                currentPlayer.addAsset(this);
                //System.out.println("You have purchased " + title + " for £" + cost);
            }
        } else {
            //trigger auction
            //TODO GUI auction activated here
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