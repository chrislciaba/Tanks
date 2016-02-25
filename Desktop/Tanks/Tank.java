import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A tank with 100 health and 500 pixels of move distance
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */
public class Tank extends MovingObject
{
    private int moveDistance; /**The maximum amount of distance able to be moved each turn*/
    private String name;/**The name of the tank*/
    private int pointDirection; /**The direction the tank barrel faces*/
    private static Image img = new ImageIcon("Images/Tank/0.GIF").getImage(); 
                        /**The default barrel position, pointing to the right*/
    private int healthRemaining; /**The health of the tank*/
    private int maxHealth; /**The maximum health a tank can have*/
    private int[] groundHeightArray; /**The array of heights of the ground*/
    private final static int XMAX = 1024; /**The size of background image*/
    private final int TANKX = img.getWidth(null); /**The width of tank, should be 60*/
    
    /**
     * Constructs a tank
     * @param name The name of the tank
     * @param x The x coordinate of the tank
     * @param heights The array of ground heights
     */
    public Tank(String name, double x, int[] heights)
    {
        super(img, x % XMAX, 0);
        this.name = name;
        pointDirection = 0;
        moveDistance = 500;
        setHealth(100);
        groundHeightArray = heights;
        
        setY(chooseY(x % XMAX));
    }

    /**
     * Constructs a tank
     * @param name The name of the tank
     * @param x The x coordinate of the tank
     * @param vx The x velocity of the tank
     * @param vy The y velocity of the tank
     * @param heights The array of ground heights
     */
    public Tank(String name, double x, double vx, double vy, int[] heights)
    {
        super(img, x % XMAX, 0, vx, vy);
        this.name = name;
        pointDirection = 0;
        moveDistance = 500;
        setHealth(100);
        groundHeightArray = heights;

        setY(chooseY(x % XMAX));
    }
    
    /**
     * Moves the tank right, keeping it in bounds
     * @param move The number of pixels to move right
     */
    public void moveRight(double move)
    {
        double x = this.getX();
        x += move;
        if (x < 0)
        {
            x = 0;
        }
        else if (x > XMAX - TANKX - 1)
        {
            x = XMAX - TANKX - 1;
        }
        setX(x);
        setY(chooseY(x));
    }
    
    /**
     * Moves the tank left, keeping it in bounds
     * @param move The number of pixels to move left
     */
    public void moveLeft(double move)
    {
        double x = this.getX();
        x -= move;
        if (x < 0)
        {
            x = 0;
        }
        else if (x > XMAX - TANKX)
        {
            x = XMAX - TANKX;
        }
        setX(x);
        setY(chooseY(x));
    }
    
    /**
     * Returns the tank's name
     * @return <code>name<code/>
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the tank's name
     * @param name The name to change <code>name<code/> to
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Sets the angle of the tank's barrel
     * @param dir the angle to set the tank to
     */
    public void setPoint(int dir)
    {
        pointDirection = dir;
    }
    
    /**
     * Returns the angle of the tank's barrel
     * @return <code>pointDirection
     */
    public int getPoint()
    {
        return pointDirection;
    }
    
    /**
     * Returns maximum distance to be traveled each turn
     * @return <code>moveDistance
     */
    public int getMoveDistance()
    {
        return moveDistance;
    }
    
    /**
     * Returns the health of the tank
     * @return <code>healthRemaining
     */
    public double getHealth()
    {
        return (double)healthRemaining/maxHealth;
    }
    
    /**
     * Sets <code>healthRemaining<code/> and <code>maxHealth<code/> to <code>health<code/>
     * @param health The desired health of the tank
     */
    public void setHealth(int health)
    {
        healthRemaining = maxHealth = health;
    }
    
    /**
     * Lowers the health count of the tank
     * @param damage The amount of damage done to the tank
     */
    public void damage(int damage)
    {
        healthRemaining -= damage;
    }
    
    /**
     * Chooses and returns the midpoint of the tank for the y value
     * @param x The x coordinate of the tank
     * @return The y coordinate where the tank belongs
     */
    public double chooseY(double x)
    {
        return groundHeightArray[(int)x + (TANKX/2)] - 39;
    }
    
    /**
     * Adjusts the angle of the tank's barrel
     * <br/>Precondition: shift must be an int, in degrees
     * @param shift The angle to shift the barrel
     */
    public void changePoint(int shift)
    {
        pointDirection += shift;
        
        if (pointDirection < 0)
        {
            pointDirection = 0;
        }
        else if (pointDirection > 180)
        {
            pointDirection = 180;
        }
        super.setImage(new ImageIcon("Images/Tank/" + pointDirection + ".GIF").getImage());
    }
}