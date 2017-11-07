package com.illumi.oms.cheat.model.Json;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.illumi.oms.cheat.utils.StringUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

public class CheatResponse implements Serializable {

	/**
	 * 
	 * @fieldName: serialVersionUID
	 * 
	 * @fieldType: long
	 * 
	 * @Description: TODO
	 * 
	 */
	private static final long serialVersionUID = 3443046764278756394L;

	private static final String MULTI_CHEATER = "<div class=\"result_li_text\"> {0} 之间有团伙作弊嫌疑 </div><br/>";

	private static final String DESC = "本结果根据玩家 {0} 至 {1} 的行为数据，综合判定，结果以进度条的形式进行直观的展示. 为节省空间, 绿色区域的玩家关系不予展示.例如:";

	private static final String PLAYER_FORMAT = "玩家 {0}({1}) 共参考{2}条历史数据, 该玩家在本俱乐部/联盟入局数 {3} 次";

	private static final Logger logger = Logger.getLogger(CheatResponse.class);

	@SerializedName("cheatInfo")
	private List<CheatInfo> vCheatInfo;

	@SerializedName("cheatGroup")
	private List<CheatGroup> vCheatGroup;

	private List<Record> userInfo;

	@SerializedName("playTimes")
	private List<PlayTimes> times;

	private long startTime;

	private long endTime;

	public List<CheatInfo> getvCheatInfo() {
		return vCheatInfo;
	}

	public void setvCheatInfo(List<CheatInfo> vCheatInfo) {
		this.vCheatInfo = vCheatInfo;
	}

	public List<CheatGroup> getvCheatGroup() {
		return vCheatGroup;
	}

	public void setvCheatGroup(List<CheatGroup> vCheatGroup) {
		this.vCheatGroup = vCheatGroup;
	}

	public List<Record> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<Record> userInfo2) {
		this.userInfo = userInfo2;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<PlayTimes> getTimes() {
		return times;
	}

	public void setTimes(List<PlayTimes> times) {
		this.times = times;
	}

	public Map<String, Object> getIntroducation() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> singleList = new ArrayList<Map<String, Object>>();

		// 两两作弊嫌疑
		for (Record user : userInfo) {
			Map<String, Object> singleMap = new HashMap<String, Object>();
			String uuid = String.valueOf(user.get("uuid"));

			// 描述
			//玩家 {0}({1}) 共参考{2}条历史数据, 该玩家在本俱乐部/联盟入局数 {3} 次
			String desc = MessageFormat.format(PLAYER_FORMAT, user.get("nickname"), String.valueOf(user.get("showid")),
					getReferenceGameTimes(String.valueOf(user.get("uuid"))),
					getPlayTimes(String.valueOf(user.get("uuid"))), getRecordTimes(String.valueOf(user.get("uuid"))));
			singleMap.put("desc", desc);

			//System.out.println("1userInfo信息" + uuid + "     描述：" + desc);

			// 当前玩家
			singleMap.put("self", user);
       
			// 作弊同伙
			List<Record> singlePartnerList = new ArrayList<Record>();
			for (int i = 0; i < vCheatInfo.size(); i++) {
				CheatInfo info = vCheatInfo.get(i);
				
				if (info.contains(uuid)) {
					String partnerUuid = info.getPartner(uuid);
					if (StrKit.isBlank(partnerUuid)) {
						continue;
					}
					Record partner = getUserBaseInfo(partnerUuid);
					
					/**
					 * 17.11.6 空指针判定
					 */
					if(partner==null) {
						continue;
					}
					partner.set("score", info.getScore());
					partner.set("sameTime", info.getSameTimes());
					singlePartnerList.add(partner);
					
					//System.out.println("2作弊同伙：  "+partnerUuid+"  分数" +info.getScore());
				}
			}
			singleMap.put("partnerList", singlePartnerList);
			singleList.add(singleMap);
		}

		map.put("single", singleList);
        
		// 团伙作弊嫌疑
		List<Map<String, Object>> multipleList = new ArrayList<Map<String, Object>>();
		for (int m = 0; vCheatGroup != null && m < vCheatGroup.size(); m++) {
			Map<String, Object> multipleMap = new HashMap<String, Object>();
			StringBuilder desc = new StringBuilder();
			CheatGroup group = vCheatGroup.get(m);
			String[] cheaters = group.getUuid().split(",");   
   //         System.out.println("3  :"+cheaters.length);
			// 描述 
			if (cheaters.length > 2) {
				StringBuffer allCheaters = new StringBuffer();
				for (String cheaterId : cheaters) {
					//获取要查找用户基本信息
					Record cheater = getUserBaseInfo(cheaterId);
					/**
					 * 2017.11.6 空指针异常
					 */
					if(cheater==null) {
						continue;
					}
					logger.warn(" cheat : " + cheater + " | " + cheaterId);
					allCheaters.append(cheater.get("nickname") + "(" + cheater.get("showid") + ") ");
			//		System.out.println("4  :"+allCheaters);
				}
				desc.append(MessageFormat.format(MULTI_CHEATER, allCheaters));
				
			} else {
				//跳过本次循环 进行下一个group解析
				continue;
			}
			//每个group描述
			multipleMap.put("desc", desc.toString());
			// 作弊团伙
			List<Object> coupleList = new ArrayList<Object>();
			for (int i = 0; i < cheaters.length - 1; i++) {
				for (int j = i + 1; j < cheaters.length; j++) {
					for (CheatInfo info : vCheatInfo) {
						//如果group组里的每两个相互比较  在cheatInfo里能找到
						if (info.contains(cheaters[i]) && info.contains(cheaters[j])) {
							Map<String, Object> cheatDetailMap = new HashMap<String, Object>();
							cheatDetailMap.put("a", getUserBaseInfo(cheaters[i]));
							cheatDetailMap.put("b", getUserBaseInfo(cheaters[j]));
							cheatDetailMap.put("score", info.getScore());
							cheatDetailMap.put("sameTimes", info.getSameTimes());
							coupleList.add(cheatDetailMap);
						}
					}
				}
			}
			multipleMap.put("coupleList", coupleList);
			//每个作弊组的信息
			multipleList.add(multipleMap);
		}

		map.put("multiple", multipleList);

		// 整体描述
		String desc = MessageFormat.format(DESC, StringUtil.timeStampToDateNormal(endTime, 10),
				StringUtil.timeStampToDateNormal(startTime, 10));
		map.put("desc", desc);
		return map;
	}

	private Object getReferenceGameTimes(String uuid) {
		for (int i = 0; i < times.size(); i++) {
			logger.debug("checking user reference times : " + uuid + " , " + times.get(i).getUuid() + " , "
					+ (times.get(i).getUuid().equals(uuid)));
			if (times.get(i).getUuid().equals(uuid)) {
				return times.get(i).getRecordTimes();
			}
		}
		return null;
	}

	private Object getPlayTimes(String uuid) {
		for (int i = 0; i < times.size(); i++) {
			logger.debug("checking user play times : " + uuid + " , " + times.get(i).getUuid() + " , "
					+ (times.get(i).getUuid().equals(uuid)));
			if (times.get(i).getUuid().equals(uuid)) {
				return times.get(i).getPlayTimes();
			}
		}
		return null;
	}

	private Object getRecordTimes(String uuid) {
		for (int i = 0; i < times.size(); i++) {
			logger.debug("checking user play times : " + uuid + " , " + times.get(i).getUuid() + " , "
					+ (times.get(i).getUuid().equals(uuid)));
			if (times.get(i).getUuid().equals(uuid)) {
				return times.get(i).getRecordTimes();
			}
		}
		return null;
	}

	private Record getUserBaseInfo(String uuid) {
		for (Record UserBaseInfo : userInfo) {
			if (Integer.parseInt(String.valueOf(UserBaseInfo.get("uuid"))) == Integer.parseInt(uuid)) {
				return UserBaseInfo;
			}
		}
		return null;
	}

}
