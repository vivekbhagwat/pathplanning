import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MapComponent extends JComponent
{
	public int mapHeight, mapWidth;
	private Map robotMap;
	
	public MapComponent(Map robotMap)
	{
		this.robotMap = robotMap;
		mapHeight = 0;
		mapWidth = 0;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		mapHeight = getHeight();
		mapWidth = getWidth();
		
		ArrayList<Polygon> obstacles = robotMap.obstacles;
		for(int i = 0; i < obstacles.size(); i++)
		{
			ArrayList<Point> points = obstacles.get(i).vertices;
			for(int j = 0; j < points.size(); j++)
			{
				drawVertex(g2, points.get(j));
				System.out.println(points.get(i));
				if (j < points.size()-1)
				{
					drawEdge(g2, points.get(j), points.get(j+1));
				}
			}
			drawEdge(g2, points.get(0), points.get(points.size()-1));
			// Point pt = points.get(i);
			// g2.fillOval(transform(pt.x), transform(pt.y), 5, 5);
			// g2.drawString("(" + ((Double)(pt.x)).toString() + ", " + ((Double)(pt.y)).toString() + ")",
				// transform(pt.x), transform(pt.y));
		}
		
		drawPoint(g2, robotMap.start, 10, "Start");
		drawPoint(g2, robotMap.goal, 10, "Goal");
		
		ArrayList<Point> paths = getPossiblePaths(robotMap.start);		
		System.out.println(paths);
		drawPossiblePaths(g2, robotMap.start, paths);
				
		
		ArrayList<Point> bound = robotMap.boundary;
		for(int i = 0; i < bound.size(); i++)
		{
			Point pt = bound.get(i);
		}
		
	}
	
	//drawEdge(g2, pt, points.get(i));
	private void drawPossiblePaths(Graphics2D g2, Point pt, ArrayList<Point> paths)
	{
		for(int i = 0; i < paths.size(); i++)
			drawEdge(g2, pt, paths.get(i));
	}
	
	
	private ArrayList<Point> getPossiblePaths(Point pt)
	{
		ArrayList<Point> points = robotMap.nodes;
		ArrayList<Point> paths = new ArrayList<Point>();
		
		ArrayList<Point> boundary = robotMap.obstacles.get(0).vertices;
		
		double[][] adjMat = robotMap.adjacencyMatrix;
		
		int ind = points.indexOf(pt);
		
		for(int i = 0; i < points.size(); i++)
		{
			if(adjMat[ind][i] != Double.POSITIVE_INFINITY
				&& !boundary.contains(points.get(i)))
			{
				paths.add(points.get(i));
			}
		}
		return paths;
	}
	
	private void drawVertex(Graphics2D g2, Point pt)
	{
		drawPoint(g2, pt, 5, null);//pt.toString());
	}
	
	private void drawPoint(Graphics2D g2, Point pt, int size, String str)
	{
		g2.fillOval(transform(pt.x), transform(pt.y), size, size);
		if (str != null)
		{
			// g2.drawString("(" + ((Double)(pt.x)).toString() + ", " + ((Double)(pt.y)).toString() + ")",
			g2.drawString(str,transform(pt.x), transform(pt.y));
		}
	}
	
	private void drawEdge(Graphics2D g2, Point pt1, Point pt2)
	{
		g2.drawLine(transform(pt1.x), transform(pt1.y),
			transform(pt2.x), transform(pt2.y));
	}
	
	private int transform(double val)
	{
		return 500+20*(int)val;
	}
}