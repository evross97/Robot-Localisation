package Robot.Localisation;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

// JUST A DUPLI8CATE OF GEORGE'S SO THAT I COULD USE HIS METHODS!!!


public class Map {

	boolean[][] wallsMap;
	int height;
	int width;
	HashMap<Point,Node> nodes;
	Node[][] nodesMap;
	public Random rand;
	Point[] walls;
	Point dropOffPoint = new Point(6,7);

	public Map(Point[] walls,int width,int height) {
		this.height = height;
		this.width = width;
		this.rand = new Random();
		this.walls = walls;
		
		nodesMap = new Node[height][width];
		
		initializeWallMap();
	}

	private void initializeWallMap() {
		wallsMap =  new boolean[height][width];
		for(Point wall : walls){
			wallsMap[(int) wall.getY()][(int) wall.getX()] = true;
		}
	}

	private void createNodes(Point end) {
		for (int y = 0; y <height; y++) {
			for (int x = 0; x < width; x++) {
				if(!wallsMap[y][x]) {
					Point p = new Point(x, y);
					nodesMap[y][x] = new Node(p, end);
				}
			}
		}
	}

	private void addNeighbors(Point end) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(!wallsMap[y][x]){ // if at empty junction
					findAndAddNeighbors(x,y);
				}
			}
		}
	}

	private void findAndAddNeighbors( int x,int y) {
		int[][] dirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
		Node node = nodesMap[y][x]; // get junction node
		for(int[] dir : dirs){
			tryAddNeighbor(node, dir);
		}
	}
	
	public void initializeNodeMap(Point end){
		createNodes(end);
		addNeighbors(end);
	}


	private void tryAddNeighbor(Node node, int[] dir) {
		int neighborX = node.point.x + dir[0];
		int neighborY = node.point.y + dir[1];
		
		if(isInsideGrid(neighborX,neighborY) && !wallsMap[neighborY][neighborX]){ // if neighbor exists
			node.addNeighbor(nodesMap[neighborY][neighborX]); // add it as a neighbor
		}
	}

	public boolean isInsideGrid(int x,int y){
		return x>= 0 && x < width && y>= 0 && y < height;
	}
	
	public Point getFreeSpace(){
		Point point;
		do {
			point = new Point(rand.nextInt(width), rand.nextInt(height-1));
		} while (!isFreeSpace(point));
		return point;
	}
	
	public boolean isFreeSpace(Point point){
		for(Point wall : walls){
			if(wall.equals(point)) return false;
		}
		return true;
	}
	
	public boolean[][] getMap() {
		return wallsMap;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
	class Node{

		public double gScore;
		public final double hScore;
		public double fScore = 0;
		public Node parent;
		public Point point;
		public HashMap<Node, Integer> neighbors;
		
		public Node(Point point, Point end){
			this.point = point;
			hScore = calculateHeuristic(point, end);
			this.neighbors = new HashMap<Node, Integer>();
		}
		
		public void addNeighbor(Node neighbor) {
			neighbors.put(neighbor,1);
		}
		
		public double calculateHeuristic(Point node,Point end){
			return (Math.sqrt(Math.pow(node.x-end.x,2) + Math.pow(node.y-end.y,2)));
		}
		
		public HashMap<Node, Integer> getNeighbors() {
			return neighbors;
		}
		
		public void changeWeightIntoNode(int weight){
			for(Node neighbor : neighbors.keySet()){
				neighbor.neighbors.put(this, weight);
			}
		}
		
		public void remove(){
			for(Node neighbor : neighbors.keySet()){
				neighbor.neighbors.remove(this);
			}
		}
	}
	
}