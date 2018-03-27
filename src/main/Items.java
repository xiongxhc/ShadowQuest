package main;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Items {
	
	// Declarations
	private String name;
	private Image itemImg;
	private String effect;
	private int xLoc;
	private int yLoc;
	
	/**
	 * Constructor for Item class
	 * @param name Name of item
	 * @param imgPath Item image file name
	 * @param effect Buff effect of item
	 * @param xLoc Item spawn location x
	 * @param yLoc Item spawn location y
	 * @throws SlickException
	 */
	public Items(String name, String imgPath, String effect, int xLoc, int yLoc) throws SlickException {
		this.name = name;
		this.itemImg= new Image("assets/items/"+ imgPath);;
		this.effect = effect;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getItemImg() {
		return itemImg;
	}

	public void setItemImg(Image itemImg) {
		this.itemImg = itemImg;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public int getxLoc() {
		return xLoc;
	}

	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getyLoc() {
		return yLoc;
	}

	public void setyLoc(int yLoc) {
		this.yLoc = yLoc;
	}
	
	/**
	 * Detecting whether player is within certain range
	 * @param p Player location
	 * @param nearDist Distance threshold
	 * @return boolean(true of false)
	 */
	public boolean isNear (Player p, int nearDist) {
		return (Math.sqrt(Math.pow(p.getX()-this.getxLoc(), 2) + Math.pow(p.getY()-this.getyLoc(), 2)) <= nearDist); 
	}

	/**
	 * Render item
	 * @param cam Camera
	 */
	public void render(Camera cam) 
    {
        this.getItemImg().drawCentered((int) this.getxLoc() - cam.getMinX(), (int) this.getyLoc() - cam.getMinY());
    }
	
	public void renderOnPanel(int x, int y) 
    {
        this.getItemImg().draw(x, y);
    }
	
}
