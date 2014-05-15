package pathX.data;

import java.util.ArrayList;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathX.pathXConstants.*;

/**
 *
 * @author Dell
 */
public class Bandit extends Sprite
{
    private ArrayList<Intersection> path;
    
    // COORDINATES OF THE TARGETED LOCATION
    private float targetX;
    private float targetY;
    
    private boolean movingToTarget;
    private boolean collided;
    private boolean movingBackToFirst;
    
    private Intersection currentIntersection;
    
    private float speed;
    
    private int money;
    
    private int pathIndex;
    
    public Bandit(SpriteType initSpriteType,
            float initX, float initY,
            float initVx, float initVy,
            String initState)
    {
        // SEND ALL THE Sprite DATA TO A Sprite CONSTRUCTOR
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        path = new ArrayList();
        movingToTarget = false;
        collided = false;
        movingBackToFirst = false;
        
        speed = 1;
        
        money = (int) Math.round(Math.random() * 100);
    }
    
    // ACCESSOR METHODS
    public ArrayList<Intersection> getPath()    {   return path;                }
    public boolean isMovingToTarget()           {   return movingToTarget;      }
    public boolean isCollided()                 {   return collided;            }
    public Intersection getCurrentIntersection(){   return currentIntersection; }
    public int getMoney()                       {   return money;               }
    public float getSpeed()                     {   return speed;               }
    
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
    public void setCurrentIntersection(Intersection currentIntersection)
    {   this.currentIntersection = currentIntersection; }
    public void setCollided(boolean collided)
    {   this.collided = collided;   }
    public void setSpeed(float speed)
    {   this.speed = speed;     }
    public void toggleCollided()
    {   collided = !collided;   }
    
    public void initPath(ArrayList<Intersection> initPath, pathXDataModel data)
    {
        path = new ArrayList(initPath.size());
        
        for (Intersection i : initPath)
        {
            path.add(i);
        }
        
        setTarget(path.get(pathIndex + 1).x, path.get(pathIndex + 1).y);
        Road roadInBetween = data.getRoad(currentIntersection, path.get(++pathIndex));
        startMovingToTarget(roadInBetween.getSpeedLimit() * data.getGameSpeed() * speed / 10);
    }
    
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
    
    /**
     * Stop moving to the target and places the tile on the Intersection.
     */
    public void stopMovingToTarget()
    {
        movingToTarget = false;
        vX = 0;
        vY = 0;
        
        currentIntersection = path.get(pathIndex);
        
        if (currentIntersection.open)
        {
            x = targetX;
            y = targetY;
        }
    }
    
    public void updatePath(MiniGame game)
    {
        Intersection nextIntersection = path.get(pathIndex);
        if (nextIntersection.open)
        {
            Road roadInBetween = ((pathXDataModel)game.getDataModel()).getRoad(currentIntersection, nextIntersection);
            float gameSpeed = ((pathXDataModel)game.getDataModel()).getGameSpeed();

            targetX = nextIntersection.x;
            targetY = nextIntersection.y;

            startMovingToTarget(roadInBetween.getSpeedLimit() * gameSpeed * speed / 10);
        }
    }
    
    @Override
    public void update(MiniGame game)
    {
        if (calculateDistanceToTarget() < INTERSECTION_RADIUS)
        {
            stopMovingToTarget();
            
            if (pathIndex < path.size() - 1 && !movingBackToFirst && currentIntersection.open)
            {
                pathIndex++;
                movingBackToFirst = pathIndex == path.size() - 1 ? true : false;
            } else if (pathIndex > 0 && currentIntersection.open)
            {
                pathIndex--;
                movingBackToFirst = pathIndex == 0 ? false : true;
            }
            updatePath(game);
        }
        super.update(game);
    }
}

