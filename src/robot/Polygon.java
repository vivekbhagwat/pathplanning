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
		if vertices.size() < numVertices
		{
			vertices.add(p);
			return p;
		}
		return null;			
	}
	
	
}