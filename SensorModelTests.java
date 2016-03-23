package Robot.Localisation;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Robot.Localisation.*;

public class SensorModelTests {

	private Map totalMap;
	private PerfectSensorModel test;
	private float[][] tempMap;
	private float[][] shouldBeMap;
	private float[][] tempMap2;

	
	@Before
	public void before(){
		Point[] walls = new Point[4];
		walls[3] = new Point(2,2);
		walls[0] = new Point(1,1);
		walls[1] = new Point(3,3);
		walls[2] = new Point(4,4);
		totalMap = new Map(walls, 5, 5);
		test = new PerfectSensorModel(totalMap);

		tempMap = new float[totalMap.getHeight()][totalMap.getWidth()];
		for (int y = 0; y < totalMap.getHeight(); y++) 
		{

			for (int x = 0; x < totalMap.getWidth(); x++) 
			{
				if(!totalMap.isFreeSpace(new Point(x,y)))
				{
					tempMap[x][y] = 1/(totalMap.getWidth() * totalMap.getHeight());
				}
			}
		}
		tempMap2 = tempMap;
		shouldBeMap = new float[totalMap.getWidth()][totalMap.getHeight()];
	}
	
	@After
	public void after(){
		test = null;
	}
	
	@Test (timeout=1)
	public void tests() {
		//testing update after sensing
		
		shouldBeMap = test.updateAfterSensing(1, 2, tempMap);
		assertEquals(0.3,shouldBeMap[1][2],0.1);


		shouldBeMap = test.updateAfterSensing(1, 3, tempMap2);
		assertEquals(0.4,shouldBeMap[0][3],0.1);

	}

}
