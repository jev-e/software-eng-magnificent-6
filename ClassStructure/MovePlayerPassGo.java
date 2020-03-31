package ClassStructure;

/**
 * Move player and perform a pass go check to conditionally collect £200
 * Used for any card labelled advance to
 */
public class MovePlayerPassGo extends CardEffect{

    int destination;

    /**
     * Default constructor for Jackson
     */
    public MovePlayerPassGo() {

    }

    /**
     * Moves player to location checking for pass go and collecting £200
     * @param cardText text to display on activation
     * @param destination tile ID to move player to
     */
    public MovePlayerPassGo(String cardText, int destination) {
        this.cardText = cardText;
        this.destination = destination;
    }

    /**
     * Move player to tile location and check if they pass Go
     * @param currentPlayer player who drew card and target to be moved
     */
    @Override
    void effect(Player currentPlayer) {
        currentPlayer.setCurrentPos(destination);
        //if current position is less than previous then the 0 position has been passed
        currentPlayer.passGo();
        System.out.println(cardText);//Display for test version
        board.tiles.get(destination).activeEffect(currentPlayer);
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}