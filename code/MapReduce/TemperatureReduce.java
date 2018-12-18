package com.doosan.hadoop.mapreduce;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/**
 * MapReduce - Reduce
 */
public class TemperatureReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		int maxValue = Integer.MIN_VALUE;
		for(IntWritable value : values){
			maxValue = Math.max(maxValue, value.get());
		}
		context.write(key, new IntWritable(maxValue));
	}
}