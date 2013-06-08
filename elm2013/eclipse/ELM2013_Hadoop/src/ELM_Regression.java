import Jama.Matrix;
public class ELM_Regression {
	
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
	
	
	
	
	public void setInputWeight(Matrix inputWeight) {
		InputWeight = inputWeight;
	}

	public void setBiasofHiddenNeurons(Matrix biasofHiddenNeurons) {
		BiasofHiddenNeurons = biasofHiddenNeurons;
	}

	public void setOutputWeight(Matrix outputWeight) {
		OutputWeight = outputWeight;
	}

	public void setActivationFunction(String activationFunction) {
		ActivationFunction = activationFunction;
	}

	public void ELM_Train(Matrix train,
			int NumberofHiddenNeurons,String ActivationFunction){
		this.ActivationFunction = ActivationFunction.toLowerCase();
		
		int Row = train.getRowDimension();
		int Atts_Num = train.getColumnDimension()-1;
		
		Matrix T = train.getMatrix(0, Row-1, 0, 0).transpose();
		Matrix P = train.getMatrix(0, Row-1, 1, Atts_Num).transpose();
		int NumberofTrainingData = Row;
		int NumberofInputNeurons = Atts_Num;
		
		Matrix InputWeight = Matrix.random(NumberofHiddenNeurons, NumberofInputNeurons);
		for(int i=1;i<=NumberofHiddenNeurons;i++){
			for(int j=1;j<=NumberofInputNeurons;j++){
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
		Matrix OutputWeight=Funs.pinv(H.transpose()).times(T.transpose());
		
		this.InputWeight = InputWeight;
		this.BiasofHiddenNeurons = BiasofHiddenNeurons;
		this.OutputWeight = OutputWeight;				
	}
	
	
	public double [] ELM_Predict(Matrix test){
		int Row = test.getRowDimension();
		double [] results = new double[Row];
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
		Matrix H_test = Funs.calculateFun(tempH_test, this.ActivationFunction);
		Matrix TY = (H_test.transpose().times(this.OutputWeight)).transpose();
		for(int i=0;i<TY.getColumnDimension();i++){
			results[i] = TY.get(0, i);
		}
		return results;
	}
		
}
