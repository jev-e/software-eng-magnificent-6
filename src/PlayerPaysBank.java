package src;

/**
 * Player pays an amount to the bank card event
 */
public class PlayerPaysBank extends CardEffect{
    private int amount;

    /**
     * Default constructor for Jackson
     */
    public PlayerPaysBank() {

    }

    /**
     * Player pays amount to the bank
     * @param cardText Text to be displayed on activation
     * @param amount amount to be deducted from player
     */
    public PlayerPaysBank(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    /**
     * On activation the amount is deducted from player
     * @param currentPlayer target player for deduction
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.addAction(cardText);//add text to log
        currentPlayer.deductAmount(amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

