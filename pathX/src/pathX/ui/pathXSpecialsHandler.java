package pathX.ui;

import pathX.data.pathXDataModel;
import static pathX.pathXConstants.*;

/**
 *
 * @author Steven Liao
 */
public class pathXSpecialsHandler 
{
    // THE PATHX GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXMiniGame game;
    
    // PROVIDES EASY ACCESS TO THE DATA MODEL
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
            case "FREEZE_TIME": break;
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
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_LIGHTS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.MAKE_LIGHT_GREEN_MODE);
            }
        }
    }
    
    public void makeLightRed()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_LIGHTS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.MAKE_LIGHT_RED_MODE);
            }
        }
    }
    
    public void freezeTime()
    {
        if (!data.won() && !data.lost())
        {
            if (game.isCurrentScreenState(GAME_SCREEN_STATE))
            {
                if (data.getBalance() >= COST_FREEZE_TIME)
                {
                    if (data.isPaused())
                    {
                        data.unpause();
                    } else
                    {
                        data.pause();
                    }
                }
            }
        }
    }
    
    public void decreaseSpeedLimit()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_SPEED_LIMIT)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.DECREASE_SPEED_LIMIT_MODE);
            }
        }
    }
    
    public void increaseSpeedLimit()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_SPEED_LIMIT)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.INCREASE_SPEED_LIMIT_MODE);
            }
        }
    }
    
    public void increasePlayerSpeed()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.INCREASE_PLAYER_SPEED_MODE);
            }
        }
    }
    
    public void flatTire()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.FLAT_TIRE_MODE);
            }
        }
    }
    
    public void emptyGasTank()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.EMPTY_GAS_TANK_MODE);
            }
        }
    }
    
    public void closeRoad()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_ROADS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.CLOSE_ROAD_MODE);
            }
        }
    }
    
    public void closeIntersection()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_ROADS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.CLOSE_INTERSECTION_MODE);
            }
        }
    }
    
    public void openIntersection()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_ROADS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.OPEN_INTERSECTION_MODE);
            }
        }
    }
    
    public void steal()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.STEAL_MODE);
            }
        }
    }
    
    public void mindControl()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.MIND_CONTROL_MODE);
            }
        }
    }
    
    public void intangibility()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.INTANGIBILITY_MODE);
            }
        }
    }
    
    public void mindlessTerror()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.MINDLESS_TERROR_MODE);
            }
        }
    }
    
    public void flying()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_SUPERPOWERS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.FLYING_MODE);
            }
        }
    }
    
    public void invincibility()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_SUPERPOWERS)
            {
                data.switchMode(pathXSpecials.pathXSpecialsMode.INVINCIBILITY_MODE);
            }
        }
    }
}
