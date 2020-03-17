package ClassStructure;

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

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.setCurrentPos(destination);
    }
}
