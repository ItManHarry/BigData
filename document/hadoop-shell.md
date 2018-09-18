# Hadoop shell操作

-  ls命令：查看文件清单

	```
		hadoop fs -ls [hdfs://hadoop0:9000]/
	```
	
- copyFromLocal命令：拷贝本地文件至hdfs

	```
		hadoop fs -copyFromLocal jdk1.7(本地文件) /(hdfs路径)
	```
	
- copyToLocal命令：拷贝hdfs文件至本地

	```
		hadoop fs -copyToLocal /jdk1.7(hdfs文件路径) /(本地路径)
	```
	
- put命令：拷贝本地文件至hdfs，等同于copyFromLocal命令

	```
		hadoop fs -put jdk1.7(本地文件) /(hdfs路径)
	```
	
- get命令：拷贝hdfs文件至本地,等同于copyToLocal命令

	```
		hadoop fs -get /jdk1.7(hdfs文件路径) /(本地路径)
	```
	
- getmerge命令：合并下载多个文件

	比如hdfs的目录/aaa/下有多个文件：log.1,log.2,log.3...
	
	```
		hadoop fs -getmerge /aaa/log.* /log.sum
	```
	
- moveFromLocal命令：移动文件至hdfs

	```
		hadoop fs -moveFromLocal jdk1.7(本地文件) /(hdfs路径)
	```
	
- moveToLocal命令：hdfs移动文件至本地

	```
		hadoop fs -moveToLocal /jdk1.7(hdfs文件路径) /(本地路径)
	```
	
- cp命令：从hdfs一个路径拷贝文件至hdfs另外一个路径

	```
		hadoop fs -cp /jdk1.7(hdfs文件路径) /(hdfs文件新路径)
	```
	
- mv命令：从hdfs一个路径移动文件至hdfs另外一个路径

	```
		hadoop fs -mv /jdk1.7(hdfs文件路径) /(hdfs文件新路径)
	```
	
- mkdir命令：在hdfs创建一个文件夹

	```
		hadoop fs -mkdir /filename
	```
	
- rm命令：删除文件/文件夹
	
	```
		hadoop fs -rm [-r] /test/test.log
	```
	
- rmdir命令：删除空文件夹
	
	```
		hadoop fs -rmdir /test
	```
	
- cat命令：查看文件内容
	
	```
		hadoop fs -cat /test/test.log
	```
	
# Hadoop分布式文件系统原理

- 首先它是一个文件系统，有一个统一的命名空间（目录），客户访问hdfs文件时就是通过指定这个目录树种的路径来进行

- 其次，它是分布式的，由多个服务器联合来实现以下功能：

	- hdfs文件系统会给客户端提供一个统一的抽象目录树，hdfs中的文件都是分块存储的，块的大小可以通过（dfs.blocksize），旧的版本默认64MB，现在是128MB.
	
	- 各个节点的datanode实际存储文件块。而且每个block都可以存储多个副本，通过设置dfs.replication，默认是3.
	
	- namenode负责维护整个hdfs文件系统的目录树，以及每个路径所对应的block块信息。
	
	- hdfs是设计适应一次写入，多次读出，并不支持文件的修改（hdfs不适合做网盘，因为不便修改，延迟大，网络开销大，成本太高）