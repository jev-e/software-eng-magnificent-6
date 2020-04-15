package ClassStructure;

import java.util.Scanner;

/**
 * Pay tax or draw opportunity knocks card event
 * Player choice currently text solution in place
 * will need updating onto GUI stage
 */
public class PayTaxOrDrawOpportunity extends CardEffect {

    private int amount;

    /**
     * Default constructor for Jackson
     */
    public PayTaxOrDrawOpportunity() {
    }

    /**
     * Text version of selection, to be updated when GUI elements are in place
     * @param cardText card text to be displayed on activation
     * @param amount amount of tax to be paid
     */
    public PayTaxOrDrawOpportunity(String cardText, int amount) {
        this.cardText = cardText;
        this.amount = amount;
    }

    /**
     * Player selects 1 if they wise to pay the fine and 2 otherwise
     * @param currentPlayer player who drew the card and is target to the fine or drawing a new card
     */
    @Override
    void effect(Player currentPlayer) {
        if (!currentPlayer.isAiAgent()) {
            Scanner userIn = new Scanner(System.in);
            System.out.println(cardText);
            int input = 0;
            while (input != 1 && input != 2) {//Checks for valid input
                System.out.println("Enter 1 to pay fine or 2 to draw an opportunity knocks card");
                input = userIn.nextInt();
            }
            if (input == 1) {
                int payment = currentPlayer.deductAmount(amount);
                this.board.taxPot += payment;//add the amount the player paid to the tax pot
            } else {
                this.board.drawOpportunityKnocks().effect(currentPlayer);//draw card and activate it's effect
            }
        } else {//AI decision
            if (currentPlayer.payTaxOrDraw()) {
                int payment = currentPlayer.deductAmount(amount);
                this.board.taxPot += payment;//add the amount the player paid to the tax pot
            } else {
                this.board.drawOpportunityKnocks().effect(currentPlayer);//draw card and activate it's effect
            }
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
