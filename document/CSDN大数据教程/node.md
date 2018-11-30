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
	
	datanode
	
	blk_xxxx			//块数据，没有元数据
	blk_xxxx_xxx.meta	//校验和数据，4个字节对应512数据字节，7个字节的都信息