package src;

import javafx.scene.control.Label;

/**
 * Free Parking tile functionality
 */
public class FreeParking extends TileEffect{

    /**
     * Default constructor for Jackson
     */
    public FreeParking() {
        canPurchase = false;
        title = "Free Parking";
        text = "You collect the tax pot";
    }

    /**
     * Free Parking tile constructor
     * @param iD board position for tile
     */
     public FreeParking(int iD) {
        this.iD = iD;
        this.title = "Free Parking";//title constant
        this.text = "You collect the tax pot";//activation text constant
         this.canPurchase = false;
    }

    @Override
    public void initGuiElements() {
        super.tileName = new Label();
    }



    /**
     * On activation the tax bot amount is given to the player who landed on the tile
     * @param currentPlayer player who landed on the tile
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        int amount = this.board.taxPot;
        this.board.taxPot = 0;//resets tax pot
        currentPlayer.addAction(text + " £" + amount);//add text to action log
        currentPlayer.payPlayerAmount(amount);
        this.board.updateTaxPot();
    }
}
