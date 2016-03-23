package Robot.Localisation;

import java.awt.Point;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) 
	{
		
		
		Point point = new Point(1,2);
		int x = point.x;
		int y = point.y;
		int direction = 0;
		Point[] walls = new Point[4];
		walls[3] = new Point(2,2);
		walls[0] = new Point(1,1);
		walls[1] = new Point(3,3);
		walls[2] = new Point(4,4);

		Map totalMap;
		totalMap = new Map(walls, 5, 5);
		float[][] fin = new float[totalMap.height][totalMap.width];
		float[][] fin1 = new float[totalMap.height][totalMap.width];
			/////////////////////
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
		
		ArrayList<Point> finall = new ArrayList<Point>();
		Localiser test = new Localiser(totalMap);
		
		fin1 = test.localise(Spaces - 1, 0, point,1);
		System.out.println("After first move: ");
		test.test(fin1);
		System.out.println("List of possible points after first move: ");
		finall = test.getHighest(fin1);
		System.out.println(finall);
		
		fin = test.localise(1, 2, new Point(1,3),1);
		fin = test.combine(fin1,fin);
		System.out.println("After second move: ");
		test.test(fin);
		System.out.println("List of possible points after second move: ");
		finall = test.getHighest(fin);
		System.out.println(finall);
	}
}

		