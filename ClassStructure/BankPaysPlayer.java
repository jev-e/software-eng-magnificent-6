package ClassStructure;

public class BankPaysPlayer extends CardEffect {

    private int amount;

    public BankPaysPlayer(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.alterBalance(amount);
        System.out.println(cardText);
    }
}
