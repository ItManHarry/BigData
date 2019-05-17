# HBase

- hadoop数据库，分布式、可伸缩、大数据存储

- 适用于对大数据的随机实时读写操作

- 数量级 ： 十亿行 * 百万列

- 开源的、分布式、版本化、非关系型数据库

- 基于hadoop、HDFL

## hbase和hive的区别

- hive是高延迟、实时性查的数据仓库

- hbase是低延迟、实时性高的数据库

## hbase应用场景

- 频繁写入

- 快速随机访问

## hbase架构

- 安装hbase


	- 解压hbase
	
```
	tar -zvxf hbase-1.2.4-bin.tar.gz
```
	
	- 设置环境变量

```
	HBASE_HOME=...
	PATH=$PATH:$HBASE_HOME/bin
```
	
	- 验证
	
```
	$> hbase version
```
	
	-配置
	
		1.local/standalone模式
			
			a.配置hbase-1.2.4/conf/hbase-env.sh，设置JAVA_HOME	

			b.创建本地目录：
			
			$>mkdir /home/hadoop/hbase/hfiles

			配置hbase-1.2.4/conf/hbase-site.xml
			
			属性：hbase.rootdir=/home/hadoop/hbase
			
```
	<property>
		<name>hbase.rootdir</name>					
		<value>file:////home/hadoop/hbase</value>
	</property>
```
			
			c.配置zk本地数据存放目录
			
			$>mkdir /home/hadoop/hbase/zk
			
			配置hbase-1.2.4/conf/hbase-site.xml
			
			属性：hbase.zookeeper.property.dataDir=/home/hadoop/hbase/zk
			
```
	<property>
		<name>hbase.zookeeper.property.dataDir</name>					
		<value>/home/hadoop/hbase/zk</value>
	</property>
```
			
		2.伪分布式
		
			a.配置hbase-1.2.4/conf/hbase-site.xml			
			
```
	<property>
		<name>hbase.cluster.distributed</name>					
		<value>true</value>
	</property>
	<property>
		<name>hbase.rootdir</name>
		<!-- 地址是hdfs url, hbase为文件夹名称 -->		
		<value>hdfs://Server0:9000/hbase</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.dataDir</name>					
		<value>/home/hadoop/hbase-1.2.4/zk</value>
	</property>
```
		3.完全分布式
		
		a.配置regionservers
		
		路径：HBASE_HOEM/conf/regionservers
		
		Server1
		Server2
		Server3
		Server4
		
		b.分发hbase安装目录到各个server
		
		c.分配/etc/profile
		
		d.主服务器启动hbase
		
			start-hbase.sh
		
		e.web访问
		
		URL:http://10.40.123.210:16010/
		
## zookeeper

- 协同服务

- HA配置

- 分布式

- SPOF			//单点故障

- 存储结构	 	//属性结构，Linux操作系统。/version控制

- 轻量级集群	//一个服务器只有1M的存储量

- 节点类型		//persistence | sequential | ephemeral

- leader机制	//最小值选举法

- NIO			//底层技术：ChannelSocket + ByteBuffer

## hbase使用外部zookeeper

- 设置HBASE_HOME/conf/hbase-env.sh配置文件

```
	HBASE_MANAGES_ZK=false
```

- 配置HBASE_HOME/conf/hbase-site.xml增加以下配置项，设置外部zookeeper

```
	<property>
		<name>hbase.zookeeper.quorum</name>
		<value>Server0:2181,Server1:2181,Server2:2181,Server3:2181,Server4:2181</value>
	</property>	
```

- 最终的hbase-site.xml配置文件：

```xml
	<configuration>
		<property>
			<name>hbase.cluster.distributed</name>					
			<value>true</value>
		</property>
		<property>
			<name>hbase.rootdir</name>					
			<value>hdfs://Server0:9000/hbase</value>
		</property>		
		<property>
			<name>hbase.zookeeper.quorum</name>
			<value>Server1:2181,Server2:2181,Server3:2181,Server4:2181</value>
		</property>
	</configuration>
```

- hmaster没有启动的问题：

	原因：由于JDK使用的是jdk1.8.0_65
	
	解决方法，修改配置文件hbase-env.sh：
		# Configure PermSize. Only needed in JDK7. You can safely remove it for JDK8+
		#export HBASE_MASTER_OPTS="$HBASE_MASTER_OPTS -XX:PermSize=128m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=256m"
		#export HBASE_REGIONSERVER_OPTS="$HBASE_REGIONSERVER_OPTS -XX:PermSize=128m -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=256m"
		注释掉就好了
	参照网址：https://blog.csdn.net/Elimeny/article/details/81318083
	
## 	hbase命令

- 启动shell

```
	$>hbase shell
```

- hbase命名空间

	查看命令(类似show databases)：
	
```
	hbase(main)> list_namespace
```

	显示结果：
	
	- default	//默认空间,创建表是默认的表空间
	- hbase		//元数据存放命名空间,相当于数据库
	
- 命名空间下的表信息

	查看命令：
	
```
	hbase(main)> list_namespace_tables 'hbase' //命名空间名称
```

	显示结果：
	
	- meta			//元数据表
	- namespace		//命名空间表
	
- 创建命名空间

	创建命令：

```
	hbase(main)> create_namespace 'doosan' 
```


- 表命令

	- 创建命令：
	
```
	hbase(main)> create 'ns[不加默认建表在default命名空间下]:employee'， 'baseinfo', 'addinfo','workinfo'
```	

	- 查看表结构命令：
	
```
	hbase(main)> desc 'ns:employee'
```	

	- 插入数据

```
	hbase(main)> put 'doosan:users','row1','sysinfo:code','20112004'
```	

	- 查询表数据命令(查询全部)：
	
```
	hbase(main)> scan 'ns:employee'
```

	- 查询表数据命令(获取单行数据)：
	
```
	hbase(main)> get 'ns:employee','row1'
```

- 查询表数据命令(获取列数据)：
	
```
	hbase(main)> get 'ns:employee','row1'，column='baseinfo'					//单列
	hbase(main)> get 'ns:employee','row1'，column=['baseinfo','addinfo',...]	//多列
```

	- 删除数据命令，必须先disable表：
	
```
	hbase(main)> disable 'ns:employee'
	hbase(main)> drop 'ns:employee'
```	

	- 删除表命令，必须先disable表：
	
```
	hbase(main)> delete 'ns:employee','row1', 'sysinfo:code'
```	

## hbase client api

- pom.xml引入包

```xml
	<dependency>
	    <groupId>org.apache.hbase</groupId>
	    <artifactId>hbase-client</artifactId>
	    <version>1.2.4</version>
	</dependency>
```

- 客户端代码

```java
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
```

