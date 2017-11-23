package com.illumi.oms.data.table.dailyreport.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.ExcelController;
import com.illumi.oms.data.utils.ExcelUtil;
import com.illumi.oms.system.model.Chart;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/data/table/dailyreport/moneydaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class MoneyDailyController extends ExcelController{

	/*
	 * @params :  
	 * dateStrat/dateEnd: yyyy-MM-dd
	 * type: 0 全部用户  1国内用户  2海外用户
	 * @return 
	 */
	public void money() {
		String dateStart = getPara("dateStart");
		String dateEnd = getPara("dateEnd");
		String type = getPara("type");
		
		System.out.println("ds:"+dateStart+"||"+"de:"+dateEnd+"||type:"+type);
		 
		//根据类型找到用户uuid
		// ID_TYPE_FACEBOOK
		//select uuid from  t_user_baseinfo where idtype = "ID_TYPE_FACEBOOK" or countrycode != 86;   
		
		
		
		
		Chart chart = new Chart();
		List<Long> flog = new ArrayList<>();
		flog.add(2l);
		flog.add(-33l);
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
	
	public void diamond() {
		Chart chart = new Chart();
		List<Long> flog = new ArrayList<>();
		flog.add(2l);
		flog.add(-33l);
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
	
	
      public void download() {
		
    	  String[] head = { "日期", "服务费", "互动道具", "魔法表情" };
	       
  		List<Map<String, String>> table1 = new ArrayList<Map<String, String>>();
  		Map<String, String> map = new HashMap<String, String>();
  		map.put(head[0], "2017-10-1");
  		map.put(head[1], "111");
  		map.put(head[2], "222");
  		map.put(head[3], "333");

  		Map<String, String> map2 = new HashMap<String, String>();

  		map2.put(head[0], "2017-10-2");
  		map2.put(head[1], "aaa");
  		map2.put(head[2], "bbb");
  		map2.put(head[3], "ccc");

  		table1.add(map);
  		table1.add(map2);
  		
  		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbookSingle("数据", head, table1);
  		
  	    renderNewExcel(xs, "中文");
	}
      
      
     
}
