package src;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Ayman Bensreti
 *	an enum for property groups
 *  name of group
 *  Building cost, cost of house = building cost, cost of hotel = building cost + removal of 4 houses
 *  building costs are dependant on colour groups only and have been removed from property structure
 *	display colour (To be added)
 */
public enum Group {
    BLUE("Blue",50, 3),
    BROWN("Brown",50, 2),
    DEEP_BLUE("Deep Blue",200, 2),
    GREEN("Green",200, 3),
    ORANGE("Orange",100, 3),
    PURPLE("Purple",100, 3),
    RED("Red",150, 3),
    YELLOW("Yellow",150, 3);

    private String name;
    private int buildingCost;
    private int memberCount;

    Group(String name,int buildingCost, int memberCount) {
        this.name = name;
        this.buildingCost = buildingCost;
        this.memberCount = memberCount;
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

    /**
     * Fetches the member count for use in the board class
     * @return int associated member count with the colour group
     */
    public int getMemberCount() { return memberCount; }

    /**
     * Fetches the name of the group for use in property colour displaying
     * @return String associated Colour
     */
    public String getName() { return name; }

    @JsonValue
    final Group value() {
        return this;
    }
}