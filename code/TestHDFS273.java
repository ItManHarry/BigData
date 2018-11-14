package com.doosan.hadoop.hdfs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class TestHDFS273 {
	FileSystem fs = null;

	@Before
	public void init() throws Exception{
		//构造配置参数对象
		//Configuration conf = new Configuration();
		//conf中会有一个参数：fs.defaultFS的默认值是file:/// 指的是本地文件系统的URI
		//conf.set("fs.defaultFS", "hdfs://10.40.123.200:9000"); //使用此方法容易报权限问题，可更换连接方式
		//构造hdfs客户端
		//FileSystem fs = FileSystem.get(conf);
		fs = FileSystem.get(new URI("hdfs://10.40.123.210:9000"), new Configuration(), "hadoop");
	}
	/**
	 * 读取hdfs文件
	 * @throws Exception 
	 */
	@Test
	public void testReadFile() throws Exception {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
		URL url = new URL("hdfs://10.40.123.210:9000/xml/MultiBill_Cond_PS_tuenserfee.xml");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		String str = new String(buf);
		System.out.println(str);
	}
	/**
	 * 读取文件 - 方式一(使用java流)
	 * @throws Exception
	 */
	@Test
	public void readFileByHadoopAPI1() throws Exception{
		Path path = new Path("/xml/MultiBill_Cond_PS_tuenserfee.xml");
		FSDataInputStream in = fs.open(path);
		byte[] b = new byte[1024];
		int len = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while((len = in.read(b)) != -1){
			baos.write(b, 0, len);
		}
		baos.close();
		fs.close();
		System.out.println(new String(baos.toByteArray()));
	}
	/**
	 * 读取文件 - 方式二(使用IOUtils)
	 * @throws Exception
	 */
	@Test
	public void readFileByHadoopAPI2() throws Exception{
		Path path = new Path("/xml/MultiBill_Cond_PS_tuenserfee.xml");
		FSDataInputStream in = fs.open(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copyBytes(in, baos, 1024);
		baos.close();
		fs.close();
		System.out.println(new String(baos.toByteArray()));
	}
	/**
	 * 创建目录
	 * @throws IOException
	 */
	@Test
	public void mkdir() throws IOException{
		Path path = new Path("/dses");
		if(fs.mkdirs(path))
			System.out.println("Create directory successfully...");
		else
			System.out.println("Create directory failed...");
		fs.close();
	}
	/**
	 * 测试创建文件
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void put() throws IllegalArgumentException, IOException{
		FSDataOutputStream out = fs.create(new Path("/dses/note2.txt"));
		out.write("Hello Hadoop World.".getBytes());
		out.close();
		fs.close();
	}
	/**
	 * 测试删除文件/文件夹
	 * @throws IOException 
	 */
	@Test
	public void remove() throws IOException{
		Path path = new Path("/dses/note2.txt");
		fs.delete(path, true);
		fs.close();
	}
	/**
	 * 递归打印文件路径
	 */
	@Test
	public void printAllPath(){
		Path path = new Path("/");
		doPrintPath(fs, path);
	}
	
	private void doPrintPath(FileSystem fileSystem, Path path){
		System.out.println(path.toUri().toString());
		try {
			if(fileSystem.isDirectory(path)){
				FileStatus[] sts = fs.listStatus(path);
				if(sts != null && sts.length > 0){
					for(FileStatus s : sts){
						Path p = s.getPath();
						doPrintPath(fileSystem, p);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}