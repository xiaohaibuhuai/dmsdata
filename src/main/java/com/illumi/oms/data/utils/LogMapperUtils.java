package com.illumi.oms.data.utils;

import java.util.HashMap;
import java.util.Map;

//表--日志映射
public class LogMapperUtils {

	private LogMapperUtils() {};
	
	/**
	 * 
	 * @return  钻石日志消耗映射
	 */
	public static Map<String,String> getDiamondMapper(){
		Map<String,String> map  = new HashMap<String,String>();
		 map.put("sum", null);
		
		
		// 110001 -- 联盟局设置.          alliance   null
		map.put("alliance", "联盟局");
		// 450002 -- 修改昵称:            changename  null
		map.put("changename", "修改昵称");
		//430003 -- 延时道具              delayprops  
		map.put("delayprops", "延时道具");
		//460001  --  Mtt门票             ticketmtt   null
		map.put("ticketmtt", "MTT门票");
		//450004 -- 俱乐部推送             clubpush    null
		map.put("clubpush", "俱乐部推送");
		//450005 -- 俱乐部修改名称         changecname  null
		map.put("changecname", "俱乐部改名");
		
		
		return map;
	}
	
	/*
	 * 德扑币消耗日志映射
	 */
	public static Map<String,String> getMoneyMapper(){
		Map<String,String> map = new HashMap<String,String>();
		   map.put("sum", null);
			//110000   玩家买入*0.1          服务费
	    	   map.put("service", "玩家买入");
			//430001    表情id      1 魔法表情   2互动道具   null
	    	   map.put("Interprops", "互动道具");
	    	   map.put("magic", "魔法表情");
			//430004 翻翻看   
	    	   map.put("lookover", "翻翻看");
			//450003 -- 发送弹幕   null
	    	   map.put("barrage", "弹幕");
			//500000  扑克机
	    	   map.put("pokermachine", "扑克机游戏");
			//500001   加勒比
	    	   map.put("caribbean", "加勒比游戏");	
		    //500002    红黑大战   数据库和日志都无		
		    //510000  游戏代码    1  牛牛  2 八八碰
	    	   map.put("cow", "牛牛_一粒大米");
	    	   map.put("eight", "八八碰_一粒大米");	
		    //800002  打赏牌谱
	    	   map.put("rewardpoker", "打赏牌谱");
			//120001 德扑币报名
	    	   map.put("mttsign", "德扑币报名MTT");
	    	   
	    	   return map;
	}
}
