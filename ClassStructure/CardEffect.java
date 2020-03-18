package ClassStructure;
/**
 * @author Ayman Bensreti
 *	Card generic structure
 */
public abstract class CardEffect {
    String cardText;
    Board board;//Reference to the game board to allow access to other game elements

    abstract void effect(Player currentPlayer);

    /**
     * Board setter for use in board constructor
     * @param board the game board the card will be linked to
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}
