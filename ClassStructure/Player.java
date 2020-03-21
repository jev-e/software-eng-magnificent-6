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
    private boolean inJail;//indicator to differ between players who are jailed or just visiting
    private int jailTime;//counter to indicate how many turns a player has spent in jail

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
        money = 1500;//All references to money is in £'s
        assets = new LinkedList<>();
        canBuy = false;
        inJail = false;
        this.jailTime = 0;
    }

    /**
     * Adds amount to players balance
     * @param amount integer amount to modify the balance by
     */
    public void payPlayerAmount(int amount) {
        money += amount;
    }

    /**
     * Deducts amount from players money if they can pay returning the amount
     * if they cannot pay assets can be sold
     * if assets will not cover payment the player is bankrupt
     * @param amount amount to be deducted
     * @return the amount of money the player was able to pay
     */
    public int deductAmount(int amount) {
        if(money > amount) {
            payPlayerAmount(-amount);
            return amount;
        }else{
            //check for ability to sell assets
            if(money > amount) {
                return amount;
            }
        }
        return money;//returns what the player can pay
    }

    /**
     * Adds an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card) to be added
     */
    public void addAsset(Object item) {
        assets.add(item);
    }

    /**
     * Removes an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card)
     */
    public void removeAsset(Object item) {
        assets.remove(item);
    }

    public String getName() {
        return name;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    /**
     * Sets the position on the board that the player is on and updates previous position
     * @param currentPos new position for player to be moved to
     */
    public void setCurrentPos(int currentPos) {
        this.previousPos = this.currentPos;//maintains previous position before change
        this.currentPos = currentPos;
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


    /**
     * Fetches the players assets list
     * @return a list of items(Property|Station|Utility|Card)
     */
    public LinkedList<Object> getAssets() {
        return assets;
    }

    public boolean CanBuy() {
        return canBuy;
    }

    /**
     * Pass go check and money
     */
    public void passGo() {
        if(currentPos < previousPos) {
            payPlayerAmount(200);//collect £200
        }
        if(!canBuy) {//Checks if the player can buy and set it to true if false
            canBuy = true;
        }
    }

    /**
     * Getter for players token enum
     * @return enum consisting of the token name and image path
     */
    public Token getToken() {
        return token;
    }

    /**
     * sets player to be in jail
     * to be added to alo move player to jail location
     */
    public void jailPlayer() {
        inJail = true;
        //jailTime++; check brief to see how many turns to be spent in jail
        //move player to jail location
    }

    /**
     * Increments time spent in jail by one turn
     */
    public void serveJailTime() {
        jailTime++;
    }

    /**
     * Time player has been in jail getter
     * @return int number of turns player has been jailed for
     */
    public int getJailTime() {
        return jailTime;
    }
    /**
     * Used to remove a player from jail by any means
     * Pay to get out/ spend turns in jail/ get out of jail card
     * maintains jailed boolean and jail count
     */
    public void leaveJail() {
        inJail = false;
        jailTime = 0;
    }
}