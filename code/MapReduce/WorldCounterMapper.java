package com.doosan.hadoop.mapreduce;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
 * MapReduce - Map
 */
public class WorldCounterMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException{
		//获取一整行文本
		String line = value.toString();
		//分割文本
		String[] words = line.split(" ");
		//统计文本
		for(String word : words){
			context.write(new Text(word), new IntWritable(1));
		}
	}
}