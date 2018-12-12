# 大数据解决的核心问题

- 分布式存储

- 分布式计算

# 云计算解决的核心问题

- 服务

- 虚拟化

# SPOF: Single Point Of Failure 单点故障

- 找到所有的配置文件，路径(各个配置文件可详细查看)：

	- /hadoop-2.7.3/share/hadoop/common/hadoop-common-2.7.3.jar/core-default.xml
	
	- /hadoop-2.7.3/share/hadoop/hdfs/hadoop-hdfs-2.7.3.jar/hdfs-default.xml
	
	- /hadoop-2.7.3/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.3.jar/mapred-default.xml
	
	- /hadoop-2.7.3/share/hadoop/yarn/hadoop-yarn-common-2.7.3.jar/yarn-default.xml
	
- 配置hadoop临时目录

	- 修改配置文件 [core-default.xml]
	
	配置属性：hadoop.tmp.dir
	
	```xml
		<property>
			<name>hadoop.tmp.dir</name>
			<value>file-path</value>
		</property>
	```

# Hadoop机架感知

	- 概念：	
	机架感知是一种计算不同计算节点（TT）的距离的技术，用以在任务调度过程中尽量减少网络带宽资源的消耗，这里用尽量，想表达的是当一个TT申请不到本地化任务时，JT会尽量调度一个机架的任务给他，因为不同机架的网络带宽资源比同一个机架的网络带宽资源更可贵。当然，机架感知不仅仅用在MR中，同样还用在HDFS数据块备份过程中（第一个replica选择本节点【如果上传是DataNode】或者随机的一个DN（系统会尽量避免存储太满和太忙的节点），第二个节点选择于第一个节点不同机架的DN，第三个选择放在第二个DN同一个机架的另一个DN上）
	
	- 编写java代码
	
```java
	package com.doosan.hadoop.switcher;
	import java.util.ArrayList;
	import java.util.List;
	import org.apache.hadoop.net.DNSToSwitchMapping;
	/**
	 * 机架感知实现
	 */
	public class DoosanDNSToSwitchMapping implements DNSToSwitchMapping {

		@Override
		public void reloadCachedMappings() {
			// TODO Auto-generated method stub

		}

		@Override
		public void reloadCachedMappings(List<String> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public List<String> resolve(List<String> names) {
			List<String> list = new ArrayList<String>();
			int index = 0; 
			//此处传递的是slaves配置文件中的各个data node节点
			for(String name : names){
				index = Integer.parseInt(name.substring(name.length() - 1));
				if(index <= 2){
					list.add("/rack1/"+name);
				}else{
					list.add("/rack2/"+name);
				}
			}
			return list;
		}
	}
```
	- eclipse打包jar
	
	- 上传jar包至每个节点，上传路径：${hadoop_home}/share/hadoop/common/lib/
	
	- 配置每台hadoop的core-site.xml文件
	
```xml
	<property>
	  <name>topology.node.switch.mapping.impl</name>
	  <value>com.doosan.hadoop.switcher.DoosanDNSToSwitchMapping</value>   #类的全名
	</property>
```

	- 启动集群
	
		start-dfs.sh
		start-yarn.sh
		
	- 验证：在任意一台机器上执行：hadoop dfsadmin -printTopology
	
# Hadoop归档(archive)
	
	- 执行hadoop命令:
	
```
	hadoop archive -archiveName files.har(file name) /my/files(files to be archived) /my(file archived save path)
```
	har归档产生一个目录，目录名称就是xxx.har，该目录下有相关的文件。
	
	_index  //索引文件
	part-0	//数据文件
	
	文件目录查看：
	
```
	$> hdfs dfs -lsr har:///my/files.har/my/file/dir
```

# 数据完整性

	- 校验和
	
		数据校验CRC-32(循环冗余校验)，任何大小的数据输入均计算得到一个32位的整数校验和。

		io.file.buffer.size	//指定多少字节校验一次
		hdfs dfs -get -crc xxx //下载文件时，同时下载校验和文件
		hdfs dfs -get -ignoreCrc xxx //下载文件时，不下载校验和文件
	
	datanode
	
	blk_xxxx			//块数据，没有元数据
	blk_xxxx_xxx.meta	//校验和数据，4个字节对应512数据字节，7个字节的都信息
	- 客户端关闭校验和
	FileSystem fs = ...;
	fs.setVerifyChecksum(false);
	fs.open(...);
	
# 文件压缩

	- 减少存储空间，提高传输效率
	
| 压缩格式 | 压缩工具 | 压缩算法 | 文件扩展名 | 是否可切割 |
| --- | --- | --- | --- | --- |
| DEFLATE | 无 | DEFLATE | .deflate | 否 |
| Gzip | gzip | DEFLATE | .gz | 否 |
| bzip2 | bzip2 | bzip2 | .bz2 | 是 |
| LZO | lzop | LZO | .lzo | 否 |
| LZ4 | 无 | LZ4 | .lz4 | 否 |
| Snappy | 无 | Snappy | .Snappy | 否 |

	- Codec

| 压缩格式 | HadoopCompressionCodec |
| --- | --- |
| DEFLATE | org.apache.hadoop.io.compress.DefaultCodec |
| Gzip | org.apache.hadoop.io.compress.GzipCodec |
| bzip2 | org.apache.hadoop.io.compressBZip2Codec |
| LZO | com.hadoop.compression.lzo.LzoCodec |
| LZ4 | org.apache.hadoop.io.compress.Lz4Codec |
| Snappy | org.apache.hadoop.io.compress.SnappyCodec |

# hadoop管理

	- namenode名称节点的本地目录
	
		- edits 	//编辑日志
		
		- fsimage 	//镜像文件

	- 设置secondary namenode 设置成独立节点，修改配置文件hdfs-site.html
	增加如下配置项目：
	
```xml
	<property>
		<name>dfs.namenode.checkpoint.period</name>
		<value>3600</value>
		<description>
			The number of seconds between two periodic checkpoint.
		</description>
	</property>
	<property>
		<name>dfs.namenode.secondary.http-address</name>
		<value>10.40.123.201:50090</value>
		<description>
			The secondary namenode http server address and port.
		</description>
	</property>	
```

	- namenode故障，恢复方式两种：
	
		- 复制2nn的数据导新的nn下
		
		- 使用-importCheckpoint选项启动namenode守护进程
		

	- 配置管理,多目录设置(配置文件hdfs-core.xml)
	
		- 单目录设置如下属性即可
	
```xml
	<property>
		<name>hadoop.tmp.dir</name>
		<value>/home/hadoop/tmp</value>
	</property>
```

		- 配置多个目录(namenode)
	
```xml
	<property>
		<name>dfs.namenode.name.dir</name>
		<!-- 每个目录中的文件相同，起到副本的作用 -->
		<value>file:///home/hadoop/name1,file:///home/hadoop/name2</value>
	</property>
```	

		- 配置多个目录(datanode)
	
```xml
	<property>
		<name>dfs.datanode.data.dir</name>
		<!-- 每个目录中的文件不同，数据节点本身存在副本 -->
		<value>file:///home/hadoop/data1,file:///home/hadoop/data2</value>
	</property>
```	
	注：配置多目录的应用场景主要是磁盘扩容的时候，增加namendoe和datanode存放路径。

	
	- hadoop配额管理
	
		- 配额分类
		
			- space quota 	//空间配额
		
			- dir quota		//目录配额
		
		- 设置命令(目录配额 )
		
			hdfs  dfsadmin -setQuota 2 /usr/...
			
		- 设置命令(空间配额 )
		
			hdfs  dfsadmin -setSpaceQuota 11 hadoop
			
	- 快照管理
	
		-快速备份
		
		hdfs dfsadmin -allowSnapshot dir_name 		//启用指定目录快照
		hdfs dfsadmin -disallowSnapshot dir_name 	//停用指定目录快照
		hdfs dfs [-createSnapshot <snapshotDir> [snapshotName]] 	//创建快照
		hdfs dfs [-deleteSnapshot <snapshotDir> [snapshotName]] 	//删除快照
		hdfs lsSnapshottableDir						//列出所有可以快照的目录
		
	- 块扫描器

	数据节点每隔多少个小时扫描块数据，进行校验和计算[hdfs-site.xml]
	
```xml
	<property>
		<name>dfs.datanode.scan.period.hours</name>
		<!-- 小时数(3周) -->
		<!-- 如果value是,则使用默认值504;如果是负数,扫描器则关闭		-->
		<value>504</value>		
	</property>
```		

	- 启动均衡器
	
		执行：start-balancer.sh
		
		
	- 回收站[core-site.xml]
	
	控制文件在trash中的存活时间(分钟数),服务端和客户端均可进行设置
	
```xml
	<property>
	  <name>fs.trash.interval</name>
	  <value>0</value>
	  <description>Number of minutes after which the checkpoint
	  gets deleted.  If zero, the trash feature is disabled.
	  This option may be configured both on the server and the
	  client. If trash is disabled server side then the client
	  side configuration is checked. If trash is enabled on the
	  server side then the value configured on the server is
	  used and the client configuration value is ignored.
	  </description>
	</property>
```

	- 节点的上线和下线(Commission&Discommission)
	
		- slaves文件仅在集群启动/停止操作才访问
		
		- 在namenode节点新增datanodes.host文件,文件内容为各个datanode的hostname
		
			Server1
			Server2
			Server3
			Server4
			Server5
			Server6
		
		- 配置文件hdfs-site.xml
		
```xml
	<!-- 决定数据节点能否连接到namenode	-->	
	<property>
	  <name>dfs.hosts</name>
	  <!-- 指定一个文件的完整路径，如果没有指定，说明所有的节点均可以连接到namenode	  -->
	  <value>/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.include.host</value>
	  <description>Names a file that contains a list of hosts that are
	  permitted to connect to the namenode. The full pathname of the file
	  must be specified.  If the value is empty, all hosts are
	  permitted.</description>
	</property>
```

	- 配置文件yarn-site.xml
		
```xml
	<property>    
		<name>yarn.resourcemanager.nodes.include-path</name>
		<value>/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.include.host</value>
		<description>Path to file with nodes to include.</description>
	</property>
```

	- 新增节点步骤
	
		1.配置hdfs-site.xml的dfs.hosts属性(/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.include.host)
	
		2.配置新datanode服务器的网络,关闭防火墙,SSH,JDK,Hadoop,hosts文件,hostname
		
		3.配置完成后重启服务器		
		4.修改namenode的hosts文件,增加新的datanode节点信息,配置完成后分发至各个服务器;配置SSH免密登录新datanode节点
		
		5.更新namenode,执行如下命令：
		
			$> hdfs dfsadmin -refreshNodes
			
		6.更新resource manager,执行命令：
		
			$> yarn rmadmin -refreshNodes
		
		7.更新slaves文件,增加新的datanode节点信息

		8.启动新的datanode和node manager
		
			- 启动datanode
			
			$> hadoop-daemon.sh start datanode
			
			- 再平衡
			
			$> ./bin/start-balancer.sh
			
		9.在web UI确定一下节点信息
		
	- include和exclude优先级，include高于exclude
	
| Include | Exclude | Result | 
| --- | --- | --- |
| N | N | 不可连接 |
| N | Y | 不可连接 |
| Y | N | 可以连接 |
| Y | Y | 可以连接,状态为退役状态 |
	
	- 退役节点步骤
	
		- 编辑/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.exclude.host文件
		
		- 配置文件hdfs-site.xml
		
```xml
	<!-- 决定数据节点能否连接到namenode	-->	
	<property>
	  <name>dfs.hosts.exclude</name>
	  <!-- 指定一个文件的完整路径，如果没有指定，说明所有的节点均可以连接到namenode	  -->
	  <value>/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.exclude.host</value>
	  <description>Names a file that contains a list of hosts that are
	  permitted to connect to the namenode. The full pathname of the file
	  must be specified.  If the value is empty, all hosts are
	  permitted.</description>
	</property>
```

	- 配置文件yarn-site.xml
		
```xml
	<property>    
		<name>yarn.resourcemanager.nodes.exclude-path</name>
		<value>/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.exclude.host</value>
		<description>Path to file with nodes to include.</description>
	</property>
```
		
	- 刷新namenode
	
		$> hdfs dfsadmin -refreshNodes
		
		$> yarn rmadmin -refreshNodes
		
	- web UI确认退役节点状态为"Decommissioned"，停止datanode。
	
		$> hadoop-daemon.sh stop datanode
		
	- 在/home/hadoop/hadoop-2.7.3/etc/hadoop/datatnodes.include.host文件中移除推荐的节点
	
	- 再次执行namenode刷新命令
	
		$> hdfs dfsadmin -refreshNodes
		
		$> yarn rmadmin -refreshNodes
		
	- 从slaves文件中删除退役的节点