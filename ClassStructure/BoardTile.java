package ClassStructure;
/**
 * @author Ayman Bensreti
 * an interface for board tile objects
 *
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