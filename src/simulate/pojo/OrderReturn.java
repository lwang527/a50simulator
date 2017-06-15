package simulate.pojo;

import java.io.Serializable;

import com.ib.controller.OrderStatus;

public class OrderReturn  implements Serializable{

	private static final long serialVersionUID = -2110817540304812752L;
	public OrderStatus status;
	public int filled;
	public int remaining;
	public double avgFillPrice;
	public long permId;
	public int parentId;
	public double lastFillPrice;
	public int clientId;
	public String whyHeld;

}
