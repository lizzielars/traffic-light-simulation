/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides the Time object that monitors the
 * current time. 
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time implements Runnable {
	
	/*------------------------------
			Attributes
	------------------------------*/
    
	public Date date = new Date(System.currentTimeMillis());
    private boolean timeIsRunning;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); 

	/*------------------------------
			Constructor
	------------------------------*/
    
    public Time() {
        this.timeIsRunning = Thread.currentThread().isAlive();
    }
    
	/*------------------------------
				Methods
	------------------------------*/
    
    // This method gets the current time
    public String getTime() {
        date = new Date(System.currentTimeMillis());
        return timeFormat.format(date);
    }

    // This method updates the time while the thread is running
    @Override
    public void run() {
        while (timeIsRunning) {
            
            Main.displayedTime.setText(getTime());
        } 
    }
    
}