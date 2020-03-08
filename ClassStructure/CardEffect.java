package ClassStructure;
/**
 * @author Ayman Bensreti
 *	Card generic structure
 */
public abstract class CardEffect {
    String cardText;

    abstract void effect(Player currentPlayer);//potential need for additional parameter of player pool
}
