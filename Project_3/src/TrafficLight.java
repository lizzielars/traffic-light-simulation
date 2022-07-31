/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides a the traffic light gui
 * for the program. 
 * 
 */

import java.awt.*;
import javax.swing.*;


public class TrafficLight extends JPanel {
	
	/*------------------------------
				Attributes
	------------------------------*/

	private static final long serialVersionUID = 1L;
	protected LightSignal greenLight = new LightSignal(Color.green);
    protected LightSignal yellowLight = new LightSignal(Color.yellow);
    protected LightSignal redLight = new LightSignal(Color.red);
    protected JPanel trafficPanel = new JPanel(new FlowLayout());

	/*------------------------------
				Constructor
	------------------------------*/
    
    public TrafficLight(){     

    	greenLight.turnOn(false);
        yellowLight.turnOn(false);
        redLight.turnOn(true);

        trafficPanel.add(redLight);
        trafficPanel.add(yellowLight);
        trafficPanel.add(greenLight);

        }

}  

// This class creates the individual lights on each traffic light
class LightSignal extends JPanel{

	/*------------------------------
				Attributes
	------------------------------*/
	
	private static final long serialVersionUID = 1L;
	private Color activeColor;
    private int radius = 14;
    private int border = 0;
    private boolean colorChanged;

    /*------------------------------
			Constructor
	------------------------------*/
    LightSignal(Color color){
    	activeColor = color;
        colorChanged = true;
    }

    // This method turns on our off a light
    public void turnOn(boolean setLightPower){
        colorChanged = setLightPower;
        repaint();      
    }

    // This method sets the size of the light
    public Dimension getPreferredSize(){
    	
        int lightSize = (radius+border)*2;
        
        return new Dimension(lightSize, lightSize);
    }

    // This method paints the components to make the circle
    public void paintComponent(Graphics graphics){
        graphics.setColor(new Color(238,238,238));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        if (colorChanged){
        	// Set color to bright
            graphics.setColor(activeColor);
        } else {
        	// Set color to dark
            graphics.setColor(activeColor.darker().darker().darker());
        }
        
        graphics.fillOval( border, border, 2*radius, 2*radius);
    }
}