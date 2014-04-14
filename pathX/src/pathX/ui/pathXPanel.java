package pathX.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathX.data.pathXDataModel;
import static pathX.pathXConstants.*;
import pathX.ui.pathXTileState;
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
    
    public pathXPanel(MiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
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
            
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            if (((pathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
            {
                renderMap(g);
                renderBackground(g);
            } else
            {
                // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
                renderBackground(g);

                // IF CURRENT SCREEN IS HELP SCREEN
                /**
                if (((pathXMiniGame)game).isCurrentScreenState(HELP_SCREEN_STATE))
                {
                    renderHelp(g);
                }*/

                if (((pathXMiniGame)game).isCurrentScreenState(SETTINGS_SCREEN_STATE))
                {
                    renderSettings(g);
                }

                // IF CURRENT SCREEN IS LEVEL SELECT SCREEN
                if (((pathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                {
                    renderMap(g);
                    renderNodeLocation(g);
                }
            }
            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);
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
    public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
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
            !s.getSpriteType().getSpriteTypeID().contains(LOCATION_BUTTON_TYPE) &&
            !s.getSpriteType().getSpriteTypeID().contains(MAP_TYPE) &&
            !s.getState().equals(LEVEL_SELECT_SCREEN_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        } else if (s.getState().contains(LEVEL_SELECT_SCREEN_STATE))
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
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
        }
    }
    
    public void renderHelp(Graphics g)
    {
        g.drawRect(40, 90, WINDOW_WIDTH - 80, WINDOW_HEIGHT - 60);
        Sprite helpSprite = game.getGUIDialogs().get(HELP_DESCRIPTION_TYPE);
        renderSprite(g, helpSprite);
    }
    
    public void renderMap(Graphics g)
    {
        Sprite map = game.getGUIDecor().get(MAP_TYPE);
        map.setState(pathXTileState.VISIBLE_STATE.toString());
        SpriteType bgST = map.getSpriteType();
        Image img = bgST.getStateImage(map.getState());
        g.drawImage(img, (int) map.getX(), (int) map.getY(), bgST.getWidth(), bgST.getHeight(), null);
    }
    
    public void renderNodeLocation(Graphics g)
    {
        Sprite location = game.getGUIButtons().get(LOCATION_BUTTON_TYPE);
        location.setEnabled(true);
        location.setState(pathXTileState.UNSUCCESSFUL_STATE.toString());
        SpriteType sTLocation = location.getSpriteType();
        Image img = sTLocation.getStateImage(location.getState());
        //g.setColor(Color.WHITE);
        //g.fillOval(100, 200, 30, 30);
        g.drawImage(img, (int) location.getX(), (int) location.getY(), sTLocation.getWidth(), sTLocation.getHeight(), null);
    }
    
    public void renderSettings(Graphics g)
    {
        g.setFont(FONT_TEXT_DISPLAY);
        g.drawString("Speed: ", 400, 440);
    }
}
