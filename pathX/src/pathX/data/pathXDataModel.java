package pathX.data;

import java.util.ArrayList;
import java.util.Iterator;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import pathX.ui.pathXMiniGame;
/**
 *
 * @author Steven Liao
 */
public class pathXDataModel extends MiniGameDataModel 
{
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private pathXMiniGame game;
    
    private ArrayList<pathXLevel> levels;
    
    // THE GAME SPEED
    private int gameSpeed;
    
    public pathXDataModel(pathXMiniGame initGame)
    {
        game = initGame;
        levels = new ArrayList<pathXLevel>();
    }
    
    public Iterator getLevelsIterator()
    {
        return levels.iterator();
    }
    
    // ACCESSOR METHODS
    public int getGameSpeed()
    {
        return gameSpeed;
    }
    
    // MUTATOR METHODS
    public void setGameSpeed(int initGameSpeed)
    {
        gameSpeed = initGameSpeed;
    }
    
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The pathX game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y)
    {
        
    }
    
    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game)
    {
        
    }
    
    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game The pathX game to be updated.
     */
    @Override
    public void updateAll(MiniGame game)
    {
        
    }
    
    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The pathX game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game)
    {
    }
}
