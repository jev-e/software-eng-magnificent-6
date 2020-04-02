package ClassStructure;

import java.util.Iterator;
import java.util.LinkedList;

public class Station extends TileEffect {
    private int cost = 200;
    private Player owner = null;
    private boolean mortgaged;

    public Station(int iD, String title) {
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
    }

    public String getTitle() {
        return this.title;
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

    public void activeEffect(Player currentPlayer) {
        int stationCount = 0;
        int amount = 0;
        if (this.owner != null) {
            LinkedList<Object> assets = this.owner.getAssets();
            Iterator var5 = assets.iterator();

            while(var5.hasNext()) {
                Object item = var5.next();
                if (item instanceof Station) {
                    ++stationCount;
                }

                switch(stationCount) {
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
                }

                int payment = currentPlayer.deductAmount(amount);
                this.owner.payPlayerAmount(payment);
                if (payment < amount) {
                    this.board.bankruptPlayer(currentPlayer);
                }
            }
        }

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
}

