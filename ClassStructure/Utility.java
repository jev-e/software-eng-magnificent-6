
package ClassStructure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Utility extends TileEffect {
    private int cost = 150;
    private Player owner = null;
    private boolean mortgaged;

    public Utility(int iD, String title) {
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
        this.mortgaged = false;
    }

    public int getCost() {
        return this.cost;
    }

    public void returnToBank() {
        this.owner = null;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int mortgage() {
        if (this.mortgaged) {
            return 0;
        } else {
            this.mortgaged = true;
            return this.cost / 2;
        }
    }

    public int sell() {
        if (this.mortgaged) {
            this.returnToBank();
            this.mortgaged = false;
            return this.cost / 2;
        } else {
            this.returnToBank();
            return this.cost;
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void activeEffect(Player currentPlayer) {
        if (this.owner != null) {
            Random rand = new Random();
            int utilityCount = 0;
            int die1 = rand.nextInt(6) + 1;
            int die2 = rand.nextInt(6) + 1;
            int result = die1 + die2;
            LinkedList<Object> assets = this.owner.getAssets();
            Iterator var10 = assets.iterator();

            while(var10.hasNext()) {
                Object item = var10.next();
                if (item instanceof Utility) {
                    ++utilityCount;
                }
            }

            String multiplier;
            int amount;
            if (utilityCount == 1) {
                amount = 4 * (die1 + die2);
                multiplier = "4";
            } else {
                amount = 10 * (die1 + die2);
                multiplier = "10";
            }

            currentPlayer.payPlayerAmount(-amount);
            this.owner.payPlayerAmount(amount);
            this.text = "You pay £" + amount + " resulting from roll |" + die1 + "|" + die2 + "| = " + result + "times multiplier " + multiplier;
        }

    }
}

