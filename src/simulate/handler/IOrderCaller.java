package simulate.handler;

import com.ib.controller.NewOrder;

public interface IOrderCaller {
	
	public void placeAnOrder(NewOrder order);
}
