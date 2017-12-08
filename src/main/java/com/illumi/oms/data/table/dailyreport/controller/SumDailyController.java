package com.illumi.oms.data.table.dailyreport.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.ArithUtils;
import com.illumi.oms.data.utils.DataBaseMapperUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/dailyreport/sumdaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class SumDailyController extends EasyuiController<Record> {

	public void chart() {
//		// 各种数据
        List<Chart> list = new ArrayList<Chart>();
//		// 1 钻石总增加
		   Chart addDiamond = getAddDiamond();
		   list.add(addDiamond);
//		// 2 钻石总消耗
		   Chart consumeDiamond =getConsumeDiamond();
		   list.add(consumeDiamond);
//		// 3 德扑币总增加
		   Chart addMoney =getAddMoney();
		   list.add(addMoney);
//		// 4 德扑币总消耗
		   Chart consumeMoney = getConsumeMoney();
		   list.add(consumeMoney);
		   
		// 牌局日报  
		// 5 昨日开局数
		     Chart gamStartChart = getGameStartChart();
		     list.add(gamStartChart);
		// 6 总活跃数
		     Chart playerChart = getPlayerCountChart();
		     list.add(playerChart);
		// 7  昨日服务费 
		     Chart serviceChart = getServiceChart();
		     list.add(serviceChart);
		//8  昨日总手数
		     Chart handChart = gethandChart(); 
		     list.add(handChart);
		     
		renderGson(list);
	}

	private Chart gethandChart() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取  (不分是否有效)
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getHandByDay"),
				new Object[] {time, time});
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getHandNumByDay"),new Object[]{stime,time});
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("h_sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getGameMap("h");
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	private Chart getServiceChart() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取  (不分是否有效)
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getServiceByDay"),
				new Object[] {time, time});
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getServiceNumByDay"),new Object[]{stime,time});
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("s_sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getGameMap("s");
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	private Chart getPlayerCountChart() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getPlyerInfoByDay"),
				new Object[] {"0",time, time });
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getPlyerNumByDay"),new Object[]{"0",stime,time});
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("p_sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getGameMap("p");
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	private Chart getGameStartChart() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getGameInfoByDay"),
				new Object[] {"0",time, time });
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getGameNumByDay"),new Object[]{"0",stime,time});
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("g_sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getGameMap("g");
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	private Chart getConsumeMoney() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		//Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24*7);
		Long time = 1511971200000l;
        Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getMoneyByDay"),
				new Object[] { "0", time, time });
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getMoneyNumByDay"),new Object[]{"0",stime,time});	
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getConsumeMoneylMap();
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	private Chart getAddMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	private Chart getConsumeDiamond() {
		Chart chart = new Chart();
		// 1昨日消耗数据
		//Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24*7);
		Long time = 1511971200000l;
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getDiamondByDay"),
				new Object[] { "0", time, time });
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getDiamondNumByDay"),new Object[]{"0",stime,time});
		Double avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		Double nowNum = Double.parseDouble(record.get("sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getConsumeDiamondlMap();
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;
	}

	//各渠道充值
	private Chart getAddDiamond() {
		Chart chart = new Chart();
		// 1昨日消耗数据

		//Long time = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		Long time = 1512316800000l;
		Long stime =DateUtils.changeHour(time, -24*7);
		// 2 数据库获取
		Record record = Db.findFirst(SqlKit.sql("data.dailyReport.getAddDiamondByDay"),
				new Object[] { "0", time, time });
        //2.1 七日变化
		Number num = Db.queryNumber(SqlKit.sql("data.dailyReport.getAddDiamondNumByDay"),new Object[]{"0",stime,time});	
		Double avgNum = null;
		if(num!=null) {
		avgNum = ArithUtils.div(num.doubleValue(), 7, 2);
		}
		Double nowNum = Double.parseDouble(record.get("sum").toString());
		String desc = getChangeDesc(avgNum,nowNum);
		chart.setDesc(desc);
		// 3 封装list
		Map<String, String> trans = DataBaseMapperUtils.getRechargelMap();
		trans.remove("日期");
		trans.remove("汇总");
		List<Map<String, Object>> list = fillDataList(chart, record, trans);
		chart.setSeriesDate("各消耗价位占比", "pie", list);
		return chart;

	}

	private String getChangeDesc(Double avgNum, Double nowNum) {
		String  desc = "";
		double result = 0;
		if(avgNum>nowNum) {
			double temp = ArithUtils.sub(avgNum, nowNum);
			System.out.println("均值大于今日"+temp+"||"+avgNum);
			result = ArithUtils.div(temp, avgNum, 4);
			result = ArithUtils.mul(result, 100);
			desc="减少";
		}else {
			double temp = ArithUtils.sub(nowNum, avgNum);
			result = ArithUtils.div(temp, avgNum, 4);
			result = ArithUtils.mul(result, 100);
			System.out.println("均值小于等于今日"+temp+"||"+result);
			desc="增加";
		}
		
		BigDecimal bg=new BigDecimal(nowNum.toString());
		String num = bg.toPlainString();
//		if(length>index+1&&num.charAt(index+1)=='0') {
//            System.out.println("ssss");
//		}
		if(num.endsWith(".0")) {
			num = num.substring(0,num.indexOf("."));
		}
		desc=num+" 较7日均值"+desc+result+"%";
		return desc;
	}
	
	

	private List<Map<String, Object>> fillDataList(Chart chart, Record record, Map<String, String> trans) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String str : record.getColumnNames()) {
			for (Entry<String, String> e : trans.entrySet()) {
				if (e.getValue().equals(str)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", e.getKey());
					map.put("y", record.get(str));
					// 数据库字段名
					map.put("drilldown", str);
					list.add(map);
				}
			}
		}
		return list;
	}
}
