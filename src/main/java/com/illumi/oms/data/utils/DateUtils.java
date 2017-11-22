package com.illumi.oms.data.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	public static long changeHour(long time,int num) {
		Date date = null;
		try {
		Calendar calendar = Calendar.getInstance();
	    date = new Date(time);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, num);
        date = calendar.getTime();
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
        return date.getTime();
	}
	
	//获取当前时间
	public static Long getCurrentZeroTime() {
//		//long dateEnd = DateUtils.getZeroTime(new Date().getTime());
//		 long dateEnd = DateUtils.changeMonth(DateUtils.getZeroTime(new Date().getTime()), -3);
//		//long dateEnd=1508256000000l;
		return changeMonth(DateUtils.getZeroTime(new Date().getTime()), -3);
	}
	
	
	public static long changeMonth(long time,int num) {
		Date date = null;
		try {
		Calendar calendar = Calendar.getInstance();
	    date = new Date(time);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        date = calendar.getTime();
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
        return date.getTime();
	}
	
	/**
	 * 获取当天零点零分零秒
	 * @param args
	 */
	public static long  getZeroTime(long time) {
		long zero=time/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		return zero;
	}

	public static DateFormat getDateFormat4Day() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	public static void main(String[] args) {
//		 String urlhead="/ilumi_transctionlog_";
//		    String urlend="/_search";
//		    
//		    String url = getUrl(new Date().getTime(), urlhead, urlend);
//		    System.out.println(url);
	}

}
