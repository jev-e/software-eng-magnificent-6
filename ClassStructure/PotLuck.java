package ClassStructure;

/**
 * Pot luck tile
 */
public class PotLuck extends TileEffect {

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
        System.out.println(text);//Display for test version
        this.board.drawPotLuck().effect(currentPlayer);
    }
}
