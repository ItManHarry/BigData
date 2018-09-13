# Hadoop shell操作

- 查看文件清单

	```
		hadoop fs -ls [hdfs://hadoop0:9000]/
	```
	
- Copy文件

	```
		hadoop fs -copyFromLocal jdk1.7(文件) /(路径)
	```