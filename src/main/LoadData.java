package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

/** Loading data to 
 */
public class LoadData {
	
	// ArrayList of StoreData type
	private ArrayList<StoreData> data;
	
	public LoadData() {
		data = new ArrayList<StoreData>(); // Initialization
	}
	
	public ArrayList<StoreData> unitReader() throws Exception {
		BufferedReader in = null;
		try {
			File file = new File("data/unit_data.txt");
			
			// Read unit data using BufferedReader.
			in = new BufferedReader(new FileReader(file));
			
			String line;
			// Reading data line by line using while loop until end of file.
		    while ((line = in.readLine()) != null) {
		    	
		    	// parse value into String array split by commas.
		    	String[] str = line.split(",");
		    	// extract value according to variable type
		    	String name = str[0];
		    	String imgFile = str[1];
		    	String type = str[2];
		    	String monsterType = str[3];
		    	int maxHP = Integer.parseInt(str[4]);
		    	int damage = Integer.parseInt(str[5]);
		    	int cooldown = Integer.parseInt(str[6]);
		    	
		    	//Each loop, add respective data into StoreData class. 
		    	data.add(new StoreData(name, imgFile, type, monsterType, maxHP, damage, cooldown ));
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}

