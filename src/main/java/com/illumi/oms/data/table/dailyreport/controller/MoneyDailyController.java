package com.illumi.oms.data.table.dailyreport.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ExcelTableSheet;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.data.utils.ExcelController;
import com.illumi.oms.data.utils.ExcelUtil;
import com.illumi.oms.data.utils.IErrCodeEnum;
import com.illumi.oms.system.model.Chart;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/data/table/dailyreport/moneydaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class MoneyDailyController extends ExcelController {

	private static final Logger log = Logger.getLogger(MoneyDailyController.class);

	/*
	 * @params : dateStrat/dateEnd: yyyy-MM-dd type: 0 全部用户 1国内用户 2海外用户
	 * 
	 * @return
	 */
	public void money() {
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");
		String type = getPara("type");

		System.out.println("ds:" + dateStart + "||" + "de:" + dateEnd + "||type:" + type);
		// 根据类型找到用户uuid
		// ID_TYPE_FACEBOOK
		// select uuid from t_user_baseinfo where idtype = "ID_TYPE_FACEBOOK" or  countrycode != 86;
		// 1找到全部用户
		String jsonString = getRequestJson(dateStart, dateEnd);
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String url = null;
		String method = "GET";
		Response response = null;
		try {
	     url = ELKUtils.getUrl4SeletedTime(DateUtils.getDateFormat4Day().parse(dateStart).getTime(), DateUtils.getDateFormat4Day().parse(dateEnd).getTime(), urlhead, urlend);

		 response = ELKUtils.getData(jsonString, method, url);
		}catch (Exception e) {
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
			e.printStackTrace();
		}
		
		try {
			url = ELKUtils.getUrl4SeletedTime(DateUtils.getDateFormat4Day().parse(dateStart).getTime(),
					DateUtils.getDateFormat4Day().parse(dateEnd).getTime(), urlhead, urlend);
			Map<Object, Long> map = paseResponse(response, "money");
			for(Entry<Object, Long> e :map.entrySet()) {
				Object key = e.getKey();
			  //  String paseKey = paseKey(key);
				Object value = e.getValue();
				
				//System.out.println("KEY:"+paseKey+"|| value"+value);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		Chart chart = new Chart();
		List<Long> flog = new ArrayList<>();
		flog.add(2l);
		flog.add(-33l);
		flog.add(23l);
		flog.add(23l);
		flog.add(1l);
		flog.add(62l);
		flog.add(56l);

		List<Long> flog2 = new ArrayList<>();
		flog2.add(12l);
		flog2.add(43l);
		flog2.add(13l);
		flog2.add(33l);
		flog2.add(22l);
		flog2.add(12l);
		flog2.add(16l);

		List<Long> flog3 = new ArrayList<>();
		flog3.add(111l);
		flog3.add(22l);
		flog3.add(66l);
		flog3.add(20l);
		flog3.add(12l);
		flog3.add(80l);
		flog3.add(13l);

		// 苹果, 步步, 九格, 微信公众号, 微信CMS, 大额, 支付宝公众号, 支付宝CMS, 安卓微信

		chart.setSeriesDate("苹果", null, flog);
		chart.setSeriesDate("步步", null, flog2);
		chart.setSeriesDate("九格", null, flog3);
		chart.setSeriesDate("微信公众号", null, flog3);
		chart.setSeriesDate("微信CMS", null, flog3);
		chart.setSeriesDate("大额", null, flog3);
		chart.setSeriesDate("支付宝公众号", null, flog3);
		chart.setSeriesDate("支付宝CMS", null, flog3);
		chart.setSeriesDate("安卓微信", null, flog3);

		List<String> list = new ArrayList<String>();
		list.add("1：00");
		list.add("2：00");
		list.add("3：00");
		list.add("4：00");
		list.add("5：00");
		list.add("6：00");
		list.add("7：00");
		list.add("8：00");

		chart.setCategories(list);

		renderGson(chart);
	}

	
	private Map<Object, Long> paseResponse(Response response, String target) {
		try {
			String jsonstring = EntityUtils.toString(response.getEntity());
			System.out.println(jsonstring);
			JSONObject jsonObj = JSON.parseObject(jsonstring);
			JSONObject ag = jsonObj.getJSONObject("aggregations");
			JSONObject sum = ag.getJSONObject("sum");
			JSONArray arry = sum.getJSONArray("buckets");
			Map<Object, Long> map = new HashMap<Object, Long>();

			for (int i = 0, len = arry.size(); i < len; i++) {
				JSONObject temp = arry.getJSONObject(i);

				// String time = temp.getString("key_as_string");
				Long key = temp.getLong("key");
				Long value = temp.getJSONObject(target + "_sum").getLong("value");
				int isErro = temp.getInteger("doc_count_error_upper_bound");
				if (isErro == 0) {
					log.error("key:" + key + "|| value:" + value + "||" + isErro);
				}
				/**
				 * 打日志 如果isErro为1 出错
				 */
				map.put(key, value);
			}
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String getRequestJson(String dateStart, String dateEnd) {
		String json = "{\n" + 
				"  \"query\": {\n" + 
				"    \"constant_score\": {\n" + 
				"      \"filter\": {\"range\": {\n" + 
				"        \"@timestamp\": {\n" + 
				"          \"gte\": \"now-24h\",\n" + 
				"          \"lte\": \"now\"\n" + 
				"        }\n" + 
				"      }}\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"aggs\":{\n" + 
				"    \"sum\":{\n" + 
				"     \"terms\": {\n" + 
				"       \"field\": \"ChannelId\",\n" + 
				"       \"show_term_doc_count_error\": true,\n" + 
				"       \"shard_size\": 30,\n" + 
				"       \"order\": {\n" + 
				"         \"money_sum\": \"desc\"\n" + 
				"       }\n" + 
				"      },\"aggs\": {\n" + 
				"        \"money_sum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"diamond_change_no\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"  \n" + 
				"}";
		return json;
	}

	public void diamond() {
		Chart chart = new Chart();
		List<Long> flog = new ArrayList<>();
		flog.add(2l);
		flog.add(-33l);
		flog.add(23l);
		flog.add(23l);
		flog.add(1l);
		flog.add(62l);
		flog.add(56l);

		List<Long> flog2 = new ArrayList<>();
		flog2.add(12l);
		flog2.add(43l);
		flog2.add(13l);
		flog2.add(33l);
		flog2.add(22l);
		flog2.add(12l);
		flog2.add(16l);

		List<Long> flog3 = new ArrayList<>();
		flog3.add(111l);
		flog3.add(22l);
		flog3.add(66l);
		flog3.add(20l);
		flog3.add(12l);
		flog3.add(80l);
		flog3.add(13l);

		// 苹果, 步步, 九格, 微信公众号, 微信CMS, 大额, 支付宝公众号, 支付宝CMS, 安卓微信

		chart.setSeriesDate("苹果", null, flog);
		chart.setSeriesDate("步步", null, flog2);
		chart.setSeriesDate("九格", null, flog3);
		chart.setSeriesDate("微信公众号", null, flog3);
		chart.setSeriesDate("微信CMS", null, flog3);
		chart.setSeriesDate("大额", null, flog3);
		chart.setSeriesDate("支付宝公众号", null, flog3);
		chart.setSeriesDate("支付宝CMS", null, flog3);
		chart.setSeriesDate("安卓微信", null, flog3);

		List<String> list = new ArrayList<String>();
		list.add("1：00");
		list.add("2：00");
		list.add("3：00");
		list.add("4：00");
		list.add("5：00");
		list.add("6：00");
		list.add("7：00");
		list.add("8：00");

		chart.setCategories(list);

		renderGson(chart);

	}

	public void download() {

		String[] head = { "日期", "服务费", "互动道具", "魔法表情", "翻翻看", "弹幕", "扑克机", "加勒比", "牛牛—一粒大米", "八八碰—一粒大米", "打赏牌谱",
				"德扑币报名MTT", "汇总" };
		String[] head1 = { "日期", "联盟局", "修改昵称", "延时道具", "MTT门票", "俱乐部推送", "俱乐部改名", "汇总" };

		String dateStart = getPara("h_dateStart");
		String dateEnd = getPara("h_dateEnd");
		String type = getPara("h_type");

//		System.out.println(dateStart + "||" + dateEnd + "||" + type);
//
//		List<Map<String, Object>> list = getList();
//		List<Map<String, Object>> list2 = getList2();
//
//		ExcelTableSheet sheet = new ExcelTableSheet("每日德扑币消耗", head, list, "第一页");
//		ExcelTableSheet sheet2 = new ExcelTableSheet("每日钻石消耗汇总", head1, list, "第二页");
//
//		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(sheet, sheet2);
//
//		renderNewExcel(xs, "统计表");
	}

	private List<Map<String, Object>> getList2() {
		String[] head = { "日期", "联盟局", "修改昵称", "延时道具", "MTT门票", "俱乐部推送", "俱乐部改名", "汇总" };
		List<Map<String, String>> table = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put(head[0], "1999-08-25");
		map.put(head[1], "12");
		map.put(head[2], "333");
		map.put(head[3], "6623");
		map.put(head[4], "4534");
		map.put(head[5], "987");
		map.put(head[6], "112");
		map.put(head[7], "233");
		Map<String, String> map1 = new HashMap<String, String>();
		map.put(head[0], "1999-08-25");
		map.put(head[1], "12");
		map.put(head[2], "333");
		map.put(head[3], "6623");
		map.put(head[4], "4534");
		map.put(head[5], "987");
		map.put(head[6], "112");
		map.put(head[7], "233");

		table.add(map);
		table.add(map1);
		return null;
	}

//	private List<Map<String, Object>> getList() {
//		String[] head = { "日期", "服务费", "互动道具", "魔法表情", "翻翻看", "弹幕", "扑克机", "加勒比", "牛牛—一粒大米", "八八碰—一粒大米", "打赏牌谱",
//				"德扑币报名MTT", "汇总" };
//		List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
//		Map<String, String> map = new HashMap<String, String>();
//		map.put(head[0], "2017-10-1");
//		map.put(head[1], "111");
//		map.put(head[2], "222");
//		map.put(head[3], "1232");
//		map.put(head[4], "1323244");
//		map.put(head[5], "4545");
//		map.put(head[6], "6666");
//		map.put(head[7], "3356753");
//		map.put(head[8], "3352223");
//		map.put(head[9], "333");
//		map.put(head[10], "334533");
//		map.put(head[11], "335213");
//		map.put(head[12], "3333");
//
//		Map<String, String> map2 = new HashMap<String, String>();
//
//		map2.put(head[0], "2017-10-1");
//		map2.put(head[1], "111");
//		map2.put(head[2], "222");
//		map2.put(head[3], "1232");
//		map2.put(head[4], "1323244");
//		map2.put(head[5], "4545");
//		map2.put(head[6], "6666");
//		map2.put(head[7], "3356753");
//		map2.put(head[8], "3352223");
//		map2.put(head[9], "333");
//		map2.put(head[10], "334533");
//		map2.put(head[11], "335213");
//		map2.put(head[12], "3333");
//
//		Map<String, String> map1 = new HashMap<String, String>();
//
//		map1.put(head[0], "2017-10-1");
//		map1.put(head[1], "111");
//		map1.put(head[2], "222");
//		map1.put(head[3], "1232");
//		map1.put(head[4], "1323244");
//		map1.put(head[5], "4545");
//		map1.put(head[6], "6666");
//		map1.put(head[7], "3356753");
//		map1.put(head[8], "3352223");
//		map1.put(head[9], "333");
//		map1.put(head[10], "334533");
//		map1.put(head[11], "335213");
//		map1.put(head[12], "3333");
//
//		Map<String, String> map3 = new HashMap<String, String>();
//
//		map3.put(head[0], "2017-10-1");
//		map3.put(head[1], "111");
//		map3.put(head[2], "222");
//		map3.put(head[3], "1232");
//		map3.put(head[4], "1323244");
//		map3.put(head[5], "4545");
//		map3.put(head[6], "6666");
//		map3.put(head[7], "3356753");
//		map3.put(head[8], "3352223");
//		map3.put(head[9], "333");
//		map3.put(head[10], "334533");
//		map3.put(head[11], "335213");
//		map3.put(head[12], "3333");
//
//		Map<String, String> map4 = new HashMap<String, String>();
//
//		map4.put(head[1], "111");
//		map4.put(head[2], "222");
//		map4.put(head[3], "1232");
//		map4.put(head[4], "1323244");
//		map4.put(head[5], "4545");
//		map4.put(head[6], "6666");
//		map4.put(head[7], "3356753");
//		map4.put(head[8], "3352223");
//		map4.put(head[9], "333");
//		map4.put(head[10], "334533");
//		map4.put(head[11], "335213");
//
//		Map<String, String> map5 = new HashMap<String, String>();
//
//		map5.put(head[0], "2017-10-1");
//		map5.put(head[1], "111");
//		map5.put(head[2], "222");
//
//		map5.put(head[4], "1323244");
//		map5.put(head[5], "4545");
//
//		map5.put(head[7], "3356753");
//		map5.put(head[8], "3352223");
//
//		map5.put(head[10], "334533");
//		map5.put(head[11], "335213");
//		map5.put(head[12], "3333");
//
//		table1.add(map);
//		table1.add(map2);
//		table1.add(map3);
//		table1.add(map4);
//		table1.add(map5);
//
//		return table1;
//	}
//	
	
	


}
