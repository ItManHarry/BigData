# Hadoop

## Hadoop版本衍化历史

 ![Hadoop版本](https://github.com/ItManHarry/BigData/blob/master/document/1.jpg)
 
Apache Hadoop 版本分为分为1.0 和2.0 两代版本，我们将第一代Hadoop 称为Hadoop1.0，第二代Hadoop 称为Hadoop 2.0。上图是Apache Hadoop 的版本衍化史：

第一代Hadoop 包含三个大版本，分别是0.20.x，0.21.x 和0.22.x，其中，0.20.x 最后演化成1.0.x，
变成了稳定版。
第二代Hadoop 包含两个版本，分别是0.23.x 和2.x，它们完全不同于Hadoop 1.0，是一套全新的架构，
均包含HDFS Federation 和YARN 两个系统，相比于0.23.x，2.x 增加了NameNode HA 和
Wire-compatibility 两个重大特性。
		
Hadoop 遵从Apache 开源协议，用户可以免费地任意使用和修改Hadoop，也正因此，市面上出现了很多Hadoop 版本，其中比较出名的一是Cloudera 公司的发行版，该版本称为CDH（Cloudera Distribution Hadoop）。
截至目前为止，CDH 共有4 个版本，其中，前两个已经不再更新，最近的两个，
分别是CDH3（在Apache Hadoop 0.20.2 版本基础上演化而来的）和CDH4 在Apache Hadoop 2.0.0版本基础上演化而来的），分别对应Apache 的Hadoop 1.0 和Hadoop 2.0。

 ![CDH版本](https://github.com/ItManHarry/BigData/blob/master/document/2.jpg)
	
## Hadoop生态圈

组件图：
 ![Hadoop生态图](https://github.com/ItManHarry/BigData/blob/master/document/3.jpg)
	
组件说明：
- HDFS：Hadoop 生态圈的基本组成部分是Hadoop 分布式文件系统（HDFS）。HDFS 是一
种数据分布式保存机制，数据被保存在计算机集群上。数据写入一次，读取多次。HDFS为HBase 等工具提供了基础。

- MapReduce：Hadoop 的主要执行框架是MapReduce，它是一个分布式、并行处理的编程模型。MapReduce 把任务分为map(映射)阶段和reduce(化简)。开发人员使用存储在HDFS 中数据（可实现快速存储），编写Hadoop 的MapReduce 任务。由于MapReduce工作原理的特性， Hadoop 能以并行的方式访问数据，从而实现快速访问数据。

- Hbase：HBase 是一个建立在HDFS 之上，面向列的NoSQL 数据库，用于快速读/写大量数据。HBase 使用Zookeeper 进行管理，确保所有组件都正常运行。

- ZooKeeper：用于Hadoop 的分布式协调服务。Hadoop 的许多组件依赖于Zookeeper，它运行在计算机集群
上面，用于管理Hadoop 操作。

- Hive：Hive 类似于SQL 高级语言，用于运行存储在Hadoop 上的查询语句，Hive 让不熟悉MapReduce 开发人员也能编写数据查询语句，然后这些语句被翻译为Hadoop 上面的MapReduce 任务。像Pig 一样，Hive 作为一个抽象层工具，吸引了很多熟悉SQL 而不是Java 编程的数据分析师。

- Pig：它是MapReduce 编程的复杂性的抽象。Pig 平台包括运行环境和用于分析Hadoop数据集的脚本语言(Pig Latin)。其编译器将Pig Latin 翻译成MapReduce 程序序列。

- Sqoop：是一个连接工具，用于在关系数据库、数据仓库和Hadoop 之间转移数据。Sqoop利用数据库技术描述架构，
进行数据的导入/导出；利用MapReduce 实现并行化运行和容错技术。