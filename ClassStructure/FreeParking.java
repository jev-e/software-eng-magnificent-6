package ClassStructure;

public class FreeParking extends TileEffect{

     public FreeParking(int iD) {
        this.iD = iD;
        this.title = "Free Parking";
        this.text = "You collect the tax pot";
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        int amount = this.board.taxPot;
        this.board.taxPot = 0;//resets tax pot
        currentPlayer.alterBalance(amount);
    }
}
