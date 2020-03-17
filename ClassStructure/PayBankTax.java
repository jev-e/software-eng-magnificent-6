package ClassStructure;

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
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.alterBalance(-amount);
    }
}
