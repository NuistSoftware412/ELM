import Jama.Matrix;

/*
 * 0 REGRESSION
 * >=1 CLASSIFICATION, it also represents number of labels
 * args[0]: ELM Type
 * args[1]: Data File Name Prefix
 * args[2]: N, number of sub-ELMs
 * args[3]: n, hidden node number of each sub-ELM
 * args[4]: Activation Function
 */
public class D_ELM {
	public static void main(String args[]){
		if(args.length!=5){
			System.out.println("Five paramters needed!");
			System.exit(0);
		}
		int ELM_Type = Integer.parseInt(args[0]);
		String FilePrefix = args[1];
		int N = Integer.parseInt(args[2]);
		int n = Integer.parseInt(args[3]);
		String af = args[4];
		long startTime = System.nanoTime();
		if(ELM_Type==0){
			ELM_Regression [] ELMs = new ELM_Regression[N];
			Matrix valid_data = Funs.load(FilePrefix+(N+1));
			Matrix T = valid_data.getMatrix(0, valid_data.getRowDimension()-1,0,0);
			double [][] results = new double[N][];
			for(int i=0;i<N;i++){
				long startTime2 = System.nanoTime();
				ELM_Regression elm = new ELM_Regression();
				elm.ELM_Train(FilePrefix+(i+1), n, af);
				long consumingTime2 = System.nanoTime() - startTime2;
				System.out.println("N="+i+"; Consumed Time=" + (double)consumingTime2/1000000000);
				ELMs[i]=elm;
				results[i]=elm.ELM_Predict(valid_data);
			}
			Matrix P = new Matrix(results);			
			Matrix Omiga = Funs.pinv(P.transpose()).times(T);
			String ELM = Funs.ELM_Merge(ELMs,Omiga);
//			System.out.println(ELM);
		}else{
			ELM_Classification [] ELMs = new ELM_Classification[N];
			for(int i=0;i<N;i++){
				long startTime2 = System.nanoTime();
				ELM_Classification elm = new ELM_Classification();
				elm.ELM_Train(FilePrefix+(i+1), ELM_Type, n, af);
				long consumingTime2 = System.nanoTime() - startTime2;
				System.out.println("N="+i+"; Consumed Time=" + (double)consumingTime2/1000000000);
				ELMs[i] = elm;
			}
			String ELM = Funs.ELM_Merge(ELMs);
//			System.out.println(ELM);
		}
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Consumed Time=" + (double)consumingTime/1000000000);
	}
}
