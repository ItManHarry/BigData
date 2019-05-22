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
				list.add("/dc/rack1");
			}else{
				list.add("/dc/rack2");
			}
		}
		return list;
	}
	
	public static void main(String[] args){
		String name = "Server1";
		System.out.println(name.substring(name.length() - 1));		
	}
}