package pathX.ui;

import java.util.HashMap;
import mini_game.Sprite;

/**
 *
 * @author Steven Liao
 */
public class pathXSpecials 
{
    private HashMap<String, Sprite> specials;
    
    private pathXMiniGame game;
    
    /**
     * Constructor to keep track of the specials
     * 
     * @param initGame 
     */
    public pathXSpecials(pathXMiniGame initGame)
    {
        game = initGame;
    }
    
    public void initSpecials()
    {
        
    }
    
    public void initSpecialsControls()
    {
        
    }
    
    public enum pathXSpecialsState
    {
        LOCKED_STATE,
        UNLOCKED_STATE
    }
}
