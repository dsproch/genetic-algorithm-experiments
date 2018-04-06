package carStuff;

import java.util.Scanner;

import extras.Notifier;

import general.GannFileProcessor;
import general.GeneticAlgorithmNeuralNetwork;
import general.Population;
import general.Trainer;

public class CarExper {
	
	private static final int[] NETWORK_SIZES = {7, 1};
	private static final int POPULATION_SIZE = 50;
	private static final int NUM_GENERATIONS = 500;
	private static final boolean SHOW_TRAINING_GRAPHICS = false;
	
	private static final boolean READ_GANN = true;
	private static final boolean WRITE_GANN = true;	//applies only if READ_GANN is false
	private static final String FILE_NAME = "gann.txt";
	
	public CarExper()
	{
		//Create Population and Populate
		Population pop = new Population();
		pop.setOrder(Population.MAX_FIRST);
		for(int k=0; k<POPULATION_SIZE; k++)
		{
			pop.add(new GeneticAlgorithmNeuralNetwork(NETWORK_SIZES));
		}
		
		CarEvaluator sce = new CarEvaluator();
		sce.showGraphics = SHOW_TRAINING_GRAPHICS;
		double bestFirstGen = sce.evaluateFinal(pop.selectBest(sce).get(0));
		
		Trainer trainer = new Trainer(pop, sce);
		
		Scanner scan = new Scanner(System.in);
		
		GeneticAlgorithmNeuralNetwork gann;
		if(!READ_GANN)
		{
			int numOfGenerationsToTrain = NUM_GENERATIONS;
			trainer.train(numOfGenerationsToTrain);
			
			Notifier.notifySend("Training Complete", "ML-Car training complete.");
			
			System.out.println("Press Enter to continue...");
			scan.nextLine();

			gann = pop.selectBest(sce).get(0);
			
			if(WRITE_GANN)
			{
				GannFileProcessor.write(gann, FILE_NAME);
			}
			
			
		} else {
			sce.showGraphics = true;
			gann = GannFileProcessor.read(FILE_NAME);
			double bestLastGen = sce.evaluateFinal(gann);
			sce.changeMap();
			sce.evaluateFinal(gann);
			scan.close();
			return;
		}
		
		System.out.println("See best again...");
		System.out.println("Press Enter to continue...");
		scan.nextLine();
		sce.showGraphics = true;
		
		double bestLastGen = sce.evaluateFinal(gann);
		sce.runBestEver();
		sce.changeMap();
		sce.runBestEver();
		System.out.println("Best of First Generation = " + bestFirstGen);
		System.out.println("Best Of Last Generation = " + bestLastGen);
		
		scan.close();
		
	}
	
	public static void main(String[] args)
	{
		System.out.println("Car Exper!");
		System.out.println("Car Exper!");
		System.out.println("Car Exper!");
		System.out.println("Car Exper!");
		CarExper ce = new CarExper();
	}

}
