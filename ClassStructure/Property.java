package ClassStructure;
/**
 * @author Ayman Bensreti
 * Structure for property objects
 */
public class Property extends BoardTile{
    Group group;
    int cost;
    int rent;
    int[] buildingRents;
    int houseCost;
    int housesNo;
    int hotelNo;
    int hotelCost;
    Player owner;
    boolean mortgaged;
    boolean completedSet;

    /**
     * Creation of new property
     * @param iD tile ID
     * @param title Property title
     * @param group colour group
     * @param cost tile cost
     * @param rent tile rent
     * @param buildingRents building rents array
     * @param houseCost single house cost
     * @param hotelCost single hotel cost
     * @param owner player that owns the property
     */
    public Property(int iD, String title,Group group, int cost, int rent, int[] buildingRents, int houseCost, int hotelCost, Player owner) {
        this.iD = iD;
        this.title = title;
        this.group = group;
        this.cost = cost;
        this.rent = rent;
        this.buildingRents = buildingRents;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        this.owner = owner;
        this.canPurchase = true;
        this.mortgaged = false;
        this.completedSet = false;
        housesNo = 0;
        hotelNo = 0;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        //TODO write deducting rent from current player and paying to owner
    }

    public int getHousesNo() {
        return housesNo;
    }

    public int getHotelNo() {
        return hotelNo;
    }
}