package ClassStructure;

import java.util.Scanner;

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

    @Override
    void effect(Player currentPlayer) {
        Scanner userIn = new Scanner(System.in);
        System.out.println(cardText);
        int input = 0;
        while(input != 1 && input != 2) {
            System.out.println("Enter 1 to pay fine or 2 to draw an opportunity knocks card");
            input = userIn.nextInt();
        }
        if(input == 1) {
            currentPlayer.alterBalance(-amount);
        }else{
            this.board.drawOpportunityKnocks().effect(currentPlayer);//draw card and activate it's effect
        }
    }
}
