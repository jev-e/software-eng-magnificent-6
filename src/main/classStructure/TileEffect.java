package classStructure;

/**
 * @author Ayman Bensreti
 * 	Effects of non property tiles
 *
 */
public abstract class TileEffect extends BoardTile{

    String text;
    Board board;

    public void setBoard(Board board) {
        this.board = board;
    }
}
