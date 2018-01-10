package com.illumi.oms.data.utils;

import java.text.DateFormat;
import java.text.ParseException;
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
		//return DateUtils.getZeroTime(1472313600000l);
		//return changeMonth(DateUtils.getZeroTime(new Date().getTime()), -3);
		//return changeMonth(DateUtils.getZeroTime(new Date().getTime()), -3);
		return DateUtils.getZeroTime((new Date().getTime()));
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
		 Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date(time));
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND,0);
         Date zero = calendar.getTime();
         return zero.getTime();
//		long zero=time/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
//		return zero;
	}
	
	public static long getZeroTime(Date d) {
		
         return getZeroTime(d.getTime());
	}

	public static DateFormat getDateFormat4Day() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	
	//计算相差多少月份
	public static int countMonths(String date1,String date2,String pattern) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        
        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        
        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        
        //开始日期若小月结束日期
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }
       
        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
    }
	
	
	public static void main(String[] args) {
		long zeroTime = DateUtils.getZeroTime(DateUtils.changeHour(new Date().getTime(), -12));
		String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zeroTime);
		System.out.println(format);
	}

}
