package simulate.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ib.controller.NewContract;
import com.ib.controller.NewOrder;
import com.ib.controller.Position;

import common.ConfigConst;
import common.ContractMake;
import simulate.handler.IOrderCaller;
import simulate.handler.OrderHandler;
import simulate.handler.PositionHandler;
import simulate.pojo.CommonConst;
import simulate.pojo.OrderReturn;
import simulate.pojo.OrderTrans;
import simulate.pojo.PositionReturn;

public class ClientController implements Runnable, IOrderCaller{

	private String host = CommonConst.HOST;
	private int portid = CommonConst.PORTID;
	private Socket client = null;
	private OutputStream output = null;
	private ObjectOutputStream objoutput = null;
	private InputStream input = null;
	private ObjectInputStream objinput = null;
	private PositionHandler accountHandler = new PositionHandler();
	private OrderHandler orderHandler = new OrderHandler();
	private NewContract contract = ContractMake.SGX_A50(ConfigConst.EXPIRE_DATE);
	
	@Override
	public void run() {
		//socket connect
		connect();
		while(!Thread.interrupted()){
			//get the input data
			try {
				int order = objinput.readInt();
				if(CommonConst.ORDER_REQ_PORTFOLIO == order){
					//get the position sent by server simulator and callback
					PositionReturn position = (PositionReturn) objinput.readObject();
					Position pos = new Position(position.contract, position.account, position.position, 
							position.marketPrice, position.marketValue, position.averageCost, position.unrealPnl, position.realPnl);
					this.accountHandler.updatePortfolio(pos);
				}else if(CommonConst.ORDER_PLACE_ORDER == order){
					//get the order state sent by server simulator and callback
					OrderReturn result = (OrderReturn) objinput.readObject();
					this.orderHandler.orderStatus(result.status, result.filled, result.remaining, 
							result.avgFillPrice, result.permId, result.parentId, 
							result.lastFillPrice, result.clientId, result.whyHeld);
				}
			} catch (IOException e) {
				System.out.println("data read failed : " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("class cast failed : " + e.getMessage());
			}
		}
		disconnect();
	}
	
	//connect to server
	public void connect(){
		try {
			//connect to server
			client = new Socket(host, portid);
			//get the output stream
			output = client.getOutputStream();
			objoutput = new ObjectOutputStream(output);
			//get the input stream
			input = client.getInputStream();
			objinput = new ObjectInputStream(input);
		} catch (UnknownHostException e) {
			System.out.println("connection failed : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("connection failed : " + e.getMessage());
		}
	}
	// disconnect
	public void disconnect(){
		try {
			if(objoutput != null) objoutput.close();
			if(objinput != null) objinput.close();
			if(output != null) output.close();
			if(input != null) input.close();
			if(client != null) client.close();
		} catch (IOException e) {
			System.out.println("connection closed : " + e.getMessage());
		}	
	}
	//request the portfolio info
	public void updatePortfolio(){
		try {
			objoutput.writeInt(CommonConst.ORDER_REQ_PORTFOLIO);
			objoutput.flush();
		} catch (IOException e) {
			System.out.println("portfolio request failed : " + e.getMessage());
		}
	}
	// place an order
	@Override
	public void placeAnOrder(NewOrder order) {
		try {
			objoutput.writeInt(CommonConst.ORDER_PLACE_ORDER);
			OrderTrans trans = new OrderTrans();
			trans.contractId = contract.conid();
			trans.action = order.action();
			trans.orderType = order.orderType();
			trans.lmtPrice = order.lmtPrice();
			trans.totalQuantity = order.totalQuantity();
			objoutput.writeObject(trans);
			objoutput.flush();
		} catch (IOException e) {
			System.out.println("order place failed : " + e.getMessage());
		}
	}
}
