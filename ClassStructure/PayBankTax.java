package ClassStructure;

/**
 * Player pays tax to the bank tile events
 * Super tax
 * Income tax
 */
public class PayBankTax extends TileEffect{
    private int amount;

    /**
     * For Income Tax and Super Tax tiles
     * @param iD location ID
     * @param title Name of tile to appear on board
     * @param text Activation text
     * @param amount tax amount to be deducted
     */
    public PayBankTax(int iD, String title, String text, int amount) {
        this.iD = iD;
        this.amount = amount;
        this.title = title;
        this.text = text;
        this.canPurchase = false;
    }

    /**
     * Deduct tax amount from player balance
     * this does not get added to the tax pot despite the name
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        int payment = currentPlayer.deductAmount(amount);
        if(payment < amount) {
            this.board.bankruptPlayer(currentPlayer);//bankrupt player for failure to pay full amount
        }
    }
}
