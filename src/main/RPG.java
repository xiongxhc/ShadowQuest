package main;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Main class for the Role-Playing Game engine.
 * Handles initialisation, input and rendering.
 */
public class RPG extends BasicGame
{
    /** Location of the "assets" directory. */
    public static final String ASSETS_PATH = "assets";
    /** Screen width, in pixels. */
    public static final int SCREEN_WIDTH = 800;
    /** Screen height, in pixels. */
    public static final int SCREEN_HEIGHT = 600;
    /** The world of our game */
    private World world;
    /** Panel Height, in pixels. */
    public static final int panelheight = 70;

    /** Create a new RPG object. */
    public RPG()
    {
        super("RPG Game Engine");
    }

    /** Initialise the game state.
     * @param gc The Slick game container object.
     */
    @Override
    public void init(GameContainer gc)
    throws SlickException
    {
        try {
			world = new World();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
    throws SlickException
    {
        // Get data about the current input (keyboard state).
        Input input = gc.getInput();

        // Update the player's movement direction based on keyboard presses.
        int dir_x = 0;
        int dir_y = 0;
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y += 1;
        if (input.isKeyDown(Input.KEY_UP))
            dir_y -= 1;
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x -= 1;
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x += 1;
        
        // Let World.update decide what to do with this data.
        world.update(dir_x, dir_y, delta, input);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g)
    throws SlickException
    {
        // Let World.render handle the rendering.
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
    throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new RPG());
        // setShowFPS(true), to show frames-per-second.
        app.setShowFPS(true);
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
    }
}
