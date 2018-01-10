package com.illumi.oms.data.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ELKUtils {

	
	private static final Logger log = Logger.getLogger(ELKUtils.class);
	
	
	public static void main(String[] args) {
	
		String[] urlhead = { "ilumi_transactionlog_","ilumi_minigame_","iii_ddd_bbb_"};
		String urlend = "/_search?size=30";
		
		String d = getUrl4SeletedTime(1504381500000l,1511811900000l, urlhead, urlend);
		
		System.out.println(d);
	}

	public static List<ChartInfo> getTaskChartChangeInfo(String target, long time, String timeformat) {
		long nowTime = new Date().getTime();
		// long startTime = DateUtils.changeHour(nowTime, hourtime);
		long startTime = nowTime + time;
		String urlMethod = "GET";
		String urlheadTask = "/ilumi_task_coinanddiamond_";
		String urlend = "/_search";
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String urlTask = getUrl(nowTime, urlheadTask, urlend, df);
		String jsonString = "{\n" + "  \"query\": {\n" + "\"constant_score\": {\n" + "\"filter\": {\"range\": {\n"
				+ "\"@timestamp\": {\n" + "\"gte\": " + startTime + ",\n" + "\"lte\": " + nowTime + "\n" + "        }\n"
				+ "      }}\n" + "    }\n" + "  }, \"aggs\": {\n" + "    \"NAME\": {\n"
				+ "      \"date_histogram\": {\n" + "        \"format\": \"yyyy-MM-dd HH:mm:ss\", \n"
				+ "        \"field\": \"@timestamp\",\n" + "        \"interval\": \"" + timeformat + "\"\n"
				+ "      }, \"aggs\": {\n" + "        \"money_change\": {\n" + "          \"sum\": {\n"
				+ "            \"field\": \"money_change_no\"\n" + "          }\n" + "        },\n"
				+ "        \"diamone_change\":{\n" + "          \"sum\": {\n"
				+ "            \"field\": \"diamond_change_no\"\n" + "          }\n" + "        },\n"
				+ "        \"ticket_change\":{\n" + "          \"sum\": {\n"
				+ "            \"field\": \"ticket_change_no\"\n" + "          }\n" + "        }\n" + "      }\n"
				+ "    }\n" + "  }\n" + "}";
		return getChartChangeInfo(jsonString, urlMethod, urlTask, target);

	}

	public static List<ChartInfo> getLogChartChangeInfo(String target, long time, String timeformat) {
		long nowTime = new Date().getTime();
		long startTime = nowTime + time;
		String urlMethod = "GET";
		// String urlheadLog="/ilumi_transctionlog_";

		String[] urlheadLog = { "/ilumi_transactionlog_", "ilumi_payment_" };
		String urlend = "/_search";
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String urlLog = getUrl(nowTime, urlheadLog, urlend, df);

		String jsonString = "{\n" + "  \"query\": {\n" + "    \"constant_score\": {\n"
				+ "      \"filter\": {\"range\": {\n" + "        \"@timestamp\": {\n" + "          \"gte\": "
				+ startTime + ",\n" + "          \"lte\": " + nowTime + "\n" + "        }\n" + "      }}\n" + "    }\n"
				+ "  }, \"aggs\": {\n" + "    \"NAME\": {\n" + "      \"date_histogram\": {\n"
				+ "        \"format\": \"yyyy-MM-dd HH:mm:ss\", \n" + "        \"field\": \"@timestamp\",\n"
				+ "        \"interval\": \"" + timeformat + "\"\n" + "      }, \"aggs\": {\n"
				+ "        \"money_change\": {\n" + "          \"sum\": {\n"
				+ "            \"field\": \"money_change_no\"\n" + "          }\n" + "        },\n"
				+ "        \"diamone_change\":{\n" + "          \"sum\": {\n"
				+ "            \"field\": \"diamond_change_no\"\n" + "          }\n" + "        },\n"
				+ "        \"ticket_change\":{\n" + "          \"sum\": {\n"
				+ "            \"field\": \"ticket_change_no\"\n" + "          }\n" + "        }\n" + "      }\n"
				+ "    }\n" + "  }\n" + "}";
		return getChartChangeInfo(jsonString, urlMethod, urlLog, target);

	}

	public static List<ChartInfo> getChartChangeInfo(String jsonString, String urlMethod, String url, String target) {
		try {
			Response response = ELKUtils.getData(jsonString, urlMethod, url);
			List<ChartInfo> list = ELKUtils.paseChartJson(response, target);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static List<RankInfo> getRankInfo(String urlMethod,String urlhead,String urlend ,String target, long time, String order) {
		long nowTime = new Date().getTime();
		long startTime = nowTime + time;
		String url = getUrl(startTime, urlhead, urlend, new SimpleDateFormat("yyyy-MM"));
		String jsonString = "{\n" + "  \"query\": {\n" + "    \"constant_score\": {\n"
				+ "      \"filter\": {\"range\": {\n" + "        \"@timestamp\": {\n" + "          \"gte\": \""
				+ startTime + "\",\n" + "          \"lte\": \"" + nowTime + "\"\n" + "        }\n" + "      }}\n"
				+ "    }\n" + "  },\n" + "  \"aggs\":{\n" + "    \"sum\":{\n" + "     \"terms\": {\n"
				+ "       \"field\": \"uuid\",\n" + "       \"show_term_doc_count_error\": true,\n"
				+ "       \"shard_size\": 100000,\n" + "       \"order\": {\n" + "         \"money_sum\": \"" + order
				+ "\"\n" + "       }\n" + "      },\"aggs\": {\n" + "        \"money_sum\": {\n"
				+ "          \"sum\": {\n" + "            \"field\": \"" + target + "\"\n" + "          }\n"
				+ "        }\n" + "      }\n" + "    }\n" + "  }\n" + "}";

		return getRankInfo(jsonString, urlMethod, url);
	}

	// 重载
	public static List<RankInfo> getRankInfo(String jsonString, String urlMethod, String url) {
		try {
			Response response = ELKUtils.getData(jsonString, urlMethod, url);
			List<RankInfo> list = ELKUtils.paseRankJson(response);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 链接ELK
	 * 
	 * @param jsonString
	 * @param method
	 * @param url
	 * @return
	 * @throws IOException
	 *             是否该抛异常
	 */
	public static Response getData(String jsonString, String method, String url) {
		try {
			HttpHost httpHost = new HttpHost("10.105.92.212", 9200, "http");
			RestClient restClient = RestClient.builder(httpHost).build();
			Map<String, String> params = Collections.singletonMap("pretty", "true");
			HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
			Response response = restClient.performRequest(method, url, params, entity);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解析url 根据所选时间范围 （月份）
	 * 
	 * @param startTime
	 * @param endTime
	 * @param urlhead
	 * @param urlend
	 * @return
	 */
	public static String getUrl4SeletedTime(long startTime, long endTime, String urlhead, String urlend) {
		String url ="";
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String stime = df.format(startTime);
		String etime = df.format(endTime);

		// 1 同一月份
		if (stime.equals(etime)) {
			url = urlhead + stime + urlend;
			return url;
		} else {
			// 判断相差的日期
			try {
				int count = DateUtils.countMonths(stime, etime, "yyyy-MM");
				String[] urldate = new String[count + 1];

				for (int i = 0; i <= count; i++) {
					// 开始时间改变月份
					urldate[i] = stime;
					stime = df.format(DateUtils.changeMonth(df.parse(stime).getTime(), 1));
				}
				// 拼装url
				for (int n = 0; n < urldate.length; n++) {
					if (n == urldate.length - 1) {
						// 结尾
						url += urlhead + urldate[n] + urlend;
					} else {
						url += urlhead + urldate[n] +",";
					}
				}
				return url;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String getUrl4SeletedTime(long startTime, long endTime, String[] urlhead, String urlend) {
		
		String url ="";
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String stime = df.format(startTime);
		String etime = df.format(endTime);
		
		if (stime.equals(etime)) {
			String temp="";
			for(int i = 0;i < urlhead.length ; i++) {
				if(i==urlhead.length-1) {
					temp+=urlhead[i]+stime;
				}else {
					temp+=urlhead[i]+stime+",";
				}
				url=temp+urlend;
				return url;
			}	
		}else {
			// 判断相差的日期
			try {
				int count = DateUtils.countMonths(stime, etime, "yyyy-MM");
				String[] urldate = new String[count + 1];

				for (int i = 0; i <= count; i++) {
					// 开始时间改变月份
					urldate[i] = stime;
					stime = df.format(DateUtils.changeMonth(df.parse(stime).getTime(), 1));
				}
				// 拼装url
				String temp="";
				boolean flag=false;
				for(int i=0;i<urlhead.length;i++) {
					
					if(i==urlhead.length-1) {
						flag=true;
					}
					for (int n = 0; n < urldate.length; n++) {
						
						if(flag) {
							if(n==urldate.length-1) {
								temp+=urlhead[i]+urldate[n];
							}else {
								temp+=urlhead[i]+urldate[n]+",";
							}
						}else {
							temp+=urlhead[i]+urldate[n]+",";
						}
					}
					url=temp+urlend;
					
				}
				return url;
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
		return null;
	}
	/**
	 * 获取解析后的url 天数为两天 月份可能为两月
	 * 
	 * @param startTime
	 * @return 年月日
	 */
	public static String getUrl(long startTime, String urlhead, String urlend, DateFormat df) {
		String url = null;
		String[] urlarr = getUrlDate(startTime, df);
		if (urlarr[1] != null) {
			url = urlhead + urlarr[0] + "," + urlhead + urlarr[1] + urlend;
		} else {
			url = urlhead + urlarr[0] + urlend;
		}
		return url;
	}

	/**
	 * 重载 多个url库
	 * 
	 * @param startTime
	 * @param urlhead
	 * @param urlend
	 * @return
	 */
	public static String getUrl(long startTime, String[] urlheads, String urlend, DateFormat dateformat) {
		String url = "";

		for (int i = 0; i < urlheads.length; i++) {
			String[] urlarr = getUrlDate(startTime, dateformat);
			if (urlarr[1] != null) {
				// 判断结尾
				url += urlheads[i] + urlarr[0] + "," + urlheads[i] + urlarr[1];
				if (i != urlheads.length - 1) {
					url += ",";
				}
			} else {
				// 判断结尾
				url += urlheads[i] + urlarr[0];
				if (i != urlheads.length - 1) {
					url += ",";
				}
			}
		}
		url += urlend;
		return url;
	}

	/**
	 * 会返回三天的日期
	 * 
	 * @param startTime
	 * @param urlheads
	 * @param urlend
	 * @return
	 */
	public static String getUrlThreeDay(long startTime, String urlheads, String urlend) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDay = df.format(startTime);
		String lastDay = df.format(DateUtils.changeHour(startTime, -24));
		String nextDay = df.format(DateUtils.changeHour(startTime, +24));
		String url = "";
		url += urlheads + lastDay + "," + urlheads + nowDay + "," + urlheads + nextDay + urlend;
		return url;
	}

	/**
	 * 优化查询 判断上一天是否是上一个月 日期为天直接返回上一天
	 * 
	 * @param startTime
	 * @return 年月日
	 */
	private static String[] getUrlDate(long startTime, DateFormat df) {
		String end = df.format(new Date(startTime));
		String start = df.format(new Date(DateUtils.changeHour(startTime, -24)));
		String[] result = new String[2];

		if (!end.equals(start)) {
			result[0] = start;
			result[1] = end;
			return result;
		}
		result[0] = end;
		return result;
	}

	/**
	 * 解析图表Json
	 * 
	 * @param response
	 * @return CoinChartInfo
	 * 
	 *         解析： aggregations获取 arr
	 */
	private static List<ChartInfo> paseChartJson(Response response, String target) {
		try {
			String jsonstring = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = JSON.parseObject(jsonstring);
			JSONObject ag = jsonObj.getJSONObject("aggregations");
			JSONObject name = ag.getJSONObject("NAME");
			JSONArray arry = name.getJSONArray("buckets");

			List<ChartInfo> list = new ArrayList<>();

			for (int i = 0, len = arry.size(); i < len; i++) {
				JSONObject temp = arry.getJSONObject(i);

				// String time = temp.getString("key_as_string");
				Long timenum = temp.getLong("key");
				Long num = temp.getJSONObject(target).getLong("value");

				String time = new SimpleDateFormat("HH").format(new Date(timenum)) + ":00";
				// String time = new SimpleDateFormat("HH:mm").format(new Date(timenum));
				list.add(new ChartInfo(time, num));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析日期范围类 数据     
	 * @param response
	 * @param target
	 * @return
	 */
	
	public static Map<Long,Map<String, Long>> paseDailyResponse(Response response, String target) {
		try {
			String jsonstring = EntityUtils.toString(response.getEntity());
//			System.out.println("***************Response Json***************");
			//System.out.println(jsonstring);
			
			JSONObject jsonObj = JSON.parseObject(jsonstring);
			JSONObject ag = jsonObj.getJSONObject("aggregations");
			JSONObject na = ag.getJSONObject("NAME");
			JSONArray arry = na.getJSONArray("buckets");
			
			Map<Long,Map<String, Long>> mmap = new HashMap<Long,Map<String, Long>>();
			for (int i = 0, len = arry.size(); i < len; i++) {
				JSONObject temp = arry.getJSONObject(i);
				//日期
				Long time = temp.getLong("key");
				JSONObject sum = temp.getJSONObject("sum");
				JSONArray arr = sum.getJSONArray("buckets");
				
				Map<String, Long> map = new HashMap<String, Long>();
				for(int j = 0, length = arr.size(); j < length; j++) {
					JSONObject te = arr.getJSONObject(j);
					// String time = temp.getString("key_as_string");
					String key = te.getString("key");
					Long value = te.getJSONObject(target + "_sum").getLong("value");
					int isErro = te.getInteger("doc_count_error_upper_bound");
					if (isErro == 1) {
						log.error("key:" + key + "|| value:" + value + "||" + isErro);
					}
					/**
					 * 打日志 如果isErro为1 出错
					 */
					map.put(key, value);
	
				}
				mmap.put(time, map);
	
			}
			return mmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析排名Json 解析：aggregations
	private static List<RankInfo> paseRankJson(Response response) {
		try {
			String jsonstring = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = JSON.parseObject(jsonstring);
			JSONObject ag = jsonObj.getJSONObject("aggregations");
			JSONObject sum = ag.getJSONObject("sum");
			JSONArray arry = sum.getJSONArray("buckets");

			List<RankInfo> list = new ArrayList<>();

			for (int i = 0, len = arry.size(); i < len; i++) {
				JSONObject temp = arry.getJSONObject(i);

				// String time = temp.getString("key_as_string");
				Long uuid = temp.getLong("key");
				//Long num = temp.getJSONObject(target + "_sum").getLong("value");
				Long num = temp.getJSONObject( "money_sum").getLong("value");
				int isErro = temp.getInteger("doc_count_error_upper_bound");
				/**
				 * 打日志 如果isErro为1 出错
				 */
				RankInfo rank = new RankInfo();
				rank.setUuid(uuid);
				rank.setChange(num);
				rank.setIsErro(isErro);

				// 查数据库再封装rank
				Record userTemp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), uuid);
				rank.setRank(i + 1);
				if (userTemp != null) {
					rank.setNickname(userTemp.getStr("nickname"));
					rank.setShowid(userTemp.getStr("showid"));
				}
				list.add(rank);

			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
}
