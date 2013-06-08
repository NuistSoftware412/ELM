
import Jama.Matrix;


public class Test {
	public static void main(String argsString []){
		Test.test(510, "/home/jychen/work/Entropy/datasets/segment/test.data");
	}
	public static void test(int testNum,String testFile){
		Matrix tst_d = Train.load(testFile, testNum);
		System.out.println("Training....");
		ELM_Classifier [][] elms = Train.train();
		System.out.println("Predicting...");
		double [][][] properties = new double[Train.T][testNum][Train.labels_num];
		double [][] entropies = new double[Train.T][testNum];
		for(int t=0;t<Train.T;t++){
			int [][] labs_tst = new int[Train.J][testNum];
			for(int j=0;j<Train.J;j++){
				ELM_Classifier elm = elms[t][j];
				elm.setTest(tst_d.getMatrix(0, testNum-1,1,Train.atts_num));
				labs_tst[j]=elm.ELM_Predict();
				Train.writeToFile("/home/jychen/work/Entropy/tmp/test/labs_"+t+"_"+j, labs_tst[j], testNum);
			}
			properties[t] = Train.calProperty(labs_tst,testNum);
			entropies[t] = Train.calEntropy(properties[t], testNum);
			
		}
		double [][] nentropies = nEntropy(entropies, testNum);
		double [] sigma = calSigma(nentropies, testNum);
		Train.writeToFile("/home/jychen/work/Entropy/tmp/test", properties, entropies, 
				nentropies, sigma, testNum);
		int [] labs = combine(nentropies,properties,sigma,testNum);
		Train.writeToFile("/home/jychen/work/Entropy/tmp/test/labs", labs, testNum);
		int right_num=0;
		for(int i=0;i<testNum;i++){
			if((int)tst_d.get(i, 0)==labs[i]){
				right_num++;
			}
		}
		System.out.println("testing accuracy: " + (double)right_num/(double)testNum);
	}
	
	public static double [][] nEntropy(double [][] entropies,int len){
		double [][] nentropies = new double[Train.T][len];
		for(int i=0;i<len;i++){
			double sum=0;
			for(int t=0;t<Train.T;t++){
				sum += entropies[t][i];
			}
			for(int t=0;t<Train.T;t++){
				if(sum==0){
					nentropies[t][i]=0;
				}else{
					nentropies[t][i]=entropies[t][i]/sum;
				}
			}
		}
		
		return nentropies;
	}
	
	public static double [] calSigma(double [][] nentropies,int len){
		double [] sigma = new double[len];
		for(int i=0;i<len;i++){
			int min_index=-1;
			int max_index=-1;
			double min=2;
			double max=-1;
			for(int t=0;t<Train.T;t++){
				if(nentropies[t][i]>=max){
					max=nentropies[t][i];
					max_index=t;
				}
				if(nentropies[t][i]<min){
					min=nentropies[t][i];
					min_index=t;
				}
			}
			sigma[i]=Math.abs(0.5*(double)(max_index-min_index));
		}
		return sigma;
	}
	
	public static int [] combine(double [][] nentropies, double [][][] properties, 
			double []sigma, int len){
		int [] labs = new int[len];
		for(int i=0;i<len;i++){
			int [] TT_id = new int[Train.T];
			int TT=0;
			double sum=0;
			for(int t=0;t<Train.T;t++){
				if(nentropies[t][i]<=sigma[i]){
					TT_id[TT]=t;
					TT++;
					double exp_tmp = nentropies[TT_id[t]][i]+1;
					sum += ((double)1)/exp_tmp;
				}
			}
			double [] alpha = new double[TT];
			for(int tt=0;tt<TT;tt++){
				double exp_tmp = nentropies[TT_id[tt]][i]+1;
				alpha[tt]=(((double)1)/exp_tmp)/sum;
			}
			double tmp=-1;
			int tmp_index=-1;
			for(int k=0;k<Train.labels_num;k++){
				double sum2=0;
				for(int tt=0;tt<TT;tt++){
					sum2 += properties[TT_id[tt]][i][k] * alpha[tt];
				}
				sum2 = sum2/TT;
				if(sum2>=tmp){
					tmp=sum2;
					tmp_index=k;
				}
			}
			labs[i]=tmp_index;
		}
		return labs;
	}
	
}
