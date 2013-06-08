package zju.cs.icca2013;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.Text;

import Jama.Matrix;

public class Ensemble_Predictor {
	private static int num_per_line = 1000;
	public static String multi_predict(String model,String ds){
		String [] tmp = ds.split(" ");
		int Row = tmp.length/3;
		System.out.println(Row);
		double [][] td = new double[Row][3];
		for(int i=0;i<Row;i++){
			for(int j=0;j<3;j++){
				td[i][j] = Double.parseDouble(tmp[i*3+j]);
			}
		}
		String parameters = "";
		for(int i=0;i<Data.elm_parameter.length;i++){
			if(Data.elm_parameter[i].startsWith(model)){
				parameters = Data.elm_parameter[i];
				break;
			}
		}
		Matrix test_data = new Matrix(td);
		String [] tmp1 = parameters.split(" ");
		String ActivationFun = tmp1[0].split("_")[1];
		String [] tmp2 = tmp1[1].split("_");
		String [] iw_str = tmp2[0].split(":");
		String [] bhn_str = tmp2[1].split(":");
		String [] ow_str = tmp2[2].split(":");
		int iw_row = Integer.parseInt(iw_str[0]);
		int iw_col = Integer.parseInt(iw_str[1]);
		Matrix InputWeight = new Matrix(iw_row, iw_col);
		for(int j=2;j<iw_str.length;j++){
			double item = Double.parseDouble(iw_str[j]);
			InputWeight.set((j-2)/iw_col, (j-2)%iw_col, item);
		}
		int bhn_row = Integer.parseInt(bhn_str[0]);
		Matrix BiasofHiddenNeurons = new Matrix(bhn_row,1);
		for(int j=1;j<bhn_str.length;j++){
			double item = Double.parseDouble(bhn_str[j]);
			BiasofHiddenNeurons.set(j-1, 0, item);
		}
		int ow_row = Integer.parseInt(ow_str[0]);
		int ow_col = Integer.parseInt(ow_str[1]);
		Matrix OutputWeight = new Matrix(ow_row,ow_col);
		for(int j=2;j<ow_str.length;j++){
			double item = Double.parseDouble(ow_str[j]);
			OutputWeight.set((j-2)/ow_col, (j-2)%ow_col, item);
		}
			
		int [] labs = Ensemble_Predictor.predict(test_data, InputWeight, BiasofHiddenNeurons, ActivationFun, OutputWeight);
		String labs_str = "";
		for(int j=0;j<labs.length;j++){
			labs_str += labs[j] + " ";
		}
		return labs_str;
	}
	
	
	private static int [] predict(Matrix test_data,Matrix InputWeight,
			Matrix BiasofHiddenNeurons,String ActivationFun,Matrix OutputWeight){
		int Row = test_data.getRowDimension();
		int [] labs = new int[Row];
		Matrix P = test_data.transpose();
		int NumberofTestingData = Row;
		Matrix tempH_test = InputWeight.times(P);
		Matrix BiasMatrix = new Matrix(BiasofHiddenNeurons.getRowDimension(),NumberofTestingData);
		for(int i=1;i<=NumberofTestingData;i++){
			for(int j=1;j<=BiasofHiddenNeurons.getRowDimension();j++){
				BiasMatrix.set(j-1, i-1, BiasofHiddenNeurons.get(j-1, 0));
			}
		}
		tempH_test=tempH_test.plus(BiasMatrix);
		Matrix H_test = Ensemble_Predictor.calculateFun(ActivationFun, tempH_test);
		Matrix TY = (H_test.transpose().times(OutputWeight)).transpose();
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
	
	private static Matrix calculateFun(String ActivationFun,Matrix tempH){
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
	
	
	
	static public String combine(Iterator<Text> values){
		int [][] v = new int[Ensemble_Predictor.num_per_line][5];
		while(values.hasNext()){
			String value = values.next().toString();
			String [] items = value.split(" ");
			for(int i=0;i<items.length;i++){
				v[i][Integer.parseInt(items[i])]++;
			}
		}
		String tags="";
		for(int i=0;i<Ensemble_Predictor.num_per_line;i++){
			int tag=0;
			int max=0;
			for(int j=0;j<5;j++){
				if(v[i][j]>max){
					tag=j;
					max=v[i][j];
				}
			}
			tags += tag + " ";
		}
		return tags;
	}
	
	
	static public String combine2(Iterator<String> values){
		int [][] v = new int[Ensemble_Predictor.num_per_line][5];
		while(values.hasNext()){
			String value = values.next();
			String [] items = value.split(" ");
			for(int i=0;i<items.length;i++){
				v[i][Integer.parseInt(items[i])]++;
			}
		}
		String tags="";
		for(int i=0;i<Ensemble_Predictor.num_per_line;i++){
			int tag=0;
			int max=0;
			for(int j=0;j<5;j++){
				if(v[i][j]>max){
					tag=j;
					max=v[i][j];
				}
			}
			tags += tag + " ";
		}
		return tags;
	}
}
