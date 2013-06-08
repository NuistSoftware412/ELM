import org.omg.CORBA.TRANSACTION_MODE;

import Jama.Matrix;



public class Voting {
	public static void main(String args[]){
		int trn_row=1800;
		int tst_row=510;
		int [][] trn_tags=new int[Train.T][trn_row];
		int [][] tst_tags=new int[Train.T][trn_row];
		Matrix train = Train.load("/home/jychen/work/Entropy/datasets/segment/train.data", trn_row);
		Matrix test = Train.load("/home/jychen/work/Entropy/datasets/segment/test.data",tst_row);
		for(int t=0;t<Train.T;t++){
			System.out.println("training...");
			Matrix tmp=Voting.resampling(train);
			ELM_Classifier elm = new ELM_Classifier(Train.atts_num, Train.labels_num,tmp,
					train.getMatrix(0, trn_row-1, 1, Train.atts_num));
			elm.ELM_Train(Train.NumofHiddenNeurons,Train.ActivationFun);
			trn_tags[t]=elm.ELM_Predict();
			System.out.println("predicting...");
			elm.setTest(test.getMatrix(0, tst_row-1,1,Train.atts_num));
			tst_tags[t]=elm.ELM_Predict();
		}
	
		int [] labels_trn = new int[trn_row];		
		for(int i=0;i<trn_row;i++){
			int [] count = new int[Train.labels_num];
			for(int k=0;k<Train.labels_num;k++){
				count[k]=0;
			}
			for(int t=0;t<Train.T;t++){
				count[trn_tags[t][i]]++;
			}
			int max=-1;
			int max_index = -1;
			for(int k=0;k<Train.labels_num;k++){
				if(count[k]>max){
					max = count[k];
					max_index = k;
				}
			}
			labels_trn[i] = max_index;
		}
		
		int [] labels_tst = new int[tst_row];		
		for(int i=0;i<tst_row;i++){
			int [] count = new int[Train.labels_num];
			for(int k=0;k<Train.labels_num;k++){
				count[k]=0;
			}
			for(int t=0;t<Train.T;t++){
				count[tst_tags[t][i]]++;
			}
			int max=-1;
			int max_index = -1;
			for(int k=0;k<Train.labels_num;k++){
				if(count[k]>max){
					max = count[k];
					max_index = k;
				}
			}
			labels_tst[i] = max_index;
		}
		
		
		int num=0;
		for(int i=0;i<trn_row;i++){
			if((int)train.get(i, 0)==labels_trn[i]){
				num++;
			}
		}
		System.out.println("training accuracy: " + (double)num/(double)trn_row);
		
		num=0;
		for(int i=0;i<tst_row;i++){
			if((int)test.get(i, 0)==labels_tst[i]){
				num++;
			}
		}
		System.out.println("testing accuracy: " + (double)num/(double)tst_row);
		
	}
	
	public static Matrix resampling(Matrix m){
		Matrix datam = new Matrix(Train.NN,Train.atts_num+1);
		int [] ids = new int[Train.NN]; 
		int count=0;
		while(count<Train.NN){
			int beta = (int)(Math.random() * Train.N);
			if(!find_id2(ids, beta, Train.NN)){
				ids[count]=beta;
				count++;
			}
			
		}
		for(int i=0;i<Train.NN;i++){
			int line = ids[i];
			Matrix tmp = m.getMatrix(line, line, 0, Train.atts_num);
			datam.setMatrix(i, i, 0, Train.atts_num, tmp);
		}
		return datam;
	}
	
	private static boolean find_id2(int [] ids, int i, int len){
		for(int j=0;j<len;j++){
			if(ids[j]==i)
				return true;
		}
		return false;
	}
}
