package general;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


public class Individual {

	private RealMatrix inputLayer;
	private RealMatrix hiddenLayer;
	private RealMatrix outputLayer;
	
	
	public Individual(int inputSize, int hiddenSize, int outputSize)
	{
//		inputLayer = MatrixUtils.createRealMatrix(inputSize, hiddenSize);
//		hiddenLayer = MatrixUtils.createRealMatrix(hiddenSize,outputSize);
		//outputLayer = MatrixUtils.createRealMatrix(outputSize,1);
		
		inputLayer = MatrixUtils.createRealMatrix(createRandomArray(inputSize, hiddenSize));
		hiddenLayer = MatrixUtils.createRealMatrix(createRandomArray(hiddenSize, outputSize));		
		
	}
	
	public Individual(RealMatrix inputLayer, RealMatrix hiddenLayer)
	{
		this.inputLayer = inputLayer;
		this.hiddenLayer = hiddenLayer;
	}
	
	public static double[][] createRandomArray(int rows, int cols)
	{
		Random gen = new Random();
		
		double[][] arr = new double[rows][cols];
		for(int x=0;x<rows;x++)
		{
			for(int y=0;y<cols;y++)
			{
//				arr[x][y] = gen.nextDouble() * 10;
				arr[x][y] = gen.nextInt(10) - 5;
			}
		}
		return arr;
	}
	
	public static void test()
	{
		double[][] aMatrix = {{1,2},{3,4},{5,6}};
		RealMatrix m1 = MatrixUtils.createRealMatrix(aMatrix);

		
		double[][] bMatrix = {{1},{1},{2}};
//		RealMatrix m2 = MatrixUtils.createRealMatrix(bMatrix);
		RealMatrix m2 = MatrixUtils.createRealMatrix(createRandomArray(3,1));
		
		RealMatrix out = m1.transpose().multiply(m2);
		sigmoid(out);
		System.out.println(out);
	}
	
	public Individual copy()
	{
		return new Individual(inputLayer.copy(), hiddenLayer.copy());
	}
	
	public static void sigmoid(RealMatrix m)
	{
		for(int x=0;x<m.getRowDimension();x++)
		{
			for(int y=0;y<m.getColumnDimension();y++)
			{
				m.setEntry(x, y, 1.0/(1 + Math.exp(m.getEntry(x, y))));
			}
		}
	}
	
	public double[] compute(double[] data)
	{
		double[][] dataArr = new double[1][data.length];
		dataArr[0] = data;
		RealMatrix d = MatrixUtils.createRealMatrix(dataArr);
		d = d.transpose();
		
		RealMatrix val = inputLayer.transpose().multiply(d);
		sigmoid(val);
		val = hiddenLayer.transpose().multiply(val);
		sigmoid(val);
		
//		System.out.println(val);
		
		return val.transpose().getData()[0];
		
		
	}
	
	public void mutate()
	{
		double mutationRate = .0015;
		
		for(int x=0;x<inputLayer.getRowDimension();x++)
		{
			for(int y=0;y<inputLayer.getColumnDimension();y++)
			{
				double offset = Math.random() % (mutationRate*2) - mutationRate;
				inputLayer.setEntry(x, y, inputLayer.getEntry(x, y) + offset);
			}
		}
		
		for(int x=0;x<hiddenLayer.getRowDimension();x++)
		{
			for(int y=0;y<hiddenLayer.getColumnDimension();y++)
			{
				double offset = Math.random() % mutationRate*2 - mutationRate;
				hiddenLayer.setEntry(x, y, hiddenLayer.getEntry(x, y) + offset);
			}
		}
		
	}
	
	public static void main(String[] args)
	{

		Individual i = new Individual(3,10,1);
		double[] v = i.compute(new double[]{2.0, 3.4, 1.1});
		
		for(int k=0;k<v.length;k++)
		{
			System.out.print(v[k] + " ");
		}
		System.out.println("\n");		
	
		
		Random gen = new Random();
		ArrayList<double[]> testData = new ArrayList<double[]>();
		for(int k=0;k<500;k++)
		{
			double[] input = new double[2];
			input[0] = gen.nextInt(100);
			input[1] = gen.nextInt(100);
			
			double[] output = new double[1];
			if(input[0] > input[1])
			{
				output[0] = 1;
			} else 
			{
				output[0] = 0;
			}
			testData.add(input);
			testData.add(output);
		}
		
		
		Individual[] inds = new Individual[20];
		double[] sums = new double[20];
		PriorityQueue<Double> pq = new PriorityQueue<Double>();
		
		
		for(int k=0;k<inds.length;k++)
		{
			inds[k] = new Individual(2,10,1);
		}
		
		int maxGens = 2000;
		double[] genAvg = new double[maxGens];
		double bestFirstGenAvg = testData.size()*2;
		Individual[] best = new Individual[1];
		
		for(int numGens=0;numGens<maxGens;numGens++)
		{
			System.out.println("\nGeneration " + numGens);
			genAvg[numGens] = 0;
			double bestAvgThisGen = testData.size()*2;
			for(int j=0;j<inds.length;j++)
			{
				sums[j] = 0;
				for(int k=0;k<testData.size()-1;k+=2)
				{
					double calc = inds[j].compute(testData.get(k))[0];
//					if(calc > .5)
//					{
//						calc = 1;
//					} else {
//						calc = 0;
//					}
					sums[j] += Math.abs(calc - testData.get(k+1)[0]);
							
							
//					sums[j] += Math.abs(inds[j].compute(testData.get(k))[0] - testData.get(k+1)[0]);
				}
				genAvg[numGens] += (sums[j]/(testData.size()/2));
				bestAvgThisGen = Math.min(bestAvgThisGen, sums[j]/(testData.size()/2));
//				System.out.println("Ind " + j + " has sum of " + (sums[j]/(testData.size()/2)));
			}
			genAvg[numGens] = genAvg[numGens]/inds.length;
			if(numGens == 0)
			{
				bestFirstGenAvg = bestAvgThisGen;
			}
			
			System.out.println("Gen 0 avg= " + genAvg[0] + " --- Current Generation Average= " + genAvg[numGens]);
			System.out.println("Gen 0 best avg= " + bestFirstGenAvg + " --- Best Avg this Gen = " + bestAvgThisGen);
			
			
			int max = testData.size()*2;
			double min = max;
			best = new Individual[inds.length/2];
			for(int k=0;k<inds.length/2;k++)
			{
				int minIndex = -1;
				min = max;
				for(int j=0;j<sums.length;j++)
				{
					if(sums[j] < min)
					{
						min = sums[j];
						minIndex = j;
					}
				}
				best[k] = inds[minIndex].copy();
			}
			for(int k=0;k<inds.length/2;k+=2)
			{
				inds[k*2] = best[k].copy();
				inds[k*2].mutate();
				
				inds[k*2+1] = best[k].copy();
				inds[k*2+1].mutate();
			}
		}
		 
		int size = 10000;
		testData = createTestData(size);
		double sum = 0;
		int numWrong = 0;
		for(int k=0;k<testData.size()-1;k+=2)
		{
			double calc = best[0].compute(testData.get(k))[0];
			if(calc > .5)
			{
				calc = 1;
			} else {
				calc = 0;
			}
			if(calc != testData.get(k+1)[0])
			{
				numWrong++;
			}
			sum += Math.abs(calc - testData.get(k+1)[0]);
		}
		sum = sum/(testData.size()/2);
		System.out.println("Best Ind on New Test Set = " + sum);
		System.out.println("Num Wrong = " + numWrong + " out of " + size);
		
		
	}
	
	public static ArrayList<double[]> createTestData(int size)
	{
		Random gen = new Random();
		ArrayList<double[]> testData = new ArrayList<double[]>();
		for(int k=0;k<size;k++)
		{
			double[] input = new double[2];
			input[0] = gen.nextInt(100);
			input[1] = gen.nextInt(100);
			
			double[] output = new double[1];
			if(input[0] > input[1])
			{
				output[0] = 1;
			} else 
			{
				output[0] = 0;
			}
			testData.add(input);
			testData.add(output);
		}
		return testData;
	}
}
