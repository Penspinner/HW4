package pathX.ui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import pathX.pathX.pathXPropertyType;
import static pathX.pathXConstants.*;
/**
 *
 * @author Steven Liao
 */
public class pathXEventHandler 
{
    // THE PATHX GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXMiniGame game;
    
    // PROVIDES QUICK ACCESS TO THE DATA MODEL
    private pathXDataModel data;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXEventHandler(pathXMiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
//        if (game.getDataModel().inProgress())
//        {
//            game.getDataModel().endGameAsLoss();
//        }
//        else 
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            data.setGameState(MiniGameState.NOT_STARTED);
            data.unpause();
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
//        if (game.getDataModel().inProgress())
//        {
//            game.getScreenSwitcher().switchToGameScreen();
//        }
//        else
        {
            game.getScreenSwitcher().switchToLevelSelectScreen();
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
        }
    }
    
    /**
     * Called when the user clicks on a level.
     */
    public void respondToLevelSelectRequest(pathXTile level)
    {
        String fileName = level.getActionCommand();
        File file = new File(LEVELS_PATH + fileName);
        boolean loadedSuccessfully = game.getXMLLevelIO().loadLevel(file, data);
        if (loadedSuccessfully)
        {
            game.initGameViewport();
            data.setCurrentLevel(fileName);
            data.initPlayerStartingLocation();
            data.generateZombiePath();
            data.getLevel().setName(level.getName());
            data.getLevel().setDescription(level.getDescription());
            game.getScreenSwitcher().switchToGameScreen();
        }
    }
    
    /**
     * Called when the user clicks on the reset button.
     */
    public void respondToResetButtonRequest()
    {
        game.reset();
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
    }
    
    /**
     * Called when the user clicks on the settings button.
     */
    public void respondToSettingsButtonRequest()
    {
        if (!game.isCurrentScreenState(SETTINGS_SCREEN_STATE))
            game.getScreenSwitcher().switchToSettingsScreen();
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
    }
    
    /**
     * Called when the user clicks on the help button.
     */
    public void respondToHelpButtonRequest()
    {
        if (!game.isCurrentScreenState(HELP_SCREEN_STATE))
            game.getScreenSwitcher().switchToHelpScreen();
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
    }
    
    /**
     * Called when the user clicks on the Home button.
     */
    public void respondToHomeButtonRequest()
    {
        game.getScreenSwitcher().switchToMenuScreen();
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
    }
    
    /**
     * Called when the user clicks on a mute button.
     * 
     * @param muteButton 
     */
    public void respondToMuteButtonRequest(Sprite muteButton, String cue)
    {
        if (!muteButton.getState().equals(pathXTileState.SELECTED_STATE.toString()))
        {
            muteButton.setState(pathXTileState.SELECTED_STATE.toString());
        } else
        {
            muteButton.setState(pathXTileState.VISIBLE_STATE.toString());
        }
        
        // MUTE THE SOUNDS
        HashMap<String, Clip> soundMap = game.getAudio().getWavAudio();
        Iterator<String> it = soundMap.keySet().iterator();
        while (it.hasNext())
        {
            String soundName = it.next();
            if (soundName.contains(cue))
            {
                game.getAudio().mute(soundName);
            }
        }
    }
    
    public void respondToPauseButtonRequest()
    {
        // ONLY TOGGLE IF DIDN'T WIN OR LOSE
        if (!data.won() && !data.lost())
        {
            if (game.isCurrentScreenState(GAME_SCREEN_STATE))
            {
                if (data.isPaused())
                {
                    data.unpause();
                } else
                {
                    data.pause();
                }
                game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
            }
        }
    }
    
    /**
     * Called when the user clicks on the start button in game screen.
     */
    public void respondToStartButtonRequest()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            data.beginGame();
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
        }
    }
    
    /**
     * Called when the user clicks on the Game Speed button in the settings screen.
     */
    public void respondToChangeGameSpeedRequest()
    {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
        
        // OPEN UP JOPTIONPANE TO OBTAIN USER INPUT
        String inputGameSpeed = JOptionPane.showInputDialog(null, "Enter a game speed between 0 and 3");
        try
        {
            if (inputGameSpeed != null && !inputGameSpeed.equals(""))
            {
                float gameSpeed = Float.parseFloat(inputGameSpeed);
                if (gameSpeed > 0 && gameSpeed <= 3)
                {
                    data.updateGameSpeed(gameSpeed);
                }
            }
        } catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }
    }
    
    /**
     * Called when the user clicks on the try again button.
     */
    public void respondToTryAgainButtonRequest()
    {
        // CHANGE GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        data.unpause();
        
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setEnabled(false);
        
        // RESTART THE SAME LEVEL
        String fileName = data.getCurrentLevel();
        File currentFile = new File(LEVELS_PATH + fileName);
        boolean loadedSuccessfully = game.getXMLLevelIO().loadLevel(currentFile, data);
        if (loadedSuccessfully)
        {
            data.initPlayerStartingLocation();
            data.generateZombiePath();
            
            // STOP DISPLAYING THE WIN/LOSE DIALOG
            game.toggleInfoDisplay();
            
            game.getScreenSwitcher().switchToGameScreen();    
        }
    }
    
    /**
     * Called when the user clicks on the leave button.
     */
    public void respondToLeaveButtonRequest()
    {
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setEnabled(false);
        game.toggleInfoDisplay();
        data.unpause();
        
        respondToExitRequest();
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
        pathXSpecialsHandler specialsHandler = game.getSpecialsHandler();
        
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ||
            game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            switch (keyCode)
            {
                // SCROLL UP
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:    {   scroll("UP");   } break;
                    
                // SCROLL DOWN
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:  {   scroll("DOWN"); } break;
                    
                // SCROLL LEFT
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:  {   scroll("LEFT"); } break;
                    
                // SCROLL RIGHT
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT: {   scroll("RIGHT");} break;
                    
                // PAUSE
                case KeyEvent.VK_F:
                {
                    if (!data.won() && !data.lost())
                    {
                        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
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
                } break;
                    
                case KeyEvent.VK_B:     {   game.getSpecialsHandler().useSpecial(ID_ATT);} break;
                case KeyEvent.VK_I:     {   data.increaseBalance(100);} break;
            }
        }
    }
}