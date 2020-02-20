/**
 * 
 * @author Ayman Bensreti
 * an interface for property objects
 */
public interface Property {
	Group group = null;
	int cost = 0;
	int rent = 0;
	int[] buildingRents  = null;
	int houseCost = 0;
	int housesNo = 0;
	int hotelCost = 0;
	Player owner = null;
	boolean mortgaged = false;
	boolean completedSet = false;
}
