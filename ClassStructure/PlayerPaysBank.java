package ClassStructure;

public class PlayerPaysBank extends CardEffect{
    private int amount;

    public PlayerPaysBank(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.alterBalance(-amount);
        System.out.println(cardText);
    }
}

