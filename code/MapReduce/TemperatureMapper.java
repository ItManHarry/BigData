package com.doosan.hadoop.mapreduce;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
 * MapReduce - Map
 */
public class TemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private static final int MISSING = 9999;
	
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException{
		//获取一整行文本
		String line = value.toString();
		//获取文本
		String year = line.substring(15, 19);
		//气温
		int airTemperature ;
		if(line.charAt(87) == '+'){
			airTemperature = Integer.parseInt(line.substring(88, 92));
		}else{
			airTemperature = Integer.parseInt(line.substring(87, 92));
		}
		//空气质量
		String quality = line.substring(92,93);
		if(airTemperature != MISSING && quality.matches("[01459]")){
			context.write(new Text(year), new IntWritable(airTemperature));
		}
	}
}