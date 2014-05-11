package pathX.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathX.data.pathXDataModel;
import pathX.pathX;
import static pathX.pathXConstants.SPECIALS_NAME_LIST;
import static pathX.pathXConstants.SPECIALS_X_COORDINATE;
import static pathX.pathXConstants.SPECIALS_Y_COORDINATE;
import properties_manager.PropertiesManager;

/**
 *
 * @author Steven Liao
 */
public class pathXSpecials 
{
    private pathXMiniGame game;
    
    private pathXDataModel data;
    
    private ArrayList<pathXTile> specialTiles;
    
    /**
     * Constructor to keep track of the specials
     * 
     * @param initGame 
     */
    public pathXSpecials(pathXMiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
        
        specialTiles = new ArrayList();
    }
    
    public ArrayList<pathXTile> getSpecialTiles()
    {
        return specialTiles;
    }
    
    public void initSpecials()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathX.pathXPropertyType.PATH_IMG);
        SpriteType sT;
        
        // LOAD ALL SPECIALS FROM A SPRITE SHEET
        String specialsSpriteSheet = props.getProperty(pathX.pathXPropertyType.IMAGE_SPRITE_SHEET_SPECIALS);
        ArrayList<BufferedImage> specialImages = game.loadSpriteSheetImagesWithColorKey(imgPath + specialsSpriteSheet,
                    16, 4, 4, 0, 0, Color.WHITE);
        
        for (int i = 0; i < specialImages.size(); i++)
        {
            BufferedImage img = specialImages.get(i);
            
            sT = new SpriteType(SPECIALS_NAME_LIST[i]);
            
            // SET THE X AND Y COORDINATES OF THE TILE
            int x = SPECIALS_X_COORDINATE + ((i % 4) * 40);
            int y = SPECIALS_Y_COORDINATE + ((i / 4) * 40);
            
            sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
            pathXTile newSpecial = new pathXTile(sT, x, y, 0, 0, 
                    pathXTileState.INVISIBLE_STATE.toString(), SPECIALS_NAME_LIST[i]);
            newSpecial.setActionCommand(SPECIALS_NAME_LIST[i]);
            
            // ADD IT TO THE LIST OF SPECIALS
            specialTiles.add(newSpecial);
            game.getGUIButtons().put(SPECIALS_NAME_LIST[i], newSpecial);
        }
    }
    
    public void initSpecialsControls()
    {   
        for (int i = 0; i < SPECIALS_NAME_LIST.length; i++)
        {
            pathXTile special = (pathXTile) game.getGUIButtons().get(SPECIALS_NAME_LIST[i]);
            special.setActionListener(new ActionListener() 
            {
                pathXTile px;
                public ActionListener init(pathXTile initPX)
                {
                    px = initPX;
                    return this;
                }
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    game.getSpecialsHandler().useSpecial(px.getActionCommand());
                }
            }.init(special));
        }
//        for (pathXTile special : specialTiles)
//        {
//            special.setActionListener(new ActionListener() 
//            {
//                pathXTile px;
//                public ActionListener init(pathXTile initPX)
//                {
//                    px = initPX;
//                    return this;
//                }
//                @Override
//                public void actionPerformed(ActionEvent e) 
//                {
//                    game.getSpecialsHandler().useSpecial(px.getActionCommand());
//                }
//            }.init(special));
//        }
    }
    
    public enum pathXSpecialsState
    {
        LOCKED_STATE,
        UNLOCKED_STATE
    }
}
