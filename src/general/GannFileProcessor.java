package general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GannFileProcessor {
	
	public static void write(GeneticAlgorithmNeuralNetwork gann, String filename) {
		
		try{
			File file = new File(filename);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			ArrayList<Integer> layerSizes = gann.getLayerSizes();
			bw.write("" + layerSizes.size());
			bw.newLine();
			
			for(int k=0;k<layerSizes.size();k++)
			{
				bw.write("" + layerSizes.get(k));
				bw.newLine();
			}
			for(int k=0;k<gann.getNumMatricies();k++)
			{
				int size = gann.getMatrixSize(k);
				for(int i=0;i<size;i++)
				{
					bw.write("" + gann.getElement(k, i));
					bw.newLine();
				}
			}
			
			bw.close();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static GeneticAlgorithmNeuralNetwork read(String filename)
	{
		GeneticAlgorithmNeuralNetwork gann = new GeneticAlgorithmNeuralNetwork();
		try{
			File file = new File(filename);
			
			if(!file.exists())
			{
				return null;
			}
			
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			line = br.readLine();
			int numSizes = Integer.parseInt(line);
			
			for(int k=0;k<numSizes;k++)
			{
				line = br.readLine();
				gann.addLayer(Integer.parseInt(line));
			}
			gann.build();
			
			for(int k=0;k<gann.getNumMatricies();k++)
			{
				int size = gann.getMatrixSize(k);
				for(int i=0;i<size;i++)
				{
					line = br.readLine();
					gann.setElement(k, i, Double.parseDouble(line));
				}
			}
			
			br.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		return gann;
	}
}
