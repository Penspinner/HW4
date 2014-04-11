package pathX.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
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
     * This method switches the application to the level select screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToLevelSelectScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToSettingsScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        // DEACTIVATE ALL MENU CONTROLS
        disableMenuButtons();
        
        // ACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToGameScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
    }
    
    /**
     * This method switches the application to the game screen, making
     * all the appropriate UI controls visible & invisible.
     */
    public void switchToMenuScreen()
    {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        
        // DEACTIVATE THE HOME BUTTON
        guiButtons.get(HOME_BUTTON_TYPE).setState(pathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        
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
    
    @Override
    public void initAudioContent()
    {
        
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
        
        sT = new SpriteType(MAP_TYPE);
        img = loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_MAP_USA));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, MAP_Y, 0, 0, pathXTileState.INVISIBLE_STATE.toString());
        guiDecor.put(MAP_TYPE, s);
        
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
                eventHandler.respondToExitRequest(); 
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
    }
    
    @Override
    public void reset()
    {
        
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
