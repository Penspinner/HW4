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
    public static final String LEVEL_SCHEMA = "PathXLevelSchema.xsd";
    public static final String PATH_DATA = "./data/";
    public static final String LEVELS_PATH = PATH_DATA + "./levels/";
    public static final String LEVEL_IMAGES_PATH = "/level_images/";
    
    // TITLE OF THE APP
    public static final String APP_TITLE = "pathX";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // GAME DESCORATIONS
    public static final String MAP_TYPE = "MAP_TYPE";
    public static final String HELP_DESCRIPTION_TYPE = "HELP_DESCRIPTION_TYPE";
    public static final String INFO_DIALOG_BOX_TYPE = "INFO_DIALOG_BOX_TYPE";
    public static final String PLAYER_TYPE = "PLAYER_TYPE";
    public static final String ZOMBIE_TYPE = "ZOMBIE_TYPE";
    public static final String POLICE_TYPE = "POLICE_TYPE";
    public static final String BANDIT_TYPE = "BANDIT_TYPE";
    
    // IN GAME MENU SCREEN USER INTERFACE CONTROLS
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
    
    // SPECIALS NAME ID
    public static final String[] SPECIALS_NAME_LIST =  {"MAKE_LIGHT_GREEN",
                                                        "MAKE_LIGHT_RED",
                                                        "FLAT_TIRE",
                                                        "EMPTY_GAS_TANK",
                                                        "DECREASE_SPEED_LIMIT",
                                                        "INCREASE_SPEED_LIMIT",
                                                        "INCREASE_PLAYER_SPEED",
                                                        "CLOSE_ROAD",
                                                        "CLOSE_INTERSECTION",
                                                        "OPEN_INTERSECTION",
                                                        "STEAL",
                                                        "MIND_CONTROL",
                                                        "INTANGIBILITY",
                                                        "MINDLESS_TERROR",
                                                        "FLYING",
                                                        "INVINCIBILITY"};
    
    // LEVEL SELECT SCREEN USER INTERFACE CONTROLS
    public static final String[] SCROLL_BUTTON_TYPE = {"SCROLL_UP_BUTTON_TYPE",
                                                        "SCROLL_DOWN_BUTTON_TYPE",
                                                        "SCROLL_LEFT_BUTTON_TYPE",
                                                        "SCROLL_RIGHT_BUTTON_TYPE"};
    public static final String SCROLL_UP_BUTTON_TYPE = "SCROLL_UP_BUTTON_TYPE";
    public static final String SCROLL_DOWN_BUTTON_TYPE = "SCROLL_DOWN_BUTTON_TYPE";
    public static final String SCROLL_LEFT_BUTTON_TYPE = "SCROLL_LEFT_BUTTON_TYPE";
    public static final String SCROLL_RIGHT_BUTTON_TYPE = "SCROLL_RIGHT_BUTTON_TYPE";
    public static final String PAUSE_BUTTON_TYPE = "PAUSE_BUTTON_TYPE";
    public static final String LEVEL_BUTTON_TYPE = "LEVEL_BUTTON_TYPE";
    
    // SETTINGS BUTTONS
    public static final String SOUND_MUTE_BOX_BUTTON_TYPE = "SOUND_MUTE_BOX_TYPE";
    public static final String MUSIC_MUTE_BOX_BUTTON_TYPE = "MUSIC_MUTE_BOX_TYPE";
    public static final String GAME_SPEED_SLIDER_TYPE = "GAME_SPEED_SLIDER_TYPE";
    public static final String CHANGE_SPEED_BUTTON_TYPE = "CHANGE_SPEED_BUTTON_TYPE";
    
    // GAMEPLAY BUTTONS
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    public static final String CLOSE_BUTTON_TYPE = "CLOSE_BUTTON_TYPE";
    public static final String TRY_AGAIN_BUTTON_TYPE = "TRY_AGAIN_BUTTON_TYPE";
    public static final String LEAVE_BUTTON_TYPE = "LEAVE_BUTTON_TYPE";
    
    // STATES TO SWITCH BETWEEN SCREENS
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    
    // GAME VIEWPORT OFFSET
    public static final int GAME_OFFSET = 200;
    
    // UI CONTROL SIZE AND OFFSETS
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 553;
    public static final int NORTH_PANEL_HEIGHT = 80;
    public static final int MENU_BUTTON_WIDTH = 128;
    public static final int MENU_BUTTON_HEIGHT = 40;
    public static final int MENU_BUTTON_MARGIN = 5;
    public static final int MENU_BUTTON_Y = 450;
    public static final int EXIT_BUTTON_X = 855;
    public static final int HOME_BUTTON_X = EXIT_BUTTON_X - 38 - MENU_BUTTON_MARGIN;
    public static final int START_BUTTON_X = 20;
    public static final int START_BUTTON_Y = 140;
    public static final int MAP_Y = 80;
    public static final int SCROLL_UP_BUTTON_X = 60;
    public static final int SCROLL_UP_BUTTON_Y = WINDOW_HEIGHT - 133;
    public static final int SCROLL_DOWN_BUTTON_X = SCROLL_UP_BUTTON_X;
    public static final int SCROLL_DOWN_BUTTON_Y = WINDOW_HEIGHT - 73;
    public static final int SCROLL_LEFT_BUTTON_X = 30;
    public static final int SCROLL_LEFT_BUTTON_Y = WINDOW_HEIGHT - 103;
    public static final int SCROLL_RIGHT_BUTTON_X = SCROLL_LEFT_BUTTON_X + 60;
    public static final int SCROLL_RIGHT_BUTTON_Y = WINDOW_HEIGHT - 103;
    public static final int PAUSE_BUTTON_X = SCROLL_UP_BUTTON_X;
    public static final int PAUSE_BUTTON_Y = WINDOW_HEIGHT - 103;
    public static final int INFO_DIALOG_BOX_X = 200;
    public static final int INFO_DIALOG_BOX_Y = 50;
    public static final int CLOSE_BUTTON_X = 400;
    public static final int CLOSE_BUTTON_Y = 400;
    public static final int TRY_AGAIN_BUTTON_X = INFO_DIALOG_BOX_X + 75;
    public static final int TRY_AGAIN_BUTTON_Y = CLOSE_BUTTON_Y;
    public static final int LEAVE_BUTTON_X = INFO_DIALOG_BOX_X + 375;
    public static final int LEAVE_BUTTON_Y = CLOSE_BUTTON_Y;
    public static final int SOUND_MUTE_BOX_BUTTON_X = 400;
    public static final int SOUND_MUTE_BOX_BUTTON_Y = 160;
    public static final int MUSIC_MUTE_BOX_BUTTON_X = SOUND_MUTE_BOX_BUTTON_X;
    public static final int MUSIC_MUTE_BOX_BUTTON_Y = SOUND_MUTE_BOX_BUTTON_Y + 80;
    public static final int CHANGE_SPEED_X = 375;
    public static final int CHANGE_SPEED_Y = 400;
    public static final int GAME_SPEED_SLIDER_X = 450;
    public static final int GAME_SPEED_SLIDER_Y = 370;
    public static final int DETAILS_X = 380;
    public static final int BALANCE_Y = 30;
    public static final int GOAL_Y = 60;
    public static final int GOAL_MONEY = 140000;
    public static final int SPECIALS_X_COORDINATE = 20;
    public static final int SPECIALS_Y_COORDINATE = 200;
    public static final int HELP_DESCRIPTION_X = 50;
    public static final int HELP_DESCRIPTION_Y = 95;
    public static final int BREAKWIDTH = 560;
    
    // COORDINATES OF THE LEVELS
    public static final int[] LEVEL_X_COORDINATES = {150,200,180,345,500,
                                                    450,150,70,80,740,
                                                    760,780,1215,1250,1230,
                                                    1325,1350,1375,25,125};
    public static final int[] LEVEL_Y_COORDINATES = {120,130,160,155,195,
                                                    230,640,460,645,830,
                                                    940,900,955,950,980,
                                                    365,400,405,500,120};
    
    // SCROLLING CONSTANT
    public static final int SCROLL_PIXELS = 5;
    
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
    
    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR   = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;
    
    // FONTS FOR DISPLAYING TEXT
    public static final Font FONT_TEXT_DISPLAY = new Font("Candara", Font.BOLD, 30);
    
    // AND FOR THE ROAD SPEED LIMITS
    public static final int DEFAULT_SPEED_LIMIT = 30;
    public static final int MIN_SPEED_LIMIT = 10;
    public static final int MAX_SPEED_LIMIT = 100;
    public static final int SPEED_LIMIT_STEP = 10;
    
    // RENDERING SETTINGS
    public static final int INTERSECTION_RADIUS = 20;
    public static final int INT_STROKE = 3;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 40;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 60;
    
    // FOR RENDERING STATS
    public static final Color STATS_TEXT_COLOR = Color.ORANGE;
    public static final Font STATS_TEXT_FONT = new Font("Monospace", Font.BOLD, 16);
    public static final String MOUSE_SCREEN_POSITION_TITLE = "Screen Mouse Position: ";
    public static final String MOUSE_LEVEL_POSITION_TITLE = "Level Mouse Position: ";
    public static final String VIEWPORT_POSITION_TITLE = "Viewport Position: ";
    
    // FOR POSITIONING THE STATS
    public static final int STATS_X = 20;
    public static final int STATS_Y_DIFF = 20;
    public static final int MOUSE_SCREEN_POSITION_Y = 20;
    public static final int MOUSE_LEVEL_POSITION_Y = MOUSE_SCREEN_POSITION_Y + STATS_Y_DIFF;
    public static final int VIEWPORT_POSITION_Y = MOUSE_LEVEL_POSITION_Y + STATS_Y_DIFF;
    
    // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String ZOMBIE_LIST_NODE = "zombies_list";
    public static final String ZOMBIE_NODE = "zombie";
    public static final String POLICE_LIST_NODE = "polices_list";
    public static final String POLICE_NODE2 = "policee";
    public static final String BANDIT_LIST_NODE = "bandits_list";
    public static final String BANDIT_NODE = "bandit";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";

    // AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";
    
    // FOR NICELY FORMATTED XML OUTPUT
    public static final String XML_INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
    public static final String XML_INDENT_VALUE = "5";
    public static final String YES_VALUE = "Yes";
}