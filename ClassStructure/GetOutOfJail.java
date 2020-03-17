package ClassStructure;

public class GetOutOfJail extends CardEffect {

    private Player owner;

    public GetOutOfJail(String cardText) {
        this.cardText = cardText;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.addAsset(this);//adds this item to players assets
        owner = currentPlayer;//keep reference the owner of the card until it is played
    }

    /**
     * Playing get out of jail free card logic. Maintains deck
     */
    void playCard() {
        owner.setJailed(false);
        owner.removeAsset(this);//removes this card from asset list
        this.board.opportunityKnocks.addLast(this);//add this card back to the bottom of the deque
    }
}
