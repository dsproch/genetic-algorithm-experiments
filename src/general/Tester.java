package general;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


public class Tester {

	private static final int[] NETWORK_SIZES = {2,50,1};
	private static final int POPULATION_SIZE = 50;
	
	public Tester()
	{
		
		//Create Population and Populate
		Population pop = new Population();
		for(int k=0; k<POPULATION_SIZE; k++)
		{
			pop.add(new GeneticAlgorithmNeuralNetwork(NETWORK_SIZES));
		}
		
		SizeCompareEvaluator sce = new SizeCompareEvaluator();
		
		double bestFirstGen = sce.evaluateFinal(pop.selectBest(sce).get(0));
		
		Trainer trainer = new Trainer(pop, sce);
		
		int numOfGenerationsToTrain = 1000;
		trainer.train(numOfGenerationsToTrain);
		
		
		GeneticAlgorithmNeuralNetwork gann = pop.selectBest(sce).get(0);
		
		double bestLastGen = sce.evaluateFinal(gann);
		System.out.println("Best of First Generation = " + bestFirstGen);
		System.out.println("Best Of Last Generation = " + bestLastGen);
	}
	
	
	
	
	private class SizeCompareEvaluator implements FitnessEvaluator
	{
		private static final int DEFAULT_NUMBER_OF_SAMPLES = 2000;
		private static final int NUMBER_RANGE = 100;
		private static final double TEST_TO_VALIDATION_PERCENT = .8;
		
		private int numSamples = DEFAULT_NUMBER_OF_SAMPLES;
		ArrayList<DataSample> samples = new ArrayList<DataSample>();
		
		public SizeCompareEvaluator()
		{
			Random gen = new Random();
			
			for(int k=0; k<numSamples; k++)
			{
				double[] x = new double[]{gen.nextInt(NUMBER_RANGE), gen.nextInt(NUMBER_RANGE)};
				samples.add(new DataSample(x));
			}
		}

		@Override
		public double evaluate(GeneticAlgorithmNeuralNetwork gann) {	
			double sum = 0;
			
			for(int k=0; k<numSamples*TEST_TO_VALIDATION_PERCENT; k++)
			{
//				gann.addLayer(1);
				double[] calc = gann.compute(samples.get(k).x);
				sum += Math.abs(calc[0] - samples.get(k).y[0]);			
			}
			return sum;
		}
		
		@Override
		public double evaluateFinal(GeneticAlgorithmNeuralNetwork gann) {
			double sum = 0;
			
			for(int k=(int) (numSamples*TEST_TO_VALIDATION_PERCENT); k<numSamples; k++)
			{
				double[] calc = gann.compute(samples.get(k).x);
				sum += Math.abs(Math.round(calc[0]) - samples.get(k).y[0]);			
			}
//			System.out.println(sum + " wrong out of " + (numSamples*(1 - TEST_TO_VALIDATION_PERCENT)));
			return sum / (numSamples*(1 - TEST_TO_VALIDATION_PERCENT));
		}
		
		private class DataSample
		{			
			public double[] x;
			public double[] y;
			
			public DataSample(double[] x)
			{
				this.x = x;
				if(this.x[0] > this.x[1])
				{
					this.y = new double[]{1};
				}
				else
				{
					this.y = new double[]{0};
				}
			}
			
			public DataSample(double[] x, double[] y)
			{
				this.x = x;
				this.y = y;
			}
		}
	}
	
	
	
	public static void main(String[] args)
	{
		Tester tester = new Tester();
	}
}
