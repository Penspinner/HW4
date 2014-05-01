package pathX.level_editor.model;

/**
 *
 * @author Dell
 */
public class Zombie 
{
    // ZOMBIE LOCATION
    public int x;
    public int y;
    
    public Zombie(int initX, int initY)
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
