import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.media.sound.ModelSource;

import ELM_Classifier.ELM_Classifier;


public class Exp1 {
	private static int labels_num = 5;
	private static int factors_num = 3;
	
	public static void main(String args[]){
		String model = "/home/jychen/work/elm/icca2013/models2/model";
		String td = "/home/jychen/work/elm/icca2013/T0/t0_norm";
		int [] size= {5000,7500,10000,12500,15000,17500,20000};
		for(int i=1;i<=7;i++){
			for(int j=0;j<5;j++){
				Exp1.exp1(model, 20, td + "_" + i + ".data", 
						size[i-1], "/home/jychen/work/elm/icca2013/T1/t1_norm.data", 300, 0.85,1.00,
						"/home/jychen/work/elm/icca2013/T2/t2_norm_P.data", 499, "/home/jychen/work/elm/icca2013/T2/t2_norm_T.data");
			}
			System.out.println("-----------------------------------------------");
		}
	}
	
	public static void ensemble(){
		String [] models ={"/home/jychen/work/elm/icca2013/models/model1",
				"/home/jychen/work/elm/icca2013/models/model2","/home/jychen/work/elm/icca2013/models/model3",
				"/home/jychen/work/elm/icca2013/models/model4","/home/jychen/work/elm/icca2013/models/model5",
				"/home/jychen/work/elm/icca2013/models/model6","/home/jychen/work/elm/icca2013/models/model7",
				"/home/jychen/work/elm/icca2013/models/model8","/home/jychen/work/elm/icca2013/models/model9",
				"/home/jychen/work/elm/icca2013/models/model10"};
		int [] num = {1,5,10,15,20,25,30,35,40,45};
		for(int i=0;i<10;i++){
			for(int j=0;j<5;j++){
			Exp1.exp1(models[i], num[i], "/home/jychen/work/elm/icca2013/T0/t0_norm3.data", 
				5000, "/home/jychen/work/elm/icca2013/T1/t1_norm.data", 300, 0.85,1.00,
				"/home/jychen/work/elm/icca2013/T2/t2_norm_P.data", 499, "/home/jychen/work/elm/icca2013/T2/t2_norm_T.data");
			}
		}
	}
	
	public static void exp1(String model_path,int model_num,String train_path,int train_num,
			String filter_path,int filter_num,double alpha,double beta,String test_path,int test_num,String test_tag){
		int [] HiddenNodeNums = new int[model_num];
		String [] ActivationFuns = new String[model_num]; 
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(model_path)))); 
			int i=0;
			String str;
			while((str=br.readLine())!=null){
				String []ss=str.split(" ");
				HiddenNodeNums[i] = Integer.parseInt(ss[0]);
				ActivationFuns[i] = ss[1];
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		
 		ELM_Classifier [] elms = new ELM_Classifier[model_num];
 		int elm_num=0;
		for(int i=0;i<model_num;i++){
			ELM_Classifier tmp = new ELM_Classifier(Exp1.factors_num,Exp1.labels_num);
			tmp.ELM_Train(train_path, HiddenNodeNums[i], ActivationFuns[i],train_num);
			if(tmp.ELM_Filter(filter_path,filter_num,alpha,beta)){
				elms[elm_num] = tmp;
				elm_num++;
			}
		}
		int [][] tags = new int[elm_num][test_num];
		for(int i=0;i<elm_num;i++){
			tags[i] = elms[i].ELM_Predict(test_path, test_num);
		}
		int [] labels = new int[test_num];
		for(int i=0;i<test_num;i++){
			int [] count = new int[Exp1.labels_num];
			for(int j=0;j<Exp1.labels_num;j++){
				count[j]=0;
			}
			for(int j=0;j<elm_num;j++){
				count[tags[j][i]]++;
			}
//			System.out.println(count[0]);
//			System.out.println(count[1]);
			int max=-1;
			int max_index = -1;
			for(int j=0;j<Exp1.labels_num;j++){
				if(count[j]>max){
					max = count[j];
					max_index = j;
				}
			}
			labels[i] = max_index;
		}
		
		int [] expected_labels = new int[test_num];
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(test_tag))));
			String str;
			int j=0;
			while((str=br.readLine())!=null){
				Double tmp = Double.parseDouble(str);
				expected_labels[j]=tmp.intValue();
				j++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int right_num=0;
		for(int i=1;i<=test_num;i++){
			if(labels[i-1]==expected_labels[i-1]){
				right_num++;
			}
		}
		System.out.println("model file: " + model_path);
		System.out.println(String.format("%.6f", right_num/(double)test_num));
		System.out.println("==================================");
		
	}
	
}
