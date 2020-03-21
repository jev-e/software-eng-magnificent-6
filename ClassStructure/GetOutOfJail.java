package ClassStructure;

/**
 * Get out of jail free card effect
 */
public class GetOutOfJail extends CardEffect {

    private Player owner;//Reference to player who currently owns this card

    /**
     * Create new get out of jail free card with provided card text
     * @param cardText text to be displayed on card
     */
    public GetOutOfJail(String cardText) {
        this.cardText = cardText;
    }

    @Override
    void effect(Player currentPlayer) {
        currentPlayer.addAsset(this);//adds this item to players assets
        owner = currentPlayer;//keep reference the owner of the card until it is played
        //When card is drawn from deck owner will be overwritten
    }

    /**
     * Playing get out of jail free card logic. Maintains deck
     */
    public void playCard() {
        owner.leaveJail();//set player out of jail and resets jailCount
        owner.removeAsset(this);//removes this card from asset list
        this.board.opportunityKnocks.addLast(this);//add this card back to the bottom of the deque
    }
}
