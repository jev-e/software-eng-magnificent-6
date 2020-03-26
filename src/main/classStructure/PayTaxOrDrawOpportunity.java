package classStructure;

import java.util.Scanner;

/**
 * Pay tax or draw opportunity knocks card event
 * classStructure.Player choice currently text solution in place
 * will need updating onto GUI stage
 */
public class PayTaxOrDrawOpportunity extends CardEffect {

    private int amount;

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
     * classStructure.Player selects 1 if they wise to pay the fine and 2 otherwise
     * @param currentPlayer player who drew the card and is target to the fine or drawing a new card
     */
    @Override
    void effect(Player currentPlayer) {
        Scanner userIn = new Scanner(System.in);
        System.out.println(cardText);
        int input = 0;
        while(input != 1 && input != 2) {//Checks for valid input
            System.out.println("Enter 1 to pay fine or 2 to draw an opportunity knocks card");
            input = userIn.nextInt();
        }
        if(input == 1) {
            int payment = currentPlayer.deductAmount(amount);
            if(payment < amount) {
                this.board.bankruptPlayer(currentPlayer);//bankrupt player for failure to pay full amount
            }
            this.board.taxPot += payment;//add the amount the player paid to the tax pot
        }else{
            this.board.drawOpportunityKnocks().effect(currentPlayer);//draw card and activate it's effect
        }
    }
}
