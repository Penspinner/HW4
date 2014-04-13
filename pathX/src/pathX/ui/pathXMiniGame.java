package pathX.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.SpriteType;
import mini_game.Sprite;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import pathX.data.pathXRecord;
import pathX.file.pathXFileManager;
import pathX.pathX;
import pathX.pathX.pathXPropertyType;
import static pathX.pathXConstants.*;
import properties_manager.PropertiesManager;
/**
 * 
 *
 * @author Steven Liao
 */
public class pathXMiniGame extends MiniGame
{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private pathXRecord record;

    // HANDLES GAME UI EVENTS
    private pathXEventHandler eventHandler;
    
    // HANDLES ERROR CONDITIONS
    private pathXErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private pathXFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    // THE SOUND IS MUTED OR NOT
    private boolean soundMuted;
    
    // THE MUSIC IS MUTED OR NOT
    private boolean musicMuted;
    
    // ACCESSOR METHODS
        // - getPlayerRecord
        // - getErrorHandler
        // - getFileManager
        // - isCurrentScreenState
    
    /**
     * Accessor method for getting the player record object, which
     * summarizes the player's record on all levels.
     * 
     * @return The player's complete record.
     */
    public pathXRecord getPlayerRecord() 
    { 
        return record; 
    }

    /**
     * Accessor method for getting the application's error handler.
     * 
     * @return The error handler.
     */
    public pathXErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    /**
     * Accessor method for getting the app's file manager.
     * 
     * @return The file manager.
     */
    public pathXFileManager getFileManager()
    {
        return fileManager;
    }

    /**
     * Used for testing to see if the current screen state matches
     * the testScreenState argument. If it mates, true is returned,
     * else false.
     * 
     * @param testScreenState Screen state to test against the 
     * current state.
     * 
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }
    
    /**
     * Used to set the state of the sound mute box.
     * 
     * @return true if the sound is muted, false otherwise
     */
    public boolean isSoundMuted()
    {
        return soundMuted;
    }
    
    /**
     * Used to set the state of the music mute box.
     * 
     * @return true if the music is muted, false otherwise
     */
    public boolean isMusicMuted()
    {
        return musicMuted;
    }
    
    /**
     * This method switches the application to the level select screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToLevelSelectScreen()
    {
        if (isCurrentScreenState(GAME_SCREEN_STATE))
        {
            guiDecor.get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
        } 
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        //guiDecor.get(MAP_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        
        currentScreenState = LEVEL_SELECT_SCREEN_STATE;
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(true);
        //guiButtons.get(LOCATION_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToSettingsScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        currentScreenState = SETTINGS_SCREEN_STATE;
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        
        currentScreenState = GAME_SCREEN_STATE;
        
        guiButtons.get(LOCATION_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        disableScrollButtons();
        
        guiDecor.get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToMenuScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
        // DISABLE DECORATIONS OR BUTTONS DEPENDING ON SCREEN FOR EFFICIENCY
        if (isCurrentScreenState(SETTINGS_SCREEN_STATE))
        {
            guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
        } else if (isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
        {
            guiDecor.get(MAP_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(LOCATION_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(LOCATION_BUTTON_TYPE).setEnabled(false);
            disableScrollButtons();
        } else if (isCurrentScreenState(GAME_SCREEN_STATE))
        {
            guiDecor.get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
        } else if (isCurrentScreenState(HELP_SCREEN_STATE))
        {
            guiDecor.get(HELP_DESCRIPTION_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        }
        
        // DEACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
        
        currentScreenState = MENU_SCREEN_STATE;
        
        // ACTIVATE THE MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToHelpScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        guiDecor.get(HELP_DESCRIPTION_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        
        currentScreenState = HELP_SCREEN_STATE;
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
    }
    
    public void disableMenuButtons()
    {
        // DEACTIVATE ALL MENU CONTROLS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
    }
    
    public void disableScrollButtons()
    {
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setEnabled(false);
    }
    
    @Override
    /**
     * Initializes the sound and music to be used by the application.
     */
    public void initAudioContent()
    {
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(pathXPropertyType.AUDIO_CUE_SELECT);

            // PLAY THE WELCOME SCREEN SONG
            
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        {
//            errorHandler.processError(SortingHatPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }        
    }

    /**
     * This helper method loads the audio file associated with audioCueType,
     * which should have been specified via an XML properties file.
     */
    private void loadAudioCue(pathXPropertyType audioCueType) 
            throws  UnsupportedAudioFileException, IOException, LineUnavailableException, 
                    InvalidMidiDataException, MidiUnavailableException
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(pathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);        
    }
    
    @Override
    public void initData()
    {
        // INIT OUR ERROR HANDLER
        errorHandler = new pathXErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        

        // LOAD THE PLAYER'S RECORD FROM A FILE
        
        
        // INIT OUR DATA MANAGER
        data = new pathXDataModel(this);
    }
    
    @Override
    public void initGUIControls()
    {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_WINDOW_ICON));
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new pathXPanel(this, (pathXDataModel) data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        sT = new SpriteType(BACKGROUND_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        // ADD THE MAP
        sT = new SpriteType(MAP_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_USA));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, MAP_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(MAP_TYPE, s);
        
        // ADD THE HELP DESCRIPTION
        sT = new SpriteType(HELP_DESCRIPTION_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_HELP_DESCRIPTION));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, HELP_DESCRIPTION_X, HELP_DESCRIPTION_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(HELP_DESCRIPTION_TYPE, s);
        
        // ADD THE INFO DIALOG BOX
        sT = new SpriteType(INFO_DIALOG_BOX_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_INFO_DIALOG_BOX));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, INFO_DIALOG_BOX_X, INFO_DIALOG_BOX_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(INFO_DIALOG_BOX_TYPE, s);
        
        // ADD EACH MENU BUTTON
        ArrayList<String> menuButtons = props.getPropertyOptionsList(pathXPropertyType.MENU_BUTTON_OPTIONS);
        ArrayList<String> menuMouseOverButtons = props.getPropertyOptionsList(pathXPropertyType.MENU_MOUSE_OVER_OPTIONS);
        float totalWidth = menuButtons.size() * (MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN) - MENU_BUTTON_MARGIN;
        Viewport viewport = data.getViewport();
        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
        for (int i = 0; i < menuButtons.size(); i++)
        {
            sT = new SpriteType(MENU_BUTTON_TYPE[i]);
            img = loadImageWithColorKey(imgPath + menuButtons.get(i), Color.BLACK);
            sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
            img = loadImageWithColorKey(imgPath + menuMouseOverButtons.get(i), Color.BLACK);
            sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
            s = new Sprite(sT, x, MENU_BUTTON_Y, 0, 0, pathXTileState.VISIBLE_STATE.toString());
            guiButtons.put(MENU_BUTTON_TYPE[i], s);
            x += MENU_BUTTON_WIDTH + MENU_BUTTON_MARGIN;
        }
        
        // ADD THE EXIT AND HOME BUTTON TO THE TOP OF THE GAME SCREEN
        
        // ADD THE EXIT BUTTON TO THE TOP RIGHT OF THE MENU SCREEN
        sT = new SpriteType(MENU_BUTTON_TYPE[4]);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_EXIT));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_EXIT_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EXIT_BUTTON_X, 0, 0, 0, pathXTileState.VISIBLE_STATE.toString());
        guiButtons.put(EXIT_BUTTON_TYPE, s);
        
        // ADD THE HOME BUTTON NEXT TO THE EXIT BUTTON
        sT = new SpriteType(MENU_BUTTON_TYPE[5]);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_X, 0, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(HOME_BUTTON_TYPE, s);
        
        // ADD THE START BUTTON
        sT = new SpriteType(START_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_START));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_START_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, START_BUTTON_X, START_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(START_BUTTON_TYPE, s);
        
        // ADD THE SCROLLING BUTTONS
        
        // ADD THE SCROLL UP BUTTON
        sT = new SpriteType(SCROLL_UP_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_UP_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SCROLL_UP_BUTTON_X, SCROLL_UP_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_UP_BUTTON_TYPE, s);
        
        // ADD THE SCROLL DOWN BUTTON
        sT = new SpriteType(SCROLL_DOWN_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_DOWN_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SCROLL_DOWN_BUTTON_X, SCROLL_DOWN_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_DOWN_BUTTON_TYPE, s);
        
        // ADD THE SCROLL LEFT BUTTON
        sT = new SpriteType(SCROLL_LEFT_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_LEFT_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SCROLL_LEFT_BUTTON_X, SCROLL_LEFT_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_LEFT_BUTTON_TYPE, s);
        
        // ADD THE SCROLL RIGHT BUTTON
        sT = new SpriteType(SCROLL_RIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_SCROLL_RIGHT_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SCROLL_RIGHT_BUTTON_X, SCROLL_RIGHT_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SCROLL_RIGHT_BUTTON_TYPE, s);
        
        // ADD THE LOCATION WITH STATUS
        sT = new SpriteType(LOCATION_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_LOCKED_LOCATION));
        sT.addState(pathXTileState.LOCKED_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_SUCCESSFUL_LOCATION));
        sT.addState(pathXTileState.SUCCESSFUL_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_UNSUCCESSFUL_LOCATION));
        sT.addState(pathXTileState.UNSUCCESSFUL_STATE.toString(), img);
        s = new Sprite(sT, 150, 120, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(LOCATION_BUTTON_TYPE, s);
        
        // ADD THE CLOSE BUTTON
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_X, CLOSE_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_BUTTON_TYPE, s);
        
        // ADD SOUND AND MUSIC MUTE BOX -- THEY BOTH USE THE SAME IMAGE
        
        // ADD THE SOUND MUTE BOX
        sT = new SpriteType(SOUND_MUTE_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_SELECTED));
        sT.addState(pathXTileState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, SOUND_MUTE_BOX_BUTTON_X, SOUND_MUTE_BOX_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_MUTE_BOX_BUTTON_TYPE, s);
        
        // ADD THE MUSIC MUTE BOX
        sT = new SpriteType(MUSIC_MUTE_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_SELECTED));
        sT.addState(pathXTileState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_MUTE_BOX_BUTTON_X, MUSIC_MUTE_BOX_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_MUTE_BOX_BUTTON_TYPE, s);
    }
    
    /**
     * Initializes the game event handlers for things like
     * game GUI buttons.
     */
    @Override
    public void initGUIHandlers()
    {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new pathXEventHandler(this);
        
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we) 
            { 
                System.exit(0);
            }
        });
        
        // PLAY GAME EVENT HANDLER
        guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.respondToPlayGameRequest();
            }
        });
        
        // RESET GAME EVENT HANDLER
        guiButtons.get(RESET_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.respondToResetGameRequest();
            }
        });
        
        // SETTINGS EVENT HANDLER
        guiButtons.get(SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.respondToSettingsRequest();
            }
        });
        
        // HELP EVENT HANDLER
        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToHelpRequest();
            }
        });
        
        guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToHomeRequest();
            }
        });
        
        guiButtons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToExitRequest();
            }
        });
        
        guiButtons.get(SCROLL_UP_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.scroll("UP");
            }
        });
        
        guiButtons.get(SCROLL_DOWN_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.scroll("DOWN");
            }
        });
        
        guiButtons.get(SCROLL_LEFT_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.scroll("LEFT");
            }
        });     
        
        guiButtons.get(SCROLL_RIGHT_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.scroll("RIGHT");
            }
        });
        
        guiButtons.get(LOCATION_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToLevelSelectRequest();
            }
        });
        
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                guiDecor.get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
                guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
            }
        });
        
        this.setKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent ke)
            {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }
    
    @Override
    public void reset()
    {
        data.reset(this);
    }
    
    @Override
    public void updateGUI()
    {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(pathXTileState.VISIBLE_STATE.toString()))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(pathXTileState.MOUSE_OVER_STATE.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(pathXTileState.MOUSE_OVER_STATE.toString()))
            {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(pathXTileState.VISIBLE_STATE.toString());
                }
            }
        }
    }
}