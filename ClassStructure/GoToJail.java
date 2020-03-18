package ClassStructure;

/**
 * Go to Jail tile event
 */
public class GoToJail extends TileEffect {

    private int jail;//Temporary
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

    /**
     * On land the player is sent to jail without pass go check. The players jailed status is set to true
     * @param currentPlayer player who landed on the tile and target of effect
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.setJailed(true);
        currentPlayer.setCurrentPos(jail);
    }
}
