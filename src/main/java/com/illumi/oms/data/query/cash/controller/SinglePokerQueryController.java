package com.illumi.oms.data.query.cash.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.SinglePokerItemInfo;
import com.illumi.oms.data.model.SinglePokerResponse;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.jfinal.ext.model.Db;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/query/cash/singlepoker", viewPath = UrlConfig.DATA_QUERY_CASH)
public class SinglePokerQueryController extends EasyuiController<Record>{

	private static final Logger log = Logger.getLogger(SinglePokerQueryController.class);
	
	
	public void execute() {
		String handid = getPara("handid");
		String roomid = getPara("roomid");
		
		//1根据gameid查 房间时间   减去一天
		//Record record = Db.use(Consts.DB_POKER2).queryFirst(SqlKit.sql("data.SinglePoker.getTimeByRoomid"),roomid);
		//Long startTime = record.getLong("createtime");
		//Integer overtag = record.getInt("overtag");
		Integer overtag = 0;
		String urlMethod  = "GET";
		String urlhead="ilumi_tableslog_";
		String urlend = "/_search";
//		String roomid="25204276";
//        String handid="11";
        //long startTime=1509552000000l;
        long startTime = 1511193600000l;
        String url = null;
        DateFormat df = DateUtils.getDateFormat4Day();
        String formatTime = df.format(startTime);
        if(overtag==1) {
        	  //结束牌局 两天
          long  judgeTime= DateUtils.changeHour(startTime,-7);
        	  //同一天
        	  if(df.format(judgeTime).equals(formatTime)) {
        		  url=urlhead+formatTime+urlend;
        	  }else {
        		  //今天和昨天
        		  url=ELKUtils.getUrl(startTime, urlhead, urlend, df);
        	  }
        }else {
        	//牌局正在进行
        	long  judgeTime= new Date().getTime();
        	  if(df.format(judgeTime).equals(formatTime)) {
        		  url=urlhead+formatTime+urlend;
        	  }else {
        		  url=ELKUtils.getUrl(DateUtils.changeHour(startTime, 24), urlhead, urlend, df);
        	  }
        	
        }
		//String url = ELKUtils.getUrlThreeDay(startTime, urlhead, urlend);
        System.out.println(url);
		String requetJson=getRequestJson(roomid,handid);
		//System.out.println(requetJson);
		Response data = ELKUtils.getData(requetJson, urlMethod, url);
		if(data==null) {
			renderJson("1");
		}
		Integer num = paseELKDataCount(data);
		log.info("roomid:"+roomid+"|| "+"handid:"+handid+"||"+"记录数："+num);
		//第二次访问获取数据
		if(num==null||num==0) {
			//出错
			renderJson("0");
			return;
		}
		url = url+"?size="+num;
		data = ELKUtils.getData(requetJson, urlMethod, url);
		
	    List<Map<String, Object>>  list= paseELKResponse(data);
	    
	    SinglePokerResponse res = paseELKList(list,"omaha");
	    //查房间昵称
	    String roomName = Db.use(Consts.DB_POKER2).queryStr(SqlKit.sql("data.SinglePoker.getRoomNameByid"),roomid);
	    //System.out.println(roomName);
	    res.setRoomName(roomName);
	    res.setRoomid(roomid);
	    res.setHandid(handid);
	    
		renderGson(res);
	}



	private Integer paseELKDataCount(Response data) {
		try {
			String jsonstring = EntityUtils.toString(data.getEntity());
			JSONObject jsonObj =JSON.parseObject(jsonstring);
			JSONObject hits = jsonObj.getJSONObject("hits");
			Integer num = Integer.parseInt(hits.get("total").toString());
			return num;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}


	private  Long paseUtcTime(String utctime) {
		try {
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			 Date d = format.parse(utctime.replace("Z", " UTC"));
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Map<String,Object>> paseELKResponse(Response response){
	try {
		String jsonstring = EntityUtils.toString(response.getEntity());
		List<Map<String,Object>>  list = new ArrayList<>();
		 JSONObject jsonObj =JSON.parseObject(jsonstring);
	     JSONObject hit = jsonObj.getJSONObject("hits");
	     JSONArray hits = hit.getJSONArray("hits");
	     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     for(int i=0,len=hits.size();i<len;i++){
	    	     Map<String,Object> map = new HashMap<>();
	    	     JSONObject shit = hits.getJSONObject(i);
	    	     JSONObject source = shit.getJSONObject("_source");
	    	      Set<Entry<String, Object>> entrySet = source.entrySet();
	    	      for(Entry<String, Object> e :entrySet) {
	    	    	   String key = e.getKey();
	    	    	   Object value = null;
	    	    	   if(key.equals("@timestamp")) {
	    	    		   value = df.format(paseUtcTime(e.getValue().toString()));
	    	    	   }else {
	    	    	      value = e.getValue();
	    	    	   }
	    	    	   map.put(key, value);
	    	      }
	    	      list.add(map);
	     }
	     return list;   
	} catch (Exception e) {
		
		e.printStackTrace();
	} 
	return null;
	
	}
	private SinglePokerResponse paseELKList(List<Map<String, Object>> list,String type) {
		
		SinglePokerResponse res = new SinglePokerResponse();
		
		SinglePokerItemHandle handle = new SinglePokerItemHandle();
		
		List<SinglePokerItemInfo> beforelist = new ArrayList<>();
		List<SinglePokerItemInfo> floplist = new ArrayList<>();
		List<SinglePokerItemInfo> turnlist = new ArrayList<>();
		List<SinglePokerItemInfo> riverlist = new ArrayList<>();
		List<SinglePokerItemInfo> resultlist = new ArrayList<>();
		List<SinglePokerItemInfo> handPokerlist = new ArrayList<>();
		
	//   1:before  2:flop 3:turn 4:river 5:结算 6:手牌
	    Integer flag = 1;
		for(Map<String,Object> m:list) {
			//1按时间顺序
			String actionType = m.get("action_type")+"";

			if(actionType.equals("flop")) {
				SinglePokerItemInfo flop = handle.handle(m, actionType);
				res.setFlopPokerDes(flop.getValue().toString());
				flag = 2;
				continue;
			}else if(actionType.equals("turn")) {
				SinglePokerItemInfo turn = handle.handle(m, actionType);
				res.setTurnPokerDes(turn.getValue().toString());
				flag = 3;
				continue;
			}else if(actionType.equals("river")) {
		        SinglePokerItemInfo river = handle.handle(m, actionType);
			    res.setRiverPokerDes(river.getValue().toString());
			    flag = 4;
			    continue;
			}else if(actionType.equals("showdown")) {
				flag = 5 ;
			}else if(actionType.equals("hand")) {
				flag = 6;
			}else if(actionType.equals("cleartable")) {
				break;
			}
			
			// 封装数据
			if(flag==1) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
					beforelist.add(h);
				}
				
			}else if(flag==2) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
				floplist.add(h);
				}
			}else if(flag==3) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
				turnlist.add(h);
				}
			}else if(flag==4) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
				riverlist.add(h);
				}
			}else if(flag==5) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
				resultlist.add(h);
				}
			}else if(flag==6) {
				SinglePokerItemInfo h = handle.handle(m, actionType);
				if(h!=null) {
				handPokerlist.add(h);
				}
			}
		}
		
		res.setBefore(beforelist.size()==0?null:beforelist);
		res.setFlop(floplist.size()==0?null:floplist);
		res.setTurn(turnlist.size()==0?null:turnlist);
		res.setRiver(riverlist.size()==0?null:riverlist);
		res.setResult(resultlist.size()==0?null:resultlist);
		res.setHandPoker(handPokerlist.size()==0?null:handPokerlist);
		
		return res;
	}


	


	private String getRequestJson(String roomid,String handid) {
		String jsonString="{\n" + 
				" \"query\": {\n" + 
				"  \"bool\": {\n" + 
				"    \"must\": [\n" + 
				"      {\n" + 
				"        \"constant_score\": {\n" + 
				"          \"filter\": {\"term\": {\n" + 
				"            \"RoomID\": \""+roomid+"\"\n" + 
				"          }\n" + 
				"          },\n" + 
				"          \"boost\": 1.2\n" + 
				"        }\n" + 
				"      },{\n" + 
				"      \"constant_score\": {\n" + 
				"        \"filter\": {\"term\": {\n" + 
				"          \"HandID\": \""+handid+"\"\n" + 
				"        }},\n" + 
				"        \"boost\": 1.2\n" + 
				"      }\n" + 
				"      }\n" + 
				"    ]\n" + 
				"    \n" + 
				"  }\n" + 
				"},\"sort\": [\n" + 
				"  {\n" + 
				"    \"@timestamp\": {\n" + 
				"      \"order\": \"asc\"\n" + 
				"    }\n" + 
				"  }\n" + 
				"]\n" + 
				"}";
		return jsonString;
	}
}
