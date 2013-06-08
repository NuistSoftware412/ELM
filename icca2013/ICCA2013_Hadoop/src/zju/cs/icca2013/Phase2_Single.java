package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Phase2_Single {
	public static void main(String [] args){
		String inFile = args[0];
		String outFile = args[1];
		long startTime = System.nanoTime();
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)))); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			String line;
			ArrayList<String[]> tag_arr = new ArrayList<String[]>();
			while((line=br.readLine())!=null){
				String [] tmp = line.split(":");
				String tags = Ensemble_Predictor.multi_predict(tmp[1],tmp[2]);
				String [] tmp2 = {tmp[0],tags};
				tag_arr.add(tmp2);
			} 
			for(int i=0;i<tag_arr.size();i++){
				for(int j=i+1;j<tag_arr.size();j++){
					if(Integer.parseInt(tag_arr.get(j)[0]) > Integer.parseInt(tag_arr.get(j)[0])){
						String [] item = tag_arr.get(j);
						tag_arr.set(j, tag_arr.get(i));
						tag_arr.set(i, item);
					}
				}
			}
			ArrayList<String> values = new ArrayList<String>();
			String key = tag_arr.get(0)[0];
			for(int i=0;i<tag_arr.size();i++){
				if(tag_arr.get(i)[0].equals(key)){
					System.out.println("in");
					values.add(tag_arr.get(i)[1]);
				}else{
					System.out.println("combine");
					String result = Ensemble_Predictor.combine2(values.iterator());
					bw.write(key+"	"+result+"\n");
					values = new ArrayList<String>();
					key = tag_arr.get(i)[0];
					System.out.println("in");
					values.add(tag_arr.get(i)[1]);
				}
			}
			System.out.println("combine");
			String result = Ensemble_Predictor.combine2(values.iterator());
			bw.write(key+"	"+result);
			bw.close();
		}catch (Exception e){
		   e.printStackTrace();
		}
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Time Consumed: " + (double)consumingTime/1000000000+"s");
	}
}
