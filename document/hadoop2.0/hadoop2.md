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
	
		1:修改/etc/hosts(每个服务器的文件内容一致,凡是localhost.xxx一律
		替换为指定host name，如master/slave1/slave2)
		
		2:修改/etc/sysconfig/network，修改HOSTNAME为hosts中ip对应的名称
		
			10.40.123.200 master			
			10.40.123.201 slave1
			10.40.123.202 slave2
			
	- 关闭防火墙
	
		执行命令：service chkconfig off
	
	- SSH互信
	
		- 首先确认ssh的安装情况（root账号）
		
			service sshd status
			
		- 若没有安装，执行以下命令进行安装：
		
			yum install sshd
			
		- ssh配置文件
		
			vi /etc/ssh/sshd_config
			
		- 重启ssh
		
			service sshd restart
		
	- ssh免密码登录（参考：https://www.linuxidc.com/Linux/2016-10/136200.htm）
		
		- 关闭防火墙，使用root账号：
		
			vi /etc/selinux/config
			
			找到SELINUX并修改为：SELINUX=disable，保存退出。
		
		- 修改sshd的配置文件（root权限）
		
			vi /etc/ssh/sshd_config
			
			找到以下内容，并去掉井号注释符：
			
			| 配置项|
			| --- |
			| RSAAuthentication yes |
			| PubkeyAuthentication yes |
			| AuthorizedKeysFile      .ssh/authorized_keys |					
			
			保存退出后，重启ssh：
			
			service sshd restart
		
		- 生成公私钥，退出root账号，在master 机器的虚拟机命令行下输入ssh-keygen -t rsa，一路回车，全部默认。
		
		- 导入到本机：cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
		
		- 修改文件权限：
				
			chmod 700 ~/.ssh
			
			chmod 600 ~/.ssh/authorized_keys	
			
		- 本机测试：
		
			ssh locahost
			
			如果能够登录，即验证成功，继续以下操作。
		
		- 在所有的slave机器上，也在命令行中输入ssh-keygen -t rsa，一路回车，全部默认。
		
		- 在master机器上复制master的授权文件到各个slave机器：
		
			scp ~/.ssh/id_rsa.pub root@目标主机ip或主机名:/home/id_rsa.pub
			
			注意把文件传送到目标主机时，要用root用户，否则会因权限不够而拒绝。输入目标主机密码后，出现OK即传输成功。
			
		-  登录到各目标主机，把公钥导入到认证文件（非root账号）
		
			cat /home/id_rsa.pub >> ~/.ssh/authorized_keys
		
		- 修改文件权限：
		
			chmod 700 ~/.ssh
			
			chmod 600 ~/.ssh/authorized_keys

		- 测试 ，在master上执行:
				
			ssh 目标主机名
					
			若能免密登录，则配置成功。
		
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
		
		- 创建临时目录/日志目录/JournalNode在本地磁盘存放数据的位置
		
			mkdir tmp
			
			mkdir logs
			
			mkdir journal
		
		- 进入etc/hadoop文件夹，修改以下配置文件：
		
			- 修改hadoo-env.sh
			
				export JAVA_HOME=/usr/java/jdk1.7.0_55
		
			- 修改配置文件core-site.xml
			
				增加如下属性的配置：
				
				```xml
					<configuration>
						<!-- 指定hdfs的nameservice为cluster -->
						<property>
							<name>fs.defaultFS</name>
							<value>hdfs://cluster</value>
						</property>
						<!-- 指定hadoop临时目录 -->
						<property>
							<name>hadoop.tmp.dir</name>
							<value>/home/hadoop/hadoop/tmp</value>
						</property>
						<!-- 指定zookeeper地址 -->
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
							<value>							
								org.apache.hadoop.io.compress.GzipCodec,
								org.apache.hadoop.io.compress.DefaultCodec,
								org.apache.hadoop.io.compression.lzo.LzoCodec,
								org.apache.hadoop.io.compress.BZip2Codec
							</value>
						</property>
						<property>
							<name>io.compression.codec.lzo.class</name>
							<value>com.hadoop.compression.lzo.LzoCodec</value>
						</property>
					</configuration>
				```
			- 修改配置文件hdfs-site.xml
			
				```xml
					<configuration>
						<!--指定hdfs的nameservice为cluster，需要和core-site.xml中的保持一致 -->
						<property>
							<name>dfs.nameservices</name>
							<value>cluster</value>
						</property>
						<!-- cluster下面有两个NameNode，分别是cluster1，cluster2 -->
						<property>
							<name>dfs.ha.namenodes.cluster</name>
							<value>slave1,slave2</value>
						</property>
						<!-- cluster1的RPC通信地址 -->
						<property>
							<name>dfs.namenode.rpc-address.cluster.slave1</name>
							<value>slave1:9000</value>
						</property>
						<!-- cluster1的http通信地址 -->
						<property>
							<name>dfs.namenode.http-address.cluster.slave1</name>
							<value>slave1:50070</value>
						</property>
						<!-- cluster2的RPC通信地址 -->
						<property>
							<name>dfs.namenode.rpc-address.cluster.slave2</name>
							<value>slave2:9000</value>
						</property>
						<!-- cluster2的http通信地址 -->
						<property>
							<name>dfs.namenode.http-address.cluster.slave2</name>
							<value>slave2:50070</value>
						</property>
						<!-- 指定NameNode的元数据在JournalNode上的存放位置 -->
						<property>
							<name>dfs.namenode.shared.edits.dir</name>
							<value>qjournal://master:8485;slave1:8485;slave2:8485/cluster</value>
						</property>
						<!-- 指定JournalNode在本地磁盘存放数据的位置 -->
						<property>
							<name>dfs.journalnode.edits.dir</name>
							<value>/home/hadoop/hadoop/journal</value>
						</property>
						<!-- 开启NameNode失败自动切换 -->
						<property>
							<name>dfs.ha.automatic-failover.enabled</name>
							<value>true</value>
						</property>
						<!-- 配置失败自动切换实现方式 -->
						<property>
							<name>dfs.client.failover.proxy.provider.cluster</name>
							<value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
						</property>
						<!-- 配置隔离机制方法，多个机制用换行分割，即每个机制暂用一行-->
						<property>
							<name>dfs.ha.fencing.methods</name>
							<value>
								sshfence
								shell(/bin/true)
							</value>
							</property>
						<!-- 使用sshfence隔离机制时需要ssh免登陆 -->
						<property>
							<name>dfs.ha.fencing.ssh.private-key-files</name>
							<value>/home/hadoop/.ssh/id_rsa</value>
						</property>
						<!-- 配置sshfence隔离机制超时时间 -->
						<property>
							<name>dfs.ha.fencing.ssh.connect-timeout</name>
							<value>30000</value>
						</property>
					</configuration>
				```
			
			- 修改配置文件mapred-site.xml
				
				```xml
					<configuration>
						<!-- 指定mr框架为yarn方式 -->
						<property>
							<name>mapreduce.framework.name</name>
							<value>yarn</value>
						</property>
					</configuration>
				```
				
			- 修改配置文件slaves
			
				修改内容为DataNode节点服务器:
				
				slave1
				
				slave2
			
			- 修改配置文件yarn-site.xml
			
			```xml
				<configuration>
					<!-- 开启RM高可靠 -->
					<property>
						<name>yarn.resourcemanager.ha.enabled</name>
						<value>true</value>
					</property>
					<!-- 指定RM的cluster id -->
					<property>
						<name>yarn.resourcemanager.cluster-id</name>
						<value>yrc</value>
					</property>					<!-- 指定RM的名字 -->
					<property>
						<name>yarn.resourcemanager.ha.rm-ids</name>
						<value>slave1,slave2</value>
					</property>
					<!-- 分别指定RM的地址 -->
					<property>
						<name>yarn.resourcemanager.hostname.slave1</name>
						<value>slave1</value>
					</property>
					<property>
						<name>yarn.resourcemanager.hostname.slave2</name>
						<value>slave2</value>
					</property>
					<!-- 指定zk集群地址 -->
					<property>
						<name>yarn.resourcemanager.zk-address</name>
						<value>master:2181,slave1:2181,slave2:2181</value>
					</property>
					<property>
						<name>yarn.nodemanager.aux-services</name>
						<value>mapreduce_shuffle</value>
					</property>
				</configuration>	
			```
			
			- 启动zookeeper
			
				cd zookeeper/bin
				
				./zkServer.sh start
				
			- 启动journalnode进程（此步骤经实际操作验证，无需单独启动，后面启动dfs时会自动启动起来）
			
				cd hadoop/sbin
				
				./hadoop-daemon.sh start journalnode
			
			- 在主服务器格式化HDFS
			
				cd hadoop/bin
				
				./hadoop namenode -format
				
				执行完成，在hadoop/tmp中生成了dfs文件，将此文件copy至slave服务器对应的目录中即可。			
			注：在第一次执行此命令是报错，原因是slave服务器的端口8485没有开启，通过编辑iptables，将
			相应的端口开启，问题解决。
				
			- 在主服务器格式化ZKFC
			
				./hdfs zkfc -format
			
			执行报错：Error:Could not find or load main class zkfc
			报错原因在于：错误在配置hdfs-site.xml文件时出错：
			```xml
				<property>
					<name>dfs.namenode.http-address.cluster.slave1</name>
					<value>slave1:50070</value>
				</property>
			```
			name属性值最后的值要和value中的主机名一致。
			修改配置后，执行命令，ZKFC正常格式化。注：其他配置也要注意这一点。
			
			- 每台服务器设置环境变量
			
			vi /etc/profile
			
			export HADOOP_HOME=/home/hadoop/hadoop
			
			export HADOOP_PREFIX=/home/hadoop/hadoop
			
			export YARN_CONF_DIR=/home/hadoop/hadoop
			
			export HADOOP_COMMON_HOME=/home/hadoop/hadoop
			
			export HADOOP_CMD=/home/hadoop/hadoop/bin/hadoop
			
			修改完成后，执行source或者重启生效：
			
			source /etc/profile
			
			- 启动HDFS(在master上执行)：
				
				cd hadoop/sbin
				
				./start-dfs.sh
				
			启动报错：Could not resolve hostname node1: Name or service not known
			
			问题原因：
			
				1、/etc/hosts配置问题。
				
				2、/etc/profile配置问题。
				
			解决：
				1、/etc/hosts 中的localhost.localdomain 替换为对应的hostname即可。
				
				2、修改etc/profile配置文件，添加：
				
					export HADOOP_COMMON_LIB_NATIVE_DIR=${HADOOP_PREFIX}/lib/native
					
					export HADOOP_OPTS="-Djava.library.path=$HADOOP_PREFIX/lib" 
					
			hadoop报错:RECEIVED SIGNAL 15: SIGTERM
			
			此问题原因是ssh免密登录没有解决，通过处理ssh免密登录，问题解决。
			
			- 每台服务器启动YARN:
			
				./start-yarn.sh