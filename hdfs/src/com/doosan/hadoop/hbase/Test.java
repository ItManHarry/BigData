package com.doosan.hadoop.hbase;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class Test {
	
	/**
	 * 创建命名空间
	 * @throws Exception
	 */
	@org.junit.Test
	public void createNamespace() throws Exception {
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connectiong successfully...");
		//得到管理程序
		Admin admin = connection.getAdmin();
		System.out.println("Get administration...");
		//获取命名空间描述
		NamespaceDescriptor nd = NamespaceDescriptor.create("BigData").build();
		System.out.println("Create namespace descriptor...");
		//执行创建命名空间
		admin.createNamespace(nd);
		System.out.println("Create namespace successfully...");
	}
	/**
	 * 创建表
	 * @throws Exception
	 */
	@org.junit.Test
	public void createTable() throws Exception {
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//得到管理程序
		Admin admin = connection.getAdmin();
		TableName name = TableName.valueOf("users");
		HTableDescriptor td = new HTableDescriptor(name);
		//创建列族
		HColumnDescriptor cd1 = new HColumnDescriptor("baseinfo");
		HColumnDescriptor cd2 = new HColumnDescriptor("employeeinfo");
		td.addFamily(cd1);
		td.addFamily(cd2);
		admin.createTable(td);
		System.out.println("Create users table  successfully...");
	}
	/**
	 * 插入数据
	 * @throws Exception
	 */
	@org.junit.Test
	public void putData() throws Exception {
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//获取表
		Table table = connection.getTable(TableName.valueOf("employee"));
		byte[] rowKey = Bytes.toBytes("row4");
		Put family = new Put(rowKey);
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("id"), Bytes.toBytes("20112004"));
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("name"), Bytes.toBytes("Jack"));
		family.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("age"), Bytes.toBytes(36));
		table.put(family);
		table.close();
		System.out.println("Put data successfully...");
	}
	/**
	 * 删除数据
	 * @throws Exception
	 */
	@org.junit.Test
	public void deleteData() throws Exception {
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//获取表
		Table table = connection.getTable(TableName.valueOf("employee"));
		Delete delete = new Delete(Bytes.toBytes("row2"));
		delete.addColumn(Bytes.toBytes("baseinfo"), Bytes.toBytes("name"));
		table.delete(delete);
		System.out.println("Delete data successfully...");
	}
	/**
	 * 查询数据
	 * @throws Exception
	 */
	@org.junit.Test
	public void scanData() throws Exception {
		//创建配置
		Configuration conf = HBaseConfiguration.create();
		//连接器
		Connection connection = ConnectionFactory.createConnection(conf);
		System.out.println("Create connection successfully...");
		//获取表
		Table table = connection.getTable(TableName.valueOf("employee"));
		//获取列族
		HColumnDescriptor[] cds = table.getTableDescriptor().getColumnFamilies();
		Scan scan = new Scan();
		ResultScanner rs = table.getScanner(scan);
		System.out.println("Table name is : " + table.getName());
		//获取行数据
		Iterator<Result> it = rs.iterator();
		String key = "";
		while(it.hasNext()){
			Result r = it.next();
			System.out.println("Row : " + new String(r.getRow()));
			for(HColumnDescriptor cd : cds){
				System.out.println("Family name : " + new String(cd.getName()));
				NavigableMap<byte[],byte[]> family = r.getFamilyMap(cd.getName());
				for(Entry<byte[], byte[]> entry : family.entrySet()){
					key = new String(entry.getKey());
					System.out.println("Key : " + key);
					System.out.println("Value : " + new String(entry.getValue()));
				}
			}
			System.out.println("--------------------------------------------------------------");
		}
		rs.close();
	}	
}