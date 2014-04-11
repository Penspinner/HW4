package pathX;

import java.awt.Color;
import java.awt.Font;

/**
 * This class stores all the constants used by pathX application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 *
 * @author Steven Liao
 */
public class pathXConstants 
{
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static final String PROPERTY_TYPES_LIST = "property_types.txt";
    public static final String PROPERTIES_FILE_NAME = "properties.xml";
    public static final String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static final String PATH_DATA = "./data/";
    
    // TITLE OF THE APP
    public static final String APP_TITLE = "pathX";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    //
    public static final String MAP_TYPE = "MAP_TYPE";
    
    // IN GAME USER INTERFACE CONTROLS
    public static final String[] MENU_BUTTON_TYPE = {"PLAY_BUTTON_TYPE",
                                                     "RESET_BUTTON_TYPE",
                                                     "SETTINGS_BUTTON_TYPE",
                                                     "HELP_BUTTON_TYPE",
                                                     "EXIT_BUTTON_TYPE",
                                                     "HOME_BUTTON_TYPE"};
    public static final String HOME_BUTTON_TYPE = "HOME_BUTTON_TYPE";
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String SETTINGS_BUTTON_TYPE = "SETTINGS_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_BUTTON_TYPE";
    public static final String EXIT_BUTTON_TYPE = "EXIT_BUTTON>TYPE";
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    
    // STATES TO SWITCH BETWEEN SCREENS
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    
    
    // UI CONTROL SIZE
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 553;
    public static final int MENU_BUTTON_WIDTH = 128;
    public static final int MENU_BUTTON_HEIGHT = 40;
    public static final int MENU_BUTTON_MARGIN = 5;
    public static final int MENU_BUTTON_Y = 450;
    public static final int EXIT_BUTTON_X = 855;
    public static final int HOME_BUTTON_X = EXIT_BUTTON_X - 38 - MENU_BUTTON_MARGIN;
    public static final int START_BUTTON_X = 20;
    public static final int START_BUTTON_Y = 120;
    public static final int MAP_Y = 80;
    
    // PRICES OF SPECIALS
    public static final int COST_CHANGE_LIGHTS = 5;
    public static final int COST_FREEZE_TIME = 10;
    public static final int COST_CHANGE_SPEED_LIMIT = 15;
    public static final int COST_CAR_MANIPULATION = 20;
    public static final int COST_CHANGE_ROADS = 25;
    public static final int COST_CAR_CONTROL = 30;
    public static final int COST_SUPERPOWERS = 40;
    
    // MUTE/UNMUTE THE VOLUME OF THE MUSIC OR SOUND EFFECT
    public static final int MUTE_VOLUME = 0;
    public static final int UNMUTE_VOLUME = 100;
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;
    
    // COLORS FOR RENDERING TEXT
    public static final Color COLOR_TEXT = Color.BLACK;
    
    // FONTS FOR DISPLAYING TEXT
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.MONOSPACED, Font.BOLD, 16);
}