package pathX.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mini_game.MiniGame;
import mini_game.SpriteType;
import mini_game.Sprite;
import mini_game.Viewport;
import pathX.data.pathXDataModel;
import pathX.file.pathXXMLLevelIO;
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
    // HANDLES GAME UI EVENTS
    private pathXEventHandler eventHandler;
    
    // MANAGES SPECIALS
    private pathXSpecials specials;
    
    // HANDLES GAME SPECIALS EVENTS
    private pathXSpecialsHandler specialsHandler;
    
    // HANDLES THE SCREEN SWITCHES
    private pathXScreenSwitcher screenSwitcher;
    
    // HANDLES THE LEVEL LOADLING
    private pathXXMLLevelIO xmlLevelIO;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    // THE SOUND IS MUTED OR NOT
    private boolean soundMuted;
    
    // THE MUSIC IS MUTED OR NOT
    private boolean musicMuted;
    
    // DISPLAYING INFO DIALOG?
    private boolean displayingInfo;
    
    // THE VIEWPORT FOR THE MAP
    private Viewport mapViewport;
    
    // THE VIEWPORT FOR THE GAME
    private Viewport gameViewport;
    
    // ACCESSOR METHODS
    public pathXSpecials getSpecials()                  {   return specials;            }
    public pathXSpecialsHandler getSpecialsHandler()    {   return specialsHandler;     }
    public pathXScreenSwitcher getScreenSwitcher()      {   return screenSwitcher;      }
    public pathXXMLLevelIO getXMLLevelIO()              {   return xmlLevelIO;          }

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
    public boolean isSoundMuted()           {   return soundMuted;      }
    public boolean isMusicMuted()           {   return musicMuted;      }
    public boolean isDisplayingInfo()       {   return displayingInfo;  }
    public Viewport getMapViewport()        {   return mapViewport;     }
    public Viewport getGameViewport()       {   return gameViewport;    }
    
    // MUTATOR METHOD
    public void setCurrentScreenState(String initCurrentScreenState)
    {
        currentScreenState = initCurrentScreenState;
    }
    // TOGGLES INFO DISPLAY
    public void toggleInfoDisplay()         {displayingInfo = !displayingInfo;}
    
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
            loadAudioCue(pathXPropertyType.AUDIO_CUE_LOSE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_INCREASE_MONEY);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_INVINCIBILITY);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_SELECT_LEVEL);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_SPECIAL_SUCCESS);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_FAIL);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_SELECT);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_ZOMBIE);
            loadAudioCue(pathXPropertyType.AUDIO_CUE_BANDIT);
            loadAudioCue(pathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(pathXPropertyType.SONG_CUE_GAME_SCREEN);
            loadAudioCue(pathXPropertyType.SONG_CUE_WIN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(pathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        {
            e.printStackTrace();
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
    
    /**
     * Initialize the map viewport for scrolling
     */
    public void initMapViewport()
    {
        Sprite map = getGUIDecor().get(MAP_TYPE);
        Viewport viewport = data.getViewport();
        viewport.setNorthPanelHeight(NORTH_PANEL_HEIGHT);
        viewport.setGameWorldSize((int) map.getAABBwidth(), (int) map.getAABBheight());
//        viewport.setViewportSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        viewport.updateViewportBoundaries();
        viewport.initViewportMargins();
        mapViewport = viewport;
    }
    
    public void initGameViewport()
    {
        int levelWidth = ((pathXDataModel)data).getBackgroundImage().getWidth(null);
        int levelHeight = ((pathXDataModel)data).getBackgroundImage().getHeight(null);
        gameViewport = new Viewport();
        gameViewport.setScreenSize(data.getViewport().getScreenWidth(), data.getViewport().getScreenHeight());
        gameViewport.setGameWorldSize(levelWidth, levelHeight);
        gameViewport.updateViewportBoundaries();
    //    gameViewport.setViewportSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameViewport.initViewportMargins();
    }
    
    /**
     * Initializes the game data used by the application. Note
     * that it is this method's obligation to construct and set
     * this Game's custom GameDataModel object as well as any
     * other needed game objects.
     */
    @Override
    public void initData()
    {
        // INIT OUR DATA MANAGER
        data = new pathXDataModel(this);
        
        // INIT OUR SCREEN SWITCHER
        screenSwitcher = new pathXScreenSwitcher(this, (pathXDataModel)data);
        
        // SPECIALS
        specials = new pathXSpecials(this, (pathXDataModel)data);
        
        // INITIALIZE THE XML LEVEL IO PARSER
        xmlLevelIO = new pathXXMLLevelIO(new File(PATH_DATA + LEVEL_SCHEMA), this);
    }
    
    /**
     * Initializes the game controls, like buttons, used by
     * the game application. Note that this includes the tiles,
     * which serve as buttons of sorts.
     */
    @Override
    public void initGUIControls()
    {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        pathXTile px;
        
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
        
        // LOAD THE GUN CURSOR
        String cursorName = props.getProperty(pathXPropertyType.IMAGE_GUN_CURSOR);
        img = loadImage(imgPath + cursorName);
        Point cursorHotSpot = new Point(15,15);
        Cursor gunCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, cursorHotSpot, cursorName);
        window.setCursor(gunCursor);
        
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
            img = loadImage(imgPath + menuButtons.get(i));
            sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
            img = loadImage(imgPath + menuMouseOverButtons.get(i));
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
        
        // ADD THE PAUSE BUTTON
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_PAUSE));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_PAUSE_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PAUSE_BUTTON_X, PAUSE_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(PAUSE_BUTTON_TYPE, s);
        
        // ADD THE LEVELS WITH STATUS
        ArrayList<String> levels = ((pathXDataModel)data).getLevelFiles();
        ArrayList<String> levelNames = ((pathXDataModel)data).getLevelNames();
        ArrayList<String> levelDescriptions = ((pathXDataModel)data).getLevelDescriptions();
        ArrayList<String> levelStates = ((pathXDataModel)data).getLevelStates();
        for (int i = 0; i < levels.size(); i++)
        {
            String level = levels.get(i);
            String levelName = levelNames.get(i);
            String levelDescription = levelDescriptions.get(i);
            String levelState = levelStates.get(i);
            sT = new SpriteType(LEVEL_BUTTON_TYPE + i);
            img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_LOCKED_LOCATION));
            sT.addState(pathXTileState.LOCKED_STATE.toString(), img);
            img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_SUCCESSFUL_LOCATION));
            sT.addState(pathXTileState.SUCCESSFUL_STATE.toString(), img);
            img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_UNSUCCESSFUL_LOCATION));
            sT.addState(pathXTileState.UNSUCCESSFUL_STATE.toString(), img);
            px = new pathXTile(sT, LEVEL_X_COORDINATES[i],LEVEL_Y_COORDINATES[i], 0, 0, levelState, levelName);
            px.setActionCommand(level);
            px.setDescription(levelDescription);
            // ADD THE LISTENER NOW SO THAT WE DON'T HAVE TO ADD IT LATER
            px.setActionListener(new ActionListener()
            {
                pathXTile px;
                public ActionListener init(pathXTile initPX)
                {
                    px = initPX;
                    return this;
                }
                public void actionPerformed(ActionEvent e)
                {
                    if (!px.getState().equals(pathXTileState.LOCKED_STATE.toString()))
                    {
                        eventHandler.respondToLevelSelectRequest(px);
                    } else
                    {
                        audio.play(pathXPropertyType.AUDIO_CUE_FAIL.toString(), false);
                    }
                }
            }.init(px));
            guiButtons.put(levelName, px);
        }
        
        // ADD THE CLOSE BUTTON
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_BUTTON_X, CLOSE_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_BUTTON_TYPE, s);
        
        // ADD THE TRY AGAIN BUTTON
        sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRY_AGAIN));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_TRY_AGAIN_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, TRY_AGAIN_BUTTON_X, TRY_AGAIN_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        s.setEnabled(false);
        guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
        
        // ADD THE LEAVE BUTTON
        sT = new SpriteType(LEAVE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVE));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_LEAVE_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEAVE_BUTTON_X, LEAVE_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        s.setEnabled(false);
        guiButtons.put(LEAVE_BUTTON_TYPE, s);
        
        // ADD SOUND AND MUSIC MUTE BOX -- THEY BOTH USE THE SAME IMAGE
        
        // ADD THE SOUND MUTE BOX
        sT = new SpriteType(SOUND_MUTE_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_SELECTED));
        sT.addState(pathXTileState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, SOUND_MUTE_BOX_BUTTON_X, SOUND_MUTE_BOX_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_MUTE_BOX_BUTTON_TYPE, s);
        guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
        
        // ADD THE MUSIC MUTE BOX
        sT = new SpriteType(MUSIC_MUTE_BOX_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_MUTE_BOX_SELECTED));
        sT.addState(pathXTileState.SELECTED_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_MUTE_BOX_BUTTON_X, MUSIC_MUTE_BOX_BUTTON_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_MUTE_BOX_BUTTON_TYPE, s);
        guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setEnabled(false);
        
        // ADD THE SPEED CHANGING BUTTON
        sT = new SpriteType(CHANGE_SPEED_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHANGE_SPEED));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BUTTON_CHANGE_SPEED_MOUSE_OVER));
        sT.addState(pathXTileState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CHANGE_SPEED_X, CHANGE_SPEED_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        s.setEnabled(false);
        guiButtons.put(CHANGE_SPEED_BUTTON_TYPE, s);
        
        // ADD THE GAME SPEED SLIDER
        sT = new SpriteType(GAME_SPEED_SLIDER_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_GAME_SPEED_SLIDER));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, GAME_SPEED_SLIDER_X, GAME_SPEED_SLIDER_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(GAME_SPEED_SLIDER_TYPE, s);
        
        specials.initSpecials();
    }
    
    /**
     * Initializes the game event handlers for things like
     * game GUI buttons.
     */
    @Override
    public void initGUIHandlers()
    {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new pathXEventHandler(this, (pathXDataModel)data);
        specialsHandler = new pathXSpecialsHandler(this, (pathXDataModel)data);
        
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
                eventHandler.respondToPlayButtonRequest();
            }
        });
        
        // RESET GAME EVENT HANDLER
        guiButtons.get(RESET_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.respondToResetButtonRequest();
            }
        });
        
        // SETTINGS EVENT HANDLER
        guiButtons.get(SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                eventHandler.respondToSettingsButtonRequest();
            }
        });
        
        // HELP EVENT HANDLER
        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToHelpButtonRequest();
            }
        });
        
        guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToHomeButtonRequest();
                displayingInfo = false;
            }
        });
        
        guiButtons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToExitRequest();
                displayingInfo = false;
            }
        });
        
        guiButtons.get(START_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToStartButtonRequest();
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
        
        guiButtons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToPauseButtonRequest();
            }
        });
        
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                audio.play(pathXPropertyType.AUDIO_CUE_SELECT.toString(), false);
                guiDecor.get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
                guiButtons.get(CLOSE_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
                guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
                toggleInfoDisplay();
            }
        });
        
        guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToMuteButtonRequest(guiButtons.get(SOUND_MUTE_BOX_BUTTON_TYPE), "AUDIO_CUE");
                soundMuted = !soundMuted;
            }
        });
        
        guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToMuteButtonRequest(guiButtons.get(MUSIC_MUTE_BOX_BUTTON_TYPE), "SONG_CUE");
                musicMuted = !musicMuted;
            }
        });
        
        guiButtons.get(CHANGE_SPEED_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToChangeGameSpeedRequest();
            }
        });
        
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToTryAgainButtonRequest();
            }
        });
        
        guiButtons.get(LEAVE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventHandler.respondToLeaveButtonRequest();
            }
        });
        
        guiDecor.get(GAME_SPEED_SLIDER_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (isCurrentScreenState(SETTINGS_SCREEN_STATE))
                {
                    Sprite gameSpeedSlider = guiDecor.get(GAME_SPEED_SLIDER_TYPE);
                }
            }
        });
        
        specials.initSpecialsControls();
        
        this.setKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent ke)
            {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }
    
    /**
     * Invoked when a new game is started, it resets all relevant
     * game data and gui control states. 
     */
    @Override
    public void reset()
    {
        data.reset(this);
    }
    
    /**
     * Updates the state of all gui controls according to the 
     * current game conditions.
     */
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
                    if (!(button instanceof pathXTile))
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