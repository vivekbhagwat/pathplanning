package robot;

public class Polygon
{
	private java.util.ArrayList<Point> vertices;
	private int numVertices;
	
	public Polygon(int numVertices)
	{
		vertices = new java.util.ArrayList<Point>(numVertices);
		this.numVertices = numVertices;
	}
	
	public Point add(Point p)
	{
		if (vertices.size() < numVertices)
		{
			vertices.add(p);
			return p;
		}
		return null;			
	}
	
	public Polygon grow()
	{
		if (vertices.size() == numVertices)
		{
			
		}
	}
	
	public Point[] setOfPoints()
	{
		if (vertices.size() == numVertices)
		{
			Point[] pts = new Point[numVertices];
			for(int i = 0; i < vertices.size(); i++)
			{
				pts[i] = vertices.get(i);
			}
			return pts;
		}
		return null;
	}
}