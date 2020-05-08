package ClassStructure;

/**
 * Go tile
 */
public class Go extends TileEffect {

    /**
     * Go tile is always in the first position
     */
     public Go() {
        this.title = "Go";
        this.canPurchase = false;
        this.iD = 0;//Is always the first tile
         this.text = "Go";
    }

    /**
     * Currently does nothing however maybe used to display some text to the user or trigger other events
     * @param currentPlayer player who landed on the tile
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.addAction(text);//add text to log
    }
}
