package ClassStructure;

import java.util.LinkedList;

public class HousingRepairs extends CardEffect {

    private int houseFine;
    private int hotelFine;

    public HousingRepairs(String cardText, int houseFine, int hotelFine) {
        this.cardText = cardText;
        this.houseFine = houseFine;
        this.hotelFine = hotelFine;
    }

    @Override
    void effect(Player currentPlayer) {
        int houseCount = 0;
        int hotelCount = 0;
        int amount;
        LinkedList<Object> assets = currentPlayer.getAssets();
        for(Object item: assets) {//Count number of houses and hotels a player owns
            if(item instanceof Property) {
                hotelCount += ((Property) item).hotelNo;
                houseCount += ((Property) item).houseCost;
            }
        }
        amount = (hotelCount*houseFine) + (houseCount*houseFine);
        currentPlayer.alterBalance(-amount);//deduct amount from players balance
    }
}
