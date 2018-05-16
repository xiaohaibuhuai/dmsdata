package com.illumi.dms.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidateObjectUtil {

    private static final Number VALIDATE_NUMBER_IS_BLANK=-1;

    public static boolean numberIsNotBlank(Number obj,Number ...flg){
        boolean isNotBlank=true;
        Number validateFlg=VALIDATE_NUMBER_IS_BLANK;
        if(flg.length>0){
            validateFlg=flg[0];
        }
        isNotBlank = isNotBlank(obj);
        if(isNotBlank){
            if(obj.getClass().isAssignableFrom(Integer.class)){
                if(validateFlg.longValue()>Integer.MAX_VALUE||validateFlg.longValue()<Integer.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                isNotBlank= obj.intValue()>validateFlg.intValue()?true:false;
            }else if(obj.getClass().isAssignableFrom(Short.class)){
                if(validateFlg.intValue()>Short.MAX_VALUE||validateFlg.intValue()<Short.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                isNotBlank= obj.shortValue()>validateFlg.shortValue()?true:false;
            }else if(obj.getClass().isAssignableFrom(Long.class)){
                if(validateFlg.doubleValue()>Long.MAX_VALUE||validateFlg.doubleValue()<Long.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                isNotBlank= obj.longValue()>validateFlg.longValue()?true:false;
            }else if(obj.getClass().isAssignableFrom(Double.class)){
                isNotBlank= obj.doubleValue()>validateFlg.doubleValue()?true:false;
            }else if(obj.getClass().isAssignableFrom(Float.class)){
                if(validateFlg.doubleValue()>Long.MAX_VALUE||validateFlg.doubleValue()<Long.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                isNotBlank= obj.floatValue()>validateFlg.floatValue()?true:false;
            }else if(obj.getClass().isAssignableFrom(BigDecimal.class)){
                if(((BigDecimal)obj).max(new BigDecimal(validateFlg.toString())).equals((BigDecimal)obj)){
                    if(obj.doubleValue()==validateFlg.doubleValue()){
                        isNotBlank= false;
                    }else{
                        isNotBlank= true;
                    }
                }else {
                    isNotBlank= false;
                }
            }else if(obj.getClass().isAssignableFrom(BigInteger.class)){
                if(((BigInteger)obj).max(new BigInteger(validateFlg.toString())).equals((BigInteger)obj)){
                    if(obj.intValue()==validateFlg.intValue()){
                        isNotBlank= false;
                    }else{
                        isNotBlank= true;
                    }
                }else {
                    isNotBlank= false;
                }
            }else if(obj.getClass().isAssignableFrom(Byte.class)){
                if(validateFlg.shortValue()>Byte.MAX_VALUE||validateFlg.shortValue()<Byte.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                isNotBlank= obj.byteValue()>validateFlg.byteValue()?true:false;
            }else{
                throw new IllegalArgumentException("没找到对应类型");
            }
        }
        return isNotBlank;
    }

	public static boolean isNotBlank(Object obj)
	{
		boolean isNotBlank=true;
		if(obj!=null){
		    if(List.class.isAssignableFrom(obj.getClass())){
		        List<Object> test = (List)obj;
                if(test.isEmpty()){
                    return false;
                }else{
                    return true;
                }
            }else if(Map.class.isAssignableFrom(obj.getClass())){
                Map<Object,Object> test = (Map)obj;
                if(test.isEmpty()){
                    return false;
                }else{
                    return true;
                }
            }else if(Set.class.isAssignableFrom(obj.getClass())){
                Set<Object> test = (Set)obj;
                if(test.isEmpty()){
                    return false;
                }else{
                    return true;
                }
            }else if(CharSequence.class.isAssignableFrom(obj.getClass())){
                CharSequence test = (CharSequence)obj;
                if(test.length()==0){
                    return false;
                }else{
                    return true;
                }
            }else if(Number.class.isAssignableFrom(obj.getClass())){
                return true;
            }
        }else{
            return  false;
        }
		return isNotBlank;
		
	}
	/**
	 * 功能描述：常用类型,判断类型为空并设置默认值，如果判断类型和默认值类型不一样会试着转换为默认类型
	 * @param object 判断值
	 * @param t  默认值
	 * @exception ClassCastException
	 * @return (T)t
	 * Examaple1 {@link #isBlankDefault(2, "")} =new String("2")
	 * Examaple2 {@link #isBlankDefault("2", 2D)} =new Double("2")
	 */
	@SuppressWarnings("unchecked")
	public static <T>T isBlankDefault(Object object,T t,Number ...flg)
	{
		boolean isBlank=true;
        if(null!=flg&&flg.length>0){
            if(isBlank(object)){
                return (T)t;
            }
            boolean isMatch = Pattern.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$",(CharSequence)object.toString());
            if(isMatch){
                isBlank=numberIsNotBlank(new BigDecimal(object.toString()),flg)?false:true;
            }else{
                isBlank=isBlank(object);
            }
        }else{
            isBlank=isBlank(object);
        }
        if(isBlank){
            return t;
        }else{
            if(null == t){
                return (T)object;
            }
            else if(List.class.isAssignableFrom(t.getClass())){
                return (T)object;
            }else if(Map.class.isAssignableFrom(t.getClass())){
                return (T)object;
            }else if(Set.class.isAssignableFrom(t.getClass())){
                return (T)object;
            }else if(CharSequence.class.isAssignableFrom(t.getClass())){
                return (T)object.toString();
            }else if(Boolean.class.isAssignableFrom(t.getClass())){
                return booleanParse(object.toString(),t);
            }else if(Number.class.isAssignableFrom(t.getClass())){
                return numberParse(object.toString(),t);
            }else{
                throw  new ClassCastException("当前输入不能装换为目标数据");
            }
        }
	}
	private static <T>T numberParse(String object,T t){
        String value = object.toString();
        boolean isMatch = Pattern.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$",(CharSequence)value);
        if(isMatch){
            if(t.getClass().isAssignableFrom(Integer.class)){
                BigDecimal bigDecimal = new BigDecimal(value);
                if(bigDecimal.longValue()>Integer.MAX_VALUE||bigDecimal.longValue()<Integer.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }

                return (T)Integer.valueOf(bigDecimal.intValue());
            }else if(t.getClass().isAssignableFrom(Short.class)){
                BigDecimal bigDecimal = new BigDecimal(value);
                if(bigDecimal.intValue()>Short.MAX_VALUE||bigDecimal.intValue()<Short.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                return (T)Short.valueOf(bigDecimal.shortValue());
            }else if(t.getClass().isAssignableFrom(Long.class)){
                BigDecimal bigDecimal = new BigDecimal(value);
                if(bigDecimal.doubleValue()>Long.MAX_VALUE||bigDecimal.doubleValue()<Long.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                return (T)Long.valueOf(bigDecimal.longValue());
            }else if(t.getClass().isAssignableFrom(Double.class)){
                return (T)new Double(value);
            }else if(t.getClass().isAssignableFrom(Float.class)){
                BigDecimal bigDecimal = new BigDecimal(value);
                if(bigDecimal.doubleValue()>Long.MAX_VALUE||bigDecimal.doubleValue()<Long.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                return (T)Float.valueOf(bigDecimal.longValue());
            }else if(t.getClass().isAssignableFrom(BigDecimal.class)){
                return (T)new BigDecimal(value);
            }else if(t.getClass().isAssignableFrom(BigInteger.class)){
                return (T)new BigInteger(value);
            }else if(t.getClass().isAssignableFrom(Byte.class)){
                BigDecimal bigDecimal = new BigDecimal(value);
                if(bigDecimal.shortValue()>Byte.MAX_VALUE||bigDecimal.shortValue()<Byte.MIN_VALUE){
                    throw new IllegalArgumentException("数据越界");
                }
                return (T)new Byte(value);
            }else{
                throw new IllegalArgumentException("没找到对应类型");
            }
        }else{
            throw  new IllegalArgumentException("当前输入不能装换为目标数据");
        }
    }
	private static <T>T booleanParse(String object,T t){
        if(object.toString().equals("true")){
            return (T)new Boolean("true");
        }else if(object.toString().equals("false")){
            return (T)new Boolean("false");
        }else if(Pattern.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$",(CharSequence)object.toString())){
            BigDecimal bigDecimal = new BigDecimal(object.toString());
            if(bigDecimal.doubleValue()>0){
                return (T)new Boolean("true");
            }else{
                return (T)new Boolean("false");
            }
        }else{
            return (T)new Boolean("false");
        }
    }
    public static <T>T numberIsBlankDefault(Number object,T t,Number ...flg)
    {
        boolean isBlank=numberIsNotBlank(object,flg);
        if(!isBlank){
            return t;
        }else{
            return numberParse(object.toString(),t);
        }
    }
	public static boolean isBlank(Object obj)
	{
		boolean isBlank=true;
		if(obj==null)
		{
			return true;
		}
		isBlank = isNotBlank(obj);
        if(isBlank){
            isBlank=false;
        }else{
            isBlank = true;
        }
		return isBlank;
	}

	public static boolean isBlank(Object ... objs )
	{
		boolean isBlank=true;
		if(objs!=null)
		{
			for (Object object : objs) {
				isBlank=isBlank(object);
				if(!isBlank)
				{
					break;
				}
			}
		}
		else
		{
			isBlank=true;
		}
		return isBlank;
		
	}
	public static boolean isNotBlank(Object ... objs )
	{
		boolean isNotBlank=true;
		if(objs!=null)
		{
			for (Object object : objs) {
				boolean isBlank=isBlank(object);
				if(isBlank)
				{
					isNotBlank=false;
					break;
				}
			}
		}
		else
		{
			isNotBlank=false;
		}
		return isNotBlank;
		
	}
	public static void main(String[] args) {
       /* Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(format.format(calendar.getTime()));

        String   test = "{&quot;total&quot;:7,&quot;queryParams&quot;:{&quot;_type&quot;:&quot;all&quot;,&quot;rows&quot;:10,&quot;page&quot;:1,&quot;startDate&quot;:&quot;2018-5-1&quot;,&quot;endDate&quot;:&quot;2018-5-8&quot;},&quot;url&quot;:&quot;/statistic/dmsuserview/user/list&quot;,&quot;column&quot;:[{&quot;name&quot;:&quot;date&quot;,&quot;text&quot;:&quot;日期&quot;},{&quot;name&quot;:&quot;regist_user_num&quot;,&quot;text&quot;:&quot;注册人数&quot;},{&quot;name&quot;:&quot;regist_buyin_user_num&quot;,&quot;text&quot;:&quot;当日注册且买入人数&quot;},{&quot;name&quot;:&quot;total_buyin_user_num&quot;,&quot;text&quot;:&quot;当日总买入用户数&quot;},{&quot;name&quot;:&quot;total_user_num&quot;,&quot;text&quot;:&quot;累计注册人数&quot;},{&quot;name&quot;:&quot;total_buyin_user_num&quot;,&quot;text&quot;:&quot;累计买入独立用户数&quot;}]}";
        */
        System.out.println(ValidateObjectUtil.isBlankDefault(null,0,0));




	}
}

