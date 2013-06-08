package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exp1_Result {
	public static void main(String args[]){
		String log = "/home/jychen/work/elm/icca2013/result_log/exp1_log_append_log";
		double [] d = new double[1000];
		int count = 0;
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(log))));
			String line;
			while((line=br.readLine())!=null){
				if(line.startsWith("Time Consumed:")){
					String [] tmp = line.split(" ");
					String [] tmp2 = tmp[2].split("s");
					System.out.print(tmp2[0]+"	");
					d[count] = Double.parseDouble(tmp2[0]);
					count++;
					if(count%3 == 0){
						System.out.println("\n-----------------------------------");
					}
				}
			} 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("*******************************************");
		for(int i=0;i<count/3;i++){
			System.out.println((d[i*3]+d[i*3+1]+d[i*3+2])/3);
		}
	}
}
