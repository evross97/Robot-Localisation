package Robot.Localisation;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActionModelTests {

	private PerfectActionModel test;
	private Map totalMap;
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
		test = new PerfectActionModel(totalMap);

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
		//testing get next point
		assertSame(5,(test.getNextPoint(1,2,3,3)).x);
		assertSame(5,(test.getNextPoint(0,2,3,3)).y);
		assertSame(1,(test.getNextPoint(2,2,3,3)).x);
		assertSame(3,(test.getNextPoint(3,2,3,3)).x);
		
		//testing move points
		shouldBeMap = test.movePoints(1, tempMap, new Point(0,3), 2);
		assertEquals(0.25,shouldBeMap[2][3],0.1);

		shouldBeMap = test.movePoints(1, tempMap2, new Point(1,3), 1);
		assertEquals(0.0,shouldBeMap[0][0],0.1);
		
		

	}

}
