package util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatTool {
	private final static String DATE_YMDHMS = "yyyy/MM/dd HH:mm:ss";
	private final static SimpleDateFormat FORMATE_YMDHMS = new SimpleDateFormat(DATE_YMDHMS);
	private final static String DATE_YMD = "yyyyMMdd";
	private final static SimpleDateFormat FORMATE_YMD = new SimpleDateFormat(DATE_YMD);
	private final static String DATE_HMS = "HH:mm:ss";
	private final static SimpleDateFormat FORMATE_HMS = new SimpleDateFormat(DATE_HMS);
	private static DecimalFormat double2format = new DecimalFormat("####0.00");
	private static DecimalFormat intformat = new DecimalFormat("0");
	private final static String DATE_YMDHMS1 = "yyyy-MM-dd HH:mm:ss";
	private final static SimpleDateFormat FORMATE_YMDHMS1 = new SimpleDateFormat(DATE_YMDHMS1);
	public static long getTime(String time){
		try {
			return FORMATE_YMDHMS1.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getWholeTime(long milliseconds){
		
		return FORMATE_YMDHMS.format(milliseconds);
	}
	
	public static String getYMD(long milliseconds){
		
		return FORMATE_YMD.format(milliseconds);
	}
	
	public static String getYMDSpliter(long milliseconds){
		
		return FORMATE_YMDHMS.format(milliseconds).substring(0, 10);
	}

	public static String getHMS(long milliseconds){
	
		return FORMATE_HMS.format(milliseconds);
	}
	
	public static long to_milliseconds(String time){
		
		try {
			return FORMATE_YMDHMS.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getTodayBasedTime(String time){
		
		return to_milliseconds(getYMDSpliter(System.currentTimeMillis()) + " " + time);
	}
	
	/**
	 * 获取时分秒日期格式
	 * @return
	 */
	public static SimpleDateFormat getHmsTimeFormat(){
		return FORMATE_HMS;
	}
	
	public static long getRelativeTime(long curMili, double hour, double minute, double second){
		return (long)(curMili + hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000);
	}
	
	public static Date getRelativeTime(Date curDate, double hour, double minute, double second){
		return new Date(getRelativeTime(curDate.getTime(), hour, minute, second));
	}
	
	/**
	 * Double转2位小数字符串
	 * @param value
	 * @return
	 */
	public static String parseDouble2Str(double value){
		return double2format.format(value);
	}
	
	public static DecimalFormat getIntFormat(){
		return intformat;
	}
}
