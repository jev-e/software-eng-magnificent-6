package ClassStructure;
/**
 * @author Ayman Bensreti
 * Structure for board tile objects
 * Property and Tile Effects
 */
public abstract class BoardTile {

    int iD;
    String title;
    boolean canPurchase;

    /**
     * Handles the desired effect for card or tiles
     */
    public abstract void activeEffect(Player currentPlayer);
}