package zju.cs.icca2013;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Phase2_DataPre {
	public static void main(String args[]){
		String sample = "/home/jychen/work/elm/icca2013/models4/t2_norm_sample.data";
		String outpath = "/home/jychen/work/elm/icca2013/exp2/";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(sample))));
			String line;
			String sample_data="";
			while((line=br.readLine())!=null){
				String []ss=line.split(":");
				sample_data = ss[2];
			}
			for(int i=51;i<=75;i++){
				BufferedWriter bw = new BufferedWriter(new FileWriter(outpath+"exp2_"+i+".data"));
				for(int j=1;j<=30;j++){
					int line_num= (i-1)*20+j;
					for(int k=1;k<=20;k++){
						int hidden_num = (k-1)/2 + 20;
						String fun = ((k-1)%2==0)?"sin":"sig";
						String item = line_num + ":" + hidden_num+"_"+fun+":"+sample_data;
						bw.write(item+"\n");
					}
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
