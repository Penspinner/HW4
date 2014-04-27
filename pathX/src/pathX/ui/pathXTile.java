package pathX.ui;

import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Steven Liao
 */
public class pathXTile extends Sprite
{
    private String name;
    /**
     * This constructor initializes this tile for use, including all the
     * sprite-related data from its ancestor class, Sprite.
     */
    public pathXTile(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState, String initName)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        // NAME OF THE TILE
        name = initName;
    }
    
    /**
     * Accessor method for getting the name of the tile.
     * 
     * @return name.
     */
    public String getName()
    {
        return name;
    }
}
