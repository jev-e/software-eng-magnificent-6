package ClassStructure;
/**
 * @author Ayman Bensreti
 *	an enum for property groups
 *  name of group
 *  Building cost, cost of house = building cost, cost of hotel = building cost + removal of 4 houses
 *  building costs are dependant on colour groups only and have been removed from property structure
 *	display colour (To be added)
 */
public enum Group {
    BLUE("Blue",50),
    BROWN("Brown",50),
    DEEP_BLUE("Deep Blue",200),
    GREEN("Green",200),
    ORANGE("Orange",100),
    PURPLE("Purple",100),
    RED("Red",150),
    YELLOW("Yellow",150);

    private String name;
    private int buildingCost;

     Group(String name,int buildingCost) {
        this.name = name;
        this.buildingCost = buildingCost;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Fetches the building cost for use in property functionality 
     * @return int associated building cost with the colour group
     */
    public int getBuildingCost() {
        return buildingCost;
    }
}