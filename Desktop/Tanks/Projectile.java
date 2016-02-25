
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A projectile that fires a single projectile with an explosion
 * radius of 25 that deals 25 damage
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */
public class Projectile extends MovingObject
{
    private final int DAMAGE = 25; /**The damage of the explosion upon impact*/
    private final int EXPLOSION_RADIUS = 25; /**The radius of the explosion upon impact*/
    private static Image img = new ImageIcon("Images/projectile.GIF").getImage();/**The image of the projectile*/
    private Tanks tanksGame;/**Allows Projectile to change the game*/
    private ImagePanel imagePanel;/**Allows Projectile to get the size of objects*/
    private int[] groundHeightArray;/**The array of heights for the ground*/
    private int lastX; /**The projectile's X coordinate from the previous frame*/
    private int lastY; /**The projectile's X coordinate from the previous frame*/

    
    /**
     * A <code>Projectile</code> is a movingObject with an x and y position
     * that explodes on contact with the ground or
     * another movingObject that is not a projectile
     * @param   x   The x coordinate of the projectile
     * @param   y   The y coordinate of the projectile
     * @param   game Allows projectile to interact with the game
     * @param   imagePanel  Allows projectile to change the image
     */
    public Projectile(double x, double y, Tanks game, ImagePanel imagePanel)
    {
        super(img, x, y);
        tanksGame = game;
        this.imagePanel = imagePanel;
        groundHeightArray = imagePanel.getHeightArray();
        SoundEffect.SHOOT.play();
    }

    /**
     * A <code>Projectile</code> is a movingObject with an x and y position
     * and vx and vy velocity that explodes on contact with the ground or
     * another movingObject that is not a projectile
     * @param   x   The x coordinate of the projectile
     * @param   y   The y coordinate of the projectile
     * @param   vx  The x velocity of the projectile
     * @param   vy  The y velocity of the projectile
     * @param   game Allows projectile to interact with the game
     * @param   imagePanel  Allows projectile to change the image
     */
    public Projectile(double x, double y, double vx, double vy, Tanks game, ImagePanel imagePanel)
    {
        super(img, x, y, vx, vy);
        tanksGame = game;
        this.imagePanel = imagePanel;
        groundHeightArray = imagePanel.getHeightArray();
        SoundEffect.SHOOT.play();
    }
    
    /**
     * Returns a positive number if there is a collision, a negative number if
     * there is a collision and the projectile should be deleted, and 0 if
     * nothing happens
     * @param   movingObjects   The arraylist of moving objects to check for collisions
     * @param   ground  The x coordinate to look for a collision at
     * @return  whether there is a collision and if it is destructive
     */
    public int collision(ArrayList<MovingObject> movingObjects, double ground)
    {
        int result = 0;
        if(getY()  >= ground)
        {
            imagePanel.changeHeight((int)getX(), EXPLOSION_RADIUS);
            SoundEffect.BOMBHIT.play();
            tanksGame.updateImage();
            result = -1;
        } 
        else
            for(int i = 0; i < movingObjects.size(); i++)
            {
                MovingObject a = movingObjects.get(i);
                if(!(a instanceof Projectile))
                {
                    if(contact(a))
                    {
                        result = -1;
                        SoundEffect.BOMBHIT.play();
                        imagePanel.changeHeight((int)getX(), EXPLOSION_RADIUS);
                        for(MovingObject t: movingObjects)
                            if(!(t instanceof Projectile) && inBlast(t))
                                if(t instanceof Tank)
                                {
                                    ((Tank)t).damage(DAMAGE);
                                    SoundEffect.TANKHIT.play();
                                }
                    }
                }
            }
        return result;
    }
    
    /**
     * Prints and moves a projectile on the screen until it collides with
     * an object or the ground.
     * @param   movingObjects   the arraylist of moving objects to add the
     *                          projectile to it and allow it to move
     */
    public void fire(ArrayList<MovingObject> movingObjects)
    {
        double ground = 0;
        
        boolean done = false;
        long lastTime = System.currentTimeMillis(); 
        
        while(!done)
        {
            long time = System.currentTimeMillis(); //The current time
            if(time - 100 > lastTime && getX() >= 0 && getX() < 1024)    //Update every .1 second
            {
                ground = groundHeightArray[(int)getX()-1];
                lastTime = time;    
                //Reset the last time we update the object to be now
                double height = getImage().getHeight(imagePanel);
               
                if(this.getY() < ground - height)
                    this.incrementGravity();     //Augment position by velocity with gravity
                if((collision(movingObjects, ground) < 0) || (this.getX() < 0 || this.getX() > 1024))
                {
                    int index = movingObjects.indexOf(this);
                    // slightly slow but reasonable due to the small size of the arraylist. Stops NullPointerException.
                    movingObjects.remove(index);
                    done = true;
                }
                if(getY() > ground - height)
                {
                    setY(ground - height);
                }
                if(lastX == (int)this.getX() && lastY == (int)this.getY())
                {
                	int index = movingObjects.indexOf(this);
                    // slightly slow but reasonable due to the small size of the arraylist. Stops NullPointerException.
                    movingObjects.remove(index);
                    done = true;
                }
                tanksGame.updateImage();
                lastX = (int)this.getX();
                lastY = (int)this.getY();
                //Repaint the objects in their new positions 
            }
            else if(getX() < 0 || getX() >= 1024)
                done = true;
        }
    }
    
    /**
     * Checks to see if this projectile comes in contact with a target
     * @param target    a movingObject to check for a collision with
     * @return  the boolean of whether this projectile comes in contact with target
     */
    public boolean contact(MovingObject target)
    {
        double x = getX();
        double y = getY();
        double tarX = target.getX();
        double tarY = target.getY();
        if(y > tarY - getImage().getHeight(imagePanel) 
           && y <= tarY + target.getImage().getHeight(imagePanel)
           && x > tarX - getImage().getWidth(imagePanel)
           && x <= tarX + target.getImage().getWidth(imagePanel))
            return true;
        return false;
    }
    
    public boolean inBlast(MovingObject target)
    {
        double x = getX();
        double tarX = target.getX();
        if(x - EXPLOSION_RADIUS > tarX - getImage().getWidth(imagePanel)
                && x - EXPLOSION_RADIUS <= tarX + target.getImage().getWidth(imagePanel))
                 return true;
        if(x + EXPLOSION_RADIUS > tarX - getImage().getWidth(imagePanel)
                && x + EXPLOSION_RADIUS <= tarX + target.getImage().getWidth(imagePanel))
                 return true;
        return false;
    }
}