package ClassStructure;


/**
 * Go to Jail tile event
 */
public class GoToJail extends TileEffect {

    /**
     * Default constructor for Jackson
     */
    public GoToJail(){
        canPurchase = false;
    }

    /**
     * Sends player to jail
     * @param iD tiles ID on the board
     */
    public GoToJail(int iD, String text, int jail) {
        this.iD = iD;
        this.title = "Go to Jail";
        this.canPurchase = false;
        this.text = text;
    }

    /**
     * On land the player is sent to jail without pass go check. The players jailed status is set to true
     * @param currentPlayer player who landed on the tile and target of effect
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.jailPlayer();
        int jailPosition = currentPlayer.getCurrentPos();
        currentPlayer.addAction(text);//add text to log
        board.tiles.get(jailPosition).activeEffect(currentPlayer);//activate jail as the player has just been moved there
        }
    }

