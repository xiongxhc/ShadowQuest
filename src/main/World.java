package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class World
{
	// Declare Objects
    private Player player;
    private TiledMap map;
    private Camera camera;
    
    // Declare ArrayList of Objects
    private ArrayList<Units> unit; 
    private ArrayList<Items> item;

    /** Tile width, in pixels. */
    private int getTileWidth()
    {
        return map.getTileWidth();
    }

    /** Tile height, in pixels. */
    private int getTileHeight()
    {
        return map.getTileHeight();
    }

    /** Create a new World object. 
     * @throws Exception */
    public World()
    throws Exception
    {
        map = new TiledMap(RPG.ASSETS_PATH + "/map.tmx", RPG.ASSETS_PATH);
        
        // Initialize units 
        unit = new ArrayList<Units>();
        addUnit(); // adding all units from data file to game map.
        
        // Initialize player 
        player = (Player)unit.get(0); 
        unit.remove(0); // remove player data after initializing it.
        
        // Initialize items 
        item = new ArrayList<Items>();
        addItem(); // add all item to map.
        
        camera = new Camera(player, RPG.SCREEN_WIDTH, RPG.SCREEN_HEIGHT);
    }
    
    /** A method to read location data from file.
     * 	Adding respective units according to character type.
     * @throws Exception */
    public void addUnit() throws Exception {
    	BufferedReader in = null;
		try {
			File file = new File("data/unit_loc.dat"); // Unit location data
			in = new BufferedReader(new FileReader(file)); // Read data from text file using BufferedReader
	
			LoadData ld = new LoadData(); // Initialize LoadData for matching
			String line;
		    while ((line = in.readLine()) != null) {
		    	StoreData data = null;
		    	
		    	// Similar process to LoadData, data is parse into respective type.
		    	String[] str = line.split(",");
		    	String name = str[0];
		    	double xLoc = Integer.parseInt(str[1]);
		    	double yLoc = Integer.parseInt(str[2]);
		    	// For all data stored, match each by unit name.
		    	for (StoreData sd: ld.unitReader()) {
		    		if (sd.getName().equals(name)) {
		    			data = sd;
		    			break;
		    		}
		    	}
		    	/* 
		    	 * Then, match the type of unit.
		    	 * Add unit object into ArrayList<Units>, initialized according to its Type.
		    	 */
		    	if (data.getType().equals("Player")) {
		    		unit.add(new Player(data.getName(), data.getImgFile(),data.getMaxHP(), 
		    				data.getDamage(), data.getCooldown(), xLoc, yLoc));
		    	} else if (data.getType().equals("Villager")) {
		    		unit.add(new Villager(data.getName(), data.getImgFile(),data.getMaxHP(),
		    				data.getDamage(), data.getCooldown(), xLoc, yLoc));
		    	} else if (data.getMonsterType().equals("Passive")) {
		    		unit.add(new Passive(data.getName(), data.getImgFile(),data.getMaxHP(), 
		    				data.getDamage(), data.getCooldown(), xLoc, yLoc));
		    	} else {
		    		unit.add(new Aggressive(data.getName(), data.getImgFile(),data.getMaxHP(), 
		    				data.getDamage(), data.getCooldown(), xLoc, yLoc));
		    	}
		    }      
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**Similar to addUnit() 
     * Loading data from item_data using BufferedReader,
     * then add into ArrayList.
     * @throws Exception
     */
    public void addItem() throws Exception {
    	BufferedReader in = null;
    	try {
    		File file = new File("data/item_data.txt");
    		in = new BufferedReader(new FileReader(file));
    		// Parsing data using while loop
    		String line;
    	    while ((line = in.readLine()) != null) {
    	    	String[] str = line.split(",");
    	    	String name = str[0];
    	    	String imgFile = str[1];
    	    	String effect = str[2];
    	    	int xLoc = Integer.parseInt(str[3]);
    	    	int yLoc = Integer.parseInt(str[4]);
    	    	// Add item object into ArrayList<Units>
    	    	item.add(new Items(name, imgFile, effect, xLoc, yLoc ));
    	    }      
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /** Remove all Dead(Defeated) Enemy from map.
     */
    public void cleanDead() {
    	Units i = null;
    	// For all unit(Enemy) 
    	for (Units u : unit) {
    		// Once an unit's HP reach Zero(0)
    		if (u.getCurrHP() == 0) { 
        		i = u; // Becomes NULL
        		break;
        	}
    	}
    	unit.remove(i); // Then removed.
    }
    
    /** When an item within range of 50 pixel of Player.
     * It will be added into the inventory.
     * Then remove from the game map.
     */
	public void pickItem() {
		Items temp = null;
        for (Items i: item){
        	// isNear 50 pixel to the player 
        	if (i.isNear(player, 50)) {
        		player.addToInventory(i); // Added to player inventory
        		temp = i; // becomes NULL 
        		break;
        	}
        }
        item.remove(temp); // Then removed.
	}

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     * @param in The user input (keyboard).
     */
    public void update(int dir_x, int dir_y, int delta, Input in)
    throws SlickException
    {
        player.move(dir_x, dir_y, delta, map); // updates player movement  
        player.update(delta); // other updates of player
        camera.update(map); // updates camera, considering borders in map. 
        
        /* Run updates for unit (NPC)
         */
        for (Units u: unit){
        	u.update(delta);
        	if (u instanceof NPC) ((NPC)u).update(player, delta, map);
        }
        
        /* Only when (T) is pressed on keyboard.
         * Run updates for unit (Friendly/Villagers), 
         * when player is within 50 pixel.
         */
        if (in.isKeyPressed(Input.KEY_T)){
        	for (Units u: unit){
        		if (u instanceof Villager && u.nearBy(player, 50, 0)) {
        			((Villager) u).interaction(player, delta); 
        		}
        	}
        }
        
        /* Only when (A) is pressed on keyboard.
         * Player will attack when target unit is either Aggressive or Passive.
         */
        if (in.isKeyDown(Input.KEY_A)){
        	for (Units u: unit){
        		if (u instanceof Aggressive || u instanceof Passive) {
        			player.attact(u);
        		}
        	}
        }
        cleanDead(); // Once dead, remove from map.
        pickItem(); // Add item to inventory, and remove from map.
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // Render the relevant section of the map
        int x = -(camera.getMinX() % getTileWidth());
        int y = -(camera.getMinY() % getTileHeight());
        int sx = camera.getMinX() / getTileWidth();
        int sy = camera.getMinY()/ getTileHeight();
        int w = (camera.getMaxX() / getTileWidth()) - (camera.getMinX() / getTileWidth()) + 1;
        int h = (camera.getMaxY() / getTileHeight()) - (camera.getMinY() / getTileHeight()) + 1;
        map.render(x, y, sx, sy, w, h);

        // Render the player
        player.render(g,camera);
      
        // Render NPC
        for (Units u: unit) {
        	if (u instanceof NPC) 
        		u.render(player, g, camera);
        }
        // Render items
        for (Items i: item) {
        	i.render(camera);
        }
        // Render player status panel.
        player.renderPanel(g);  
    }
}
