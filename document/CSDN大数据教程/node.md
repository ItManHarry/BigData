# 大数据解决的核心问题

- 分布式存储

- 分布式计算

# 云计算解决的核心问题

- 服务

- 虚拟化

# SPOF: Single Point Of Failure 单点故障

- 找到所有的配置文件，路径(各个配置文件可详细查看)：

	- /hadoop-2.7.3/share/hadoop/common/hadoop-common-2.7.3.jar/core-default.xml
	
	- /hadoop-2.7.3/share/hadoop/hdfs/hadoop-hdfs-2.7.3.jar/hdfs-default.xml
	
	- /hadoop-2.7.3/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.3.jar/mapred-default.xml
	
	- /hadoop-2.7.3/share/hadoop/yarn/hadoop-yarn-common-2.7.3.jar/yarn-default.xml
	
- 配置hadoop临时目录

	- 修改配置文件 [core-default.xml]
	
	配置属性：hadoop.tmp.dir
	
	```xml
		<property>
			<name>hadoop.tmp.dir</name>
			<value>file-path</value>
		</property>
	```
