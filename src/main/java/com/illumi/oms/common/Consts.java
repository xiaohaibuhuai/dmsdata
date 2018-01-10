package com.illumi.oms.common;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Consts
{

	
	public static final String SESSION_USER = "user";
	public static final int SEND_TYPE_NOW = 1;
	/***
	 * 分布式session开关 请在redis.properties 配置ip和端口
	 */
	public static final boolean OPEN_REDIS = true;
	//牌局类型
	public static final int POKEY_TYPE_GROUP = 1;//圈子
	public static final int POKEY_TYPE_CLUB = 2;//俱乐部
	public static final int POKEY_TYPE_FAST = 3;//快速局
	//统计的数据类型  1:每日用户数，2每日用户增量，3每日活跃用户，4每日俱乐部数，5每日俱乐部增量
	public static final int DATA_USER_TOTAL = 1;
	public static final int DATA_USER_INC = 2;
	public static final int DATA_USER_ACTIVE = 3;
	public static final int DATA_CLUB_TOTAL = 4;
	public static final int DATA_CLUB_INC = 5;
	
	//比赛记录状态 比赛状态 0：待执行 ，1：已执行，2：已失效
	public static final int RACE_STATUS_NEW = 0; 
	public static final int RACE_STATUS_DONE = 1;
	public static final int RACE_STATUS_CANCEL = 2;
	//消息推送记录状态 0：待执行 ，1：已执行，2：已失效
	public static final int APNS_STATUS_NEW = 0; 
	public static final int APNS_STATUS_DONE = 1;
	public static final int APNS_STATUS_CANCEL = 2;
	//语言类型 1：中文，2：英文，3:繁体
	public static final int LANG_ZH = 1;
	public static final int LANG_EN = 2;
	public static final int LANG_ZHT = 3;
	
	//数据源名称
	public static final String DB_POKER = "pokerdb";
	//数据源拆分库
	public static final String DB_POKER2 = "pokerdb2";
    //data查询库
    public static final String DB_POKERDATA = "pokerdata";
	// 配置更新库
	public static final String DB_PRO = "prodb";
	//数据源日志查询库
    public static final String DB_STAT = "statdb";
	//商品类型
	public static final String MCODE_SILVER="9000001"; //银卡
	public static final String MCODE_GOLD="9000002";//金卡
	public static final String MCODE_SOIL="9000003";//土卡
	
	
	public static  HashMap<String,String> locationMap = new HashMap<String,String>();
	
	static{
		String json = "{1:'北京',2:'上海',114001:'广州',114002:'深圳',3:'香港',4:'澳门',112001:'武汉',105001:'南京',119001:'西安',111001:'郑州',116001:'成都',5:'天津',105002:'苏州',6:'重庆',7:'海外',100000:'河北省',101000:'山西省',102000:'辽宁省',103000:'吉林省',104000:'黑龙江省',105000:'江苏省',106000:'浙江省',107000:'安徽省',108000:'福建省',109000:'江西省',110000:'山东省',111000:'河南省',112000:'湖北省',113000:'湖南省',114000:'广东省',115000:'海南省',116000:'四川省',117000:'贵州省',118000:'云南省',119000:'陕西省',120000:'甘肃省',121000:'青海省',122000:'台湾省',123000:'内蒙古自治区',124000:'广西壮族自治区',125000:'宁夏回族自治区',126000:'新疆维吾尔自治区',127000:'西藏自治区'}";
		Gson gson = new Gson();
		locationMap = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
	}

	public static void main(String[] args){
		for(String str:Consts.locationMap.keySet()){
			System.out.println(str);
		}
		System.out.println(Consts.locationMap.keySet().size());
	}

}
