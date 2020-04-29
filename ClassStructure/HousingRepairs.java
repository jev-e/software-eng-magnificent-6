package ClassStructure;

import java.util.LinkedList;

/**
 * House repair card event
 */
public class HousingRepairs extends CardEffect {


    private int houseFine;//Fine amount per house
    private int hotelFine;//Fine amount per hotel

    /**
     * Default constructor for Jackson
     */
    public HousingRepairs() {

    }

    /**
     * Housing repair card
     * @param cardText text to be shown on activation
     * @param houseFine fine amount per house owned
     * @param hotelFine fine amount per hotel owned
     */
    public HousingRepairs(String cardText, int houseFine, int hotelFine) {
        this.cardText = cardText;
        this.houseFine = houseFine;
        this.hotelFine = hotelFine;
    }

    /**
     * On activation iterates through the players assets counting the number of houses and hotels
     * The player is fined an amount per hotel and house
     * @param currentPlayer player who drew the card
     */
    @Override
    void effect(Player currentPlayer) {
        int houseCount = 0;
        int hotelCount = 0;
        int amount;
        LinkedList<Object> assets = currentPlayer.getAssets();// get players asset list
        for(Object item: assets) {//Count number of houses and hotels a player owns
            if(item instanceof Property) {//checks the asset is a property
                hotelCount += ((Property) item).getHotelNo();
                houseCount += ((Property) item).getHousesNo();
            }
        }
        amount = (hotelCount*hotelFine) + (houseCount*houseFine);//calculate the total fine
        //note potentially add to card text here the amount that the player is charged
        currentPlayer.deductAmount(amount);//deduct amount from players balance
    }
    public int getHouseFine() {
        return houseFine;
    }

    public void setHouseFine(int houseFine) {
        this.houseFine = houseFine;
    }

    public int getHotelFine() {
        return hotelFine;
    }

    public void setHotelFine(int hotelFine) {
        this.hotelFine = hotelFine;
    }
}
