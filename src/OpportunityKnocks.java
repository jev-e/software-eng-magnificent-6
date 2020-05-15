package src;

import javafx.scene.control.Label;

/**
 * Opportunity Knocks tile event
 */
public class OpportunityKnocks extends TileEffect{
    String currentCardText = "";//empty string by default holds the text of the last card that was drawn
    /**
     * Default constructor for Jackson
     */
    public OpportunityKnocks() {
        canPurchase = false;
        title = "Opportunity Knocks";
        text = "Draw Opportunity Knocks card";
    }

    /**
     * Creates a new opportunity knocks tile spot
     * @param iD board location to place tile
     */
    public OpportunityKnocks(int iD) {
        this.title = "Opportunity Knocks";
        this.iD = iD;
        this.canPurchase = false;
        this.text = "Draw Opportunity Knocks card";
    }

    /**
     * Draws Opportunity Knocks card and activates it's effect
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        CardEffect currentCard = this.board.drawOpportunityKnocks();
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

    /**
     * init the gui elements
     */
    @Override
    public void initGuiElements() {
        super.tileName = new Label();
    }
}
