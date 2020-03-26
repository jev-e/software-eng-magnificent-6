package classStructure;

import java.util.LinkedList;

/**
 * @author Ayman
 * classStructure.Birthday card event
 */
public class Birthday extends CardEffect{

    /**
     * classStructure.Birthday card event is non generic and takes no parameters
     */
    public Birthday() {
        this.cardText = "It's your birthday. Collect £10 from each player";//card text is unchanging
    }

    /**
     * £10s is taken from each player other than the current player who receives the total money collected
     * @param currentPlayer target player to receive birthday money
     */
    @Override
    void effect(Player currentPlayer) {
        int payment = 0;
        LinkedList<Player> players = this.board.turnOrder;//get turn order from the board
        for(Player p : players) {
            if(!p.equals(currentPlayer)) {//only take from other players than current
                int temp = p.deductAmount(10);//take £10 from player
                if(temp < 10) {
                    this.board.bankruptPlayer(p);//bankrupt player for failure to pay
                }
                payment += temp;
            }
        }
        currentPlayer.payPlayerAmount(payment);//give player the total collected
    }
}
