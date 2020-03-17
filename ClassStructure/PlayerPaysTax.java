package ClassStructure;

public class PlayerPaysTax extends CardEffect {

    private int amount;

    public PlayerPaysTax(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.alterBalance(-amount);
        this.board.taxPot += amount;//add amount to tax pot
    }
}
