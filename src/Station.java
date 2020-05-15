package src;

import javafx.scene.control.Label;

import java.util.LinkedList;

/**
 * Station tile events
 */
public class Station extends TileEffect {

    private int cost;
    private Player owner;

    /**
     * Default constructor for Jackson
     */
    public Station() {
        owner = null;
        canPurchase = true;
    }

    /**
     * Creates a new station tile
     *
     * @param iD    board position for station
     * @param title name of the station
     */
    public Station(int iD, String title) {
        this.owner = null;
        this.cost = 200;//all stations cost 200
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
    }

    /**
     * On land on options to purchase if the station is unowned
     * If station is owned a count for the number stations owned by the owner is done
     * Player that lands on tiles pays differing amounts depending on number of stations
     *
     * @param currentPlayer player to have amount deducted from
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.addAction("Landed on " + title);
        int stationCount = 0;
        int amount = 0;
        if (owner != null && owner != currentPlayer && !owner.isInJail()) {
            LinkedList<Object> assets = owner.getAssets();
            for (Object item : assets) {
                if (item instanceof Station) {
                    stationCount++;//count number of stations owned by this tiles owner
                }
            }
            switch (stationCount) {//selects the amount based on how many station owned
                case 1:
                    amount = 25;
                    break;
                case 2:
                    amount = 50;
                    break;
                case 3:
                    amount = 100;
                    break;
                case 4:
                    amount = 200;
                    break;
            }
            text = "Player owns " + stationCount + " station(s). You pay £" + amount;
            currentPlayer.addAction(text);//add text to log
            int payment = currentPlayer.deductAmount(amount);//take from current player
            owner.payPlayerAmount(payment);// gives the amount the player was able to pay

        } else {
            if (owner == null) {
                if (currentPlayer.CanBuy()) {
                    purchase(currentPlayer);
                }
            } else if (owner.isInJail()) {
                currentPlayer.addAction("Owner is in jail");//add text to log
            }
        }
    }


    /**
     * Asks current player is they wish to purchase property, if so the ownership is transferred and purchase amount
     * deducted from player, if not an auction is triggered
     *
     * @param currentPlayer the player currently on property tile
     */
    private void purchase(Player currentPlayer) {

        boolean wishToPurchase; //flag for purchase choice

        if (!currentPlayer.isAiAgent()) {
            wishToPurchase = super.board.getPurchase(title, cost, currentPlayer);
        } else {
            wishToPurchase = currentPlayer.decide(this);
        }
        if (wishToPurchase) {
            if (cost > currentPlayer.getMoney()) {
                //no purchase can be made, trigger auction
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
            super.board.callAuctionSetupScene(currentPlayer, this);
        }

    }

    /**
     * "sells" the asset, sets owner to null and removes asset from owners asset tree
     * @return cost
     */
    public int sellStation() {
        owner.removeAsset(this);
        return cost;
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
        super.tilePrice = new Label();
    }

    @Override
    public void setTilePrice() {
        super.tilePrice.setText("£" + String.valueOf(cost));
    }

    @Override
    public Label getTilePrice() {
        return super.tilePrice;
    }
}