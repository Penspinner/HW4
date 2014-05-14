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
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathX.pathX.pathXPropertyType;
import pathX.ui.pathXMiniGame;
import static pathX.pathXConstants.*;
import pathX.ui.pathXSpecials;
import pathX.ui.pathXTile;
import pathX.ui.pathXTileState;
import properties_manager.PropertiesManager;
import pathX.ui.pathXSpecials.pathXSpecialsMode;
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
    
    pathXSpecialsMode mode;
    
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
    float playerSpeed;
    int currentLevelCounter;
    int balance;
    
    String currentLevel;
    
    public pathXDataModel(pathXMiniGame initGame)
    {
        game = initGame;
        level = new pathXLevel();
        levelFiles = props.getPropertyOptionsList(pathXPropertyType.LEVEL_OPTIONS);
        levelDescriptions = props.getPropertyOptionsList(pathXPropertyType.LEVEL_DESCRIPTIONS);
        levelNames = props.getPropertyOptionsList(pathXPropertyType.LEVEL_NAME_OPTIONS);
        levelStates = new ArrayList();
        movingTiles = new ArrayList<pathXTile>();
        specialTiles = new ArrayList();
        
        mode = pathXSpecialsMode.NORMAL_MODE;
        
        gameSpeed = 1;
        playerSpeed = 1;
        currentLevelCounter = 0;
        
        initLevelStates();
        initCharacterImages();
    }
    
    // ACCESSOR METHODS
    public pathXLevel           getLevel()                  {   return level;                   }
    public pathXTile            getPlayer()                 {   return player;                  }
    public float                getGameSpeed()              {   return gameSpeed;               }
    public float                getPlayerSpeed()            {   return playerSpeed;             }
    public int                  getCurrentLevelCounter()    {   return currentLevelCounter;     }
    public int                  getBalance()                {   return balance;                 }
    public String               getCurrentLevel()           {   return currentLevel;            }
    public ArrayList<String>    getLevelFiles()             {   return levelFiles;              }
    public ArrayList<String>    getLevelNames()             {   return levelNames;              }
    public ArrayList<String>    getLevelDescriptions()      {   return levelDescriptions;       }
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
    
    // THESE ARE FOR TESTING WHICH SPECIAL MODE THE APP CURRENTLY IS IN
    public boolean isMakeLightGreenMode()       {   return mode == pathXSpecialsMode.MAKE_LIGHT_GREEN_MODE;     }
    public boolean isMakeLightRedMode()         {   return mode == pathXSpecialsMode.MAKE_LIGHT_RED_MODE;       }
    public boolean isFlatTireMode()             {   return mode == pathXSpecialsMode.FLAT_TIRE_MODE;            }
    public boolean isEmptyGasTankMode()         {   return mode == pathXSpecialsMode.EMPTY_GAS_TANK_MODE;       }
    public boolean isDecreaseSpeedLimitMode()   {   return mode == pathXSpecialsMode.DECREASE_SPEED_LIMIT_MODE; }
    public boolean isIncreaseSpeedLimitMode()   {   return mode == pathXSpecialsMode.INCREASE_SPEED_LIMIT_MODE; }
    public boolean isIncreasePlayerSpeedMode()  {   return mode == pathXSpecialsMode.INCREASE_PLAYER_SPEED_MODE;}
    public boolean isCloseRoadMode()            {   return mode == pathXSpecialsMode.CLOSE_ROAD_MODE;           }
    public boolean isCloseIntersectionMode()    {   return mode == pathXSpecialsMode.CLOSE_INTERSECTION_MODE;   }
    public boolean isOpenIntersectionMode()     {   return mode == pathXSpecialsMode.OPEN_INTERSECTION_MODE;    }
    public boolean isStealMode()                {   return mode == pathXSpecialsMode.STEAL_MODE;                }
    public boolean isMindControlMode()          {   return mode == pathXSpecialsMode.MIND_CONTROL_MODE;         }
    public boolean isIntangibilityMode()        {   return mode == pathXSpecialsMode.INTANGIBILITY_MODE;        }
    public boolean isMindlessTerrorMode()       {   return mode == pathXSpecialsMode.MINDLESS_TERROR_MODE;      }
    public boolean isFlyingMode()               {   return mode == pathXSpecialsMode.FLYING_MODE;               }
    public boolean isInvincibilityMode()        {   return mode == pathXSpecialsMode.INVINCIBILITY_MODE;        }
    public boolean isNormalMode()               {   return mode == pathXSpecialsMode.NORMAL_MODE;               }
    
    // MUTATOR METHODS
    public void setCurrentLevel(String initCurrentLevel)
    {   currentLevel = initCurrentLevel;                    }
    
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
        String imgPath = props.getProperty(pathXPropertyType.PATH_IMG);
        SpriteType sT = new SpriteType(PLAYER_TYPE);
        BufferedImage img = game.loadImage(imgPath + props.getProperty(pathXPropertyType.IMAGE_PLAYER));
        sT.addState(pathXTileState.VISIBLE_STATE.toString(), img);
        
        int location = (startingLocationImage.getHeight(null) - img.getHeight()) / 2;
        int w = startingLocationImage.getWidth(null);
        int h = startingLocationImage.getHeight(null);
        int x = level.getStartingLocation().x-(w/2) + startingLocationImage.getWidth(null);
        int y = level.getStartingLocation().y-(h/2) + location;
        
        player = new pathXTile(sT, x, y, 0, 0, pathXTileState.VISIBLE_STATE.toString(), "PLAYER");
        player.setCurrentIntersection(level.startingLocation);
    }
    
    public void switchMode(pathXSpecialsMode initMode)
    {
        mode = initMode;
    }
    
    public void updateGameSpeed(float newGameSpeed)
    {
        gameSpeed = newGameSpeed;
    }
    
    public void increaseBalance(int extraMoney)
    {
        if (inProgress())
        {
            balance += extraMoney;
            game.getAudio().play(pathXPropertyType.AUDIO_CUE_INCREASE_MONEY.toString(), false);
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
        // UPDATE THE LEVEL TO FIT THE BACKGROUND IMAGE SIZE
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
            if (distance < INTERSECTION_RADIUS + 5)
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
        Viewport viewport = game.getGameViewport();
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
    
    public void toggleIntersectionColor(int x, int y, boolean b)
    {
        Intersection i = findIntersectionAtCanvasLocation(x, y);
        if (i != null)
        {
            i.setOpen(b);
        }
    }
    
    public void increaseRoadSpeedLimit(Road r)
    {
        double newSpeedLimit = r.getSpeedLimit() * 1.5;
        r.setSpeedLimit((int) newSpeedLimit);
    }
    
    public void decreaseRoadSpeedLimit(Road r)
    {
        int newSpeedLimit = r.getSpeedLimit() / 2;
        r.setSpeedLimit(newSpeedLimit);
    }
    
    public void stopFor(int x, int y, int time)
    {
        for (Police p : level.getPolices())
        {
            if (p.containsPoint(x, y))
            {
                p.setVx(0);
                p.setVy(0);
            }
        }

        for (Zombie z : level.getZombies())
        {
            if (z.containsPoint(x, y))
            {
                
            }
        }

        for (Bandit b : level.getBandits())
        {
            if (b.containsPoint(x, y))
            {
                
            }
        }
    }
    
    public void toggleIntersection(int x, int y, boolean b)
    {
        Intersection i = findIntersectionAtCanvasLocation(x, y);
        if (i != null)
        {
            i.setOpen(b);
            
            ArrayList<Road> roadsTouchingIntersection = getNeighborRoads(i);
            for (Road r : roadsTouchingIntersection)
            {
                r.setOpen(b);
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
    public ArrayList<Intersection> findShortestPathToIntersection(Intersection currentIntersection, Intersection selectedIntersection)
    {
        // COORDINATES OF THE SELECTED INTERSECTION
        int selectedX = selectedIntersection.x;
        int selectedY = selectedIntersection.y;
        
        // VISITED AND NOT VISITED LIST FOR THE ALGORITHM
        ArrayList<Intersection> visitedNodes = new ArrayList();
        ArrayList<Intersection> notVisitedNodes = new ArrayList();
        
        Intersection startIntersection = currentIntersection;
        
        ArrayList<Intersection> path = new ArrayList();
        
        // A MAP TO LOOK UP THE SHORTEST PATHS FROM EACH INTERSECTION
        // KEY IS THE INTERSECTION
        // VALUE IS THE PREDECESSOR
        HashMap<Intersection, Intersection> shortestPaths = new HashMap();
        
        // RESET ALL THE INTERSECTIONS TO INFINITE WEIGHT EXCEPT FOR CURRENT
        for (Intersection i : level.intersections)
        {
            i.weight = Integer.MAX_VALUE;
        }
        
        // INITIALIZE THE WEIGHT OF THE CURRENT INTERSECTION TO 0
        currentIntersection.setWeight(0);
        
        // KEEPS TRACK OF THE TOTAL DISTANCE TRAVELED SO FAR
        double totalDistance = 0;
        
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
            for (int i = 0; i < neighbors.size(); i++)
            {
                Intersection intersection = neighbors.get(i);
                if (visitedNodes.contains(intersection))
                {
                    neighbors.remove(intersection);
                }
            }
            
            // CALCULATE DISTANCE TO ALL NEIGHBORS
            for (Intersection i : neighbors)
            {
                // CALCULATE DISTANCE TO NEIGHBOR
                double distance = calculateDistanceBetweenPoints(currentIntersection.x, currentIntersection.y, i.x, i.y);
                double total = distance + totalDistance;
                
                // SET THE WEIGHT 
                if (total < i.weight)
                {
                    i.setWeight(total);
                }
                
                // PUT THE SHORTEST PATH IN THE LIST
                if (!shortestPaths.containsKey(i))
                {
                    shortestPaths.put(i, currentIntersection);
                }
            }
            
            // GETS THE CLOSESt NEIGHBOR OF THE CURRENT INTERSECTION
            Intersection nextIntersection;
            if (!neighbors.contains(selectedIntersection))
                nextIntersection = getClosestNeighbor(neighbors, currentIntersection);
            else
                nextIntersection = selectedIntersection;
            
            Intersection temp = shortestPaths.get(nextIntersection);
            if (temp != null)
            {
                if (temp.weight > totalDistance + calculateDistanceBetweenPoints(currentIntersection.x, currentIntersection.y, 
                                                    nextIntersection.x, nextIntersection.y))
                {
                    shortestPaths.remove(temp);
                    shortestPaths.put(temp, currentIntersection);
                }
            }
            
            totalDistance += nextIntersection.weight;
            
            currentIntersection = nextIntersection;
            visitedNodes.add(currentIntersection);
        }
        
        Intersection predecessor = shortestPaths.get(selectedIntersection);
        
        // ADD THE SELECTED INTERSECTION TO THE PATH
        path.add(selectedIntersection);
        
        // INSERT THE INTERSECTIONS TO THE FRONT OF THE ARRAYLIST WHILE IT DOES
        // NOT CONTAIN THE CURRENT INTERSECTION
        while (!path.contains(startIntersection))
        {
            // ADD THE PREDECESSOR TO THE FRONT OF THE ARRAYLIST
            path.add(0, predecessor);
            
            // CHANGE THE PREDECESSOR TO THE NEXT PREDECESSOR
            predecessor = shortestPaths.get(predecessor);
        }
        
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
        intersections.remove(level.startingLocation);
        intersections.remove(level.destination);
        Intersection closestIntersection = intersections.get(0);
        if (intersections.size() > 1)
        {
            for (int i = 1; i < intersections.size(); i++)
            {
                Intersection intersection = intersections.get(i);
                if (calculateDistanceBetweenPoints(ci.x, ci.y, closestIntersection.x, closestIntersection.y) >
                    calculateDistanceBetweenPoints(ci.x, ci.y, intersection.x, intersection.y) &&
                    intersection != level.startingLocation && intersection != level.destination)
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
        
        int x = (int) p.getX();
        int y = (int) p.getY();
        
        Intersection currentIntersection = findIntersectionAtCanvasLocation(x, y);
        p.setCurrentIntersection(currentIntersection);
        
        ArrayList<Intersection> neighbors = getNeighbors(currentIntersection);
        neighbors.remove(level.startingLocation);
        neighbors.remove(level.destination);
        
        int random = (int) (Math.random() * neighbors.size());
        Intersection nextIntersection = neighbors.get(random);
        Road roadInBetween = getRoad(currentIntersection, nextIntersection);
        if (roadInBetween != null)
        {
            p.setTarget(nextIntersection.x, nextIntersection.y);
            p.startMovingToTarget(roadInBetween.speedLimit * gameSpeed * playerSpeed / 10);
        }
    }
    
    public void generateZombiePath()
    {
        for (Zombie z : level.zombies)
        {
            int x = (int) z.getX();
            int y = (int) z.getY();
            
            // PATH TO GO IN LOOP
            ArrayList<Intersection> path = new ArrayList();

            // FIND INTERSECTION AT THE ZOMBIE'S LOCATION
            Intersection currentIntersection = findIntersectionAtCanvasLocation(x, y);
            z.setCurrentIntersection(currentIntersection);
            z.setLoopDestination(currentIntersection);

            // GET NEIGHBORS OF THE CURRENT INTERSECTION
            ArrayList<Intersection> neighbors = getNeighbors(currentIntersection);
            Intersection nextIntersection = neighbors.get(0);
            path.add(nextIntersection);
            
            Intersection afterIntersection = neighbors.get(1);
            ArrayList<Intersection> pathToAdjacentNeighbors = findShortestPathToIntersection(nextIntersection, afterIntersection);
            pathToAdjacentNeighbors.remove(level.startingLocation);
            pathToAdjacentNeighbors.remove(level.destination);
            pathToAdjacentNeighbors.remove(nextIntersection);
            for (Intersection i : pathToAdjacentNeighbors)
            {
                path.add(i);
            }
            
            ArrayList<Intersection> pathFromAfterIntersectionToDestination = findShortestPathToIntersection(afterIntersection, currentIntersection);
            pathFromAfterIntersectionToDestination.remove(level.startingLocation);
            pathFromAfterIntersectionToDestination.remove(level.destination);
            pathFromAfterIntersectionToDestination.remove(afterIntersection);
            for (Intersection i : pathFromAfterIntersectionToDestination)
            {
                path.add(i);
            }

            z.initPath(path, this);
        }
    }
    
    public void generateBanditPath()
    {
        for (Bandit b : level.bandits)
        {
            int x = (int) b.getX();
            int y = (int) b.getY();
                        
            // PATH TO GO IN LOOP
            ArrayList<Intersection> path = new ArrayList();

            // FIND INTERSECTION AT THE ZOMBIE'S LOCATION
            Intersection currentIntersection = findIntersectionAtCanvasLocation(x, y);
            
        }
    }
    
    public void checkForCollisions()
    {
        Iterator<Zombie> itZ = zombiesIterator();
        while (itZ.hasNext())
        {
            Zombie z = itZ.next();
            if (player.containsPoint(z.getX(), z.getY()) && !z.isCollided())
            {
                if (!isStealMode())
                {
                    z.toggleColided();
                    playerSpeed -= .1;
                } else
                {
                    balance += z.getMoney();
                }
            } else
            {
                z.setCollided(false);
            }
        }
        
        Iterator<Police> itP = policesIterator();
        while (itP.hasNext())
        {
            Police p = itP.next();
            if (player.containsPoint(p.getX(), p.getY()) && !p.isCollided())
            {
                if (!isStealMode())
                {
                    endGameAsLoss();
                } else
                {
                    balance += p.getMoney();
                }
            } else
            {
                p.setCollided(false);
            }
        }
        
        Iterator<Bandit> itB = banditsIterator();
        while (itB.hasNext())
        {
            Bandit b = itB.next();
            if (player.containsPoint(b.getX(), b.getY()) && !b.isCollided())
            {
                if (!isStealMode())
                {
                    level.money -= (level.money / 10);
                } else
                {
                    balance += b.getMoney();
                }
            } else
            {
                b.setCollided(false);
            }
        }
    }
    
    @Override
    public void endGameAsWin()
    {
        // PAUSE THE GAME FIRST
        pause();
        game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(false);
        
        // CHANGES THE GAME STATE USING INHERTIED FUNCTIONALITY
        super.endGameAsWin();
        
        // DISPLAYS THE INFO DIALOG BOX
        game.getGUIDecor().get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setEnabled(true);
        game.toggleInfoDisplay();
        
        // INCREASE YOUR BALANCE BY THE AMOUNT YOU ROBBED
        balance += level.money;
    }
    
    @Override
    public void endGameAsLoss()
    {
        // PAUSE THE GAME FIRST
        pause();
        game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(false);
        
        // CHANGES THE GAME STATE USING INHERTIED FUNCTIONALITY
        super.endGameAsLoss();
        
        // DISPLAYS THE INFO DIALOG BOX
        game.getGUIDecor().get(INFO_DIALOG_BOX_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setState(pathXTileState.VISIBLE_STATE.toString());
        game.getGUIButtons().get(LEAVE_BUTTON_TYPE).setEnabled(true);
        game.toggleInfoDisplay();
        
        
        // DECREASES YOUR BALANCE BY THE AMOUNT YOU HAVE TO PAY
        balance -= (balance / 10);
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
        
        // MAKE SURE THE CURSOR IS IN THE VIEWPORT
        if (x > 200)
        {
            if (isNormalMode())
            {
                Intersection i = findIntersectionAtCanvasLocation(x+gameViewport.getViewportX(), y+gameViewport.getViewportY());
                if (i != null)
                {
                    ArrayList<Intersection> shortestPath = findShortestPathToIntersection(player.getCurrentIntersection(), i);
                    player.resetIndex();
                    player.initPath(shortestPath);
                    return;
                }
            } else if (isMakeLightGreenMode())
            {
                toggleIntersectionColor(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), true);
            } else if (isMakeLightRedMode())
            {
                toggleIntersectionColor(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), false);
            } else if (isDecreaseSpeedLimitMode())
            {
                Road r = selectRoadAtCanvasLocation(x+gameViewport.getViewportX(), y+gameViewport.getViewportY());
                if (r != null)
                {
                    decreaseRoadSpeedLimit(r);
                }
            } else if (isDecreaseSpeedLimitMode())
            {
                Road r = selectRoadAtCanvasLocation(x+gameViewport.getViewportX(), y+gameViewport.getViewportY());
                if (r != null)
                {
                    increaseRoadSpeedLimit(r);
                }
            } else if (isIncreasePlayerSpeedMode())
            {
                playerSpeed += .2;
            } else if (isFlatTireMode())
            {
                stopFor(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), 10);
            } else if (isEmptyGasTankMode())
            {
                stopFor(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), 20);
            } else if (isCloseRoadMode())
            {
                Road r = selectRoadAtCanvasLocation(x+gameViewport.getViewportX(), y+gameViewport.getViewportY());
                if (r != null)
                {
                    r.setOpen(false);
                }
            } else if (isCloseIntersectionMode())
            {
                toggleIntersection(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), true);
            } else if (isOpenIntersectionMode())
            {
                toggleIntersection(x+gameViewport.getViewportX(), y+gameViewport.getViewportY(), false);
            } else if (isStealMode())
            {
                
            } else if (isMindControlMode())
            {
                
            } else if (isIntangibilityMode())
            {
                
            } else if (isMindlessTerrorMode())
            {
                
            } else if (isFlyingMode())
            {
                
            } else if (isInvincibilityMode())
            {
                
            }
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
        gameState = MiniGameState.NOT_STARTED;
        unpause();
        level.reset();
        // RESET ALL LEVELS BACK TO UNSUCCESSFUL STATE
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

            // MOVE THE PLAYER ACCORDING TO ITS PATH
            player.update(game);
            if (player.getCurrentIntersection() == level.destination)
            {
                endGameAsWin();
                return;
            }
            
            // WE GENERATE A RANDOM TARGET FOR THE POLICE TO MOVE TO
            for (Police p : level.getPolices())
            {
                if (!p.isMovingToTarget())
                    generatePolicePath(p);
                p.update(game);
            }
            
            for (Zombie z : level.getZombies())
            {
                z.update(game);
            }
            
//            for (Bandit b : level.getBandits())
//            {
//                
//            }
            
//            checkForCollisions();
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
