package Robot.Localisation;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocaliserTests {

	private Localiser test;
	private Map totalMap;

	@Before
	public void before(){
		
		Point[] walls = new Point[4];
		walls[3] = new Point(2,2);
		walls[0] = new Point(1,1);
		walls[1] = new Point(3,3);
		walls[2] = new Point(4,4);
		totalMap = new Map(walls, 5, 5);
		test = new Localiser(totalMap);
	}
	
	@After
	public void after(){
		test = null;
	}
	
	@Test (timeout=1)
	public void tests() {
		
		//testing localise
		assertSame(new Point(2,1), test.localise(1, 0, new Point(2,0),1));
		assertSame(new Point(2,3), test.localise(1, 1, new Point(1,3),1));
		assertSame(new Point(2,3), test.localise(1, 2, new Point(2,4),1));
		assertSame(new Point(3,1), test.localise(2, 3, new Point(4,1),1));


	}

}
