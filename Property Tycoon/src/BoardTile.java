/**
 * 
 * @author Ayman Bensreti
 * an interface for board tile objects
 *
 */
public interface BoardTile {
	
	int iD = 0;
	String title = null;
	boolean canPurchase = false;
	
	/**
	 * Handles the desired effect for card or tiles
	 */
	public void activeEffect();
}
