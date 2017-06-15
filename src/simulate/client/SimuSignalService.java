package simulate.client;

import java.util.Random;

import common.ConfigConst;
import common.SignalType;
import simulate.pojo.DataMkt;

/**
 * 
* @ClassName: SignalService 
* @Description: 信号分析服务，开启另外一个线程来做此工作
* @author wangling
* @email ling.wang@hidimension.com
* @date 2016年1月13日 下午5:20:03 
*
 */
public class SimuSignalService {

	//平均和标准差计算Signal
	public void computeSignal(){
		SignalSimulator analyzer = new SignalSimulator();
		new Thread(analyzer).start();
	}
}

class SignalSimulator implements Runnable {
	
	private DataMkt storage = DataMkt.instance;
	
	public void run() {
		int start_loc = 0;
		int current_loc = 0;
		while(true){
			if(storage.hisSize() > 0) break;
		}
		while(true){
			current_loc = storage.hisSize();
			if(start_loc < current_loc){
				processLine(start_loc, current_loc);
				start_loc = current_loc;
			}
		}
	}
	
	private void processLine(int start, int end){
		int sigcount = end - start;
		for(int i = 0; i < sigcount; i++){
			SignalType st = SignalType.Suspend;
			Random rand = new Random();
			double result = rand.nextDouble();
			if(result < 0.2){
				st = SignalType.Undetermined;
			}else if(result < 0.4){
				st = SignalType.Suspend;
			}else if(result < 0.6){
				st = SignalType.Rise;
			}else if(result < 0.8){
				st = SignalType.Fall;
			}else{
				st = SignalType.Off;
			}
			storage.addSignal(st);
		}
	}
}


/**
 * 
* @ClassName: SignalAnalyzer 
* @Description: 分析市场数据，生成信号
* @author wangling
* @email ling.wang@hidimension.com
* @date 2016年1月13日 下午5:14:59 
*
 */
class SignalAnalyzer implements Runnable{
	
	private int sampleSize = ConfigConst.SIGNAL_ANALYZE_SIZE;
	
	private DataMkt storage = DataMkt.instance;
	
	@Override
	public void run() {
		int start_loc = 0;
		int current_loc = 0;
		while(true){
			if(storage.hisSize() > 0) break;
		}
		while(true){
			current_loc = storage.hisSize();
			if(start_loc < current_loc){
				processLine(start_loc, current_loc);
				start_loc = current_loc;
			}
		}
	}
	
	private void processLine(int start, int end){
		int sigcount = end - start;
		SignalType st = null;
		for(int i = 0; i < sigcount; i++){
			int current = start + i;
			st = SignalType.Suspend;
			if(current > sampleSize - 2){
				double average = getAverage(current);
				double std = getStd(current, average);
				double price = storage.getHis(current).close;
				double refprice = average + 0.5 * std;
				
				if(price > refprice) st = SignalType.Rise;
				else if (price < refprice) st = SignalType.Fall;
				else st = SignalType.Suspend;
			}
			storage.addSignal(st);
		}
	}
	
	private double getStd(int curindex, double average){
		double sum = 0d;
		for(int i = curindex - sampleSize + 1; i < curindex + 1; i ++){
			sum += Math.pow((storage.getHis(i).close - average), 2);
		}
		return Math.pow(sum/sampleSize, 0.5);
	}
	
	private double getAverage(int curindex){
		double sum = 0d;
		for(int i = curindex - sampleSize + 1; i < curindex + 1; i ++){
			sum += storage.getHis(i).close;
		}
		return sum/sampleSize;
	}
}

