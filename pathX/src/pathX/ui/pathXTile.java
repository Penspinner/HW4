package pathX.ui;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathX.pathXConstants.*;
import pathX.data.Intersection;

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
    
    private int pathIndex;
    
    private ArrayList<Intersection> path;
    
    private Intersection currentIntersection;
    
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
    
    public Intersection getCurrentIntersection()
    {
        return currentIntersection;
    }
    
    public ArrayList<Intersection> getPath()
    {
        return path;
    }
    
    public void setCurrentIntersection(Intersection currentIntersection)
    {
        this.currentIntersection = currentIntersection;
    }
    
    public void setTarget(int initTargetX, int initTargetY)
    {
        targetX = initTargetX;
        targetY = initTargetY;
    }
    
    public void resetIndex()
    {
        pathIndex = 0;
    }
    
    public boolean isMovingToTarget()
    {
        return movingToTarget;
    }
    
    public void initPath(ArrayList<Intersection> initPath)
    {
        path = new ArrayList(initPath.size());
        
        for (Intersection i : initPath)
        {
            path.add(i);
        }
        
        setTarget(path.get(pathIndex).x - INTERSECTION_RADIUS, path.get(pathIndex).y - INTERSECTION_RADIUS);
        startMovingToTarget(10);
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
    
    /**
     * This method calculates the distance from this tile's current location
     * to the target coordinates on a direct line.
     * 
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget()
    {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = targetX - x;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = targetY - y;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
    }
    
    @Override
    public void update(MiniGame game)
    {
        if (calculateDistanceToTarget() < INTERSECTION_RADIUS)
        {
            
            movingToTarget = false;
            vX = 0;
            vY = 0;
            
            if (pathIndex < path.size() - 1)
            {
                pathIndex++;
                x = targetX;
                y = targetY;
                
                targetX = path.get(pathIndex).x - INTERSECTION_RADIUS;
                targetY = path.get(pathIndex).y - INTERSECTION_RADIUS;

                startMovingToTarget(10);
            } else
            {
                resetIndex();
            }
        }
        super.update(game);
    }
}
