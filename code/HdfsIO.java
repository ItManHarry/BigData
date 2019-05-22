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
		fs.close();
	}
	/**
	 * 测试文件内容读取
	 * @throws IOException
	 */
	@Test
	public void testRead() throws IOException{
		Path path = new Path("/test/test.log");
		FSDataInputStream in = fs.open(path);
		byte[] b = new byte[128];
		int content = in.read(b);
		while(content != -1){
			System.out.println("Content is : " + new String(b));
			content = in.read(b);
		}
		in.close();
		fs.close();
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
		fs.close();
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
		fs.close();
	}
}