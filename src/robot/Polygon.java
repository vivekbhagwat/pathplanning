package robot;
import java.util.ArrayList;

public class Polygon
{
	public ArrayList<Point> vertices;
	public int numVertices;
	
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
	
	public Polygon grow(double amount)
	{
		assert vertices.size() == numVertices;
		Polygon poly = clone();
		Point p, left, right;
		for(int i = 0; i < numVertices-1; i++) {
			p = this.vertices.get(i % numVertices);
			left = this.vertices.get((i-1) % numVertices);
			right = this.vertices.get((i+1) % numVertices);
			left = p.sub(left).unit().mult(amount);
			right = p.sub(right).unit().mult(amount);
			p = left.translate(right).translate(p);
			poly.vertices.set(i, p);
		}
		return poly;
	}
	
	@SuppressWarnings("unchecked")
	public Polygon clone()
	{
		assert vertices.size() == numVertices;
		Polygon p = new Polygon(numVertices);
		p.vertices = (ArrayList<Point>)vertices.clone();
		return p;
	}
	
	public Point[] setOfPoints()
	{
		assert (vertices.size() == numVertices);
		
		Point[] pts = new Point[numVertices];
		for(int i = 0; i < vertices.size(); i++)
		{
			pts[i] = vertices.get(i);
		}
		return pts;
	}
}