package ClassStructure;

/**
 * Jail tile effect player
 */
public class Jail extends TileEffect{

    /**
     * Default constructor for Jackson
     */
    public Jail() {
        canPurchase = false;
    }

    /**
     * Creates Jail tile
     * @param iD location of tile
     * @param title Name of tile
     * @param text Text to be displayed on tile activation
     */
    public Jail(int iD, String title, String text) {
        this.iD = iD;
        this.title = title;
        this.text = text;
        this.canPurchase = false;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        if (currentPlayer.isInJail()) {
            if (currentPlayer.getJailTime() == 2) {
                currentPlayer.leaveJail();//Frees the player after staying two turns in jail and resets jail time
                System.out.println(currentPlayer.getName() + " has left jail");//Display for test version
            } else {
                currentPlayer.serveJailTime();//Increases the jail time count
                System.out.println(currentPlayer.getName() + "is in jail");
                //move on with players turn they are still in jail
            }
        } else {
            System.out.println("Just visiting");
            //Otherwise player is just visiting
        }

    }
}
