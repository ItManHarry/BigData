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
public class TemperatureRun {

	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.err.println("Usage:MaxTemperature <input path> <out path>");
			System.exit(-1);
		}
		//创建配置对象
		Configuration conf = new Configuration();
		//创建作业对象
		Job job = Job.getInstance(conf);
		//设置jar搜索类
		job.setJarByClass(TemperatureRun.class);
		//设置作业名称
		job.setJobName("Max Temperature...");
		//添加输入路径
		FileInputFormat.addInputPath(job, new Path(args[0]));
		//设置输出路径
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//设置Mapper类
		job.setMapperClass(TemperatureMapper.class);
		//设置Reduce类
		job.setReducerClass(TemperatureReduce.class);
		//设置输出key类型
		job.setOutputKeyClass(Text.class);
		//设置输出value类型
		job.setOutputValueClass(IntWritable.class);
		//程序退出
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}