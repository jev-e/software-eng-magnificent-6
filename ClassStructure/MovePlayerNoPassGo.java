package ClassStructure;

/**
 * Move player to a provided location without performing a pass go check
 * used for any move backwards event or where the card specifies not to pass go or collect
 */
public class MovePlayerNoPassGo extends CardEffect{

    int destination;

    /**
     * Moves player to location without checking for pass go or collecting £200
     * @param cardText text to display on activation
     * @param destination tile ID to move player to
     */
    public MovePlayerNoPassGo(String cardText, int destination) {
        this.cardText = cardText;
        this.destination = destination;
    }

    /**
     * Moves player to tile location
     * @param currentPlayer player who drew the card and target to be moved
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.setCurrentPos(destination);
    }
}
