package com.illumi.oms.data.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBaseMapperUtils {
	public static ConcurrentHashMap<String,Map<Integer,String>> TypeMaps = new ConcurrentHashMap<>();



	public static void main(String[] args){
		Map<Integer, String> g = getGameroomTypeMap("g");
		getGameroomTypeMap("g");


		getGameroomTypeMap("g");
		getGameroomTypeMap("g");
		getGameroomTypeMap("h");
		Map<Integer, String> g1 = getGameroomTypeMap("h");

		for(Map.Entry<Integer,String> e:g1.entrySet())
		{

			System.out.println(e.getKey()+"||"+e.getValue());
		}

	}
	public static Map<Integer,String> getGameroomTypeMap(String type){

		Map<Integer, String> targetMap = TypeMaps.get(type);
		if(targetMap==null){
			Map<Integer, String> map = new HashMap<Integer, String>();
			map.put(1,type+"_normal");
			map.put(3,type+"_normalins");
			map.put(4,type+"_six");
			map.put(5,type+"_omaha");
			map.put(6,type+"_omahains");
			map.put(10,type+"_short");
			map.put(11,type+"_shortins");
			//sng 单独库 查
			//map.put("6",type+"_sng");
			Map<Integer, String> gameMap = TypeMaps.putIfAbsent(type, map);

			return map;
		}


		System.out.println("直接获取");
		return targetMap;
	}



	//玩家活跃 和牌局统计表
	public static Map<String,String> getGameMap(String type){
		Map<String, String> map = new HashMap<String, String>();
		map.put("普通局",type+"_normal");
		map.put("普通保险局",type+"_normalins");
		map.put("奥马哈局",type+"_omaha");
		map.put("奥马哈保险局",type+"_omahains");
		map.put("6+局",type+"_six");
		map.put("SNG局",type+"_sng");
		map.put("短排局",type+"_short");
		map.put("短排保险局",type+"_shortins");
		map.put("日期", "targetdate");
		map.put("汇总", type+"_sum");
		return map;
	}


	public static Map<String,String> getBaseGameMap(String type){
		Map<String, String> map = new HashMap<String, String>();
		map.put("普通局",type+"_normal");
		map.put("普通保险局",type+"_normalins");
		map.put("奥马哈局",type+"_omaha");
		map.put("奥马哈保险局",type+"_omahains");
		map.put("6+局",type+"_six");
		map.put("SNG局",type+"_sng");
		map.put("短排局",type+"_short");
		map.put("短排保险局",type+"_shortins");
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
		    map.put("奔驰宝马_一粒大米", "benz_bmw");
		    map.put("捕鱼_一粒大米", "fish");
			map.put("打赏牌谱","rewardpoker");
			map.put("德扑币报名MTT","mttsign");
			map.put("日期", "targetdate");
			map.put("奔驰宝马_一粒大米", "benz_bmw");
			map.put("捕鱼_一粒大米", "fish");
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

		//安卓微信充值
		map.put("微信CMS","303");
		map.put("支付宝大额","401");
		map.put("支付宝公众号","402");
		map.put("支付宝CMS","403");
		map.put("钻石基金","501");
		map.put("个人大额充值","502");

		map.put("安卓微信充值","301");


		return map;
	}
}
