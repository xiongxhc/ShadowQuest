package main;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** The character which the user plays as.
 */
public class Player extends Units
{
	private final int reviveX = 738;
	private final int reviveY = 549;
	private final int ADD_HP = 80;
	private final int ADD_DAMAGE = 30;
	private final int ADD_COOLDOWN = 300;
	
	// In pixels
    private double width, height;
    
    private Image panel;

    // Pixels per millisecond
    private static final double SPEED = 0.25;
    
    private ArrayList<Items> inventory;
    
    /**
     * /** Creates a new Player.
     * @param name Name
     * @param imgPath Path of player's image file.
     * @param maxHP Player Max health
     * @param damage Player damage
     * @param cooldown Player cooldown
     * @param x The Player's starting x location in pixels.
     * @param y The Player's starting y location in pixels.
     * @throws SlickException
     */
    public Player(String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y)
        throws SlickException
    {
        super(name, imgPath, maxHP, damage, cooldown, x, y);
        panel = new Image("assets/panel.png");
        inventory = new ArrayList<Items>();
    }
    
    public void renderPanel(Graphics g)
    {
        // Panel colours
        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle
        int inv_x, inv_y;           // Coordinates to draw inventory item

        float health_percent;       // Player's health, as a percentage

        // Panel background image
        panel.draw(0, RPG.SCREEN_HEIGHT - RPG.panelheight);

        // Display the player's health
        text_x = 15;
        text_y = RPG.SCREEN_HEIGHT - RPG.panelheight + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        
        text =  this.getCurrHP() + "/" + this.getMaxHP();         

        bar_x = 90;
        bar_y = RPG.SCREEN_HEIGHT - RPG.panelheight + 20;
        bar_width = 90;
        bar_height = 30;
        health_percent = (float) (1.0*this.getCurrHP()/this.getMaxHP());
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's damage and cooldown
        text_x = 200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = this.getDamage() + "";                           
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = this.getCooldown() + "";                                    
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", 420, text_y);
        bar_x = 490;
        bar_y = RPG.SCREEN_HEIGHT - RPG.panelheight + 10;
        bar_width = 288;
        bar_height = bar_height + 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);

        inv_x = 490;
        inv_y = RPG.SCREEN_HEIGHT - RPG.panelheight
            + ((RPG.panelheight - 72) / 2);
        
        for (Items i: inventory)        
        {
            i.renderOnPanel(inv_x, inv_y);
            inv_x += 72;
        }
    }

    /** Move the player in a given direction.
     * Prevents the player from moving outside the map space, and also updates
     * the direction the player is facing.
     * @param world The world the player is on (to check blocking).
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void move(double dir_x, double dir_y, double delta, TiledMap map)
    {
        // Update player facing based on X direction
        this.unitFacing(dir_x, 0);

        // Move the player by dir_x, dir_y, as a multiple of delta * speed
        double new_x = this.getX() + dir_x * delta * SPEED;
        double new_y = this.getY() + dir_y * delta * SPEED;

        // Move in x first
        double x_sign = Math.signum(dir_x);
        if(!terrainBlocks(new_x + x_sign * width / 4, this.getY() + height / 4, map) 
                && !terrainBlocks(new_x + x_sign * width / 4, this.getY() - height / 4, map)) {
            this.setX(new_x);
        }
        
        // Then move in y
        double y_sign = Math.signum(dir_y);
        if(!terrainBlocks(this.getX() + width / 4, new_y + y_sign * height / 4, map) 
                && !terrainBlocks(this.getX() - width / 4, new_y + y_sign * height / 4, map)){
            this.setY(new_y);
        }
   
    }
    
    public void attact(Units monster) {
    	int monsterHP;
		if(this.nearBy(this, NEAR_DIST, 0)) {
			if (this.getTimer() == this.getCooldown()) {
				if (monster instanceof Passive) {
					((Passive) monster).setAttacked(true);
				}
				monsterHP = monster.getCurrHP() - this.getDamage();
				if (monsterHP <= 0) {
					monster.setCurrHP(0);
				} else {
					monster.setCurrHP(monsterHP);
				}
			}
		}
    }
    
    public ArrayList<Items> getInventory() {
		return inventory;
	}

	public void addToInventory(Items i) {
    	inventory.add(i);
		if (i.getName().equalsIgnoreCase("Amulet of Vitality")) {
			this.setCurrHP(this.getCurrHP() + ADD_HP);
			this.setMaxHP(this.getMaxHP() + ADD_HP);
		} else if (i.getName().equalsIgnoreCase("Sword of Strength")) {
			this.setDamage(this.getDamage() + ADD_DAMAGE);
		} else {
			this.setCooldown(this.getCooldown() - ADD_COOLDOWN);
		}
    }
    
	public void update(int delta) {
		super.update(delta);
		if( this.getCurrHP() <= 0 ) {
			this.setX(reviveX);
			this.setY(reviveY);
			this.setCurrHP(this.getMaxHP());
		}
		
	}
    /** Draw the player to the screen at the correct place.
     * @param g The current Graphics context.
     * @param cam_x Camera x position in pixels.
     * @param cam_y Camera y position in pixels.
     */
    public void render(Graphics g, Camera cam)
    {
        super.render(this, g, cam);
    }
}
