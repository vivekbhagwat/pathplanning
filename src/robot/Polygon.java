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
			System.out.println(p);
			vertices.add(p);
			return p;
		}
		return null;			
	}
	
	public static Point intersect(Point p1, Point p2, Point p3, Point p4)
	{
		double t1, t2, t3, t4, t5, t6, t7;
		t1 = p1.x - p2.x;
		t2 = p3.x - p4.x;
		t3 = p1.y - p2.y;
		t4 = p3.y - p4.y;
		t5 = p1.x*p2.y - p1.y*p2.x;
		t6 = p3.x*p4.y - p3.y*p4.x;
		t7 = t1*t4 - t3*t2;
		return new Point((t5*t2 - t1*t6)/t7, (t5*t4 - t3*t6)/t7);
	}
	
	public Polygon grow(double amount)
	{
		assert vertices.size() == numVertices;
		
		Polygon poly = clone();
		Point left, right, normleft, normright, p;
		Point eleft1, eleft2, eright1, eright2; // extended points
		Point center = new Point(0,0);
		int i;
		/* calculate the center of the polygon */
		for(i = 0; i < numVertices; i++)
			center.translate(vertices.get(i));
		center.mult(1/(double)numVertices);
		
		/* move the line segments out */
		for(i = 0; i < numVertices; i++) {
			// grab two edges
			left = this.vertices.get((i-1 + numVertices) % numVertices).clone();
			p = this.vertices.get(i % numVertices).clone();
			right = this.vertices.get((i+1) % numVertices).clone();
			
			/* turn the normals right side out (center is always inside the polygon */
			normleft  = left.sub(p).unit().perpendicular();
			normright = p.sub(right).unit().perpendicular();
			if(normleft.dot(center) < 0)
				normleft.mult(-1.0);
			if(normright.dot(center) < 0)
				normright.mult(-1.0);
			normleft.mult(amount);
			normright.mult(amount);
			
			/* move the points out */
			eleft1 = left.translate(normleft);
			eleft2 = p.translate(normleft);
			eright1 = p.translate(normright);
			eright2 = right.translate(normright);
			
			/* find the intersection */
			p = intersect(eleft1, eleft2, eright1, eright2);
					
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
	
	public String toString()
	{
		String s = "[Polygon ";
		for(Point p : vertices) {
			s = s + "<" + p.x + ", " + p.y + ">, ";
		}
		s = s + "]";
		return s;
	}
}