package ClassStructure;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Go.class, name = "go"),
        @JsonSubTypes.Type(value = Utility.class, name = "utility"),
        @JsonSubTypes.Type(value = PayBankTax.class, name = "payBankTax"),
        @JsonSubTypes.Type(value = PotLuck.class, name = "potLuck"),
        @JsonSubTypes.Type(value = Station.class, name = "station"),
        @JsonSubTypes.Type(value = OpportunityKnocks.class, name = "opportunityKnocks"),
        @JsonSubTypes.Type(value = FreeParking.class, name = "freeParking"),
        @JsonSubTypes.Type(value = GoToJail.class, name = "goToJail"),
        @JsonSubTypes.Type(value = Jail.class, name = "jail"),
})

/**
 * @author Ayman Bensreti
 * 	Effects of non property tiles
 *
 */
public abstract class TileEffect extends BoardTile{

    String text;
    Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Board getBoard() {
        return board;
    }
}
