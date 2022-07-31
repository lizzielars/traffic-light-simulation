/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides the Car object that monitors the
 * cars position and speed measured in meters per second. 
 * 
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
	
	/*------------------------------
			Attributes
	------------------------------*/
	
    protected int xPosition;
    private int yPosition = 0;
    private final AtomicBoolean carIsRunning = new AtomicBoolean(false);
    protected final AtomicBoolean carIsAtLight = new AtomicBoolean(false);
    protected final AtomicBoolean carPaused = new AtomicBoolean(false);
    protected Thread thread;
    private int speedMetersPerSecond = 5;
    
	/*------------------------------
			Constructor
	------------------------------*/
    
    public Car(int min, int max) {
        
    	this.xPosition = ThreadLocalRandom.current().nextInt(min, max);
    
    }
    
	/*------------------------------
			Get Methods
	------------------------------*/
    
    public synchronized int getXPosition() {
    	
    	return this.xPosition;
    	
    }

    public synchronized String getPosition() {
    	
        return "(" + xPosition + ", " + yPosition + ")";
        
    }
    
    public int getCarSpeed() {
    	
        if(carIsRunning.get()) {
        	
            if(carIsAtLight.get()) 
            	speedMetersPerSecond = 0;

            else { 
            	speedMetersPerSecond = 5;
            }
        } else 
        	speedMetersPerSecond = 0;
        
        return speedMetersPerSecond;
    }
    
	/*------------------------------
			Methods
	------------------------------*/
    
    // This method starts a new thread of the car if there is none
    public void start() {
    	
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    // This method stops the car and interrupts the thread
    public void stopCar() {
        thread.interrupt();
        carIsRunning.set(false);
    }
    
    // This method pauses the car
    public void pauseCar() {
        carPaused.set(true);
    }
    
    // This method resumes the car after it has been paused
    public synchronized void resume() {
        //If car has stopped, start the car and notify
        if(carPaused.get() || carIsAtLight.get()) {
        	carPaused.set(false);
        	carIsAtLight.set(false);
            notify();
         }
    }
    
    // This method monitors the cars position while the simulation is running
    @Override
    public void run() {
 
        carIsRunning.set(true);
        
        // While the car is running
        while(carIsRunning.get()) {
            try {
            	// While the car is in range
                while(xPosition < 3000) {
                    synchronized(this) {
                    	// If the car has stopped, thread waits
                        while(carPaused.get() || carIsAtLight.get()) {
                            wait(); 
                        }
                    }
                    // If the car is running
                    if(carIsRunning.get()) {
                    // Thread sleeps for 100 milliseconds and the car moves
                    Thread.sleep(100);
                    xPosition+=5;
                    }
                }
                // Else the car isn't moving
                xPosition = 0; 
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
    
}