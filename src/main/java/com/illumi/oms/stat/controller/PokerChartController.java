package com.illumi.oms.stat.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/pokerchart" ,viewPath=UrlConfig.STAT)
public class PokerChartController extends EasyuiController<Record>
{
	
	public void common(){

		Object[] paras = getParas();
		List<Record> total = Db.use("pokerdb2").find(SqlKit.sql("stat.pokerchart.getCommonTotal"),paras);
		List<Record> type = Db.use("pokerdb2").find(SqlKit.sql("stat.pokerchart.getCommonTypeTotal"),paras);
		Chart chart = new Chart();
		List<Long> seriesTotal = new ArrayList<Long>();
		List<Long> seriesGrp = new ArrayList<Long>();
		List<Long> seriesClub = new ArrayList<Long>();
		List<Long> seriesFast = new ArrayList<Long>();
		for (Record rd : total)
		{
			chart.categories.add(DateUtil.format(rd.getDate("date"),"MM-dd"));
			seriesTotal.add(rd.getLong("total"));
		}
		for (Record rd : type)
		{
			seriesGrp.add(rd.getLong("grp"));
			seriesClub.add(rd.getLong("club"));
			seriesFast.add(rd.getLong("fast"));
		}

		chart.setSeriesDate("牌局总数", "spline",seriesTotal);
		chart.setSeriesDate("圈子局", "spline",seriesGrp);
		chart.setSeriesDate("俱乐部局", "spline",seriesClub);
		chart.setSeriesDate("快速局", "spline",seriesFast);
		
		renderGson(chart);
	}
	
	

	
	
	private Object[] getParas(){
		
		DateTime dateStart = DateTime.now().minusDays(30).withMillisOfDay(0);
		DateTime dateEnd = DateTime.now().withMillisOfDay(0);
		Object[] obj = {dateStart.getMillis(),dateEnd.getMillis()};
		
		return obj;
	}

	
	
	

	
}
