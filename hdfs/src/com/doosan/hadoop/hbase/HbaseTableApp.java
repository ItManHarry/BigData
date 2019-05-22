package com.doosan.hadoop.hbase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseTableApp {
	
	public static void main(String[] args) throws Exception{
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//得到管理程序
		Admin admin = connection.getAdmin();
		TableName name = TableName.valueOf("employee");
		HTableDescriptor td = new HTableDescriptor(name);
		//创建列族
		HColumnDescriptor cd = new HColumnDescriptor("baseinfo");
		td.addFamily(cd);
		admin.createTable(td);
		System.out.println("Create employee table  successfully...");
	}
}