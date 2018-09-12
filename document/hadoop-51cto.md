# 大数据技术生态体系

- Hadoop ： 元老级分布式海量数据存储、处理技术系统，擅长离线分析。

- Hbase ： 基于Hadoop的分布式海量数据库，离线分析和在线分析业务

- Hive SQL ： 基于Hadoop的数据仓库工具，使用方便，功能丰富，使用方法类似SQL

- Zookeeper ：集群协调服务

- Sqoop ： 数据的导入导出工具

- Flume ： 数据采集框架

- Storm ： 实时流式计算框架，流式处理领域头牌框架

- Spark ： 基于内存的分布式运算框架，一站式处理 all in one 
	
	- SparkCore
	
	- SparkSQL
	
	- SparkStreaming
	
- 机器学习

	- Mahout ： 基于MapReduce的机器学习算法库
	
	- MLLIB ： 基于Spark机器学习算法库
	
# 学习路线

- 理解框架的功能和适用场景

- 使用（安装部署，编程规范，API）

- 运行机制

- 结构原理

- 源码

# Hadoop基本概念

- Hadoop是用于处理（运算分析）海量数据的技术平台，且采用分布式集群的方式

- Hadoop的两个功能

	- 提供海量数据的存储服务
	
	- 提供分析海量数据的编程框架及运行平台
	
- Hadoop的三大核心组件

	- HDFS : Hadoop分布式文件系统海量数据存储（集群服务器）
	
	- MapReduce ： 分布式运算框架（导jar包写程序），海量数据算法（替代品：storm、spark等）
	
	- Yarn ： 资源调度管理集群（可以理解为一个分布式的操作系统，管理和分配集群硬件资源）
	
- 使用Hadoop：
	
	- 可以吧Hadoop理解为一个编程框架（类比：struts、spring等），有着自己的API封装和用户编程规范，用户可以借助API来实现数据处理逻辑
	
	- 从另外一个角度，Hadoop又可以理解为一个提供服务的软件（类比：数据库、缓存服务redis等），用户程序客户端向Hadoop集群请求服务来实现特定的功能
	
- Hadoop安装部署

	- 网络环境准备
	
		- 修改hostname
		
			修改/etc/sysconfig/network文件
			
		- 修改/etc/hosts文件：新增IP地址和主机名即可
		
		- 关闭防火墙
		
			执行：chkconfig iptables on/off
		
		- ssh免密登录
		
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
		- ssh免密码登录 - 方式二
		
			执行命令：ssh-copy-id hostname
				

	- JDK安装
	
		- 上传jdk7u79linuxx64.tar.gz到主服务器
		 
		- 执行：tar -zvxf jdk7u79linuxx64.tar.gz命令进行解压
		
		- 执行：mv jdk7u79linuxx64/ jdk7 进行文件重命名
		
		- 配置环境变量：
		
			vi /etc/profile
			
			添加：
			
				export JAVA_HOME=/home/hadoop/Java/jdk7
				
				export PATH=.:$PATH:$JAVA_HOME/bin
				
		- 执行：source /etc/profile或者重启服务器生效
		
		- 远程拷贝jdk和/etc/profile文件至其他机器
		
		- 在其他机器执行 source /etc/profile或者重启服务器生效
		
	- Hadoop安装
	
		- 上传hadoop-2.4.1.tar.gz到主服务器
		
		- 解压hadoop-2.4.1.tar.gz
		
			tar -zvxf hadoop-2.4.1.tar.gz
			
	- Hadoop配置
	
		- 配置hadoop-env.sh
		
			修改JAVA_HOME为本地JDK即可
			
		- 配置 core-site.xml
		
		```xml
			<configuration>
				<property>
					<name>fs.defaultFS</name>
					<!-- uri格式：hdfs://主机名:端口号 -->
					<value>hdfs://hadoop0:9000</value>
				</property>
				<property>
					<!-- hadoop进程临时目录 -->
					<name>hadoop.tmp.dir</name>
					<value>/home/hadoop/hadoop-2.4.1/tmp</value>
				</property>
			</configuration>
		```