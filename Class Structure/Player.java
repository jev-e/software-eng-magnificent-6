import java.util.LinkedList;

/**
 *
 * @author Ayman Bensreti
 *	Player information
 */
public class Player {
    private int currentPos;
    private int previousPos;
    private String name;
    private Token token;
    private int money;
    private LinkedList<Object> assets;
    private boolean canBuy;

    public Player(String name, Token token) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 0;//TODO look up starting money in brief
        assets = new LinkedList<Object>();
        canBuy = false;
    }

    /**
     * Alters the players balance
     * @param amount positive or negative integer to modify the balance by
     */
    public void alterBalance(int amount) {
        money += amount;
    }

    public String getName() {
        return name;
    }

    public Token getToken() {
        return token;
    }
}