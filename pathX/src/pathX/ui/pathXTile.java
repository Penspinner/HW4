package pathX.ui;

import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Steven Liao
 */
public class pathXTile extends Sprite
{
    private String name;
    
    // COORDINATES OF THE TARGETED LOCATION
    private float targetX;
    private float targetY;
    
    private boolean movingToTarget;
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
    
    public void setTarget(int initTargetX, int initTargetY)
    {
        targetX = initTargetX;
        targetY = initTargetY;
    }
    
    public boolean isMovingToTarget()
    {
        return movingToTarget;
    }
    
    public void startMovingToTarget(int maxVelocity)
    {
                // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY/diffX;
        float angleInRadians = (float)Math.atan(tanResult);
        
        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float)(maxVelocity * Math.cos(angleInRadians));
        
        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) vX *= -1;
        if ((diffX > 0) && (vX < 0)) vX *= -1;
        
        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float)(maxVelocity * Math.sin(angleInRadians));        
        
        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) vY *= -1;
        if ((diffY > 0) && (vY < 0)) vY *= -1;
    }
    
    @Override
    public void update(MiniGame game)
    {
        super.update(game);
    }
}
