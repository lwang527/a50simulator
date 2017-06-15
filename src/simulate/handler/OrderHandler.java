package simulate.handler;

import com.ib.controller.ApiController.IOrderHandler;
import com.ib.controller.NewOrderState;
import com.ib.controller.OrderStatus;

/**
 * 
 * @author Administrator
 *
 */
public class OrderHandler implements IOrderHandler{
	

	@Override
	public void orderState(NewOrderState orderState) {
	}

	@Override
	public void orderStatus(OrderStatus orderStatus, int filled, int remaining, double avgFillPrice, long permId, 
			int parentId, double lastFillPrice, int clientId, String whyHeld) {

		String str = "callback function invoked, current time is "+System.currentTimeMillis()+" : orderStatus="+orderStatus.name()
				+",filled="+filled+",remaining=" + remaining +",avgFillPrice=" + avgFillPrice
				+",permId=" + permId+",parentId=" + parentId+",lastFillPrice=" + lastFillPrice
				+",clientId=" + clientId+",whyHeld=" + whyHeld;
		System.out.println(str);
	}

	@Override
	public void handle(int code, String msg) {
	}

}
