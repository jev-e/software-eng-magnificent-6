package ClassStructure;
/**
 * @author Ayman Bensreti
 * Structure for property objects
 */
public class Property extends BoardTile{
    Group group;//enum colour group the tile belongs to
    int cost;//amount to be paid to purchase property. This is also the sell value
    int rent;//default rent of a property which developments are added to
    int[] buildingRents; //4 int length array, amounts to increase rent by per house [1 House,2 Houses,3 Houses, 4 Houses]
    int housesNo;
    int hotelNo;
    int hotelRent;//A given property may have only one hotel this functions the same as building rent
    Player owner;
    boolean mortgaged;//if property is mortgaged no rent is deducted. Can be restored by paying half the cost to the bank
    boolean completedSet;//If a single player owns all properties in group this is true and false otherwise

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
        housesNo = 0;
        hotelNo = 0;
    }

    /**
     * Property activation functionality, both unowned and owned
     * @param currentPlayer land on player, either able to purchase or subject to rent
     */
    @Override
    public void activeEffect(Player currentPlayer) {
        /*Involves checks for hotels and houses
        * if there is a full set
        * if the property is mortgaged
        * purchase functionality if not owned
        */
        //TODO write deducting rent from current player and paying to owner
    }




    public int getHousesNo() {
        return housesNo;
    }

    public int getValue () { return (this.cost + ((hotelNo + housesNo) * this.group.getBuildingCost()));}

    public String getTitle() { return title;}

    public int getHotelNo() {
        return hotelNo;
    }

    public Player getOwner(){
        return this.owner;
    }

    public void addHotel(){
        this.hotelNo += 1;
    }

    public void addHouse(){
        this.housesNo += 1;
    }

    public Boolean isDeveloped(){
        return(this.housesNo != 0 | this.hotelNo != 0);
    }

    /**
     * Clears the owner
     *///
    public void returnToBank() {
        owner = null;
    }

    /**
     * Function to sell houses and hotels on a property.
     * @return Either the cost of the building or 0 if their is no buildings to sell.
     */
    public int sellHouseOrHotel(){
        if(this.hotelNo == 1){
            this.hotelNo -= 1;
            return(group.getBuildingCost());
        }
        else if(this.housesNo > 0){
            this.housesNo -= 1;
            return(group.getBuildingCost());
        }
        else{
            return(0);
        }
    }

    /**
     * Function to allow for the ability to mortgage the property.
     * Will set the property to being mortgaged to stop rent being acquired
     * @return Half the value of the property.
     */
    public int mortgageProperty() {
        if (this.isDeveloped()) {
            return (0);
        }
        else if(this.mortgaged){
            return (0);
        }
        else{
            this.mortgaged = true;
            return(this.cost/2);
        }
    }

    /**
     * Function to sell the property. Will not be able to sell if property is developed, and will only sell at
     * half price when property is mortgaged.
     * @ 0 for developed properties, half of property cost if mortgaged and full property cost if not mortgaged.
     */
    public int sellProperty(){
        if (this.isDeveloped()) {
            return (0);
        }
        else if(this.mortgaged){
            this.returnToBank();
            this.mortgaged = false;
            return(this.cost/2);
        }
        else {
            this.returnToBank();
            return (this.cost);
        }
    }
}