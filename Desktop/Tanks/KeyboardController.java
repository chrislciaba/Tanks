import java.awt.event.*;

/**
 * This class translates input from the keyboard to moves
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */
public class KeyboardController implements KeyListener
{
	private Tanks tanksGame; /**Allows KeyboardController to control the game*/
	
	/**
	 * Constructs a keyboardController
	 * @param game Allows keyboardController to control the game
	 */
    public KeyboardController(Tanks game) 
    {
    	tanksGame = game;
    }
    
    /**
     * Calls the method corresponding to the key that has been pressed
     * <br/> Precondition: e != null
     * @param e The KeyEvent for the key pressed
     */
    public void keyPressed(KeyEvent e)
    {
    	int keyCode = e.getKeyCode();
    	switch(keyCode)
    	{
    		case KeyEvent.VK_LEFT:	//left arrow
    			tanksGame.leftKey();
    			break;
    		case KeyEvent.VK_UP:	//up arrow
    			tanksGame.upKey();
    			break;
    		case KeyEvent.VK_DOWN:	//down arrow
    			tanksGame.downKey();
    			break;
    		case KeyEvent.VK_RIGHT:	//right arrow
    			tanksGame.rightKey();
    			break;
    		case KeyEvent.VK_ENTER: //enter
    		     tanksGame.enterKey();
    			break;
    		default:
    			// ignore other keys
    			break;
    	}
    	
    }
    
    /**
     * Tells you when the key has been released
     * <br/> Precondition: e != null
     * @param e The KeyEvent for the key pressed
     */
    public void keyReleased(KeyEvent e)
    {
    	//System.out.println("Key Released!");
    }
    
    /**
     * Tells you when a key has been typed
     * <br/> Precondition: e != null
     * @param e The KeyEvent for the key pressed
     */
    public void keyTyped(KeyEvent e)
    {
    	//System.out.println("Key Typed!");
    }
}