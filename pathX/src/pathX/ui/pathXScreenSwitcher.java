package pathX.ui;

import java.util.Iterator;
import mini_game.MiniGameState;
import mini_game.Sprite;
import pathX.data.pathXDataModel;
import pathX.data.pathXLevel;
import pathX.pathX;
import static pathX.pathXConstants.*;
import pathX.pathX.pathXPropertyType;
/**
 *
 * @author Dell
 */
public class pathXScreenSwitcher 
{
    private pathXMiniGame game;
    
    private pathXDataModel data;
    
    public pathXScreenSwitcher(pathXMiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
    }
    
    /**
     * This method switches the application to the level select screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToLevelSelectScreen()
    {
        if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            game.getGUIDecor().get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(START_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(START_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(HOME_BUTTON_TYPE).setX(HOME_BUTTON_X);
            game.getGUIButtons().get(HOME_BUTTON_TYPE).setY(0);
            game.getGUIButtons().get(EXIT_BUTTON_TYPE).setX(EXIT_BUTTON_X);
            game.getGUIButtons().get(EXIT_BUTTON_TYPE).setY(0);
            enableSpecialButtons(false, data.getSpecialsCounter());
        } 
        
        // CHANGE THE BACKGROUND
        game.getGUIDecor().get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        //guiDecor.get(MAP_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        
        game.setCurrentScreenState(LEVEL_SELECT_SCREEN_STATE);
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(SCROLL_UP_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToSettingsScreen()
    {
        // CHANGE THE BACKGROUND
        game.getGUIDecor().get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        game.setCurrentScreenState(SETTINGS_SCREEN_STATE);
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setEnabled(true);
        
        // SET THE STATE OF THE MUTE BOXES
        if (game.isSoundMuted())
        {
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.SELECTED_STATE.toString());
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
        } else
        {
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
        }
        if (game.isMusicMuted())
        {
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.SELECTED_STATE.toString());
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
        } else
        {
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
        }
        game.getGUIButtons().get(CHANGE_SPEED_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(CHANGE_SPEED_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen()
    {
        enableSpecialButtons(true, data.getSpecialsCounter());
        disableLevelButtons();
        
        // CHANGE THE BACKGROUND
        game.getGUIDecor().get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        game.getGUIDecor().get(MAP_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        
        game.setCurrentScreenState(GAME_SCREEN_STATE);
        
//        game.getGUIButtons().get(LEVEL_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        
        game.toggleInfoDisplay();
        game.getGUIDecor().get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(START_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(START_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setX(40);
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setY(90);
        game.getGUIButtons().get(EXIT_BUTTON_TYPE).setX(90);
        game.getGUIButtons().get(EXIT_BUTTON_TYPE).setY(90);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToMenuScreen()
    {
        // CHANGE THE BACKGROUND
        game.getGUIDecor().get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
        // DISABLE DECORATIONS OR BUTTONS DEPENDING ON SCREEN FOR EFFICIENCY
        if (game.isCurrentScreenState(SETTINGS_SCREEN_STATE))
        {
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(CHANGE_SPEED_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(CHANGE_SPEED_BUTTON_TYPE).setEnabled(false);
        } else if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
        {
            game.getGUIDecor().get(MAP_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            disableScrollButtons();
            disableLevelButtons();
        } else if (game.isCurrentScreenState(GAME_SCREEN_STATE))
        {
            if (data.inProgress())
                game.getAudio().stop(pathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
            else if (data.won())
                game.getAudio().stop(pathXPropertyType.SONG_CUE_WIN.toString());
            game.getAudio().play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
            game.getDataModel().setGameState(MiniGameState.NOT_STARTED);
            game.getDataModel().unpause();
            game.getGUIDecor().get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(START_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(START_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(false);
            game.getGUIButtons().get(HOME_BUTTON_TYPE).setX(HOME_BUTTON_X);
            game.getGUIButtons().get(HOME_BUTTON_TYPE).setY(0);
            game.getGUIButtons().get(EXIT_BUTTON_TYPE).setX(EXIT_BUTTON_X);
            game.getGUIButtons().get(EXIT_BUTTON_TYPE).setY(0);
            enableSpecialButtons(false, data.getSpecialsCounter());
            disableScrollButtons();
        } else if (game.isCurrentScreenState(HELP_SCREEN_STATE))
        {
            game.getGUIDecor().get(HELP_DESCRIPTION_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        }
        
        // DEACTIVATE THE HOME BUTTON
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setEnabled(false);
        
        game.setCurrentScreenState(MENU_SCREEN_STATE);
        
        // ACTIVATE THE MENU CONTROLS
        game.getGUIButtons().get(PLAY_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(PLAY_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(RESET_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(RESET_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(SETTINGS_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(HELP_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(HELP_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToHelpScreen()
    {
        // CHANGE THE BACKGROUND
        game.getGUIDecor().get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        game.getGUIDecor().get(HELP_DESCRIPTION_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        
        game.setCurrentScreenState(HELP_SCREEN_STATE);
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(HOME_BUTTON_TYPE).setEnabled(true);
    }
    
    public void disableMenuButtons()
    {
        // DEACTIVATE ALL MENU CONTROLS
        game.getGUIButtons().get(PLAY_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(PLAY_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(RESET_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(RESET_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(SETTINGS_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(HELP_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(HELP_BUTTON_TYPE).setEnabled(false);
    }
    
    public void disableScrollButtons()
    {
        game.getGUIButtons().get(SCROLL_UP_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        game.getGUIButtons().get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
    }
    
    public void disableLevelButtons()
    {
        Iterator<Sprite> it = game.getGUIButtons().values().iterator();
        while (it.hasNext())
        {
            Sprite level = it.next();
            if (level.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
            {
                level.setEnabled(false);
            }
        }
    }
    
    public void enableSpecialButtons(boolean b, int numSpecials)
    {
        for (int i = 0; i < numSpecials; i++)
        {
            game.getGUIButtons().get(SPECIALS_NAME_LIST[i]).setEnabled(b);
            if (b)
                game.getGUIButtons().get(SPECIALS_NAME_LIST[i]).setState(pathXTileState.VISIBLE_STATE.toString());
            else
                game.getGUIButtons().get(SPECIALS_NAME_LIST[i]).setState(pathXTileState.INVISIBLE_STATE.toString());
        }
    }
}
