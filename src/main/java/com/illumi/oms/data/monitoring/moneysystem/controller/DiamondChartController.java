package com.illumi.oms.data.monitoring.moneysystem.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.model.RankInfo;
import com.illumi.oms.data.utils.ArithUtils;
import com.illumi.oms.data.utils.DataBaseMapperUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.DataGrid;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/monitoring/moneysystem/diamondchart" ,viewPath=UrlConfig.DATA_MONITORING_MONEYSYSTEM)
public class DiamondChartController extends EasyuiController<Record>{

	
	
	public void diamondChange() {
		String target="diamone_change";  //钻石变化
		long time = -60*60*1000*24;
		String timeformat ="1h";
		List<ChartInfo> chartlistLog = ELKUtils.getLogChartChangeInfo(target, time,timeformat);
		List<ChartInfo> chartlistTask =ELKUtils.getTaskChartChangeInfo(target, time,timeformat);
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
		  for(ChartInfo c:chartlistTask) {
			  categories.add(c.getDate());
			  fdata.add(c.getNum());
		  } 
		  for(ChartInfo c:chartlistLog) {
			  categories.add(c.getDate());
			  flog.add(c.getNum());
		  } 
		  
		  chartlistLog=null;
		  chart.setCategories(categories);
		  chart.setSeriesDate("数据库", null,fdata);
		  chart.setSeriesDate("日志", null,flog);
		  renderGson(chart);
	}
	
	
	
	public void recharge() {
		
		Chart chart = new Chart();
		
		
		
		List<Long>  flog=new ArrayList<>();
		flog.add(-2l);
		flog.add(33l);
		flog.add(23l);
		flog.add(23l);
		flog.add(1l);
		flog.add(62l);
		flog.add(56l);
		
		List<Long>  flog2=new ArrayList<>();
		flog2.add(12l);
		flog2.add(-43l);
		flog2.add(13l);
		flog2.add(33l);
		flog2.add(22l);
		flog2.add(12l);
		flog2.add(16l);
		
		
		List<Long>  flog3=new ArrayList<>();
		flog3.add(111l);
		flog3.add(22l);
		flog3.add(66l);
		flog3.add(-20l);
		flog3.add(12l);
		flog3.add(80l);
		flog3.add(13l);
		
		
		//苹果, 步步, 九格, 微信公众号, 微信CMS, 大额, 支付宝公众号, 支付宝CMS, 安卓微信

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
	
	
	//支付买入钻石
	public void increaseDiamond() {
		DataGrid data = new DataGrid();
		String target="diamond_change_no";
		String order="desc";
		long time = -60*60*1000*24;
		String urlMethod = "POST";
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfoTemp(urlMethod, urlhead, urlend, target, time, order);
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
		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, urlend, target, time, order);
		data.setData(list);
		renderGson(data);
	}
	//充值次数排名
	public void RechargeTimes() {
		DataGrid data = new DataGrid();
		long endtime = new Date().getTime();
		long time = -60*60*1000*24;
		long stime = endtime+time;
		String urlMethod = "POST";
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?request_cache=false";
		String url = ELKUtils.getUrl(endtime, urlhead, urlend,new SimpleDateFormat("yyyy-MM") );
		String jsonString=getRechargeTimesRequest(stime,endtime);
		//List<RankInfo> list = ELKUtils.getRankInfo(urlMethod, urlhead, urlend, target, time, order);
		List<RankInfo> list = ELKUtils.getRankInfo(jsonString, urlMethod, url);
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
		String urlend = "/_search?request_cache=false";
		List<RankInfo> list = ELKUtils.getRankInfoTemp(urlMethod, urlhead, urlend, target, time, order);
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
	
	
	
	
	private String getRechargeTimesRequest(long stime, long endtime) {
		String json="{\n" + 
				"  \"query\": {\n" + 
				"    \"constant_score\": {\n" + 
				"      \"filter\": {\"range\": {\n" + 
				"        \"@timestamp\": {\n" + 
				"          \"gte\": \""+stime+"\",\n" + 
				"          \"lte\": \""+endtime+"\"\n" + 
				"        }\n" + 
				"      }}\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"aggs\":{\n" + 
				"    \"sum\":{\n" + 
				"     \"terms\": {\n" + 
				"       \"field\": \"Uuid\",\n" + 
				"       \"show_term_doc_count_error\": true,\n" + 
				"       \"shard_size\": 100000,\n" + 
				"       \"order\": {\n" + 
				"         \"money_sum\": \"desc\"\n" + 
				"       }\n" + 
				"      },\"aggs\": {\n" + 
				"        \"money_sum\": {\n" + 
				"          \"value_count\": {\n" + 
				"            \"field\": \"Uuid\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		return json;
	}
}
