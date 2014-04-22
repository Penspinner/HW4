package pathX.data;

import java.util.ArrayList;

/**
 *
 * @author Steven Liao
 */
public class pathXRecord 
{
    private ArrayList<pathXLevelRecord> levels;
    
    public pathXRecord()
    {
        levels = new ArrayList<pathXLevelRecord>();
    }
    
    public ArrayList getLevels()
    {
        return levels;
    }
}
