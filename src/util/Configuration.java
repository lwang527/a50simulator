/**   
* @Title: Configuration.java 
* @Package com.hidi.trade.dao 
* @version V1.0   
*/
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/** 
* @ClassName: Configuration 
* @Description: 从服务器配置文件读取配置信息，创建一次数据会话连接 
* @author wangling
* @email ling.wang@hidimension.com
* @date 2016年1月7日 下午9:02:18 
*  
*/
public class Configuration {
	
	public static Configuration instance = new Configuration();
	Properties property = null;
	public Configuration(){
		property = new Properties();
		try {
			property.load(new FileInputStream(new File(getResourcePath())));
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException:" + e.getMessage());
		}
	}
	
	private String getResourcePath(){
		URL url = Configuration.class.getProtectionDomain().getCodeSource().getLocation();
		String path = "";
		try {
			path = URLDecoder.decode(url.getPath(), "UTF-8");
			path = path.substring(1, path.lastIndexOf("/") + 1) + "ibserver.properties";
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException:" + e.getMessage());
		}
		return path;
	}
	
	/**
	 * 
	* @Title: getProperties 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public String getProperties(String key){
		return property.getProperty(key);
	}
}