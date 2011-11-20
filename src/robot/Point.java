package robot;

public class Point
{
	public double x;
	public double y;
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = x;
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
}