package ClassStructure;
import java.util.LinkedList;
/**
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

    /**
     * Sets players name and token and initialises the players starting assets
     * @param name player name
     * @param token one of token enum
     */
    public Player(String name, Token token) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 0;//TODO look up starting money in brief
        assets = new LinkedList<>();
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

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.previousPos = this.currentPos;//maintains previous position before change
        this.currentPos = currentPos;
        if(currentPos < previousPos):
            alterBalance(200);
    }

    public int getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(int previousPos) {
        this.previousPos = previousPos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public LinkedList<Object> getAssets() {
        return assets;
    }

    public void setAssets(LinkedList<Object> assets) {
        this.assets = assets;
    }

    public boolean CanBuy() {
        return canBuy;
    }

    public void setCanBuy(boolean canBuy) {
        this.canBuy = canBuy;
    }

    public Token getToken() {
        return token;
    }
}
