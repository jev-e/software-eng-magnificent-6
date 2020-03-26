package classStructure;

import java.util.LinkedList;
import java.util.Random;

public class Utility extends TileEffect {

    private int cost;
    private Player owner;

    public Utility(int iD, String title) {
        this.owner = null;
        this.cost = 150;
        this.iD = iD;
        this.title = title;
        this.canPurchase = true;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        if(owner != null) {
            String multiplier;
            Random rand = new Random();
            int utilityCount = 0;
            int die1 = rand.nextInt(6)+1;
            int die2 = rand.nextInt(6)+1;
            int result = die1 + die2;
            int amount;//amount to be paid
            LinkedList<Object> assets = owner.getAssets();
            for(Object item : assets) {
                if(item instanceof Utility) {
                    utilityCount++;//fetches number of utilities owned by this tiles owner
                }
            }
            if(utilityCount == 1) {
                amount = 4 * (die1 + die2);
                multiplier = "4";
            }else{
                amount = 10 * (die1 + die2);
                multiplier = "10";
            }
            currentPlayer.payPlayerAmount(-amount);//take the amount from current player
            owner.payPlayerAmount(amount);//give owner the amount
            this.text  ="You pay £"+amount+" resulting from roll |"+die1+"|"+die2+"| = "+result + "times multiplier "
            + multiplier;
        }else{
            //purchase logic if required
        }
    }
}
