package simulate.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.ib.controller.OrderStatus;
import com.ib.controller.OrderType;
import com.ib.controller.Position;
import com.ib.controller.Types.Action;

import simulate.pojo.CommonConst;
import simulate.pojo.DataMkt;
import simulate.pojo.HistoryUnit;
import simulate.pojo.OrderReturn;
import simulate.pojo.OrderTrans;
import simulate.pojo.PositionReturn;
/**
 * the socket server listens the request of client and returns the process result
 * @author Administrator
 *
 */
public class ApiSimController implements Runnable{

	private ServerSocket serverSocket = null;
	private int portid = CommonConst.PORTID;
	private Socket client = null;
	private OutputStream output = null;
	private ObjectOutputStream objoutput = null;
	private InputStream input = null;
	private ObjectInputStream objinput = null;
	
	@Override
	public void run() {
		connect();
		while(!Thread.interrupted()){
			try {
				//establish the connection when request has listened
				client = serverSocket.accept();
				//get the outputstream
				output = client.getOutputStream();
				objoutput = new ObjectOutputStream(output);
				//get the inputstream
				input = client.getInputStream();
				objinput = new ObjectInputStream(input);
				
				sendData();
			} catch (IOException e1) {
				System.out.println("server errors : " + e1.getMessage());
			}
		}
		disconnect();
	}
	
	protected void sendData(){
		writePosition();
		// loop always
		while(true){
			try {
				int order = objinput.readInt();
				if(CommonConst.ORDER_REQ_PORTFOLIO == order){
					writePosition();
				}else if(CommonConst.ORDER_PLACE_ORDER == order){
					writeOrder();
				}
			} catch (IOException e) {
				System.out.println("there are exceptions caused by client, connection between server and client is broken:" + e.getMessage());
				break;
			} catch (ClassNotFoundException e) {
				System.out.println("class cast exception:" + e.getMessage());
			} 
		}	
	}
	
	protected void writeOrder() throws ClassNotFoundException, IOException{
		OrderTrans trans = (OrderTrans) objinput.readObject();
		//submited the order request
		OrderReturn result = new OrderReturn();
		HistoryUnit unit = DataMkt.instance.getLatestHis();
		result.status = OrderStatus.Submitted;
		result.filled = 0;
		result.remaining = trans.totalQuantity;
		result.avgFillPrice = trans.lmtPrice;
		result.lastFillPrice = unit.wap;
		writeOrderMin(result);
		while(true){
			unit = DataMkt.instance.getLatestHis();
			if(unit.volume == 0) {
				//if the volume is zero, the order will be cancelled
				continue;
			}else{
				result = new OrderReturn();
				result.status = OrderStatus.Filled;
				result.remaining = 0;
				if(trans.action.equals(Action.BUY)){
					result.filled = trans.totalQuantity;
					result.avgFillPrice = unit.high;
				}else{
					result.filled = -trans.totalQuantity;
					result.avgFillPrice = unit.low;
				}
				result.lastFillPrice = unit.wap;
				
				if(trans.orderType.equals(OrderType.MKT)){
					//if it's mkt order, check the trade volume we can get
				}else if(trans.orderType.equals(OrderType.LMT)){
					//if it's lmt order,check the price is between high and low, then fill the order
					if(trans.lmtPrice > unit.high || trans.lmtPrice < unit.low) {
						return;
					}
				}
				writeOrderMin(result);
				//if the order is filled, update the portfolio info
				Position pos = new Position(null, CommonConst.ACCOUNT, DataMkt.instance.getPosition().position() + result.filled, 
						unit.wap, result.avgFillPrice * result.filled, result.avgFillPrice, 0, 0);
				DataMkt.instance.setPosition(pos);
				writePosition();
				break;
			}
		}
	}
	
	protected void writeOrderMin(OrderReturn result){
		try {
			objoutput.writeInt(CommonConst.ORDER_PLACE_ORDER);
			objoutput.writeObject(result);
			objoutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void writePosition(){
		try {
			PositionReturn result = new PositionReturn();
			Position pos = DataMkt.instance.getPosition();
			result.contract = pos.contract();
			result.account = pos.account();
			result.position = pos.position();
			result.marketPrice = pos.marketPrice();
			result.marketValue = pos.marketValue();
			result.averageCost = pos.averageCost();
			result.unrealPnl = pos.unrealPnl();
			result.realPnl = pos.realPnl();
			objoutput.writeInt(CommonConst.ORDER_REQ_PORTFOLIO);
			objoutput.writeObject(result);
			objoutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//connect
	public void connect(){
		try {
			serverSocket = new ServerSocket(portid);
		} catch (IOException e) {
			System.out.println("server errors:" + e.getMessage());
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
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
			System.out.println("server errors : " + e.getMessage());
		}	
	}	
}
