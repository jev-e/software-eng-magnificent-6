package ClassStructure;

import javafx.scene.control.Label;

/**
 * Player pays tax to the bank tile events
 * Super tax
 * Income tax
 */
public class PayBankTax extends TileEffect{

    private int amount;

    /**
     * Default constructor for Jackson
     */
    public PayBankTax() {
        canPurchase = false;
    }

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
        currentPlayer.addAction(text);//add text to log
        currentPlayer.deductAmount(amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * init the gui elements
     */
    @Override
    public void initGuiElements() {
        super.tileName = new Label();
    }
}
