package ClassStructure;

/**
 * Opportunity Knocks tile event
 */
public class OpportunityKnocks extends TileEffect{

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
        System.out.println(text);//Display for test version
        this.board.drawOpportunityKnocks().effect(currentPlayer);
    }
}
