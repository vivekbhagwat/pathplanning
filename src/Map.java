import java.io.*;
import java.util.ArrayList;

public class Map
{
	public final double ROBOT_SIZE = 0.35; //random value?
	public ArrayList<Point> boundary, nodes;
	public ArrayList<Polygon> obstacles, originalObstacles;
	public double[][] adjacencyMatrix;
	public Point start, goal;
	
	public Map(String inputFile, Point start, Point goal)
	{
		boundary = new ArrayList<Point>();
		nodes = new ArrayList<Point>();
		obstacles = new ArrayList<Polygon>();
		this.start = start;
		this.goal = goal;
		nodes.add(start);
		nodes.add(goal);
			
		processFile(inputFile);
		assert nodes != null;
	
		adjacencyMatrix = new double[nodes.size()][nodes.size()];
		fillAdjacencyMatrix();
	}
	
	public Map(String inputFile, String goalFile)
	{
		ArrayList<Point> points = processGoalFile(goalFile);
		assert points != null;
		/* System.out.println(points); */
		Point start = points.get(0);
		Point goal  = points.get(1);
		
		/* copy paste */
		boundary = new ArrayList<Point>();
		nodes = new ArrayList<Point>();
		obstacles = new ArrayList<Polygon>();
		originalObstacles = new ArrayList<Polygon>();
		this.start = start;
		this.goal = goal;
		nodes.add(start);
		nodes.add(goal);
			
		processFile(inputFile);
		assert nodes != null;
		
		adjacencyMatrix = new double[nodes.size()][nodes.size()];
		fillAdjacencyMatrix();
		
		/* for(int i = 0; i < adjacencyMatrix.length; i++) {
			for(int j = 0; j < adjacencyMatrix.length; j++) {
				System.out.print(adjacencyMatrix[i][j]+",\t");
			}
			System.out.println();
		} */
	}
	
	private void fillAdjacencyMatrix()
	{
		for(int i = 0; i < adjacencyMatrix.length; i++)
		{
			for(int j = 0; j < adjacencyMatrix.length; j++)
			{
				Point begin = nodes.get(i);
				Point end   = nodes.get(j);
				adjacencyMatrix[i][j] = begin.distFrom(end);
				for(int k = 0; k < obstacles.size(); k++) {
					Polygon poly = obstacles.get(k);
					/* check for interior overlapping polygon crossings */ // !!!
					Point mid = begin.translate(end).mult(0.5);
					if(k > 0 && poly.interior(mid))
					{
						adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
						break;
					}
					/* check for interior crossing of polygons */
					int ind1 = poly.vertices.indexOf(begin);
					int ind2 = poly.vertices.indexOf(end);
					if(ind1 > -1 && ind2 > -1 &&
					   !(Math.abs(ind1-ind2) == 1 || Math.abs(ind1-ind2) == poly.vertices.size()-1))
					{
						adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
						break;
					}
					/* do more mundane intersection checking */
					if(poly.intersect(begin, end))
					{
						adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
						break;
					}
				}
			}
		}
		/* for(int i = 0; i < adjacencyMatrix.length; i++)
		{
			for(int j = 0; j < adjacencyMatrix.length; j++)
			{
				System.out.print(String.format("%.8g", adjacencyMatrix[i][j]) + ",\t");
			}
			System.out.println();
		} */
	}
	
	//fills in map with points from inputFile
	private void processFile(String inputFile)
	{
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(inputFile));
		} catch(FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
			return;
		}
		
		try {
			int numPolygons = Integer.parseInt(br.readLine());
			Polygon[] polygons = new Polygon[numPolygons];
			
			for(int i = 0; i < polygons.length; i++)
			{
				int numVertices = Integer.parseInt(br.readLine());
				polygons[i] = new Polygon(numVertices);				
				originalObstacles.add(new Polygon(numVertices));
				Polygon last = originalObstacles.get(originalObstacles.size()-1);
				for(int j = 0; j < numVertices; j++)
				{
					String line = br.readLine();
					polygons[i].add(new Point(line));
					last.add(new Point(line));
				}
				
				// don't grow the first one
				if(i > 0)
				{
					polygons[i] = polygons[i].makeConvex();
					polygons[i] = polygons[i].grow(ROBOT_SIZE/2);
				}
				System.out.println(polygons[i].vertices);
				
				obstacles.add(polygons[i]);
				addToMap(polygons[i]);
			}
		} catch(IOException e) {
			System.out.println(e);
			e.printStackTrace();
			return;
		} finally {
			try {
				if(br != null)
					br.close();
			}catch(IOException e)
			{
				System.out.println(e);
				e.printStackTrace();
				return;
			}
		}
	}
	
	private ArrayList<Point> processGoalFile(String goalFile)
	{
		BufferedReader br;
		ArrayList<Point> points = new ArrayList<Point>();
		
		try {
			br = new BufferedReader(new FileReader(goalFile));
		} catch(FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		
		try {
			points.add(new Point(br.readLine())); //start
			points.add(new Point(br.readLine())); //goal
		} catch(IOException e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(br != null)
					br.close();
			}catch(IOException e)
			{
				System.out.println(e);
				e.printStackTrace();
				return null;
			}
		}
		return points;
	}
	
	private void addToMap(Polygon p)
	{
		Point[] vertices = p.setOfPoints();
		
		for(int j = 0; j < vertices.length; j++)
		{
			nodes.add(vertices[j]);
		}
	}
	
}