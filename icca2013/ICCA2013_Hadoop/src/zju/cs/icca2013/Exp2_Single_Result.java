package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exp2_Single_Result {
	public static void main(String args[]){
		String log = "/home/jychen/work/elm/icca2013/result_log/exp2_2single3_log";
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
		double item=0;
		for(int i=0;i<count/5;i++){
			item += d[i*5] + d[i*5+1] + d[i*5+2] + d[i*5+3] + d[i*5+4];
			System.out.println(item + 323.522142);
		}
	}
}
