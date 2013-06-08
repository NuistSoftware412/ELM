import java.util.Arrays;
import Jama.Matrix;

public class ELM_Classification {
	private Matrix InputWeight;
	private Matrix BiasofHiddenNeurons;
	private Matrix OutputWeight;
	private String ActivationFunction;
	
	public Matrix getInputWeight() {
		return this.InputWeight;
	}

	public Matrix getBiasofHiddenNeurons() {
		return this.BiasofHiddenNeurons;
	}

	public Matrix getOutputWeight() {
		return this.OutputWeight;
	}

	public String getActivationFunction() {
		return this.ActivationFunction;
	}

	public void ELM_Train(Matrix train, int Label_Num,
			int NumberofHiddenNeurons,String ActivationFunction){
		this.ActivationFunction = ActivationFunction.toLowerCase();
		int Row = train.getRowDimension();
		int Atts_Num = train.getColumnDimension()-1;
		
		Matrix T = train.getMatrix(0, Row-1, 0, 0).transpose();
		Matrix P = train.getMatrix(0, Row-1, 1, Atts_Num).transpose();
		int  NumberofTrainingData = Row;
		int NumberofInputNeurons = Atts_Num;
		double [] arr = new double[Row];
		for(int i=0;i<Row;i++){
			arr[i] = train.get(i, 0);
		}
		Arrays.sort(arr);
		double [][] arr2 = new double[1][Row];
		for(int i=0;i<Row;i++){
			arr2[0][i] = arr[i];
		}
		Matrix sorted_target = new Matrix(arr2);
	
		double [][] label_arr = new double[1][Label_Num];
		Matrix label = new Matrix(label_arr);
		for(int i=1;i<=Label_Num;i++){
			label.set(0, i-1, sorted_target.get(0, 0));
		}
		int j=1;
		for(int i = 1;i<NumberofTrainingData;i++){
			if(sorted_target.get(0, i-1)!=label.get(0,j-1)){
				j++;
				label.set(0, j-1, sorted_target.get(0, i-1));
			}
		}
		int number_class = j;
		int NumberofOutputNeurons=number_class;
		Matrix temp_T = new Matrix(NumberofOutputNeurons,NumberofTrainingData);
		for(int i=1;i<=NumberofTrainingData;i++){
			for(j=1;j<=number_class;j++){
				if(label.get(0, j-1)==T.get(0, i-1)){
					break;
				}
			}
			temp_T.set(j-1, i-1, 1);
		}
		for(int i=1;i<=NumberofOutputNeurons;i++){
			for(j=1;j<=NumberofTrainingData;j++){
				double tmp = temp_T.get(i-1, j-1)*2-1;
				temp_T.set(i-1, j-1, tmp);
			}
		}
		
		Matrix InputWeight = Matrix.random(NumberofHiddenNeurons, NumberofInputNeurons);
		for(int i=1;i<=NumberofHiddenNeurons;i++){
			for(j=1;j<=NumberofInputNeurons;j++){
				double tmp = InputWeight.get(i-1, j-1)*2-1;
				InputWeight.set(i-1, j-1, tmp);
			}
		}
		
		Matrix BiasofHiddenNeurons = Matrix.random(NumberofHiddenNeurons, 1);
		Matrix tempH = InputWeight.times(P);
		Matrix BiasMatrix = new Matrix(NumberofHiddenNeurons,NumberofTrainingData);
		for(int i=1;i<=NumberofTrainingData;i++){
			BiasMatrix.setMatrix(0, NumberofHiddenNeurons-1, i-1,i-1,BiasofHiddenNeurons);
		}
		tempH=tempH.plus(BiasMatrix);
		Matrix H = Funs.calculateFun(tempH,this.ActivationFunction);
		Matrix OutputWeight=Funs.pinv(H.transpose()).times(temp_T.transpose());
		
		this.InputWeight = InputWeight;
		this.BiasofHiddenNeurons = BiasofHiddenNeurons;
		this.OutputWeight = OutputWeight;				
	}
	
	public int [] ELM_Predict(String TestingData_File){
		Matrix test = Funs.load(TestingData_File);
		int Row = test.getRowDimension();
		int [] labs = new int[Row];
		Matrix P = test.getMatrix(0, Row-1, 1, test.getColumnDimension()-1).transpose();
		int NumberofTestingData = Row;
		Matrix tempH_test = this.InputWeight.times(P);
		Matrix BiasMatrix = new Matrix(this.BiasofHiddenNeurons.getRowDimension(),NumberofTestingData);
		for(int i=1;i<=NumberofTestingData;i++){
			for(int j=1;j<=this.BiasofHiddenNeurons.getRowDimension();j++){
				BiasMatrix.set(j-1, i-1, this.BiasofHiddenNeurons.get(j-1, 0));
			}
		}
		tempH_test=tempH_test.plus(BiasMatrix);
		Matrix H_test = Funs.calculateFun(tempH_test,this.ActivationFunction);
		Matrix TY = (H_test.transpose().times(this.OutputWeight)).transpose();
		for(int i=1;i<=TY.getColumnDimension();i++){
			int tmp=1;
			double tmp_v = -10000;
			for(int j=1;j<=TY.getRowDimension();j++){
				if(TY.get(j-1, i-1)>=tmp_v){
					tmp_v = TY.get(j-1, i-1);
					tmp = j-1;
				}
			}
			labs[i-1] = tmp;
		}
		return labs;
	}
	
}
