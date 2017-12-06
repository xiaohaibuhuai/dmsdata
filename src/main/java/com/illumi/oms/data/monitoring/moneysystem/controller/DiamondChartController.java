package com.illumi.oms.data.monitoring.moneysystem.controller;

import java.util.ArrayList;
import java.util.List;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.model.RankInfo;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.DataGrid;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
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
	
	
	public void increaseDiamond() {
		DataGrid data = new DataGrid();
		String target="diamond";
		String order="desc";
		long time = -60*60*1000*24;
		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
		data.setData(list);
		renderGson(data);
	}
	public void reduceDiamond() {
		DataGrid data = new DataGrid();
		String target="diamond";
		String order="asc";
		long time = -60*60*1000*24;
		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
		data.setData(list);
		renderGson(data);
	}
//	public void increaseRecharge() {
//		DataGrid data = new DataGrid();
//		String target="";
//		String order="desc";
//		long time = -60*60*1000*24;
//		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
//		data.setData(list);
//		renderGson(data);
//	}
//     public void reduceRecharge() {
//    	 DataGrid data = new DataGrid();
// 		String target="money";
// 		String order="asc";
// 		long time = -60*60*1000*24;
// 		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
// 		data.setData(list);
// 		renderGson(data);
//	}
}
