/**
 *
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
    int hotelCost;
    Player owner;
    boolean mortgaged;
    boolean completedSet;

    public Property(Group group, int cost, int rent, int[] buildingRents, int houseCost, int hotelCost, Player owner) {
        this.group = group;
        this.cost = cost;
        this.rent = rent;
        this.buildingRents = buildingRents;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        this.owner = owner;
    }

    @Override
    public void activeEffect(Player currentPlayer) {
        //TODO write deducting rent from current player and paying to
    }
}