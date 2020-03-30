package ClassStructure;

/**
 * Go to jail card event
 */
public class GoToJailCard extends CardEffect{

    /**
     * Default constructor for Jackson
     */
    public GoToJailCard() {
    }

    /**
     * Card that when drawn sends player to jail
     * @param cardText card text to display on activation
     */
    public GoToJailCard(String cardText) {
        this.cardText = cardText;
    }

    /**
     * Sends player to jail and updates their jailed indicator
     * Option to use get out of jail free card or to pay in player method
     * @param currentPlayer player to be sent to jail
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.jailPlayer();
    }
}
