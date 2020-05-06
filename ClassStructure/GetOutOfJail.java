package ClassStructure;

/**
 * Get out of jail free card effect
 */
public class GetOutOfJail extends CardEffect {

    private Player owner;//Reference to player who currently owns this card
    private String deck;//indicator to which deck this card belongs to

    /**
     * Default constructor for Jackson
     */
    public GetOutOfJail() {

    }

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
        currentPlayer.addAction(cardText);//add text to log
        //When card is drawn from deck owner will be overwritten
    }

    /**
     * Playing get out of jail free card logic. Maintains deck
     */
    public void playCard() {
        owner.leaveJail();//set player out of jail and resets jailCount
        owner.removeAsset(this);//removes this card from asset list
        if (deck.equals("pot")) {
            this.board.potLuck.addLast(this);//returns to pot luck deck
        } else {
            this.board.opportunityKnocks.addLast(this);//returns to opportunity knocks deck
        }
    }

    /**
     * Returns the card to the deck it came from as get out of jail cards can be from either decks
     */
    public void returnCard() {
        if (deck.equals("pot")) {
            this.board.potLuck.addLast(this);//returns to pot luck deck
        } else {
            this.board.opportunityKnocks.addLast(this);//returns to opportunity knocks deck
        }
    }

    /**
     * Setter to assign the deck that a card has been placed into
     *
     * @param deck name of deck that this card belongs to pot | opp
     */
    public void setDeck(String deck) {
        this.deck = deck;
    }

}
