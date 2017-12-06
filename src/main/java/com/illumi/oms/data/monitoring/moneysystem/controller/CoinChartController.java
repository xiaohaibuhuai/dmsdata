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
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
                                 
@ControllerBind(controllerKey = "/data/monitoring/moneysystem/coinchart", viewPath = UrlConfig.DATA_MONITORING_MONEYSYSTEM)
public class CoinChartController extends EasyuiController<Record>{

	private static final Logger log = Logger.getLogger(CoinChartController.class);
	
	public void sum() {
		String target="money_change";
		long time = -60*60*1000*24;
		String timeformat  ="1h";
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
		   * 日期可能重复
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
	
	
	

  /**
   * 增长排名
   */
	public void increaseRank() {
		DataGrid data = new DataGrid();
		String target="money";
		String order="desc";
		long time = -60*60*1000*24;
		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
		data.setData(list);
		renderGson(data);
	}
	
	/**
	 * 减少排名
	 */
	public void reduceRank() {
		DataGrid data = new DataGrid();
		String target="money";
		String order="asc";
		long time = -60*60*1000*24;
		List<RankInfo> list = ELKUtils.getRankInfo(target,time,order);
		data.setData(list);
		renderGson(data);
	}
}
