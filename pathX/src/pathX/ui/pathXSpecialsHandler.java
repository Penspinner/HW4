package pathX.ui;

import pathX.data.pathXDataModel;

/**
 *
 * @author Steven Liao
 */
public class pathXSpecialsHandler 
{
    // THE PATHX GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXMiniGame game;
    
    private pathXDataModel data;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXSpecialsHandler(pathXMiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
    }
    
    public void useSpecial(String special)
    {
        switch (special)
        {
            case "MAKE_LIGHT_GREEN": makeLightGreen(); break;
            case "MAKE_LIGHT_RED": makeLightRed(); break;
            case "DECREASE_SPEED_LIMIT": decreaseSpeedLimit(); break;
            case "INCREASE_SPEED_LIMIT": increaseSpeedLimit(); break;
            case "INCREASE_PLAYER_SPEED": increasePlayerSpeed(); break;
            case "FLAT_TIRE": flatTire(); break;
            case "EMPTY_GAS_TANK": emptyGasTank(); break;
            case "CLOSE_ROAD": closeRoad(); break;
            case "CLOSE_INTERSECTION": closeIntersection(); break;
            case "OPEN_INTERSECTION": openIntersection(); break;
            case "STEAL": steal(); break;
            case "MIND_CONTROL": mindControl(); break;
            case "INTANGIBILITY": intangibility(); break;
            case "MINDLESS_TERROR": mindlessTerror(); break;
            case "FLYING": flying(); break;
            case "INVINCIBILITY": invincibility(); break;
        }
    }
    
    public void makeLightGreen()
    {
        
    }
    
    public void makeLightRed()
    {
        
    }
    
    public void decreaseSpeedLimit()
    {
        
    }
    
    public void increaseSpeedLimit()
    {
        
    }
    
    public void increasePlayerSpeed()
    {
        
    }
    
    public void flatTire()
    {
        
    }
    
    public void emptyGasTank()
    {
        
    }
    
    public void closeRoad()
    {
        
    }
    
    public void closeIntersection()
    {
        
    }
    
    public void openIntersection()
    {
        
    }
    
    public void steal()
    {
        
    }
    
    public void mindControl()
    {
        
    }
    
    public void intangibility()
    {
        
    }
    
    public void mindlessTerror()
    {
        
    }
    
    public void flying()
    {
        
    }
    
    public void invincibility()
    {
        
    }
}
