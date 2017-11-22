package com.illumi.oms.data.table.cash.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/cash/gamestatistic", viewPath = UrlConfig.DATA_TAB_CASH)
public class GameStatisticController extends EasyuiController<Record> {

	public void chart() {

		long dateEnd = DateUtils.getZeroTime(new Date().getTime());
		//long dateEnd = 1508256000000l;
		Chart chart = getChartDateByDay(14, dateEnd);
		renderGson(chart);
	}
	/**
	 * 14日开局总数统计
	 */

	public void sum() {
		// 1 获取时间
		long dateEnd = DateUtils.getZeroTime(new Date().getTime());
		//long dateEnd = 1508256000000l;
		long dateStart = DateUtils.changeHour(dateEnd, -14 * 24);
		// 2 查数据库
		List<Record> gameInfo = Db.find(SqlKit.sql("data.reportForms.getGameInfoByDate"),
				new Object[] { dateStart, dateEnd });
		
		//倒序
	    Collections.reverse(gameInfo); 
		DataGrid<Record> data = new DataGrid<Record>();
		data.setRows(gameInfo);

		// Gson 会有colums
		renderJson(data);
	}

	/**
	 * 14日有效开局
	 */
	public void valid() {
		// 1获取时间
		long dateEnd = DateUtils.getZeroTime(new Date().getTime());
		//long dateEnd = 1508256000000l;
		long dateStart = DateUtils.changeHour(dateEnd, -14 * 24);
		// 2 查数据库
		List<Record> gameValidInfo = Db.find(SqlKit.sql("data.reportForms.getGameValidInfoByDate"),
				new Object[] { dateStart, dateEnd });
		//倒序
	    Collections.reverse(gameValidInfo); 
		DataGrid<Record> data = new DataGrid<Record>();
		data.setRows(gameValidInfo);

		renderJson(data);

	}

	private String getGameValidSql() {
		String sql = "select from_unixtime(createtime/1000, '%m月%d日') days,COUNT(*) as num from t_bill_base_info where  createtime  BETWEEN ? AND ? AND totalgamenum>=10 GROUP BY days";
		return sql;
	}

	private String getgamenumSql() {
		String sql = "select from_unixtime(createtime/1000, '%m月%d日') days,COUNT(*) as num from t_bill_base_info where  createtime  BETWEEN ? AND ? GROUP BY days";
		return sql;
	}

	/**
	 * 去重 (如果某天没有记录 预留当天位置为null)
	 * 
	 * @param record
	 * @param dates
	 *            所查时间的集合
	 * @return
	 */
	private List<Long> parseRecord(List<Record> record, List<String> dates, int target) {
		List<Long> list = new ArrayList<Long>();
		// 如果某天没有记录
		if (record.size() != target) {
			Map<String, Long> datesmap = new TreeMap<String, Long>();
			for (String d : dates) {
				datesmap.put(d, null);
			}
			for (Record r : record) {
				String day = r.getStr("days");
				if (datesmap.containsKey(day)) {
					datesmap.put(day, r.getLong("num"));
				}
			}
			for (String d : dates) {
				list.add(datesmap.get(d));
			}
		} else {
			// 相等
			for (Record r : record) {
				list.add(r.getLong("num"));
			}
		}
		return list;
	}
	
	
	private Chart getChartDateByDay(int target, long dateEnd) {
		long dateStart = DateUtils.changeHour(dateEnd, -24 * target);
		// 封装日期
		List<String> dates = getDays(dateStart, target);
		String sqlSum = getgamenumSql();
		String sqlValid = getGameValidSql();
		List<Record> recordSum = Db.use(Consts.DB_POKER2).findByCache("getRecordSumByDate", dateEnd, sqlSum, new Object[] { dateStart, dateEnd });
		List<Record> recordValid = Db.use(Consts.DB_POKER2).findByCache("getVaildRecordSumByDate", dateEnd, sqlValid, new Object[] { dateStart, dateEnd });
		// 去重
		List<Long> sum = parseRecord(recordSum, dates, target);
		List<Long> valid = parseRecord(recordValid, dates, target);

		// 百分比
		List<Double> pre = new ArrayList<>(target);
		// 保留两位
		DecimalFormat df = new DecimalFormat("#0.00");
		for (int i = 0; i < sum.size(); i++) {
			if (sum.get(i) != null && valid.get(i) != null) {
				Double d = (valid.get(i) * 1.0 / sum.get(i)) * 100;
				String format = df.format(d);
				Double result = Double.valueOf(format.toString());
				pre.add(result);
			} else {
				pre.add(null);
			}
		}
		 //{y:29.7,extra:'hhh', suffix: '%'}, 
		List<Map<String,Object>> validPrelist=new ArrayList<Map<String,Object>>();
		for(int i=0,n=valid.size();i<n;i++) {
			Map<String,Object> map = new HashMap<>();
			map.put("y", valid.get(i));
			map.put("ext", pre.get(i));
			validPrelist.add(map);
		}
		
		List<Object> series = new ArrayList<Object>();
		series.add(sum);
		series.add(validPrelist);
		//series.add(pre);
		Chart chart = new Chart();
		chart.setCategories(dates);
		chart.setSeries(series);
		return chart;
	}
	
	private List<String> getDays(long dateStart, int index) {
		List<String> list = new ArrayList<>();
		DateFormat df = new SimpleDateFormat("MM月dd日");
		for (int i = 0; i < index; i++) {
			list.add(df.format(dateStart));
			dateStart = DateUtils.changeHour(dateStart, 24);
		}
		return list;
	}
}
