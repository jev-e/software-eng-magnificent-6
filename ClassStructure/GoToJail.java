package ClassStructure;

public class GoToJail extends TileEffect {

    private int jail;
    /**
     * Sends player to jail
     * @param iD tiles ID on the board
     * @param jail temporary ID pointer to location of jail for testing full version will be constant
     */
    public GoToJail(int iD, String text, int jail) {
        this.iD = iD;
        this.title = "Go to Jail";
        this.canPurchase = false;
        this.text = text;
        this.jail = jail;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.setJailed(true);
        currentPlayer.setCurrentPos(jail);
    }
}
