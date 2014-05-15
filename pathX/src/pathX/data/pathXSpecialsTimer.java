package pathX.data;

import java.util.ArrayList;
import pathX.ui.pathXMiniGame;
import static pathX.pathXConstants.*;
import pathX.ui.pathXSpecials;
import pathX.ui.pathXSpecials.pathXSpecialsMode;

/**
 *
 * @author Dell
 */
public class pathXSpecialsTimer extends Thread
{
    private pathXMiniGame game;
    private pathXDataModel data;
    private String special;
    private int x;
    private int y;
    
    public pathXSpecialsTimer(pathXMiniGame initGame, pathXDataModel initData, String initSpecial)
    {
        this(initGame, initData, initSpecial, 0, 0);
    }
    
    public pathXSpecialsTimer(pathXMiniGame initGame, pathXDataModel initData, String initSpecial, int canvasX, int canvasY)
    {
        game = initGame;
        data = initData;
        special = initSpecial;
        x = canvasX;
        y = canvasY;
    }
    
    @Override
    public void run()
    {
        // TIMED SPECIALS ONLY
        switch (special)
        {
            case "MAKE_LIGHT_GREEN": makeLightGreen(); break;
            case "MAKE_LIGHT_RED": makeLightRed(); break;
            case "FREEZE_TIME": freezeTime(); break;
            case "FLAT_TIRE": flatTire(); break;
            case "EMPTY_GAS_TANK": emptyGasTank(); break;
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
        Intersection i = data.findIntersectionAtCanvasLocation(x, y);
        if (i != null)
        {
            if (!i.open)
            {
                i.setOpen(true);
                pause(10000);
                i.setOpen(false);
            }
        }
    }
    
    public void makeLightRed()
    {
        Intersection i = data.findIntersectionAtCanvasLocation(x, y);
        if (i != null)
        {
            if (i.open)
            {
                i.setOpen(false);
                pause(10000);
                i.setOpen(true);
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
                    if (!data.isPaused())
                    {
                        data.pause();
                        pause(10000);
                        data.unpause();
                    } 
                }
            }
        }
    }
    
    public void flatTire()
    {
        for (Police p : data.getLevel().getPolices())
        {
            if (p.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
            {
                float oldVelocityX = p.getVx();
                float oldVelocityY = p.getVy();
                p.setVx(0);
                p.setVy(0);
                pause(10000);
                p.setVx(oldVelocityX);
                p.setVy(oldVelocityY);
            }
        }

        for (Zombie z : data.getLevel().getZombies())
        {
            if (z.containsPoint(x, y))
            {
                float oldVelocityX = z.getVx();
                float oldVelocityY = z.getVy();
                z.setVx(0);
                z.setVy(0);
                pause(10000);
                z.setVx(oldVelocityX);
                z.setVy(oldVelocityY);
            }
        }

        for (Bandit b : data.getLevel().getBandits())
        {
            if (b.containsPoint(x, y))
            {
                float oldVelocityX = b.getVx();
                float oldVelocityY = b.getVy();
                b.setVx(0);
                b.setVy(0);
                pause(10000);
                b.setVx(oldVelocityX);
                b.setVy(oldVelocityY);
            }
        }
    }
    
    public void emptyGasTank()
    {
        for (Police p : data.getLevel().getPolices())
        {
            if (p.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
            {
                float oldVelocityX = p.getVx();
                float oldVelocityY = p.getVy();
                p.setVx(0);
                p.setVy(0);
                pause(10000);
                p.setVx(oldVelocityX);
                p.setVy(oldVelocityY);
            }
        }

        for (Zombie z : data.getLevel().getZombies())
        {
            if (z.containsPoint(x, y))
            {
                float oldVelocityX = z.getVx();
                float oldVelocityY = z.getVy();
                z.setVx(0);
                z.setVy(0);
                pause(10000);
                z.setVx(oldVelocityX);
                z.setVy(oldVelocityY);
            }
        }

        for (Bandit b : data.getLevel().getBandits())
        {
            if (b.containsPoint(x, y))
            {
                float oldVelocityX = b.getVx();
                float oldVelocityY = b.getVy();
                b.setVx(0);
                b.setVy(0);
                pause(10000);
                b.setVx(oldVelocityX);
                b.setVy(oldVelocityY);
            }
        }
    }
    
    public void steal()
    {
        pause(10000);
        data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void mindControl()
    {
        if (data.selectedSprite == null)
        {
            data.selectedSprite = data.findSpriteAtCanvasLocation(x, y);
        } else
        {
            Intersection i = data.findIntersectionAtCanvasLocation(x, y);
            if (i != null)
            {
                
            }
        }
    }
    
    public void intangibility()
    {
        pause(10000);
        data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void mindlessTerror()
    {
        
    }
    
    public void flying()
    {
        pause(10000);
        data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void invincibility()
    {
        pause(10000);
        data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void pause(long milliseconds)
    {
        try
        {
            sleep(milliseconds);
        } catch (InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }
}
