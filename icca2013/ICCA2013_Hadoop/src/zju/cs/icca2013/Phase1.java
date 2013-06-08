package zju.cs.icca2013;

import java.io.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


public class Phase1 extends Configured implements Tool {
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {
		private Text model = new Text();
		private Text elm_text = new Text();

		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			String [] tmp = line.split(":");
			String [] tmp2 = tmp[0].split("_");
			ELM_Classifier elm = new ELM_Classifier(3, 5);
			elm.ELM_Train(tmp[1], Integer.parseInt(tmp2[0]), tmp2[1]);
			if(elm.ELM_Filter(0.85, 1)){
				elm_text.set(elm.toString());
				model.set(tmp[0]);
				output.collect(model, elm_text);
			}
		}
	}


	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();
		JobConf conf = new JobConf(Phase1.class);
		conf.setJobName("ELMs Training");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		System.out.println(args[0]);
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Time Consumed: " + (double)consumingTime/1000000000+"s");
	}

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}