import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

/**
 * The main class to run tanks
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabattoni
 */


public class Tanks
{
    private JFrame window;
    private ImagePanel imagePanel;
    private ArrayList<MovingObject> movingObjects; // players must be the first entries
    private double xMax;
    private int[] groundHeightArray;
    // Remember, the axis is down and right, not up and to the right
    private int currentObject;
    private int numPlayers;
    private boolean winner;
    private int moveDistanceRemaining;
    private int playersTurn;
    
    /**
     * Makes a new Game of Tanks
     * <p>
     * initializes the window, makes tanks, then 
     * starts the game
     * @throws InterruptedException
     */
    public Tanks() throws InterruptedException
    {
    	currentObject = 0;
    	winner = false;
        movingObjects = new ArrayList<MovingObject>();
        start();
        moveDistanceRemaining = 0;
        playersTurn = 0;
    } 
    
    /**
     * Calls methods to start game
     * @throws InterruptedException
     */
    public void start() throws InterruptedException
    {
    	String terrainType = init();
    	setNumPlayers();
    	placeTanks(terrainType);
    	while(!winner)
    		runGame();
    	if(numPlayers > 0)
    	    JOptionPane.showMessageDialog(null,
                    ((Tank)movingObjects.get(0)).getName() + " wins!",
                    "We have a winner!", JOptionPane.PLAIN_MESSAGE);
    	else
    	    JOptionPane.showMessageDialog(null,
                    "No one wins!",
                    "Tie", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Initializes the window with the background
     */
    public String init()
    {
        String terrainType = "";
        int terrainTypeSeed = (int) (Math.random() * 3);
        if(terrainTypeSeed==0)
        {
        	terrainType = "Grass";
        }
        else if(terrainTypeSeed==1)
        {
        	terrainType = "Snow";
        }
        else
        {
        	terrainType = "Desert";
        }
    	imagePanel = new ImagePanel("Images/" + terrainType + "bkg.JPG", "Images/" + terrainType + ".JPG", this);
        Dimension d = imagePanel.getSize();
        xMax = d.getWidth();
        
        groundHeightArray = imagePanel.getHeightArray();
        window = new JFrame("Tanks");
        
        window.setSize((int)d.getWidth(), (int)d.getHeight() + 20);
        window.add(imagePanel);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Passes a reference to the current Tanks game to the KeyboardController 
        // which allows keypresses to actually change the game
        window.addKeyListener(new KeyboardController(this));
        SoundEffect.MUSIC.loopMusic();
        return terrainType;
    }
    
    
    /**
     * Adds numPlayers tanks to the field as well as some terrain scatters
     */
    public void placeTanks(String terrainType)
    {
        //adds the tanks
    	for(int i = 0; i < numPlayers; i++)
        {
            double tankX = Math.random() * (xMax - 31);
            String name = JOptionPane.showInputDialog("Please enter a name for player " + (i + 1));
            if (name == null || name.equals(""))
            {
            	name = "Player " + (i+1);
            }
            movingObjects.add(new Tank(name, tankX, groundHeightArray));
        }
        
    	//adds the scatters
        for(int numScatter = 0; numScatter <= ((Math.random()*8)+2); numScatter++)
        {
        	movingObjects.add(new Scatter(terrainType, groundHeightArray, (Math.random() * (xMax - 20) + 10)));
        }
                
        imagePanel.updateImages(movingObjects);
    }
   
    /**
     * Moves an object to the left
     */
    public void leftKey()
    {
    	int distance = 10; // distance in pixels to move for each keypress
    	if (moveDistanceRemaining >= distance)
    	{
    		movingObjects.get(currentObject).moveLeft(distance);
    		imagePanel.updateImages(movingObjects);
    		moveDistanceRemaining -= 10;
    	}
    }
    
    /**
     * Moves an object to the right
     */
    public void rightKey()
    {
    	int distance = 10; // distance in pixels to move for each keypress
    	if (moveDistanceRemaining >= distance)
    	{
    		movingObjects.get(currentObject).moveRight(distance);
    		imagePanel.updateImages(movingObjects);
    		moveDistanceRemaining -= 10;
    	}
    }
    
    /**
     * Moves the tank barrel left
     */
    public void upKey()
    {
    	MovingObject object = movingObjects.get(currentObject);
    	if (object instanceof Tank)
    	{
    		((Tank)object).changePoint(10);
    	}
    	imagePanel.updateImages(movingObjects);
    }
    
    /**
     * Moves the tank barrel right
     */
    public void downKey()
    {
    	MovingObject object = movingObjects.get(currentObject);
    	if (object instanceof Tank)
    	{
    		((Tank)object).changePoint(-10);
    	}
    	imagePanel.updateImages(movingObjects);
    }
    
    /**
     * Notifies <code>runGame()</code> to fire
     */
    public void enterKey()
    {
        synchronized(this)
        {
            notifyAll();
        } 
    }
    
    /**
     * Returns the health of player
     * @param player The index of the desired tank
     * @return The health of the tank in decimal form (out of 1)
     */
    public double getHealth(int player)
    {
        if(player < numPlayers && movingObjects.get(player) instanceof Tank)
            return ((Tank) movingObjects.get(player)).getHealth();
        else 
            return 0;
    }
    
    /**
     * Fires projectile in the direction selected
     * @throws InterruptedException
     */
    public void fire() throws InterruptedException
    {
        synchronized(this)
        {
        	boolean done = false;
        	String power = "";
        	while(!done)
        	{
                power = JOptionPane.showInputDialog("Please enter firing strength");
                if(!(power == null || !anInt(power)))
                    done = true;
                else
                    wait();
        	}

            int firingStrength = Integer.parseInt(power);
            Tank tank = (Tank) movingObjects.get(currentObject);
            int tankPoint = tank.getPoint();
            double vy = -firingStrength*Math.sin(Math.PI*tankPoint/180);
            double vx = firingStrength*Math.cos(Math.PI*tankPoint/180);
            int x = 0;
            int y = (int)tank.getY() - 3;
            if(tankPoint == 90)
            {
                x = (int)tank.getX() + 30;
                y -= 1;
            }
            else if(tankPoint < 90)
                x = (int)tank.getX() + 63;
            else
                x = (int)tank.getX() - 3;
            Projectile projectile = new Projectile(x, y, vx, vy, this, imagePanel);
            movingObjects.add(projectile);
            projectile.fire(movingObjects);
            	
        }
    }
    
    /**
     * Starts and runs the game
     * @throws InterruptedException
     */
    private void runGame() throws InterruptedException
    {   
        synchronized(this)
        {
            while(numPlayers > 1)
            {
                JOptionPane.showMessageDialog(null, ((Tank)movingObjects.get(playersTurn)).getName() + "'s turn", "", JOptionPane.PLAIN_MESSAGE);
                moveDistanceRemaining = ((Tank)movingObjects.get(currentObject)).getMoveDistance();
                
                wait();
                fire();
                
                int countMax = movingObjects.size();
                for (int count = 0; count < countMax; count++)
                {
                    MovingObject object = movingObjects.get(count);
                    object.fall(this, imagePanel, groundHeightArray);
                    if (object instanceof Tank)
                    {	
                        if (((Tank)object).getHealth() == 0)
                        {
                        	SoundEffect.TANKDIE.play();
                        	JOptionPane.showMessageDialog(null, 
                        	        ((Tank)object).getName() + " has been defeated!",
                        	        "Player eliminated!",
                        	        JOptionPane.PLAIN_MESSAGE);
                            movingObjects.remove(object);
                            count--;
                            countMax--;
                            numPlayers--;
                        }
                    }
                }
                
                playersTurn++;
                if(numPlayers > 0)
                {
                    playersTurn = playersTurn % numPlayers;
                    currentObject = playersTurn;
                }
            }
            winner = true;
        }
    }
    
    /**
     * Basic main statement to run the game
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException
    {
        Tanks theGame = new Tanks();
    }
    
    /**
     * Updates the image (for use in other classes that lack access to imagePanel)
     */
    public void updateImage()
    {
        imagePanel.updateImages(movingObjects);
    }
    
   
    
    /**
     * Returns the number of players
     * @return the numPlayers
     */
    public int getNumPlayers()
    {
        return numPlayers;
    }

    /**
     * Creates a dialogue to get the number of players
     * <p>There must be between two and four player, and an empty entry
     * will be interpreted as two players
     * @param numPlayers the numPlayers to set
     */
    public void setNumPlayers()
    {
        String newNum = "";
        while(newNum == null || !anInt(newNum))
        {
            newNum = JOptionPane.showInputDialog("Please enter the number of players");
            if(newNum.equals(""))
            {
                numPlayers = 2;
                return;
            }
        }
        numPlayers = Integer.parseInt(newNum);
    }

    /**
     * Checks to see is <code>newNum<code/> is an int
     * @param newNum A string to check if it is an int
     * @return Whether <code>newNum<code/> is an int
     */
    private boolean anInt(String newNum)
    {
        try
        {
            Integer.parseInt(newNum);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }

	public String getName(int i) {
		return ((Tank)(movingObjects.get(i))).getName();
	}
} 