package simulate.client;

import com.ib.controller.NewOrder;

import common.ConfigConst;
import common.OrderMake;
import common.SignalType;
import simulate.handler.IOrderCaller;
import simulate.pojo.DataMkt;
import simulate.pojo.HistoryUnit;
import simulate.pojo.PositionLock;

public class SimuOrderService implements Runnable {

	private DataMkt storage = DataMkt.instance;
	private IOrderCaller caller;
	
	public SimuOrderService(IOrderCaller caller){
		this.caller = caller;
	}
	
	@Override
	public void run() {
		int start_loc = 0;
		int current_loc = 0;
		while(true){
			if(storage.getPosition() != null) break;
		}
		while(true){
			current_loc = storage.hisSize();
			if(start_loc < current_loc){
				processLine(start_loc, current_loc);
				start_loc = current_loc;
			}
		}
	}

	public void placeAnOrder(int quantity, int direction){
		NewOrder order = null;
		if(direction == ConfigConst.POS_BUY) {
			// 市价买入
			order = OrderMake.buy_mkt_ioc(quantity);
			System.out.println("you send an buying mkt order to buy in " + quantity);
		}else if(direction == ConfigConst.POS_SELL) {
			//市价卖出
			order = OrderMake.sell_mkt_ioc(quantity);
			System.out.println("you send an selling mkt order to sell out " + quantity);
		}
		caller.placeAnOrder(order);
		PositionLock.instance.reset();
	}

	private void processLine(int start_loc, int current_loc){
		for(int i = 0; i < current_loc - start_loc; i++){
			int curindex = start_loc + i;
			while(true){
				if(storage.getSignalSize() > curindex) break;
			}
			SignalType  st = storage.getSignal(curindex);
			synchronized (PositionLock.instance) {
				PositionLock.instance.read();
				if(!PositionLock.instance.isPositionUpdated()) continue;
				int position = PositionLock.instance.getPosition().position();
				System.out.println("your current position="+position+", current signal=" + st);
				
				if(st.equals(SignalType.Suspend) || st.equals(SignalType.Undetermined)) continue;
				HistoryUnit topdata = storage.getHis(curindex);
				String dataStr = String.format( "current data info is: time=%s, open=%s,high=%s,low=%s,close=%s,volume=%s,wap=%s", 
						topdata.time, topdata.open, topdata.high, topdata.low, topdata.close, topdata.volume, topdata.wap);
				System.out.println(dataStr);
				
				switch(st){
				case Rise ://上涨时买入
					if(position == 0){
						//平仓时买入1
						placeAnOrder(1, ConfigConst.POS_BUY);
					}
					else if(position < 0){
						//持负仓时买入2
						placeAnOrder(2, ConfigConst.POS_BUY);
					}
					break;
				case Fall : //下跌时卖出
					if(position == 0){
						//平仓时卖出1
						placeAnOrder(1, ConfigConst.POS_SELL);
					}
					else if(position > 0){
						//持正仓时卖出2
						placeAnOrder(2, ConfigConst.POS_SELL);
					}
					break;	
				case Off : //平仓
					if(position > 0){
						placeAnOrder(position, ConfigConst.POS_SELL);
					}
					else if(position < 0){
						placeAnOrder(-position, ConfigConst.POS_BUY);
					}
					break;	
				default:
					break;
				}	
			}
		}
	}
}
