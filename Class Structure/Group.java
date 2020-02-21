/**
 *
 * @author Ayman Bensreti
 *	an enum for property groups
 *	name of group
 *	display colour (To be added)
 */
public enum Group {
    BLUE("Blue"),
    BROWN("Brown"),
    DEEP_BLUE("DeepBlue"),
    GREEN("Green"),
    ORANGE("Orange"),
    PURPLE("Purple"),
    RED("Red"),
    YELLOW("Yellow");

    private String name;

    private Group(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}