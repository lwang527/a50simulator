package common;

import com.ib.controller.NewOrder;
import com.ib.controller.OrderType;
import com.ib.controller.Types.Action;
import com.ib.controller.Types.TimeInForce;
/**
 * 
* @ClassName: OrderMake 
* @Description: 订单生成工具
* @author wangling
* @email ling.wang@hidimension.com
* @date 2016年1月13日 下午5:22:30 
*
 */
public class OrderMake {
	
	public static NewOrder buy_mkt_ioc(int quantity){
		NewOrder order = new NewOrder();
		order.action(Action.BUY);
		order.orderType(OrderType.MKT);
		order.totalQuantity(quantity);
		order.tif(TimeInForce.IOC);
		order.transmit(true);
		return order;
	}
	
	public static NewOrder sell_mkt_ioc(int quantity){
		NewOrder order = new NewOrder();
		order.action(Action.SELL);
		order.orderType(OrderType.MKT);
		order.totalQuantity(quantity);
		order.tif(TimeInForce.IOC);
		order.transmit(true);
		return order;
	}
	
	public static NewOrder buy_lmt_ioc(int quantity, double price){
		NewOrder order = new NewOrder();
		order.action(Action.BUY);
		order.lmtPrice(price);
		order.orderType(OrderType.LMT);
		order.totalQuantity(quantity);
		order.tif(TimeInForce.IOC);
		order.transmit(true);
		return order;
	}
	
	public static NewOrder sell_lmt_ioc(int quantity, double price){
		NewOrder order = new NewOrder();
		order.lmtPrice(price);
		order.action(Action.SELL);
		order.orderType(OrderType.LMT);
		order.totalQuantity(quantity);
		order.tif(TimeInForce.IOC);
		order.transmit(true);
		return order;
	}
	
	public static NewOrder copy(NewOrder old){
		NewOrder order = new NewOrder();
		order.action(old.action());
		order.lmtPrice(old.lmtPrice());
		order.orderType(old.orderType());
		order.totalQuantity(old.totalQuantity());
		order.tif(old.tif());
		order.transmit(true);
		return order;
	}
}
