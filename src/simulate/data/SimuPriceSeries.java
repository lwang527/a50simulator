package simulate.data;

import java.util.Date;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;

import simulate.pojo.DataMkt;
import simulate.ui.HistoryChart;
import util.FormatTool;

public class SimuPriceSeries {
	
	private DataMkt storage = DataMkt.instance;
	private TimeSeries priceSeries = HistoryChart.priceSeries;
	private int start_loc = 0;
	private int current_loc = 0;
	
	public void execute(){
		while(true){
			current_loc = storage.hisSize();
			if(start_loc < current_loc){
				processLine(start_loc, current_loc);
				start_loc = current_loc;
			}
        }         
	}
	
	private void processLine(int start_loc, int current_loc){
		Second second = null;
    	for(int i = start_loc; i < current_loc; i++){
    		//current period : second
    		second = new Second(new Date(FormatTool.getTime(this.storage.getHis(i).time)));
    		//current price
    		double price = this.storage.getHis(i).close;
        	priceSeries.addOrUpdate(second, price);
    	}
    }
}
