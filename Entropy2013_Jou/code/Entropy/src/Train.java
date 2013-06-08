import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import Jama.Matrix;


public class Train {
	public static int T=9;
	public static int J=7;
	public static int labels_num=7;
	public static int atts_num=19;
	public static int N=1800;
	public static int NN=900;
	public static int NumofHiddenNeurons=10;
	public static String ActivationFun="sin";	
	public static String file_trn = "/home/jychen/work/Entropy/datasets/segment/train.data";
	public static Matrix L;
	
	public static ELM_Classifier[][] train(){
		Train alg = new Train();
		ELM_Classifier [][] elms = new ELM_Classifier[Train.T][Train.J];
		Train.L = Train.load(file_trn,Train.N);		
		double [][][] properties = new double[Train.T][N][Train.labels_num];
		double [][] entropies = new double[Train.T][N];
		double [] weights = alg.initWeights();
		for(int t=0;t<Train.T;t++){
			System.out.println("Begin iteraction: " + t);
			int [][] labs_trn = new int[Train.J][Train.N];
			for(int j=0;j<Train.J;j++){
				Matrix resample = alg.resampling(weights);
				ELM_Classifier elm = new ELM_Classifier(Train.atts_num, Train.labels_num,resample,
						Train.L.getMatrix(0, Train.N-1, 1, Train.atts_num));
				elm.ELM_Train(Train.NumofHiddenNeurons,Train.ActivationFun);
				labs_trn[j] = elm.ELM_Predict();
				
				int num=0;
				for(int i=0;i<N;i++){
					if((int)L.get(i, 0)==labs_trn[j][i]){
						num++;
					}
				}
				System.out.println("t="+t+" j="+j+": " + (double)num/(double)N);
				
				
				elms[t][j]=elm;
				Train.writeToFile("/home/jychen/work/Entropy/tmp/train/labs_"+t+"_"+j, labs_trn[j], N);
			}
			properties[t] = Train.calProperty(labs_trn,N);
			entropies[t] = Train.calEntropy(properties[t],N);
			Train.writeToFile("weights_"+t, weights, N);
			weights = alg.calWeight(entropies[t],weights);
		}
		
		double [][] nentropies = Test.nEntropy(entropies,N);		
		double [] sigma = Test.calSigma(nentropies, N);
		Train.writeToFile("/home/jychen/work/Entropy/tmp/train", properties, entropies, nentropies,sigma, N);
			
		int [] labs = Test.combine(nentropies,properties,sigma,N);
		Train.writeToFile("/home/jychen/work/Entropy/tmp/train/labs", labs, N);
		int right_num=0;
		for(int i=0;i<N;i++){
			if((int)L.get(i, 0)==labs[i]){
				right_num++;
			}
		}
		System.out.println("training accuracy: " + (double)right_num/(double)N);
		
		return elms;
	}
	
	
	
    public static Matrix load(String path,int Row){
    	double [][] trn_d=new double [Row][atts_num+1];
    	try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			int i=0;
			String str;
			while((str=br.readLine())!=null){
				String []ss=str.split(" ");
				for(int j=0;j<atts_num+1;j++){
					trn_d[i][j]=Double.parseDouble(ss[j]);
				}
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		return new Matrix(trn_d);
	}
    
	public Matrix resampling(double [] weights){
		Matrix datam = new Matrix(NN,atts_num+1);
		int [] ids = new int[NN]; 
		int count=0;
		double alpha = max(weights, N);
		while(count<NN){
			double beta = Math.random() * alpha;
			int [] weights_tmp = new int[N];
			int len=0;
			for(int i=0;i<N;i++){
				if(weights[i]>=beta && !find_id(ids, i, count)){
					weights_tmp[len]=i;
					len++;
				}
			}
			int gama = (int)Math.random()*len;
			ids[count]=(int)weights_tmp[gama];
			count++;
		}
		for(int i=0;i<NN;i++){
			int line = ids[i];
			Matrix tmp = L.getMatrix(line, line, 0, atts_num);
			datam.setMatrix(i, i, 0, atts_num, tmp);
		}
		return datam;
	}
	
	private boolean find_id(int [] ids, int i, int len){
		for(int j=0;j<len;j++){
			if(ids[j]==i)
				return true;
		}
		return false;
	}
	
	private double max(double [] d,int len){
		double tmp=0;
		for(int i=0;i<len;i++){
			if(d[i]>tmp){
				tmp=d[i];
			}
		}
		return tmp;
	}
	
	public static double [][] calProperty(int [][] labs, int len){
		double [][] properties = new double[len][labels_num];
		for(int i=0;i<len;i++){
			int [] count = new int[labels_num];
			for(int l=0;l<labels_num;l++){
				count[l]=0;
			}
			for(int j=0;j<J;j++){
				count[labs[j][i]]++;
			}
			for(int l=0;l<labels_num;l++){
				properties[i][l] = ((double)count[l])/((double)J);
			}
		}
		return properties;
	}
	
	public static double [] calEntropy(double [][] properties,int len){
		double [] entropies = new double[len];
		for(int i=0;i<len;i++){
			entropies[i]=0;
			for(int l=0;l<labels_num;l++){
				if(properties[i][l]!=0 && properties[i][l]!=1){
					entropies[i] += -properties[i][l]*Math.log(properties[i][l]);
				}
			}
		}
		return entropies;
	}
	
	public double [] initWeights(){
		double [] weights = new double[N];
		for(int i=0;i<N;i++){
			weights[i] = ((double)1)/((double)N);
		}
		return weights;
	}
	public double [] calWeight(double [] entropies, double [] weights){
		double sum=0;
		for(int i=0;i<N;i++){
			double tmp = Math.exp(entropies[i]);
			weights[i] = weights [i] * tmp;
			sum += weights[i];
		}
//		System.out.println(weights[N-1]);
		for(int i=0;i<N;i++){
			weights[i] = weights[i]/sum;
		}
//		System.out.println(weights[N-1]);
		return weights;
	}
	
	public static void writeToFile(String dir, int [] labs,int len){
		String out_lab = dir;
		try{
			BufferedWriter bl = new BufferedWriter(new FileWriter(out_lab));
			for(int i=0;i<len;i++){
				bl.write(labs[i]+"\n");
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
	
	public static void writeToFile(String dir, double [] labs,int len){
		String out_lab = dir;
		try{
			BufferedWriter bl = new BufferedWriter(new FileWriter(out_lab));
			for(int i=0;i<len;i++){
				bl.write(labs[i]+"\n");
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
	
	public static void writeToFile(String dir,double [][][] properties, double [][] entropies, 
			double [][] nentropies,double [] sigma,int len){
		
		try {
			for(int t=0;t<Train.T;t++){
				String path = dir + "/properties_" + t;
				BufferedWriter bw= new BufferedWriter(new FileWriter(path));
				for(int i=0;i<len;i++){
					String tmp="";
					for(int k=0;k<Train.labels_num;k++){
						tmp = tmp + properties[t][i][k] + " ";
					}
					bw.write(tmp+"\n");
				}
				bw.close();
			}
			String path2 = dir + "/entropies";
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(path2));
			for(int i=0;i<len;i++){
				String tmp2="";
				for(int t=0;t<Train.T;t++){
					tmp2 = tmp2 + entropies[t][i] + " ";
				}
				bw2.write(tmp2 + "\n");
			}
			bw2.close();
			String path3 = dir + "/nentropies";
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(path3));
			for(int i=0;i<len;i++){
				String tmp3="";
				for(int t=0;t<Train.T;t++){
					tmp3 = tmp3 + entropies[t][i] + " ";
				}
				bw3.write(tmp3 + "\n");
			}
			bw3.close();
			String path4 = dir+"/sigma";
			BufferedWriter bw4 = new BufferedWriter(new FileWriter(path4));
			for(int i=0;i<len;i++){
				bw4.write(sigma[i]+"\n");
			}
			bw4.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
}
