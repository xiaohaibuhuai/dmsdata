package com.illumi.oms.data.utils;

import java.util.HashMap;
import java.util.Map;

//表--日志映射
public class LogMapperUtils {

	private LogMapperUtils() {};


    /**
	 * 充值渠道
	 *
	 * 前端进行匹配解析
	 */

	public static String[]  getRechargeMapper(){

		String[] rechargeMapperArr= {"101","102","201","202",
				"301","302","303","401","402","403","501","502","sum"};

		return rechargeMapperArr;
	}

	/**
	 * 
	 * @return  钻石日志消耗映射
	 */
	public static Map<String,String> getDiamondMapper(){
		Map<String,String> map  = new HashMap<String,String>();
		map.put("sum", null);
		
		
		// 110001 -- 联盟局设置.          alliance   null
		map.put("alliance", "联盟局设置");
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
		
//		map.put("platinumcard","兑换白金卡");
//		map.put("","兑换金卡");


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
	    	   map.put("barrage", "发送弹幕");
			//500000  扑克机
	    	   map.put("pokermachine", "扑克机游戏");
			//500001   加勒比
	    	   map.put("caribbean", "加勒比游戏");
		   //600001  牛牛_一粒大米
	    	   map.put("cow", "600001");
	    	   //600002 八八碰_一粒大米
	    	   map.put("eight", "600002");
	    	   //600003 奔驰宝马_一粒大米
	           map.put("benz_bmw", "600003");
	       //600004 捕鱼_一粒大米
	           map.put("fish", "600004");

		    //800002  打赏牌谱
	    	   map.put("rewardpoker", "牌谱打赏");
			//120001 德扑币报名
	    	   map.put("mttsign", "德扑币报名MTT");
	    	   return map;
	}


	public static Map<String,Look> getLookMapper(){
		Map<String,Look> map = new HashMap<String,Look>();
		map.put("animateItem#item1", new Look("animateItem#item1", "互动道具", "kiss"));
		map.put("animateItem#item2", new Look("animateItem#item2", "互动道具", "cheers"));
		map.put("animateItem#item3", new Look("animateItem#item3", "互动道具", "holy_shit"));
		map.put("animateItem#item4", new Look("animateItem#item4", "互动道具", "niubility"));
		map.put("animateItem#item5", new Look("animateItem#item5", "互动道具", "bomb"));
		map.put("animateItem#item6", new Look("animateItem#item6", "互动道具", "bluff"));
		map.put("animateItem#item7", new Look("animateItem#item7", "互动道具", "an_wei"));
		map.put("animateItem#item8", new Look("animateItem#item8", "互动道具", "nice_hand"));
		map.put("animateItem#item9", new Look("animateItem#item9", "互动道具", "red_packet"));
		map.put("animateItem#item10", new Look("animateItem#item10", "互动道具", "fish"));
		map.put("animateItem#item12", new Look("animateItem#item12", "互动道具", "shark"));
		map.put("animateItem#item15", new Look("animateItem#item15", "互动道具", "m-gun"));


		map.put("animate#magicface1", new Look("animate#magicface1", "魔法表情", "race"));
		map.put("animate#magicface2", new Look("animate#magicface2", "魔法表情", "raise"));
		map.put("animate#magicface3", new Look("animate#magicface3", "魔法表情", "take_ur_time"));
		map.put("animate#magicface4", new Look("animate#magicface4", "魔法表情", "missed"));
		map.put("animate#magicface5", new Look("animate#magicface5", "魔法表情", "i_will_get_u"));
		map.put("animate#magicface6", new Look("animate#magicface6", "魔法表情", "fishies"));
		map.put("animate#magicface7", new Look("animate#magicface7", "魔法表情", "hahaha"));
		map.put("animate#magicface8", new Look("animate#magicface8", "魔法表情", "dont_bb_me"));
		map.put("animate#magicface9", new Look("animate#magicface9", "魔法表情", "ur_too_good"));
		map.put("animate#magicface10", new Look("animate#magicface10", "魔法表情", "oh_no"));
		map.put("animate#magicface11", new Look("animate#magicface11", "魔法表情", "no_more_chasing"));
		map.put("animate#magicface12", new Look("animate#magicface12", "魔法表情", "lets_do_it"));


		return map;


	}


}


