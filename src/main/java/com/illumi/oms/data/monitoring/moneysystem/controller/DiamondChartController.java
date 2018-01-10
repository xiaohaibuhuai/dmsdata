package com.illumi.oms.data.monitoring.moneysystem.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.ext.format.DateFormat;
import org.elasticsearch.client.Response;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.model.RankInfo;
import com.illumi.oms.data.model.SnapShot.DiamondSnapShotDate;
import com.illumi.oms.data.utils.ArithUtils;
import com.illumi.oms.data.utils.DataBaseMapperUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.data.utils.LogMapperUtils;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.DataGrid;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@ControllerBind(controllerKey = "/data/monitoring/moneysystem/diamondchart" ,viewPath=UrlConfig.DATA_MONITORING_MONEYSYSTEM)
public class DiamondChartController extends EasyuiController<Record>{



	public void diamondChange() {
		String target="diamone_change";  //钻石变化
		long time = -60*60*1000*24;
		String timeformat ="1h";
		String[] urlHead = { "ilumi_transactionlog_", "ilumi_payment_" };
		List<ChartInfo> chartlistLog = ELKUtils.getchartChangeInfo(urlHead,target, time,timeformat);
		List<ChartInfo> chartlistTask =ELKUtils.getchartChangeInfo("ilumi_task_coinanddiamond_",target, time,timeformat);
		/**
		 * 链接不上会报空指针异常
		 */
		//3 放入数据
		Chart chart = new Chart();
		//categories
		List<String> categories =new ArrayList<>();

		//3.1 来自数据库
		List<Long>  fdata=new ArrayList<>();

		List<Long>  flog=new ArrayList<>();
		/**
		 * 日期可能重复  ||
		 */
		DateTimeFormatter dateFormat= DateTimeFormat.forPattern("HH:00").withZone(DateTimeZone.getDefault());
		for(ChartInfo c:chartlistTask) {
			categories.add(c.getDate().toString(dateFormat));
			fdata.add(c.getNum());
		}
		for(ChartInfo c:chartlistLog) {
			categories.add(c.getDate().toString(dateFormat));
			flog.add(c.getNum());
		}

		chartlistLog=null;
		chart.setCategories(categories);
		chart.setSeriesDate("数据库", null,fdata);
		chart.setSeriesDate("日志", null,flog);
		renderGson(chart);
	}



	public void recharge() {

		Long   nowTime = new Date().getTime();
		Long   startTime = DateUtils.changeHour(nowTime, -24);
		String method = "GET";
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String url = ELKUtils.getIndices(startTime, urlhead, urlend, new SimpleDateFormat("yyyy-MM"));
		String jsonString =getRechargeRequest();

		Response response = ELKUtils.getData(jsonString, method, url);
		Map<Long, Map<String, Long>> paseDailyResponseMap = ELKUtils.paseDailyResponse(response, "money");

		long endtime = new Date().getTime();
		long time = -60*60*1000*24;

		Chart chart = new Chart();
		//封装日期  需要排序
		List<Long> datelist = new ArrayList<Long>();

		for(Entry<Long, Map<String, Long>> e:paseDailyResponseMap.entrySet()) {
			datelist.add(e.getKey());
		}

		//排序
		Collections.sort(datelist);
		//映射map
		Map<String, String> channelMap = DataBaseMapperUtils.getRechargelMap();
		//结果map
		Map<String, List<Double>> resultMap = initRelustMap(channelMap);
		//根据时间顺序封装

		List<String> dateRelust = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");

		for(Long date:datelist) {
			//映射
			if(paseDailyResponseMap.containsKey(date)) {

				Map<String, Long> map = paseDailyResponseMap.get(date);
				for(String key:resultMap.keySet()) {
					List<Double> list = resultMap.get(key);
					double doub = Double.parseDouble(String.valueOf(value).equals("null")?"0":String.valueOf(value));
					if(doub!=0) {
						doub=ArithUtils.div(doub,100);
					}
					list.add(doub);
				}

			}

			dateRelust.add(df.format(date));
		}
		//封装日期
		chart.setCategories(dateRelust);

		//封装数据
		for (Entry<String, List<Double>> e : resultMap.entrySet()) {
			chart.setSeriesDate(e.getKey(), null, e.getValue());
		}
		renderGson(chart);

	}


	//支付买入钻石
	public void increaseDiamond() {
		DataGrid data = new DataGrid();
		String target="diamond_change_no";
		String order="desc";
		long time = -60*60*1000*24;
		String urlMethod = "POST";
		String urlhead = "ilumi_payment_";
//		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, "", target, time, order);
		data.setData(list);
		renderGson(data);
	}
	//钻石减少
	public void reduceDiamond() {
		DataGrid data = new DataGrid();
		String target="diamond_change_no";
		String order="asc";
		long time = -60*60*1000*24;
		String urlMethod = "POST";
		String urlhead = "ilumi_transactionlog_";
//		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, "", target, time, order);
		data.setData(list);
		renderGson(data);
	}
	//充值次数排名
	public void RechargeTimes() {
		DataGrid data = new DataGrid();
		long endtime = new Date().getTime();
		long time = -60*60*1000*24;
		String order="desc";
		long stime = endtime+time;
		String urlMethod = "POST";
		String urlhead = "ilumi_payment_";
//		String urlend = "/_search?request_cache=false";
//		String url = ELKUtils.getIndices(endtime, urlhead, urlend,new SimpleDateFormat("yyyy-MM") );
		List<RankInfo> list =ELKUtils.getRechargeTimesRequest(urlMethod, urlhead, "", time, order);
		//List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, urlend, target, time, order);
//		List<RankInfo> list = ELKUtils.getRankInfo(jsonString, urlMethod, url);
		data.setData(list);
		renderGson(data);
	}
	//充值额度排名
	public void RechargeNum() {
		DataGrid data = new DataGrid();
		String target="cach_earn_no";
		String order="desc";
		long time = -60*60*1000*24;
		String urlMethod = "POST";
		String urlhead = "ilumi_payment_";
//		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, "", target, time, order);
		//除以100
		for(RankInfo rank:list) {
			Object change = rank.getChange();
			Double num = Double.parseDouble(String.valueOf(change).equals("null")?"0":String.valueOf(change));
			Double resultNum = ArithUtils.div(num, 100, 5);
			rank.setChange(resultNum);
		}


		data.setData(list);
		renderGson(data);
	}





	private Map<String, List<Double>> initRelustMap(Map<String,String> channelMap) {
		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		for (Entry<String, String> e : channelMap.entrySet()) {
			//只保留映射项
			if(!e.getKey().equals("日期")&&!e.getKey().equals("汇总")) {
				map.put(e.getKey(), new ArrayList<Double>());
			}

		}
		return map;
	}
	private String getRechargeRequest() {
		String json = "{\n" +
				"  \"query\": {\n" +
				"    \"constant_score\": {\n" +
				"      \"filter\": {\"range\": {\n" +
				"        \"@timestamp\": {\n" +
				"          \"gte\": \"now-24h\",\n" +
				"          \"lte\": \"now\",\n" +
				"          \"time_zone\":\"+08:00\"\n" +
				"\n" +
				"        }\n" +
				"      }}\n" +
				"    }\n" +
				"  },\n" +
				"  \"aggs\": {\n" +
				"    \"NAME\": {\n" +
				"      \"date_histogram\": {\n" +
				"        \"field\": \"@timestamp\",\n" +
				"        \"interval\": \"hour\",\n" +
				"          \"time_zone\":\"+08:00\"\n" +
				"      }, \"aggs\": {   \n" +
				"        \"sum\":{\n" +
				"     \"terms\": {\n" +
				"       \"field\": \"channelid\",\n" +
				"       \"show_term_doc_count_error\": true,\n" +
				"       \"shard_size\": 30,\n" +
				"       \"order\": {\n" +
				"         \"money_sum\": \"desc\"\n" +
				"       }\n" +
				"      },\"aggs\": {\n" +
				"        \"money_sum\": {\n" +
				"          \"sum\": {\n" +
				"            \"field\": \"cach_earn_no\"\n" +
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
