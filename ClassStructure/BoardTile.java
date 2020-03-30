package ClassStructure;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @Type(value = TileEffect.class, name = "tileEffect"),
        @Type(value = Property.class, name = "property")
})

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

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}