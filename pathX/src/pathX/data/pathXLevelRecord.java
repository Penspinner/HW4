package pathX.data;

/**
 *
 * @author Steven Liao
 */
public class pathXLevelRecord 
{
    // LOCATION X AND Y COORDINATES OF THE LEVEL
    private int x;
    private int y;
    
    private String levelName;
    private String state;
    
    public pathXLevelRecord(String initLevelName, int initX, int initY)
    {
        levelName = initLevelName;
        x = initX;
        y = initY;
    }
    
    // ACCESSOR METHODS
    public int getX()           {   return x;       }
    public int getY()           {   return y;       }
    public String getState()    {   return state;   }
    
    // MUTATOR METHODS
    public void setX(int x)     {   this.x = x;     }
    public void setY(int y)     {   this.y = y;     }
    public void setState(String state) {this.state = state;}
    
    @Override
    public String toString() 
    {
        return x + ", " + y;
    }
}
