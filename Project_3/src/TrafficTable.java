/*
 * 
 * @author Elizabeth Larson
 * Date: 10/12/2021
 * Project: CMSC 335 Project 3
 * Description: This class provides the data table that monitors the
 * simulation. 
 * 
 */

import javax.swing.table.AbstractTableModel;

class TrafficTable extends AbstractTableModel {
	
	/*------------------------------
			Attributes
	------------------------------*/

	private static final long serialVersionUID = 1L;
	private String[] COLUMNNAMES = {"Car", "Speed (Meters/Second)", "Position(Feet)(X,Y)"};
    private Object[][] data;
    
	/*------------------------------
			Constructor
	------------------------------*/
    
    TrafficTable(Object[][] data){
    	this.data = data;
    }
    
	/*------------------------------
			Get Methods
	------------------------------*/

    public int getColumnCount() {
        return COLUMNNAMES.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return COLUMNNAMES[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }


}