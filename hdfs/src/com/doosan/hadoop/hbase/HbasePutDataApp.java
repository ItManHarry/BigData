package com.doosan.hadoop.hbase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class HbasePutDataApp {
	
	public static void main(String[] args) throws Exception{
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//获取表
		Table table = connection.getTable(TableName.valueOf("employee"));
		byte[] rowKey = Bytes.toBytes("row3");
		Put family = new Put(rowKey);
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("id"), Bytes.toBytes("20112004"));
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("name"), Bytes.toBytes("Harry"));
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("age"), Bytes.toBytes(36));
		table.put(family);
		table.close();
		System.out.println("Put data successfully...");
	}
}
