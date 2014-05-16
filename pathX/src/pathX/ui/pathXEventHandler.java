package pathX.ui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import pathX.data.pathXSpecialsTimer;
import pathX.pathX.pathXPropertyType;
import static pathX.pathXConstants.*;
import pathX.ui.pathXSpecials.pathXSpecialsMode;
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
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            game.getAudio().play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
            game.getAudio().stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
            data.setGameState(MiniGameState.NOT_STARTED);
            data.unpause();
            game.getScreenSwitcher().switchToLevelSelectScreen();
            return;
        }
        // AND CLOSE THE GAME
        System.exit(0);
    }
    
    /**
     * Called when the user clicks the Play button.
     */
    public void respondToPlayButtonRequest()
    {
        game.getScreenSwitcher().switchToLevelSelectScreen();
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
    }
    
    /**
     * Called when the user clicks on a level.
     */
    public void respondToLevelSelectRequest(pathXTile level)
    {
        String fileName = level.getActionCommand();
        int levelNumber = level.getPathIndex();
        File file = new File(LEVELS_PATH + fileName);
        boolean loadedSuccessfully = game.getXMLLevelIO().loadLevel(file, data);
        if (loadedSuccessfully)
        {
            game.getAudio().stop(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT_LEVEL.toString(), false);
            game.initGameViewport();
            data.setCurrentLevel(fileName);
            data.initPlayerStartingLocation();
            data.resetPlayerSpeed();
            data.generateZombiePath();
            data.generateBanditPath();
            data.setMindlessTerror(false);
            data.getLevel().setName(level.getName());
            data.getLevel().setDescription(level.getDescription());
            data.getLevel().setLevelNumber(levelNumber);
            game.getScreenSwitcher().switchToGameScreen();
        }
    }
    
    /**
     * Called when the user clicks on the reset button.
     */
    public void respondToResetButtonRequest()
    {
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
        
        // SHOW AN ALERT IN CASE IT WAS BY ACCIDENT
        int selection = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the game?");
        if (selection == JOptionPane.OK_OPTION)
        {
            game.reset();
        }
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
            if (soundName.startsWith(cue))
            {
                game.getAudio().mute(soundName);
            }
        }
    }
    
    public void respondToPauseButtonRequest()
    {
        if (!data.isPaused())
        {
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
            game.getSpecialsHandler().useSpecial("FREEZE_TIME");
        }
    }
    
    /**
     * Called when the user clicks on the start button in game screen.
     */
    public void respondToStartButtonRequest()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE) &&
            !data.won() && !data.lost() && !data.inProgress())
        {
            data.beginGame();
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
            game.getAudio().play(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
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
        if (data.won())
            game.getAudio().stop(pathXPropertyType.SONG_CUE_WIN.toString());
        
        // CHANGE GAME STATE
        data.setGameState(MiniGameState.NOT_STARTED);
        data.unpause();
     
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
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
            game.initGameViewport();
            data.initPlayerStartingLocation();
            data.resetPlayerSpeed();
            data.generateZombiePath();
            data.generateBanditPath();
            
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
        game.getAudio().play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
        if (data.won())
            game.getAudio().stop(pathXPropertyType.SONG_CUE_WIN.toString());
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
                // SCROLLING BUTTONS
                
                // SCROLL UP
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:    {   scroll("UP");                                       } break;
                    
                // SCROLL DOWN
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:  {   scroll("DOWN");                                     } break;
                    
                // SCROLL LEFT
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:  {   scroll("LEFT");                                     } break;
                    
                // SCROLL RIGHT
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT: {   scroll("RIGHT");                                    } break;
                    
                // ESCAPE IF YOU DON'T WANT TO USE SPECIAL    
                case KeyEvent.VK_ESCAPE:
                {   
                    data.setMindlessTerror(false);
                    data.switchMode(pathXSpecialsMode.NORMAL_MODE);     
                } break;
                
                // SPECIALS
                    
                // PAUSE
                case KeyEvent.VK_F:     {   specialsHandler.useSpecial("FREEZE_TIME");          } break;
                    
                // MAKE LIGHTS GREEN
                case KeyEvent.VK_G:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[0]);      } break;
                    
                // MAKE LIGHTS RED
                case KeyEvent.VK_R:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[1]);      } break;
                    
                // FLAT TIRE
                case KeyEvent.VK_T:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[2]);      } break;
                    
                // EMPTY GAS TANK
                case KeyEvent.VK_E:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[3]);      } break;
                    
                // INCREASE SPEED LIMIT
                case KeyEvent.VK_X:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[4]);      } break;
                    
                // DECREASE SPEED LIMIT
                case KeyEvent.VK_Z:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[5]);      } break;
                
                // INCREASE PLAYER SPEED
                case KeyEvent.VK_P:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[6]);      } break;
                    
                // CLOSE ROAD
                case KeyEvent.VK_H:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[7]);      } break;
                    
                // CLOSE INTERSECTION
                case KeyEvent.VK_C:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[8]);      } break;
                    
                // OPEN INTERSECTION
                case KeyEvent.VK_O:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[9]);      } break;
                    
                // MIND CONTROL
                case KeyEvent.VK_M:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[10]);     } break;
                    
                // MINDLESS TERROR
                case KeyEvent.VK_L:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[11]);     } break;
                    
                // STEAL
                case KeyEvent.VK_Q:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[12]);     } break;
                    
                // INTANGIBILITY
                case KeyEvent.VK_B:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[13]);     } break;
                    
                // FLYING
                case KeyEvent.VK_Y:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[14]);     } break;
                    
                // INVINCIBILITY
                case KeyEvent.VK_V:     {   specialsHandler.useSpecial(SPECIALS_NAME_LIST[15]);     } break;
                    
                // CHEATS
                    
                // CHEAT INCREASE MONEY
                case KeyEvent.VK_I:     
                {   
                    data.changeBalance(100);
                    game.getAudio().play(pathXPropertyType.AUDIO_CUE_INCREASE_MONEY.toString(), false);
                } break;
                    
                // CHEAT JUMP LEVEL
                case KeyEvent.VK_J:     
                {
                    if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                    {
                        Sprite levelToBeSuccessful = game.getGUIButtons().get(data.getLevelNames().get(data.getCurrentLevelCounter()));
                        levelToBeSuccessful.setState(pathXTileState.SUCCESSFUL_STATE.toString());
                        data.unlockNextLevel();
                    }
                } break;
            }
        }
    }
}