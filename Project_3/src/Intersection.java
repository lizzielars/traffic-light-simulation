/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides the Intersection object that monitors the
 * traffic lights. 
 * 
 */

import java.util.concurrent.atomic.AtomicBoolean;


public class Intersection implements Runnable {


	/*------------------------------
			Attributes
	------------------------------*/
	
	private TrafficLight trafficLight;
    private final String[] COLORS = {"Green", "Yellow", "Red"};
    private int i = 0;
    private String currentLight = COLORS[i];
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    protected final AtomicBoolean intersectionPaused = new AtomicBoolean(false);
    protected Thread thread;
    
    
	/*------------------------------
			Constructor
	------------------------------*/
    
    public Intersection(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

	/*------------------------------
			Get Methods
	------------------------------*/
    
    public synchronized String getColor() {
        this.currentLight = COLORS[i];
        return this.currentLight;
    }
    
    // This method starts turns the light green
    public synchronized void resumeTraffic() {
    	intersectionPaused.set(false);
        notify();
    }
    // This method starts a new thread of the intersection
    public void start() {
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    // This method interrupts the thread of this object
    public void stopIntersection() {
        thread.interrupt();
        isRunning.set(false);
    }
    
    // This method pauses the thread of this object
    public void pauseIntersection() {
        thread.interrupt();
        intersectionPaused.set(true);
    }

    // This method monitors the intersection lights
    @Override
    public void run() {
    	
        isRunning.set(true);
        // While the simulation is running
        while(isRunning.get()) {
            try {
                synchronized(this) {
                	// If the intersection is paused, wait
                        while(intersectionPaused.get()) {
                            wait();
                        }
                    }
                switch (getColor()) {
                    case "Green":
                    	// If light is green, change color to green for 10 seconds
                        trafficLight.greenLight.turnOn(true);
                        trafficLight.yellowLight.turnOn(false);
                        trafficLight.redLight.turnOn(false);
                        //Stay green for 10 seconds
                        Thread.sleep(10000);
                        i++;
                        break;
                    case "Yellow":
                    	// If light is yellow, change color to green for 2 seconds
                        trafficLight.greenLight.turnOn(false);
                        trafficLight.yellowLight.turnOn(true);
                        trafficLight.redLight.turnOn(false);
                        //Yellow for 5 seconds
                        Thread.sleep(2000);
                        i++;
                        break;
                    case "Red":
                    	// If light is red, change color to red for 12 seconds
                        trafficLight.greenLight.turnOn(false);
                        trafficLight.yellowLight.turnOn(false);
                        trafficLight.redLight.turnOn(true);
                        //Red for 5 seconds
                        Thread.sleep(12000);
                        //Set i back to 0
                        i = 0;
                        break;
                    default:
                        break;
                }
                
            } catch (InterruptedException ex) {
                // If a thread gets interrupted, pause intersection
            	intersectionPaused.set(true);
            }
        }
    }
    
}