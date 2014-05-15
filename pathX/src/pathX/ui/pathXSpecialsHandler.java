package pathX.ui;

import pathX.data.pathXDataModel;
import pathX.data.pathXSpecialsTimer;
import pathX.pathX.pathXPropertyType;
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
            case "FREEZE_TIME": freezeTime(); break;
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
    
    public void playSpecial()
    {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SPECIAL_SUCCESS.toString(), false);
    }
    
    public void playFail()
    {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_FAIL.toString(), false);
    }
    
    public void makeLightGreen()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_LIGHTS)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_LIGHTS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.MAKE_LIGHT_GREEN_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void makeLightRed()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_LIGHTS)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_LIGHTS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.MAKE_LIGHT_RED_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void freezeTime()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_FREEZE_TIME)
            {
                playSpecial();
                data.changeBalance(-COST_FREEZE_TIME);
                pathXSpecialsTimer freezeTime = new pathXSpecialsTimer(game, data, "FREEZE_TIME");
                freezeTime.start();
            } else
            {
                playFail();
            }
        }
        
    }
    
    public void decreaseSpeedLimit()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_SPEED_LIMIT)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_SPEED_LIMIT);
                data.switchMode(pathXSpecials.pathXSpecialsMode.DECREASE_SPEED_LIMIT_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void increaseSpeedLimit()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_SPEED_LIMIT)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_SPEED_LIMIT);
                data.switchMode(pathXSpecials.pathXSpecialsMode.INCREASE_SPEED_LIMIT_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void increasePlayerSpeed()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_MANIPULATION);
                float newPlayerSpeed = (float) (data.getPlayerSpeed() + .2);
                data.setPlayerSpeed(newPlayerSpeed);
            } else
            {
                playFail();
            }
        }
    }
    
    public void flatTire()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_MANIPULATION);
                data.switchMode(pathXSpecials.pathXSpecialsMode.FLAT_TIRE_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void emptyGasTank()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_MANIPULATION)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_MANIPULATION);
                data.switchMode(pathXSpecials.pathXSpecialsMode.EMPTY_GAS_TANK_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void closeRoad()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_ROADS)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_ROADS);
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
                playSpecial();
                data.changeBalance(-COST_CHANGE_ROADS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.CLOSE_INTERSECTION_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void openIntersection()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CHANGE_ROADS)
            {
                playSpecial();
                data.changeBalance(-COST_CHANGE_ROADS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.OPEN_INTERSECTION_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void steal()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_CONTROL);
                data.switchMode(pathXSpecials.pathXSpecialsMode.STEAL_MODE);
                pathXSpecialsTimer steal = new pathXSpecialsTimer(game, data, "STEAL");
                steal.start();
            } else
            {
                playFail();
            }
        }
    }
    
    public void mindControl()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_CONTROL);
                data.switchMode(pathXSpecials.pathXSpecialsMode.MIND_CONTROL_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void intangibility()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_CONTROL);
                data.switchMode(pathXSpecials.pathXSpecialsMode.INTANGIBILITY_MODE);
                pathXSpecialsTimer intangibility = new pathXSpecialsTimer(game, data, "INTANGIBILITY");
                intangibility.start();
            } else
            {
                playFail();
            }
        }
    }
    
    public void mindlessTerror()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_CAR_CONTROL)
            {
                playSpecial();
                data.changeBalance(-COST_CAR_CONTROL);
                data.switchMode(pathXSpecials.pathXSpecialsMode.MINDLESS_TERROR_MODE);
                pathXSpecialsTimer mindlessTerror = new pathXSpecialsTimer(game, data, "MINDLESS_TERROR");
                mindlessTerror.start();
            }
        }
    }
    
    public void flying()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_SUPERPOWERS)
            {
                playSpecial();
                data.changeBalance(-COST_SUPERPOWERS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.FLYING_MODE);
            } else
            {
                playFail();
            }
        }
    }
    
    public void invincibility()
    {
        if (data.inProgress())
        {
            if (data.getBalance() >= COST_SUPERPOWERS)
            {
                playSpecial();
                data.changeBalance(-COST_SUPERPOWERS);
                data.switchMode(pathXSpecials.pathXSpecialsMode.INVINCIBILITY_MODE);
                pathXSpecialsTimer invincibility = new pathXSpecialsTimer(game, data, "INVINCIBILITY");
                invincibility.start();
            } else
            {
                playFail();
            }
        }
    }
}
