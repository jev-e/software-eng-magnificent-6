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
        //When card is drawn from deck owner will be overwritten
        System.out.println("You get a get out of jail free card");
    }

    /**
     * Playing get out of jail free card logic. Maintains deck
     */
    public void playCard() {
        owner.leaveJail();//set player out of jail and resets jailCount
        owner.removeAsset(this);//removes this card from asset list
        System.out.println(cardText);//Display for test version
        if(deck == "pot") {
            this.board.potLuck.addLast(this);//returns to pot luck deck
        }else{
            this.board.opportunityKnocks.addLast(this);//returns to opportunity knocks deck
        }
    }

    public void returnCard() {
        if(deck == "pot") {
            this.board.potLuck.addLast(this);//returns to pot luck deck
        }else{
            this.board.opportunityKnocks.addLast(this);//returns to opportunity knocks deck
        }
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }
}
