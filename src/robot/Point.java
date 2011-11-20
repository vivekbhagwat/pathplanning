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
	
	public Point(String line)
	{
		String[] nums = line.split("\\s");
		this.x = Double.parseDouble(nums[0]);
		this.y = Double.parseDouble(nums[1]);
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
}