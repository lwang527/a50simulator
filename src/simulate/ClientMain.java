package simulate;

import simulate.client.ClientController;
import simulate.client.SimuOrderService;
import simulate.client.SimuSignalService;
import simulate.server.DataFetcher;
import simulate.ui.SimuStsFrame;

public class ClientMain {

	public static void main(String[] args) {
		//get history data
		DataFetcher fetcher = new DataFetcher();
		new Thread(fetcher).start();
		//compute signal
		SimuSignalService signalService = new SimuSignalService();
    	signalService.computeSignal();
    	//show window
    	SimuStsFrame.MainFrame.setVisible(true);
    	//start the client
    	ClientController client = new ClientController();
    	new Thread(client).start();
    	//start order
    	SimuOrderService orderService = new SimuOrderService(client);
    	new Thread(orderService).start();
	}
}
