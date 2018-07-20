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
	
	- SSH互信
	
	- JDK安装
	
	- Zookeeper安装
	
	- Hadoop安装