package pathX.data;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathX.pathXConstants.*;
import pathX.ui.pathXMiniGame;
/**
 *
 * @author Dell
 */
public class Police extends Sprite
{
    private Intersection firstIntersection;
    
    private Intersection nextIntersection;
    
    // COORDINATES OF THE TARGETED LOCATION
    private float targetX;
    private float targetY;
    
    private boolean movingToTarget;
    private boolean collided;
    
    public Police(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
    }
    
    // ACCESSOR METHODS
    public boolean isMovingToTarget()           {   return movingToTarget;}
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
    public void setTarget(int initTargetX, int initTargetY)
    {
        targetX = initTargetX;
        targetY = initTargetY;
    }
    public void setNextIntersection(Intersection nextIntersection)
    {
        this.nextIntersection = nextIntersection;
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
    
    public void initPathToFirstIntersection(Intersection intersection, int speedLimit)
    {
        firstIntersection = intersection;
        
        setTarget(intersection.x - INTERSECTION_RADIUS, intersection.y - INTERSECTION_RADIUS);
        startMovingToTarget(speedLimit / 20);
    }
    
    public void initCurrentIntersection(Intersection firstIntersection)
    {
        this.firstIntersection = firstIntersection;
        x = firstIntersection.x;
        y = firstIntersection.y;
    }
    
    public void moveRandomly(MiniGame game)
    {
        x = targetX;
        y = targetY;
        pathXMiniGame miniGame = (pathXMiniGame) game;
        ArrayList<Intersection> neighbors = ((pathXDataModel)game.getDataModel()).getNeighbors(firstIntersection);
        int random = (int) (Math.random() * neighbors.size());
        nextIntersection = neighbors.get(random);
        setTarget(nextIntersection.x, nextIntersection.y);
        Road roadInBetween = ((pathXDataModel)game.getDataModel()).getRoad(firstIntersection, nextIntersection);
        startMovingToTarget(roadInBetween.speedLimit);
    }
    
    @Override
    public void update(MiniGame game)    
    {
        if (calculateDistanceToTarget() < INTERSECTION_RADIUS)
        {
            vX = 0;
            vY = 0;
            movingToTarget = false;
        }
        else
        {
            super.update(game);
        }
    }
}
