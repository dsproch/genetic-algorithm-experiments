package carStuff;

import java.awt.Point;


public class Map {
	
	private boolean[][] track;
	private boolean[][] visited;
	
	private Point start;
	private Point startDirection;
	
	private int lapsCompleted = 0;
	
	public Map()
	{
		start = new Point(0, 0);
		
		track = new boolean[5][5];
		track[0][0] = true;
		track[1][0] = true;
		track[2][0] = true;
		track[3][0] = true;
		track[4][0] = true;
		
		track[0][1] = true;
		track[4][1] = true;
		
		track[0][2] = true;
		track[4][2] = true;
		track[3][2] = true;
		track[2][2] = true;
		
		track[0][3] = true;
		track[2][3] = true;
		
		track[0][4] = true;
		track[1][4] = true;
		track[2][4] = true;
//		track[3][4] = true;
		
//				{true, true, true, true, false},
//				{true, false, false, true, false},
//				{true, false, false, true, true},
//				{true, false, false, false, true},
//				{true, true, true, true, true}
//			};
		visited = new boolean[track.length][track[0].length];
	}
	
	public static boolean[][] getTrack1() {
		boolean[][] tra = new boolean[5][5];
		tra[0][0] = true;
		tra[1][0] = true;
		tra[2][0] = true;
		tra[3][0] = true;
		tra[4][0] = true;
		
		tra[0][1] = true;
		tra[4][1] = true;
		
		tra[0][2] = true;
		tra[4][2] = true;
		tra[3][2] = true;
		tra[2][2] = true;
		
		tra[0][3] = true;
		tra[2][3] = true;
		
		tra[0][4] = true;
		tra[1][4] = true;
		tra[2][4] = true;
//		track[3][4] = true;
		
		return tra;
	}
	
	public static boolean[][] getTrack2() {
		boolean[][] tra = new boolean[5][5];
		tra[0][0] = true;
		tra[1][0] = true;
		tra[2][0] = true;
		
		tra[0][1] = true;
		tra[2][1] = true;
		
		tra[0][2] = true;
		tra[2][2] = true;
		tra[3][2] = true;
		
		tra[0][3] = true;
		tra[3][3] = true;
		
		tra[0][4] = true;
		tra[1][4] = true;
		tra[2][4] = true;
		tra[3][4] = true;
		
		return tra;
	}
	
	public void setTrack(boolean[][] track)
	{
		this.track = track;
		visited = new boolean[track.length][track[0].length];
	}
	
	public boolean[][] getTrack()
	{
		return track;
	}
	
	
	public Point getStart()
	{
		return start;
	}
	
	public boolean isPath(int x, int y)
	{
		if(x < 0 || y < 0 || x >= getTrack().length || y >= getTrack()[0].length)
		{
			return false;
		}
		return track[x][y];
	}
	
	public void visit(int x, int y)
	{
		if(x < 0 || y < 0 || x >= getTrack().length || y >= getTrack()[0].length)
		{
			return;
		}
		visited[x][y] = true;
		if(getScore() == getMaxScore())
		{
			this.lapsCompleted++;
			visited = new boolean[track.length][track[0].length];
//			System.out.println("Map::visit():: Lap Completed = " + this.lapsCompleted);
		}
	}
	
	public void reset()
	{
		this.lapsCompleted = 0;
		visited = new boolean[track.length][track[0].length];
	}
	
	public double getFullScore()
	{
		return getScore() + getMaxScore() * Math.pow(2, this.lapsCompleted) * lapsCompleted;
	}
	
	public double getMaxScore()
	{
		double sum = 0;
		for(int x=0;x<track.length;x++)
		{
			for(int y=0;y<track[0].length;y++)
			{
				if(track[x][y])
				{
					sum += 1;
				}
			}
		}
		
		return sum;
	}
	
	public double getScore()
	{
		double sum = 0;
		for(int x=0;x<visited.length;x++)
		{
			for(int y=0;y<visited[0].length;y++)
			{
				if(visited[x][y])
				{
					sum += 1;
				}
			}
		}
		
		return sum;
	}

}
