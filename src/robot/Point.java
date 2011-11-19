package robot;

public class Point
{
	public int x;
	public int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = x;
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
}