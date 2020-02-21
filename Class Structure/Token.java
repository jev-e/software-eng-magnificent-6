/**
 *
 * @author Ayman Bensreti
 *	an enum for player tokens
 *	name of token
 *	image path //TODO Paths to be added
 */
public enum Token {
    CAT("Cat",""),
    HATSTAND("Hat Stand",""),
    SMARTPHONE("Smartphone",""),
    BOOT("Boot",""),
    GOBLET("Goblet",""),
    SPOON("Spoon","");

    private String name;
    private String imgPath;

    private Token(String name, String imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return name;
    }
}