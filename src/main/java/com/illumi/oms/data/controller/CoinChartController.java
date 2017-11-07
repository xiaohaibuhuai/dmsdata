package com.illumi.oms.data.controller;


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
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/coinchart", viewPath = UrlConfig.DATA)
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
		List<RankInfo> list = new ArrayList<>();
		list.add(new RankInfo(1, 12l, 123, "23", 12));
		list.add(new RankInfo(2, 1008l, 123, "23", 12));
		list.add(new RankInfo(3, 14, 123, "23", 12));
		list.add(new RankInfo(4, 14, 123, "23", 12));
		list.add(new RankInfo(5, 122, 123, "23", 12));
		list.add(new RankInfo(6, 47, 123, "23", 12));
		list.add(new RankInfo(7, 75, 123, "23", 12));
		list.add(new RankInfo(8, 89, 123, "23", 12));
		list.add(new RankInfo(9, 96, 123, "23", 12));
		list.add(new RankInfo(10, 365, 123, "23", 12));
		data.setData(list);
		renderGson(data);
	}
	
	/**
	 * 减少排名
	 */
	public void reduceRank() {
		DataGrid data = new DataGrid();
		List<RankInfo> list = new ArrayList<>();
		list.add(new RankInfo(1, 11658, 123, "23", 12));
		list.add(new RankInfo(2, 63474, 123, "23", 12));
		list.add(new RankInfo(3, 10028, 123, "23", 12));
		list.add(new RankInfo(4, 73978, 123, "23", 12));
		list.add(new RankInfo(5, 16, 123, "23", 12));
		list.add(new RankInfo(6, 17, 123, "23", 12));
		list.add(new RankInfo(7, 56, 123, "23", 12));
		list.add(new RankInfo(8, 10028, 123, "23", 12));
		list.add(new RankInfo(9, 10086, 123, "23", 12));
		list.add(new RankInfo(10, 30065, 123, "23", 12));
		data.setData(list);
		renderGson(data);
	}
	
	
	




    


	
}
