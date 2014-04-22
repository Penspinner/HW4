/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathX.data;

/**
 *
 * @author Dell
 */
public class pathXLevel 
{
    // LOCATION X AND Y COORDINATES OF THE LEVEL
    private int x;
    private int y;
    
    private String levelName;
    private String state;
    
    public pathXLevel(String initLevelName, int initX, int initY)
    {
        levelName = initLevelName;
        x = initX;
        y = initY;
    }
    
    // ACCESSOR METHODS
    public int getX()           {   return x;       }
    public int getY()           {   return y;       }
    public String getLevelName(){   return levelName;}
    public String getState()    {   return state;   }
    
    // MUTATOR METHODS
    public void setX(int x)     {   this.x = x;     }
    public void setY(int y)     {   this.y = y;     }
    public void setName(String levelName)   {this.levelName = levelName;}
    public void setState(String state) {this.state = state;}
    
    @Override
    public String toString() 
    {
        return x + ", " + y;
    }
}
