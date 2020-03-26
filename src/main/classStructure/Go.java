package classStructure;

/**
 * classStructure.Go tile
 */
public class Go extends TileEffect {

    /**
     * classStructure.Go tile is always in the first position
     */
     public Go() {
        this.title = "Go";
        this.canPurchase = false;
        this.iD = 0;//Is always the first tile
    }

    /**
     * Currently does nothing however maybe used to display some text to the user or trigger other events
     * @param currentPlayer player who landed on the tile
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        //currently does nothing potential to add text prompt on landing
    }
}
