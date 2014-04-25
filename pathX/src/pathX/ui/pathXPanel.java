package pathX.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import pathX.data.pathXLevel;
import static pathX.pathXConstants.*;
/**
 *
 * @author Steven Liao
 */
public class pathXPanel extends JPanel
{
    // THIS IS ACTUALLY OUR pathX APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private pathXDataModel data;
    
    // LEVEL TO BE RENDERED
    Ellipse2D.Double levelCircle;
    
    public pathXPanel(MiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
        levelCircle = new Ellipse2D.Double(0, 0, 30, 30);
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
            
            Graphics2D g2 = (Graphics2D) g;
            
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            if (((pathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
            {
                renderUSAMap(g2);
                renderLevel(g2);
                renderBackground(g2);
            } else if (((pathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE))
            {
                renderLevelMap(g2);
                renderBackground(g2);
            } else
            {
                // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
                renderBackground(g2);

                // IF CURRENT SCREEN IS HELP SCREEN
                /**
                if (((pathXMiniGame)game).isCurrentScreenState(HELP_SCREEN_STATE))
                {
                    renderHelp(g);
                }*/

                if (((pathXMiniGame)game).isCurrentScreenState(SETTINGS_SCREEN_STATE))
                {
                    renderGameSpeed(g2);
                }
            }
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g2);
        } 
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
    // RENDERING HELPER METHODS
        // - renderBackground
        // - renderGUIControls
        // - renderSnake
        // - renderTiles
        // - renderDialogs
        // - renderGrid
        // - renderDebuggingText
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics2D g2)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g2, bg);
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(pathXTileState.INVISIBLE_STATE.toString()) &&
            !s.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE) &&
            !s.getSpriteType().getSpriteTypeID().contains(MAP_TYPE) &&
            !s.getState().equals(LEVEL_SELECT_SCREEN_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        } else if (s.getState().equals(LEVEL_SELECT_SCREEN_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), img.getWidth(null), img.getHeight(null), null); 
        }
    }
    
    /**
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics2D g2)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g2, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g2, s);
        }
    }
    
    public void renderHelp(Graphics2D g2)
    {
        g2.drawRect(40, 90, WINDOW_WIDTH - 80, WINDOW_HEIGHT - 60);
        Sprite helpSprite = game.getGUIDialogs().get(HELP_DESCRIPTION_TYPE);
        //renderSprite(g, helpSprite);
    }
    
    public void renderUSAMap(Graphics2D g2)
    {
        Sprite map = game.getGUIDecor().get(MAP_TYPE);
        map.setState(pathXTileState.VISIBLE_STATE.toString());
        SpriteType bgST = map.getSpriteType();
        Image img = bgST.getStateImage(map.getState());
        //g.drawImage(img, (int) map.getX(), (int) map.getY(), bgST.getWidth(), bgST.getHeight(), null);
        Viewport viewport = ((pathXMiniGame)game).getMapViewport();
        int viewportX = viewport.getViewportX();
        int viewportY = viewport.getViewportY();
        g2.drawImage(img, 0, NORTH_PANEL_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT,
                viewportX, viewportY, WINDOW_WIDTH + viewportX, WINDOW_HEIGHT + viewportY, null);
    }
    
    public void renderLevelMap(Graphics2D g2)
    {
        
    }
    
    public void renderLevel(Graphics2D g2)
    {
        for (Sprite level : game.getGUIButtons().values())
        {
            if (level.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
            {
                level.setEnabled(true);
                level.setState(pathXTileState.UNSUCCESSFUL_STATE.toString());
                SpriteType sTLevel = level.getSpriteType();
                Image img = sTLevel.getStateImage(level.getState());
                Viewport viewport = ((pathXMiniGame)game).getMapViewport();
                if (level.getY() <= 50)
                    level.setEnabled(false);
                else
                    level.setEnabled(true);
                g2.drawImage(img, (int) level.getX(), (int) level.getY(), sTLevel.getWidth(), sTLevel.getHeight(), null);
            }
        }
//        if (viewport.isCircleBoundingBoxInsideViewport(img.getWidth(null), img.getHeight(null), 30))
//        g2.drawImage(img, (int) level.getX() - viewport.getViewportX(), (int) level.getY() - viewport.getViewportY(), (int) (level.getX() + level.getAABBwidth() - viewport.getViewportX()), (int) (level.getY() + level.getAABBheight() - viewport.getViewportY()), 
//                0, 0, sTLevel.getWidth(), sTLevel.getHeight(), null);
        
//        Iterator<pathXLevel> it = data.getLevelsIterator();
//        while (it.hasNext())
//        {
//            pathXLevel p = it.next();
//            if (viewport.isCircleBoundingBoxInsideViewport(30, 30, 15))
//            {
//            if (p.getState().equals("LOCKED_STATE"))
//            {
//                g2.setColor(Color.WHITE);
//            } else if (p.getState().equals("UNSUCCESSFUL_STATE"))
//            {
//                g2.setColor(Color.RED);
//            } else
//            {
//                g2.setColor(Color.GREEN);
//            }
//            levelCircle.x = p.getX() - viewport.getViewportX() - 15;
//            levelCircle.y = p.getY() - viewport.getViewportY() - 15;
//            g2.fill(levelCircle);
//            
//            g2.setColor(Color.BLACK);
//            Stroke s = new BasicStroke(3);
//            g2.setStroke(s);
//            g2.draw(levelCircle);
//            }
//        }
    }
    
    public void renderGameSpeed(Graphics2D g2)
    {
        g2.setFont(FONT_TEXT_DISPLAY);
        g2.drawString("Speed: ", 400, 440);
    }
}
