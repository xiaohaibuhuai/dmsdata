package com.illumi.oms.data.table.dailyreport.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.Response;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ExcelTableSheet;
import com.illumi.oms.data.utils.ArithUtils;
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

@ControllerBind(controllerKey = "/data/table/dailyreport/rechargedaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class RechargeDailyController extends ExcelController {
	private static final Logger log = Logger.getLogger(RechargeDailyController.class);

	// 图表
	public void rechargeChart() {
		List<Record> records = getDataList();
		Chart chart = new Chart();
		List<String> datelist = new ArrayList<String>();
		//初始化map
		Map<String, List<Double>> resultMap = initRelustMap();
		
		Map<String, String> channelMap = getChannelMap();
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
	// 表格
	public void dataGrid() {
	
		List<Record> list = getDataList();
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}

	//下载
	public void download() {
		Map<String, String> channelMap = getChannelMap();
		channelMap.put("日期", "targetdate");
		channelMap.put("汇总", "sum");
		
		String[]  head = {"日期","苹果充值","步步德扑","九格创想","微信公众号","微信CMS","支付宝大额","支付宝CMS","安卓微信充值","汇总"};
		List<Record> dataList = getDataList();
		//标题时间类型  并格式化时间
		String timeDes=FormatExcelTime(dataList);
		//标题类型
		String type = getPara("type");
		String resultType=type.equals("0")?"全部":type.equals("1")?"国内":"海外";
		String title = "每日充值汇总_"+resultType+timeDes;
		ExcelTableSheet sheet = new ExcelTableSheet(title, head, "第一页",dataList,null);
		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(sheet);

		renderNewExcel(xs, "充值日报_"+resultType+timeDes);
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
	// 苹果支付 谷歌支付 步步德扑克 九格创想 微信安卓 微信公众号 微信CMS 支付宝大额 支付宝CMS 支付宝公众号
	private String paseKey(String key) {
		switch (key) {
		case "101":
			return "苹果充值";
		case "102":
			return "谷歌支付";
		case "201":
			return "步步德扑";
		case "202":
			return "九格创想";
		case "301":
			return "微信安卓充值";
		case "302":
			return "微信公众号";
		case "303":
			return "微信CMS";
		case "401":
			return "支付宝大额";
		case "402":
			return "支付宝公众号";
		case "403":
			return "支付宝CMS";
		default:
			return null;
		}
	}
	private Map<String, List<Double>> initRelustMap() {
		// 苹果支付 谷歌支付 步步德扑克 九格创想 微信安卓 微信公众号 微信CMS 支付宝大额 支付宝CMS 支付宝公众号
		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		Map<String, String> channelMap = getChannelMap();
		for (Entry<String, String> e : channelMap.entrySet()) {
			
				map.put(e.getKey(), new ArrayList<Double>());

		}
		return map;
	}
	private List<Record> getDataList() {
		Long dateStart = getLongDate("dateStart");
		Long dateEnd = getLongDate("dateEnd");
		String type = getPara("type");
	    return Db.find(SqlKit.sql("data.dailyReport.getRechargeByDay"),new Object[] {type,dateStart,dateEnd});
	}

	private Map<String, String> getChannelMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("苹果充值","101");
		map.put("谷歌支付","102");
		map.put("步步德扑","201");
		map.put("九格创想","202");
		map.put("微信安卓","301");
		map.put("微信公众号","302");
		map.put("微信CMS","303");
		map.put("支付宝大额","401");
		map.put("支付宝公众号","402");
		map.put("支付宝CMS","403");

		return map;
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
	
	
	

}
