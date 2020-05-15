package src;

import javafx.scene.control.Label;

/**
 * Jail tile effect player
 */
public class Jail extends TileEffect{

    /**
     * Default constructor for Jackson
     */
    public Jail() {
        canPurchase = false;
    }

    /**
     * Creates Jail tile
     * @param iD location of tile
     * @param title Name of tile
     * @param text Text to be displayed on tile activation
     */
    public Jail(int iD, String title, String text) {
        this.iD = iD;
        this.title = title;
        this.text = text;
        this.canPurchase = false;
    }

    /**
     * Handles the jail activation cases
     * if player is not jailed they are just visiting
     * if player's jail time is at 2 release them
     *
     * @param currentPlayer player that activated this tile
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        if (currentPlayer.isInJail()) {
            if (currentPlayer.getJailTime() == 2) {
                currentPlayer.leaveJail();//Frees the player after staying two turns in jail and resets jail time
                currentPlayer.addAction("You have left jail");
            } else {
                currentPlayer.serveJailTime();//Increases the jail time count
                currentPlayer.addAction("You are in jail");
                //move on with players turn they are still in jail
            }
        } else {
            currentPlayer.addAction(text);//add text to log
            //Otherwise player is just visiting
        }


    }

    /**
     * init the gui elements
     */
    @Override
    public void initGuiElements() {
        super.tileName = new Label();
    }
}
