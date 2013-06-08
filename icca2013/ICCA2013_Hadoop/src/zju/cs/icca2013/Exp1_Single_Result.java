package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exp1_Single_Result {
	public static void main(String args[]){
		String log = "/home/jychen/work/elm/icca2013/result_log/exp1_single3_log";
		double [] d = new double[1000];
		int count=0;
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(log))));
			String line;
			while((line=br.readLine())!=null){
				if(line.startsWith("Time Consumed:")){
					String [] tmp = line.split(" ");
					String [] tmp2 = tmp[2].split("s");
					System.out.println(tmp2[0]);
					d[count] = Double.parseDouble(tmp2[0]);
					count++;
				}
			} 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-----------------------------------------");
		double item = d[0]+d[1]+d[2]+d[3];
		System.out.println(item);
		for(int i=4;i<count;i=i+2){
			item += d[i] + d[i+1];
			System.out.println(item);
		}
	}
}
