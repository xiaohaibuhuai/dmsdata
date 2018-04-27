package com.illumi.dms.common.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import com.illumi.dms.common.Consts;
import com.illumi.dms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jfinal.core.Controller;


public class StringUtil extends StringUtils {

    private static final String letterChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    /**
     * 参数为返回随机数的长度
     *
     * @param length 需要生成的字符串长度
     * @return
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 将字符串型的json数据转换为List<map>返回
     *
     * @return
     */
//    public static List<Map> getMapFromJsonString(String jsonString) {
//        List<Map> list = JsonUtil.getListMapFromJsonString(jsonString);
//        return list;
//    }

    public static boolean isNullOrEmpty(String value) {
        if (value != null)
            return value.length() == 0;
        return true;
    }

    public static String getPathFileName(String filePath, boolean... includeType) {
        if (isNullOrEmpty(filePath))
            return null;
        int i = filePath.lastIndexOf("\\");
        if (i == -1) {
            i = filePath.lastIndexOf("/");
        }
        int j = filePath.lastIndexOf(".");
        if (i == -1 || j == -1)
            return null;
        if (includeType.length > 0) {
            return filePath.substring(i + 1);
        }
        return filePath.substring(i + 1, j);
    }

    public static boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除前后空格，如果为空则返回空字符串
     * @param value
     * @return
     */
    public static String getStringValue(String value) {
        if (null == value || "".equals(value.trim()))
            return "";
        return value.trim();
    }
    /**
     * 去掉最后一个字符
     * getSubEnd(这里用一句话描述这个方法的作用)
     * (这里描述这个方法适用条件 – 可选)
     * @param str
     * @return
     * String
     * @exception
     */
    public static String getSubEnd(String str){
    	if(str!= null){
    		return str.substring(0,str.length()-1);
    	}
    	return null;
    }
    
    
    /**
	 * 生成唯一字符串 
	 * 作者:解镭 Email:xielei@live.com 
	 * 创建日期：May 14, 2008 
	 * 创建时间: 5:07:39 PM
	 * @param length 需要长度
	 * @param symbol 是否允许出现特殊字符 -- !@#$%^&*()
	 * @return
	 */
	public static String getUniqueString(int length, boolean symbol)
			throws Exception {
		Random ran = new Random();
		int num = ran.nextInt(61);
		String returnString = "";
		String str = "";
		for (int i = 0; i < length;) {
			if (symbol)
				num = ran.nextInt(70);
			else
				num = ran.nextInt(61);
			str = strArray[num];
			if (!(returnString.indexOf(str) >= 0)) {
				returnString += str;
				i++;
			}
		}
		return returnString;
	}

	/**
	 * 生成唯一字符串 会已时间 加上你需要数量的随机字母 
	 * 如:getUniqueString(6,true,"yyyyMMddHHmmss")
	 * 返回:20080512191554juHkn4 
	 * 作者:解镭 Email:xielei@live.com 
	 * 创建日期：May 14, 2008
	 * 创建时间: 5:07:39 PM
	 * @param length 需要长度
	 * @param symbol 是否允许出现特殊字符 -- !@#$%^&*()
	 * @param dateformat 时间格式字符串
	 * @return 
	 */
	public static String getUniqueString(int length, boolean symbol,
			String dateformat) throws Exception {
		Random ran = new Random();
		int num = ran.nextInt(61);
		Calendar d = Calendar.getInstance();
		Date nowTime = d.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(dateformat);
		String returnString = sf.format(nowTime);
		String str = "";
		for (int i = 0; i < length;) {
			if (symbol)
				num = ran.nextInt(70);
			else
				num = ran.nextInt(61);
			str = strArray[num];
			if (!(returnString.indexOf(str) >= 0)) {
				returnString += str;
				i++;
			}
		}
		return returnString;
	}

	/**
	 * 给生成唯一字符串使用
	 */
	private static String[] strArray = new String[] { "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
			"s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "!", "@", "#", "$", "%", "^", "&", "(",
			")" };
	
	/**
     * 生成唯一字符串 
     * 作者:解镭 Email:xielei@live.com 
     * 创建日期：May 14, 2008 
     * 创建时间: 5:07:39 PM
     * @param length 需要长度
     * @param symbol 是否允许出现特殊字符 -- !@#$%^&*()
     * @return
     */
    public static String getUniqueStringByCharAndNum(int length)
            throws Exception {
        Random ran = new Random();
        int num = ran.nextInt(61);
        String returnString = "";
        String str = "";
        for (int i = 0; i < length;) {
            num = ran.nextInt(35);
            str = strArrayCharAndNum[num];
            if (!(returnString.indexOf(str) >= 0)) {
                returnString += str;
                i++;
            }
        }
        return returnString;
    }
	
	/**
     * 给生成唯一字符串使用
     */
    private static String[] strArrayCharAndNum = new String[] {"A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9"};
	
	/**
	 * 
	 * isIP(验证带个ip)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param ip
	 * @return
	 * boolean
	 * @exception
	 */
	public static boolean isIP(String ip) {
		String ipRegex = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9][0-9]|[0-9])\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9][0-9]|[0-9])\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9][0-9]|[0-9])\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9][0-9]|[0-9])";
		Pattern pattern = Pattern.compile(ipRegex);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	
	//还原被转义字符
	
	public static String decodeStr(String str){
		return org.apache.commons.lang.StringEscapeUtils.unescapeHtml(str);
	}
	 /* 
     * 将时间转换为时间戳
     */    
    public static long dateToStamp(String s){
    	long ts = 0l;
    	if(isNotBlank(s)){
    	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ts = date.getTime();
    	}
        return ts;
    }
    /* 
     * 将时间转换为时间戳
     */    
    public static long dateToStamp(String s,String format){
        long ts = 0l;
        if(isNotBlank(s)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ts = date.getTime();
        }
        return ts;
    }
    public static void main(String[] args) {
        System.out.println(dateToStamp("2017-08-15 12:00:00","yyyy-MM-dd HH:mm:ss"));
        
//        System.out.println(timeStampToDateNormal(System.currentTimeMillis(), "yyyy-MM-dd"));
//        System.out.println(timeStampToDateNormal(1499356800000l));
    }
    /*
     * 时间戳转可视日期
     */
    public static String timeStampToDateNormal(Long datetime) {
    	String date = "0";
    	try{
    		if(datetime != 0){
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		date = sdf.format(new Date(datetime));
    		}else{
    			date = "";
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return date;
    }
    
    /*
     * 时间戳转可视日期
     */
    public static String timeStampToDateNormal(Long datetime,String format) {
        String date = "0";
        try{
            if(datetime != 0){
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.format(new Date(datetime));
            }else{
                date = "";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }
    
    /*
     * Date转可视日期
     */
    public static String dateToString(Date datetime, String format) {
        String date = "0";
        try{
            if(datetime != null){
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.format(datetime);
            }else{
                date = "";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }
    /*
     * 截取过长字段
     */
    public static String FixedString(String value,int number) {
        if(value != null && !"".equals(value) ){
        	if(value.length() > number){
        		value = value.substring(0, number)+"...";
        	}
        	return value.trim();
        }else{
        	return "";
        }
    }
    /**
	 * Report the action
	 */
	public static String report(Controller controller) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder("\nJFinal action report -------- ").append(simpleDateFormat.format(new Date())).append(" ------------------------------\n");
        User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
        sb.append("USER and Controller: ").append(user.getStr("account")+"/"+user.getName()+"\n");
        String urlParas = controller.getPara();
        if (urlParas != null) {
            sb.append("UrlPara   : ").append(urlParas).append("\n");
        }
        // print all parameters
        HttpServletRequest request = controller.getRequest();
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter   : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=").append(values[0]);
                }
                else {
                    sb.append(name).append("[]={");
                    for (int i=0; i<values.length; i++) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }

        String controllerStr = controller.toString();
        controllerStr=controllerStr.substring(0,controllerStr.indexOf("@"));
        String requestURI = request.getRequestURI();
        requestURI=requestURI.substring(requestURI.lastIndexOf("/")+1);

        sb.append("Controller  :"+controllerStr+":"+requestURI+"()");
        sb.append("\n");
        sb.append("--------------------------------------------------------------------------------\n");
        return sb.toString();
	}
	
	
	public static String ArrayToStr(String[] array){
        String str ="";
        if(array != null){
            for(int i=0;i<array.length;i++){
                str+=array[i]+",";
            }
        }
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
	
	public static String[] StrToArray(String str){
        String[] array = null;
        if(str != null && !"".equals(str)){
            array = str.split(",");
        }
        return array;
    }
	
	public static String divisionLTWO(double num1,double num2){
	    double percent = num1 / num2;
	    //输出一下，确认你的小数无误
//	    System.out.println("小数：" + percent);
	    //获取格式化对象
	    NumberFormat nt = NumberFormat.getPercentInstance();
	    //设置百分数精确度2即保留两位小数
	    nt.setMinimumFractionDigits(0);
	    //最后格式化并输出
//	    System.out.println("百分数：" + nt.format(percent));
	    return nt.format(percent);
	}
	
	public static String division(double num1,double num2){
        double percent = num1 / num2;
        NumberFormat nt = NumberFormat.getInstance();
        nt.setMinimumFractionDigits(0);
        return nt.format(percent);
    }
	
	public static String secToTime(long time) {  
        String timeStr = null;  
        long hour = 0;  
        long minute = 0;  
        long second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    private static String unitFormat(long i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Long.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }
    //(num2-num1)/num1增长百分比
    public static String divisionInc(double num1,double num2){
        double percent = (num2-num1) / num1;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(0);
        return nt.format(percent);
    }
    
    /** 
     * 将emoji表情替换成空串 
     *   
     * @param source 
     * @return 过滤后的字符串 
     */  
    public static String filterEmoji(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\\x{10000}-\\x{10FFFF}]", "");
        } else {
            return source;
        }
    }
    
    
    /** 
     * 获得指定日期的后一天 
     *  
     * @param specifiedDay 
     * @return 
     */  
    public static Date getSpecifiedDayAfter(Date date) {  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
        return c.getTime();  
    }  
	
}
