package Robot.Localisation;

import java.awt.Point;

public class PerfectSensorModel {
	
	private Map totalMap;

	public PerfectSensorModel(Map totalMap)
	{
		this.totalMap = totalMap;
	}
	/**
	 * Calculates the initial uniform distribution based on the number of free spaces in the map
	 * Uses the actual number of points that the robot is from an obstacle and compares this to how many points 
	 * each node on the map is away from the nearest obstacle in the same direction
	 * It then decreases or increases the probability of that node accordingly
	 * @param distFromWall the number of junctions between the robot and the nearest obstacle
	 * @param direction the direction the robot is facing
	 * @param totalMap the original map showing obstacles
	 * @param tempMap the map showing the probability of the robot being at each point
	 * 
	 * @return tempMap updated
	 * 
	 */
	public float[][] updateAfterSensing(int distFromWall, int direction, float[][] tempMap) 
	{
		
		for (int y = 0; y < totalMap.getHeight(); y++) {

			for (int x = 0; x < totalMap.getWidth(); x++) {

				// make sure to respect obstructed grid points
				if (totalMap.isFreeSpace(new Point(x,y))) 
				{
					int thisDist = getDist(x,y, direction);
					
					if(thisDist == distFromWall)
					{
						
						tempMap[x][y] = updateProbability(tempMap,x,y);
						
					}
				}
			}
		}
			
		return tempMap;
	}

	/**
	 * Update the probability of a given point in the map
	 * @param tempMap the map of probabilities
	 * @param x the current x-coordinate
	 * @param y the current y-coordinate
	 * @param value how the probability should be changed
	 * @return the new probability of the node
	 */
	private float updateProbability(float[][] tempMap, int x, int y) {
		
		tempMap[x][y] = 1;
		return tempMap[x][y];
	}

	/**
	 * Calculate the number of points between the given position and the nearest obstacle
	 * @param x the current x-coordinate
	 * @param y the current y-coordinate
	 * @param direction the direction of the actual robot
	 * @param totalMap to map of obstacles
	 * @return the number of points between the position and the nearest obstacle
	 */
	private int getDist(int x, int y, int direction) {
		int Spaces = 0;
		if(direction == 0)
		{
			
			Point nextPoint = new Point(x, y++);
			while(totalMap.isFreeSpace(nextPoint) && totalMap.isInsideGrid(nextPoint.x, nextPoint.y))
			{
				Spaces++;
				nextPoint.setLocation(x, y++);
			}
		}
		if(direction == 1)
		{
			Point nextPoint = new Point(x++, y);
			while(totalMap.isFreeSpace(nextPoint) && totalMap.isInsideGrid(nextPoint.x, nextPoint.y))
			{
				Spaces++;
				nextPoint.setLocation(x++, y);
			}
		}
		if(direction == 2)
		{
			Point nextPoint = new Point(x--, y);
			while(totalMap.isFreeSpace(nextPoint) && totalMap.isInsideGrid(nextPoint.x, nextPoint.y))
			{
				Spaces++;
				nextPoint.setLocation(x--, y);
			}
		}
		if(direction == 3)
		{
			Point nextPoint = new Point(x, y--);
			while(totalMap.isFreeSpace(nextPoint) && totalMap.isInsideGrid(nextPoint.x, nextPoint.y))
			{
				Spaces++;
				nextPoint.setLocation(x, y--);
			}
		}
		return Spaces;
	}
}
