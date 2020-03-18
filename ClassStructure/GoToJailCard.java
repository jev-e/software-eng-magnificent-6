package ClassStructure;

/**
 * Go to jail card event
 */
public class GoToJailCard extends CardEffect{

    private int jail;//Temporary

    /**
     * Card that when drawn sends player to jail
     * @param cardText card text to display on activation
     * @param jail for testing reference to ID of jail will be constant in full board version
     */
    public GoToJailCard(String cardText, int jail) {
        this.cardText = cardText;
        this.jail = jail;
    }

    /**
     * Sends player to jail and updates their jailed indicator
     * @param currentPlayer player to be sent to jail
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.setJailed(true);
        currentPlayer.setCurrentPos(jail);
    }
}
