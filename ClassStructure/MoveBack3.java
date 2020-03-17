package ClassStructure;

public class MoveBack3 extends CardEffect {

    public MoveBack3() {
        this.cardText = "You move back 3 spaces";
    }

    /**
     * moves player back 3 spaces
     */
    @Override
    void effect(Player currentPlayer) {
        int max = board.tiles.size();
        int position = currentPlayer.getCurrentPos();
        int newPosition = ((position - 3) + max) % max;//Performs wrap around
        currentPlayer.setCurrentPos(newPosition);
        System.out.println(cardText);
    }
}
