package simulate.pojo;

import com.ib.controller.Position;

public class PositionLock {
public static PositionLock instance = new PositionLock();
	
	private Position position = null;
	
	private boolean positionUpdated;
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean isPositionUpdated() {
		return positionUpdated;
	}

	public void setPositionUpdated(boolean positionUpdated) {
		this.positionUpdated = positionUpdated;
	}
	
	public void read(){
		position= DataMkt.instance.getPosition();
	}
	
	public void reset(){
		position= DataMkt.instance.getPosition();
		positionUpdated =false;
	}
}
