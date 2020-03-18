package ClassStructure;

/**
 * Opportunity Knocks tile event
 */
public class OpportunityKnocks extends TileEffect{

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
        this.board.drawOpportunityKnocks().effect(currentPlayer);
    }
}
