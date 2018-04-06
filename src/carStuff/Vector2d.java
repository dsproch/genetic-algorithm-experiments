package carStuff;

public class Vector2d {
	public double x;
	public double y;
	
	public Vector2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public Vector2d(Vector2d a)
	{
		this.x = a.x;
		this.y = a.y;
	}
	public void multiplyBy(Vector2d a)
	{
		this.x *= a.x;
		this.y *= a.y;
	}
	public void multiplyBy(double x, double y)
	{
		this.x *= x;
		this.y *= y;
	}
	public void add(Vector2d a)
	{
		this.x += a.x;
		this.y += a.y;
	}
	public void add(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	public void subtract(double x, double y)
	{
		this.x -= x;
		this.y -= y;
	}
	public void subtract(Vector2d a)
	{
		this.x -= a.x;
		this.y -= a.y;
	}
	public static Vector2d subtract(Vector2d a, Vector2d b)
	{
		return new Vector2d(a.x - b.x, a.y - b.y);
	}
	public static Vector2d add(Vector2d a, Vector2d b)
	{
		return new Vector2d(a.x + b.x, a.y + b.y);
	}
	
	public boolean isWithin(double distance, Vector2d vec)
	{
		if(Math.sqrt(Math.pow(this.x - vec.x,2.0f) + Math.pow((this.y - vec.y), 2.0f)) <= distance)
		{
			return true;
		}
		
		
		return false;
	}
	
	public static Vector2d rotatePoint(double cx, double cy, double angle, Vector2d p)
	{
		double s = (double)Math.sin(angle);
		double c = (double)Math.cos(angle);

		// translate point back to origin:
		p.x -= cx;
		p.y -= cy;

		// rotate point
		Vector2d ret = new Vector2d(p.x * c + p.y * s, -p.x * s + p.y * c);

		// translate point back:
		
		ret.add(cx, cy);
		
		return ret;
	}
}
