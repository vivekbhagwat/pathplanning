import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MapComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	public int mapHeight, mapWidth;
	private Map robotMap;
	private Graphics2D g2;
	private LinkedList<Point> finalPath;
	
	public MapComponent(Map robotMap)
	{
		this.robotMap = robotMap;
		mapHeight = 0;
		mapWidth = 0;
		this.finalPath = null;
	}
	
	public void setPath(LinkedList<Point> path)
	{
		this.finalPath = path;
	}
	
	public void paintComponent(Graphics g)
	{
		this.g2 = (Graphics2D)g;
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
					drawEdge(points.get(j), points.get(j+1));
				}
			}
			drawEdge(points.get(0), points.get(points.size()-1));
		}
		
		drawPoint(g2, robotMap.start, 10, "Start");
		drawPoint(g2, robotMap.goal, 10, "Goal");
		
		drawPossiblePaths();
				
		
		ArrayList<Point> bound = robotMap.boundary;
		for(int i = 0; i < bound.size(); i++)
		{
			Point pt = bound.get(i);
		}
		drawPath();
	}
	
	//drawEdge(g2, pt, points.get(i));
	private void drawPossiblePaths()
	{
		// for(int i = 0; i < paths.size(); i++)
			// drawEdge(g2, pt, paths.get(i));
		double[][] adjMat = robotMap.adjacencyMatrix;
		
		ArrayList<Point> points = robotMap.nodes;
		ArrayList<Point> wall = robotMap.obstacles.get(0).vertices;
		
		for(int i = 0; i < adjMat.length; i++)
		{
			for(int j = 0; j < adjMat.length; j++)
			{
				if(adjMat[i][j] != Double.POSITIVE_INFINITY)
				{
					if(!wall.contains(points.get(i)) && !wall.contains(points.get(j)))
						drawEdge(points.get(i), points.get(j));
				}
			}
		}
	}
	
	private void drawPath()
	{
		g2.setColor(Color.CYAN);
		Iterator<Point> it = this.finalPath.iterator();
		Point p2 = it.next();
		while(it.hasNext())
		{
			Point p1 = p2;
			if(it.hasNext())
			{
				p2 = it.next();			
				System.out.println("Drawing edge from " + p1 + " to " + p2);
				drawEdge(p1,p2);
			}
		}
		g2.setColor(Color.BLACK);
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
	
	private void drawEdge(Point pt1, Point pt2)
	{
		g2.drawLine(transform(pt1.x), transform(pt1.y),
			transform(pt2.x), transform(pt2.y));
	}
	
	private int transform(double val)
	{
		return 500+20*(int)val;
	}
}