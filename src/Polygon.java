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
	
	/* Ignores parallel lines as nulls */
	public static Point intersectLines(Point p1, Point p2, Point p3, Point p4)
	{
		double t1, t2, t3, t4, t5, t6, t7;
		t1 = p1.x - p2.x;
		t2 = p3.x - p4.x;
		t3 = p1.y - p2.y;
		t4 = p3.y - p4.y;
		t5 = p1.x*p2.y - p1.y*p2.x;
		t6 = p3.x*p4.y - p3.y*p4.x;
		t7 = t1*t4 - t3*t2;
		if(t7 == 0)
			return null;
		return new Point((t5*t2 - t1*t6)/t7, (t5*t4 - t3*t6)/t7);
	}
	
	public static Point intersectLineSegments(Point p1, Point p2, Point p3, Point p4)
	{
		double d1, d2;
		Point p = intersectLines(p1, p2, p3, p4);
		if(p == null)
			return null;
		d1 = p1.distFrom(p2);
		d2 = p3.distFrom(p4);
		// check the line intersections themselves
		if(d1 < p.distFrom(p1) || d1 < p.distFrom(p2) ||
		   d2 < p.distFrom(p3) || d2 < p.distFrom(p4))
			return null;
		return p;
	}
	
	/* do we ever intersect the polygon? */
	public boolean intersect(Point p1, Point p2)
	{
		assert vertices.size() == numVertices;
		/* for each edge, check if there's an intersection */
		Point p3, p4, inter;
		for(int i = 0; i < numVertices; i++) {
			p3 = this.vertices.get(i % numVertices).clone();
			p4 = this.vertices.get((i+1) % numVertices).clone();
			inter = intersectLineSegments(p1,p2, p3,p4);
			if(inter != null) {
				if(!inter.equal(p1) && !inter.equal(p2)) {
					return true;
				}
			}
		}
		return false;
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
			center = center.translate(vertices.get(i));
		center = center.mult(1/((double)numVertices));
		
		/* move the line segments out */
		for(i = 0; i < numVertices; i++) {
			// grab two edges
			left = this.vertices.get((i-1 + numVertices) % numVertices);
			p = this.vertices.get(i % numVertices);
			right = this.vertices.get((i+1) % numVertices);
			
			/* turn the normals right side out (center is always inside the polygon */
			normleft  = left.sub(p).perpendicular().unit();
			normright = p.sub(right).perpendicular().unit();
			if(normleft.dot(p.sub(center)) < 0)
				normleft = normleft.mult(-1.0);
			if(normright.dot(p.sub(center)) < 0)
				normright = normright.mult(-1.0);
			normleft = normleft.mult(amount);
			normright = normright.mult(amount);
			
			/* move the points out */
			eleft1 = left.translate(normleft);
			eleft2 = p.translate(normleft);
			eright1 = p.translate(normright);
			eright2 = right.translate(normright);
			
			/* find the intersection */
			p = intersectLines(eleft1, eleft2, eright1, eright2);
			assert p != null;
					
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
		for(int i = 0; i < p.vertices.size(); i++)
		{
			p.vertices.set(i, p.vertices.get(i).clone());
		}
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
	
	public Polygon makeConvex()
	{
		System.out.println(vertices);
		Polygon p = clone();
		ArrayList<Point> pts = new ArrayList<Point>();
		pts.add(new Point(0.,0.));
		pts.addAll(vertices);
		int min_y_index = 1;
		for(int i = 1; i < pts.size(); i++)
		{
			if (pts.get(min_y_index).y > pts.get(i).y)
				min_y_index = i;
		}
		swap(pts, 1, min_y_index);
		
		pts.set(0, pts.get(pts.size() - 1));
		
		int m = 2;
		for(int i = 3; i < pts.size(); i++)
		{
			System.out.println("m: " + m + " i: " + i);
			while(i < pts.size()-1 && Point.ccw(pts.get(m-1), pts.get(m), pts.get(i)) <= 0 )
			{
				if(m == 2)
				{
					swap(pts, m, i);
					i += 1;
				}
				else
				{
					m -= 1;
				}
			}
			m++;
			swap(pts, m, i);
		}
		p.vertices = new ArrayList<Point>(numVertices);
		System.out.println(m);
		System.out.println("pts:" + pts);
		for(int i = 1; i <= m; i++)
		{
			p.vertices.add(pts.get(i));
		}
		p.numVertices = p.vertices.size();
		return p;
	}
	
	//mutator
	private void swap(ArrayList<Point> pts, int i, int j)
	{
		Point temp = pts.get(i);
		pts.set(i, pts.get(j));
		pts.set(j, temp);
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