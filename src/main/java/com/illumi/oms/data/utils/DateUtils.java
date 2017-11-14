package com.illumi.oms.data.utils;

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

	
	public static void main(String[] args) {
		 String urlhead="/ilumi_transctionlog_";
		    String urlend="/_search";
		    
		    String url = getUrl(new Date().getTime(), urlhead, urlend);
		    System.out.println(url);
	}


	/**
     * 优化查询   
     * @param startTime
     * @return 年月日
     */
	public static String getUrl(long startTime, String urlhead, String urlend) {
		String url =null;
		String[] urlarr = DateUtils.getUrlDate(startTime);
	    if(urlarr[1]!=null) {
	    	
	    	// 
	      url = urlhead+urlarr[0]+","+urlhead+urlarr[1]+urlend;
	    }else {
	      url = urlhead+urlarr[0]+urlend;
	    }
		return url;
	}
	
	/**
     * 优化查询    判断上一天是否是上一个月
     * @param startTime
     * @return 年月日
     */
	private static  String[] getUrlDate(long startTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String end = df.format(new Date(startTime));
		String start = df.format(new Date(changeHour(startTime, -24)));
		String[] result =  new String[2];
		
		if(!end.equals(start)) {
			result[0] =start;
			result[1] = end;
			return result;
		}
		result[0]=end;
		return result;
	}
}
