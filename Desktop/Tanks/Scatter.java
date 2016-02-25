/**
 * A class that creates ground scatter objects and images to add variety to the terrain
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */

public class Scatter extends MovingObject
{
	public Scatter(String terrain, int[] groundHeightArray, double x)
	{
		super("Images/" + terrain + "Tree.PNG", x, 0);
		int imageHeight = getImage().getHeight(null);
		setY(groundHeightArray[(int) (x + getImage().getWidth(null)/2)] - imageHeight);
	}
}