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