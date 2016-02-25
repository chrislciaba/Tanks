import java.awt.*;
import javax.swing.*;

/**
 * A basic object that moves
 * <p>Has an image, x and y position, and x and y velocities that can be 
 * affected by environmental forces, such as gravity
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */
public class MovingObject
{
    private Image image; /**The image of the class*/
    private double x; /**Pixels*/
    private double y; /**Pixels*/
    private double vx; /**Pixels per frame*/
    private double vy; /**Pixels per frame*/
    private final double GRAVITY = -1; /**Pixels per frame^2*/
    private final int XMAX = 1024; /**Size of bg image*/
    private final int TANKX = 40; /**Width of tank*/
    
    /**
     * Constructs a new moving object with image and position
     * @param img The image of the object
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     */
    public MovingObject(Image img, double x, double y)
    {
        image = img;
        this.x = x;
        this.y = y;
        vx = vy = 0;
    }
    
    /**
     * Constructs a new moving object with image, position, and velocity
     * @param img The image of the object
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     * @param vx The x velocity of the object
     * @param vy The y velocity of the object
     */
    public MovingObject(Image img, double x, double y, double vx, double vy)
    {
        image = img;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }
    
    /**
     * Constructs a new moving object with the image from path and position
     * @param path The path of the image for the object
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     */
    public MovingObject(String path, double x, double y)
    {
        this(new ImageIcon(path).getImage(), x, y);
    }
    
    /**
     * Constructs a new moving object with the image from path, position, and velocity
     * @param path The path of the image for the object
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     * @param vx The x velocity of the object
     * @param vy The y velocity of the object
     */
    public MovingObject(String path, double x, double y, double vx, double vy)
    {
        this(new ImageIcon(path).getImage(), x, y, vx, vy);
    }
    
    /**
     * Moves an object with no gravity
     */
    public void incrementPosition()
    {
        x += vx;
        y += vy;
    }
    
    /**
     * Moves an object with gravity 
     * <p>Environmental effects are implemented here
     */
    public void incrementGravity()
    {
        x += vx;
        y += vy;
        vy -= GRAVITY;
    }
    
    /**
     * Makes the tank fall when there is no ground below it
     * @param tanksGame Allows <code>fall()<code/> to update the image
     * @param imagePanel Allows <code>fall()<code/> to get the height of the image
     * @param groundHeightArray Allows <code>MovingObject<code/> to determine
     *                          the height of the ground
     */
    public void fall(Tanks tanksGame, ImagePanel imagePanel, int[] groundHeightArray)
    {
        double ground = 0;
        boolean done = false;
        long lastTime = System.currentTimeMillis(); 
        
        while(!done && getX() >= 0 && getX() < 1024)
        {
            ground = groundHeightArray[(int) (x + image.getWidth(null)/2)];
            long time = System.currentTimeMillis(); //The current time
            if(time - 100 > lastTime)    //Update every .1 second
            {
                lastTime = time;    
                //Reset the last time we update the object to be now
                double height = getImage().getHeight(imagePanel);
               
                if(this.getY() < ground - height)
                    this.incrementGravity();     //Augment position by velocity with gravity
                if(getY() >= ground - height)
                {
                    setY(ground - height);
                    done = true;
                }
                tanksGame.updateImage();  
                //Repaint the objects in their new positions 
            }
        }
    }
    
    /**
     * Move the object 1 pixel to the left
     */
    public void moveLeft()
    {
        x -= 1;
        if (x > XMAX - TANKX -1)
        {
            x = XMAX - TANKX - 1;
        }
    }
    
    /**
     * Move the object move pixel to the left
     * @param move The number of pixels to move left
     */
    public void moveLeft(double move)
    {
        x -= move;
        if (x < 0)
        {
        	x = 0;
        }
        else if (x > XMAX - TANKX -1)
        {
        	x = XMAX - TANKX - 1;
        }
    }
    
    /**
     * Move the object 1 pixel to the right
     */
    public void moveRight()
    {
    	moveRight(1);
    }
    
    /**
     * Move the object move pixel to the right
     * @param move The number of pixels to move right
     */
    public void moveRight(double move)
    {
        x += move;
        if (x < 0)
        {
        	x = 0;
        }
        else if (x > XMAX - TANKX)
        {
        	x = XMAX - TANKX;
        }
    }
    
    /**
     * Sets the image
     * @param img The image to set the object to
     */
    public void setImage(Image img)
    {
        image = img;
    }
    
    /**
     * Sets the position to <code>x<code/> and <code>y<code/>
     * @param x The x position to set this to
     * @param y The y position to set this to
     */
    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Sets the velocity to <code>vx<code/> and <code>vy<code/>
     * @param vx The x velocity to set this to
     * @param vy The y velocity to set this to
     */
    public void setVelocity(double vx, double vy)
    {
        this.vx = vx;
        this.vy = vy;
    }
    
    /**
     * Returns the x coordinate
     * @return <code>x
     */
    public double getX()
    {
        return x;
    }
    
    /**
     * Returns the y coordinate
     * @return <code>y
     */
    public double getY()
    {
        return y;
    }
    
    /**
     * Returns the x velocity
     * @return <code>vx
     */
    public double getVx()
    {
        return vx;
    }

    /**
     * Returns the y velocity
     * @return <code>vy
     */
    public double getVy()
    {
        return vy;
    }

    /**
     * Sets the x coordinate
     * @param x The x coordinate to set this to
     */
    public void setX(double x)
    {
    	this.x = x;
    }
    
    /**
     * Sets the y coordinate
     * @param y The y coordinate to set this to
     */
    public void setY(double y)
    {
        this.y = y;
    } 
    
    /**
     * Returns the image of this object
     * @return <code>img
     */
    public Image getImage()
    {
        return image;
    }
}