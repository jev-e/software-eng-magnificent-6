package ClassStructure;

/**
 * Pot luck tile
 */
public class PotLuck extends TileEffect {
    String currentCardText = "";//empty string by default holds the text of the last card that was drawn
    /**
     * Default constructor for Jackson
     */
    public PotLuck() {
        canPurchase = false;
        title = "Pot Luck";
        text = "Draw Pot Luck card";
    }

    /**
     * Creates new pot luck tile at given board location
     * @param iD tile position on board
     */
    public PotLuck(int iD) {
        this.title = "Pot Luck";
        this.iD = iD;
        this.canPurchase = false;
        this.text = "Draw Pot Luck card";
    }

    /**
     * Draws pot luck card and activates it's effect
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        CardEffect currentCard = this.board.drawPotLuck();
        currentPlayer.addAction(text);//add text to log
        currentCard.effect(currentPlayer);
    }

    /**
     * Gets the current card text from the tile to be displayed to the GUI
     *
     * @return text of card that was drawn
     */
    public String getCurrentCardText() {
        return currentCardText;
    }
}

