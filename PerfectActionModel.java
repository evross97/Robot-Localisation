package Robot.Localisation;

import java.awt.Point;

public class PerfectActionModel
{

	private Map totalMap;

	public PerfectActionModel(Map totalMap)
	{
		this.totalMap = totalMap;
	}
	/**
	 * Simulate one move forward in the current direction of the robot and return the map of probabilities once updated with the results from the 'move'
	 * 
	 * @param direction the direction of the robot
	 * @param totalMap the map of obstacles
	 * @param tempMap the map containing the probabilities for every point
	 * @param previous the previous position of the robot (could be a simulated position, i.e. the robot hasn't actually been there)
	 * @param noSteps the number of steps taken by the robot from the last position
	 * @return the temppMap containing the updated probabilities
	 */
	public float[][] movePoints (int direction, float[][] tempMap, Point previous, int noSteps) {

		float oldX = previous.x;
		float oldY = previous.y;
		boolean Obstacle = false;
		Point newPos = new Point();
		if(direction == 0)
		{
			newPos.setLocation(oldX, oldY + noSteps);
		}
		else if(direction == 1)
		{
			newPos.setLocation(oldX + noSteps, oldY);
		}
		else if(direction == 2)
		{
			newPos.setLocation(oldX, oldY - noSteps);
		}
		else if(direction == 3)
		{
			newPos.setLocation(oldX - noSteps, oldY);
		}
		
		if(totalMap.isFreeSpace(newPos))
		{
			Obstacle = true;
		}
		else
		{
			Obstacle = false;
		}
		
		
		for (int y = 0; y < totalMap.getHeight(); y++) {

			for (int x = 0; x < totalMap.getWidth(); x++) 
			{	
				Point nextPoint = new Point(getNextPoint(direction, noSteps, x, y));
				if(totalMap.isFreeSpace(nextPoint) != Obstacle)
				{
					tempMap[x][y] = updateProbability(tempMap,x,y);
				}
			}
		}
			
		return tempMap;
	}
	
	/**
	 * Calculates the next position the robot would be in given the previous x and y coordinates, the direction and the number of steps take
	 * @param direction the direction of the robot
	 * @param noSteps the number of steps taken by the robot
	 * @param x the previous x coordinate of the robot
	 * @param y the previous y coordinate of the robot
	 * @return the resulting position
	 */
	public Point getNextPoint(int direction, int noSteps, int x, int y)
	{
		Point nextPoint = new Point();
		if(direction == 0)
		{
			nextPoint = new Point(x, y + noSteps);
		}
		else if(direction == 1)
		{
			nextPoint = new Point(x + noSteps, y);
		}
		else if(direction == 2)
		{
			nextPoint = new Point(x- noSteps, y);
		}
		else if(direction == 3)
		{
			nextPoint = new Point(x, y- noSteps);
		}
		
		return nextPoint;
	}

	/**
	 * Update the probability of a given point in the map, used to get rid of any nodes which wouldn't have allowed a move in the direction of the robot
	 * @param tempMap the map of probabilities
	 * @param x the current x-coordinate
	 * @param y the current y-coordinate

	 * @return the new probability of the node
	 */
	private float updateProbability(float[][] tempMap, int x, int y) {
		
		tempMap[x][y] = 0f;
		return tempMap[x][y];
	}

}
