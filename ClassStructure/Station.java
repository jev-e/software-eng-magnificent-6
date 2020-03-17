package ClassStructure;

import java.util.LinkedList;

public class Station extends TileEffect {

    private int cost;
    private Player owner;

    public Station(int iD, String title) {
        this.owner = null;
        this.cost = 200;
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        int stationCount = 0;
        int amount = 0;
        if (owner != null) {
           LinkedList<Object> assets = owner.getAssets();
           for(Object item : assets) {
               if(item instanceof Station) {
                   stationCount++;//count number of stations owned by this tiles owner
               }
               switch (stationCount) {
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
               owner.alterBalance(amount);
               currentPlayer.alterBalance(-amount);//take from current player
           }
        }else{
            //purchase logic
        }
    }
}
