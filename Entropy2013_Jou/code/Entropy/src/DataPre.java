import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class DataPre {
	public static void main(String args[]){
		segment_data();
	}
	public static void segment_data(){
		int row=2310;
		String []ss = new String[row];
		int trn_row=1800;
		int tst_row=510;
		double [][] data=new double[row][Train.atts_num];
		int [] tags = new int[row];
		String path="/home/jychen/work/Entropy/datasets/segment/segment1.dat";
		int i=0;
		double min=1000000,max=-10000000;
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			String str;
			while((str=br.readLine())!=null){
				int tag = Integer.parseInt(str.substring(str.length()-1, str.length()))-1;
				tags[i] = tag;
				String [] tmp = str.substring(0,str.length()-2).split(" ");
				for(int j=0;j<Train.atts_num;j++){
					data[i][j]=Double.parseDouble(tmp[j]);
					if(data[i][j]<min){
						min=data[i][j];
					}
					if(data[i][j]>max){
						max=data[i][j];
					}
				}
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}	
			
		String trn_path="/home/jychen/work/Entropy/datasets/segment/train2.data";
		String tst_path="/home/jychen/work/Entropy/datasets/segment/test2.data";
		try{
			BufferedWriter bl = new BufferedWriter(new FileWriter(trn_path));
			for(int j=0;j<trn_row;j++){
				String tmp2=tags[j]+" ";
				for(int k=0;k<Train.atts_num;k++){
					tmp2+=(data[j][k]-min)/(max-min)+" ";
				}
				bl.write(tmp2+"\n");
			}
			bl.close();
			
			BufferedWriter bl2 = new BufferedWriter(new FileWriter(tst_path));
			for(int j=0;j<tst_row;j++){
				String tmp2=tags[trn_row+j]+" ";
				for(int k=0;k<Train.atts_num;k++){
					tmp2+=(data[trn_row+j][k]-min)/(max-min)+" ";
				}
				bl2.write(tmp2+"\n");
				
			}
			bl2.close();
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	public static void diabetes_data(){
		int trn_row = 150;
		String trn_path = "/home/jychen/work/Entropy/datasets/diabetes/test.data";
		String [] ss = new String[trn_row];
		int i=0;
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(trn_path)))); 
			String str;
			while((str=br.readLine())!=null){
				ss[i]=str;
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		String trn_path2="/home/jychen/work/Entropy/datasets/diabetes/test2.data";
		try{
			BufferedWriter bl = new BufferedWriter(new FileWriter(trn_path2));
			for(int j=0;j<trn_row;j++){
				String tmp = ss[j];
				bl.write(tmp.substring(tmp.length()-1, tmp.length()) + " " + 
						tmp.substring(0,tmp.length()-2)+"\n");
			}
			bl.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void landsat_data(){
		String [] ss = new String[3500];
		String path="/home/jychen/work/Entropy/datasets/landsat/train.data";
		int i=0;
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			String str;
			while((str=br.readLine())!=null){
				ss[i]=str;
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		String path2="/home/jychen/work/Entropy/datasets/landsat/train2.data";
		try{
			BufferedWriter bl = new BufferedWriter(new FileWriter(path2));
			for(int j=0;j<700;j++){
				bl.write(ss[j]+"\n");
				bl.write(ss[j+700]+"\n");
				bl.write(ss[j+1400]+"\n");
				bl.write(ss[j+2100]+"\n");
				bl.write(ss[j+2800]+"\n");
			}
			bl.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
