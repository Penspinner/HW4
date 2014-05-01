package pathX.ui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import static pathX.pathXConstants.*;
/**
 *
 * @author Steven Liao
 */
public class pathXEventHandler 
{
    // THE PATHX GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXEventHandler(pathXMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            game.reset();
            game.getScreenSwitcher().switchToLevelSelectScreen();
            return;
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
    
    /**
     * Called when the user clicks the Play button.
     */
    public void respondToPlayButtonRequest()
    {
        if (game.getDataModel().inProgress())
        {
            game.getScreenSwitcher().switchToGameScreen();
        }
        else
        {
            game.getScreenSwitcher().switchToLevelSelectScreen();
        }
    }
    
    /**
     * Called when the user clicks on a level.
     */
    public void respondToLevelSelectRequest(pathXTile px)
    {
//        ArrayList<String> levels = ((pathXDataModel)game.getDataModel()).getLevelNames();
//        String levelName = "";
//        for (String l : levels)
//        {
//            if (px.getSpriteType().getSpriteTypeID().contains(l))
//            {
//                levelName = px.getSpriteType().getSpriteTypeID().substring(17);
//            }
//        }
        File file = new File(LEVELS_PATH + px.getName());
        boolean loadedSuccessfully = game.getXMLLevelIO().loadLevel(file, ((pathXDataModel)game.getDataModel()));
        if (loadedSuccessfully)
        {
            game.initGameViewport();
            ((pathXDataModel)game.getDataModel()).initPlayerStartingLocation();
            ((pathXDataModel)game.getDataModel()).initZombieLocation();
            game.getScreenSwitcher().switchToGameScreen();
        }
    }
    
    /**
     * Called when the user clicks on the reset button.
     */
    public void respondToResetButtonRequest()
    {
        game.reset();
    }
    
    /**
     * Called when the user clicks on the settings button.
     */
    public void respondToSettingsButtonRequest()
    {
        if (!game.isCurrentScreenState(SETTINGS_SCREEN_STATE))
            game.getScreenSwitcher().switchToSettingsScreen();
    }
    
    /**
     * Called when the user clicks on the help button.
     */
    public void respondToHelpButtonRequest()
    {
        if (!game.isCurrentScreenState(HELP_SCREEN_STATE))
            game.getScreenSwitcher().switchToHelpScreen();
    }
    
    /**
     * Called when the user clicks on the Home button.
     */
    public void respondToHomeButtonRequest()
    {
        game.getScreenSwitcher().switchToMenuScreen();
    }
    
    /**
     * Called when the user clicks on a mute button.
     * 
     * @param muteButton 
     */
    public void respondToMuteButtonRequest(Sprite muteButton)
    {
        if (!muteButton.getState().equals(pathXTileState.SELECTED_STATE.toString()))
        {
            muteButton.setState(pathXTileState.SELECTED_STATE.toString());
        } else
        {
            muteButton.setState(pathXTileState.VISIBLE_STATE.toString());
        }
    }
    
    public void respondToPauseButtonRequest()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE) &&
            game.getDataModel().isPaused())
        {
            game.getDataModel().unpause();
        } else if (game.isCurrentScreenState(GAME_SCREEN_STATE) &&
                game.getDataModel().isPaused())
        {
            game.getDataModel().pause();
        }
    }
    
    /**
     * Called when the user clicks on the start button in game screen.
     */
    public void respondToStartButtonRequest()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            game.getDataModel().setGameState(MiniGameState.IN_PROGRESS);
        }
    }
    
    /**
     * Called when the user clicks on the scroll buttons. This will scroll 
     * the map according to the direction being clicked.
     * 
     * @param direction 
     */
    public void scroll(String direction)
    {
        Viewport viewport = game.getMapViewport();
        Sprite map = game.getGUIDecor().get(MAP_TYPE);        
//        viewport.setNorthPanelHeight(80);
//        viewport.setGameWorldSize((int) map.getAABBwidth(), (int) map.getAABBheight());
//        viewport.setViewportSize(WINDOW_WIDTH, WINDOW_HEIGHT);
//        viewport.updateViewportBoundaries();
//        viewport.initViewportMargins();
        Viewport gameViewport = game.getGameViewport();
        switch (direction)
        {
            case "UP":
            {
                //if (map.getY() + WINDOW_HEIGHT - map.getAABBheight() < WINDOW_HEIGHT - map.getAABBheight() + MAP_Y)
                if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                {
                    if (viewport.getMinViewportY() < viewport.getViewportY())
                    {
                        map.setY(map.getY() + SCROLL_PIXELS);
                        viewport.scroll(0, -SCROLL_PIXELS);
                        for (Sprite node : game.getGUIButtons().values())
                            if (node.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
                                node.setY(node.getY() + SCROLL_PIXELS);
                    }
                }
                else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
                {
                    if (gameViewport.getMinViewportY() < gameViewport.getViewportY())
                    {
                        gameViewport.scroll(0, -SCROLL_PIXELS);
                    }
                }
            } break;

            case "DOWN":
            {
//                if (map.getY() >= WINDOW_HEIGHT - map.getAABBheight() - 23)
                if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                {
                    if (viewport.getMaxViewportY() + NORTH_PANEL_HEIGHT > viewport.getViewportY())
                    {
                        map.setY(map.getY() - SCROLL_PIXELS);
                        viewport.scroll(0, SCROLL_PIXELS);
                        for (Sprite node : game.getGUIButtons().values())
                            if (node.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
                                node.setY(node.getY() - SCROLL_PIXELS);
                    }
                }
                else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
                {
                    if (gameViewport.getMaxViewportY() > gameViewport.getViewportY())
                    {
                        gameViewport.scroll(0, SCROLL_PIXELS);
                    }
                }
            } break;

            case "LEFT":
            {
//                if (map.getX() + WINDOW_WIDTH - map.getAABBwidth() < WINDOW_WIDTH - map.getAABBwidth())
                if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                {
                    if (viewport.getMinViewportX() < viewport.getViewportX())
                    {
                        map.setX(map.getX() + SCROLL_PIXELS);
                        viewport.scroll(-SCROLL_PIXELS, 0);
                        for (Sprite node : game.getGUIButtons().values())
                            if (node.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
                                node.setX(node.getX() + SCROLL_PIXELS);
                    }
                }
                else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
                {
                    if (gameViewport.getMinViewportX() < gameViewport.getViewportX())
                    {
                        gameViewport.scroll(-SCROLL_PIXELS, 0);
                    }
                }
            } break;

            case "RIGHT":
            {
//                if (map.getX() >= WINDOW_WIDTH - map.getAABBwidth())
                if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                {
                    if (viewport.getMaxViewportX() > viewport.getViewportX())
                    {
                        map.setX(map.getX() - SCROLL_PIXELS);
                        viewport.scroll(SCROLL_PIXELS, 0);
                        for (Sprite node : game.getGUIButtons().values())
                            if (node.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
                                node.setX(node.getX() - SCROLL_PIXELS);
                    }
                }
                else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
                {
                    if (gameViewport.getMaxViewportX() + GAME_OFFSET > gameViewport.getViewportX())
                    {
                        gameViewport.scroll(SCROLL_PIXELS, 0);
                    }
                }
            } break;
        }
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    {
        
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
            game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            Sprite map = game.getGUIDecor().get(MAP_TYPE);
            
            switch (keyCode)
            {
                // SCROLL UP
                case KeyEvent.VK_UP:
                {
                    scroll("UP");
                } break;
                    
                // SCROLL DOWN
                case KeyEvent.VK_DOWN:
                {
                    scroll("DOWN");
                } break;
                    
                // SCROLL LEFT
                case KeyEvent.VK_LEFT:
                {
                    scroll("LEFT");
                } break;
                    
                // SCROLL RIGHT
                case KeyEvent.VK_RIGHT:
                {
                    scroll("RIGHT");
                } break;
            }
        }
    }
}
