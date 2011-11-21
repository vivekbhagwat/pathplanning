import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MapComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
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
		
		g2.drawString("Shop",10,10);
	}
}