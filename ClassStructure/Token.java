package ClassStructure;
/**
 * @author Ayman Bensreti
 *	an enum for player tokens
 *	name of token
 *  image path
 *	Text symbol for prototype versions
 */
public enum Token {
    CAT("Cat","CatToken.png","@"),
    HATSTAND("Hat Stand","HatstandToken.png","%"),
    SMARTPHONE("Smartphone","PhoneToken.png","$"),
    BOOT("Boot","BootToken.png","£"),
    GOBLET("Goblet","GobletToken.png","&"),
    SPOON("Spoon","SpoonToken.png","*");


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private String name;
    private String imgPath;
    private String symbol;//For text based versions only to be removed

     Token(String name, String imgPath, String symbol) {
        this.name = name;
        this.imgPath = imgPath;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return name;
    }
}