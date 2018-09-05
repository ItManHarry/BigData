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
	
