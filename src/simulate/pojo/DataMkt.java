package simulate.pojo;

import java.util.Vector;

import com.ib.controller.Position;

import common.SignalType;

/**
 * market data
 * @author Administrator
 *
 */
public class DataMkt {

	public static DataMkt instance = new DataMkt();

	//position
	private Position position;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	//history data list
	private Vector<HistoryUnit> historylist = new Vector<HistoryUnit>();
	
	public void addHis(HistoryUnit data){
		this.historylist.add(data);
	}
	
	public HistoryUnit getHis(int index){
		return this.historylist.get(index);
	}
	
	public HistoryUnit getLatestHis(){
		return this.historylist.lastElement();
	}
	
	public int hisSize(){
		return this.historylist.size();
	}
	
	//存储Signal数据
	private Vector<SignalType> signal_list = new Vector<SignalType>();
	
	public void addSignal(SignalType st){
		this.signal_list.add(st);
	}
	
	public SignalType getSignal(int index){
		return this.signal_list.get(index);
	}
	
	public int getSignalSize(){
		return this.signal_list.size();
	}	
}
