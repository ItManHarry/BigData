package com.doosan.hadoop.mapreduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;
/**
 * MapReduce - run
 */
public class WorldCounterJob {

	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.err.println("Usage:World Counter <input path> <out path>");
			System.exit(-1);
		}
		//创建配置对象
		Configuration conf = new Configuration();
		//创建作业对象
		Job job = Job.getInstance(conf);
		//设置jar搜索类
		job.setJarByClass(WorldCounterJob.class);
		//设置作业名称
		job.setJobName("World Counter...");
		//添加输入路径
		FileInputFormat.addInputPath(job, new Path("hdfs://10.40.123.210:9000/data/worlds.txt"));
		//设置输出路径
		FileOutputFormat.setOutputPath(job, new Path("hdfs://10.40.123.210:9000/data/out"));
		//设置Mapper类
		job.setMapperClass(WorldCounterMapper.class);
		//设置Reduce类
		job.setReducerClass(WorldCounterReduce.class);
		//设置输出key类型
		job.setOutputKeyClass(Text.class);
		//设置输出value类型
		job.setOutputValueClass(IntWritable.class);
		//程序退出
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}