package ClassStructure;
import java.util.LinkedList;

public class Birthday extends CardEffect{

    public Birthday() {
        this.cardText = "It's your birthday. Collect £10 from each player";
    }

    @Override
    void effect(Player currentPlayer) {
        LinkedList<Player> players = this.board.turnOrder;
        int otherPlayersCount = players.size() - 1; //count of players who are not you
        for(Player p : players) {
            if(!p.equals(currentPlayer)) {//only take from other players than current
                p.alterBalance(-10);//take £10 from player
            }
        }
        currentPlayer.alterBalance(10*otherPlayersCount);
    }
}
