/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathX.level_editor.model;

/**
 *
 * @author Dell
 */
public class Bandit 
{
    // POLICE LOCATION
    public int x;
    public int y;
    
    public Bandit(int initX, int initY)
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
