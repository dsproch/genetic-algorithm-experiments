package carStuff;

import java.util.Random;

import org.lwjgl.opengl.Display;

import general.FitnessEvaluator;
import general.GeneticAlgorithmNeuralNetwork;

public class CarEvaluator implements FitnessEvaluator {
	
	private static boolean firstRun = true;
	public boolean showGraphics = true;
	
	private boolean limitTime = true;
	private static final long MAX_TICKS = 1500; // 5000;
	
	private static GeneticAlgorithmNeuralNetwork bestEverGann;
	private static double bestEverScore = 0;
	
	private Map map = new Map();
	private Random gen = new Random();
	
	private boolean randomMap = false;

	@Override
	public double evaluate(GeneticAlgorithmNeuralNetwork gann) {
		// TODO Auto-generated method stub
		if(randomMap && gen.nextBoolean()) {
			map.setTrack(Map.getTrack1());
		} else if(randomMap) {
			map.setTrack(Map.getTrack2());
		}
		
		map.reset();
		Car car = new Car(gann, map);
		
		
		
		Renderer renderer = new Renderer(map, car);
		
		if(firstRun)
		{
			renderer.init();
			firstRun = false;
		}
		
		boolean running = true;
		long currTicks = 0;
		
		while(!Display.isCloseRequested() && running)
		{
			currTicks++;
//			System.out.println("CurrTicks = " + currTicks);
			
			if(showGraphics)
			{
				renderer.renderMap();
				renderer.renderCar();
			}
			car.update();
			
//			System.out.println("CarEvaluator::evaluate():: " + map.getScore() + " " + car.isAlive() + " (" + car.getIndex().x + ", " + car.getIndex().y + ")");
			
			
			if(!car.isAlive() || car.getScore() > 100000 || (limitTime && currTicks > MAX_TICKS))
//			if(!car.isAlive())
			{
				running = false;
			}
			
//			System.out.println("CarEvaluator::evaluate():: showGraphics = " + showGraphics);
			if(showGraphics)
			{
				Display.update();
				Display.sync(120);
			}
				
		}
		
		double score = map.getFullScore()*5000 + car.getScore();
		
//		System.out.println("CarEvaluator::evaluate():: score = " + score);
		
		if(score > bestEverScore)
		{
			bestEverScore = score;
			bestEverGann = gann;
		}
		return score;
	}

	@Override
	public double evaluateFinal(GeneticAlgorithmNeuralNetwork gann) {
		// TODO Auto-generated method stub
		return evaluate(gann);
	}
	
	public void runBestEver()
	{
//		limitTime = false;
		randomMap = false;
		evaluate(bestEverGann);
		randomMap = true;
	}
	
	
	public void changeMap()
	{
		boolean[][] track = new boolean[5][5];
		track[0][0] = true;
		track[1][0] = true;
		track[2][0] = true;
		
		track[0][1] = true;
		track[2][1] = true;
		
		track[0][2] = true;
		track[2][2] = true;
		track[3][2] = true;
		
		track[0][3] = true;
		track[3][3] = true;
		
		track[0][4] = true;
		track[1][4] = true;
		track[2][4] = true;
		track[3][4] = true;
		
		map.setTrack(track);
	}

}
