package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Phase1_DataPre {
	public static void main(String args[]){
		String sample = "/home/jychen/work/elm/icca2013/models3/t0_norm_sample.data";
		String outpath = "/home/jychen/work/elm/icca2013/exp1/";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(sample))));
			String line;
			String sample_data="";
			while((line=br.readLine())!=null){
				String []ss=line.split(":");
				sample_data = ss[1];
			}
			for(int i=1;i<=32;i++){
				BufferedWriter bw = new BufferedWriter(new FileWriter(outpath+"exp1_"+i+".data"));
				for(int j=1;j<=25;j++){
					int num=(int)(Math.random() * 100  + 20);
					String fun = (j%2==0)?"sin":"sig";
					String item = num+"_"+ fun + ":" + sample_data;
					bw.write(item+"\n");
				}
				bw.close();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
