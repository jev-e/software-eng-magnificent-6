package src;

/**
 * Card event, moves the player back 3 spaces
 */
public class MoveBack3 extends CardEffect {

    /**
     * Non generic card type. Always performs the same action
     */
    public MoveBack3() {
        this.cardText = "You move back 3 spaces";
    }

    /**
     * moves player back 3 spaces and activates the tile on landing
     */
    @Override
    void effect(Player currentPlayer) {
        int max = board.tiles.size();//get number of tiles for differing size boards, can be hard coded in full version
        int position = currentPlayer.getCurrentPos();//get players position
        int newPosition = ((position - 3) + max) % max;//Performs wrap around
        currentPlayer.setCurrentPos(newPosition);//update the players position to the one calculated
        currentPlayer.addAction(cardText);//add text to log
        board.tiles.get(newPosition).activeEffect(currentPlayer);//triggers that tiles effect
    }
}
