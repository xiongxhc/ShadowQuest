package main;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public abstract class NPC extends Units
{
	/**
	 * NPC constructor
	 * @param name of NPC
	 * @param imgPath Image name of NPC
	 * @param maxHP NPC max health
	 * @param damage NPC damage
	 * @param cooldown NPC cooldown
	 * @param x NPC x coordinate
	 * @param y NPC y coordinate
	 * @throws SlickException
	 */
	public NPC (String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y)
	        throws SlickException
	    {
	        super(name, imgPath, maxHP, damage, cooldown, x, y);
	    }
	
	/**
	 * NPC update
	 * @param p Player
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map Game map
	 */
	public void update(Player p, int delta, TiledMap map) {
		super.update(delta);
	}
	
	/**
	 * @param p Player
	 * @param g Graphic renderer
	 * @param cam Camera
	 */
	@Override
	public void render(Player p, Graphics g, Camera cam) {
		super.render(p, g, cam);
		// Panel colours
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle

        float health_percent;       // Player's health, as a percentage
        
        text =  this.getName();                          // name

        
        bar_width = g.getFont().getWidth(text) + 6;
        bar_height = g.getFont().getHeight(text) + 4;
        bar_x = (int) this.getX() - cam.getMinX() - bar_width/2;
        bar_y = (int) this.getY() - cam.getMinY() - 25 - bar_height;
        health_percent = (float) (1.0*this.getCurrHP()/this.getMaxHP());                         
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        text_y = bar_y;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
	}
}
