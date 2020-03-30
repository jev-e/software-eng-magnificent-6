package ClassStructure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;



@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @Type(value = Birthday.class, name = "birthday"),
        @Type(value = PlayerPaysBank.class, name = "playerPaysBank"),
        @Type(value = BankPaysPlayer.class, name = "bankPaysPlayer"),
        @Type(value = MovePlayerNoPassGo.class, name = "movePlayerNoPassGo"),
        @Type(value = MovePlayerPassGo.class, name = "movePlayerPassGo"),
        @Type(value = PlayerPaysTax.class, name = "playerPaysTax"),
        @Type(value = PayTaxOrDrawOpportunity.class, name = "payTaxOrDrawOpportunity"),
        @Type(value = GoToJailCard.class, name = "goToJailCard"),
        @Type(value = GetOutOfJail.class, name = "getOutOfJail"),
        @Type(value = MoveBack3.class, name = "moveBack3"),
        @Type(value = HousingRepairs.class, name = "housingRepairs"),
})
/**
 * @author Ayman Bensreti
 *	Card generic structure
 */
public abstract class CardEffect {

    String cardText;
    Board board;//Reference to the game board to allow access to other game elements

    abstract void effect(Player currentPlayer);

    /**
     * Board setter for use in board constructor
     * @param board the game board the card will be linked to
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }
}
