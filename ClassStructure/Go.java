package ClassStructure;

public class Go extends TileEffect {

     public Go() {
        this.title = "Go";
        this.canPurchase = false;
        this.iD = 0;//Is always the first tile
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        //currently does nothing potential to add text prompt on landing
    }
}
