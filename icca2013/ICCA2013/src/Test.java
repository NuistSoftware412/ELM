import java.util.Arrays;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.*;
import ELM_Classifier.ELM_Classifier;
import Jama.Matrix;

public class Test {
	public static void main(String args[]) {
		ELM_Classifier elm = new ELM_Classifier(3, 5);
		elm.ELM_Train("/home/jychen/work/Entropy/datasets/test.data", 26,
				"sig", 5000);
		int[] TT = elm.ELM_Predict(
				"/home/jychen/work/elm/icca2013/T2/t2_norm_P.data", 499);

		// ELM_Classifier elm2 = new ELM_Classifier(8,2);
		// elm2.ELM_Train("/home/jychen/diabetes_train", 50, "sin",576);
		//
		// int [] TT2 = elm2.ELM_Predict("/home/jychen/diabetes_test", 499);

		int count = 0;
		int[] tag = new int[499];
		String inpath = "/home/jychen/work/elm/icca2013/T2/t2_norm_T.data";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(inpath))));
			String str;
			int j = 0;
			while ((str = br.readLine()) != null) {
				Double tmp = Double.parseDouble(str);
				tag[j] = tmp.intValue();
				j++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i <= 499; i++) {
			if (TT[i - 1] == tag[i - 1]) {
				count++;
			}
		}
		double acc = (double) count / (double)499;
		System.out.println(String.format("%.6f", acc));
	}
}
