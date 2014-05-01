package pathX.data;

import java.awt.Image;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import pathX.pathX.pathXPropertyType;
import pathX.ui.pathXMiniGame;
import static pathX.pathXConstants.*;
import pathX.ui.pathXTile;
import pathX.ui.pathXTileState;
import properties_manager.PropertiesManager;
/**
 *
 * @author Steven Liao
 */
public class pathXDataModel extends MiniGameDataModel 
{
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private pathXMiniGame game;
    
    // THE CURRENT LEVEL
    private pathXLevel level;
    
    ArrayList<String> levelNames;
    ArrayList<String> levelStates;
    ArrayList<pathXTile> movingTiles;
    ArrayList<pathXTile> zombies;
    ArrayList<pathXTile> police;
    ArrayList<pathXTile> bandits;
    
    HashMap<String, pathXTile> guiCharacters;
    
    PropertiesManager props = PropertiesManager.getPropertiesManager();

    // WE ONLY NEED TO TURN THIS ON ONCE
    boolean levelBeingEdited;
    Image backgroundImage;
    Image startingLocationImage;
    Image destinationImage;
    Image playerImage;
    Image zombieImage;
    Image policeImage;
    Image banditImage;

    // THE SELECTED INTERSECTION OR ROAD MIGHT BE EDITED OR DELETED
    // AND IS RENDERED DIFFERENTLY
    Intersection selectedIntersection;
    Road selectedRoad;
    
    // WE'LL USE THIS WHEN WE'RE ADDING A NEW ROAD
    Intersection startRoadIntersection;
    
    // THESE BOOLEANS HELP US KEEP TRACK OF
    // @todo DO WE NEED THESE?
    boolean isMousePressed;
    boolean isDragging;
    boolean dataUpdatedSinceLastSave;
    
    // THE GAME SPEED
    int gameSpeed;
    int currentLevelCounter;
    int balance;
    int goal;
    
    public pathXDataModel(pathXMiniGame initGame)
    {
        game = initGame;
        level = new pathXLevel();
        levelNames = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        levelStates = new ArrayList();
        movingTiles = new ArrayList<pathXTile>();
        initLevelStates();
        initCharacterImages();
    }
    
    // ACCESSOR METHODS
    public pathXLevel           getLevel()                  {   return level;                   }
    public int                  getGameSpeed()              {   return gameSpeed;               }
    public int                  getCurrentLevelCounter()    {   return currentLevelCounter;     }
    public int                  getBalance()                {   return balance;                 }
    public ArrayList<String>    getLevelNames()             {   return levelNames;              }
    public ArrayList<String>    getLevelStates()            {   return levelStates;             }
    public ArrayList<pathXTile> getMovingTiles()            {   return movingTiles;             }
    public ArrayList<pathXTile> getZombies()                {   return zombies;                 }
    public ArrayList<pathXTile> getPolice()                 {   return police;                  }
    public ArrayList<pathXTile> getBandits()                {   return bandits;                 }
    public Image                getBackgroundImage()        {   return backgroundImage;         }
    public Image                getStartingLocationImage()  {   return startingLocationImage;   }
    public Image                getDesinationImage()        {   return destinationImage;        }
    public Image                getPlayerImage()            {   return playerImage;             }
    public Image                getZombieImage()            {   return zombieImage;             }
    public Image                getPoliceImage()            {   return policeImage;             }
    public Image                getBanditImage()            {   return banditImage;             }
    public Intersection         getSelectedIntersection()   {   return selectedIntersection;    }
    public Road                 getSelectedRoad()           {   return selectedRoad;            }
    public Intersection         getStartRoadIntersection()  {   return startRoadIntersection;   }
    public Intersection         getStartingLocation()       {   return level.startingLocation;  }
    public Intersection         getDestination()            {   return level.destination;       }
    public boolean              isDataUpdatedSinceLastSave(){   return dataUpdatedSinceLastSave;}    
    public boolean isStartingLocation(Intersection testInt)  
    {   return testInt == level.startingLocation;           }
    public boolean isDestination(Intersection testInt)
    {   return testInt == level.destination;                }
    public boolean isSelectedIntersection(Intersection testIntersection)
    {   return testIntersection == selectedIntersection;    }
    public boolean isSelectedRoad(Road testRoad)
    {   return testRoad == selectedRoad;                    }
    
    // INITIALIZES THE IMAGES FOR THE PLAYER, ZOMBIE, POLICE, AND BANDITS
    public void initCharacterImages()
    {
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        playerImage = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_PLAYER));
        zombieImage = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_ZOMBIE));
        policeImage = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_POLICE));
        banditImage = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_BANDIT));
    }
    
    public void initPlayerStartingLocation()
    {
        pathXTile player = game.getGUICharacters().get(PLAYER_TYPE);
        int x = level.getStartingLocation().x + startingLocationImage.getWidth(null) + GAME_OFFSET;
        int location = (startingLocationImage.getHeight(null) - (int) player.getAABBheight()) / 2;
        int y = level.getStartingLocation().y + location;
        int w = startingLocationImage.getWidth(null);
        int h = startingLocationImage.getHeight(null);
        int x1 = level.getStartingLocation().x-(w/2) + startingLocationImage.getWidth(null) + GAME_OFFSET;
        int y1 = level.getStartingLocation().y-(h/2) + location;
        int x2 = x1 + w + GAME_OFFSET;
        int y2 = y1 + h + location;
        player.setX(x1);
        player.setY(y1);
    }
    
    public void initZombieLocation()
    {
        pathXTile zombie = game.getGUICharacters().get(ZOMBIE_TYPE);
        zombies = new ArrayList(level.numZombies);
        for (int i = 0; i < level.numZombies; i++)
        {
            zombie.setX(level.getIntersections().get(i+2).x + GAME_OFFSET);
            zombie.setY(level.getIntersections().get(i+2).y);
            zombies.add(zombie);
        }
    }
    
    // ITERATOR METHODS FOR GOING THROUGH THE GRAPH
    public Iterator intersectionsIterator()
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        return intersections.iterator();
    }
    public Iterator roadsIterator()
    {
        ArrayList<Road> roads = level.roads;
        return roads.iterator();
    }
    
    public void initLevelStates()
    {
        levelStates.add(pathXTileState.UNSUCCESSFUL_STATE.toString());
        
        // ALL LEVEL BUTTON STATES SHOULD BE LOCKED UNTIL THE PREVIOUS LEVEL
        // HAS BEEN SUCCESSFULLY ROBBED
        for (int i = 1; i < 20; i++)
        {
            levelStates.add(pathXTileState.LOCKED_STATE.toString());
        }
    }
    
    /**
     * Updates the background image.
     */
    public void updateBackgroundImage(String newBgImage)
    {
        // UPDATE THE LEVEL TO FIT THE BACKGROUDN IMAGE SIZE
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.backgroundImageFileName = newBgImage;
        backgroundImage = game.loadImage(imgPath + LEVEL_IMAGES_PATH + level.backgroundImageFileName);
        int levelWidth = backgroundImage.getWidth(null);
        int levelHeight = backgroundImage.getHeight(null);
        viewport.setGameWorldSize(levelWidth, levelHeight);
    }

    /**
     * Updates the image used for the starting location and forces rendering.
     */
    public void updateStartingLocationImage(String newStartImage)
    {
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.startingLocationImageFileName = newStartImage;
        startingLocationImage = game.loadImage(imgPath + LEVEL_IMAGES_PATH + level.startingLocationImageFileName);
    }

    /**
     * Updates the image used for the destination and forces rendering.
     */
    public void updateDestinationImage(String newDestImage)
    {
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        level.destinationImageFileName = newDestImage;
        destinationImage = game.loadImage(imgPath + LEVEL_IMAGES_PATH + level.destinationImageFileName);
    }
    
    /**
     * For changing the edit mode, and thus what edit operations
     * the user may perform.
     */
//    public void switchEditMode(PXLE_EditMode initEditMode)
//    {
//        if (levelBeingEdited)
//        {
//            // SET THE NEW EDIT MODE
////            editMode = initEditMode;
//            
//            // UPDATE THE CURSOR
////            view.updateCursor(editMode);
//
//            // IF WE'RE ADDING A ROAD, THEN NOTHING SHOULD BE SELECTED 
////            if (editMode == PXLE_EditMode.ADDING_ROAD_START)
//            {
//                selectedIntersection = null;
//                selectedRoad = null;            
//            }
//            
//            // RENDER
////            view.getCanvas().repaint();
//        }
//    }

    /**
     * Adds an intersection to the graph
     */
    public void addIntersection(Intersection intToAdd)
    {
        ArrayList<Intersection> intersections = level.getIntersections();
        intersections.add(intToAdd);
//        view.getCanvas().repaint();
    }

    /**
     * Calculates and returns the distance between two points.
     */
    public double calculateDistanceBetweenPoints(int x1, int y1, int x2, int y2)
    {
        double diffXSquared = Math.pow(x1 - x2, 2);
        double diffYSquared = Math.pow(y1 - y2, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }

    /**
     * Moves the selected intersection to (canvasX, canvasY),
     * translating it into level coordinates.
     */
    public void moveSelectedIntersection(int canvasX, int canvasY)
    {
        selectedIntersection.x = canvasX + viewport.getViewportX();
        selectedIntersection.y = canvasY + viewport.getViewportY();
    }

    /**
     * Searches the level graph and finds and returns the intersection
     * that overlaps (canvasX, canvasY).
     */
    public Intersection findIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // CHECK TO SEE IF THE USER IS SELECTING AN INTERSECTION
        for (Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x + GAME_OFFSET, i.y, canvasX + viewport.getViewportX(), canvasY + viewport.getViewportY());
            if (distance < INTERSECTION_RADIUS)
            {
                // MAKE THIS THE SELECTED INTERSECTION
                return i;
            }
        }
        return null;
    }

    /**
     * Deletes the selected item from the graph, which might be either
     * an intersection or a road.
     */
    public void deleteSelectedItem()
    {
        // DELETE THE SELECTED INTERSECTION, BUT MAKE SURE IT'S 
        // NOT THE STARTING LOCATION OR DESTINATION
        if ((selectedIntersection != null)
                && (selectedIntersection != level.startingLocation)
                && (selectedIntersection != level.destination))
        {
            // REMOVE ALL THE ROADS THE INTERSECTION IS CONNECTED TO
            ArrayList<Road> roadsMarkedForDeletion = new ArrayList();
            for (Road r : level.roads)
            {
                if ((r.node1 == selectedIntersection)
                        || (r.node2 == selectedIntersection))
                    roadsMarkedForDeletion.add(r);
            }
            
            // NOW REMOVE ALL THE ROADS MARKED FOR DELETION
            for (Road r : roadsMarkedForDeletion)
            {
                level.roads.remove(r);
            }
            
            // THEN REMOVE THE INTERSECTION ITSELF
            level.intersections.remove(selectedIntersection);
            
            // AND FINALLY NOTHING IS SELECTED ANYMORE
            selectedIntersection = null;
            //switchEditMode(PXLE_EditMode.NOTHING_SELECTED);            
        }
        // THE SELECTED ITEM MIGHT BE A ROAD
        else if (selectedRoad != null)
        {
            // JUST REMOVE THE NODE, BUT NOT ANY OF THE INTERSECTIONS
            level.roads.remove(selectedRoad);
            selectedRoad = null;
            //switchEditMode(PXLE_EditMode.NOTHING_SELECTED);
        }
    }
    
    /**
     * Unselects any intersection or road that might be selected.
     */
    public void unselectEverything()
    {
        selectedIntersection = null;
        selectedRoad = null;
        startRoadIntersection = null;
//        view.getCanvas().repaint();
    }

    /**
     * Searches to see if there is a road at (canvasX, canvasY), and if
     * there is, it selects and returns it.
     */
    public Road selectRoadAtCanvasLocation(int canvasX, int canvasY)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(canvasX+viewport.getViewportX(), canvasY+viewport.getViewportY());
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
                this.selectedRoad = r;
                //this.switchEditMode(PXLE_EditMode.ROAD_SELECTED);
                return selectedRoad;
            }
        }
        return null;
    }

    /**
     * Checks to see if (canvasX, canvasY) is free (i.e. there isn't
     * already an intersection there, and if not, adds one.
     */
    public void addIntersectionAtCanvasLocation(int canvasX, int canvasY)
    {
        // FIRST MAKE SURE THE ENTIRE INTERSECTION IS INSIDE THE LEVEL
        if ((canvasX - INTERSECTION_RADIUS) < 0) return;
        if ((canvasY - INTERSECTION_RADIUS) < 0) return;
        if ((canvasX + INTERSECTION_RADIUS) > viewport.getViewportWidth()) return;
        if ((canvasY + INTERSECTION_RADIUS) > viewport.getViewportHeight()) return;
        
        // AND ONLY ADD THE INTERSECTION IF IT DOESN'T OVERLAP WITH
        // AN EXISTING INTERSECTION
        for(Intersection i : level.intersections)
        {
            double distance = calculateDistanceBetweenPoints(i.x-viewport.getViewportX(), i.y-viewport.getViewportY(), canvasX, canvasY);
            if (distance < INTERSECTION_RADIUS)
                return;
        }          
        
        // LET'S ADD A NEW INTERSECTION
        int intX = canvasX + viewport.getViewportX();
        int intY = canvasY + viewport.getViewportY();
        Intersection newInt = new Intersection(intX, intY);
        level.intersections.add(newInt);
    }
    
    /**
     * Retrieves the money, police, bandits, and zombies stats from
     * the view and uses it to refresh the level values.
     */
    public void refreshLevelStats()
    {
//        if (!view.isRefreshingSpinners())
        {
            // GET THE DATA FROM THE VIEW
//            int money = view.getCurrentMoney();
//            int numPolice = view.getCurrentPolice();
//            int numBandits = view.getCurrentBandits();
//            int numZombies = view.getCurrentZombies();
//            
//            // AND USE IT TO UPDATE THE LEVEL
//            level.setMoney(money);
//            level.setNumPolice(numPolice);
//            level.setNumBandits(numBandits);
//            level.setNumZombies(numZombies);
        }
    }

    /**
     * Increases the speed limit on the selected road.
     */
    public void increaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit < MAX_SPEED_LIMIT)
            {
                speedLimit += SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                //view.getCanvas().repaint();
            }
        }
    }

    /**
     * Decreases the speed limit on the selected road.
     */
    public void decreaseSelectedRoadSpeedLimit()
    {
        if (selectedRoad != null)
        {
            int speedLimit = selectedRoad.getSpeedLimit();
            if (speedLimit > MIN_SPEED_LIMIT)
            {
                speedLimit -= SPEED_LIMIT_STEP;
                selectedRoad.setSpeedLimit(speedLimit);
                //view.getCanvas().repaint();
            }
        }
    }    

    /**
     * Toggles the selected road, making it one way if it's currently
     * two-way, and two-way if it's currently one way.
     */
    public void toggleSelectedRoadOneWay()
    {
        if (selectedRoad != null)
        {
            selectedRoad.setOneWay(!selectedRoad.isOneWay());
            //view.getCanvas().repaint();
        }
    }
    
    /**
     * 
     * @param selectedIntersection 
     */
    public void findShortestPathToIntersection(Intersection selectedIntersection)
    {
        pathXTile player = game.getGUICharacters().get(PLAYER_TYPE);
        int x = selectedIntersection.x + GAME_OFFSET;
        int y = selectedIntersection.y;
        player.setTarget(x, y);
        player.startMovingToTarget(20);
        ArrayList<Road> roadsVisited = new ArrayList<Road>();
        ArrayList<Intersection> intersectionsVisited = new ArrayList<Intersection>();
        ArrayList<Intersection> currentIntersection = new ArrayList<Intersection>();
        
        Road roadTouchingSelectedIntersection = findRoadTouchingIntersection(x, y);
        Road currentRoad = findRoadAtTileLocation(player);
    }
    
    public Road findRoadTouchingIntersection(int x, int y)
    {
        for (int i = 0; i < level.roads.size(); i++)
        {
            if (x == level.roads.get(i).node1.x &&
                y == level.roads.get(i).node1.y)
            {
                return level.roads.get(i);
            }
        }
        return null;
    }
    
    public Road findRoadAtTileLocation(pathXTile tile)
    {
        for (Road r : level.roads)
        {
            
        }
        return null;
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
        Intersection i = findIntersectionAtCanvasLocation(x, y);
        if (i != null)
        {
            findShortestPathToIntersection(i);
        }
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
        try
        {
            game.beginUsingData();
            
            pathXTile player = ((pathXMiniGame)game).getGUICharacters().get(PLAYER_TYPE);
            player.update(game);
        } finally
        {
            game.endUsingData();
        }
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
