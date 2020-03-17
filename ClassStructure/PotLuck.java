package ClassStructure;

public class PotLuck extends TileEffect {

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
        this.board.drawPotLuck().effect(currentPlayer);
    }
}
