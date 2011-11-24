import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MapComponent extends JComponent//JScrollPane
{
	private static final long serialVersionUID = 1L;
	public int mapHeight, mapWidth;
	private Map robotMap;
	private Graphics2D g2;
	private LinkedList<Point> finalPath;
	double margins = 0.1;
	// for bounding box
	private double minx, miny, maxx, maxy;
	
	public MapComponent(Map robotMap)
	{
		this.robotMap = robotMap;
		mapHeight = 0;
		mapWidth = 0;
		this.finalPath = null;
		
		/* get a bounding box */
		ArrayList<Polygon> obstacles = robotMap.obstacles;
		// init values to sane values
		minx = obstacles.get(0).vertices.get(0).x;
		maxx = obstacles.get(0).vertices.get(0).x;
		miny = obstacles.get(0).vertices.get(0).y;
		maxy = obstacles.get(0).vertices.get(0).y;
		for(Polygon poly : obstacles)
		{
			for(Point p : poly.vertices)
			{
				if(p.x < minx)
					minx = p.x;
				if(p.x > maxx)
					maxx = p.x;
				if(p.y < miny)
					miny = p.y;
				if(p.y > maxy)
					maxy = p.y;
			}
		}
		minx -= (maxx-minx)*margins;
		maxx += (maxx-minx)*margins;
		miny -= (maxy-miny)*margins;
		maxy += (maxy-miny)*margins;
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
		
		/* drawn the obstacles */
		ArrayList<Polygon> obstacles = robotMap.obstacles;
		ArrayList<Polygon> originalObstacles = robotMap.originalObstacles;
		for(int i = 0; i < obstacles.size(); i++)
		{
			ArrayList<Point> points = obstacles.get(i).vertices;
			ArrayList<Point> originalPoints = originalObstacles.get(i).vertices;
			/* System.out.println("\n" + originalPoints);
			System.out.println(points + "\n"); */
			for(int j = 0; j < points.size(); j++)
			{
				drawVertex(g2, points.get(j));
				g2.setColor(Color.GRAY);
				drawVertex(g2, originalPoints.get(j));
				g2.setColor(Color.RED);				
				if (j < points.size()-1)
				{
					drawEdge(points.get(j), points.get(j+1));
					g2.setColor(Color.GRAY);
					drawEdge(originalPoints.get(j), originalPoints.get(j+1));
					g2.setColor(Color.RED);					
				}
			}
			drawEdge(points.get(0), points.get(points.size()-1));
			g2.setColor(Color.RED);
			drawEdge(originalPoints.get(0), originalPoints.get(originalPoints.size()-1));
			g2.setColor(Color.BLACK);			
		}
		

		drawPoint(g2, robotMap.start, 10, "Start");
		drawPoint(g2, robotMap.goal, 10, "Goal");
		
		g2.setColor(Color.BLUE);
		drawPossiblePaths();
		
		ArrayList<Point> bound = robotMap.boundary;
		for(int i = 0; i < bound.size(); i++)
		{
			Point pt = bound.get(i);
		}
		drawPath();
	}
	
	private void drawPossiblePaths()
	{
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
		drawPoint(g2, pt, 5, null);
	}
	
	private void drawPoint(Graphics2D g2, Point pt, int size, String str)
	{
		Point ptt = transform(pt);
		g2.fillOval((int)ptt.x, (int)ptt.y, size, size);
		if (str != null)
		{
			g2.drawString(str, (int)ptt.x, (int)ptt.y);
		}
	}
	
	private void drawEdge(Point pt1, Point pt2)
	{
		Point pp1 = transform(pt1);
		Point pp2 = transform(pt2);
		g2.drawLine((int)pp1.x, (int)pp1.y, (int)pp2.x, (int)pp2.y);
	}
	
	private Point transform(Point val)
	{
		double w = getWidth();
		double h = getHeight();
		// System.out.println("Width = " + w + "\tHeight = " + h);
		boolean wideWidth = (w/h) > 1;
		double dim = wideWidth ? h : w;
		return new Point((val.x-minx)/(maxx-minx)*dim, (val.y-miny)/(maxy-miny)*dim);
	}
}