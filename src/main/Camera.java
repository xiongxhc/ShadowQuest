package main;


import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the camera that controls our viewpoint.
 */
public class Camera
{

    /** The unit this camera is following */
    private Player unitFollow;
    
    /** The width and height of the screen */
    /** Screen width, in pixels. */
    private int screenwidth;
    /** Screen height, in pixels. */
    private int screenheight;

    /** The camera's position in the world, in x and y coordinates. */
    private int xPos;
    private int yPos;

    /** Create a new Camera object.
     * 
     * @param player The player to follow
     * @param screenwidth The width of the viewport in pixels
     * @param screenheight The height of the viewport in pixels
     */
    public Camera(Player player, int screenwidth, int screenheight)
    {   
        this.unitFollow = player;
        this.screenwidth = screenwidth;
        this.screenheight = screenheight;
    }

    /** Update the game camera to re-centre its viewpoint around the player 
     */
    public void update(TiledMap map)
    throws SlickException
    {
    	//Position of camera is depended on player, and conditions for black border.
    	if(unitFollow.getX() > 0+screenwidth/2 && unitFollow.getX() 
    			< ((map.getWidth()*map.getTileWidth())-screenwidth/2))
    		xPos = (int) unitFollow.getX();
    	if(unitFollow.getY() > 0+screenheight/2 && unitFollow.getY() 
    			< ((map.getHeight()*map.getTileHeight())-screenheight/2))
    		yPos = (int) unitFollow.getY();
    }
        
    /** Returns the minimum x value on screen 
     */
    public int getMinX(){
        return xPos - screenwidth/2;
    }
    
    /** Returns the maximum x value on screen 
     */
    public int getMaxX(){
        return xPos + screenwidth/2;
    }
    
    /** Returns the minimum y value on screen 
     */
    public int getMinY(){
        return yPos - screenheight/2;
    }
    
    /** Returns the maximum y value on screen 
     */
    public int getMaxY(){
        return yPos + screenheight/2;
    }

    /** Tells the camera to follow a given unit.
     * 
     * @param unit The new unit to follow
     */
    public void followUnit(Object unit)
    {
        if(unit instanceof Player){
            unitFollow = (Player) unit;
        }
    }
    
}