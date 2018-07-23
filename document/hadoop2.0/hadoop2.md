# Hadoop生态系统

 ![Hadoop版本](https://github.com/ItManHarry/BigData/blob/master/document/hadoop2.0/framework.jpg)

# Hadoop1.0和Hadoop2.0的区别

 ![Hadoop版本](https://github.com/ItManHarry/BigData/blob/master/document/hadoop2.0/differents.jpg)
 
 注：主要区别就是2.0增加了YARN集群资源处理框架，使得MapReduce只专注于数据处理。
 这样做解决了以下Hadoop1.0的几个问题：

 - 静态资源配置
 
 - 资源无法共享
 
 - 资源划分力度过大
 
 - 没有引入有效的资源隔离机制
 
# Hadoop2.0安装方式

- 自动安装部署

	- Ambari：http://ambari.apache.org
	
	- Cloudera Manager(收费)
	
- 使用rpm包安装部署

	- Apache Hadoop不提供
	
	- HDP和CDH提供
	
- 使用JAR包安装部署

	- 各个版本均提供

# Hadoop2.X安装步骤

- 软件环境

	- centos6.5/redhat6.5,jdk1.7, zookeeper3.4.5,hadoop2.4.1
	
- 硬件环境

	- 2T硬盘,64G内存,12核CPU,千兆网卡
	
- 集群搭建步骤

	- Hostname修改
	
		1:修改/etc/hosts(每个服务器的文件内容一致)
		
		2:修改/etc/sysconfig/network，修改HOSTNAME为hosts中ip对应的名称
		
			10.40.123.200 master			
			10.40.123.200 slave1
			10.40.123.200 slave2
	
	- SSH互信
		
		- 生成公私钥，在master 机器的虚拟机命令行下输入ssh-keygen，一路回车，全部默认
		
		- 复制一份master 的公钥文件，cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
		
		- 在所有的slave机器上，也在命令行中输入ssh-keygen，一路回车，全部默认
		
		- 在所有的salve机器上，从master 机器上复制master 的公钥文件：scp master:~/.ssh/authorized_keys /home/hadoop/.ssh/
	
	- JDK安装
	
		- 上传jdk7u79linuxx64.tar.gz到各个服务器
		 
		- 执行：tar -zvxf jdk7u79linuxx64.tar.gz命令进行解压
		
		- 执行：mv jdk7u79linuxx64/ jdk7 进行文件重命名
		
		- 配置环境变量：
		
			vi /etc/profile
			
			添加：
				export JAVA_HOME=/home/hadoop/Java/jdk7
				export PATH=.:$PATH:$JAVA_HOME/bin
				
		- 执行：source /etc/profile或者重启服务器生效
		
	
	- Zookeeper安装
	
		- 将下载的zookeeper-3.4.5.tar.gz上传至各个服务器
		
		- 执行：tar -zvxf zookeeper-3.4.5.tar.gz命令进行解压
		
		- 执行：mv zookeeper-3.4.5/ zookeeper 进行文件重命名
		
		- 进入zookeeper目录，创建data和logs文件
		
			mkdir data
			mkdir logs
		- 进入zookeeper/conf目录，新增配置文件zoo.cfg(zookeeper启动读取)
		
			文件内容如下：
			clientPort=2181
			tickTime=2000
			initLimit=10
			syncLimit=5
			dataDir=/home/hadoop/zookeeper/data
			dataLogDir=/home/hadoop/zookeeper/logs
			server.1=master:2888:3888
			server.2=slave1:2888:3888
			server.3=slave2:2888:3888
		- 根据hosts配置的信息记zoo.cfg配置文件信息，在data文件夹下新增myid，内容就是1/2/3
		
			cd data
			vi myid
			
		- zookeeper启动、重启、停止、状态查看命令
		
			cd bin
			
			./zkServer.sh start
			./zkServer.sh restart
			./zkServer.sh stop
			./zkServer.sh status
		
		注：启动查看状态出现如下错误：
		
			zookeeper Error contacting service. It is probably not running
			
		- 排查各个配置文件
		
		- 查看对应的端口是否开启（我启动失败原因就是2888/3888端口没有开启）
	
	- Hadoop安装
	
		- 解压hadoop-2.4.1.tar.gz
		
			tar -zvxf hadoop-2.4.1.tar.gz
			
		- 重命名hadoop-2.4.1
		
			mv hadoop-2.4.1/ hadoop
		
		- 创建临时目录及日志目录
		
			mkdir tmp
			mkdir logs
		
		- 进入etc/hadoop文件夹，修改以下配置文件：
		
			- 修改配置文件core-site.xml
			
				增加如下属性的配置：
				
				```xml
					<configuration>
						<property>
							<name>fs.defaultFS</name>
							<value>hdfs://cluster</value>
						</property>
						<property>
							<name>hadoop.tmp.dir</name>
							<value>/home/hadoop/hadoop/tmp</value>
						</property>
						<property>
							<name>ha.zookeeper.quorum</name>
							<value>master:2181,slave1:2181,slave2:2181</value>
						</property>
						<property>
							<name>hadoop.proxyuser.hue.hosts</name>
							<value>*</value>
						</property>
						<property>
							<name>hadoop.proxyuser.hue.groups</name>
							<value>*</value>
						</property>
						<property>
							<name>io.compression.codecs</name>							
							
							<value>							org.apache.hadoop.io.compress.GzipCodec,org.apache.hadoop.io.compress.DefaultCodec,org.apache.hadoop.io.compression.lzo.LzoCodec,org.apache.hadoop.io.compress.BZip2Codec</value>
						</property>
						<property>
							<name>io.compression.codec.lzo.class</name>
							<value>com.hadoop.compression.lzo.LzoCodec</value>
						</property>
					</configuration>
				```
		
			- 修改配置文件mapred-site.xml
		
			- 修改配置文件slaves
			
				修改内容为DataNode节点服务器:
				
				slave1
				slave2
			
			- 修改配置文件yarn-site.xml
			
			- 修改配置文件hdfs-site.xml
			
			