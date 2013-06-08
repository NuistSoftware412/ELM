import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import Jama.Matrix;


public class D_ELM_MR {
	public static void main(String args[]) throws IOException{
		if(args.length!=7){
			System.out.println("Five paramters needed!");
			System.exit(0);
		}
		String input = args[0];
		String output = args[1];
		
		int ELM_Type = Integer.parseInt(args[2]);
		int n = Integer.parseInt(args[3]);
		String af = args[4];
		int column = Integer.parseInt(args[5]);
		String valid_file = args[6];
		
		long startTime = System.nanoTime();
		JobConf conf = new JobConf(D_ELM_MR.class);
		conf.setOutputKeyClass(LongWritable.class);
		conf.setOutputValueClass(Text.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		conf.setMapperClass(ELM2013Map.class);
		conf.setReducerClass(ELM2013Reduce.class);
		conf.setJobName(ELM_Type+":"+n+":"+af);
		conf.setInt("ELM_Type", ELM_Type);
		conf.setInt("n", n);
		conf.set("af", af);
		conf.setInt("column", column);
		conf.set("valid_file", valid_file);
		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));
		
		JobClient.runJob(conf);
		
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Consumed Time=" + (double)consumingTime/1000000000);
	}									
	
	
	public static class ELM2013Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>,JobConfigurable {
		private Text tags_value = new Text();
		private LongWritable td_id = new LongWritable();
		private int ELM_Type;
		private int n;
		private int column;
		private String af;
		public void configure(JobConf mapconf){
			ELM_Type = mapconf.getInt("ELM_Type", 0);
			n = mapconf.getInt("n", 0);
			column = mapconf.getInt("column", 0);
			af = mapconf.get("af");
		}
		public void map(LongWritable key, Text value,
				OutputCollector<LongWritable, Text> output, Reporter reporter)throws IOException {	
			long startTime = System.nanoTime();
			String s = value.toString();
			String [] items = s.split(" ");
			int lines = items.length/column;
			Matrix train = new Matrix(lines, column);
			
			for (int i = 0; i < lines; i++) {
				for (int j = 0; j < column; j++) {
					train.set(i, j, Double.parseDouble(items[column * i + j]));
				}
			}
			String elm_str = "";
			if(ELM_Type==0){
				long startTime2 = System.nanoTime();
				ELM_Regression elm = new ELM_Regression();
				elm.ELM_Train(train, n, af);
				long consumingTime2 = System.nanoTime() - startTime2;
				System.out.println("Train Consumed Time=" + (double)consumingTime2/1000000000);
				elm_str = Funs.ELMFormat(ELM_Type, af, elm.getInputWeight(), 
						elm.getBiasofHiddenNeurons(), elm.getOutputWeight());
			}else{
				long startTime2 = System.nanoTime();
				ELM_Classification elm = new ELM_Classification();
				elm.ELM_Train(train, ELM_Type, n, af);
				long consumingTime2 = System.nanoTime() - startTime2;
				System.out.println("Train Consumed Time=" + (double)consumingTime2/1000000000);
				elm_str = Funs.ELMFormat(ELM_Type, af, elm.getInputWeight(), 
						elm.getBiasofHiddenNeurons(), elm.getOutputWeight()); 
			}
			tags_value.set(elm_str);
			td_id.set(1);
			output.collect(td_id, tags_value);
			long consumingTime = System.nanoTime() - startTime;
			System.out.println("Map Consumed Time=" + (double)consumingTime/1000000000);
		}
	}
	
	public static class ELM2013Reduce extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text>,JobConfigurable {
		private Text tag_value = new Text();
		private LongWritable td_id = new LongWritable();
		private String tmp_file = "/tmp/elm2013.tmp";
		private int ELM_Type;
		private String valid_file;
		private String af;
		private FileSystem fs;
		public void configure(JobConf reduceconf){
			ELM_Type = reduceconf.getInt("ELM_Type", 0);
			valid_file = reduceconf.get("valid_file");
			af = reduceconf.get("af");
			try {
				fs=FileSystem.get(reduceconf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void reduce(LongWritable key, Iterator<Text> values,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			
			ArrayList<String> ELMs = new ArrayList<String>();
			while(values.hasNext()){
				String value = values.next().toString();
				ELMs.add(value);
			}
			int N = ELMs.size();
			String ELM="";
			if(ELM_Type==0){
			    Path filenamePath = new Path(valid_file);
				FSDataInputStream in = fs.open(filenamePath);
				OutputStream out = new FileOutputStream(tmp_file);
			    byte[] ioBuffer = new byte[1024]; 
			    int readLen = in.read(ioBuffer);
			    while (-1 != readLen) {  
			    	out.write(ioBuffer, 0, readLen);  
			    	readLen = in.read(ioBuffer);
			    }
			    in.close();
				Matrix valid=Funs.load(tmp_file);
				Matrix T = valid.getMatrix(0, valid.getRowDimension()-1,0,0);
				ELM_Regression [] elms = new ELM_Regression[N];
				double [][] results = new double[N][];
				for(int i=0;i<N;i++){
					ELM_Regression elm = Funs.deFormatElm_Regression(ELMs.get(i));
					elms[i] = elm;
					results[i]=elm.ELM_Predict(valid);
				}
				Matrix P = new Matrix(results);	
				Matrix Omiga = Funs.pinv(P.transpose()).times(T);
				ELM = Funs.ELM_Merge(elms,Omiga);
			}else{
				String InputWeight="";
				int InputWeight_Row=0;
				int InputWeight_Column=0;
				String BiasofHiddenNeurons="";
				int BiasofHiddenNeurons_Row=0;
				int BiasofHiddenNeurons_Column=0;
				String OutputWeight="";
				int OutputWeight_Row=0;
				int OutputWeight_Column=0;
				for(int i=0;i<N;i++){
					String [] tmp = ELMs.get(i).split(":");
					String [] tmp2 = tmp[2].split(";");
					InputWeight = InputWeight + tmp2[2] + " ";
					InputWeight_Row = Integer.parseInt(tmp2[0]);
					InputWeight_Column = Integer.parseInt(tmp2[1]);
					String [] tmp3 = tmp[3].split(";");
					BiasofHiddenNeurons = BiasofHiddenNeurons + tmp3[2] + " ";
					BiasofHiddenNeurons_Row = Integer.parseInt(tmp3[0]);
					BiasofHiddenNeurons_Column = Integer.parseInt(tmp3[1]);
					String [] tmp4 = tmp[4].split(";");
					OutputWeight = OutputWeight + tmp4[2] + " ";
					OutputWeight_Row=Integer.parseInt(tmp4[0]);
					OutputWeight_Column=Integer.parseInt(tmp4[1]);
				}
				ELM=ELM_Type+":"+af+":"+
				(InputWeight_Row*N)+";"+InputWeight_Column+";"+InputWeight+":"+
				(BiasofHiddenNeurons_Row*N)+";"+BiasofHiddenNeurons_Column+";"+BiasofHiddenNeurons+":"+
				(OutputWeight_Row*N)+";"+OutputWeight_Column+";"+OutputWeight_Column;
			}
			tag_value.set(ELM);
			td_id.set(1);
			output.collect(td_id, tag_value);
		}
	}
}
