import Jama.Matrix;


public class Single {
	public static void main(String args[]){
		int trn_row=1800;
		int tst_row=510;
		Matrix train = Train.load("/home/jychen/work/Entropy/datasets/segment/train.data", trn_row);
		Matrix test = Train.load("/home/jychen/work/Entropy/datasets/segment/test.data",tst_row);
		ELM_Classifier elm = new ELM_Classifier(Train.atts_num, Train.labels_num,train,
				train.getMatrix(0, trn_row-1, 1, Train.atts_num));
		elm.ELM_Train(Train.NumofHiddenNeurons,Train.ActivationFun);
		int [] labs_trn = elm.ELM_Predict();
		int num=0;
		for(int i=0;i<trn_row;i++){
			if((int)train.get(i, 0)==labs_trn[i]){
				num++;
			}
		}
		System.out.println("training accuracy: " + (double)num/(double)trn_row);
		elm.setTest(test.getMatrix(0, tst_row-1,1,Train.atts_num));
		int [] labs_tst=elm.ELM_Predict();
		num=0;
		for(int i=0;i<tst_row;i++){
			if((int)test.get(i, 0)==labs_tst[i]){
				num++;
			}
		}
		System.out.println("testing accuracy: " + (double)num/(double)tst_row);
	}
}
