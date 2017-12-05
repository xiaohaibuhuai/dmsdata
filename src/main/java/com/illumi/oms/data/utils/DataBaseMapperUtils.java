package com.illumi.oms.data.utils;

import java.util.HashMap;
import java.util.Map;

public class DataBaseMapperUtils {

	
	public static Map<String,String> getGameStatrtMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("普通局","g_normal");
		map.put("普通保险局","g_normalins");
		map.put("奥马哈局","g_omaha");
		map.put("奥马哈保险局","g_omahains");
		map.put("6+局","g_six");
		map.put("SNG局","g_sng");
		map.put("日期", "targetdate");
		map.put("汇总", "g_sum");
		return map;
	}
	public static Map<String,String> getConsumeDiamondlMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("联盟局","alliance");
		map.put("修改昵称","changename");
		map.put("延时道具","delayprops");
		map.put("MTT门票","ticketmtt");
		map.put("俱乐部推送","clubpush");
		map.put("俱乐部改名","changecname");
		map.put("日期", "targetdate");
		map.put("汇总", "sum");
		return map;
	}
	//数据库与前端映射
	public static Map<String, String> getConsumeMoneylMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("服务费","service");
			map.put("互动道具","Interprops");
			map.put("魔法表情","magic");
			map.put("翻翻看","lookover");
			map.put("弹幕","barrage");
			map.put("扑克机","pokermachine");
			map.put("加勒比","caribbean");
			map.put("牛牛_一粒大米","cow");
			map.put("八八碰_一粒大米","eight");
			map.put("打赏牌谱","rewardpoker");
			map.put("德扑币报名MTT","mttsign");
			map.put("日期", "targetdate");
			map.put("汇总", "sum");
			return map;
		}
	public static Map<String, String> getRechargelMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("苹果充值","101");
		map.put("谷歌支付","102");
		map.put("步步德扑","201");
		map.put("九格创想","202");
		map.put("微信安卓","301");
		map.put("微信公众号","302");
		map.put("微信CMS","303");
		map.put("支付宝大额","401");
		map.put("支付宝公众号","402");
		map.put("支付宝CMS","403");

		return map;
	}
}
