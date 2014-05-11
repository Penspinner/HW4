package pathX.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
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
    
    ArrayList<String> levelFiles;
    ArrayList<String> levelDescriptions;
    ArrayList<String> levelNames;
    ArrayList<String> levelStates;
    ArrayList movingTiles;
    ArrayList<pathXTile> specialTiles;
    ArrayList<pathXTile> zombies;
    ArrayList<pathXTile> police;
    ArrayList<pathXTile> bandits;
    
    pathXTile player;
    
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
    float gameSpeed;
    int currentLevelCounter;
    int balance;
    int goal;
    
    public pathXDataModel(pathXMiniGame initGame)
    {
        game = initGame;
        level = new pathXLevel();
        levelFiles = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        levelDescriptions = props.getPropertyOptionsList(props);
        levelNames = props.getPropertyOptionsList(pathXPropertyType.LEVEL_NAME_OPTIONS);
        levelStates = new ArrayList();
        movingTiles = new ArrayList<pathXTile>();
        specialTiles = new ArrayList();
        
        gameSpeed = 1;
        currentLevelCounter = 0;
        
        initLevelStates();
        initCharacterImages();
    }
    
    // ACCESSOR METHODS
    public pathXLevel           getLevel()                  {   return level;                   }
    public pathXTile            getPlayer()                 {   return player;                  }
    public float                  getGameSpeed()            {   return gameSpeed;               }
    public int                  getCurrentLevelCounter()    {   return currentLevelCounter;     }
    public int                  getBalance()                {   return balance;                 }
    public ArrayList<String>    getLevelFiles()             {   return levelFiles;              }
    public ArrayList<String>    getLevelNames()             {   return levelNames;              }
    public ArrayList<String>    getLevelStates()            {   return levelStates;             }
    public ArrayList<pathXTile> getMovingTiles()            {   return movingTiles;             }
    public ArrayList<pathXTile> getSpecialTiles()           {   return specialTiles;            }
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
//        pathXTile player = game.getGUICharacters().get(PLAYER_TYPE);
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        SpriteType sT = new SpriteType(PLAYER_TYPE);
        BufferedImage img = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_PLAYER));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        player = new pathXTile(sT, 0, 0, 0, 0, pathXTileState.VISIBLE_STATE.toString(), "PLAYER");
        int location = (startingLocationImage.getHeight(null) - (int) player.getAABBheight()) / 2;
        int w = startingLocationImage.getWidth(null);
        int h = startingLocationImage.getHeight(null);
        int x = level.getStartingLocation().x-(w/2) + startingLocationImage.getWidth(null);
        int y = level.getStartingLocation().y-(h/2) + location;
        player.setCurrentIntersection(level.startingLocation);
        player.setX(x);
        player.setY(y);
    }
    
    public void updateGameSpeed(float newGameSpeed)
    {
        gameSpeed = newGameSpeed;
    }
    
//    public void initZombieLocation()
//    {
//        pathXTile zombie = game.getGUICharacters().get(ZOMBIE_TYPE);
//        zombies = new ArrayList(level.numZombies);
//        for (int i = 0; i < level.numZombies; i++)
//        {
//            zombie.setX(level.getIntersections().get(i+2).x + GAME_OFFSET);
//            zombie.setY(level.getIntersections().get(i+2).y);
//            zombie.setCurrentIntersection(level.getIntersections().get(i+2));
//            zombies.add(zombie);
//        }
//        
//        pathXTile policee = game.getGUICharacters().get(POLICE_TYPE);
//        police = new ArrayList(level.numPolice);
//        for (int i = 0; i < level.numPolice; i++)
//        {
//            policee.setX(level.getIntersections().get(i+2).x + GAME_OFFSET);
//            policee.setY(level.getIntersections().get(i+2).y);
//            police.add(policee);
//        }
//        
//        pathXTile bandit = game.getGUICharacters().get(BANDIT_TYPE);
//        bandits = new ArrayList(level.numBandits);
//        for (int i = 0; i < level.numBandits; i++)
//        {
//            bandit.setX(level.getIntersections().get(i+2).x + GAME_OFFSET);
//            bandit.setY(level.getIntersections().get(i+2).y);
//            bandits.add(bandit);
//        }
//    }
    
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
    public Iterator zombiesIterator()
    {
        ArrayList<Zombie> zombies = level.zombies;
        return zombies.iterator();
    }
    public Iterator policesIterator()
    {
        ArrayList<Police> polices = level.polices;
        return polices.iterator();
    }
    public Iterator banditsIterator()
    {
        ArrayList<Bandit> bandits = level.bandits;
        return bandits.iterator();
    }
    
    public void initLevelStates()
    {
        levelStates.add(pathXTileState.UNSUCCESSFUL_STATE.toString());
        
        // ALL LEVEL BUTTON STATES SHOULD BE LOCKED UNTIL THE PREVIOUS LEVEL
        // HAS BEEN SUCCESSFULLY ROBBED
        for (int i = 1; i < 20; i++)
        {
            // CHANGE BACK LATER ON TO LOCKED
            levelStates.add(pathXTileState.UNSUCCESSFUL_STATE.toString());
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
            double distance = calculateDistanceBetweenPoints(i.x, i.y, canvasX + viewport.getViewportX(), canvasY + viewport.getViewportY());
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
    
    public float calculateDistance(float x1, float y1, float x2, float y2)
    {   
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = x2 - x1;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = y2 - y1;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
    }
    
    /**
     * 
     * @param selectedIntersection 
     */
    public ArrayList<Intersection> findShortestPathToIntersection(Intersection selectedIntersection)
    {
        int selectedX = selectedIntersection.x;
        int selectedY = selectedIntersection.y;
        
        // VISITED AND NOT VISITED LIST FOR THE ALGORITHM
        ArrayList<Intersection> visitedNodes = new ArrayList();
        ArrayList<Intersection> notVisitedNodes = new ArrayList();
        
        Intersection currentIntersection = player.getCurrentIntersection();
        
        // INITIALIZE THE WEIGHT OF THE CURRENT INTERSECTION TO 0
        currentIntersection.setWeight(0);
        
        // KEEPS TRACK OF THE TOTAL DISTANCE TRAVELED SO FAR
        double totalPathWeight = 0;
        
        // ADD ALL THE INTERSECTIONS TO THE NOT VISITED LIST
        Iterator<Intersection> it = intersectionsIterator();
        while (it.hasNext())
        {
            Intersection i = it.next();
            notVisitedNodes.add(i);
        }
        
        // ADD THE CURRENT NODE TO THE VISITED LIST
        visitedNodes.add(currentIntersection);
        
        while (currentIntersection != selectedIntersection)
        {    
            // GET THE NEIGHBORS OF THE CURRENT INTERSECTION
            ArrayList<Intersection> neighbors = getNeighbors(currentIntersection);
            Iterator<Intersection> itN = neighbors.iterator();
            
            // REMOVE THE INTERSECTIONS THAT HAVE ALREADY BEEN VISITED
            for (Intersection i : neighbors)
            {
                if (visitedNodes.contains(i))
                {
                    neighbors.remove(i);
                }
            }
            
            // CALCULATE DISTANCE TO ALL NEIGHBORS
            for (Intersection i : neighbors)
            {
                // CALCULATE DISTANCE TO NEIGHBOR
                double distance = calculateDistanceBetweenPoints(currentIntersection.x, currentIntersection.y, i.x, i.y);
                double total = distance + totalPathWeight;
                
                // SET THE WEIGHT 
                if (total < i.weight)
                {
                    i.setWeight(total);
                }
            }
            
            Intersection closestIntersection = getClosestNeighbor(neighbors, currentIntersection);
            
            currentIntersection = closestIntersection;
            visitedNodes.add(currentIntersection);
        }
        
        
        ArrayList<Intersection> path = new ArrayList();
        Road startRoad = findRoadAtSpriteLocation(player);
//        if (currentIntersection == level.startingLocation)
//        {
//            return new ArrayList();
//        } else
//        {
//            ArrayList<Road> roadsVisited = new ArrayList<Road>();
//            ArrayList<Intersection> intersectionsVisited = new ArrayList<Intersection>();
//            intersectionsVisited.add(currentIntersection);
//
//            // LIST TO STORE THE SHORTEST PATH TO THE SELECTED INTERSECTION
//            
//
//            // SAVES ALL NODE1 AS KEY AND NODE2 AS VALUE IN A HASHMAP
//            HashMap<Intersection, Intersection> intersections = new HashMap();
//            for (Road r : level.roads)
//            {
//                Intersection i1 = r.node1;
//                Intersection i2 = r.node2;
//                intersections.put(i1, i2);
//            }
//
//            while (!found)
//            {
//                ArrayList<Intersection> neighbors = getNeighbors(currentIntersection);
//                if (neighbors.size() == 1)
//                {
//                    currentIntersection = neighbors.get(0);
//                    player.setCurrentIntersection(currentIntersection);
//                    path.add(neighbors.get(0));
//                    if (neighbors.get(0).x == selectedX && neighbors.get(0).y == selectedY)
//                        return path;
//                }
//                else
//                {
//                    for (Intersection i : neighbors)
//                    {
//                        if (i.x == selectedX && i.y == selectedY &&
//                            !intersectionsVisited.contains(i))
//                        {
//                            path.add(i);
//                            return path;
//                        }
//                    }
//                    currentIntersection = getClosestNeighbor(neighbors, currentIntersection);
//                    path.add(currentIntersection);
//                    intersectionsVisited.add(currentIntersection);
//                }
//            }
//            return path;
//        }
        return path;
    }
    
    public Intersection getClosestIntersection(Road currentRoad, pathXTile player)
    {
        int x1 = currentRoad.node1.x;
        int y1 = currentRoad.node1.y;
        int x2 = currentRoad.node2.x;
        int y2 = currentRoad.node2.y;
        int px = (int) player.getX();
        int py = (int) player.getY();
        double distanceNode1 = calculateDistanceBetweenPoints(px, py, x1, y1);
        double distanceNode2 = calculateDistanceBetweenPoints(px, py, x2, y2);
        
        if (distanceNode1 < distanceNode2)
            return currentRoad.node1;
        else
            return currentRoad.node2;
    }
    
    public ArrayList<Intersection> getNeighbors(Intersection intersection)
    {
        ArrayList<Intersection> neighbors = new ArrayList();
        ArrayList<Road> roads = level.roads;
        int x = intersection.x;
        int y = intersection.y;
        
        for (int i = 0; i < roads.size(); i++)
        {
            if (roads.get(i).node1.x == x && roads.get(i).node1.y == y)
            {
                neighbors.add(roads.get(i).node2);
            } else if (roads.get(i).node2.x == x && roads.get(i).node2.y == y)
            {
                neighbors.add(roads.get(i).node1);
            }
        }
        return neighbors;
    }
    
    public Intersection getClosestNeighbor(ArrayList<Intersection> intersections, Intersection ci)
    {
        Intersection closestIntersection = intersections.get(0);
        if (intersections.size() > 1)
        {
            for (int i = 1; i < intersections.size(); i++)
            {
                Intersection intersection = intersections.get(i);
//                if (calculateDistanceBetweenPoints(ci.x, ci.y, closestIntersection.x, closestIntersection.y) >
//                    calculateDistanceBetweenPoints(ci.x, ci.y, intersection.x, intersection.y))
                if (intersection.weight < closestIntersection.weight)
                {
                    closestIntersection = intersection;
                }
            }
        }
        return closestIntersection;
    }
    
    public ArrayList<Road> getNeighborRoads(Intersection intersection)
    {
        ArrayList<Road> neighborRoads = new ArrayList();
        ArrayList<Road> roads = level.roads;
        int x = intersection.x;
        int y = intersection.y;
        
        for (int i = 0; i < roads.size(); i++)
        {
            // IF EITHER NODE 1 OR NODE 2 OF ROAD IS A NEIGHBOR
            if ((roads.get(i).node1.x == x &&
                roads.get(i).node1.y == y) ||
                (roads.get(i).node2.x == x &&
                roads.get(i).node2.y == y))
            {
                neighborRoads.add(roads.get(i));
            }
        }
        return neighborRoads;
    }
    
    public Road getShortestRoad(ArrayList<Road> roads)
    {
        Road shortestRoad = roads.get(0);
        for (Road r : roads)
        {
            if (shortestRoad.distance > r.distance)
            {
                shortestRoad = r;
            }
        }
        return shortestRoad;
    }
    
    public Road getRoad(Intersection i1, Intersection i2)
    {
        for (Road r : level.roads)
        {
            if (((r.node1.x == i1.x && r.node1.y == i1.y) &&
                (r.node2.x == i2.x && r.node2.y == i2.y)) ||
                ((r.node1.x == i2.x && r.node1.y == i2.y) &&
                (r.node2.x == i1.x && r.node2.y == i1.y)))
            {
                return r;
            }
        }
        return null;
    }
    
    public Road findRoadAtSpriteLocation(Sprite s)
    {
        Iterator<Road> it = level.roads.iterator();
        Line2D.Double tempLine = new Line2D.Double();
        double centerX = s.getX() + 17.5;
        double centerY = s.getY() + 17.5;
        while (it.hasNext())
        {
            Road r = it.next();
            tempLine.x1 = r.node1.x;
            tempLine.y1 = r.node1.y;
            tempLine.x2 = r.node2.x;
            tempLine.y2 = r.node2.y;
            double distance = tempLine.ptSegDist(centerX+viewport.getViewportX(), centerY+viewport.getViewportY());
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                // SELECT IT
//                this.selectedRoad = r;
                //this.switchEditMode(PXLE_EditMode.ROAD_SELECTED);
                return r;
            }
        }
        return null;
    }
    
    public void generatePolicePath(Police p)
    {
        // FIND NEIGHBORS, CHOOSE RANDOM NEIGHBOR, GO THERE, REPEAT
        
        ArrayList<Intersection> path = new ArrayList();
        int x = (int) p.getX() + INTERSECTION_RADIUS;
        int y = (int) p.getY() + INTERSECTION_RADIUS;
        
        Intersection currentIntersection = findIntersectionAtCanvasLocation(x, y);
        p.initCurrentIntersection(currentIntersection);
        
        ArrayList<Intersection> neighbors = getNeighbors(currentIntersection);
        if (neighbors.contains(level.startingLocation))
        {
            neighbors.remove(level.startingLocation);
        }
        
        int random = (int) (Math.random() * neighbors.size());
        Intersection nextIntersection = neighbors.get(random);
        p.setNextIntersection(nextIntersection);
        Road roadInBetween = getRoad(currentIntersection, nextIntersection);
        if (roadInBetween != null)
        {
            p.setTarget(nextIntersection.x , nextIntersection.y );
            p.startMovingToTarget(roadInBetween.speedLimit / 10);
        }
    }
    
    public void generateZombiePath(Zombie z)
    {
        int x = (int) z.getX();
        int y = (int) z.getY();
        Intersection currentIntersection = findIntersectionAtCanvasLocation(x, y);
    }
    
    public void checkForCollisions()
    {
        Iterator<Zombie> itZ = zombiesIterator();
        while (itZ.hasNext())
        {
            Zombie z = itZ.next();
            if (player.containsPoint(z.getX(), z.getY()))
            {
                
            }
        }
        
        Iterator<Police> itP = policesIterator();
        while (itP.hasNext())
        {
            Police p = itP.next();
            if (player.containsPoint(p.getX(), p.getY()))
            {
                endGameAsLoss();
            }
        }
        
        Iterator<Bandit> itB = banditsIterator();
        while (itB.hasNext())
        {
            Bandit b = itB.next();
            if (player.containsPoint(b.getX(), b.getY()))
            {
                
            }
        }
    }
    
    @Override
    public void endGameAsWin()
    {
        super.endGameAsWin();
        
        balance += level.money;
        
    }
    
    @Override
    public void endGameAsLoss()
    {
        super.endGameAsLoss();
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
        Viewport gameViewport = this.game.getGameViewport();
        if (x > 200)
        {
            Intersection i = findIntersectionAtCanvasLocation(x+gameViewport.getViewportX(), y+gameViewport.getViewportY());
            if (i != null)
            {
                ArrayList<Intersection> shortestPath = findShortestPathToIntersection(i);
                player.resetIndex();
                player.initPath(shortestPath);
            }
        } else
        {
//            for (pathXTile special : ((pathXMiniGame)game).getSpecials().getSpecialTiles())
//            {
//                if (special.containsPoint(x, y))
//                {
//                    String actionCommand = special.getActionCommand();
//                    ((pathXMiniGame)game).getSpecialsHandler().useSpecial(actionCommand);
//                }
//            }
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
            
//            pathXTile player = ((pathXMiniGame)game).getGUICharacters().get(PLAYER_TYPE);
            player.update(game);
            
            for (Police p : level.getPolices())
            {
                if (!p.isMovingToTarget())
                    generatePolicePath(p);
                p.update(game);
            }
            
            checkForCollisions();
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
