package ClassStructure;
import com.sun.deploy.panel.IProperty;
import org.w3c.dom.stylesheets.StyleSheet;

import javax.rmi.CORBA.Util;
import java.util.LinkedList;
import java.util.Scanner;
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
     * Checks to see if player owns any property, train staions or utilities.
     * @return whether they own any properties or not.
     */
    public boolean ownsProperty(){
        boolean  check = false;
        for (int i = 0; i < assets.size(); i++){
            if (!(assets.get(i) instanceof GetOutOfJail)){
                check = true;
            }
        }
        return(check);
    }

    /**
     * Determines the total netwworth of a player. This incudes his money, his properties, stations, utilities and
     * any buildings they have developed on any properties.
     * @return The total networth of the player
     */
    public int networth(){
        int value = this.money;
        for(Object item : assets){
            if (item instanceof Property){
                value += ((Property) item).getValue();
            }
            else if(item instanceof Utility){
                value += ((Utility) item).getCost();
            }
            else if(item instanceof Station){
                value += ((Station) item).getCost();
            }
        }
        return(value);
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
            while(this.money < amount && networth() > amount){
                System.out.println("You do not have enough money to pay the debt. Please sell assets to pay your debt:");
                sellAsset();
            }
            if (this.money < amount){
                this.money = this.bankruptPlayer();
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
     *Creates a text menu to show off the available properties that the player owns.
     */
    public int displayProperties(){
        int posVal = 0;
        for(Object item : assets){
            posVal ++;
            if (item instanceof Property){
                System.out.println(posVal + ": " + ((Property) item).getTitle());
            }
            else if(item instanceof Utility){
                System.out.println(posVal + ": " + ((Utility) item).getTitle());
            }
            else if(item instanceof Station){
                System.out.println(posVal + ": " + ((Station) item).getTitle());
            }
        }
        return(posVal);
    }

    /**
     * Allows for the selling of properties and placing the money into the player's account
     * @param item The property that is being sold
     * @return whether selling the property was successful or not.
     */
    public boolean sellProperty(Object item){
        int amount;
        boolean found = true;
        if(item instanceof Property){
            amount = ((Property) item).sellProperty();
            if(amount != 0){
                assets.remove(item);
                this.money += amount;
            }
            else{
                System.out.println("Property still has buildings developed. Please sell buildings first.");
            }
        }
        else if (item instanceof Utility){
            this.money += ((Utility) item).sell();
            this.assets.remove(item);
        }
        else if(item instanceof Station){
            this.money += ((Station) item).sell();
            this.assets.remove(item);
        }
        else{
            System.out.println("Invalid option.  please type in a a valid number");
            found = false;
        }
        return(found);
    }

    /**
     * Allows for mortgaging properties and placing the money into the player's account
     * @param item
     * @return whether mortgaging the property was successful or not.
     */
    public boolean mortgageProperty(Object item){
        int amount;
        boolean found = true;
        if(item instanceof Property){
            amount = ((Property) item).mortgageProperty();
            if(amount != 0){
                this.money += amount;
            }
            else{
                System.out.println("Property still has buildings developed. Please sell buildings first.");
            }
        }
        else if (item instanceof Utility){
            this.money += ((Utility) item).mortgage();
            this.assets.remove(item);
        }
        else if(item instanceof Station){
            this.money += ((Station) item).mortgage();
            this.assets.remove(item);
        }
        else{
            System.out.println("Invalid option.  please type in a a valid number");
            found = false;
        }
        return(found);
    }

    /**
     * Allows for selling a building off a property and giving the money to the player.
     * @param item The property that is being mortgaged
     * @return whether selling a building was successful for or not.
     */
    public boolean sellBuildings(Property item){
        boolean found =  true;
        int amount = item.sellHouseOrHotel();
        if(amount == 0){
            System.out.println("This property has no buildings to sell");
            found = false;
        }
        this.money += amount;
        return(found);
    }

    /**
     *
     * @param item
     * @return
     */
    public void propertyManegment(Object item){
        Scanner scan = new Scanner(System.in);
        System.out.println("1: Sell\n2: mortgage");
        if(item instanceof Property ){
            System.out.println("3: sell buildings");
        }
        boolean found = false;
        while(!found){
            String choice = scan.next();
            if (choice.equals("1")){
                found = sellProperty(item);
            }
            else if(choice.equals("2")){
                found = mortgageProperty(item);
            }
            else if(choice.equals("3") && item instanceof Property){
                found = sellBuildings((Property) item);
            }
            else{
                System.out.print("Invalid Option. Please try again");
            }
        }
    }


    /**
     *
     */
    public void sellAsset(){
        Scanner scan = new Scanner(System.in);
        int pos = 0;
        displayProperties();
        boolean found = false;
        while(!found){
            try{
                pos = Integer.parseInt(scan.next());
                if (pos > assets.size() || pos < 1){
                    if (assets.get(pos-1) instanceof GetOutOfJail){
                        System.out.println("Invalid option.  please type in a a valid number");
                    }
                    else{
                        propertyManegment(assets.get(pos-1));
                        found = true;
                    }
                }
                else{
                    System.out.println("Invalid option.  please type in a a valid number");
                }
            }
            catch (Exception e){
                System.out.println("Invalid option. please type in a a valid number");
                scan.next();
            }
        }
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

    /**
     *
     * @return
     */
    public int bankruptPlayer() {
        int amount = this.networth();
        this.money = 0;
        for(Object item : this.assets){
            if(item instanceof Property){
                ((Property) item).returnToBank();
            }
            else if(item instanceof Utility){
                ((Utility) item).returnToBank();
            }
            else if(item instanceof Station){
                ((Station) item).returnToBank();
            }
            else if(item instanceof GetOutOfJail){
                ((GetOutOfJail) item).returnCard();
            }
        }
        assets = null;
        return(amount);
    }
}