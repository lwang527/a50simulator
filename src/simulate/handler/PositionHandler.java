package simulate.handler;

import com.ib.controller.Position;
import com.ib.controller.ApiController.IAccountHandler;

import simulate.pojo.DataMkt;
import simulate.pojo.PositionLock;

public class PositionHandler implements IAccountHandler{

	private DataMkt storage = DataMkt.instance;
	@Override
	public void accountValue(String account, String tag, String value, String currency) {
	}

	@Override
	public void accountTime(String time) {
	}

	@Override
	public void accountDownloadEnd(String para) {
	}

	@Override
	public void updatePortfolio(Position position) {
		synchronized (PositionLock.instance) {
			storage.setPosition(position);
			PositionLock.instance.setPosition(position);
			PositionLock.instance.setPositionUpdated(true);
			StringBuffer sb = new StringBuffer();
			sb.append("Account " + position.account() + " position info is :");
			sb.append("position=" + position.position());
			sb.append(",averageCost=" + position.averageCost());
			sb.append(",marketPrice=" + position.marketPrice());
			sb.append(",marketValue=" + position.marketValue());
			System.out.println(sb.toString());
		}
	}
}
