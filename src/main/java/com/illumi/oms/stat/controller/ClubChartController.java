package com.illumi.oms.stat.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.model.Data;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/clubchart" ,viewPath=UrlConfig.STAT)
public class ClubChartController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(ClubController.class);
	/**
	 * 
	 * sum(俱乐部总数及活跃俱乐部数)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 */
	public void sum(){
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",俱乐部总数及活跃俱乐部数查询(/stat/clubchart/sum)");
		
		List<Data> totalList = Data.dao.find(SqlKit.sql("stat.task.getDataList"), Consts.DATA_CLUB_TOTAL,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd")) ;
		List<Record> activeList =Db.use(Consts.DB_POKER2).findByCache("getActiveClubsByDate", DateTime.now().withMillisOfDay(0).getMillis(),SqlKit.sql("stat.clubchart.getActiveClub"),DateTime.now().minusDays(31).withMillisOfDay(0).getMillis(),DateTime.now().withMillisOfDay(0).getMillis()) ;

		Chart chart = new Chart();
		
		List<Integer> totalSeries = new ArrayList<Integer>();
		List<Long> activeSeries = new ArrayList<Long>();

		for (Data data : totalList)
		{
			chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
			totalSeries.add(data.getInt("total")/10);
		}
		
		for (Record rd : activeList)
		{
			activeSeries.add(rd.getLong("num"));
		}


		chart.setSeriesDate("俱乐部总数","spline", totalSeries);
		chart.setSeriesDate("活跃俱乐部","spline", activeSeries);
	 
		
		renderGson(chart);
	}
	
	/**
	 * 
	 * inc(俱乐部增量曲线图)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 */
	public void inc(){
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",俱乐部增量查询(/stat/clubchart/inc)");
		List<Data> incList = Data.dao.find(SqlKit.sql("stat.task.getDataList"), Consts.DATA_CLUB_INC,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd")) ;
	

		Chart chart = new Chart();
		
		List<Integer> incSeries = new ArrayList<Integer>();

		for (Data data : incList)
		{
			chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
			incSeries.add(data.getInt("total"));
		}
		

		chart.setSeriesDate("新增俱乐部","spline", incSeries);

		renderGson(chart);
	}
	

}
