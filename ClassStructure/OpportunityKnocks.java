package ClassStructure;

public class OpportunityKnocks extends TileEffect{

    public OpportunityKnocks(int iD) {
        this.title = "Opportunity Knocks";
        this.iD = iD;
        this.canPurchase = false;
        this.text = "Draw Opportunity Knocks Luck card";
    }

    /**
     * Draws pot luck card and activates it's effect
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        this.board.drawOpportunityKnocks().effect(currentPlayer);
    }
}
