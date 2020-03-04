/**
 *
 * @author Ayman Bensreti
 *	an enum for player tokens
 *	name of token
 *	image path //TODO Paths to be added
 */

import java.lang.*;


public enum Token {
    CAT("Cat","CatToken.png"),
    HATSTAND("Hat Stand","HatstandToken.png"),
    SMARTPHONE("Smartphone","PhoneToken.png"),
    BOOT("Boot","BootToken.png"),
    GOBLET("Goblet","GobletToken.png"),
    SPOON("Spoon","SpoonToken.png");

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
    public String getPath() {
        return imgPath;
    }

}



