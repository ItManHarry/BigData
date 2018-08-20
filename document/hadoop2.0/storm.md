# Storm安装配置

# Storm下载
	
	- 登录apache官网现在storm安装包
	
	url地址：http://storm.apache.org/downloads.html
	
# Storm解压

	tar -zxvf apache-storm-0.10.2.tar.gz
	
	重命名：
		
	mv apache-storm-0.10.2 storm

# Storm配置

	- 修改配置文件 storm/conf/storm.yaml
	
	- 文件末尾添加如下配置：
	
	```
		storm.zookeeper.servers:
			- "10.40.123.200"
			- "10.40.123.201"
			- "10.40.123.202"
		nimbus.host: "10.40.123.200"
		supervisor.slots.ports:
			- 6700
			- 6701
			- 6702
			- 6703
			- 6700
	```
	
	- 拷贝文件到supervisor机器上
	
	```
		scp -rp storm slave1:~/
	```
	
	- 各个服务器修改环境变量配置文件/etc/profile
	
	```
		export STORM_HOME=/home/hadoop
		export PATH=.:$PATH:$STORM_HOME/bin
	```
	
	生效环境变量：
	
	```
		source /etc/profile
	```
	
# 启动Storm集群

	- 启动主节点nimbus
	
	```
		nohup storm nimbus &
		nohup storm ui &
	```
	
	- 分别启动从节点supervisor
	
	```
		nohup storm supervisor &
	```