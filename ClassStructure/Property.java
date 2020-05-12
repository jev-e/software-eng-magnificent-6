package ClassStructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.LinkedList;

/**
 * @author Ayman Bensreti, Calvin Boreham
 * Structure for property objects
 */
public class Property extends BoardTile{
    private Group group;//enum colour group the tile belongs to
    private int cost;//amount to be paid to purchase property. This is also the sell value
    private int rent;//default rent of a property which developments are added to
    private int[] buildingRents; //4 int length array, amounts to increase rent by per house [1 House,2 Houses,3 Houses, 4 Houses]
    private int housesNo;
    private int hotelNo;
    private int hotelRent;//A given property may have only one hotel this functions the same as building rent
    private Player owner;
    private boolean mortgaged;//if property is mortgaged no rent is deducted. Can be restored by paying half the cost to the bank
    private boolean completedSet;//If a single player owns all properties in group this is true and false otherwise
    private boolean rentDoubled; //Flag representing whether the rent on this property has been doubled
    private boolean developed; //flag for if property improved

    // GUI Assets
    @JsonIgnore
    Label tilePrice = new Label();
    @JsonIgnore
    private Canvas colourDisplay = new Canvas(100,15); // Displays colour of property group

    /**
     * Default constructor for Jackson
     */
    public Property() {
        canPurchase = true;
        mortgaged = false;
        completedSet = false;
        rentDoubled = false;
        developed = false;
        owner = null;
    }

    /**
     * Creation of new property
     * @param iD tile ID
     * @param title Property title
     * @param group colour group
     * @param cost tile cost
     * @param rent tile rent
     * @param buildingRents building rents array
     * @param owner player that owns the property
     */
    public Property(int iD, String title,Group group, int cost, int rent, int[] buildingRents,int hotelRent, Player owner) {
        this.iD = iD;
        this.title = title;
        this.group = group;
        this.cost = cost;
        this.rent = rent;
        this.buildingRents = buildingRents;
        this.hotelRent = hotelRent;
        this.owner = owner;
        this.canPurchase = true;
        this.mortgaged = false;
        this.completedSet = false;
        this.rentDoubled = false;
        this.housesNo = 0;
        this.hotelNo = 0;
    }

    /**
     * classStructure.Property activation functionality, both unowned and owned
     * @param currentPlayer land on player, either able to purchase or subject to rent
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        currentPlayer.addAction("Landed on " + title);
        if (owner != null && owner != currentPlayer && !mortgaged && !owner.isInJail()) {
            //there is an owner that is allowed to collect rent,so collect rent
            collectRent(currentPlayer);
        } else if (owner == null) {
            if (currentPlayer.CanBuy()) {
                purchase(currentPlayer);
            }
        }
    }

    /**
     * Getter for number of houses on property
     * @return number of houses
     */
    public int getHousesNo() {
        return housesNo;
    }

    /**
     * Getter for number of hotels on property
     * @return number of hotels
     */
    public int getHotelNo() {
        return hotelNo;
    }

    /**
     * Getter for owner of property
     * @return owner player owner of property
     */
    public Player getOwner() { return owner; }

    /**
     * Getter for if property has been developed or not
     * @return boolean true if property developed, false otherwise
     */
    public boolean getDeveloped() { return developed; }


    /**
     * Function to sell houses and hotels on a property.
     * @return Either the cost of the building or 0 if their is no buildings to sell.
     */
    public int sellHouseOrHotel(){
        if (hotelNo == 1) {
            hotelNo = 0;
            housesNo = 4;
            rent -= hotelRent;
            return (group.getBuildingCost());
        } else if (canSellHouse()) {
            rent -= buildingRents[housesNo - 1];
            housesNo -= 1;
            if (housesNo == 0) {
                developed = false;
            }
            return (group.getBuildingCost());
        } else {
            return (0);
        }
    }

    /**
     * Function to allow for the ability to mortgage the property.
     * Will set the property to being mortgaged to stop rent being acquired
     * @return Half the value of the property.
     */
    public int mortgageProperty() {
        if ( developed ) {
            return (0);
        } else if( mortgaged ){
            return (0);
        } else {
            mortgaged = true;
            return( cost/2 );
        }
    }

    /**
     * method to un-mortgage the property
     */
    public void unmortgageProperty() {
        if( mortgaged && owner.getMoney() >= (cost/2)){
            owner.deductAmount(cost/2); //pay un mortgaging fee
            mortgaged = false; //update flag
        } else if( mortgaged && (owner.getMoney() < (cost/2))) {
            //System.out.println("Sorry, you cannot afford to do this");
        } else{
            //System.out.println("Property not currently mortgaged");
        }
    }

    /**
     * Function to sell the property. Will not be able to sell if property is developed, and will only sell at
     * half price when property is mortgaged.
     * @ 0 for developed properties, half of property cost if mortgaged and full property cost if not mortgaged.
     */
    public int sellProperty(){
        if ( developed ) {
            return (0);
        } else if( mortgaged ){
            owner.removeAsset(this);
            mortgaged = false;
            return(this.cost/2);
        } else {
            owner.removeAsset(this);
            return (this.cost);
        }
    }


    /**
     * Asks current player is they wish to purchase property, if so the ownership is transferred and purchase amount
     * deducted from player, if not an auction is triggered
     *
     * @param currentPlayer the player currently on property tile
     */
    private void purchase( Player currentPlayer) {

        boolean wishToPurchase = false; //flag for purchase choice

        if (!currentPlayer.isAiAgent()) {
            wishToPurchase = false;//TODO Change this to get player choice from GUI
        } else {
            wishToPurchase = currentPlayer.decide(this);
        }
        if (wishToPurchase) {
            if (cost > currentPlayer.getMoney()) {
                //no purchase can be made, trigger auction
                //TODO GUI auction activated here
            } else {
                //deduct purchase cost from player
                currentPlayer.deductAmount(cost);
                //transfer ownership
                owner = currentPlayer;
                currentPlayer.addAsset(this);
                currentPlayer.addAction("Purchased " + title + " for £" + cost);
            }
        } else {
            //trigger auction
            //TODO GUI auction activated here
        }

    }

    /**
     * Doubles rent if the board is part of a complete set and has no improvements, halves rent if board is no longer
     * in a complete set
     */
    public void updateRent() {
        if( housesNo == 0 && hotelNo == 0 && completedSet ){
            rent *= 2;
            rentDoubled = true;
        } else if( rentDoubled && !completedSet ){
            rent /= 2;
            rentDoubled = false;
        } else if ( rentDoubled && housesNo == 1 ){
            rent /= 2;
            rentDoubled = false;
        }
    }

    /**
     * Deducts rent amount for property from currentPlayer and pays rent amount to property owner
     *
     * @param currentPlayer the player currently on property tile
     */
    private void collectRent( Player currentPlayer) {
        //deduct rent from current player
        currentPlayer.addAction("Pay £" + rent + " rent to " + owner.getName());
        int amountPayed = currentPlayer.deductAmount( rent ); //deduct amount from player
        //give owner rent
        owner.payPlayerAmount( amountPayed ); //gives owner amount renter was able to pay (e.g. if bankrupt)
    }

    /**
     * Increments housesNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds, updates rent amount
     *
     * @return true|false based on successful purchase
     */
    public boolean purchaseHouse() {
        if (canBuyHouse()) {
            housesNo++;
            if (housesNo == 1) {
                developed = true;
                updateRent();
            }
            owner.deductAmount(group.getBuildingCost());
            rent += buildingRents[housesNo - 1];
            return true;
        } else {
            return false;
        }
    }

    /**
     * Increments hotelNo, removes money from players account. A check will have already been performed to ensure
     * sufficient funds
     *
     * @return true|false based on successful purchase
     */
    public boolean purchaseHotel() {
        if (canBuyHotel()) {
            //'sell' 4 houses
            hotelNo = 1;
            housesNo = 0;
            owner.deductAmount(group.getBuildingCost());
            rent += hotelRent;
            //System.out.println("Hotel purchased for " + title + ", the new rent is: £" + rent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Can a user currently build a house on this tile based on building rules
     *
     * @return can buy house true|false yes|no
     */
    public boolean canBuyHouse() {
        int desiredCount = housesNo + 1;//user wants to add one more house
        if (desiredCount > 4) {
            return false;//House limit reached
        } else {
            LinkedList<Property> setMembers = new LinkedList<>();
            LinkedList<Integer> houseCounts = new LinkedList<>();
            for (Object item : owner.getAssets()) {
                if (item instanceof Property && ((Property) item).getGroup() == group) {//if item is member of this set
                    setMembers.add((Property) item);//add item to set
                }
            }
            for (Property property : setMembers) {//fetch the house counts
                if (property.equals(this)) {
                    houseCounts.add(desiredCount);//this tile desired count is being tested
                } else if (property.getHotelNo() == 1) {
                    houseCounts.add(4);//hotel is treated like 4 houses
                } else {
                    houseCounts.add(property.getHousesNo());//get how houses on tile
                }
            }
            for (int countA : houseCounts) {
                for (int countB : houseCounts) {
                    if (Math.abs(countA - countB) > 1) {//if the difference between building is greater than 1
                        return false;//building violation
                    }
                }
            }
        }
        return true;//no violations found can build
    }

    /**
     * Check that selling a house on this tile will not violate building rules
     *
     * @return can sell house true|false yes|no
     */
    public boolean canSellHouse() {
        if (housesNo == 0) {
            return false;//no houses to sell
        } else {
            int desiredCount = housesNo - 1;//user wants to remove one house
            LinkedList<Property> setMembers = new LinkedList<>();
            LinkedList<Integer> houseCounts = new LinkedList<>();
            for (Object item : owner.getAssets()) {
                if (item instanceof Property && ((Property) item).getGroup() == group) {//if item is member of this set
                    setMembers.add((Property) item);//add item to set
                }
            }
            for (Property property : setMembers) {//fetch the house counts
                if (property.equals(this)) {
                    houseCounts.add(desiredCount);//this tile desired count is being tested
                } else if (property.getHotelNo() == 1) {
                    houseCounts.add(4);//hotel is treated like 4 houses
                } else {
                    houseCounts.add(property.getHousesNo());//get how houses on tile
                }
            }
            for (int countA : houseCounts) {
                for (int countB : houseCounts) {
                    if (Math.abs(countA - countB) > 1) {//if the difference between building is greater than 1
                        return false;//building violation
                    }
                }
            }
        }
        return true;//no violations can build
    }

    /**
     * Check if a hotel can be built on this tile based on other set members building counts
     *
     * @return can buy hotel true|false yes|no
     */
    public boolean canBuyHotel() {
        int desiredCount = 4;//user wants to exchange 4 houses for a hotel
        if (housesNo != 4 || hotelNo == 1) {
            return false;//Not enough houses or hotel already built
        } else {
            LinkedList<Property> setMembers = new LinkedList<>();
            LinkedList<Integer> houseCounts = new LinkedList<>();
            for (Object item : owner.getAssets()) {
                if (item instanceof Property && ((Property) item).getGroup() == group) {//if item is member of this set
                    setMembers.add((Property) item);//add item to set
                }
            }
            for (Property property : setMembers) {//fetch the house counts
                if (property.equals(this)) {
                    houseCounts.add(desiredCount);//this tile desired count is being tested
                } else if (property.getHotelNo() == 1) {
                    houseCounts.add(4);//hotel is treated like 4 houses
                } else {
                    houseCounts.add(property.getHousesNo());//get how houses on tile
                }
            }
            for (int countA : houseCounts) {
                for (int countB : houseCounts) {
                    if (Math.abs(countA - countB) > 1) {//if the difference between building is greater than 1
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int[] getBuildingRents() {
        return buildingRents;
    }

    public void setBuildingRents(int[] buildingRents) {
        this.buildingRents = buildingRents;
    }

    public void setHousesNo(int housesNo) {
        this.housesNo = housesNo;
    }

    public void setHotelNo(int hotelNo) {
        this.hotelNo = hotelNo;
    }

    public int getHotelRent() {
        return hotelRent;
    }

    public void setHotelRent(int hotelRent) {
        this.hotelRent = hotelRent;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }

    public boolean isCompletedSet() {
        return completedSet;
    }

    public void setCompletedSet(boolean completedSet) {
        this.completedSet = completedSet;
    }

    public boolean isRentDoubled() {
        return rentDoubled;
    }

    public void setRentDoubled(boolean rentDoubled) {
        this.rentDoubled = rentDoubled;
    }

    public boolean isDeveloped() {
        return developed;
    }

    public void setDeveloped(boolean developed) {
        this.developed = developed;
    }

    // GUI Functions

    // Label getter and setter
    public void setTilePrice() { this.tilePrice.setText(String.valueOf(tilePrice)); }

    public Label getTilePrice() { return this.tilePrice; }

    // Canvas Functions
    @Override
    public Canvas getColourDisplay() { return this.colourDisplay; }

    @Override
    public void setColour() {

        Paint temp;
        switch (this.group.name()) {
            case "BLUE":
                temp = Color.BLUE;
                break;
            case "BROWN":
                temp = Color.BROWN;
                break;
            case "DEEP_BLUE":
                temp = Color.DARKBLUE;
                break;
            case "GREEN":
                temp = Color.GREEN;
                break;
            case "ORANGE":
                temp = Color.ORANGE;
                break;
            case "PURPLE":
                temp = Color.PURPLE;
                break;
            case "RED":
                temp = Color.RED;
                break;
            case "YELLOW":
                temp = Color.YELLOW;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.group.name());
        }

        GraphicsContext gc = this.colourDisplay.getGraphicsContext2D();
        gc.setFill(temp);
        gc.fillRect(0, 0, 100, 15);
    }
}