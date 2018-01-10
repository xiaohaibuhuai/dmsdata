package com.illumi.oms.data.table.dailyreport.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ExcelTableSheet;
import com.illumi.oms.data.utils.DataBaseMapperUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.data.utils.ExcelController;
import com.illumi.oms.data.utils.ExcelUtil;
import com.illumi.oms.data.utils.IErrCodeEnum;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/dailyreport/moneydaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class MoneyDailyController extends ExcelController {

	private static final Logger log = Logger.getLogger(MoneyDailyController.class);

	/*
	 * @params : dateStrat/dateEnd: yyyy-MM-dd type: 0 全部用户 1国内用户 2海外用户
	 * 
	 * @return
	 */
	public void moneyChart() {
		List<Record> records = getMoneyList();
		Chart chart = new Chart();
		List<String> datelist = new ArrayList<String>();
		//初始化map
		Map<String, List<Double>> resultMap = initMoneyRelustMap();
		
		Map<String, String> channelMap = DataBaseMapperUtils.getConsumeMoneylMap();
		for(Record r:records) {
			//1 封装日期
			datelist.add(DateUtils.getDateFormat4Day().format(r.getLong("targetdate")));
			for(String key:resultMap.keySet()) {
				List<Double> list = resultMap.get(key);
				Object value = r.get(channelMap.get(key));
				list.add(Double.parseDouble(value.toString()));
			}	
		}
		
		chart.setCategories(datelist);
		for (Entry<String, List<Double>> e : resultMap.entrySet()) {
			chart.setSeriesDate(e.getKey(), null, e.getValue());
		}
		
	   renderGson(chart);
	}
	
	public void diamondChart() {
		List<Record> records = getDiamondList();
		Chart chart = new Chart();
		List<String> datelist = new ArrayList<String>();
		//初始化map
		Map<String, List<Double>> resultMap = initDiamondRelustMap();
		
		Map<String, String> channelMap = DataBaseMapperUtils.getConsumeDiamondlMap();
		for(Record r:records) {
			//1 封装日期
			datelist.add(DateUtils.getDateFormat4Day().format(r.getLong("targetdate")));
			for(String key:resultMap.keySet()) {
				List<Double> list = resultMap.get(key);
				Object value = r.get(channelMap.get(key));
				list.add(Double.parseDouble(value.toString()));
			}	
		}
		chart.setCategories(datelist);
		for (Entry<String, List<Double>> e : resultMap.entrySet()) {
			chart.setSeriesDate(e.getKey(), null, e.getValue());
		}
	   renderGson(chart);
	}
	public void moneyDataGrid() {
		List<Record> list = getMoneyList();
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}
	public void diamondDataGrid() {
		List<Record> list = getDiamondList();
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}
	public void download() {
		String[] head1 = { "日期", "服务费", "互动道具", "魔法表情", "翻翻看", "弹幕", "扑克机", "加勒比", "牛牛_一粒大米", "八八碰_一粒大米","奔驰宝马_一粒大米","捕鱼_一粒大米","打赏牌谱",
				"德扑币报名MTT", "汇总" };
		String[] head2 = { "日期", "联盟局", "修改昵称", "延时道具", "MTT门票", "俱乐部推送", "俱乐部改名", "汇总" };

		List<Record> moneyList = getMoneyList();
		List<Record> diamondList = getDiamondList();
		String timeDes1 = FormatExcelTime(moneyList);
		String timeDes2 = FormatExcelTime(diamondList);
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");
		String type = getPara("type");
		String resultType=type.equals("0")?"全部":type.equals("1")?"国内":"海外";
		String titleMoney = "每日德扑币消耗汇总_"+resultType+timeDes1;
		String titleDiamond = "每日钻石消耗汇总_"+resultType+timeDes2;
		ExcelTableSheet sheet = new ExcelTableSheet(titleMoney, head1,"每日德扑币消耗",moneyList,DataBaseMapperUtils.getConsumeMoneylMap());
		ExcelTableSheet sheet2 = new ExcelTableSheet(titleDiamond, head2,"每日钻石消耗汇总",diamondList,DataBaseMapperUtils.getConsumeDiamondlMap());

		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(sheet, sheet2);

		renderNewExcel(xs, "货币消耗日报_"+resultType+"("+dateStart+"至"+dateEnd+")");
	}


	private Long getLongDate(String string) {
		try {
		String time = getPara(string);
		long result = DateUtils.getDateFormat4Day().parse(time).getTime();
		return result;
		}catch (Exception e) {
			e.printStackTrace();
			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
		}
		return null;
	}
	
	private List<Record> getMoneyList() {
		Long dateStart = getLongDate("dateStart");
		Long dateEnd = getLongDate("dateEnd");
		String type = getPara("type");
	    return Db.find(SqlKit.sql("data.dailyReport.getMoneyByDay"),new Object[] {type,dateStart,dateEnd});
	}
	
	private List<Record> getDiamondList() {
		Long dateStart = getLongDate("dateStart");
		Long dateEnd = getLongDate("dateEnd");
		String type = getPara("type");
	    return Db.find(SqlKit.sql("data.dailyReport.getDiamondByDay"),new Object[] {type,dateStart,dateEnd});
	}
	
	
	private Map<String, List<Double>> initMoneyRelustMap() {
		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		Map<String, String> channelMap = DataBaseMapperUtils.getConsumeMoneylMap();
		for (Entry<String, String> e : channelMap.entrySet()) {
			//只保留映射项
			if(!e.getKey().equals("日期")&&!e.getKey().equals("汇总")) {
				map.put(e.getKey(), new ArrayList<Double>());
			}

		}
		return map;
	}
	private Map<String, List<Double>> initDiamondRelustMap(){
		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		Map<String, String> channelMap = DataBaseMapperUtils.getConsumeDiamondlMap();
		for (Entry<String, String> e : channelMap.entrySet()) {
			//只保留映射项
			if(!e.getKey().equals("日期")&&!e.getKey().equals("汇总")) {
				map.put(e.getKey(), new ArrayList<Double>());
			}
		}
		return map;
	}
	private String FormatExcelTime(List<Record> dataList) {
		//格式化日期
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String startDate="";
				String endDate="";
				for(int i=0,n=dataList.size();i<n;i++) {
					
					Record record = dataList.get(i);
					String time = df.format(record.get("targetdate"));
					record.set("targetdate",time);
					if(i==0) {
						startDate=time;
					}
					if(i==dataList.size()-1) {
						endDate=time;
					}
				}
		return "("+startDate+" 至 "+endDate+")";
	}
}
