package h1bpkg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Que7 {

	public static class Q7Map extends Mapper<LongWritable,Text,Text,LongWritable>
	{
		public void map(LongWritable key, Text value, Context context)
		{
			try
			{
				String[] str = value.toString().split("\t");
				long sno = Long.parseLong(str[0]);
				String year = str[7];
				
				context.write(new Text(year), new LongWritable(sno));
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static class Q7Reduce extends Reducer<Text, LongWritable, Text, LongWritable>
	{
		public void reduce(Text key, Iterable<LongWritable> values, Context context) 
		{
			try
			{
				long count = 0l;
				
				for(LongWritable val: values)
				{
					count++;
				}
				
				
				context.write(key, new LongWritable(count));
				
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "Petitions count");
		job.setJarByClass(Que7.class);
		
		job.setMapperClass(Q7Map.class);
		job.setReducerClass(Q7Reduce.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}
	
	
	
}
