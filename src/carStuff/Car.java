package carStuff;

import java.awt.Point;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import general.GeneticAlgorithmNeuralNetwork;

public class Car {
	
	public Vector2d pos;
	public Vector2d velocity = new Vector2d(0,0);
	
	private boolean isAlive = true;
	
	public static final double SPEED = 2.5;
	public static final double DELTA_ANGLE = .05;
	
	public double angle = 0;
	
	private double score = 0;
	
	public static final int DETECT_RANGE = Renderer.TILE_SIZE - 5;
	
	private GeneticAlgorithmNeuralNetwork gann;
	private Map map;
	private int inputSize;
	private int outputSize;
	
	public Car(GeneticAlgorithmNeuralNetwork gann, Map map)
	{
		Point start = map.getStart();
		this.map = map;
		this.gann = gann;
		this.pos = new Vector2d(Renderer.TILE_SIZE*start.x + .5f*Renderer.TILE_SIZE, Renderer.TILE_SIZE*start.y + .5f*Renderer.TILE_SIZE);
		
		this.inputSize = gann.getInputSize();
		this.outputSize = gann.getOutputSize();
		
	}
	
	public Point getIndex()
	{
		Point p = new Point();
		p.x = (int)pos.x / Renderer.TILE_SIZE;
		p.y = (int)pos.y / Renderer.TILE_SIZE;
		
		if(pos.x < 0)
		{
			p.x = -1;
		}
		if(pos.y < 0)
		{
			p.y = -1;
		}
		
		return p;
	}
	
	public void update()
	{
		double speedMult = 1;
		int mag = 1;
		
		double[] inputVector = calcInputVector();
		
		//Output size 1
		if(outputSize == 1)
		{
			double val = gann.compute(inputVector)[0];
			if(val < .5)
			{
				mag = -1;
			}
		} else if (outputSize == 2) {
//			Output size 2
			double[] val = gann.compute(inputVector);
			if(val[0] < .5)
			{
				mag = -1;
			}
			if((val[0] > .5 && val[1] > .5) || (val[0] < .5 && val[1] < .5))
			{
				mag = 0;
			}
		} else if(outputSize == 3) {
			//Output size 3
			double[] val = gann.compute(inputVector);
			if(val[0] < .5)
			{
				mag = -1;
			}
			if((val[0] > .5 && val[1] > .5) || (val[0] < .5 && val[1] < .5))
			{
				mag = 0;
			}
			if(val[2] > .5)
			{
				speedMult = .5f;
			}
		}
		
		
		
//		
		

		
		this.angle += mag*DELTA_ANGLE;
		this.angle = this.angle % 360;
		
		if(angle > 270 || angle < 90)
		{
			velocity.x = (double)Math.cos(angle)*SPEED*speedMult;
		}
		else
		{
			velocity.x = (double)Math.cos(angle)*SPEED*speedMult * -1;
		}
		
		if(angle < 180)
		{
			velocity.y = (double)Math.sin(angle)*SPEED*speedMult;
		}
		else
		{
			velocity.y = (double)Math.sin(angle)*SPEED*speedMult * -1;
		}
		
		pos.x += velocity.x;
		pos.y += velocity.y;
		
//		System.out.println("Car::update():: pos = " + pos.x + " " + pos.y);
		Point index = this.getIndex();
//		System.out.println("Car::update():: index = " + index.x + " " + index.y);
		if(!map.isPath(index.x, index.y))
		{
			isAlive = false;
//			System.out.println("Car::update():: pos = " + pos.x + " " + pos.y);
		}
		else
		{
			map.visit(index.x, index.y);
//			score += Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2));
			score += 1000 - Math.pow(inputVector[0] - inputVector[inputVector.length-1], 2);			
//			for(double sub : inputVector) {
//				score += Math.pow(sub, 2);
//			}
//			score = score / Math.pow(inputVector.length, 2);

		}
			
	}
	public double getScore()
	{
		return score;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public double[] calcInputVector()
	{
		double deltaAng = Math.toRadians(180.0 / (inputSize-1));
		
		double[] input = new double[inputSize];
		
		for(int k=0; k<inputSize; k++)
		{
			double ang = (inputSize/2)*deltaAng*-1 + k*deltaAng;
			input[k] = calcDistAt(ang);
		}
				
		//5 Input
//		input[0] = calcDistAt(-90);
//		input[1] = calcDistAt(-45);
//		input[2] = calcDistAt(0);
//		input[3] = calcDistAt(45);
//		input[4] = calcDistAt(90);
		
		//7 Input
//		input[0] = calcDistAt(-90);
//		input[1] = calcDistAt(-60);
//		input[2] = calcDistAt(-30);
//		input[3] = calcDistAt(0);
//		input[4] = calcDistAt(30);
//		input[0] = calcDistAt(60);
//		input[1] = calcDistAt(90);
		
		
//		System.out.print("Car::calcInputVector():: " + "Input Vector = ");
//		for(int k=0;k<5;k++)
//		{
//			System.out.print(input[k] + " ");
//		}
//		System.out.println();
		
		
		return input;
	}
	
	public double calcDistAt(double ang)
	{
		double calcAngle = angle - ang;
		
		double y = pos.y + (Math.sin(calcAngle) * DETECT_RANGE);
		double x = pos.x + (Math.cos(calcAngle) * DETECT_RANGE);
		
//		System.out.println("Car::calcDistAt():: calcPos = " + x + " " + y);
		
		return calcDistTo(x, y);
		
	}
	
	public void renderSightLines()
	{
		double deltaAng = Math.toRadians(180.0 / (inputSize-1));

		for(int k=0; k<inputSize; k++)
		{
			double ang = (inputSize/2)*deltaAng*-1 + k*deltaAng;
			double calcAngle = angle - ang;
			
			double y = pos.y + (Math.sin(calcAngle) * DETECT_RANGE);
			double x = pos.x + (double)(Math.cos(calcAngle) * DETECT_RANGE);
			
			GL11.glColor3f(1.0f, 0.0f, 0.2f);
		    GL11.glBegin(GL11.GL_LINES);
	
		    GL11.glVertex2d(pos.x, pos.y);
		    GL11.glVertex2d(x, y);
		    GL11.glEnd();
		    
		    double detLen = calcDistTo(x, y);
		    y = pos.y + (double)(Math.sin(calcAngle) * detLen);
			x = pos.x + (double)(Math.cos(calcAngle) * detLen);
			
			GL11.glColor3f(0.0f, 1.0f, 0.2f);
		    GL11.glBegin(GL11.GL_LINES);
	
		    GL11.glVertex2d(pos.x, pos.y);
		    GL11.glVertex2d(x, y);
		    GL11.glEnd();
		    
		}
		
		
	}
	
	public double calcDistTo(double dx, double dy)
	{
		Vector2d diff = calcSightLineEnds(dx, dy);
		
		return Math.sqrt(Math.pow(diff.x, 2) + Math.pow(diff.y, 2));
	}
	
	
	
	public Vector2d calcSightLineEnds(double dx, double dy)
	{
		Point index = getIndex();
		double dist = Car.DETECT_RANGE;
		
		int x = (int)dx / Renderer.TILE_SIZE;
		int y = (int)dy / Renderer.TILE_SIZE;
		
		if(index.x != x && index.y != y && map.isPath(x, y))
		{
			if(x > index.x)
			{
				dx = dx - .1f*Renderer.TILE_SIZE;
			}
			else
			{
				dx = dx + .1f*Renderer.TILE_SIZE;
			}
			if(y > index.y)
			{
				dy = dy - .1f*Renderer.TILE_SIZE;
			}
			else
			{
				dy = dy + .1f*Renderer.TILE_SIZE;
			}
			return calcSightLineEnds(dx, dy);
		}
		if(x < 0 || y < 0 || x >= map.getTrack().length || y >= map.getTrack()[0].length || map.isPath(x, y))
		{
			return new Vector2d(dx - pos.x, dy-pos.y);
		}
		
		
		Vector2d blockPos = new Vector2d(x*Renderer.TILE_SIZE,y*Renderer.TILE_SIZE);
		Vector2d diff = new Vector2d(0,0);
		
		if(index.x > x)
		{
			blockPos.x += Renderer.TILE_SIZE;
		}
		if(index.y > y)
		{
			blockPos.y += Renderer.TILE_SIZE;
		}
		
		
		if(index.y < y)
		{
			diff.y = (dy - pos.y) - (dy % Renderer.TILE_SIZE); 
		}
		else if(index.y > y)
		{
			diff.y = Math.abs(dy - pos.y) - (Renderer.TILE_SIZE - (dy % Renderer.TILE_SIZE));
		}
		else
		{
			diff.y = Math.abs(dy - pos.y);
		}
		
		
		if(index.x < x)
		{
			diff.x = (dx - pos.x) - (dx % Renderer.TILE_SIZE); 
		}
		else if(index.x > x)
		{
			diff.x = Math.abs(dx - pos.x) - (Renderer.TILE_SIZE - (dx % Renderer.TILE_SIZE));
		}
		else
		{
			diff.x = Math.abs(dx - pos.x);
		}
		
		// (50, 150)  to  (0,1)
		
		return diff;
	}
	
	
	

}
