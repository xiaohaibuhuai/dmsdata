package com.illumi.oms.data.monitoring.moneysystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.model.Data;
import com.illumi.oms.stat.controller.ClubController;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@ControllerBind(controllerKey = "/data/monitoring/moneysystem/mttchart", viewPath = UrlConfig.DATA_MONITORING_MONEYSYSTEM)
public class MTTChartController extends EasyuiController<Record> {

	
	public void mttChange() {
		String target="ticket_change";  //钻石变化
		long time = -60*60*1000*24;
		String timeformat ="1h";
		String[] urlHead = { "/ilumi_transactionlog_", "ilumi_payment_" };
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
		   * 日期可能重复
		   */
		DateTimeFormatter dateFormat= DateTimeFormat.forPattern("HH:00");
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

		Chart chart = new Chart();
		List<Long> flog = new ArrayList<>();
		flog.add(2l);
		flog.add(33l);
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
	// private static final Logger log = Logger.getLogger(ClubController.class);
	// /**
	// *
	// * sum(俱乐部总数及活跃俱乐部数)
	// * (这里描述这个方法适用条件 – 可选)
	// * void
	// * @exception
	// */
	// public void sum(){
	// User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
	// log.info(user1.getStr("account")+"/"+user1.getName()+",俱乐部总数及活跃俱乐部数查询(/stat/clubchart/sum)");
	//
	// List<Data> totalList = Data.dao.find(SqlKit.sql("stat.task.getDataList"),
	// Consts.DATA_CLUB_TOTAL,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd"))
	// ;
	// List<Record> activeList
	// =Db.use(Consts.DB_POKER2).findByCache("getActiveClubsByDate",
	// DateTime.now().withMillisOfDay(0).getMillis(),SqlKit.sql("stat.clubchart.getActiveClub"),DateTime.now().minusDays(31).withMillisOfDay(0).getMillis(),DateTime.now().withMillisOfDay(0).getMillis())
	// ;
	//
	// Chart chart = new Chart();
	//
	// List<Integer> totalSeries = new ArrayList<Integer>();
	// List<Long> activeSeries = new ArrayList<Long>();
	//
	// for (Data data : totalList)
	// {
	// chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
	// totalSeries.add(data.getInt("total")/10);
	// }
	//
	// for (Record rd : activeList)
	// {
	// activeSeries.add(rd.getLong("num"));
	// }
	//
	//
	// chart.setSeriesDate("俱乐部总数","spline", totalSeries);
	// chart.setSeriesDate("活跃俱乐部","spline", activeSeries);
	//
	//
	// renderGson(chart);
	// }
	//
	// /**
	// *
	// * inc(俱乐部增量曲线图)
	// * (这里描述这个方法适用条件 – 可选)
	// * void
	// * @exception
	// */
	// public void inc(){
	// User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
	// log.info(user1.getStr("account")+"/"+user1.getName()+",俱乐部增量查询(/stat/clubchart/inc)");
	// List<Data> incList = Data.dao.find(SqlKit.sql("stat.task.getDataList"),
	// Consts.DATA_CLUB_INC,DateTime.now().minusDays(31).toString("yyyy-MM-dd"),DateTime.now().minusDays(1).toString("yyyy-MM-dd"))
	// ;
	//
	//
	// Chart chart = new Chart();
	//
	// List<Integer> incSeries = new ArrayList<Integer>();
	//
	// for (Data data : incList)
	// {
	// chart.categories.add(DateUtil.format(data.getDate("targetDate"),"MM-dd"));
	// incSeries.add(data.getInt("total"));
	// }
	//
	//
	// chart.setSeriesDate("新增俱乐部","spline", incSeries);
	//
	// renderGson(chart);
	// }

}
