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
			```
		3.完全分布式