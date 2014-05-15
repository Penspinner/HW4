package pathX.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathX.data.Bandit;
import pathX.data.Intersection;
import pathX.data.Police;
import pathX.data.Road;
import pathX.data.Zombie;
import pathX.data.pathXDataModel;
import pathX.data.pathXLevel;
import pathX.pathX;
import pathX.pathX.pathXPropertyType;
import static pathX.pathXConstants.*;
import properties_manager.PropertiesManager;
/**
 *
 * @author Steven Liao
 */
public class pathXPanel extends JPanel
{
    // THIS IS ACTUALLY OUR pathX APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private pathXMiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private pathXDataModel data;
    
//    private Viewport mapViewport;
    
//    private Viewport gameViewport;
    
    // LEVEL TO BE RENDERED
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
    
    public pathXPanel(pathXMiniGame initGame, pathXDataModel initData)
    {
        game = initGame;
        data = initData;
//        mapViewport = game.getMapViewport();
//        gameViewport = game.getGameViewport();
        
              // MAKE THE RENDER OBJECTS TO BE RECYCLED
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0,0,0,0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++)
        {
            recyclableStrokes.put(i, new BasicStroke(i*2));
        }
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(   GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
            
            Graphics2D g2 = (Graphics2D) g;
            
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            if (((pathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
            {
                renderUSAMap(g2);
                renderLevel(g2);
                renderBackground(g2);
                renderDetails(g2);
                
                renderGUIControls(g2);
            } else if (((pathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE))
            {
                renderLevelBackground(g2);
                renderRoads(g2);
                renderIntersections(g2);
                renderPlayer(g2);
                renderEnemies(g2);
                renderBackground(g2);
                renderBalance(g2);
                renderGUIControls(g2);
                if (game.isDisplayingInfo())
                    renderLevelDetails(g2);
            } else
            {
                // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
                renderBackground(g2);

                // IF CURRENT SCREEN IS HELP SCREEN
                /**
                if (((pathXMiniGame)game).isCurrentScreenState(HELP_SCREEN_STATE))
                {
                    renderHelp(g);
                }*/

                if (((pathXMiniGame)game).isCurrentScreenState(SETTINGS_SCREEN_STATE))
                {
                    renderGameSpeed(g2);
                }
                // AND THE BUTTONS AND DECOR
                renderGUIControls(g2);
            }
        } 
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
    // RENDERING HELPER METHODS
        // - renderBackground
        // - renderGUIControls
        // - renderSnake
        // - renderTiles
        // - renderDialogs
        // - renderGrid
        // - renderDebuggingText
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics2D g2)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g2, bg);
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(pathXTileState.INVISIBLE_STATE.toString()) &&
            !s.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE) &&
            !s.getSpriteType().getSpriteTypeID().contains(MAP_TYPE) &&
            !s.getState().equals(LEVEL_SELECT_SCREEN_STATE) &&
            !s.getState().equals(GAME_SCREEN_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
        } else if (s.getState().equals(LEVEL_SELECT_SCREEN_STATE) ||
                s.getState().equals(GAME_SCREEN_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), img.getWidth(null), img.getHeight(null), null); 
        }
    }
    
    /**
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics2D g2)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g2, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g2, s);
        }
    }
    
    public void renderHelp(Graphics2D g2)
    {
        g2.drawRect(40, 90, WINDOW_WIDTH - 80, WINDOW_HEIGHT - 60);
        Sprite helpSprite = game.getGUIDialogs().get(HELP_DESCRIPTION_TYPE);
        //renderSprite(g, helpSprite);
    }
    
    public void renderUSAMap(Graphics2D g2)
    {
        Sprite map = game.getGUIDecor().get(MAP_TYPE);
        map.setState(pathXTileState.VISIBLE_STATE.toString());
        SpriteType bgST = map.getSpriteType();
        Image img = bgST.getStateImage(map.getState());
        g2.drawImage(img, (int) map.getX(), (int) map.getY(), bgST.getWidth(), bgST.getHeight(), null);
//        Viewport viewport = ((pathXMiniGame)game).getMapViewport();
//        int viewportX = viewport.getViewportX();
//        int viewportY = viewport.getViewportY();
//        g2.drawImage(img, 0, NORTH_PANEL_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT,
//                viewportX, viewportY, WINDOW_WIDTH + viewportX, WINDOW_HEIGHT + viewportY, null);
        }
    
    public void renderDetails(Graphics2D g2)
    {
        g2.setColor(COLOR_TEXT);
        g2.setFont(FONT_TEXT_DISPLAY);
        g2.drawString("Balance: $" + data.getBalance(), DETAILS_X, BALANCE_Y);
        g2.drawString("Goal: $" + GOAL_MONEY, DETAILS_X, GOAL_Y);
    }
    
    public void renderLevel(Graphics2D g2)
    {
        for (Sprite level : game.getGUIButtons().values())
        {
            if (level.getSpriteType().getSpriteTypeID().contains(LEVEL_BUTTON_TYPE))
            {
//                level.setState(pathXTileState.UNSUCCESSFUL_STATE.toString());
                SpriteType sTLevel = level.getSpriteType();
                Image img = sTLevel.getStateImage(level.getState());
                Viewport viewport = ((pathXMiniGame)game).getMapViewport();
                if (level.getY() <= 50)
                    level.setEnabled(false);
                else
                    level.setEnabled(true);
                g2.drawImage(img, (int) level.getX(), (int) level.getY(), sTLevel.getWidth(), sTLevel.getHeight(), null);
                if (level.containsPoint(data.getLastMouseX(), data.getLastMouseY()) &&
                    !level.getState().equals(pathXTileState.LOCKED_STATE.toString()))
                {
                    g2.setFont(FONT_TEXT_DISPLAY);
                    g2.drawString(((pathXTile)level).getName(), 500, 520);
                }
            }
        }
    }
    
    public void renderGameSpeed(Graphics2D g2)
    {
        g2.setFont(FONT_TEXT_DISPLAY);
        g2.drawString("" + data.getGameSpeed(), 500, 358);
    }
    
    public void renderBalance(Graphics2D g2)
    {
        g2.setFont(STATS_TEXT_FONT);
        g2.drawString(data.getLevel().getName(), 210, 30);
        g2.drawString("$" + data.getLevel().getMoney(), 210, 50);
        g2.drawString("Balance: " + data.getBalance(), 20, 400);
    }
    
    /**
     * Draws the strings on the info dialog box depending on the game state.
     * If level just loaded, it will draw a description of the level.
     * Otherwise it will draw a description based on whether you won or lost.
     * 
     * @param g2 
     */
    public void renderLevelDetails(Graphics2D g2)
    {
        // GET THE CURRENT LEVEL
        pathXLevel level = data.getLevel();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        g2.setFont(FONT_TEXT_DISPLAY);
        HashMap<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
        map.put(TextAttribute.FONT, FONT_TEXT_DISPLAY);
        float breakWidth = BREAKWIDTH;
        float drawPosY = 150;
        float drawPosX = 220;
        
        if (!data.won() && !data.lost())
        {
            // FIRST RENDER THE STATE AND CITY OF THE LEVEL
            g2.drawString(level.getName(), 220, 90);

            // THEN RENDER THE DESCRIPTION
            AttributedString s1 = new AttributedString(level.getDescription() + level.getMoney() + "!", map);
            AttributedCharacterIterator paragraph = s1.getIterator();
            int paragraphStart = paragraph.getBeginIndex();
            int paragraphEnd = paragraph.getEndIndex();
            FontRenderContext frc = g2.getFontRenderContext();

            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            lineMeasurer.setPosition(paragraphStart);

            while (lineMeasurer.getPosition() < paragraphEnd)
            {
                TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                drawPosY += layout.getAscent();
                layout.draw(g2, drawPosX, drawPosY);
                drawPosY += layout.getDescent() + layout.getLeading();
            }
        } else if (data.won())
        {
            String winTextTop = props.getProperty(pathXPropertyType.TEXT_WIN_TOP);
            g2.drawString(winTextTop, 220, 90);
            
            // THEN RENDER THE DESCRIPTION
            String winTextBottom = props.getProperty(pathXPropertyType.TEXT_WIN_BOTTOM);
            AttributedString s1 = new AttributedString(winTextBottom + level.getMoney() + "!", map);
            AttributedCharacterIterator paragraph = s1.getIterator();
            int paragraphStart = paragraph.getBeginIndex();
            int paragraphEnd = paragraph.getEndIndex();
            FontRenderContext frc = g2.getFontRenderContext();

            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            lineMeasurer.setPosition(paragraphStart);

            while (lineMeasurer.getPosition() < paragraphEnd)
            {
                TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                drawPosY += layout.getAscent();
                layout.draw(g2, drawPosX, drawPosY);
                drawPosY += layout.getDescent() + layout.getLeading();
            }
        } else if (data.lost())
        {
            String loseTextTop = props.getProperty(pathXPropertyType.TEXT_LOSE_TOP);
            g2.drawString(loseTextTop, 220, 90);
            
            // THEN RENDER THE DESCRIPTION
            String loseTextBottom = props.getProperty(pathXPropertyType.TEXT_LOSE_BOTTOM);
            AttributedString s1 = new AttributedString(loseTextBottom, map);
            AttributedCharacterIterator paragraph = s1.getIterator();
            int paragraphStart = paragraph.getBeginIndex();
            int paragraphEnd = paragraph.getEndIndex();
            FontRenderContext frc = g2.getFontRenderContext();

            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            lineMeasurer.setPosition(paragraphStart);

            while (lineMeasurer.getPosition() < paragraphEnd)
            {
                TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                drawPosY += layout.getAscent();
                layout.draw(g2, drawPosX, drawPosY);
                drawPosY += layout.getDescent() + layout.getLeading();
            }
        }
    }
    
        // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics2D g2)
    {
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        Image backgroundImage = data.getBackgroundImage();
        g2.drawImage(backgroundImage, GAME_OFFSET, 0, gameViewport.getViewportWidth(), gameViewport.getViewportHeight(), 
                gameViewport.getViewportX(), gameViewport.getViewportY(), 
                gameViewport.getViewportX() + gameViewport.getViewportWidth() - GAME_OFFSET, gameViewport.getViewportY() + gameViewport.getViewportHeight(), null);
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2)
    {
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        Viewport viewport = data.getViewport();
        Iterator<Road> it = data.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext())
        {
            Road road = it.next();
            if (!data.isSelectedRoad(road) && road.open)
                renderRoad(g2, road, INT_OUTLINE_COLOR);
        }
        
        // NOW DRAW THE LINE BEING ADDED, IF THERE IS ONE
//        if (data.isAddingRoadEnd())
//        {
//            Intersection startRoadIntersection = data.getStartRoadIntersection();
//            recyclableLine.x1 = startRoadIntersection.x-viewport.getViewportX();
//            recyclableLine.y1 = startRoadIntersection.y-viewport.getViewportY();
//            recyclableLine.x2 = data.getLastMouseX()-viewport.getViewportX();
//            recyclableLine.y2 = data.getLastMouseY()-viewport.getViewportY();
//            g2.draw(recyclableLine);
//        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
//        Road selectedRoad = data.getSelectedRoad();
//        if (selectedRoad != null)
//        {
//            renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
//        }
    }
    
    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c)
    {
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        g2.setColor(c);
        int strokeId = road.getSpeedLimit()/10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x-gameViewport.getViewportX();
        recyclableLine.y1 = road.getNode1().y-gameViewport.getViewportY();
        recyclableLine.x2 = road.getNode2().x-gameViewport.getViewportX();
        recyclableLine.y2 = road.getNode2().y-gameViewport.getViewportY();

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay())
        {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }

    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2)
    {
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        Iterator<Intersection> it = data.intersectionsIterator();
        while (it.hasNext())
        {
            Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!data.isStartingLocation(intersection))
                && (!data.isDestination(intersection))
                && gameViewport.isCircleBoundingBoxInsideViewport(intersection.x, intersection.y, INTERSECTION_RADIUS))
            {
                // FIRST FILL
                if (intersection.isOpen())
                {
                    g2.setColor(OPEN_INT_COLOR);
                } else
                {
                    g2.setColor(CLOSED_INT_COLOR);
                }
                recyclableCircle.x = intersection.x - gameViewport.getViewportX() - INTERSECTION_RADIUS;
                recyclableCircle.y = intersection.y - gameViewport.getViewportY() - INTERSECTION_RADIUS;
                g2.fill(recyclableCircle);

                // AND NOW THE OUTLINE
                if (data.isSelectedIntersection(intersection))
                {
                    g2.setColor(HIGHLIGHTED_COLOR);
                } else
                {
                    g2.setColor(INT_OUTLINE_COLOR);
                }
                Stroke s = recyclableStrokes.get(INT_STROKE);
                g2.setStroke(s);
                g2.draw(recyclableCircle);
            }
        }

        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        Image startImage = data.getStartingLocationImage();
        Intersection startInt = data.getStartingLocation();
        renderIntersectionImage(g2, startImage, startInt);

        Image destImage = data.getDesinationImage();
        Intersection destInt = data.getDestination();
        renderIntersectionImage(g2, destImage, destInt);
    }

    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i)
    {
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x-(w/2);
        int y1 = i.y-(h/2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);
        
        // ONLY RENDER IF INSIDE THE VIEWPORT
        if (gameViewport.isRectInsideViewport(x1, y1, x2, y2))
        {
            g2.drawImage(img, x1 - gameViewport.getViewportX(), y1 - gameViewport.getViewportY(), null);
        }        
    }

    // HELPER METHOD FOR RENDERING SOME SCREEN STATS, WHICH CAN
    // HELP WITH DEBUGGING
    private void renderStats(Graphics2D g2)
    {
        Viewport viewport = data.getViewport();
        g2.setColor(STATS_TEXT_COLOR);
        g2.setFont(STATS_TEXT_FONT);
        g2.drawString(MOUSE_SCREEN_POSITION_TITLE + data.getLastMouseX() + ", " + data.getLastMouseY(),
                STATS_X, MOUSE_SCREEN_POSITION_Y);
        int levelMouseX = data.getLastMouseX() + viewport.getViewportX();
        int levelMouseY = data.getLastMouseY() + viewport.getViewportY();
        g2.drawString(MOUSE_LEVEL_POSITION_TITLE + levelMouseX + ", " + levelMouseY,
                STATS_X, MOUSE_LEVEL_POSITION_Y);
        g2.drawString(VIEWPORT_POSITION_TITLE + viewport.getViewportX() + ", " + viewport.getViewportY(),
                STATS_X, VIEWPORT_POSITION_Y);
    }
    
    // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
    }
    
    // RENDERS THE PLAYER NEXT TO THE STARTING LOCATION AT FIRST
    // THEN RENDERS ACCORDING TO USER
    public void renderPlayer(Graphics2D g2)
    {
//        pathXTile player = game.getGUICharacters().get(PLAYER_TYPE);
        pathXTile player = data.getPlayer();
        SpriteType sT = player.getSpriteType();
        Image img = sT.getStateImage(player.getState());
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        g2.drawImage(img,
                (int) player.getX() - gameViewport.getViewportX(), 
                (int) player.getY() - gameViewport.getViewportY(), 
                null);
    }
    
    private void renderEnemies(Graphics2D g2)
    {
        Viewport gameViewport = ((pathXMiniGame)game).getGameViewport();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        SpriteType spriteType;
        
        Iterator<Zombie> itZ = data.zombiesIterator();
        while (itZ.hasNext())
        {
            Zombie currentZombie = itZ.next();
            spriteType = currentZombie.getSpriteType();
            Image zombieImage = spriteType.getStateImage(currentZombie.getState());
            g2.drawImage(zombieImage, (int) currentZombie.getX() - gameViewport.getViewportX() - INTERSECTION_RADIUS, 
                    (int) currentZombie.getY() - gameViewport.getViewportY() - INTERSECTION_RADIUS, null);
        }
        
        Iterator<Police> itP = data.policesIterator();
        while (itP.hasNext())
        {
            Police currentPolice = itP.next();
            spriteType = currentPolice.getSpriteType();
            Image policeImage = spriteType.getStateImage(currentPolice.getState());
            g2.drawImage(policeImage, (int) currentPolice.getX() - gameViewport.getViewportX() - INTERSECTION_RADIUS, 
                    (int) currentPolice.getY() - gameViewport.getViewportY() - INTERSECTION_RADIUS, null);
        }
        
        Iterator<Bandit> itB = data.banditsIterator();
        while (itB.hasNext())
        {
            Bandit currentBandit = itB.next();
            spriteType = currentBandit.getSpriteType();
            Image banditImage = spriteType.getStateImage(currentBandit.getState());
            g2.drawImage(banditImage, (int)currentBandit.getX() - gameViewport.getViewportX() - INTERSECTION_RADIUS, 
                    (int) currentBandit.getY() - gameViewport.getViewportY() - INTERSECTION_RADIUS, null);
        }
    }
}