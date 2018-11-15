# Hadoop安装部署

	- 网络环境准备
	
		- 修改hostname
		
			修改/etc/sysconfig/network文件
			
		- 修改/etc/hosts文件：新增IP地址和主机名即可
		
		- 关闭防火墙(一定要执行以下操作,操作完成后重启)
		
			1.执行：chkconfig iptables off
			
			2.使用root账号：
			
				# vi /etc/selinux/config					
			
				找到SELINUX并修改为：SELINUX=disabled，保存退出。
		
		- ssh免密登录
		
			- 主节点生成ssh秘钥-使用普通账号即可（如：hadoop）：
				
				ssh-keygen，一路回车，全部默认。
			
			- 配置hosts文件
				
				root: vi /etc/hosts
			
			- 执行命令：ssh-copy-id hostname(hosts文件中的各个主机名称)
				
			- 免密登录测试：
			
				ssh hostname(hosts文件中的各个主机名称)				

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
			
	- Hadoop配置(伪分布式)
	
		- 创建logs/tmp文件夹
		
			mkdir logs
			mkdir tmp
			
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
		
		- 配置hdfs-site.xml
		
```xml
	<configuration>
		<property>
			<!-- 副本数量 -->
			<name>dfs.replication</name>					
			<value>3</value>
		</property>
	</configuration>
```
		
		- 配置mapred-site.xml
		
		执行：mv mapred-site.xml.template mapred-site.xml
		
```xml
	<configuration>
		<property>
			<name>mapreduce.framework.name</name>					
			<value>yarn</value>
		</property>
	</configuration>
```
		
		- 配置yarn-site.xml		
		
```xml
	<configuration>
		<property>
			<name>yarn.resourcemanager.hostname</name>
			<!-- 主机名 -->					
			<value>hadoop0</value>
		</property>
		<property>
			<name>yarn.nodemanager.aux-services</name>					
			<value>mapreduce_shuffle</value>
		</property>
	</configuration>
```
		
		- 配置slaves	
		
```
	hadoop0
```
	
	- Hadoop手动启动
	
	
		- 格式化namenode
		
```
	cd /home/hadoop/hadoop-2.4.1/bin
	./hadoop namenode -format
```
	
		- 手动启动各个进程
		
```
	cd /home/hadoop/hadoop-2.4.1/sbin
	--启动hadoop相关进程
	./hadoop-daemon.sh start namenode
	./hadoop-daemon.sh start datanode
	./hadoop-daemon.sh start secondarynamenode
	./hadoop-daemon.sh start
	--查看各个进程对应的端口
	netstat -nltp
	--启动yarn相关进程
	./yarn-daemon.sh start resoucemanager
	./yarn-daemon.sh start nodemanager
```
		
		- 自动化脚本启动
		
```
	cd /home/hadoop/hadoop-2.4.1/sbin
	./start-dfs.sh
	./start-yarn.sh
```
	
	- Hadoop配置(分布式)
	
		- 配置ssh免密登录
		
			- 关闭防火墙
		
				1.执行：chkconfig iptables off
			
				2.使用root账号：
				
					# vi /etc/selinux/config					
				
					找到SELINUX并修改为：SELINUX=disabled，保存退出。

			- 拷贝主机(hadoop0)sshid：
			
				ssh-copy-id hadoop1
				ssh-copy-id hadoop2
				
		- JDK安装

			scp拷贝jdk至各个节点
			
			设置环境变量：/etc/profile
			
			source /etc/profile生效			
			
		- hadoop安装
		
			scp拷贝hadoop至各个节点
			
			删除各个节点hadoop下的tmp文件夹(一定要删除)
			
		- 主节点启动集群
		
			./start-dfs.sh
			./start-yarn.sh
			
		- 动态新增节点
		
			- 新增hadoop用户
		
			- 配置新机器网络，关闭防火墙，ip&hostname&hosts(和主机一致,即所有服务节点的hosts文件保持一致)
			
			- 配置主服务器hosts
			
			- 配置主服务器和新服务器之间的ssh免密登录 ： ssh-copy-id hostname
			
			- 拷贝jdk至新服务器，配置java环境变量
			
			- 拷贝hadoop到新机器,删除hadoop目录下的tmp文件夹(一定要删除)
			
			- 新服务器手动启动datanode ： ./hadoop-daemon.sh start datanode
