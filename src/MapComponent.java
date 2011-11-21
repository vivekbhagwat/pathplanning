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
		
		g2.drawString("Shop",100,100);
		g2.drawRect(0,0,100,100);
		ArrayList<Point> points = robotMap.nodes;
		for(int i = 0; i < points.size(); i++)
		{
			drawPoint(g2, points.get(i));
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
	
	private int transform(double val)
	{
		return 500+20*(int)val;
	}
}