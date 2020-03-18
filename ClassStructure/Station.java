package ClassStructure;

import java.util.LinkedList;

/**
 * Station tile events
 */
public class Station extends TileEffect {

    private int cost;
    private Player owner;

    /**
     * Creates a new station tile
     * @param iD board position for station
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
     * @param currentPlayer player to have amount deducted from
     */
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
               int payment = currentPlayer.deductAmount(amount);//take from current player
               owner.alterBalance(payment);// gives the amount the player was able to pay
               if(payment < amount) {//if the player couldn't pay the full amount
                   this.board.bankruptPlayer(currentPlayer);//bankrupt player
               }
           }
        }else{
            //purchase logic
        }
    }
}
