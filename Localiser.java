package Robot.Localisation;

import java.awt.Point;
import java.util.ArrayList;

public class Localiser {

	private PerfectSensorModel sensorModel;
	private PerfectActionModel actionModel;
	private float[][] tempMap;
	private Map totalMap;
	private float[][] tempMapOld;
	private float[][] tempMapNew;
	private int noTimes = 0;
	private Point shouldBe;
	
	/**
	 * Constructor to create the sensor and action models and the map showing the probabilities of the robot being in each position on the map
	 * @param map the map showing all the obstacles
	 */
	public Localiser(Map map)
	{
		this.totalMap = map;
		this.tempMap = new float[totalMap.getWidth()][ totalMap.getHeight()];
		this.tempMapOld = new float[totalMap.getWidth()][ totalMap.getHeight()];
		this.tempMapNew = new float[totalMap.getWidth()][ totalMap.getHeight()];
		this.sensorModel = new PerfectSensorModel(totalMap);
		this.actionModel = new PerfectActionModel(totalMap);
		
	}
	/**
	 * For testing, prints out the map of probabilities
	 */
	public void test(float[][] map)
	{
		for(int i = 0; i < totalMap.getHeight(); i++)
		{
			for(int j = 0; j < totalMap.getWidth(); j++)
			{
				System.out.print(map[j][i] + "   ");
			}
			System.out.println();
		}

	}
	
	public void test2(boolean [][] map)
	{
		for(int i = 0; i < totalMap.getHeight(); i++)
		{
			for(int j = 0; j < totalMap.getWidth(); j++)
			{
				System.out.print(map[j][i] + "   ");
			}
			System.out.println();
		}

	}
	
	/**
	 * Called every time the robot should re-localise
	 * 
	 * @param distFromWall the robot's current distance to the wall
	 * @param direction the direction that the robot is facing - north is 0, east is 1 etc.
	 * @param previous the previous position of the robot
	 * @return the array of probabilities for the robot's position
	 */
	public float[][] localise(int distFromWall, int direction, Point previous, int noSteps)
	{
		shouldBe = actionModel.getNextPoint(direction, noSteps, previous.x, previous.y);

		tempMap = new float[totalMap.getWidth()][ totalMap.getHeight()];
		tempMap = setInitial();
		//test2(totalMap.getMap());
		//System.out.println("Previous " + previous + "Distance from Wall " + distFromWall);
		tempMap = this.sensorModel.updateAfterSensing(distFromWall, direction, tempMap);
		//System.out.println("After sensor model: ");
		//test(tempMap);
		tempMap = setFinalProbability();
		//System.out.println("After sensor model probs");
		//test(tempMap);
		tempMap = this.actionModel.movePoints(direction, tempMap, previous, noSteps);
		//System.out.println("After action model ");
		//test(tempMap);
		tempMap = setFinalProbability();
		//System.out.println("After action model probs");
		//test(tempMap);
		previous = actionModel.getNextPoint(direction, noSteps, previous.x, previous.y);
		distFromWall --;
		//System.out.println("Previous " + previous + "Distance from Wall " + distFromWall);
		
	
		while(getHighestProb() < 0.8 && distFromWall > 0 && totalMap.isFreeSpace(previous))
		{
			tempMapOld = clone(tempMapOld,tempMap);
			tempMap = setInitial();
			tempMap = this.sensorModel.updateAfterSensing(distFromWall, direction, tempMap);
			//System.out.println("After sensor model: ");
			//test(tempMap);
			tempMap = setFinalProbability();
			//System.out.println("After sensor model probs");
			//test(tempMap);
			tempMap = this.actionModel.movePoints(direction, tempMap, previous, noSteps);
			//System.out.println("After action model ");
			//test(tempMap);
			tempMap = setFinalProbability();
			//System.out.println("After action model probs");
			//test(tempMap);
			previous = actionModel.getNextPoint(direction, noSteps, previous.x, previous.y);
			distFromWall --;
			//System.out.println("Previous " + previous + "Distance from wall " + distFromWall);
			
			noTimes++;
			Point newPoint = new Point();
			for(int y = 0; y < totalMap.getHeight(); y++)
			{
				for(int x = 0; x < totalMap.getWidth(); x++)
				{
					if(direction == 0)
					{
						newPoint.setLocation(x, y + noTimes);
						if(totalMap.isInsideGrid(newPoint.x, newPoint.y) && totalMap.isFreeSpace(newPoint) && totalMap.isFreeSpace(new Point(x,y)))
						{
							tempMapNew[x][y] = tempMap[x][y + noTimes];
						}
					}
					else if(direction == 1)
					{
						newPoint.setLocation(x + noTimes, y);
						if(totalMap.isInsideGrid(newPoint.x, newPoint.y) && totalMap.isFreeSpace(newPoint))
						{
							tempMapNew[x][y] = tempMap[x + noTimes][y];
						}
					}
					else if(direction == 2)
					{
						newPoint.setLocation(x - noTimes, y);
						if(totalMap.isInsideGrid(newPoint.x, newPoint.y) && totalMap.isFreeSpace(newPoint))
						{
							tempMapNew[x][y] = tempMap[x - noTimes][y];
						}
					}
					else if(direction == 3)
					{
						newPoint.setLocation(x, y - noTimes);
						if(totalMap.isInsideGrid(newPoint.x, newPoint.y) && totalMap.isFreeSpace(newPoint))
						{
							tempMapNew[x][y] = tempMap[x][y - noTimes];
						}
					}
				}
			}
			tempMap = combine(tempMapOld,tempMapNew);
			tempMap = setFinalProbability();
			//System.out.println("After combine");
			//test(tempMap);
			//System.out.println("Highest" + getHighestProb());
			
		}
		return tempMap;
		
	}
	
	private float[][] clone(float[][] newMap, float[][] oldMap) {
		for(int i = 0; i < totalMap.getHeight(); i++)
		{
			for(int j = 0; j < totalMap.getWidth(); j++)
			{
				newMap[j][i] = oldMap[j][i];
			}
		}
		return newMap;
	}

	public float[][] combine(float[][] Old, float[][] New) {
		for(int i = 0; i < totalMap.getHeight(); i++)
		{
			for(int j = 0; j < totalMap.getWidth(); j++)
			{
				New[j][i] = New[j][i] + Old[j][i];
				
			}
		}
		
		float p = 0;
		for(int y = 0; y < totalMap.getHeight(); y++)
		{
			for(int x = 0; x <totalMap.getWidth(); x++)
			{
				p = p + New[x][y];			
			}
		}
		for(int y = 0; y < totalMap.getHeight(); y++)
		{
			for(int x = 0; x < totalMap.getWidth(); x++)
			{
				if(totalMap.isFreeSpace(new Point(x,y)))
				{
					New[x][y] = New[x][y]/p;
				}
			}
		}
		
		return New;
	}
	
	/**
	 * Set all the free spaces in the map to have the same probability - make sure they are all uniformly distributed
	 * @return the map with the initial probabilities
	 */
	private float[][] setInitial() {
		
		int freeSpaces = 0;
		
		
		for (int y = 0; y < totalMap.getHeight(); y++) 
		{

			for (int x = 0; x < totalMap.getWidth(); x++) 
			{
				if(totalMap.isFreeSpace(new Point(x,y)))
				{
					freeSpaces++;
				}
			}
		}
		//System.out.println(freeSpaces);
		
		for (int y = 0; y < totalMap.getHeight(); y++) 
		{

			for (int x = 0; x < totalMap.getWidth(); x++) 
			{
				if(totalMap.isFreeSpace(new Point(x,y)))
				{
					tempMap[x][y] = 1/freeSpaces;
				}
			}
		}
		return tempMap;
		
	}

	/**
	 * Finds the points in the map of probabilities with the highest value
	 * @param map the 2d array
	 * @return a list of those points
	 */
	public ArrayList<Point> getHighest(float[][] map) {
		
		float probability = 0;
		ArrayList<Point> highest = new ArrayList<Point>();
		for(int x = 0; x < map.length; x++)
		{
			for(int y = 0; y < map.length; y++)
			{
				if(map[x][y] > probability)
				{
					probability = tempMap[x][y];
				}
			}
		}
		for(int x = 0; x < map.length; x++)
		{
			for(int y = 0; y < map.length; y++)
			{
				if (map[x][y] == probability)
				{
					highest.add(new Point(x,y));
				}
			}
		
		}
		return highest;
	}

	/**
	 * Finds the value of the point in the map with the highest probability
	 * @return the value of that point
	 */
	private float getHighestProb() 
	{
		Point highest = new Point(0,0);
		float probability = 0;
		for(int y = 0; y < tempMap.length; y++)
		{
			for(int x = 0; x < tempMap.length; x++)
			{
				if(tempMap[x][y] > probability)
				{
					probability = tempMap[x][y];
					highest.setLocation(x, y);
				}
			}
		}
		return probability;
	}
	
	/**
	 * Redistributes the probabilities once the algorithm has traversed through all the points and updated them

	 * @return the tempMap of probabilities once updated
	 */
	public float[][] setFinalProbability() 
	{
			
		float p = 0;
		for(int y = 0; y < totalMap.getHeight(); y++)
		{
			for(int x = 0; x <totalMap.getWidth(); x++)
			{
				p = p + tempMap[x][y];			
			}
		}
		for(int y = 0; y < totalMap.getHeight(); y++)
		{
			for(int x = 0; x < totalMap.getWidth(); x++)
			{
				if(totalMap.isFreeSpace(new Point(x,y)))
				{
					tempMap[x][y] =tempMap[x][y]/p;
				}
			}
		}
		return tempMap;

			
	}
}
