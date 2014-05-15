package pathX.data;

import java.util.ArrayList;
import mini_game.SpriteType;
import pathX.ui.pathXTileState;
import pathX.ui.pathXTile;

/**
 *
 * @author Dell
 */
public class pathXLevel
{
    // EVERY LEVEL HAS A NAME
    String levelName;
    
    String name;
    
    String description;
    
    String state;

    // THE LEVEL BACKGROUND
    String startingLocationImageFileName;

    // COMPLETE LIST OF INTERSECTIONS SORTED LEFT TO RIGHT
    ArrayList<Intersection> intersections;

    // COMPLETE LIST OF ROADS SORTED BY STARTING INTERSECTION LOCATION LEFT TO RIGHT
    ArrayList<Road> roads;
    
    // COMPLETE LIST OF ZOMBIES, POLICES, AND BANDITS
    ArrayList<Zombie> zombies;
    ArrayList<Police> polices;
    ArrayList<Bandit> bandits;

    // THE STARTING LOCATION AND DESTINATION
    Intersection startingLocation;
    String backgroundImageFileName;
    Intersection destination;
    String destinationImageFileName;

    // THE AMOUNT OF MONEY TO BE EARNED BY THE LEVEL
    int money;

    // THE NUMBER OF POLICE, BANDITS, AND ZOMBIES
    int numPolice;
    int numBandits;
    int numZombies;
    
    int levelNumber;

    /**
     * Default constructor, it just constructs the graph data structures
     * but does not fill in any data.
     */
    public pathXLevel()
    {
        // INIT THE GRAPH DATA STRUCTURES
        intersections = new ArrayList();
        roads = new ArrayList();
        zombies = new ArrayList();
        polices = new ArrayList();
        bandits = new ArrayList();
    }

    /**
     * Initializes this level to get it up and running.
     */
    public void init (  String initLevelName,
                        String initBackgroundImageFileName,
                        String initStartingLocationImageFileName,
                        int startingLocationX, 
                        int startingLocationY,
                        String initDestinationImageFileName,
                        int destinationX, 
                        int destinationY)
    {
        // THESE THINGS ARE KNOWN
        levelName = initLevelName;
        state = pathXTileState.UNSUCCESSFUL_STATE.toString();
        backgroundImageFileName = initBackgroundImageFileName;
        startingLocationImageFileName = initStartingLocationImageFileName;
        destinationImageFileName = initDestinationImageFileName;
        
        // AND THE STARTING LOCATION AND DESTINATION
        startingLocation = new Intersection(startingLocationX, startingLocationY);
        intersections.add(startingLocation);
        destination = new Intersection(destinationX, destinationY);
        intersections.add(destination);
        
        // THESE THINGS WILL BE PROVIDED DURING LEVEL EDITING
        money = 0;
        numPolice = 0;
        numBandits = 0;
        numZombies = 0;
    }
    
    // ACCESSOR METHODS
    public String                   getLevelName()                      {   return levelName;                       }
    public String                   getName()                           {   return name;                            }
    public String                   getDescription()                    {   return description;                     }
    public String                   getStartingLocationImageFileName()  {   return startingLocationImageFileName;   }
    public String                   getBackgroundImageFileName()        {   return backgroundImageFileName;         }
    public String                   getDestinationImageFileName()       {   return destinationImageFileName;        }
    public ArrayList<Intersection>  getIntersections()                  {   return intersections;                   }
    public ArrayList<Road>          getRoads()                          {   return roads;                           }
    public ArrayList<Zombie>        getZombies()                        {   return zombies;                         }
    public ArrayList<Police>        getPolices()                        {   return polices;                         }
    public ArrayList<Bandit>        getBandits()                        {   return bandits;                         }
    public Intersection             getStartingLocation()               {   return startingLocation;                }
    public Intersection             getDestination()                    {   return destination;                     }
    public int                      getMoney()                          {   return money;                           }
    public int                      getNumPolice()                      {   return numPolice;                       }
    public int                      getNumBandits()                     {   return numBandits;                      }
    public int                      getNumZombies()                     {   return numZombies;                      }
    public int                      getLevelNumber()                    {   return levelNumber;                     }
    
    // MUTATOR METHODS
    public void setLevelName(String levelName)
    {   this.levelName = levelName;                                             }
    public void setName(String name)
    {   this.name = name;                                                       }
    public void setDescription(String description)
    {   this.description = description;                                         }
    public void setBackgroundImageFileName(String backgroundImageFileName)    
    {   this.backgroundImageFileName = backgroundImageFileName;                 }
    public void setStartingLocationImageFileName(String startingLocationImageFileName)    
    {   this.startingLocationImageFileName = startingLocationImageFileName;     }
    public void setDestinationImageFileName(String destinationImageFileName)    
    {   this.destinationImageFileName = destinationImageFileName;               }
    public void setMoney(int money)    
    {   this.money = money;                                                     }
    public void setNumPolice(int numPolice)    
    {   this.numPolice = numPolice;                                             }
    public void setNumZombies(int numZombies)
    {   this.numZombies = numZombies;                                           }
    public void setNumBandits(int numBandits)
    {   this.numBandits = numBandits;                                           }
    public void setLevelNumber(int levelNumber)
    {   this.levelNumber = levelNumber;                                         }
    public void setStartingLocation(Intersection startingLocation)
    {   this.startingLocation = startingLocation;                               }
    public void setDestination(Intersection destination)
    {   this.destination = destination;                                         }
    
    /**
     * Clears the level graph and resets all level data.
     */
    public void reset()
    {
        levelName = "";
        startingLocationImageFileName = "";
        intersections.clear();
        roads.clear();
        zombies.clear();
        polices.clear();
        bandits.clear();
        startingLocation = null;
        backgroundImageFileName = "";
        destination = null;
        destinationImageFileName = "";
        money = 0;
        numPolice = 0;
        numBandits = 0;
        numZombies = 0;
    }
}