package com.illumi.oms.stat.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.model.Data;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/userchart" ,viewPath=UrlConfig.STAT)
public class UserChartController extends EasyuiController<Record>
{
	/**
	 * 
	 * sum(用户总数及活跃用户)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 */
	public void sum(){
		
		List<Data> totalList = Data.dao.find(SqlKit.sql("stat.task.getDataList"), Consts.DATA_USER_TOTAL,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd")) ;
		List<Data> activeList = Data.dao.find(SqlKit.sql("stat.task.getDataList"), Consts.DATA_USER_ACTIVE,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd")) ;
		

		Chart chart = new Chart();
		
		List<Integer> totalSeries = new ArrayList<Integer>();
		List<Integer> activeSeries = new ArrayList<Integer>();

		for (Data data : totalList)
		{
			chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
			totalSeries.add(data.getInt("total")/10);
		}
		
		for (Data data : activeList)
		{
			activeSeries.add(data.getInt("total"));
		}


		chart.setSeriesDate("用户总数","spline", totalSeries);
		chart.setSeriesDate("活跃用户","spline", activeSeries);
	 
		
		renderGson(chart);
	}
	
	/**
	 * 
	 * inc(用户增量曲线图)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 */
	public void inc(){
		
		List<Data> incList = Data.dao.find(SqlKit.sql("stat.task.getDataList"), Consts.DATA_USER_INC,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd")) ;
	

		Chart chart = new Chart();
		
		List<Integer> incSeries = new ArrayList<Integer>();

		for (Data data : incList)
		{
			chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
			incSeries.add(data.getInt("total"));
		}
		

		chart.setSeriesDate("用户注册数","spline", incSeries);

	 
		
		renderGson(chart);
	}
	

}
