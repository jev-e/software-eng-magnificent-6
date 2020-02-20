import java.util.LinkedList;

/**
 * 
 * @author Ayman Bensreti
 *	An interface for player objects
 */
public interface Player {
	int currentPos = 0;
	int previousPos = 0;
	String name = null;
	Token token = null;
	int money = 0; //Check brief for default value
	LinkedList<Object> assets = null;
	boolean canBuy = false;
	
	/**
	 * Alters the players balance
	 * @param amount positive or negative integer to modify the balance by
	 */
	public void alterBalance(int amount);
	
}
