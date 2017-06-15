package simulate.pojo;

import java.io.Serializable;

import com.ib.controller.OrderType;
import com.ib.controller.Types.Action;

public class OrderTrans  implements Serializable{

	private static final long serialVersionUID = -5429187611101052373L;
	public int totalQuantity;
	public Action action;
	public OrderType orderType;
	public double lmtPrice;
	public int contractId;
}
