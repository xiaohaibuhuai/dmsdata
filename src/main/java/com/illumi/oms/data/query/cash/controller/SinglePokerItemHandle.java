package com.illumi.oms.data.query.cash.controller;

import java.util.HashMap;
import java.util.Map;

import com.illumi.oms.common.Consts;
import com.illumi.oms.data.model.SinglePokerItemInfo;
import com.jayqqaa12.jbase.jfinal.ext.model.Db;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.log.Logger;

public class SinglePokerItemHandle {

	private static final Logger log = Logger.getLogger(SinglePokerItemHandle.class);
	private  Map<Long,String> map = new HashMap<>();

	public static void main(String[] args) {
		SinglePokerItemHandle s = new SinglePokerItemHandle();
		String pokerSuitDes = s.getPokerSuitDes(14);
		String pokerSuitDes1 = s.getPokerSuitDes(26);
		String pokerSuitDes2 = s.getPokerSuitDes(48);
		System.out.println(pokerSuitDes);
		System.out.println(pokerSuitDes1);
		System.out.println(pokerSuitDes2);
	}




	public SinglePokerItemInfo handle(Map<String, Object> m, String type) {
		switch (type) {
			case "ante":
				return paseAnte(m, "前注");
			case "sb":
				return paseSb(m, "小盲");
			case "bb":
				return paseBB(m, "大盲");
			case "raise":
				return paseRaise(m, "加注");
			case "call":
				return paseCall(m, "跟注");
			case "check":
				return paseCheck(m, "看牌");
			case "fold":
				return paseFold(m, "弃牌");
			case "insconfig":
				// 日志目前没有
				return paseInsconfig(m, "保险开关");
			case "insbuy":
				return paseInsbuy(m, "买保险");
			case "inscover":
				// 目前没有
				return paseInscover(m, "背保险");
			case "insresult":
				return paseInsresult(m, "碎保险");
			case "insfold":
				// 日志数据不全
				return paseInsfold(m, "放弃保险");
			case "selectouts":
				// 目前日志没有
				return paseSelectouts(m, "选outs");
			case "showcard":
				return paseShowcard(m, "亮牌");
			case "hand":
				return paseHand(m, "手牌");
			case "showdown":
				return paseShowdown(m, "赢取");
			case "flop":
				return paseFlop(m, "Flop发牌");
			case "turn":
				return paseTurn(m, "Turn发牌");
			case "river":
				return paserRiver(m, "River发牌");
			case "preaction":
				return pasePreaction(m,"preaction");
			default:
				return paserOther(m);
		}
	}



	/**
	 * 优化查询  缓存
	 */
	private String getUserName(Long uuid) {
		if(!map.containsKey(uuid)) {
			String name = Db.use(Consts.DB_POKER).queryStr(SqlKit.sql("data.SinglePoker.getUserNickNameByid"), uuid);
			if (name != null) {
				map.put(uuid, name);
				return name;
			}
			log.error("NOT FIND USER ||" + uuid);
		}else {
			System.out.println("map缓存");
			return map.get(uuid);
		}

		return null;
	}

	private SinglePokerItemInfo basePase(Map<String, Object> m, String type) {
		// 优化只创建一个对象
		SinglePokerItemInfo pinfo = new SinglePokerItemInfo();
		// 1 uuid
		String uuid = m.get("uuid").toString();
		// 2 昵称
		if (uuid != null) {
			pinfo.setUuid(Long.parseLong(uuid));
			String userName = getUserName(Long.parseLong(m.get("uuid") + ""));
			if (userName != null) {
				pinfo.setNickName(userName);
			}
		}
		// 3时间
		String time = m.get("@timestamp").toString();
		pinfo.setTime(time);

		// 4 key

		pinfo.setKey(type);
		return pinfo;
	}

	//["17","6"]  转换为 int数组
	private Integer[] String2IntgerArr(String s) {
		String[] split = s.split(",");
		int length = split.length;
		Integer[] intarr = new Integer[length];
		for (int i = 0; i < length; i++) {
			String replace = split[i].replace("\"", "");
			replace = replace.replace("[", "");
			replace = replace.replace("]", "");
			intarr[i] = Integer.parseInt(replace);
		}

		return intarr;
	}




	// 获取数字对应的牌
	private String getPokerSuitDes(Integer i) {
		int value = i % 13;
		value = value + 1;
		String strvalue = null;
		switch (value) {
			case 13:strvalue = "K";break;
			case 12:strvalue = "Q";break;
			case 11:strvalue = "J";break;
			case 1: strvalue = "A";break;
			default:strvalue = value + "";break;
		}
		// 0-12代表黑桃Spade, 13-25红桃Heart, 26-38方块Diamond, 39-51梅花Club）
		int dec = i / 13;
		String strdec = null;
		switch (dec) {
			case 0:strdec = "s";break;
			case 1:strdec = "h";break;
			case 2:strdec = "d";break;
			case 3:strdec = "c";break;
			default:break;
		}
		String result = strvalue+strdec;
		log.info("牌型数字："+i+"||"+"解析后:"+result);
		return result;
	}

	// 重载 拼接字符串 As,Ks,Qs
	private String getPokerSuitDes(Integer[] arr) {
		String des = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				des += getPokerSuitDes(arr[i]);


			} else {
				des += getPokerSuitDes(arr[i]) + ",";

			}
		}
		return des;
	}

	private SinglePokerItemInfo paserOther(Map<String, Object> m) {

		SinglePokerItemInfo sinfo = basePase(m, m.get("action_type").toString());

		return sinfo;
	}
	private SinglePokerItemInfo paserRiver(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		String s = m.get("card")+"";
		//转换为Integer数组
		Integer[] arr = String2IntgerArr(s);
		sinfo.setValue(getPokerSuitDes(arr));
		return sinfo;
	}

	private SinglePokerItemInfo paseTurn(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		String s = m.get("card")+"";
		//转换为Integer数组
		Integer[] arr = String2IntgerArr(s);
		sinfo.setValue(getPokerSuitDes(arr));
		return sinfo;
	}

	private SinglePokerItemInfo paseFlop(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		String s = m.get("card")+"";
		//转换为Integer数组
		System.out.println("转换前的字符串:"+s);
		Integer[] arr = String2IntgerArr(s);
		for(int a:arr) {
			System.out.println("转换后的数字："+a+"||");
		}
		sinfo.setValue(getPokerSuitDes(arr));
		return sinfo;
	}


	private SinglePokerItemInfo paseShowdown(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);

		String str = m.get("showdown_type").toString();
		String value = null;
		if(str!=null) {
			if(str.equals("noshow")) {
				value="未摊牌";
			}else if(str.equals("show")){
				value="摊牌";
			}
		}

		//String num = m.get("showdown_number").toString();
		String num = m.get("coin_change_no").toString();
		sinfo.setValue(value+":"+num);
		return sinfo;
	}

	private SinglePokerItemInfo paseHand(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		String s = m.get("card")+"";
		//转换为Integer数组
		Integer[] arr = String2IntgerArr(s);

		String remainNum=m.get("coin_rest_no")+"";
		sinfo.setValue(getPokerSuitDes(arr)+"   初始计分牌:"+remainNum);
		return sinfo;
	}

	private SinglePokerItemInfo paseShowcard(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		// 返回一个或两个数
		String s = m.get("card") + "";
		//转换为Integer数组
		Integer[] arr = String2IntgerArr(s);

		sinfo.setValue(getPokerSuitDes(arr));
		return sinfo;
	}

	private SinglePokerItemInfo paseSelectouts(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		return sinfo;
	}

	// 直接放弃保险
	private SinglePokerItemInfo paseInsfold(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		return sinfo;
	}

	private SinglePokerItemInfo paseInsresult(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		if(m.get("uuid").equals("1000")) {
			return null;
		}


		if(m.get("insresult_number").toString().equals("0")) {
			sinfo.setValue("保险未碎");
		}else {
			//sinfo.setValue(m.get("insresult_number").toString());
			sinfo.setValue(m.get("coin_cost_no").toString());
		}
		return sinfo;
	}

	// 日志没有
	private SinglePokerItemInfo paseInscover(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		return sinfo;
	}

	private SinglePokerItemInfo paseInsbuy(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		if(m.get("uuid").equals("1000")) {
			return null;
		}
		//	sinfo.setValue(m.get("insbuy_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());

		return sinfo;
	}

	// 日志没有
	private SinglePokerItemInfo paseInsconfig(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		// 目前没有值
		return sinfo;
	}

	private SinglePokerItemInfo paseFold(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		String result = m.get("fold_type").toString();
		if (result.equals("0")) {
			sinfo.setValue("主动弃牌");
		} else if (result.equals("tuoguan")) {
			sinfo.setValue("托管");
		} else if (result.equals("auto")) {
			sinfo.setValue("弃牌");
		}
		return sinfo;
	}

	private SinglePokerItemInfo paseCheck(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		// check_type 只有0
		return sinfo;
	}

	private SinglePokerItemInfo paseCall(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		//sinfo.setValue(m.get("call_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());
		return sinfo;
	}

	private SinglePokerItemInfo paseRaise(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		//sinfo.setValue(m.get("raise_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());
		return sinfo;
	}

	private SinglePokerItemInfo paseBB(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		//sinfo.setValue(m.get("bb_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());
		return sinfo;
	}

	private SinglePokerItemInfo paseSb(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		//sinfo.setValue(m.get("sb_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());
		return sinfo;
	}

	private SinglePokerItemInfo paseAnte(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);
		//sinfo.setValue(m.get("ante_number").toString());
		sinfo.setValue(m.get("coin_cost_no").toString());
		return sinfo;
	}


	private SinglePokerItemInfo pasePreaction(Map<String, Object> m, String type) {
		SinglePokerItemInfo sinfo = basePase(m, type);

		sinfo.setValue(m.get("preaction_state"));



		return sinfo;
	}
}
