package general;

public interface FitnessEvaluator {

	public abstract double evaluate(GeneticAlgorithmNeuralNetwork gann);
	
	public abstract double evaluateFinal(GeneticAlgorithmNeuralNetwork gann);
}
