package simulate.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import simulate.pojo.CommonConst;
import simulate.pojo.DataMkt;
import simulate.pojo.HistoryUnit;

public class DataFetcher  implements Runnable {

	private String filePath = CommonConst.DATAFILEPATH;
	
	@Override
	public void run() {
		BufferedReader br = null;
		try {
			//check if the file exists
			File file = new File(filePath);
			if(!file.exists()) return;
			//build bufferedreader object
			br = new BufferedReader(new FileReader(filePath));
			//read file content util the end
			String line = null;
			while((line = br.readLine()) != null){
				String units[] = line.split(",");
				HistoryUnit hisunit = new HistoryUnit();
				hisunit.time = units[0].trim();
				hisunit.open = Double.parseDouble(units[1].trim());
				hisunit.high = Double.parseDouble(units[2].trim());
				hisunit.low = Double.parseDouble(units[3].trim());
				hisunit.close = Double.parseDouble(units[4].trim());
				hisunit.volume = Integer.parseInt(units[5].trim());
				hisunit.wap = Double.parseDouble(units[6].trim());
				//wrap the history market data
				DataMkt.instance.addHis(hisunit);
//				System.out.println("read data: time=" + hisunit.time + ", open=" + hisunit.open + ", high=" + hisunit.high 
//						+ ", low=" + hisunit.low + ", close=" + hisunit.close + ", volume=" + hisunit.volume + ", wap=" + hisunit.wap);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally{
			//close the open reader
			try {
				if(br !=null) br.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}	
		}
	}
}
