package ClassStructure;

public class MovePlayerPassGo extends CardEffect{

    int destination;

    /**
     * Moves player to location checking for pass go and collecting £200
     * @param cardText text to display on activation
     * @param destination tile ID to move player to
     */
    public MovePlayerPassGo(String cardText, int destination) {
        this.cardText = cardText;
        this.destination = destination;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.setCurrentPos(destination);
        //if current position is less than previous then the 0 position has been passed
        if(currentPlayer.getCurrentPos() < currentPlayer.getPreviousPos()) {
            currentPlayer.alterBalance(200);//collect £200 for passing go
        }
    }
}