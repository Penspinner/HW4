/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathX.data;

/**
 *
 * @author Dell
 */
public class Timer implements Runnable 
{
    int seconds;
    public Timer(int initSeconds)
    {
        seconds = initSeconds;
    }
    
    public void run()
    {
        try
        {
            long milliseconds = seconds * 1000;
            Thread.sleep(milliseconds);
        } catch (InterruptedException ie)
        {
            
        }
    }
}
