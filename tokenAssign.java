/**
 *
 * @author Jacob Evans
 *	Class / Method for creation of players
 *	Dependant on token/player classes
 */


import java.util.Scanner;
import java.lang.*;

public class tokenAssign {
    public static void main(String[] args) {
        Player player1;
        player1 = playerCreate();
        System.out.println(player1.getName());
        System.out.println(player1.getToken());
        System.out.println(player1.getToken().getPath());
    }

    public static Player playerCreate() {
        String newline = System.lineSeparator();
        System.out.print("Please enter your name: ");
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();
        System.out.print("Your name is " + name + newline);
        System.out.print(("Please pick your token from the options below:" + newline +
                "1. Cat" + newline +
                "2. Hatstand" + newline +
                "3. Smartphone" + newline +
                "4. Boot" + newline +
                "5. Goblet" + newline +
                "6. Spoon" + newline));
        String playerChoice = in.nextLine();
        String tokenChoice = playerChoice.toUpperCase();
        Player newPlayer;
        try {
            newPlayer = new Player(name, Token.valueOf(tokenChoice));
        } catch (IllegalArgumentException e) {
            // Do some error handling, player didn't choose any tokens
            // Maybe a do .. while()
            return null;
        }
        System.out.println("You have chosen the " + tokenChoice + " token.");

        return newPlayer;
    }
}
