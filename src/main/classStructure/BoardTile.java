package classStructure;

/**
 * @author Ayman Bensreti, Calvin Boreham
 * Structure for board tile objects
 * classStructure.Property and Tile Effects
 */
public abstract class BoardTile {

    int iD;
    String title;
    boolean canPurchase;
    Board board;

    /**
     * Handles the desired effect for card or tiles
     */
    public abstract void activeEffect(Player currentPlayer);

    /**
     * classStructure.Board setter for use in board constructor
     *
     * @param board the game board the card will be linked to
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}