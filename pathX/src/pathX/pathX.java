package pathX;

import properties_manager.PropertiesManager;
import static pathX.pathXConstants.*;
import xml_utilities.InvalidXMLFileFormatException;
import pathX.ui.pathXMiniGame;
import pathX.ui.pathXErrorHandler;
/**
 * pathX is a game application that is ready to be customized 
 * to play different flavors of the game.
 * 
 * @author Steven Liao
 */
public class pathX 
{
    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    static pathXMiniGame miniGame = new pathXMiniGame();
    
    /**
     * This is where the pathX game application starts execution. We'll
     * load the application properties and then use them to build our
     * user interface and start the window in real-time mode.
     */
    public static void main(String[] args)
    {
        try
        {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);

            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(pathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);
        } 
        // CATCH THE ESCEPTION IF THERE WAS AN ERROR WITH THE XML FILE
        catch (InvalidXMLFileFormatException ixmlffe)
        {
            
        }
    }
    
    /**
     * 
     */
    public enum pathXPropertyType
    {
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // pathX_properties.xml
        IMAGE_BACKGROUND_MENU,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_HOME_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_PLAY_GAME,
        IMAGE_BUTTON_PLAY_GAME_MOUSE_OVER,
        IMAGE_BUTTON_RESET_GAME,
        IMAGE_BUTTON_RESET_GAME_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        
        // AUDIO CUES
        AUDIO_CUE_LOSE,
        AUDIO_CUE_WIN,
        AUDIO_CUE_SELECT,
        AUDIO_CUE_ZOMBIE,
        AUDIO_CUE_BANDIT,
        AUDIO_CUE_POLICE,
        SONG_CUE_MENU_SCREEN,
        SONG_CUE_GAME_SCREEN,
    }
}