package simulate.server;

import com.ib.controller.Position;

import simulate.pojo.CommonConst;
import simulate.pojo.DataMkt;

public class DataInitialize {

	public static void initialize(){
		Position position = new Position(null, CommonConst.ACCOUNT, 0, 0, 0, 0, 0, 0);
		DataMkt.instance.setPosition(position);
	}
}
