package main;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Passive extends NPC
{
	private int wanderTimer;
	private int escapeTimer;
	private int randX, randY;
	private boolean isAttacked;

	public Passive (String name, String imgPath, int maxHP, int damage, int cooldown, double x, double y)
	        throws SlickException
	    {
	        super(name, imgPath, maxHP, damage, cooldown, x, y);
	        wanderTimer = 3000;
	        escapeTimer = 5000;
	        randX = 0;
	        randY = 0;
	        isAttacked = false;
	    }
	
	public void setAttacked(boolean isAttacked) {
		this.isAttacked = isAttacked;
	}

	public void wander(int delta, TiledMap map) {
		double wanderX, wanderY;
		if (wanderTimer == 3000) {
			randX = -1 + new Random().nextInt(3);
			randY = -1 + new Random().nextInt(3);
		}
		wanderX = this.getX() + delta * randX * 0.1;
		wanderY = this.getY() + delta * randY * 0.1;
	
		// Move in x first
        if(!terrainBlocks(wanderX, wanderY, map)) {
        	this.setX(wanderX);
    		this.setY(wanderY);
        }
        unitFacing(randX, 0);
	}
	
	public void escape(Player p, int delta, TiledMap map) {
		double distX, distY, amount, distTotal, dX , dY;
		
		distX = this.getX() - p.getX();
		distY = this.getY() - p.getY();
		amount = delta * 0.20;
		distTotal = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
		dX = (distX/distTotal) * amount;
		dY = (distY/distTotal) * amount;
		
		double nextX = this.getX() + dX;
        double nextY = this.getY() + dY;

        // Move in x first
        if(!terrainBlocks(nextX, nextY, map)) {
            this.setX(nextX);
            this.setY(nextY);
        }
        unitFacing(dX, distX);
	}
	
	@Override
	public void update(Player p, int delta, TiledMap map) {
		super.update(p, delta, map);
		
		wanderTimer = wanderTimer - delta <= 0 ? 3000 : wanderTimer - delta;
		escapeTimer = escapeTimer - delta <= 0 ? 5000 : escapeTimer - delta;
		
		if (isAttacked && escapeTimer == 5000){
			isAttacked = false;
		}
		if (!this.isAttacked) {
			wander(delta, map);
		} else {
			escapeTimer = 5000 - 1;
			escape(p, delta, map);
		}

	}
	
	public void render (Player p, Graphics g, Camera cam) {
		super.render(p, g, cam);
	}
}
