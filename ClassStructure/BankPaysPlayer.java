package ClassStructure;

/**
 * @author Ayman
 * Card effect for cards where the player recieves money from the bank
 */
public class BankPaysPlayer extends CardEffect {

    private int amount;

    /**
     * Default constructor for Jackson
     */
    public BankPaysPlayer() {
    }


    /**
     * Bank payment constructor to player card event
     * @param cardText Text to be shown when card is activated
     * @param amount The amount to be given to the player
     */
    public BankPaysPlayer(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    /**
     * Active effect of card
     * @param currentPlayer The target player who drew the card
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.addAction(cardText);//add text to log
        currentPlayer.payPlayerAmount(amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
