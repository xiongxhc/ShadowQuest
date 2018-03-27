package main;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Villager extends NPC
{
	private String dialogue;
	private int dialogueTimer;
	
	private boolean isDisplayed;
	
	/**
	 * Constuctor of Villager
	 * @param name
	 * @param imgPath
	 * @param maxHP
	 * @param damage
	 * @param cooldown
	 * @param x
	 * @param y
	 * @throws SlickException
	 */
	public Villager (String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y)
	        throws SlickException
	{
	        super(name, imgPath, maxHP, damage, cooldown, x, y);
	        dialogue = null;
	        dialogueTimer = 4000;
	        isDisplayed = false;
	}
	
	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	/**
	 * Interaction method between villager and player
	 * @param p Player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map Game map
	 */
	public void interaction(Player p, int delta) {
		isDisplayed = true;
		dialogueTimer = 4000-1;
		
		switch (this.getName()) {
		case "Prince Aldric":
			boolean haveElixir = false;
			for (Items i : p.getInventory()){
				if (i.getName().equalsIgnoreCase("Elixir of Life")){
					haveElixir = true;
					break;
				}
			}
			if (!haveElixir) {
				dialogue = "Please seek out the Elixir of Life to cure the king.";
			} else {
				dialogue = "The elixir! My father is cured! Thank you!";
			}
			break;
			
		case "Elvira":	
			if (p.getCurrHP() == p.getMaxHP()) {
				dialogue = "Return to me if you ever need healing.";
			} else {
				dialogue = "You're looking much healthier now.";
				p.setCurrHP(p.getMaxHP());
			}
			break;
			
		case "Garth":
			boolean haveAmulet = false;
			boolean haveSword = false;
			boolean haveTome = false;
			
			for (Items i: p.getInventory()) {
				if (i.getName().equalsIgnoreCase("Amulet of Vitality"))
					haveAmulet = true;
				if (i.getName().equalsIgnoreCase("Sword of Strength")) 
					haveSword = true;
				if (i.getName().equalsIgnoreCase("Tome of Agility")) 
					haveTome = true;
			}
			
			if (!haveAmulet) {
				dialogue = "Find the Amulet of Vitality, across the river to the west.";
			} else if (!haveSword) {
				dialogue = "Find the Sword of Strength - cross the bridge to the east, then head south.";
			} else if (!haveTome) {
				dialogue = "Find the Tome of Agility, in the Land of Shadows.";
			} else {
				dialogue = "You have found all the treasure I know of.";
			}
			break;
		}
	}
	@Override
	public void update(Player p, int delta, TiledMap map) {
		super.update(p, delta, map);
		dialogueTimer = dialogueTimer - delta <= 0 ? 4000 : dialogueTimer - delta;	
	}
	
	@Override
	public void render(Player p, Graphics g, Camera cam) {
		super.render(p, g, cam);
		
		if (isDisplayed && dialogueTimer == 4000) {
			isDisplayed = false;
		}
		
		if( dialogue != null && isDisplayed) {
			// Panel colours
	        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
	        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp

	        // Variables for layout
	        int dialogue_x, dialogue_y;         // Coordinates to draw text
	        int bar_x, bar_y;           // Coordinates to draw rectangles
	        int bar_width, bar_height;  // Size of rectangle to draw

	        bar_width = g.getFont().getWidth(dialogue) + 6;
	        bar_height = g.getFont().getHeight(dialogue) + 4;
	        bar_x = (int) this.getX() - cam.getMinX() - bar_width/2;
	        bar_y = (int) this.getY() - cam.getMinY() - 50 - bar_height;
	        dialogue_x = bar_x + (bar_width - g.getFont().getWidth(dialogue)) / 2;
	        dialogue_y = bar_y;
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(dialogue, dialogue_x, dialogue_y);
		}
	}
}
