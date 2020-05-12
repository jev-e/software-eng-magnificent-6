package ClassStructure;
/**
 * @author Ayman Bensreti
 *	an enum for player tokens
 *	name of token
 *  image path
 *	Text symbol for prototype versions
 */
public enum Token {
    CAT("Cat","Lib/Tokens/CatToken.png","@"),
    HATSTAND("Hat Stand","Lib/Tokens/HatstandToken.png","%"),
    SMARTPHONE("Smartphone","Lib/Tokens/PhoneToken.png","$"),
    BOOT("Boot","Lib/Tokens/BootToken.png","£"),
    GOBLET("Goblet","Lib/Tokens/GobletToken.png","&"),
    SPOON("Spoon","Lib/Tokens/SpoonToken.png","*");


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