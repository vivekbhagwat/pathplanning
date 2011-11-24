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
		if(t7 == 0.0f)
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
	
	/* Ray starting at p1, in direction of p2, intersecting with segment p3/p4 */
	public static Point intersectRay(Point p1, Point p2, Point p3, Point p4)
	{
		double d1, d2;
		Point p = intersectLines(p1, p2, p3, p4);
		if(p == null)
			return null;
		d2 = p3.distFrom(p4);
		// check the line intersections themselves
		if((d2 < p.distFrom(p3) || d2 < p.distFrom(p4)) || (p1.sub(p2).dot(p1.sub(p)) < 0))
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
			if(inter != null){
				if(!(inter.equal(p1) || inter.equal(p2))) {
					/* System.out.print(p1 + " ");
					System.out.print(p2 + " ");
					System.out.println(inter); */
					return true;
				}
			}
			/* allow movement along a polygon */
			if((p1.equal(p3) && p2.equal(p4)) || (p2.equal(p3) && p1.equal(p4))) {
				return false;
			}
		}
		return false;
	}
	
	/* find if a point is interior to the polygon */
	public boolean interior(Point p)
	{
		assert vertices.size() == numVertices;
		/* for each edge, check if there's an intersection */
		Point center = new Point(0.0, 0.0);
		for(int i = 0; i < numVertices; i++) {
			center = center.translate(vertices.get(i));
		}
		center = center.mult(1/(double)numVertices);
		Point dir = p.sub(center);
		System.out.println(dir);
		Point p2 = p.translate(dir);
		Point p3, p4, inter;
		int countInter = 0;
		for(int i = 0; i < numVertices; i++) {
			p3 = this.vertices.get(i % numVertices).clone();
			p4 = this.vertices.get((i+1) % numVertices).clone();
			inter = intersectRay(p,p2, p3,p4);
			if(inter != null) {
				if(inter.equal(p))
					continue;
				countInter += 1;
			}
		}
		return countInter % 2 == 1;
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
		// System.out.println(vertices);
		Polygon p = clone();
		ArrayList<Point> pts = (ArrayList<Point>)vertices.clone();
		
		//sort!!!
		for(int i = 0; i < pts.size(); i++)
		{
			for(int j = i; j < pts.size(); j++)
			{
				if(pts.get(j).x < pts.get(i).x)
					swap(pts, i, j);
				else if(pts.get(j).x == pts.get(i).x)
				{
					if(pts.get(j).y < pts.get(i).y)
						swap(pts,i,j);
				}
			}
		}
		
		ArrayList<Point> lower = new ArrayList<Point>();
		for(Point pt : pts)
		{
			while(lower.size() >= 2 && Point.ccw(lower.get(lower.size()-2), lower.get(lower.size()-1), pt ) <= 0)
				lower.remove(lower.size()-1); //pop
			lower.add(pt);
		}
		lower.remove(lower.size()-1);
		
		ArrayList<Point> upper = new ArrayList<Point>();

		//sort reverse!!!
		for(int i = 0; i < pts.size(); i++)
		{
			for(int j = i; j < pts.size(); j++)
			{
				if(pts.get(j).x > pts.get(i).x)
					swap(pts, i, j);
				else if(pts.get(j).x == pts.get(i).x)
				{
					if(pts.get(j).y > pts.get(i).y)
						swap(pts,i,j);
				}
			}
		}		

		for(Point pt : pts)
		{
			while(upper.size() >= 2 && Point.ccw(upper.get(upper.size()-2), upper.get(upper.size()-1), pt ) <= 0)
				upper.remove(upper.size()-1); //pop
			upper.add(pt);
		}
		upper.remove(upper.size()-1);
		
		lower.addAll(upper);
		
		p.vertices = (ArrayList<Point>)(lower.clone());
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