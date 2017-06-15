package simulate.pojo;

import java.io.Serializable;

import com.ib.controller.NewContract;

public class PositionReturn implements Serializable{

	private static final long serialVersionUID = -2866036314127064981L;
	
	public NewContract contract;
	public String account;
	public int position;
	public double marketPrice;
	public double marketValue;
	public double averageCost;
	public double unrealPnl;
	public double realPnl;
}
