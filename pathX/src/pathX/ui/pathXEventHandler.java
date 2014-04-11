package pathX.ui;

import java.awt.event.KeyEvent;
/**
 *
 * @author Steven Liao
 */
public class pathXEventHandler 
{
    // THE PATHX GAME, IT PROVIDES ACCESS TO EVERYTHING
    private pathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public pathXEventHandler(pathXMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
    
    /**
     * Called when the user clicks the Play button.
     */
    public void respondToPlayGameRequest()
    {
        if (game.getDataModel().inProgress())
        {
            
        }
        else
        {
            game.switchToLevelSelectScreen();
        }
    }
    
    
    public void respondToResetGameRequest()
    {
        
    }
    
    
    public void respondToSettingsRequest()
    {
        game.switchToSettingsScreen();
    }
    
    
    public void respondToHelpRequest()
    {
        game.switchToHelpScreen();
    }
    
    public void respondToHomeRequest()
    {
        game.switchToMenuScreen();
    }
}
