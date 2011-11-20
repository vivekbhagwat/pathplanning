package robot;
import java.util.ArrayList

public class Polygon
{
	private ArrayList<Point> vertices;
	private int numVertices;
	
	public Polygon(int numVertices)
	{
		vertices = new ArrayList<Point>(numVertices);
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
		assert vertices.size() == numVertices;
		Polygon poly = clone();
		for(int i = 0; i < numVertices-1; i++) {
			poly.vertices.get(i % numVertices);
		}
		return poly;
	}
	
	public Polygon clone()
	{
		assert vertices.size() == numVertices;
		Polygon p = new Polygon(numVertices);
		p.vertices = (ArrayList<Point>)vertices.clone();
		return p;
	}
	
	public Point[] setOfPoints()
	{
		assert (vertices.size() == numVertices)
		
		Point[] pts = new Point[numVertices];
		for(int i = 0; i < vertices.size(); i++)
		{
			pts[i] = vertices.get(i);
		}
		return pts;
	}
}