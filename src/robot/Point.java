package robot;

public class Point implements Comparable<Point>
{
	public double x;
	public double y;
	// below is mostly for the path finding algorithms
	public double dist = Double.MAX_VALUE;
	public boolean known = false;
	public Point path = null;//previous node in path
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(String line)
	{
		String[] nums = line.split("\\s");
		this.x = Double.parseDouble(nums[0]);
		this.y = Double.parseDouble(nums[1]);
	}
	
	public Point sub(Point p)
	{
		return new Point(this.x - p.x, this.y - p.y);
	}
	public Point mult(Double m)
	{
		return new Point(this.x*m, this.y*m);
	}
	public Point unit()
	{
		return this.clone().mult(1/this.distFrom(new Point(0,0)));
	}
	
	public Point perpendicular()
	{
		return new Point(this.y, -this.x);
	}
	public double dot(Point other)
	{
		return this.x*other.x + this.y*other.y;
	}
	
	public double distFrom(Point p)
	{
		return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
	}
	
	//argument acts as a translation vector
	public Point translate(Point vector)
	{
		return new Point(this.x + vector.x, this.y + vector.y);
	}
	
	public Point translate(int x, int y)
	{
		return new Point(this.x + x, this.y + y);
	}
	
	public Point clone()
	{
		return new Point(this.x, this.y);
	}
	
	@Override
	public int compareTo(Point other)
	{
		if(this.dist == other.dist)
			return 0;
		return this.dist - other.dist > 0 ? 1 : -1;
	}
	
	public String toString()
	{
		return "<Point " + x + "," + y + ">";
	}
}