package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Phase1_Single {
	public static void main(String [] args){
		String inFile = args[0];
		String outFile = args[1];
		long startTime = System.nanoTime();
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)))); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			String line;
			while((line=br.readLine())!=null){
				String [] tmp = line.split(":");
				String [] tmp2 = tmp[0].split("_");
				ELM_Classifier elm = new ELM_Classifier(3, 5);
				elm.ELM_Train(tmp[1], Integer.parseInt(tmp2[0]), tmp2[1]);
				if(elm.ELM_Filter(0.85, 1)){
					bw.write(tmp[0] + " " + elm.toString() + "\n");
				}
			} 
			bw.close();
		}catch (Exception e){
		   e.printStackTrace();
		}
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Time Consumed: " + (double)consumingTime/1000000000+"s");



	}
}
