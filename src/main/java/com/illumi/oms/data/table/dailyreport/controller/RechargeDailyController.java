package com.illumi.oms.data.table.dailyreport.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.Response;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ExcelTableSheet;
import com.illumi.oms.data.utils.ArithUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.data.utils.ExcelController;
import com.illumi.oms.data.utils.ExcelUtil;
import com.illumi.oms.data.utils.IErrCodeEnum;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/dailyreport/rechargedaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class RechargeDailyController extends ExcelController {

	private static final Logger log = Logger.getLogger(RechargeDailyController.class);

	// 图表
	public void rechargeChart() {
		// 付给成员变量
		Map<Long, Map<String, Long>> map = getResponseMap();
		if(map==null) {
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
		}else {
		
		Chart chart = new Chart();
		List<Long> longList = new ArrayList<Long>();
		// 初始化map
		Map<String, List<Long>> resultMap = initRelustMap();
		for (Entry<Long, Map<String, Long>> e : map.entrySet()) {
			// 1封装日期
			longList.add(e.getKey());
		}
		// 排序
		Collections.sort(longList);
		// 封装日期
		List<String> datelist = new ArrayList<String>();
		for (Long key : longList) {
			datelist.add(DateUtils.getDateFormat4Day().format(key));
			Map<String, Long> valueMap = map.get(key);
			// 1
			//获取channeMap
			Set<String> channeSet = getChannelMap().keySet();

			for (Entry<String, Long> ee : valueMap.entrySet()) {
				String paseKey = paseKey(ee.getKey());
				if (channeSet.contains(paseKey)) {
					List<Long> list = resultMap.get(paseKey);
					list.add(ee.getValue());
					channeSet.remove(paseKey);
				}
			}
			// 2遍历map 赋值0;
			for (String s : channeSet) {
				List<Long> list = resultMap.get(s);
				list.add(0l);
			}
		}

		chart.setCategories(datelist);
		for (Entry<String, List<Long>> e : resultMap.entrySet()) {
			chart.setSeriesDate(e.getKey(), null, e.getValue());
		}
		renderGson(chart);
		}
	}

	// 表格
	public void dataGrid() {
		DataGrid<Map<String,Object>> data = new DataGrid<>();
		List<Map<String,Object>> list = getDataList();
		data.setRows(list);
		renderJson(data);
	}

	//下载
	public void download() {
		Map<String, String> channelMap = getChannelMap();
		channelMap.put("日期", "targetdate");
		channelMap.put("汇总", "sum");
		
		String[]  head = {"日期","苹果充值","步步德扑","九格创想","微信公众号","微信CMS","支付宝大额","支付宝CMS","安卓微信充值","汇总"};
		List<Map<String, Object>> dataList = getDataList();
		//格式化日期
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startDate="";
		String endDate="";
		for(int i=0,n=dataList.size();i<n;i++) {
			
			Map<String, Object> map = dataList.get(i);
			String time = df.format(map.get("targetdate"));
			map.put("targetdate",time);
			if(i==0) {
				startDate=time;
			}
			if(i==dataList.size()-1) {
				endDate=time;
			}
			
		}
		
		
		
		ExcelTableSheet sheet = new ExcelTableSheet("每日充值汇总（"+startDate+" 至 "+endDate+")", head, dataList, "第一页");
		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(channelMap, sheet);

		renderNewExcel(xs, "充值日报("+startDate+" 至 "+endDate+")");
	}

	
	
	

	private String getRequestJson(String stime, String etime) {
		String json = "{\n" + "  \"query\": {\n" + "    \"constant_score\": {\n" + "      \"filter\": {\"range\": {\n"
				+ "        \"@timestamp\": {\n" + "          \"gte\": \"" + stime + "\",\n" + "          \"lte\": \""
				+ etime + "\",\n" + "          \"time_zone\":\"+08:00\"\n" + "\n" + "        }\n" + "      }}\n"
				+ "    }\n" + "  },\n" + "  \"aggs\": {\n" + "    \"NAME\": {\n" + "      \"date_histogram\": {\n"
				+ "        \"field\": \"@timestamp\",\n" + "        \"interval\": \"day\",\n"
				+ "          \"time_zone\":\"+08:00\"\n" + "      }, \"aggs\": {   \n" + "        \"sum\":{\n"
				+ "     \"terms\": {\n" + "       \"field\": \"ChannelId\",\n"
				+ "       \"show_term_doc_count_error\": true,\n" + "       \"shard_size\": 30,\n"
				+ "       \"order\": {\n" + "         \"money_sum\": \"desc\"\n" + "       }\n"
				+ "      },\"aggs\": {\n" + "        \"money_sum\": {\n" + "          \"sum\": {\n"
				+ "            \"field\": \"cach_earn_no\"\n" + "          }\n" + "        }\n" + "      }\n"
				+ "    }}\n" + "    }\n" + "  }\n" + "  \n" + "}";

		return json;
	}

	// 苹果支付 谷歌支付 步步德扑克 九格创想 微信安卓 微信公众号 微信CMS 支付宝大额 支付宝CMS 支付宝公众号
	private String paseKey(String key) {
		switch (key) {
		case "101":
			return "苹果充值";
		case "102":
			return "谷歌支付";
		case "201":
			return "步步德扑";
		case "202":
			return "九格创想";
		case "301":
			return "微信安卓充值";
		case "302":
			return "微信公众号";
		case "303":
			return "微信CMS";
		case "401":
			return "支付宝大额";
		case "402":
			return "支付宝公众号";
		case "403":
			return "支付宝CMS";
		default:
			return null;
		}

	}

	//通过type类型  0 全部  1 国内 2 国外
	private Map<Long, Map<String, Long>> getResponseMap() {
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");
		String type = getPara("type");
		System.out.println("ds:" + dateStart + "||" + "de:" + dateEnd + "||type:" + type);
		Response response = null;
		if(type.equals("0")) {
		  response = ResponseFullData(dateStart,dateEnd);
		}else if(type.equals("1")) {
			
		}else if(type.equals("2")) {
			 response = ResponseAbroadData(dateStart,dateEnd);
			 System.out.println("获取国外信息");
		}
		
		// 解析Response
		Map<Long, Map<String, Long>> map = ELKUtils.paseDailyResponse(response, "money");
		return map;
	}

	
	// type类型为 2  国外请求
	private Response ResponseAbroadData(String dateStart, String dateEnd) {
		Long timeStart = null;
		Long timeEnd = null;
		try {
			timeStart = DateUtils.getDateFormat4Day().parse(dateStart).getTime();
			timeEnd = DateUtils.getDateFormat4Day().parse(dateEnd).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Record> uuids = Db.use(Consts.DB_POKER2).find(SqlKit.sql("data.reportForms.getForeignUUID"));
		
		String jsonString = getRequestAbroadJson(dateStart,dateEnd,uuids);
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String method = "GET";
		String url = ELKUtils.getUrl4SeletedTime(timeStart, timeEnd, urlhead, urlend);
		
		
		System.out.println(url);
		System.out.println(jsonString);
		System.out.println("*************ELK请求************");
		
		Response response = ELKUtils.getData(jsonString, method, url);
		
		
		if (response == null) {
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
		}
		return response;
	}

	private Response ResponseFullData(String dateStart, String dateEnd) {
		Long timeStart = null;
		Long timeEnd = null;
		try {
			timeStart = DateUtils.getDateFormat4Day().parse(dateStart).getTime();
			timeEnd = DateUtils.getDateFormat4Day().parse(dateEnd).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonString = getRequestJson(dateStart, dateEnd);
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String method = "GET";
		String url = ELKUtils.getUrl4SeletedTime(timeStart, timeEnd, urlhead, urlend);
		Response response = ELKUtils.getData(jsonString, method, url);
		if (response == null) {
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
		}
		return response;
	}

	private Map<String, List<Long>> initRelustMap() {
		// 苹果支付 谷歌支付 步步德扑克 九格创想 微信安卓 微信公众号 微信CMS 支付宝大额 支付宝CMS 支付宝公众号
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		Map<String, String> channelMap = getChannelMap();
		for (Entry<String, String> e : channelMap.entrySet()) {
			
				map.put(e.getKey(), new ArrayList<Long>());

		}
		return map;
	}

	private List<Map<String,Object>> getDataList() {
		Map<Long, Map<String, Long>> map = getResponseMap();
		if(map==null) {
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
		}else {
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Long> longList = new ArrayList<Long>();
		for (Entry<Long, Map<String, Long>> e : map.entrySet()) {
			// 1封装日期
			longList.add(e.getKey());
		}
		// 排序
		Collections.sort(longList);
		for (Long key : longList) {

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("targetdate", key);

			// 1获取channle去重
			Map<String, String> channelMap = getChannelMap();
			for (Entry<String, String> chanle : channelMap.entrySet()) {
				m.put(chanle.getValue(), 0);
			}

			for (Entry<String, Long> e : map.get(key).entrySet()) {
				m.put(e.getKey(), ArithUtils.div(e.getValue().doubleValue(),100));
			}

			// 计算总值
			Double sum = 0.0;
			for (Entry<String, Object> sumEntry : m.entrySet()) {
				if (!sumEntry.getKey().equals("targetdate")) {
					Double b = Double.parseDouble(sumEntry.getValue().toString());
					sum = ArithUtils.add(sum, b);
				}
			}
			m.put("sum", sum);
			list.add(m);
		}
		return list;
		}
		return null;
	}

	private Map<String, String> getChannelMap() {
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
	
	
	
	
	private String getRequestAbroadJson(String dateStart,String dateEnd,List<Record> arrayList) {
		String uuids="";
		for(int i =0,n=arrayList.size();i<n;i++) {
			String target = arrayList.get(i).getInt("uuid")+"";
			if(i==n-1) {
				uuids+="\""+target+"\"";
			}else {
				uuids+="\""+target+"\""+",";
			}
		}
		String json="{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + 
				"        {\"constant_score\": {\n" + 
				"          \"filter\": {\n" + 
				"            \"range\": {\n" + 
				"              \"@timestamp\": {\n" + 
				"                \"gte\": \""+dateStart+"\",\n" + 
				"                \"lte\": \""+dateEnd+"\"\n" + 
				"              }\n" + 
				"            }\n" + 
				"          },\n" + 
				"          \"boost\": 1.2\n" + 
				"        }\n" + 
				"          \n" + 
				"        },{\n" + 
				"          \"constant_score\": {\n" + 
				"            \"filter\": {\n" + 
				"              \"terms\": {\n" + 
				"                \"Uuid\": [\n" + 
				"                  "+uuids+"\n" + 
				"                ]\n" + 
				"              }\n" + 
				"            },\n" + 
				"            \"boost\": 1.2\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"NAME\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"@timestamp\",\n" + 
				"        \"interval\": \"day\",\n" + 
				"          \"time_zone\":\"+08:00\"\n" + 
				"      }, \"aggs\": {   \n" + 
				"        \"sum\":{\n" + 
				"     \"terms\": {\n" + 
				"       \"field\": \"action_name\",\n" + 
				"       \"show_term_doc_count_error\": true,\n" + 
				"       \"shard_size\": 30,\n" + 
				"       \"order\": {\n" + 
				"         \"money_sum\": \"desc\"\n" + 
				"       }\n" + 
				"      },\"aggs\": {\n" + 
				"        \"money_sum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"money_change_no\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }}\n" + 
				"    }\n" + 
				"  }\n" + 
				"  \n" + 
				"}";
		return json;
	}


}
