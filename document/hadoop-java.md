# Hadoop java端操作

- 创建java工程，引入hadoop开发jar包

	- hadoop-2.4.1\share\hadoop\hdfs下的hadoop-hdfs-2.4.1.jar以及hadoop-2.4.1\share\hadoop\hdfs\lib下的所有jar包
	
	- hadoop-2.4.1\share\hadoop\common下的hadoop-common-2.4.1.jar以及hadoop-2.4.1\share\hadoop\common\lib下的所有jar包
	
- 编写java程序 - hadoop基本操作

```java
	package com.doosan.hadoop.hdfs;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.net.URI;
	import org.apache.hadoop.conf.Configuration;
	import org.apache.hadoop.fs.BlockLocation;
	import org.apache.hadoop.fs.FileStatus;
	import org.apache.hadoop.fs.FileSystem;
	import org.apache.hadoop.fs.LocatedFileStatus;
	import org.apache.hadoop.fs.Path;
	import org.apache.hadoop.fs.RemoteIterator;
	import org.junit.Before;
	import org.junit.Test;

	public class HdfsUtil {	
		@Before
		public void init() throws Exception{
			//构造配置参数对象
			//Configuration conf = new Configuration();
			//conf中会有一个参数：fs.defaultFS的默认值是file:/// 指的是本地文件系统的URI
			//conf.set("fs.defaultFS", "hdfs://10.40.123.200:9000"); //使用此方法容易报权限问题，可更换连接方式
			//构造hdfs客户端
			//FileSystem fs = FileSystem.get(conf);
			fs = FileSystem.get(new URI("hdfs://10.40.123.200:9000"), new Configuration(), "hadoop");
		}
		/**
		 * 文件下载到本地
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		@Test
		public void testDownload() throws IllegalArgumentException, IOException{
			fs.copyToLocalFile(false, new Path("/DUMS_Pad_TP.apk"), new Path("C:\\Users\\20112004\\Desktop\\tmp\\hadoop"), true);
			fs.close();
		}
		/**
		 * 文件夹操作
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		@Test
		public void testMkdir() throws IllegalArgumentException, IOException{
			boolean exists = fs.exists(new Path("/hadoop"));
			System.out.println("hadoop directory exists ?" + (exists ? "Yes" : "No"));
			if(exists){
				System.out.println("hadoop directory exists...");
			}else{
				System.out.println("hadoop directory doesn't exist, do create ...");
				fs.mkdirs(new Path("/hadoop"));
			}
			System.out.println("update a file to the hadoop ...");
			fs.copyFromLocalFile(new Path("C:\\Users\\20112004\\Desktop\\tmp\\20180910.rar"), new Path("/hadoop"));
			System.out.println("file uploaded successfully...");
			fs.close();
		}
		/**
		 * 文件夹删除
		 * @throws IllegalArgumentException
		 * @throws IOException
		 */
		@Test
		public void testRmdir() throws IllegalArgumentException, IOException{
			System.out.println("Now we are going to delete the hadoop file...");
			Path path = new Path("/hadoop");
			fs.delete(path, true);
			boolean es = fs.exists(path);
			if(es){
				System.out.println("The hadoop file deleted failed...");
			}else{
				System.out.println("The hadoop file deleted successfully...");
			}
			fs.close();
		}
		/**
		 * 文件信息查看
		 * @throws FileNotFoundException
		 * @throws IOException
		 */			
		@Test
		public void testFileStatus() throws FileNotFoundException, IOException{
			Path path = new Path("/");
			RemoteIterator<LocatedFileStatus> list = fs.listFiles(path, true);
			while(list.hasNext()){
				LocatedFileStatus lf = list.next();
				System.out.println("File name : " + lf.getPath().getName());
			}
			System.out.println("----------------------------------------");
			FileStatus[] ls = fs.listStatus(path);
			for(FileStatus f : ls){
				if(f.isDirectory())
					System.out.println("-d\t" + f.getPath().getName());
				else
					System.out.println("-\t" + f.getPath().getName());
			}
			fs.close();
		}
		@Test
		public void testOthers() throws IOException{
			Path path = new Path("/jdk7u79linuxx64.tar.gz");
			BlockLocation[] bls = fs.getFileBlockLocations(path, 0, 153512879);
			for(BlockLocation b : bls){
				System.out.println("Block offset : " + b.getOffset());
				System.out.println("Block length : " + b.getLength());
				System.out.println("Block hosts : " + b.getNames()[0]);
				System.out.println("Block host ip : " + b.getHosts()[0]);
			}
			//更改文件副本数
			fs.setReplication(path, (short)3);
		}
		/**
		 * 基本测试
		 * @param args
		 * @throws Exception
		 */
		public static void main(String[] args) throws Exception {
			//构造配置参数对象
			Configuration conf = new Configuration();
			//conf中会有一个参数：fs.defaultFS的默认值是file:/// 指的是本地文件系统的URI
			//conf.set("fs.defaultFS", "hdfs://10.40.123.200:9000"); //使用此方法容易报权限问题，可更换连接方式
			//构造hdfs客户端
			//FileSystem fs = FileSystem.get(conf);
			FileSystem fs = FileSystem.get(new URI("hdfs://10.40.123.200:9000"), conf, "hadoop");
			//上传文件
			fs.copyFromLocalFile(new Path("C:\\Users\\20112004\\Desktop\\tmp\\DUMS_Pad_TP.apk"), new Path("/"));
			//上传完毕后，关闭连接
			fs.close();
		}
		
		FileSystem fs = null;
	}
```

- 编写java程序 - hadoop IO流操作

```java
	package com.doosan.hadoop.hdfs;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.net.URI;
	import java.net.URISyntaxException;
	import org.apache.hadoop.conf.Configuration;
	import org.apache.hadoop.fs.FSDataInputStream;
	import org.apache.hadoop.fs.FSDataOutputStream;
	import org.apache.hadoop.fs.FileSystem;
	import org.apache.hadoop.fs.Path;
	import org.apache.hadoop.io.IOUtils;
	import org.junit.Before;
	import org.junit.Test;

	public class HdfsIO {

		
		FileSystem fs = null;
		
		@Before
		public void init() throws IOException, InterruptedException, URISyntaxException{
			fs = FileSystem.get(new URI("hdfs://10.40.123.200:9000"), new Configuration(), "hadoop");
		}
		/**
		 * 从指定位置进行数据读取
		 * @throws IOException
		 */
		@Test
		public void testSeek() throws IOException{
			Path path = new Path("/xml/MultiBill_REPORTQUEREYFORPARTS.xml");
			FSDataInputStream in = fs.open(path);
			in.seek(200);
			FileOutputStream out = new FileOutputStream(new File("d:/seek.xml"));
			IOUtils.copyBytes(in, out, new Configuration());
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
		/**
		 * 使用流进行下载文件
		 * @throws IOException
		 */
		@Test
		public void testDownload() throws IOException{
			Path path = new Path("/DUMS_Pad_TP.apk");
			FSDataInputStream in = fs.open(path);
			FileOutputStream out = new FileOutputStream(new File("d:/ums.apk"));
			IOUtils.copyBytes(in, out, new Configuration());
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
		/**
		 * 使用流进行文件上传
		 * @throws Exception
		 */
		@Test
		public void testUpload() throws Exception{
			FileInputStream in = new FileInputStream(new File("d:/ums.apk"));
			FSDataOutputStream out = fs.create(new Path("/test/ums.apk"));
			IOUtils.copyBytes(in, out, 4096);
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
	}
```