# Spark

# Scala安装

- 下载scala-2.10.4.tar.gz

- 解压scala-2.10.4.tar.gz

```
	tar -zvxf scala-2.10.4.tar.gz
```

- 配置环境变量

```
	vi /etc/profile
```
	
增加：

```	
	export SCALA_HOME=/home/hadoop/scala-2.10.4
	export PATH=.:$PATH:$SCALA_HOME/bin
```
	  
生效环境变量

```	
	source /etc/profile
```
	
- 拷贝Scala到其他服务器并设置环境变量

# Spark安装

- 下载spark-1.4.1.tar.gz

- 解压spark-1.4.1.tar.gz

```
	tar -zvxf spark-1.4.1.tar.gz spark-1.4
```

- 配置spark-env文件

```
	cd conf
	#复制生成spark-env.sh文件
	cp -r spark-env.sh.template spark-env.sh
	#编辑spark-env.sh
	vi spark-env.sh
	#增加以下配置项
	export JAVA_HOME=/home/hadoop/Java/jdk7
	export SCALA_HOME=/home/hadoop/scala-2.10.4
	export SPARK_MASTER=master
	export SPARK_MASTER_PORT=7077
	#复制生成slaves文件
	cp -r slaves.template slaves
	#编辑slaves文件
	vi slaves
	#添加节点主机
	slave1
	slave2
```

- 配置环境变量

```
	vi /etc/profile
```
	
增加：

```	
	export SPARK_HOME=/home/hadoop/spark-1.4
	export PATH=.:$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
```
	  
生效环境变量

```	
	source /etc/profile
```
	
- 拷贝spark到其他服务器并设置环境变量

- 启动Spark集群

	在主服务器上/home/hadoop/spark-1.4/sbin中执行：
	./start-all.sh
	即可启动服务，通过jps查看启动的节点。
	
	在主节点登录查看：
	
	http://localhost:8080