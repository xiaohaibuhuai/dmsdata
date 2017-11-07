package com.illumi.oms.data.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.Consts;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.model.RankInfo;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ELKUtils {

	
	/**
	 * 获取变化率数据从Log  
	 */
	public static List<ChartInfo> getTaskChartChangeInfo(String target,long time,String timeformat) {
		long nowTime = new Date().getTime();
		//long startTime = DateUtils.changeHour(nowTime, hourtime);
		long startTime = nowTime + time;
		String urlMethod="GET";
		String urlheadTask="/ilumi_task_coinanddiamond_";
	    String urlend="/_search";
	    String urlTask = DateUtils.getUrl(nowTime,urlheadTask,urlend);
	    
	    String jsonString = "{\n" + "  \"query\": {\n" + "\"constant_score\": {\n" + "\"filter\": {\"range\": {\n" + "\"@timestamp\": {\n" + "\"gte\": "
	    +startTime+",\n" + 
        		"\"lte\": "+nowTime+"\n" + 
				"        }\n" + 
				"      }}\n" + 
				"    }\n" + 
				"  }, \"aggs\": {\n" + 
				"    \"NAME\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"format\": \"yyyy-MM-dd HH:mm:ss\", \n" + 
				"        \"field\": \"@timestamp\",\n" + 
				"        \"interval\": \""+timeformat+"\"\n" + 
				"      }, \"aggs\": {\n" + 
				"        \"money_change\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"money_change_no\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"diamone_change\":{\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"diamond_change_no\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"ticket_change\":{\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"ticket_change_no\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		return getChartChangeInfo(jsonString, urlMethod, urlTask, target);
	    
		}
	public static List<ChartInfo> getLogChartChangeInfo(String target,long time,String timeformat) {
		long nowTime = new Date().getTime();
//		long startTime = DateUtils.changeHour(nowTime, hourtime);
		long startTime = nowTime + time;
		String urlMethod="GET";
	    String urlheadLog="/ilumi_transctionlog_";
	    String urlend="/_search";
	    String urlLog = DateUtils.getUrl(nowTime,urlheadLog,urlend);
	    
	    String jsonString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"constant_score\": {\n" + 
				"      \"filter\": {\"range\": {\n" + 
				"        \"@timestamp\": {\n" + 
        		"          \"gte\": "+startTime+",\n" + 
        		"          \"lte\": "+nowTime+"\n" + 
				"        }\n" + 
				"      }}\n" + 
				"    }\n" + 
				"  }, \"aggs\": {\n" + 
				"    \"NAME\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"format\": \"yyyy-MM-dd HH:mm:ss\", \n" + 
				"        \"field\": \"@timestamp\",\n" + 
				"        \"interval\": \""+timeformat+"\"\n" + 
				"      }, \"aggs\": {\n" + 
				"        \"money_change\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"money_change_no\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"diamone_change\":{\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"diamond_change_no\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"ticket_change\":{\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"ticket_change_no\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		return getChartChangeInfo(jsonString, urlMethod, urlLog, target);
	    
		}
	
	
	/**
	 * 获取变化率数据 
	 * @param jsonString
	 * @param urlMethod
	 * @param url
	 * @param target
	 * @return
	 */
	public static List<ChartInfo> getChartChangeInfo(String jsonString,String urlMethod,String url,String target) {
	    //"/ilumi_transctionlog_2017-10-11,ilumi_transctionlog_2017-10-12/_search";
	    try {
	    Response response = ELKUtils.getDate(jsonString, urlMethod, url);
	    List<ChartInfo> list = ELKUtils.paseChartJson(response,target);
	    return list;
	    }catch (Exception e) {
			e.printStackTrace();
		}
			return null;
		}
	
	
	/**
	 * 排名信息
	 * @param jsonString
	 * @param urlMethod
	 * @param url
	 * @param target
	 * @return
	 */
	
	public  static  List<RankInfo>  getRankInfo(String jsonString,String urlMethod,String url,String target) {
		try {
		    Response response = ELKUtils.getDate(jsonString, urlMethod, url);
		    List<RankInfo> list = ELKUtils.paseRankJson(response,target);
		    return list;
		    }catch (Exception e) {
				e.printStackTrace();
			}
				return null;
	}
	
	/**
	 * 链接ELK 
	 * @param jsonString
	 * @param method
	 * @param url
	 * @return
	 * @throws IOException  是否该抛异常
	 */
	private static Response getDate(String jsonString,String method,String url)  {
		try {
		    HttpHost httpHost = new HttpHost("10.105.92.212",9200,"http");
	        RestClient restClient = RestClient.builder(httpHost).build();
	        Map<String, String> params = Collections.singletonMap("pretty", "true");
	        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
	        Response response = restClient.performRequest(method,url,params,entity);
		    return response;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 解析图表Json
	 * @param response
	 * @return CoinChartInfo
	 */
	private static List<ChartInfo> paseChartJson(Response response,String target) {
		try {
		String jsonstring = EntityUtils.toString(response.getEntity());
	    JSONObject jsonObj =JSON.parseObject(jsonstring);
	     JSONObject ag = jsonObj.getJSONObject("aggregations");
	     JSONObject name = ag.getJSONObject("NAME");
	     JSONArray arry = name.getJSONArray("buckets");
	     
	     List<ChartInfo> list = new ArrayList<>();
	     
	     for(int i=0,len=arry.size();i<len;i++){
	    	    JSONObject temp=  arry.getJSONObject(i);
	    	    
	    	    //String time = temp.getString("key_as_string");
	    	    Long timenum = temp.getLong("key");
	    	    Long  num = temp.getJSONObject(target).getLong("value");
	    	    
	    	    String time = new SimpleDateFormat("HH").format(new Date(timenum))+":00";
	    	    //String time = new SimpleDateFormat("HH:mm").format(new Date(timenum));
	    	    list.add(new ChartInfo(time, num));
	    	    
	     }
	     return list;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//解析排名Json
      private static List<RankInfo> paseRankJson(Response response, String target) {try {
		String jsonstring = EntityUtils.toString(response.getEntity());
	    JSONObject jsonObj =JSON.parseObject(jsonstring);
	     JSONObject ag = jsonObj.getJSONObject("aggregations");
	     JSONObject name = ag.getJSONObject("NAME");
	     JSONArray arry = name.getJSONArray("buckets");
	     
	     List<RankInfo> list = new ArrayList<>();
	     
	     for(int i=0,len=arry.size();i<len;i++){
	    	    JSONObject temp=  arry.getJSONObject(i);
	    	    
	    	    //String time = temp.getString("key_as_string");
	    	    Long uuid = temp.getLong("key");
	    	    Long  num = temp.getJSONObject(target).getLong("value");
	    	    int 	isErro=temp.getInteger("doc_count_error_upper_bound");
	    	    /**
	    	     * 打日志   如果isErro为1  出错
	    	     */
	    	    RankInfo rank = new RankInfo();
	    	    rank.setUuid(uuid);
	    	    rank.setChange(num);
	    	    rank.setIsErro(isErro);
	    	    //查数据库封装rank  耦合度很大。
	    	    Record userTemp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), uuid);
	    	    rank.setNickname(userTemp.getStr("nickname"));
	    	    rank.setShowid(userTemp.getLong("showid"));
	    	    rank.setRank(i+1);
	    	   // rank.setUuid(uuid);
	    	    
	     }
	     return list;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		}

}
