package main;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Aggressive extends NPC
{	
	/**
	 * Constructor of aggressive enemy monsters
	 * @param name The name of aggressive monsters
	 * @param imgPath Image file name
	 * @param maxHP Maximum health for monster
	 * @param damage The damage or attack strength of monster
	 * @param cooldown Rate/Cooldown between attacks intervals
	 * @param x Position coordinate x of monster
	 * @param y Position coordinate y of monster
	 * @throws SlickException
	 */
	public Aggressive (String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y)
	        throws SlickException 
	{
	        super(name, imgPath, maxHP, damage, cooldown, x, y);
	}
	
	/**
	 * The chasing mechanics once player is approaching monster.
	 * @param p Player that set as a reference for distance.
	 * @param delta Time passed since last frame (milliseconds).
	 * @param map Game map are include for terrain blocking purposes.
	 */
	public void chase(Player p, int delta, TiledMap map) {
		
		double distX, distY, amount, distTotal, dX , dY;
		
		/*
		 * Formula given in project specification.
		 */
		if( this.nearBy(p, FAR_DIST, NEAR_DIST)) {
			distX = this.getX() - p.getX();
			distY = this.getY() - p.getY();
			amount = delta * PLAYER_SPEED;
			distTotal = Math.sqrt(Math.pow(distX, SQUARED) + Math.pow(distY, SQUARED));
			dX = (distX/distTotal) * amount;
			dY = (distY/distTotal) * amount;
			
			double nextX = this.getX() - dX;
	        double nextY = this.getY() - dY;

	        // The blocking of monsters in map.
	        if(!terrainBlocks(nextX, nextY, map)) {
	            this.setX(nextX);
	            this.setY(nextY);
	        }
	     // Facing direction control via next and previous position.
	     unitFacing(dX, distX);
		}
	}
	
	/**
	 * The combat method using to deal damage to player.
	 * @param p Player for combat reference
	 */
	public void combat(Player p) {
		int playerHP;
		if( this.nearBy(p, NEAR_DIST, 0)) {
			if (this.getTimer() == this.getCooldown()) {
				playerHP = p.getCurrHP() - this.getDamage();
				if (playerHP <= 0) {
					p.setCurrHP(0);
				} else {
					p.setCurrHP(playerHP);
				}
			}
		}
	}
	
	/**
	 * Update for aggressive monster.
	 */
	@Override
	public void update(Player p, int delta, TiledMap map) {
		super.update(p, delta, map);
		combat(p);
		chase(p, delta, map);
	} 
	
	/**
	 * Update for aggressive monster.
	 * Super all parameter
	 */
	public void render (Player p, Graphics g, Camera cam) {
		super.render(p, g, cam);
	}
}
