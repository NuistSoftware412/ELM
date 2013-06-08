import java.awt.ItemSelectable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import Jama.Matrix;
import Jama.SingularValueDecomposition;


public class Funs {
	public static double MACHEPS = 2E-16;
	public static Matrix pinv(Matrix x) {
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
	
	public static Matrix calculateFun(Matrix tempH, String af){
		Matrix H = new Matrix(tempH.getRowDimension(),tempH.getColumnDimension());
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
	
	public static Matrix load(String path){
		int line=0;
		int column=0;
		try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			String str;
			while((str=br.readLine())!=null){
				line++;
				String []ss=str.split(" ");
				column=ss.length;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}

		double [][] trn_d=new double [line][column];
    	try{   
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))); 
			int i=0;
			String str;
			while((str=br.readLine())!=null){
				String []ss=str.split(" ");
				for(int j=0;j<column;j++){
					trn_d[i][j]=Double.parseDouble(ss[j]);
				}
				++i;
			} 
		}catch (Exception e){
		   e.printStackTrace();
		}
		return new Matrix(trn_d);
	}
	
	public static String ELM_Merge(ELM_Classification [] ELMs){
		int N = ELMs.length;
		if(N==0){
			return "No ELMs Trained\n";
		}
		int ELM_Type=1;
		String ActivationFunction = ELMs[0].getActivationFunction();
		double [][] InputWeight = 
			new double[ELMs[0].getInputWeight().getRowDimension()*N][ELMs[0].getInputWeight().getColumnDimension()];
		double [][] OutputWeight = 
			new double[ELMs[0].getOutputWeight().getRowDimension()*N][ELMs[0].getOutputWeight().getColumnDimension()];
		double [][] BiasofHiddenNeurons = 
			new double[ELMs[0].getBiasofHiddenNeurons().getRowDimension()*N][ELMs[0].getBiasofHiddenNeurons().getColumnDimension()];
		for(int i=0;i<N;i++){
			ELM_Classification elm = ELMs[i];
			for(int j=0;j<elm.getInputWeight().getRowDimension();j++){
				for(int k=0;k<elm.getInputWeight().getColumnDimension();k++){
					InputWeight[i*elm.getInputWeight().getRowDimension()+j][k]=elm.getInputWeight().get(j, k);
				}
			}
			for(int j=0;j<elm.getBiasofHiddenNeurons().getRowDimension();j++){
				for(int k=0;k<elm.getBiasofHiddenNeurons().getColumnDimension();k++){
					BiasofHiddenNeurons[i*elm.getBiasofHiddenNeurons().getRowDimension()+j][k]=elm.getBiasofHiddenNeurons().get(j, k);
				}
			}
			for(int j=0;j<elm.getOutputWeight().getRowDimension();j++){
				for(int k=0;k<elm.getOutputWeight().getColumnDimension();k++){
					OutputWeight[i*elm.getOutputWeight().getRowDimension()+j][k]=elm.getOutputWeight().get(j, k);
				}
			}
		}
		
		return Funs.ELMToString(ELM_Type, ActivationFunction, new Matrix(InputWeight), 
				new Matrix(BiasofHiddenNeurons), new Matrix(OutputWeight));
	}
	
	public static String ELM_Merge(ELM_Regression [] ELMs, Matrix Omiga){
		int N = ELMs.length;
		if(N==0){
			return "No ELMs Trained\n";
		}
		if(N!=Omiga.getRowDimension()){
			return "Omiga error\n";
		}
		
		int ELM_Type=0;
		String ActivationFunction = ELMs[0].getActivationFunction();
		double [][] InputWeight = 
			new double[ELMs[0].getInputWeight().getRowDimension()*N][ELMs[0].getInputWeight().getColumnDimension()];
		double [][] OutputWeight = 
			new double[ELMs[0].getOutputWeight().getRowDimension()*N][ELMs[0].getOutputWeight().getColumnDimension()];
		double [][] BiasofHiddenNeurons = 
			new double[ELMs[0].getBiasofHiddenNeurons().getRowDimension()*N][ELMs[0].getBiasofHiddenNeurons().getColumnDimension()];
		for(int i=0;i<N;i++){
			double w= Omiga.get(i, 0);
			ELM_Regression elm = ELMs[i];
			for(int j=0;j<elm.getInputWeight().getRowDimension();j++){
				for(int k=0;k<elm.getInputWeight().getColumnDimension();k++){
					InputWeight[i*elm.getInputWeight().getRowDimension()+j][k]=elm.getInputWeight().get(j, k);
				}
			}
			for(int j=0;j<elm.getBiasofHiddenNeurons().getRowDimension();j++){
				for(int k=0;k<elm.getBiasofHiddenNeurons().getColumnDimension();k++){
					BiasofHiddenNeurons[i*elm.getBiasofHiddenNeurons().getRowDimension()+j][k]=elm.getBiasofHiddenNeurons().get(j, k);
				}
			}
			for(int j=0;j<elm.getOutputWeight().getRowDimension();j++){
				for(int k=0;k<elm.getOutputWeight().getColumnDimension();k++){
					OutputWeight[i*elm.getOutputWeight().getRowDimension()+j][k]=elm.getOutputWeight().get(j, k)*w;
				}
			}
		}
		
		return Funs.ELMToString(ELM_Type, ActivationFunction, new Matrix(InputWeight), 
				new Matrix(BiasofHiddenNeurons), new Matrix(OutputWeight));
		
	}
	
	public static String ELMToString(int ELM_Type, String ActivationFunction, Matrix InputWeight,
			Matrix BiasofHiddenNeurons, Matrix OutputWeight){
		String s = "Type:" + ELM_Type + "\n";
		s = s + "ActivationFunction:"+ActivationFunction+"\n";
		s = s + "InputWeight:" + "\n";
		for(int i=0;i<InputWeight.getRowDimension();i++){
			String tmp="";
			for(int j=0;j<InputWeight.getColumnDimension();j++){
				tmp = tmp + InputWeight.get(i, j) + " ";
			}
			tmp=tmp+"\n";
			s=s+tmp;
		}
		s=s+"BiasofHiddenNeurons:"+"\n";
		for(int i=0;i<BiasofHiddenNeurons.getRowDimension();i++){
			String tmp="";
			for(int j=0;j<BiasofHiddenNeurons.getColumnDimension();j++){
				tmp = tmp + BiasofHiddenNeurons.get(i, j) + " ";
			}
			tmp=tmp+"\n";
			s=s+tmp;
		}
		s=s+"OutputWeight:"+"\n";
		for(int i=0;i<OutputWeight.getRowDimension();i++){
			String tmp="";
			for(int j=0;j<OutputWeight.getColumnDimension();j++){
				tmp = tmp + OutputWeight.get(i, j) + " ";
			}
			tmp=tmp+"\n";
			s=s+tmp;
		}
		return s;
	}
	
	public static String ELMFormat(int ELM_Type, String ActivationFunction, Matrix InputWeight,
			Matrix BiasofHiddenNeurons, Matrix OutputWeight){
		String s = ELM_Type + ":";
		s = s +ActivationFunction+":";
		s = s + InputWeight.getRowDimension() + ";" + InputWeight.getColumnDimension()+";";
		for(int i=0;i<InputWeight.getRowDimension();i++){
			for(int j=0;j<InputWeight.getColumnDimension();j++){
				s = s + InputWeight.get(i, j) + " ";
			}
		}
		s=s+":";
		
		s = s + BiasofHiddenNeurons.getRowDimension() + ";" + BiasofHiddenNeurons.getColumnDimension()+";";
		for(int i=0;i<BiasofHiddenNeurons.getRowDimension();i++){
			for(int j=0;j<BiasofHiddenNeurons.getColumnDimension();j++){
				s = s + BiasofHiddenNeurons.get(i, j) + " ";
			}
		}
		s=s+":";
		
		s = s + OutputWeight.getRowDimension() + ";" + OutputWeight.getColumnDimension()+";";
		for(int i=0;i<OutputWeight.getRowDimension();i++){
			for(int j=0;j<OutputWeight.getColumnDimension();j++){
				s = s + OutputWeight.get(i, j) + " ";
			}
		}
		
		return s;
	}
	
	public static ELM_Regression deFormatElm_Regression(String elms){
		ELM_Regression elm = new ELM_Regression();
		String [] items = elms.split(":");
		
		elm.setActivationFunction(items[1]);
		
		String [] iws = items[2].split(";");
		int iw_row = Integer.parseInt(iws[0]);
		int iw_column = Integer.parseInt(iws[1]);
		String [] iw_value = iws[2].split(" "); 
		double [][] iw = new double[iw_row][iw_column];
		for(int i=0;i<iw_row;i++){
			for(int j=0;j<iw_column;j++){
				
				iw[i][j]=Double.parseDouble(iw_value[i*iw_column+j]);
			}
		}
		elm.setInputWeight(new Matrix(iw));

		String [] bs = items[3].split(";");
		int b_row = Integer.parseInt(bs[0]);
		int b_column = Integer.parseInt(bs[1]);
		String [] b_value = bs[2].split(" "); 
		double [][] b = new double[b_row][b_column];
		for(int i=0;i<b_row;i++){
			for(int j=0;j<b_column;j++){
				
				b[i][j]=Double.parseDouble(b_value[i*b_column+j]);
			}
		}
		elm.setBiasofHiddenNeurons(new Matrix(b));
		
		
		String [] ows = items[2].split(";");
		int ow_row = Integer.parseInt(ows[0]);
		int ow_column = Integer.parseInt(ows[1]);
		String [] ow_value = ows[2].split(" "); 
		double [][] ow = new double[ow_row][ow_column];
		for(int i=0;i<ow_row;i++){
			for(int j=0;j<ow_column;j++){
				ow[i][j]=Double.parseDouble(ow_value[i*ow_column+j]);
			}
		}
		elm.setOutputWeight(new Matrix(ow));
		
 		return elm;
	}
	
}
