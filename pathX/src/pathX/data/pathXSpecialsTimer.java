package pathX.data;

import java.util.ArrayList;
import mini_game.Sprite;
import pathX.pathX;
import pathX.ui.pathXMiniGame;
import static pathX.pathXConstants.*;
import pathX.ui.pathXSpecials;
import pathX.ui.pathXSpecials.pathXSpecialsMode;
import pathX.pathX.pathXPropertyType;

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
    private Sprite s;
    
    public pathXSpecialsTimer(pathXMiniGame initGame, pathXDataModel initData, String initSpecial)
    {
        this(initGame, initData, initSpecial, 0, 0);
    }
    
    public pathXSpecialsTimer(pathXMiniGame initGame, pathXDataModel initData, String initSpecial, int canvasX, int canvasY)
    {
        this(initGame, initData, initSpecial, canvasX, canvasY, null);
    }
    
    public pathXSpecialsTimer(pathXMiniGame initGame, pathXDataModel initData, String initSpecial, int canvasX, int canvasY, Sprite initS)
    {
        game = initGame;
        data = initData;
        special = initSpecial;
        x = canvasX;
        y = canvasY;
        s = initS;
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
            if (z.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
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
            if (b.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
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
                pause(20000);
                p.setVx(oldVelocityX);
                p.setVy(oldVelocityY);
            }
        }

        for (Zombie z : data.getLevel().getZombies())
        {
            if (z.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
            {
                float oldVelocityX = z.getVx();
                float oldVelocityY = z.getVy();
                z.setVx(0);
                z.setVy(0);
                pause(20000);
                z.setVx(oldVelocityX);
                z.setVy(oldVelocityY);
            }
        }

        for (Bandit b : data.getLevel().getBandits())
        {
            if (b.containsPoint(x + INTERSECTION_RADIUS, y + INTERSECTION_RADIUS))
            {
                float oldVelocityX = b.getVx();
                float oldVelocityY = b.getVy();
                b.setVx(0);
                b.setVy(0);
                pause(20000);
                b.setVx(oldVelocityX);
                b.setVy(oldVelocityY);
            }
        }
    }
    
    public void steal()
    {
        pause(10000);
        playFail();
        if (data.isStealMode())
            data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void mindControl()
    {
        if (s != null)
        {
            if (s instanceof Police)
            {
                Police p = (Police) s;
                float speed = p.getSpeed();
                p.setSpeed(0);
                pause(20000);
                p.setSpeed(speed);
            } else if (s instanceof Zombie)
            {
                Zombie z = (Zombie) s;
                z.setTarget(x, y);
                z.startMovingToTarget(10);
            } else if (s instanceof Bandit)
            {
                Bandit b = (Bandit) s;
                b.setTarget(x, y);
                b.startMovingToTarget(10);
            }
        }
    }
    
    public void intangibility()
    {
        pause(10000);
        playFail();
        if (data.isIntangibilityMode())
            data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void mindlessTerror()
    {
        Sprite s = data.collidedSprite;
        if (s instanceof Police)
        {
            Police p = (Police) s;
            float speed = p.getSpeed();
            p.setSpeed(0);
            pause(20000);
            p.toggleColided();
            p.setSpeed(speed);
        } else if (s instanceof Zombie)
        {
            Zombie z = (Zombie) s;
            float speed = z.getSpeed();
            z.setSpeed(0);
            pause(20000);
            z.toggleColided();
            z.setSpeed(speed);
        } else if (s instanceof Bandit)
        {
            Bandit b = (Bandit) s;
            float speed = b.getSpeed();
            b.setSpeed(0);
            pause(20000);
            b.toggleCollided();
            b.setSpeed(speed);
        }
        data.setCollidedSprite(null);
        if (data.isMindlessTerrorMode())
            data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void flying()
    {
        pause(10000);
        data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
    
    public void invincibility()
    {
        pause(10000);
        playFail();
        if (data.isInvincibilityMode())
            data.switchMode(pathXSpecialsMode.NORMAL_MODE);
    }
        
    public void playFail()
    {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_FAIL.toString(), false);
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
