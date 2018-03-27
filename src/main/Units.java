package main;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public abstract class Units {
	
	protected final int FAR_DIST = 150;
	protected final int NEAR_DIST = 50;
	protected final int SQUARED = 2;
	protected final double PLAYER_SPEED = 0.25;
	
	private String name;
	private Image unitImg;
	private Image imgFlipped;
	private boolean alive;
	private boolean faceLeft;
	
	private double x, y;
	private int currHP;
	private int maxHP;
	private int damage;
	private int cooldown;
	private int timer;
	
	/**
	 * Constructor for Unit class
	 * @param name Unit name
	 * @param imgPath Unit image file
	 * @param maxHP Unit Health
	 * @param damage Unit damage
	 * @param cooldown Unit cooldown
	 * @param x Unit location x
	 * @param y Unit location y
	 * @throws SlickException
	 */
	public Units(String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y) 
			throws SlickException 
	{
		this.name = name;
		unitImg = new Image("assets/units/"+ imgPath);
		imgFlipped = unitImg.getFlippedCopy(true, false);
		alive = true;
		faceLeft = false;
		
		this.maxHP = maxHP;
		this.currHP = maxHP;
		this.damage = damage;
		this.cooldown = cooldown;
		this.timer = cooldown;
		this.x = x;
		this.y = y;
	}
	
	public String getName() {
		return name;
	}

	public int getTimer() {
		return timer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getUnitImg() {
		return unitImg;
	}

	public void setUnitImg(Image unitImg) {
		this.unitImg = unitImg;
	}

	public Image getImgFlipped() {
		return imgFlipped;
	}

	public void setImgFlipped(Image imgFlipped) {
		this.imgFlipped = imgFlipped;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isFaceLeft() {
		return faceLeft;
	}

	public void setFaceLeft(boolean faceLeft) {
		this.faceLeft = faceLeft;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getCurrHP() {
		return currHP;
	}

	public void setCurrHP(int currHP) {
		this.currHP = currHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public boolean nearBy (Player p, int farDist, int nearDist) {
		return (Math.sqrt(Math.pow(p.getX()-this.getX(), 2) + Math.pow(p.getY()-this.getY(), 2)) <= farDist && 
				Math.sqrt(Math.pow(p.getX()-this.getX(), 2) + Math.pow(p.getY()-this.getY(), 2)) >= nearDist); 
	}

    /** Determines whether a particular map coordinate blocks movement.
     * @param x Map x coordinate (in pixels).
     * @param y Map y coordinate (in pixels).
     * @return true if the coordinate blocks movement.
     */
    public boolean terrainBlocks(double x, double y, TiledMap map)
    {
        // Check we are within the bounds of the map
        if (x < 0 || y < 0 || x > map.getWidth() * map.getTileWidth() || y > map.getHeight() * map.getTileHeight()) {
            return true;
        }
        
        // Check the tile properties
        int tile_x = (int) x / map.getTileWidth();
        int tile_y = (int) y / map.getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return !block.equals("0");
    }
    
    /**
     * Determine the unit facing
     * @param nX NextX
     * @param X coordinate x
     */
    public void unitFacing (double nX, double X) {
    	// Update unit facing based on X direction
        if (nX > X)
        	this.setFaceLeft(false);
        else if (nX < X)
            this.setFaceLeft(true);
    }
    /** Unit update
	 * @param delta Time passed since last frame (milliseconds).
     */
    public void update(int delta) {
		timer = timer - delta <= 0 ? cooldown : timer - delta;
	}
	
    /**
     * Render unit
     * @param p Player
	 * @param g Graphic renderer.
	 * @param map Game map
     */
	public void render(Player p, Graphics g, Camera cam) 
    {   
        Image whichImg;
        whichImg = this.faceLeft ? this.getImgFlipped() : this.getUnitImg();
        whichImg.drawCentered((int) this.getX() - cam.getMinX(), (int) this.getY() - cam.getMinY());
    }

}
