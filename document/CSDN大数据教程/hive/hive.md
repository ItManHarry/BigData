# Hive

	hive - 数据仓库软件
		
- OLAP	//on line analyze process,在线分析处理
	
- OLTP	//on line transaction process, 在线事务处理(RDMS)

## HQL(Hive Querey Language)
	
- 支持三种数据类型结构

	- table(表)
	
	- partition(分区)
	
	- bucket(桶hash)
	
- 特性

	- schema存储在数据库中,处理hdfs的数据。
	
	- OLAP
	
	- 查询语言 HiveQL(HQL)
	
	- 可伸缩、可扩展、速度快。
	
- 架构

	- client ： webui | CLI | ...
	
	- hive	 : 	meta store + Hive引擎（处理引擎、执行引擎）
	
	- 底层	 ： hadoop（hdfs + MapReduce）
	
- 组件

	- metadata，存储在rdbms中（原数据：table + database + ）

	- HQL处理引擎
	
		写查询语句用于MapReduce作业
		
	- HQL执行引擎
	
		执行查询工作，得到查询结果。
		
	- HDFS/Hbase
	
		存储数据
		
- Hive安装

	- 下载地址：http://archive.apache.org/dist/hive/hive-2.1.1/
	
	- 解压(在hadoop的namenode节点解压安装)：
	
```
		tar -xzvf apache-hive-2.1.1-bin.tar.gz
```

	- 配置环境变量
	
```
		vi /etc/profile
		export HIVE_HOME=/home/hadoop/hive2.1.1/bin
		source /etc/profile
```
	
	- 启动hive
	
		$> hive
		

- Hive配置

	- 复制$HIVE_HOME/conf下的hive-env.sh.template到hive-env.sh，导入hadoop环境变量即可

```
		$> cp hive-env.sh.template hive-env.sh
		$> vi hive-env.sh
		HADOOP_HOME=/home/hadoop/hadoop-2.7.3
```

	- 配置hive-site.xml
	
```
		$> cp hive-default.xml.template hive-site.xml
		$> vi hive-site.xml		
```	

	- 初始化元数据库,初始化完成后生成metastore_db文件夹，路径和执行命令的路径一致，后续执行
	hive指令也在当前目录下。
	
```
		$> bin/schematool -initSchema -dbType derby		
```
	
	- 处理hive启动报错
	
		- 报错信息：
	
```
	Logging initialized using configuration in jar:file:/home/hadoop/hive2.1.1/lib/hive-common-2.1.1.jar!/hive-log4j2.properties Async: true
	Exception in thread "main" java.lang.IllegalArgumentException: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
			at org.apache.hadoop.fs.Path.initialize(Path.java:205)
			at org.apache.hadoop.fs.Path.<init>(Path.java:171)
			at org.apache.hadoop.hive.ql.session.SessionState.createSessionDirs(SessionState.java:644)
			at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:563)
			at org.apache.hadoop.hive.ql.session.SessionState.beginStart(SessionState.java:531)
			at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:705)
			at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:641)
			at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
			at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
			at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
			at java.lang.reflect.Method.invoke(Method.java:497)
			at org.apache.hadoop.util.RunJar.run(RunJar.java:221)
			at org.apache.hadoop.util.RunJar.main(RunJar.java:136)
	Caused by: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
			at java.net.URI.checkPath(URI.java:1823)
			at java.net.URI.<init>(URI.java:745)
			at org.apache.hadoop.fs.Path.initialize(Path.java:202)
```
	
	- 处理方法，编辑hive-site.xml文件，修改所有的${system:java.io.tmpdir}和${system:user.name}的值
	
```xml
	<property>
		<name>hive.exec.local.scratchdir</name>
		<!-- value>${system:java.io.tmpdir}/${system:user.name}</value -->
		<value>/home/hadoop/jdk1.8.0_65/tmp/hadoop</value>
		<description>Local scratch space for Hive jobs</description>
	</property>
```

	- 启动成功后，进入hive命令提示符下，使用exit命令退出
	
```
		hive> exit;
```	
	
	- 将元数据存放到MySQL数据中 ， 修改hive-site.xml文件的配置
	
```xml
	  <property>
		<name>javax.jdo.option.ConnectionURL</name>
		<value>jdbc:mysql://10.40.34.64:3306/hive</value>
		<description>
		  JDBC connect string for a JDBC metastore.
		  To use SSL to encrypt/authenticate the connection, provide database-specific SSL flag in the connection URL.
		  For example, jdbc:postgresql://myhost/db?ssl=true for postgres database.
		</description>
	  </property>
	  <property>
		<name>javax.jdo.option.ConnectionDriverName</name>
		<value>com.mysql.jdbc.Driver</value>
		<description>Driver class name for a JDBC metastore</description>
	  </property>
	  <property>
		<name>javax.jdo.option.ConnectionUserName</name>
		<value>hive</value>
		<description>Username to use against metastore database</description>
	  </property>
	  <property>
		<name>javax.jdo.option.ConnectionPassword</name>
		<value>hive</value>
		<description>password to use against metastore database</description>
	  </property>
```

	- 将MySQL驱动包加到$HIVE_HOME/lib下
	
	- 删除metastore_db文件夹，重新格式化hive
	
```
		$> bin/schematool -initSchema -dbType mysql
```
	注：mysql格式化后，不会生成metastore_db文件夹

# Hive数据类型

- Integral
	
		- TINYINT  		byte		1
		
		- SMALLINT		short		2
				
		- INT			int			4
		
		- BIGINT		long		8
		
- String

	''、""都可以。
	VARCHAR 0 - 65535
	CHAR 	255
	
- Timestamp 精确到纳秒

	格式："YYYY-MM-DD HH:MM:SS.fffffffff"
		  "yyyy-mm-dd hh:mm:ss.fffffffff"

- date	
	
	格式："YYYY-MM-DD"
	
- Decimals - 常量
	
	格式：	DECIMAL(scale, precision)  
	例：decimal(10, 0) - 总长度10位，小数0位
	
- union type

	格式：UNIONTYPE<int, double, array<string>, struct<a:int, b:string>>
	例：{0:1}
		{1:2.0}
		{2:["three","four"]}
		{3:{"a":5,"b":"five"}}
		{2:["six","seven"]}
		{3:{"a":8,"b":"eight"}}
		{0:9}
		{1:10.0}
		
- Floating point types

- Decimal type

- Null type

- Complex types 

	- Arrays
		
		Array<data_type>
	
	- Maps
	
		Map<primitive_type, data_type>
	
	- Structs
	
		struct<col_name:data_type[comment col_comment],...>
		
		
- Hive数据库操作(注：语法和mysql基本一致)

	mysql存放hive元数据（表结构信息、库信息、列信息......）
	
	1. 登录hive shell
	
		$> hive
		
	2.创建数据库 
	
		hive> create database myhive [location] 'HDFS FILE PATH[hdfs://10.40.123.210:900/hive/wh/dbname]';	//通过location自定义库文件路径,路径使用hdfs URL格式
		hive> show databases;	
		
	hadoop:数据库创建完成后，hadoop生成myhive.db文件，文件路径：/user/hive/warehouse
	mysql:存储表dbs
			
		select * from dbs;
		
	3.删除数据
	
		hive> drop database myhive;
		
	4.创建表
	
		hive> create table user(id int , name varchar(20), age int);
		
		4.1 hive执行SQL语句

		$> hive -e "create table dbname.tablename(...)"
		
		4.2 hive执行脚本
		
			a. 创建脚本文件 [h.sql]
			
			b. 执行脚本
			
				$> hive -f h.sql
		
	完整语法：
		
		create table if not exists employee (eid int, name String, salary String, destination String)
		comment 'Employee details'
		row format delimited
		fields terminated by '\t'
		lines terminated by '\n'
		stored as textfile;
		
	mysql:存储表tbls
	
		select * from tbls;
		
	mysql:存储列信息columns_v2
		
	5.查看表结构
	
		hive> desc tablename
		
		
	6.删除表
	
		hive> drop table default.user;
		
	7.本地数据导入
	
		语法：LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partclos1= val1, partclos2=val2 ...)]
		
		例：hive> load data local inpath 'filepath' [overwrite] into table dbname.tablename;
		overwrite一般不使用，否则会覆盖表下所有的数据。
	
		local		//本地文件
		overwrite 	//覆盖表数据
		partition 	//分区表
		
		
	8.修改表结构
	
		8.1 修改表名：alter table old_name rename new_name
		
		8.2 新增列：alter table t1 add columns(col_name1 col_type1,col_name2 col_type2 ...)
		
		8.3 修改列: alter table t1 change col_name new_name new_type
		
		8.4 删除列：alter table t1 drop [column] col_name
		
		
		8.5 替换列：alter table t1 replace columns(col_spec[,col_spec ...])
		
- Hive执行命令

		$> hive -e "select * form myhive.employee"		//一次性执行命令
		
		$> hive -S -e "select * from myhive.employee"	//一次性静态执行
		
		$> hive -f "*.sql"								//执行SQL脚本
		
		$> hive --help									//查看帮助
		
		hive>! cmd xxx;									//hive下执行简单的shell命令
		
		hive> set hive.cli.print.header=true;			//动态设置属性 
		
		hive> show databases 'my*';						//通配符"*"
	
# Hive架构

- client ： webui CLI

- Hive(Process Engine --> Excute Engine)

- Hadoop(hdfs)

# 管理表

- 创建表，指定注释+表属性

```
	create table if not exists t3(id int comment 'id is identity')
	comment 'table sample'
	tblproperties('creator'='centos','createtime'='2019-1-28')
```	

- desc extended tablename ;//显示表扩展信息

- desc formated tablename ;//格式化显示表信息


- 托管表（内部表）  ：
	
	语法：create mananged table ... 	//删除元数据，表数据一并删除掉了-
	
- 外部表			：

	语法：create external table ...		//删除元数据，表数据不会被删除
	
- 分区表			：

	语法：
		$hive> create table tn (id int, name string) partitioned by (province string, city string);
		
	添加分区：
	
		$hive> alter table tn add partition(province='ShanDong',city='JiNing') [location '...'];

	查看分区信息：
	
		$hive > show partitions tn;
		
	删除分区：
	
		$hive> alter table tn drop partition(province='ShanDong',city='JiNing');
		
	对应元数据表MySQL：partitions
	
	应用案例：日志；数据细化，表下面再次分区（Hive优化查询的主要手段之一）
	
	分区表插入数据：
	
	语法：$hive> INSERT INTO TABLENAME [PARTITION(PART1=VAL1,PART2=VAL2...)] VALUES(VALUES_ROW...)
	
	$hive> insert into tn partition(province='SD',city='JN') values(1, 'Harry');

	创建表，携带数据（可做备份表使用）
	
	$hive> create table user1 as select * from user;
	
	创建表，不携带数据（复制表）
	
	$hive> create table user1 like user;
	
	
	动态创建分区：
	
	1. 创建分区表
	
		create table if not exists t4 (id int ,name string, age int) partitioned by (province string, city string);
		
	2.关闭严格分区模式
	
		设置配置文件hive-site.xml中的"hive.exec.dynamic.partition.mode"的值为"nostrict"
		
	3.执行插入数据动态增加分区
	
		$hive> insert into t4 partition(province, city) select id ,name, age , province, city from t1;
		
# 远程连接hive

