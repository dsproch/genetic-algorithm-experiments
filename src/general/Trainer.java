package general;
import java.util.PriorityQueue;


public class Trainer {
	
	private Population population;
	private FitnessEvaluator fitnessEvaluator;

	public Trainer(Population population, FitnessEvaluator fitnessEvaluator)
	{
		this.population = population;
		this.fitnessEvaluator = fitnessEvaluator;
	}
	
	public void train(int numGenerations)
	{
		System.out.print("Training...");
		long currTime = System.currentTimeMillis();
		long prevTime = currTime;
		
		for(int currGen=0; currGen<numGenerations; currGen++)
		{
//			currTime = System.currentTimeMillis();
//			System.out.println("Trainer::train():: Delta Generation Time = " + (currTime - prevTime));
//			prevTime = currTime;
			
			if(currGen % (int)(numGenerations * .1) == 0)
			{
				System.out.print(" " + ((double)currGen / numGenerations * 100) + "%...");
			}

//			population.repopulateWithBest(fitnessEvaluator);
			population.repopulateWithBestCrossbreed(fitnessEvaluator);
		}
		System.out.println();
		
		for(int k=0; k<population.size(); k++)
		{
			GeneticAlgorithmNeuralNetwork gann = population.get(k);
			System.out.println("Network " + k + " evaluation = " + this.fitnessEvaluator.evaluateFinal(gann));
		}
	}

	public void mutate(GeneticAlgorithmNeuralNetwork gann) {
		//TODO: better mutate method
		gann.mutate();
	}


}
