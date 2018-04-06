package general;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


public class GeneticAlgorithmNeuralNetwork {
	
	public static double DEFAULT_MUTATION_RATE = .0015;
	public static double DEFAULT_MUTATION_CHANCE = .05;
	
	private double mutationRate = DEFAULT_MUTATION_RATE;
	
	private RealMatrix[] layers;
	private ArrayList<Integer> layerSizes = new ArrayList<Integer>();
	private boolean built = false;
	
	
	public GeneticAlgorithmNeuralNetwork()
	{
		
	}
	public GeneticAlgorithmNeuralNetwork(int[] sizes)
	{
		for(int size : sizes)
		{
			addLayer(size);
		}
		build();
	}
	public GeneticAlgorithmNeuralNetwork(ArrayList<Integer> sizes)
	{
		for(int size : sizes)
		{
			addLayer(size);
		}
		build();
	}

	public GeneticAlgorithmNeuralNetwork copy()
	{
		GeneticAlgorithmNeuralNetwork gann = new GeneticAlgorithmNeuralNetwork();
		for(int size : layerSizes)
		{
			gann.addLayer(size);
		}
		gann.build();
		
		for(int k=0; k<gann.layers.length; k++)
		{
			for(int x=0; x<gann.layers[k].getRowDimension(); x++)
			{
				for(int y=0; y<gann.layers[k].getColumnDimension(); y++)
				{
					gann.layers[k].setEntry(x, y, this.layers[k].getEntry(x, y));
				}
			}
		}
		
		return gann;
	}
	
	public void addLayer(int size)
	{
		if(built)
		{
			System.out.println("Error: Cannot add layer; Network already built.");
			return;
		}
		layerSizes.add(size);
	}
	
	public ArrayList<Integer> getLayerSizes()
	{
		return layerSizes;
	}
	
	public void build()
	{
		layers = new RealMatrix[layerSizes.size()-1];
		
		for(int k=0;k<layerSizes.size()-1;k++)
		{
			layers[k] = MatrixUtils.createRealMatrix(createRandomArray(layerSizes.get(k), layerSizes.get(k+1)));
		}
		built = true;
	}
	
	public double[] compute(double[] data)
	{
		if(!built)
		{
			System.out.println("Error: Cannot compute; Network not built yet.");
			return null;
		}
		
		double[][] dataArr = new double[1][data.length];
		dataArr[0] = data;
		RealMatrix val = MatrixUtils.createRealMatrix(dataArr);
		val = val.transpose();

		for(int k=0;k<layers.length;k++)
		{
			val = layers[k].transpose().multiply(val);
			sigmoid(val);
		}

		return val.transpose().getData()[0];
	}
	
	public static GeneticAlgorithmNeuralNetwork crossbreed(GeneticAlgorithmNeuralNetwork gann1, GeneticAlgorithmNeuralNetwork gann2)
	{
		//TODO: implement check for gann match (all matricies same size)
		Random gen = new Random();
		GeneticAlgorithmNeuralNetwork gannChild = gann1.copy();
		for(int k=0; k<gann1.layers.length; k++)
		{
			for(int x=0; x<gann1.layers[k].getRowDimension(); x++)
			{
				for(int y=0; y<gann1.layers[k].getColumnDimension(); y++)
				{
					if(gen.nextBoolean())
					{
						gannChild.layers[k].setEntry(x, y, gann2.layers[k].getEntry(x, y));
					}
					if(gen.nextDouble() < DEFAULT_MUTATION_CHANCE)
					{
						double val = gannChild.layers[k].getEntry(x, y);
						double offset = Math.random() % (DEFAULT_MUTATION_RATE*2) - DEFAULT_MUTATION_RATE;
						gannChild.layers[k].setEntry(x, y, val + offset);
					}
				}
			}
		}
		
		return gannChild;
	}
	
	public GeneticAlgorithmNeuralNetwork mutate()
	{		
		if(!built)
		{
			System.out.println("Error: Cannot compute; Network not built yet.");
			return null;
		}
		
		for(int k=0;k<layers.length;k++)
		{
			for(int x=0;x<layers[k].getRowDimension();x++)
			{
				for(int y=0;y<layers[k].getColumnDimension();y++)
				{
					double offset = Math.random() % (mutationRate*2) - mutationRate;
					layers[k].setEntry(x, y, layers[k].getEntry(x, y) + offset);
				}
			}
		}
		return this;
	}
	
	private static double[][] createRandomArray(int rows, int cols)
	{
		Random gen = new Random();
		
		double[][] arr = new double[rows][cols];
		for(int x=0;x<rows;x++)
		{
			for(int y=0;y<cols;y++)
			{
//				arr[x][y] = gen.nextDouble() * 10;
				arr[x][y] = gen.nextDouble()*2 - 1;
			}
		}
		return arr;
	}

	private static void sigmoid(RealMatrix m)
	{
		for(int x=0;x<m.getRowDimension();x++)
		{
			for(int y=0;y<m.getColumnDimension();y++)
			{
				m.setEntry(x, y, 1.0/(1 + Math.exp(m.getEntry(x, y))));
			}
		}
	}

	public int getInputSize()
	{
		return layerSizes.get(0);
	}
	
	public int getOutputSize()
	{
		return this.layerSizes.get(layerSizes.size()-1);
	}

	public int getNumMatricies() {
		return layers.length;
	}
	
	public double[][] getMatrix(int matrixNum) {
		return layers[matrixNum].getData();
	}
	
	public void setMatrix(int matrixNum, double[][] data) {
		layers[matrixNum] = MatrixUtils.createRealMatrix(data);
	}
	
	public int getMatrixSize(int matrixNum)
	{
		return layers[matrixNum].getColumnDimension() * layers[matrixNum].getRowDimension();
	}
	
	public double getElement(int matrixNum, int index)
	{
		int c = layers[matrixNum].getColumnDimension();
		return layers[matrixNum].getEntry(index/c, index%c);
	}
	
	public void setElement(int matrixNum, int index, double val)
	{
		int c = layers[matrixNum].getColumnDimension();
		layers[matrixNum].setEntry(index/c, index%c, val);
	}
}
