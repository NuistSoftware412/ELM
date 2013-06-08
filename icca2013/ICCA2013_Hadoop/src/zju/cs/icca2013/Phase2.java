package zju.cs.icca2013;

import java.io.*;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


public class Phase2 extends Configured implements Tool {
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, IntWritable, Text> {
		private Text tags_value = new Text();
		private IntWritable td_id = new IntWritable();

		public void map(LongWritable key, Text value,
				OutputCollector<IntWritable, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			String [] tmp = line.split(":");
			td_id.set(Integer.parseInt(tmp[0]));
			String tags = Ensemble_Predictor.multi_predict(tmp[1],tmp[2]);
			tags_value.set(tags);
			System.out.println("In mapper: " + td_id);
			output.collect(td_id, tags_value);
		}
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<IntWritable, Text, IntWritable, Text> {
		private Text tag_value = new Text();
		public void reduce(IntWritable key, Iterator<Text> values,
				OutputCollector<IntWritable, Text> output, Reporter reporter)
				throws IOException {
			String result = Ensemble_Predictor.combine(values);
			tag_value.set(result);
			System.out.println("In reducer: " + key);
			output.collect(key,tag_value);
		}
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();
		JobConf conf = new JobConf(Phase2.class);
		conf.setJobName("Elm combination");

		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
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