import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Data_Preparation {
	public static void main(String args[]){
		Data_Preparation.test_data();
	}
	public static void test_data(){
		String inpath = "/home/jychen/work/elm/icca2013/T2/t2_norm.data";
		String outpath = "/home/jychen/work/elm/icca2013/T2/t2_norm_P.data";
		String outpath2 = "/home/jychen/work/elm/icca2013/T2/t2_norm_T.data";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inpath))));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(outpath2));
			String str;
			int count = 0;
			while((str=br.readLine())!=null){
				String []ss=str.split(" ");
				bw.write(ss[1]+" "+ss[2] + " " + ss[3] + "\n");
				bw2.write(ss[0]+"\n");
				count++;
			} 
			bw.close();
			bw2.close();
			System.out.println(count);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static void train_data(){
		String inpath = "/home/jychen/work/elm/diabetes_train";
		String outpath = "/home/jychen/diabetes_train";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inpath))));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));
			String str;
			int count = 0;
			while((str=br.readLine())!=null){
				String []ss=str.split("  ");
				String newLine = ss[0] + " " + ss[1];
				bw.write(newLine+"\n");
				count++;
			} 
			bw.close();
			System.out.println(count);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
