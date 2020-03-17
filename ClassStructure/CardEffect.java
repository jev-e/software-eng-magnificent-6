package ClassStructure;
/**
 * @author Ayman Bensreti
 *	Card generic structure
 */
public abstract class CardEffect {
    String cardText;
    Board board;

    abstract void effect(Player currentPlayer);

    public void setBoard(Board board) {
        this.board = board;
    }
}
