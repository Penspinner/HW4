package pathX.data;

/**
 *
 * @author Dell
 */
public class Police 
{
    // POLICE LOCATION
    public int x;
    public int y;
    
    public Police(int initX, int initY)
    {
        x = initX;
        y = initY;
    }
    
    // ACCESSOR METHODS
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
}
