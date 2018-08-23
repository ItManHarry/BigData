# Spark

# Spark安装配置

- 下载scala-2.10.4.tar.gz和Spark-1.4.1.tar.gz

- 解压scala-2.10.4.tar.gz

```
	tar -zvxf scala-2.10.4.tar.gz
```

- 配置环境变量

	vi /etc/profile
	
	增加：
	
		export SCALA_HOME=/home/hadoop/scala-2.10.4
		export PATH=.:$PATH:$SCALA_HOME/bin
		  
	生效环境变量
	
	source /etc/profile
	
	