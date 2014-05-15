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
    private Intersection targetIntersection;
    
    // COORDINATES OF THE TARGETED LOCATION
    private float targetX;
    private float targetY;
    
    private boolean movingToTarget;
    private boolean collided;
    
    private float speed;
    
    private int money;
    
    public Police(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        movingToTarget = false;
        collided = false;
        
        speed = 1;
        
        money = (int) Math.round(Math.random() * 150);
    }
    
    // ACCESSOR METHODS
    public boolean isMovingToTarget()           {   return movingToTarget;  }
    public boolean isCollided()                 {   return collided;        }
    public Intersection getTargetIntersection() {   return targetIntersection;}
    public int getMoney()                       {   return money;           }
    public float getSpeed()                     {   return speed;           }
    
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
    public void setTargetIntersection(Intersection targetIntersection)
    {   this.targetIntersection = targetIntersection;   }
    public void setCollided(boolean collided)
    {   this.collided = collided;   }
    public void setSpeed(float speed)
    {   this.speed = speed;         }
    public void toggleColided()
    {   collided = !collided;   }
    
    public void startMovingToTarget(float maxVelocity)
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
    
    public void setCurrentIntersection(Intersection firstIntersection)
    {
        this.firstIntersection = firstIntersection;
        x = firstIntersection.x;
        y = firstIntersection.y;
    }
    
    @Override
    public void update(MiniGame game)    
    {
        if (calculateDistanceToTarget() < INTERSECTION_RADIUS)
        {
            movingToTarget = false;
            vX = 0;
            vY = 0;
            
            if (targetIntersection.open)
            {
                x = targetX;
                y = targetY;
            }
        }
        else
        {
            super.update(game);
        }
    }
}
