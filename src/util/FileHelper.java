package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件读取帮助类
 * @author Administrator
 * 
 */
public class FileHelper {

	/**
	 * 从指定文件路径中读取文件内容
	 * @param filePath
	 * @return
	 */
	public String readFile(String filePath){
		BufferedReader br = null;
		String result = "";
		StringBuffer total = new StringBuffer();
		try {
			//根据指定文件路径构造BufferedReader对象
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
				return result;
			}
			br = new BufferedReader(new FileReader(filePath));
			//按行读取文件内容，直到文件末尾
			String line = null;
			//注：此:符作为行级分割器，文本内容中一定不要有该符号，此处确定文件不会用到才使用。
			while((line = br.readLine()) != null){
				total.append(":");
				total.append(line);
			}
			if(total.length() > 0) result = total.toString().substring(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally{
			//打开的流一定要在finally块中关闭
			try {
				if(br !=null) br.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}	
		}
		return result;
	}
	
	/**
	 * 写文件
	 * @param filePath 文件路径
	 * @param content 文件内容
	 */
	public void writeFile(String filePath, String content){
		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath);
			fw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			//打开的流一定要在finally块中关闭
			try {
				if(fw !=null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
