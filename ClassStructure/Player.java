package ClassStructure;

import GUI.Main;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ayman Bensreti, Calvin Boreham
 *	Player information
 */
public class Player {
    private int currentPos; //ID of player position on board
    private int previousPos; //ID of player's previous position on board
    private String name; //name of player
    private Token token; //token representation of player on board
    private int money; //money held by player
    public LinkedList<Object> assets; //assets of player (utilities, station, property, get out of jail card)
    private boolean canBuy; //flag for if player is eligible for purchasing property
    private boolean inJail; //indicator to differ between players who are jailed or just visiting
    private int jailTime; //counter to indicate how many turns a player has spent in jail
    private Board board; //board the player is playing on
    private int lastRoll1;//Last die 1 result for use in Utilities and GUI
    private int lastRoll2;//Last die 2 result
    private boolean aiAgent;//flag for control method (userInput|Agent response)
    private Trait personality;
    private String actionLog;//Event text to be displayed to the player or at the end of AI turn

    /**
     * Sets players name and token and initialises the players starting assets
     *
     * @param name  player name
     * @param token one of token enum
     * @param board the current board being played
     */
    public Player(String name, Token token, Board board, boolean aiAgent) {
        this.name = name;
        this.token = token;
        currentPos = 0;
        previousPos = 0;
        money = 1500;//All references to money is in £'s
        assets = new LinkedList<>();
        canBuy = false;
        inJail = false;
        this.aiAgent = aiAgent; //by default assumed human player
        this.jailTime = 0;
        this.board = board;
        if (aiAgent) {
            personality = new Trait();//generate personality
        }
        actionLog = "";//Action log is initially empty
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
     * if assets will not cover payment the player is bankrupt, return the net worth of the player
     * it is imperative that it is checked that the player has enough money to purchase items such as houses before
     * this method is called
     * @param amount amount that was payed
     * @return the amount of money the player was able to pay
     */
    public int deductAmount(int amount) {
        int payableAmount;

        if(money >= amount) { //player can pay, so pay full amount
            payPlayerAmount(-amount); //deduct money from player
            payableAmount = amount; //update payable amount to full amount
        }else{
            //find player net worth
            int netWorth = netWorth();
            if (netWorth >= amount) {
                //can pay after asset selling, trigger it
                //owner money will be updated
                if (!isAiAgent()) {
                    assetSelling(amount - money);
                } else {
                    agentSellAssets(amount - money);
                }
                //pay amount
                payPlayerAmount(-amount);
                payableAmount = amount; //update payable amount to full amount
            }else{
                //no point asset selling, bankrupt player
                bankrupt();
                //return the amount they could have played
                payableAmount = netWorth;
            }
        }
        return payableAmount;//returns what the player can pay
    }

    /**
     * Adds an item(Property|Station|Utility|Card) to players assets
     * updates owner for non card assets
     *
     * @param item one of (Property|Station|Utility|Card) to be added
     */
    public void addAsset(Object item) {
        assets.add(item);
        if (item instanceof Property) {
            ((Property) item).setOwner(this);
        } else if (item instanceof Station) {
            ((Station) item).setOwner(this);
        } else if (item instanceof Utility) {
            ((Utility) item).setOwner(this);
        }
        board.updatePlayerAssets(this, item, "add");
    }

    /**
     * Removes an item(Property|Station|Utility|Card) to players assets
     * @param item one of (Property|Station|Utility|Card)
     */
    public void removeAsset(Object item) {
        if (item instanceof Property) {
            ((Property) item).setOwner(null);
        } else if (item instanceof Station) {
            ((Station) item).setOwner(null);
        } else if (item instanceof Utility) {
            ((Utility) item).setOwner(null);
        }
        assets.remove(item);
        board.updatePlayerAssets(this, item, "remove");
    }

    /**
     * Getter for player name
     * @return name player name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for player current position
     * @return currentPos players position on the board
     */
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
        board.movePlayerToken(this);

    }

    public int getPreviousPos() {
        return previousPos;
    }

    public void setPreviousPos(int previousPos) {
        this.previousPos = previousPos;
    }

    /**
     * Setter for player name
     * @param name the new player name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Getter for player money
     *
     * @return money the amount in player bank account
     */
    public int getMoney() {
        return money;
    }

    /**
     * Setter for player money for use in testing
     *
     * @param amount the amount to set player money to
     */
    public void setMoney(int amount) {
        money = amount;
    }

    /**
     * Getter for player board
     *
     * @return board player currently on
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Setter for player board
     *
     * @param board the board currently in play
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isAiAgent() {
        return aiAgent;
    }

    public void setAiAgent(boolean aiAgent) {
        this.aiAgent = aiAgent;
        if (isAiAgent()) {
            personality = new Trait();//generate traits
        }
    }

    /**
     * Fetches the players assets list
     *
     * @return a list of items(Property|Station|Utility|Card)
     */
    public LinkedList<Object> getAssets() {
        return assets;
    }

    /**
     * Fetches whether the player can buy a property or not
     * @return canBuy boolean representation of player's ability to purchase assets
     */
    public boolean CanBuy() {
        return canBuy;
    }

    /**
     * Pass go check and money
     */
    public void passGo() {
        System.out.println("Pass Go Called");
        if(currentPos < previousPos) {
            payPlayerAmount(200);//collect £200
            addAction("Passed Go, collect £200");//add text to log
            if(!canBuy) {//Checks if the player can buy and set it to true if false
                canBuy = true;
            }
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
     * Gives the player the option to pay or use get out of jail free card
     */
    public void jailPlayer() {
        inJail = true;//sets player in jail
        GetOutOfJail getOutOfJail = null;
        setCurrentPos(10);
        for (Object item : assets) {//looks for a get out of jail free card
            if (item instanceof GetOutOfJail) {
                getOutOfJail = (GetOutOfJail) item;//if found getOutOfJail is set to this card
            }//If the player has two the first one is used
        }
        //Player choice here
        if (!isAiAgent()) {//if human player
            //TODO GUI Jail decision
            board.callJailSetupScene(this, getOutOfJail);
        } else {
            if (getOutOfJail != null) {
                addAction("Use Get Out of Jail card");
                getOutOfJail.playCard();//AI uses card
            } else if (!personality.isPatient()) {//AI is not patient
                if (personality.isCautious()) {
                    if (cautiousWillBuy(50)) {//checks if AI will pay bail
                        deductAmount(50);
                        addAction("pay bail");
                        leaveJail();
                    }
                } else {
                    if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip
                        deductAmount(50);
                        addAction("pay bail");
                        leaveJail();//pay bail based on coin flip
                    }
                }
            }
            //otherwise do nothing and serve time
        }
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
     * Check to see if the player is in jail for jail tile and rent checks
     */
    public boolean isInJail() {
        return inJail;
    }

    public int getLastRoll1() {
        return lastRoll1;
    }

    public void setLastRoll1(int lastRoll1) {
        this.lastRoll1 = lastRoll1;
    }

    public int getLastRoll2() {
        return lastRoll2;
    }

    public void setLastRoll2(int lastRoll2) {
        this.lastRoll2 = lastRoll2;
    }

    /**
     * Adds text of an event onto action log, each event is separated by a new line
     *
     * @param action event text to be added
     */
    public void addAction(String action) {
        actionLog += action + "\n";//add action
    }

    /**
     * Gets the action log of undisplayed actions the player has taken.
     * Action log is cleared each time this method is called
     *
     * @return string of undisplayed actions the player has taken
     */
    public String getActionLog() {
        String actions = actionLog;//pass action log into actions
        actionLog = "";//clear action log as it has been displayed
        return actions;
    }

    /**
     * Updates flags for all player's properties, updates base rent amount if complete set with no properties. Should
     * be called whenever a change to property ownership has occurred - e.g in purchase, sale, trading etc
     */
    public void completeSetProperties() {
        HashMap<Group, Integer> count = new HashMap<>(); //frequency distribution

        for (Object asset : assets) { //for each asset owned by player
            //pull out all properties owned and form a count
            if (asset instanceof Property) {
                int prev = 0;
                if (count.get(((Property) asset).getGroup()) != null) {
                    prev = count.get(((Property) asset).getGroup());
                }
                count.put(((Property) asset).getGroup(), prev + 1);
            }
        }

        for(Group key : count.keySet()){ //for each key in frequency distribution
            for( Object asset: assets){ //for each asset player owns
                if ((asset instanceof Property) && count.get(key) == key.getMemberCount() &&
                        !((Property) asset).isCompletedSet() && ((Property) asset).getGroup() == key) {
                    //if asset is property and the property is part of a group where the number owned matches
                    //member count in group enum and property is not already marked as a complete set
                    ((Property) asset).setCompletedSet(true);
                    ((Property) asset).updateRent();
                } else if ((asset instanceof Property) && count.get(key) != key.getMemberCount() &&
                        ((Property) asset).isCompletedSet() && ((Property) asset).getGroup() == key) {
                    //if asset is property and the group count is incorrect for property's group's member count
                    //and property is marked as complete set
                    ((Property) asset).setCompletedSet(false);
                    ((Property) asset).updateRent();
                }
            }
        }
    }


    /**
     * player method to develop properties if able
     */
    public void developProperties() {

        if (improvableProperties().size() == 0) {
            return;//no properties to develop
        }
        if (!isAiAgent()) {
            //TODO GUI develop properties
        } else {
            //AI decision
            if (decideDevelop()) {
                agentDevelopProperties();//AI developing logic
            }
        }
    }

    /**
     * Activate GUI input for user raising funds by selling assets
     *
     * @param amount the amount the player must raise
     */
    public void assetSelling(int amount) {
        if (!isAiAgent()) {//if human player to insure player is not AI
            board.callAssetSellingScene(this, amount);
        }
    }

    /**
     * GUI method to only show improvable properties to user
     *
     * @return list or improvable properties
     */
    public LinkedList<Property> improvableProperties() {
        //loop through assets, finding all properties
        LinkedList<Property> properties = new LinkedList<>();
        for (Object asset : assets) {
            if (asset instanceof Property) {
                //if potentially improvable (part of complete set and doesn't have a hotel built and isn't currently mortgaged)
                if (((Property) asset).isCompletedSet() && ((Property) asset).getHotelNo() != 1 && !((Property) asset).isMortgaged()) {
                    properties.add((Property) asset);//add development ready tiles
                }
            }
        }
        return properties;
    }


    /**
     * Finds the net worth of the player by adding resell value of all assets to current money
     *
     * @return net worth
     */
    public int netWorth() {
        int netWorth = 0;
        //loop through assets
        for( Object asset : assets ){
            //property
            if( asset instanceof Property ){
                //mortgaged?
                if( ((Property) asset).isMortgaged() ){
                    netWorth += ( (((Property) asset).getCost())/2 );
                } else {
                    if( ((Property) asset).isDeveloped() ){
                        //add resell value of all properties
                        if( ((Property) asset).getHousesNo() >= 1){
                            netWorth += (((Property) asset).getHousesNo() * ((Property) asset).getGroup().getBuildingCost());
                        } else if( ((Property) asset).getHotelNo() == 1){
                            netWorth += (((Property) asset).getGroup().getBuildingCost() * 5);
                        }
                    }
                    //property worth
                    netWorth += ((Property) asset).getCost();
                }
            } else if( asset instanceof Station ){
                netWorth += ((Station) asset).getCost();
            } else if( asset instanceof Utility ){
                netWorth += ((Utility) asset).getCost();
            }
        }

        return netWorth + money;
    }

    /**
     * Removes player from turnorder and transfers all assets to bank ownership
     */
    private void bankrupt() {
        //remove player from turnorder
        addAction("Bankrupt");
        board.turnOrder.remove(this);
        board.repeat = false;
        //transfer all assets to bank ownership
        for (Object asset : assets) {
            if (asset instanceof GetOutOfJail) {
                ((GetOutOfJail) asset).returnCard();
            } else if (asset instanceof Property) {
                ((Property) asset).setCompletedSet(false);
                ((Property) asset).setDeveloped(false);
                ((Property) asset).setHotelNo(0);
                ((Property) asset).setHousesNo(0);
                ((Property) asset).updateRent();
                ((Property) asset).setOwner(null);
            } else if (asset instanceof Station) {
                ((Station) asset).setOwner(null);
            } else if (asset instanceof Utility) {
                ((Utility) asset).setOwner(null);
            }
        }
    }


    /**
     * Finds all properties with the same group as the passed property in the player's asset list
     *
     * @param property, a property tile on the board
     */
    private ArrayList<Property> findGroups( Property property ) {
        ArrayList<Property> group = new ArrayList<>();
        //find property group
        for( Object asset: assets){
            if(asset instanceof Property){
                if(((Property) asset).getGroup() == property.getGroup()){
                    group.add((Property) asset);
                }
            }
        }

        return group;
    }

    /**
     * Processes player request to leave the game
     */
    public void leaveGame() {
        //ask user
        if (!isAiAgent()) {//ask human players only
            boolean confirm = true;//TODO change this to get user decision from GUI
            boolean possible;

            //get user votes
            if (confirm) {//if player wishes to leave
                possible = board.getLeaveVotes(this);//ask other players
                if (possible) {//if all player agree
                    bankrupt(); //remove from turn order and transfer all assets
                }
            }
        }
    }

    /*
     ---------Artificial Agent Methods---------
     For jail decisions see jail method
     */

    /**
     * Gets the traits of an AI for testing purposes potential to add traits that update during the game
     *
     * @return Trait object Agents personality setup
     */
    public Trait getPersonality() {
        return personality;
    }

    /**
     * Cautious personality cost verses network assessment
     *
     * @param cost amount of money to decide about
     * @return true|false spend|don't spend
     */
    public boolean cautiousWillBuy(int cost) {
        double threshold;
        double cash = money;
        threshold = 1 - (cost / cash);//cost/cash = 1 when cost=cash
        double roll = ThreadLocalRandom.current().nextDouble(0, 1);
        //When net worth is high and cost is low threshold will be almost 1 and random number will likely be lower
        return roll < threshold;
        //When cost is high and net worth is low threshold is low and random number will likely be higher
    }

    /**
     * Property purchase decision for AI
     *
     * @param property property to decide based on
     * @return true|false buy|do not buy
     */
    public boolean decide(Property property) {
        Group propGroup = property.getGroup();
        if (money < property.getCost()) {
            return false;//AI cannot afford the property
        } else if (money > 1000) {//TODO tune this value, requires testing
            return true;//player has a lot of money always buy
        } else if (personality.hasTwoSetAffinity()) {

            if (propGroup == Group.BROWN || propGroup == Group.DEEP_BLUE) {
                return true;//buy this property AI has affinity for it
            }
        } else if (personality.isPlanner()) {
            if (personality.getGroupA() == null) {
                personality.setGroupA(propGroup);//assign purchased property group to one of groups to track
                return true; //purchase property
            } else if (personality.getGroupB() == null) {
                personality.setGroupB(propGroup);//assign purchased property group to one of groups to track
                return true; //purchase property

            } else if (personality.getGroupC() == null) {
                personality.setGroupC(propGroup);//assign purchased property group to one of groups to track
                return true; //purchase property
            } else {
                if (propGroup == personality.getGroupA() || propGroup == personality.getGroupB() || propGroup == personality.getGroupC()) {
                    return true;//property is a member of AIs plan groups so buy tile
                }
            }
        } else if (personality.isCautious()) {
            return cautiousWillBuy(property.getCost());//performs cost against net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {//no driving trait
            return true;//player has money buy
        }
        return false;
    }

    /**
     * AI purchase decision on train stations
     *
     * @param station station for the agent to decide to purchase or not
     * @return purchase|!purchase true|false
     */
    public boolean decide(Station station) {
        if (money < station.getCost()) {
            return false;//cannot afford tile
        } else if (money > 1000) {//TODO tune this value, requires testing
            return true;//player has a lot of money always buy
        } else if (personality.hasTrainAffinity()) {
            return true;//player has affinity for stations
        } else if (personality.isCautious()) {
            return cautiousWillBuy(station.getCost());//cost net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;//player has money buy
        }
    }


    /**
     * AI purchase decision on train stations
     *
     * @param utility utility tile for agent to decide to purchase or not
     * @return purchase|!purchase true|false
     */
    public boolean decide(Utility utility) {
        if (money < utility.getCost()) {
            return false;//cannot afford tile
        } else if (money > 1000) {//TODO tune this value, requires testing
            return true;//player has a lot of money always buy
        } else if (personality.hasUtilityAffinity()) {
            return true;//player has affinity for utilities
        } else if (personality.isCautious()) {
            return cautiousWillBuy(utility.getCost());//cost net worth judgement
        } else if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;//player has money buy
        }
    }

    /***
     * Return a list of trade-able asset for the selected player
     * @param p Selected player
     * @return A trade-able assets for the selected player
     */
    public LinkedList<Object> tradeableAssets( Player p ) {
        LinkedList<Object> owned = new LinkedList<>();

        for (Object item : p.getAssets()) {
            if (item instanceof Property) {
                owned.add(item);
            } else if (item instanceof Station) {
                owned.add(item);
            } else if (item instanceof Utility) {
                owned.add(item);
            }
        }
        return owned;
    }

    /**
     * Creates a list of all assets owned by other players that are can be traded
     *
     * @return object list of (Station|Utility|Property)*
     */
    public LinkedList<Object> ownedAssets() {
        LinkedList<Object> owned = new LinkedList<>();
        for (Player p : board.turnOrder) {
            if (p != this) {//ignore this agent
                for (Object item : p.getAssets()) {
                    if (item instanceof Property) {
                        if (!(((Property) item).isCompletedSet())) {//do not include complete set items
                            owned.add(item);
                        }
                    } else if (item instanceof Station) {
                        owned.add(item);
                    } else if (item instanceof Utility) {
                        owned.add(item);
                    }
                }
            }
        }
        return owned;
    }

    /**
     * Create a list of each asset the agent wants that other players own
     *
     * @return item list (Station|Utility|Property) owned assets
     */
    public LinkedList<Object> wantedItems() {
        LinkedList<Object> wanted = new LinkedList<>();
        for (Object item : ownedAssets()) {//for each item owned by another player
            if (item instanceof Property) {
                Group itemGroup = ((Property) item).getGroup();
                if (personality.hasTwoSetAffinity()) {
                    if (itemGroup == Group.BROWN || itemGroup == Group.DEEP_BLUE) {
                        wanted.add(item);
                    }
                } else if (personality.isPlanner()) {
                    if (itemGroup == personality.getGroupA() || itemGroup == personality.getGroupB() || itemGroup == personality.getGroupC()) {
                        wanted.add(item);
                    }
                } else if (hasGroupMember(itemGroup)) {
                    wanted.add(item);
                }
            } else if (item instanceof Station) {
                if (personality.hasTrainAffinity()) {
                    wanted.add(item);
                }
            } else if (item instanceof Utility) {
                if (personality.hasUtilityAffinity()) {
                    wanted.add(item);
                }
            }
        }
        return wanted;
    }

    public void initiateTrade() {
        LinkedList<Object> offerItems = willTrade();//items agent is willing to trade
        LinkedList<Object> desiredItems = wantedItems();//items agent wants that another player owns
        //if agent has items it is willing to trade
        if (offerItems.size() > 0 && desiredItems.size() > 0) {
            Collections.shuffle(offerItems);//randomise offer and desired order and pick first in each
            Collections.shuffle(desiredItems);
            LinkedList<Object> give = new LinkedList<>();
            LinkedList<Object> take = new LinkedList<>();
            Object desiredItem = desiredItems.removeFirst();
            give.add(offerItems.removeFirst());
            take.add(desiredItem);
            Player tradeTarget = null;
            if (desiredItem instanceof Property) {
                tradeTarget = ((Property) desiredItem).getOwner();
            } else if (desiredItem instanceof Station) {
                tradeTarget = ((Station) desiredItem).getOwner();
            } else if (desiredItem instanceof Utility) {
                tradeTarget = ((Utility) desiredItem).getOwner();
            }
            //AI agents only offer trades to other AIs
            if (tradeTarget != null && tradeTarget.isAiAgent()) {
                if (tradeTarget.decide(take, give)) {
                    BoardTile get = (BoardTile) take.getFirst();
                    BoardTile lose = (BoardTile) give.getFirst();
                    tradeTarget.removeAsset(get);//remove asset from target player
                    addAsset(get);//get the desired item
                    removeAsset(lose);//remove item from lost
                    tradeTarget.addAsset(lose);//give item in return
                    completeSetProperties();
                    tradeTarget.completeSetProperties();
                    addAction("Trade " + get.getTitle() + " for " + lose.getTitle());
                }
            }
        }
    }

    /**
     * List of assets that the AI agent is willing to part with
     *
     * @return assets that agent will trade (Station|Utility|Property)*
     */
    public LinkedList<Object> willTrade() {
        LinkedList<Object> tradeAssets = new LinkedList<>();
        for (Object item : assets) {
            if (item instanceof Property) {
                Group itemGroup = ((Property) item).getGroup();
                if (!(((Property) item).isCompletedSet())) {//do not add complete set properties
                    if (personality.hasTwoSetAffinity()) {
                        if (itemGroup == Group.BROWN || itemGroup == Group.DEEP_BLUE) {
                            continue;//has affinity skip property
                        }
                    } else if (personality.isPlanner()) {
                        if (itemGroup == personality.getGroupA() || itemGroup == personality.getGroupB() || itemGroup == personality.getGroupC()) {
                            continue;//has affinity skip property
                        }
                    }
                    tradeAssets.add(item);//Has no affinity add property
                }
            } else if (item instanceof Station) {
                if (!(personality.hasTrainAffinity())) {
                    tradeAssets.add(item);//no affinity for stations add to list
                }
            } else if (item instanceof Utility) {
                if (!(personality.hasUtilityAffinity())) {
                    tradeAssets.add(item);//no affinity for Utilities add to list
                }
            }//else item is a card ignore it
        }
        return tradeAssets;
    }

    public boolean hasGroupMember(Group g) {
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).getGroup() == g) {
                    return true;//player has property of group g
                }
            }
        }
        return false;
    }

    /**
     * Trade decision making
     * Agent will only accept 1 item for 1 item trades
     * Agent will only accept items that they have an affinity
     * if Agent is wild card they will accept trades Chaotically given trade no other trait interaction has occurred
     *
     * @param give    what the AI will give to player
     * @param receive what the AI will receive get from player
     * @return true|false accept|decline
     */
    public boolean decide(LinkedList<Object> give, LinkedList<Object> receive) {
        if (give.size() != 1 || receive.size() != 1) {
            return false;//only accepts 1 for 1 trades
        }
        Object lose = give.getFirst();//1 for 1 trade valid set these objects
        Object get = receive.getFirst();
        if (!willTrade().contains(lose)) {//if lose is not an item agent is willing to trade
            return false;
        } else if (get instanceof Property) {
            Group getGroup = ((Property) get).getGroup();
            if (lose instanceof Property && ((Property) lose).getGroup() == getGroup) {
                return false;//no point swapping tiles of same group
            }
            if (personality.hasTwoSetAffinity()) {
                if (getGroup == Group.BROWN || getGroup == Group.DEEP_BLUE) {
                    return true;//Agent has affinity for item
                }
            } else if (personality.isPlanner()) {
                if (getGroup == personality.getGroupA() || getGroup == personality.getGroupB() || getGroup == personality.getGroupC()) {
                    return true;//Agent has planned to collect properties of this group
                }
            } else {
                if (hasGroupMember(getGroup)) {
                    return true;//player has one or more of these Properties
                }
            }
        } else if (get instanceof Station) {
            if (personality.hasTrainAffinity()) {
                return true;//Agent has affinity for item
            }
        } else if (get instanceof Utility) {
            if (personality.hasUtilityAffinity()) {
                return true;//Agent has affinity for item
            }
        }
        if (personality.isWildCard()) {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip for remaining personalities
                return true;
            } else {
                return false;
            }
        }
        return true; //default
    }

    /**
     * Decision to bid on an item in auction
     *
     * @param item       Property|Utility|Station that is up for auction
     * @param highestBid The current highest bid to help the agent decide if they want to out bid
     * @return The amount the agent is bidding, 0 indicates no bid
     */
    public int auctionDecide(Object item, int highestBid) {
        if (highestBid + 50 > money) {
            return 0;//Agent does not have enough to out bid
        } else if (item instanceof Station) {
            if (personality.hasTrainAffinity()) {//check for train affinity
                return highestBid + 50;//place bid for item
            }
        } else if (item instanceof Utility) {
            if (personality.hasUtilityAffinity()) {//check for utility affinity
                return highestBid + 50;
            }
        } else if (item instanceof Property) {
            Group itemGroup = ((Property) item).getGroup();
            if (personality.hasTwoSetAffinity()) {//Check for property preference
                if (itemGroup == Group.BROWN || itemGroup == Group.DEEP_BLUE) {
                    return highestBid + 50;
                }
            } else if (personality.isPlanner()) {
                if (itemGroup == personality.getGroupA() || itemGroup == personality.getGroupB() || itemGroup == personality.getGroupC()) {
                    return highestBid + 50;
                }
            } else if (personality.isCautious()) {
                if (cautiousWillBuy(highestBid + 50)) {//cost judgement based on current money
                    return highestBid + 50;
                }
            } else if (personality.isWildCard()) {
                if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip
                    return highestBid + 50;
                }
            } else {//No driving trait
                if (money > 1000) {//always bid if money is high
                    return highestBid + 50;
                }
            }

        }
        return 0; //No bid was made
    }

    /**
     * AI decision to pay £10 tax or draw opportunity knocks
     *
     * @return tax = true, draw = false
     */
    public boolean payTaxOrDraw() {
        if (money < 10) {
            return false;//Can't afford to pay draw a card
        } else if (personality.isCautious()) {
            return cautiousWillBuy(10);//based on net worth cost ratio
        } else if (personality.isWildCard()) {
            return false;//wild cards take chances
        }
        if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip for remaining personalities
            return true;
        } else {
            return false;
        }
    }

    /**
     * AI development decision
     *
     * @return true|false develop|don't develop
     */
    public boolean decideDevelop() {
        if (personality.isInvestor()) {
            return true;//investors will always attempt to develop
        } else if (money > 1500) {//TODO tune this value, requires testing
            return true;//player has a lot of money develop
        } else if (personality.isCautious()) {
            return cautiousWillBuy(125);//cost verses net worth using average building cost
        } else {
            if (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5) {//coin flip for remaining personalities
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Property developing procedure for AI
     */
    public void agentDevelopProperties() {
        addAction("Develop Properties");
        LinkedList<Property> properties = new LinkedList<>();
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).isCompletedSet() && ((Property) item).getHotelNo() != 1) {
                    properties.add((Property) item);//add development ready tiles
                }
            }
        }
        boolean loop;
        if (personality.isInvestor()) {
            loop = true;
            boolean housePurchased;
            boolean hotelPurchased;
            while (loop && money > properties.peekFirst().getGroup().getBuildingCost()) {//while can develop
                hotelPurchased = false;
                housePurchased = false;
                for (Property p : properties) {
                    if (p.purchaseHouse()) {
                        housePurchased = true;
                        break;//building purchased exit loop
                    } else if (p.purchaseHotel()) {
                        hotelPurchased = true;
                        break;//building purchased exit loop
                    }
                }
                if (!hotelPurchased && !housePurchased) {
                    loop = false;//failed to buy house or hotel end development
                } else if (money < 100) {
                    loop = false;
                }
            }
        } else if (personality.isCautious()) {
            while (cautiousWillBuy(properties.peekFirst().getGroup().getBuildingCost())) {//while willing to spend
                for (Property p : properties) {
                    if (p.purchaseHouse()) {
                        break;//building successfully purchased end for loop
                    } else if (p.purchaseHotel()) {
                        break;//building successfully purchased end for loop
                    }
                }
            }
        } else {
            //wild card and no trait random actions
            while (ThreadLocalRandom.current().nextDouble(0, 1) > 0.5 && money > properties.peekFirst().getGroup().getBuildingCost()) {//while coin flip successful
                for (Property p : properties) {//attempt to build 1 building
                    if (p.purchaseHouse()) {
                        break;//building successfully purchased end for loop
                    } else if (p.purchaseHotel()) {
                        break;//building successfully purchased end for loop
                    }
                }
            }
        }
    }

    /**
     * Sell building for AI agent.
     * When provided a developed property group buildings will be sold within building restrictions
     *
     * @param group property group with buildings to sell
     * @return the amount from the sale
     */
    public int agentSellBuilding(Group group) {
        int houseCount = 0;
        Property hasMostHouses = null;
        for (Object item : assets) {
            if (item instanceof Property && ((Property) item).getGroup() == group) {//property of desired
                if (((Property) item).getHotelNo() == 1) {
                    return ((Property) item).sellHouseOrHotel();//sell hotel
                } else {
                    if (houseCount < ((Property) item).getHousesNo()) {
                        houseCount = ((Property) item).getHousesNo();
                        hasMostHouses = (Property) item;
                    }
                }
            }
        }
        if (hasMostHouses != null) {
            return hasMostHouses.sellHouseOrHotel();
        } else {
            return 0;//nothing was sold
        }
    }

    public boolean hasDeveloped(Group g) {
        for (Object item : assets) {
            if (item instanceof Property && ((Property) item).getGroup() == g) {
                if (((Property) item).isDeveloped()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sell a single item to raise funds
     *
     * @return amount raised from sale
     */
    public int agentSellItem() {
        //Sell utilities
        for (Object item : assets) {
            if (item instanceof Utility) {
                return ((Utility) item).sellUtility();
            }
        }
        //sell stations
        for (Object item : assets) {
            if (item instanceof Station) {
                return ((Station) item).sellStation();
            }
        }
        //sell non set properties
        for (Object item : assets) {
            if (item instanceof Property) {
                if (!(((Property) item).isCompletedSet())) {
                    int propertyAmount = ((Property) item).sellProperty();
                    completeSetProperties();
                    return propertyAmount;
                }
            }
        }
        //sell buildings
        for (Object item : assets) {
            if (item instanceof Property && hasDeveloped(((Property) item).getGroup())) {
                if (((Property) item).isCompletedSet()) {
                    int buildingSaleAmount = agentSellBuilding(((Property) item).getGroup());//try to sell building
                    if (buildingSaleAmount != 0) {
                        return buildingSaleAmount;//building sold return amount
                    }
                }
            }
        }
        //sell set properties
        for (Object item : assets) {
            if (item instanceof Property) {
                if (((Property) item).isCompletedSet() && !(((Property) item).isDeveloped())) {
                    int propertyAmount = ((Property) item).sellProperty();//sell property
                    completeSetProperties();
                    return propertyAmount;
                }
            }
        }
        return 0;//Could not sell item
    }

    /**
     * Asset Selling loop for agent
     * Sells items one at a time until funds have been raised
     *
     * @param amount amount to be raised
     */
    public void agentSellAssets(int amount) {
        addAction("Sell assets to raise £" + amount);
        int amountRaised = 0;//amount from selling assets
        int count = 0;
        while (amountRaised < amount) {
            count++;
            amountRaised += agentSellItem();
        }
        payPlayerAmount(amountRaised);//give AI agent amount raised
        completeSetProperties();
    }

    public void agentRetire() {
        for (Player p : board.turnOrder) {
            if (!p.isAiAgent()) {
                return;//Only allow agents to retire if this is an AI game
            }
        }
        bankrupt();//Ai agent leaves game
    }
}