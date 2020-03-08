import ClassStructure.*;

import java.util.Scanner;
import java.lang.*;

/**
 * @author Jacob Evans
 *	Class / Method for creation of players
 *	Dependant on token/player classes
 */
public class tokenAssign {
    public static void main(String[] args) {
        Player player1;
        player1 = playerCreate();
        System.out.println(player1.getName());
        System.out.println(player1.getToken());
        System.out.println(player1.getToken().getImgPath());
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
        Player newPlayer = null;
        String tokenChoice = playerChoice.toUpperCase();
        if ("CAT".equals(tokenChoice)) {
            System.out.println("You have chosen the Cat token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);

        } else if ("HATSTAND".equals(tokenChoice)) {
            System.out.println("You have chosen the Hatstand token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);

        } else if ("SMARTPHONE".equals(tokenChoice)) {
            System.out.println("You have chosen the Smartphone token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);


        } else if ("BOOT".equals(tokenChoice)) {
            System.out.println("You have chosen the Boot token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);


        } else if ("GOBLET".equals(tokenChoice)) {
            System.out.println("You have chosen the Goblet token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);


        } else if ("SPOON".equals(tokenChoice)) {
            System.out.println("You have chosen the Spoon token.");
            Token playerToken = Token.valueOf(tokenChoice);
            newPlayer = new Player(name,playerToken);
        }
        return newPlayer;
    }
}
