package general;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;


public class Population implements Iterable {
	
	public static final int MIN_FIRST = 1;
	public static final int MAX_FIRST = -1;
	
	private int order = MIN_FIRST;
	private static final double SELECTION_PERCENTAGE = .1;
	

	private ArrayList<GeneticAlgorithmNeuralNetwork> networks = new ArrayList<GeneticAlgorithmNeuralNetwork>();
	
	public void add(GeneticAlgorithmNeuralNetwork gann)
	{
		networks.add(gann);
	}
	
	public ArrayList<GeneticAlgorithmNeuralNetwork> selectBest(FitnessEvaluator fitnessEvaluator)
	{
		PriorityQueue<FitnessNetworkPair> pq = new PriorityQueue<FitnessNetworkPair>();
		
		//Compute the Fitness of all Networks
		pq.clear();
		long currTime = System.currentTimeMillis();
		long prevTime = currTime;
		for(int i=0; i<size(); i++)
		{
//			currTime = System.currentTimeMillis();
//			System.out.println("Population::selectBest():: Delta eval Time = " + (currTime - prevTime));
//			prevTime = currTime;
			
			GeneticAlgorithmNeuralNetwork gann = get(i);
			pq.add(new FitnessNetworkPair(gann.copy(), fitnessEvaluator.evaluate(gann)));
		}
		ArrayList<GeneticAlgorithmNeuralNetwork> best = new ArrayList<GeneticAlgorithmNeuralNetwork>();
		for(int selected=0; selected<size()*SELECTION_PERCENTAGE; selected++)
		{
//			System.out.println(pq.peek().fitness);
			best.add(pq.poll().network);
		}
//		System.out.println("*********");

		
		return best;
	}
	
	public ArrayList<GeneticAlgorithmNeuralNetwork> selectBest2(FitnessEvaluator fitnessEvaluator)
	{
		ArrayList<FitnessNetworkPair> bestPairs = new ArrayList<FitnessNetworkPair>();
		//Compute the Fitness of all Networks

		for(int i=0; i<size(); i++)
		{
			GeneticAlgorithmNeuralNetwork gann = get(i);
			bestPairs.add(new FitnessNetworkPair(gann.copy(), fitnessEvaluator.evaluate(gann)));
		}
		Collections.sort(bestPairs);
		ArrayList<GeneticAlgorithmNeuralNetwork> best = new ArrayList<GeneticAlgorithmNeuralNetwork>();
//		System.out.println("Population::selectBest2():: begin List: ");
		for(int selected=0; selected<size()*SELECTION_PERCENTAGE; selected++)
		{
//			System.out.println(pq.peek().fitness);
			best.add(bestPairs.get(selected).network);
//			System.out.println("Population::selectBest2():: fit= " + bestPairs.get(selected).fitness);
		}
//		System.out.println("*********");
		
		return best;

	}
	
	public void repopulateWithBest(FitnessEvaluator fitnessEvaluator)
	{
		ArrayList<GeneticAlgorithmNeuralNetwork> best = this.selectBest(fitnessEvaluator);
		
		Population population2 = new Population();
		for(GeneticAlgorithmNeuralNetwork gann : best)
		{
			for(int numDups=0; numDups<1/SELECTION_PERCENTAGE; numDups++)
			{
				population2.add(gann.copy().mutate());
			}
		}
		this.setPopulationTo(population2);
	}
	
	public void repopulateWithBestCrossbreed(FitnessEvaluator fitnessEvaluator)
	{
		ArrayList<GeneticAlgorithmNeuralNetwork> best = this.selectBest(fitnessEvaluator);
		
		Population population2 = new Population();
		for(int k=0; k<best.size(); k++)
		{
			population2.add(best.get(k).copy());
			population2.add(new GeneticAlgorithmNeuralNetwork(best.get(k).getLayerSizes()));
//			System.out.println("Population::repopWithCross():: adding best copy...");
		}
//		System.out.println("Population::repopWithCross():: Done.");
		int holdSize = size();
		
		Random gen = new Random();
		for(int k=0; population2.size()<holdSize; k++)
		{
			GeneticAlgorithmNeuralNetwork holdGann1 = best.get(k%best.size());
			GeneticAlgorithmNeuralNetwork holdGann2 = best.get(gen.nextInt(best.size()));
			population2.add(GeneticAlgorithmNeuralNetwork.crossbreed(holdGann1, holdGann2));
		}
		
		this.setPopulationTo(population2);
	}
	
	public void mutate()
	{
		for(GeneticAlgorithmNeuralNetwork gann : networks)
		{
			gann.mutate();
		}
	}
	
	public void setOrder(int flag)
	{
		this.order = flag;
	}
	
	public void setPopulationTo(Population pop)
	{
		this.networks = pop.getNetworks();
	}
	
	public ArrayList<GeneticAlgorithmNeuralNetwork> getNetworks()
	{
		return networks;
	}
	
	public int size()
	{
		return networks.size();
	}
	
	public GeneticAlgorithmNeuralNetwork get(int index)
	{
		return networks.get(index);
	}

	@Override
	public Iterator<GeneticAlgorithmNeuralNetwork> iterator() {
		return networks.iterator();
	}
	
	private class FitnessNetworkPair implements Comparable
	{
		public GeneticAlgorithmNeuralNetwork network;
		public double fitness;
		
		public FitnessNetworkPair(GeneticAlgorithmNeuralNetwork network, double fitness)
		{
			this.network = network;
			this.fitness = fitness;
		}

		@Override
		public int compareTo(Object o) {
			FitnessNetworkPair fnp = (FitnessNetworkPair) o;
			if(this.fitness > fnp.fitness)
			{
				return 1 * order;
			}
			else if(this.fitness < fnp.fitness)
			{
				return -1 * order;
			}
			else
			{
				return 0;
			}
		}
	}
}
