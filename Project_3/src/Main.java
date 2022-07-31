/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides a GUI interface for Project 3 
 * of CMSC 335. It provides the current time, real-time light
 * displaay for three intersections, and the speed and X,Y positions of 
 * three cars as they traverse each of the 3 intersections.
 * 
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.event.*;



public class Main extends JFrame implements Runnable, ChangeListener {
	
	/*------------------------------
				Attributes
	------------------------------*/
	
	private static final long serialVersionUID = 1L;
	
	// JPanels for formatting widgets in window	
	private JPanel panel1 = new JPanel();
	private JPanel timePanel = new JPanel(new FlowLayout());
	private JPanel lightsPanel = new JPanel(new GridLayout(2, 3, 5, 5));
	private JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 5, 5));
	private JPanel carDataPanel = new JPanel(new FlowLayout());
	
	// JLabel to display current time
	protected static JLabel displayedTime = new JLabel();
    
    // Buttons to Start, Pause, or Stop program
    private JButton startButton = new JButton("Start");
    private JButton pauseButton = new JButton("Pause");
    private JButton stopButton = new JButton("Stop"); 
    
    // Traffic light widgets for the three intersections
    private static TrafficLight trafficLight1 = new TrafficLight();
    private static TrafficLight trafficLight2 = new TrafficLight();
    private static TrafficLight trafficLight3 = new TrafficLight();
    
    // Booleans to traffic if program is running and the threads are running
    private static boolean isProgramRunning;
    private static final AtomicBoolean IsTrafficRunning = new AtomicBoolean(false);
    
    // Three intersections
    private Intersection intersection1 = new Intersection(trafficLight1);
    private Intersection intersection2 = new Intersection(trafficLight2);
    private Intersection intersection3 = new Intersection(trafficLight3);
    
    // Creates sliders to monitor changes to threads
    static JSlider car1Monitor = new JSlider(0, 3000);
    static JSlider car2Monitor = new JSlider(0, 3000);
    static JSlider car3Monitor = new JSlider(0, 3000);
    
    // Creates three cars
    private Car car1 = new Car(0,500);
    private Car car2 = new Car(1000, 2000);
    private Car car3 = new Car(2500, 3000);
    
    // Arrays of cars and intersections in program
    private Car[] cars = {car1, car2, car3};
    private Intersection[] intersections = {intersection1, intersection2, intersection3};
    
    // Defines thread for program
    private static Thread programThread;
    
    // Set initial data for data table
    private Object[][] trafficData = {
        {"Car 1", 0, car1.getPosition()},
        {"Car 2", 0, car2.getPosition()},
        {"Car 3", 0, car3.getPosition()},
    };
    
    //Table for displaying data
    
    private JTable dataTable = new JTable(new TrafficTable(trafficData));
    
    
	/*------------------------------
			Constructor
	------------------------------*/
    
    public Main() {
    	
    	// Set layout of window
        setLayout(new FlowLayout());
        
        // Sets layout of main panel
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        
        // Add panel to window
        add(panel1);
        
        // Check if current thread is alive
        isProgramRunning = Thread.currentThread().isAlive();
        
        // Build program GUI
        buildGUI();
        
        setButtonActionListeners();
    }
    
    
	/*------------------------------
				Methods
	------------------------------*/
    
    // This method sets the window size, title, and position
    
    // This method sets the size, title, and position of the window
    private void setUpWindow() {
    	
        setSize(500,300);
        
        setVisible(true);
        
        setTitle("Traffic Light Simulation");
        
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    // Assigns variables to gui objects and builds them
    private void buildGUI() {
        
        JLabel time = new JLabel("Time: ");
        
        JLabel trafficLight1Label = new JLabel("Light 1(1000 Meters): ");
        JLabel trafficLight2Label = new JLabel("Light 2(2000 Meters): ");
        JLabel trafficLight3Label = new JLabel("Light 3(3000 Meters): ");
        
        // Listens to changes in the car monitors
        car1Monitor.addChangeListener(this);
        car2Monitor.addChangeListener(this);
        car3Monitor.addChangeListener(this);
    
        // Set size of data table
        dataTable.setPreferredScrollableViewportSize(new Dimension(450, 75));
        
        JPanel dataPanel = new JPanel();  
        
        //Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(dataTable);
        dataPanel.add(scrollPane);
        
        // Add all panels to main panel
        panel1.add(timePanel);
        panel1.add(lightsPanel);
        panel1.add(lightsPanel);
        panel1.add(buttonsPanel);
        panel1.add(carDataPanel);
        
        // Add time widgets to time panel
        timePanel.add(time);
        timePanel.add(displayedTime);
        
        // Add traffic lights and labels to lights panel        
        lightsPanel.add(trafficLight1Label);
        lightsPanel.add(trafficLight2Label);
        lightsPanel.add(trafficLight3Label);
        lightsPanel.add(trafficLight1.trafficPanel);
        lightsPanel.add(trafficLight2.trafficPanel);
        lightsPanel.add(trafficLight3.trafficPanel);
        
        // Add buttons to buttons panel
        buttonsPanel.add(startButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(stopButton);
        
        // Add car data to car data panel
        carDataPanel.add(dataPanel);
        
        pack();
    }
    
    // This method adds action listeners to all three buttons. 
    // Adds Action Listeners to buttons
    private void setButtonActionListeners() {

        startButton.addActionListener((ActionEvent e) -> {
        	
        	// If traffic is not running
            if(!IsTrafficRunning.get()) {
                // Start intersection, car, and program threads threads
                intersection1.start();
                intersection2.start();
                intersection3.start();
                
                car1.start();
                car2.start();
                car3.start();
                
                programThread.start();
                
            }
            else {
            	displayErrorMessage("Traffic already started!");
            }
            //Traffic is running
            IsTrafficRunning.set(true);   
        });
        
        pauseButton.addActionListener((ActionEvent e) -> {
        	
        	// If traffic is running
        	if(IsTrafficRunning.get()) {
                
            	// For each car
                for(Car car : cars) {
                	// Pause car
                    car.pauseCar();
                }
                
                // For each intersection
                for(Intersection intersection : intersections) {
                    
                	// Pause intersection threads
                    intersection.pauseIntersection();
                }
                
                // Change pause button text to "Unpause"
                pauseButton.setText("Unpause");
                
                // Traffic is not running
                IsTrafficRunning.set(false);
            
            // Else unpause cars and intersections
            } else {
            	
                for(Car car : cars) {
                	
                    if(car.carPaused.get()) {
                        car.resume();
                    }
                }
                for(Intersection intersection : intersections) {
                    intersection.resumeTraffic();
                }
                
                // Set pause button text back to "Pause"
                pauseButton.setText("Pause");
                
                // Traffic is running
                IsTrafficRunning.set(true);
            }
        });
        
        stopButton.addActionListener((ActionEvent e) -> {
        	// If traffic is running
            if(IsTrafficRunning.get()) {
            	// Stop each car
                for(Car car : cars) {
                    car.stopCar();
                }
                // Stop each intersection
                for(Intersection intersection: intersections) {
                    intersection.stopIntersection();
                }
                IsTrafficRunning.set(false);
            }
            else {
            	displayErrorMessage("Traffic already stopped!");
            }
        });
    }
    
    // Method to display an error message in a new window
 	
    // This method displays an error message window
    private void displayErrorMessage(String errorMessage) {
 			
 		// Creates a new error window 
 		JFrame errorWindow = new JFrame("Error");
 		JOptionPane.showMessageDialog(errorWindow, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
 	}
    

    // This method updates the data table based on the car monitors
	@Override
    public void stateChanged(ChangeEvent e) {
		
        // Updates data in the table
        trafficData[0][2] = car1.getPosition();
        trafficData[1][2] = car2.getPosition();
        trafficData[2][2] = car3.getPosition();
        trafficData[0][1] = car1.getCarSpeed();
        trafficData[1][1] = car2.getCarSpeed();
        trafficData[2][1] = car3.getCarSpeed();
        dataTable.repaint();
    }
    
    
    @Override
    public void run() {
        while(isProgramRunning) {
            //While running, if simulation is running, set car sliders to car xPosition and get data
            if(IsTrafficRunning.get()) {
            car1Monitor.setValue(car1.getXPosition());
            car2Monitor.setValue(car2.getXPosition());
            car3Monitor.setValue(car3.getXPosition());

            monitorTraffic();
            }
        }
    }
    
    // This method monitors the traffic. If the cars are near a red light, they are set
    // to stop. Otherwise, they are set to go.
    private void monitorTraffic() {
    	
    	// If the traffic is running
        if(IsTrafficRunning.get()) {
        	
	        switch(intersection1.getColor()) {
	        	// If the first intersections light is Red
	            case "Red":
	            	// For each car
	                for(Car car : cars) {
	                    // If the car is getting close to the red light
	                    if(car.getXPosition()>750 && car.getXPosition()<1000) {
	                    	// Set car is at light to true
	                        car.carIsAtLight.set(true);
	                    }
	                }
	                break;
	            // If the first intersections light if green
	            case "Green":
	            	// For each car
	                for(Car car : cars) {
	                	// If the car is at the light
	                    if(car.carIsAtLight.get()) {
	                    	// Car starts driving again
	                        car.resume();
	                    }
	                }
	                break;
	        }
	        
	        switch(intersection2.getColor()) {
	        // If the second intersections light is Red
	            case "Red":
	            	// For each car
	                for(Car car : cars) {
	                	// If the car is getting close to the red light
	                    if(car.getXPosition()>1750 && car.getXPosition()<2000) {
	                    	// Set car is at light to true
	                        car.carIsAtLight.set(true);
	                    }
	                }
	                break;
	             // If the second intersections light if green
	            case "Green":
	            	// For each car
	                for(Car car : cars) {
	                	// If the car is at the light
	                    if(car.carIsAtLight.get()) {
	                    	// Car starts driving again
	                        car.resume();
	                    }
	                }
	                break;
	        }
	        
	        switch(intersection3.getColor()) {
	        	// If the third intersections light is Red
	            case "Red":
	            	// For each car
	                for(Car car : cars) {
	                	// If the car is getting close to the red light
	                    if(car.getXPosition()>2700 && car.getXPosition()<3000) {
	                    	// Set car is at light to true
	                        car.carIsAtLight.set(true);
	                    }
	                }
	                break;
	            // If the third intersections light is green
	            case "Green":
	            	// For each car
	                for(Car car : cars) {
	                	// If the car is stopped at the light
	                    if(car.carIsAtLight.get()) {
	                    	// Car starts driving again
	                        car.resume();
	                    }
	                }
	                break;
	        }
        }
        
    }
   
	/*------------------------------
			Main Method
	------------------------------*/
    public static void main(String[] args) {
    	
    	// Initialized GUI
        Main gui = new Main();
        
        // Displays GUI
        gui.setUpWindow();
        
        // Assigns thread to GUI
        programThread = new Thread(gui);
        
        // Creates a mew time thread
        Thread time = new Thread(new Time());
        
        // Starts time thread
        time.start();
    }   
}