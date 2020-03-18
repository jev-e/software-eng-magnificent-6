package ClassStructure;

/**
 * Player pays an amount to the bank card event
 */
public class PlayerPaysBank extends CardEffect{
    private int amount;

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
        int payment = currentPlayer.deductAmount(amount);
        if(payment < amount) {
            this.board.bankruptPlayer(currentPlayer);//bankrupt current player for failure to pay full amount
        }
        System.out.println(cardText);//for testing
    }
}

