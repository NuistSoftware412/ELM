package ELM_Classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;
import com.sun.org.apache.xpath.internal.operations.And;

import Jama.Matrix;
import Jama.SingularValueDecomposition;


public class ELM_Classifier {
	private double MACHEPS = 2E-16;
	private int Columns;
	private int Labels;
	private Matrix InputWeight;
	private Matrix BiasofHiddenNeurons;
	private Matrix OutputWeight;
	private String ActivationFun;
	public ELM_Classifier(int Columns,int Labels){
		this.Columns = Columns;
		this.Labels = Labels;
	}
	public void ELM_Train(String TD_File, int NumofHiddenNeurons, String ActivationFun, int Row){
		double [][] train_data = loadFile(TD_File, Row, this.Columns+1);
		Matrix train = new Matrix(train_data);
//		train.print(8, 5);
		Matrix T = train.getMatrix(0, Row-1, 0, 0).transpose();
//		T.print(T.getRowDimension(), 5);
		Matrix P = train.getMatrix(0, Row-1, 1, this.Columns).transpose();
//		P.print(P.getRowDimension(), 5);
		int  NumberofTrainingData = Row;
		int NumberofInputNeurons = this.Columns;
		double [] arr = new double[Row];
		for(int i=0;i<Row;i++){
			arr[i] = train_data[i][0];
		}
		Arrays.sort(arr);
		double [][] arr2 = new double[1][Row];
		for(int i=0;i<Row;i++){
			arr2[0][i] = arr[i];
		}
		Matrix sorted_target = new Matrix(arr2);
//		sorted_target.print(sorted_target.getRowDimension(), 5);
	
		double [][] label_arr = new double[1][this.Labels];
		Matrix label = new Matrix(label_arr);
		for(int i=1;i<=this.Labels;i++){
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
//		System.out.println(number_class);
//		T.print(T.getRowDimension(), 5);
//		label.print(label.getRowDimension(), 5);
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
//		temp_T.print(temp_T.getRowDimension(), 5);
		
		Matrix InputWeight = Matrix.random(NumofHiddenNeurons, NumberofInputNeurons);
		for(int i=1;i<=NumofHiddenNeurons;i++){
			for(j=1;j<=NumberofInputNeurons;j++){
				double tmp = InputWeight.get(i-1, j-1)*2-1;
				InputWeight.set(i-1, j-1, tmp);
			}
		}
//		InputWeight.print(InputWeight.getRowDimension(), 5);
		
		Matrix BiasofHiddenNeurons = Matrix.random(NumofHiddenNeurons, 1);
//		BiasofHiddenNeurons.print(BiasofHiddenNeurons.getRowDimension(), 5);
		Matrix tempH = InputWeight.times(P);
//		tempH.print(tempH.getRowDimension(), 5);
		Matrix BiasMatrix = new Matrix(NumofHiddenNeurons,NumberofTrainingData);
		for(int i=1;i<=NumberofTrainingData;i++){
			BiasMatrix.setMatrix(0, NumofHiddenNeurons-1, i-1,i-1,BiasofHiddenNeurons);
		}
//		BiasMatrix.print(BiasMatrix.getRowDimension(), 5);
		tempH=tempH.plus(BiasMatrix);
//		tempH.print(tempH.getRowDimension(), 5);
		Matrix H = calculateFun(ActivationFun, tempH);
		Matrix OutputWeight=pinv(H.transpose()).times(temp_T.transpose());
		
//		InputWeight.print(InputWeight.getRowDimension(), 5);
//		BiasofHiddenNeurons.print(BiasofHiddenNeurons.getRowDimension(), 5);
//		OutputWeight.print(OutputWeight.getRowDimension(), 5);
		
		this.InputWeight = InputWeight;
		this.BiasofHiddenNeurons = BiasofHiddenNeurons;
		this.OutputWeight = OutputWeight;
		this.ActivationFun = ActivationFun;
	}
	
	public boolean ELM_Filter(String file,int Row,double alpha,double beta){
		int [] labs = new int[Row];
		Matrix test_data = new Matrix(loadFile(file, Row, this.Columns+1));
//		test_data.print(test_data.getRowDimension(), test_data.getColumnDimension());
		Matrix P = test_data.getMatrix(0, Row-1,1,this.Columns).transpose();
		Matrix T = test_data.getMatrix(0, Row-1,0,0).transpose();
		
		int NumberofTestingData = Row;
		Matrix tempH_test = this.InputWeight.times(P);
		Matrix BiasMatrix = new Matrix(this.BiasofHiddenNeurons.getRowDimension(),NumberofTestingData);
		for(int i=1;i<=NumberofTestingData;i++){
			for(int j=1;j<=this.BiasofHiddenNeurons.getRowDimension();j++){
				BiasMatrix.set(j-1, i-1, this.BiasofHiddenNeurons.get(j-1, 0));
			}
		}
		tempH_test=tempH_test.plus(BiasMatrix);
		Matrix H_test = calculateFun(this.ActivationFun, tempH_test);
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
		int count=0;
		for(int i=0;i<Row;i++){
			Double tmp = T.get(0, i);
			if(tmp.intValue()==labs[i]){
				count++;
			}
		}
		double acc = (double)count/(double)Row;
		System.out.println("Filter accuracy: " + String.format("%.6f", acc));
		if(acc>alpha && acc<beta){
			return true;
		}else{
			return false;
		}
	}
	
	
	public int [] ELM_Predict(String file,int Row){
		int [] labs = new int[Row];
		Matrix test_data = new Matrix(loadFile(file, Row, this.Columns));
//		test_data.print(test_data.getRowDimension(), test_data.getColumnDimension());
		Matrix P = test_data.transpose();
		int NumberofTestingData = Row;
		Matrix tempH_test = this.InputWeight.times(P);
		Matrix BiasMatrix = new Matrix(this.BiasofHiddenNeurons.getRowDimension(),NumberofTestingData);
		for(int i=1;i<=NumberofTestingData;i++){
			for(int j=1;j<=this.BiasofHiddenNeurons.getRowDimension();j++){
				BiasMatrix.set(j-1, i-1, this.BiasofHiddenNeurons.get(j-1, 0));
			}
		}
		tempH_test=tempH_test.plus(BiasMatrix);
		Matrix H_test = calculateFun(this.ActivationFun, tempH_test);
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
	
	private double[][] loadFile(String path,int line,int column){
		double [][] x=new double [line][column];
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			int i=0;
			String str;
			while((str=br.readLine())!=null){
				String []ss=str.split(" ");
				for(int j=0;j<ss.length;j++){
					x[i][j]=Double.parseDouble(ss[j]);
				}
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		return x;
	}
	
	
	private Matrix pinv(Matrix x) {
		  if (x.rank() < 1)
		   return null;
		  if (x.getColumnDimension() > x.getRowDimension())
		   return pinv(x.transpose()).transpose();
		  SingularValueDecomposition svdX = new SingularValueDecomposition(x);
		  double[] singularValues = svdX.getSingularValues();
		  double tol = Math.max(x.getColumnDimension(), x.getRowDimension()) * singularValues[0] * MACHEPS;
		  double[] singularValueReciprocals = new double[singularValues.length];
		  for (int i = 0; i < singularValues.length; i++)
		   singularValueReciprocals[i] = Math.abs(singularValues[i]) < tol ? 0 : (1.0 / singularValues[i]);
		  double[][] u = svdX.getU().getArray();
		  double[][] v = svdX.getV().getArray();
		  int min = Math.min(x.getColumnDimension(), u[0].length);
		  double[][] inverse = new double[x.getColumnDimension()][x.getRowDimension()];
		  for (int i = 0; i < x.getColumnDimension(); i++)
		   for (int j = 0; j < u.length; j++)
		    for (int k = 0; k < min; k++)
		     inverse[i][j] += v[i][k] * singularValueReciprocals[k] * u[j][k];
		  return new Matrix(inverse);
		 }
	
	private Matrix calculateFun(String ActivationFun,Matrix tempH){
		Matrix H = new Matrix(tempH.getRowDimension(),tempH.getColumnDimension());
		String af = ActivationFun.toLowerCase();
		if(af.equals("sig") || af.equals("sigmoid")){
			for(int i=0;i<tempH.getRowDimension();i++){
				for(int j=0;j<tempH.getColumnDimension();j++){
					double tmp = 1/(1+Math.exp(0-tempH.get(i, j)));
					H.set(i, j, tmp);
				}
			}
		}else if(af.equals("sin") || af.equals("sine")){
			for(int i=0;i<tempH.getRowDimension();i++){
				for(int j=0;j<tempH.getColumnDimension();j++){
					double tmp = Math.sin(tempH.get(i, j));
					H.set(i, j, tmp);
				}
			}
		}else if(af.equals("hardlim")){
			for(int i=0;i<tempH.getRowDimension();i++){
				for(int j=0;j<tempH.getColumnDimension();j++){
					double tmp = Math.exp(tempH.get(i, j));
					H.set(i, j, tmp);
				}
			}
		}else{
			System.out.println("Unknown activation function!");
		}
		return H;
	}
}
