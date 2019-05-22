package com.doosan.hadoop.hbase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseNamespaceApp {

	public static void main(String[] args) throws Exception{
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connectiong successfully...");
		//得到管理程序
		Admin admin = connection.getAdmin();
		System.out.println("Get administration...");
		//获取命名空间描述
		NamespaceDescriptor nd = NamespaceDescriptor.create("bd").build();
		System.out.println("Create namespace descriptor...");
		//执行创建命名空间
		admin.createNamespace(nd);
		System.out.println("Create namespace successfully...");
	}
}