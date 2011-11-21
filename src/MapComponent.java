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
				drawPoint(g2, points.get(i));
				
				if (j < points.size()-1)
				{
					drawEdge(g2, points.get(i), points.get(i+1));
				}
			}
			// Point pt = points.get(i);
			// g2.fillOval(transform(pt.x), transform(pt.y), 5, 5);
			// g2.drawString("(" + ((Double)(pt.x)).toString() + ", " + ((Double)(pt.y)).toString() + ")",
				// transform(pt.x), transform(pt.y));
		}
		ArrayList<Point> bound = robotMap.boundary;
		for(int i = 0; i < bound.size(); i++)
		{
			Point pt = bound.get(i);
			
		}
		
	}
	
	private void drawPoint(Graphics2D g2, Point pt)
	{
		g2.fillOval(transform(pt.x), transform(pt.y), 5, 5);
		g2.drawString("(" + ((Double)(pt.x)).toString() + ", " + ((Double)(pt.y)).toString() + ")",
			transform(pt.x), transform(pt.y));
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