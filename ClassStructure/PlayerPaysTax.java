package ClassStructure;

/**
 * Player pays amount to tax pot card event
 */
public class PlayerPaysTax extends CardEffect {

    private int amount;

    /**
     * Generic structure for cards that take an amount and add it to the tax pot
     * @param cardText text to be shown when card is activated
     * @param amount amount to be deducted from player and added to tax pot
     */
    public PlayerPaysTax(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    /**
     * on activation takes amount from player adding it to the tax pot
     * @param currentPlayer target player to have amount deducted from
     */
    @Override
    void effect(Player currentPlayer) {
        int payment = currentPlayer.deductAmount(amount);
        if(payment < amount) {
            this.board.bankruptPlayer(currentPlayer);//bankrupt player for failure to pay full amount
        }
        this.board.taxPot += payment;//add amount to tax pot
    }
}
