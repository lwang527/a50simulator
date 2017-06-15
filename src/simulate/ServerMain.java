package simulate;

import simulate.server.ApiSimController;
import simulate.server.DataFetcher;
import simulate.server.DataInitialize;

/**
 * simulate the security trade server system
 * @author Administrator
 *
 */
public class ServerMain {

	public static void main(String[] args) {
		//init portfolio
		DataInitialize.initialize();
		//read history market data
		DataFetcher fetcher = new DataFetcher();
		new Thread(fetcher).start();
		//build server connection
		ApiSimController server = new ApiSimController();
		new Thread(server).start();
	}
}