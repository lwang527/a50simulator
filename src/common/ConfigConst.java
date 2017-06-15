package common;

import util.Configuration;
import util.FormatTool;

public class ConfigConst {

	//Socket服务器和客户端的IP和端口号
	public static String SOCKET_IP_ADDRESS = "localhost";
	
	//TWS服务器和客户端的IP和端口号及过期时间
	public static String EXPIRE_DATE = FormatTool.getYMD(System.currentTimeMillis()).substring(0, 6);
	
	//信号生成时统计样本数据均值和标准方差的样本数目
	public static int SIGNAL_ANALYZE_SIZE = 200;
	
	//IB期货时间运行时间
	public static String TWS_AM_START = Configuration.instance.getProperties("TWS_AM_START");
	public static String TWS_AM_END = Configuration.instance.getProperties("TWS_AM_END");
	public static String TWS_PM_START = Configuration.instance.getProperties("TWS_PM_START");
	public static String TWS_PM_END = Configuration.instance.getProperties("TWS_PM_END");
	public static int TWS_ST_SPAN = Integer.parseInt(Configuration.instance.getProperties("TWS_ST_SPAN"));;
	
	//买卖状态
	public static int POS_BUY = 1;
	public static int POS_SELL = 2;
	
	//账户信息
	public static String ACCOUNT_INFO = Configuration.instance.getProperties("ACCOUNT_INFO"); 
	
	//设置数据在服务器与本地的最大时间间隔，单位为second
	public static int MAXIMUM_TIMESPAN = 5;
	
	//设置蜡烛图统计ohlc数据的时间间隔，单位为second
	public static int CANDLESTICK_TIMESPAN = 30;
	
	//设置屏幕坐标系时间轴显示的时间间隔，单位为minute
	public static double SCREEN_SHOW_TIMESPAN = 1;
}
