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
import com.illumi.oms.data.model.ExcelTableSheet;
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
		
    	  String[] head = { "日期", "服务费", "互动道具", "魔法表情","翻翻看","弹幕","扑克机","加勒比","牛牛—一粒大米","八八碰—一粒大米","打赏牌谱","德扑币报名MTT","汇总"};
  		String[] head1 = {"日期","联盟局","修改昵称","延时道具","MTT门票","俱乐部推送","俱乐部改名","汇总"};
  		
  		String dateStart = getPara("h_dateStart");
          String dateEnd = getPara("h_dateEnd");
          String type = getPara("h_type");
          
          System.out.println(dateStart+"||"+dateEnd+"||"+type);
  	       
  		List<Map<String,String>> list = getList();
  		List<Map<String,String>> list2 = getList2();
  		
  		ExcelTableSheet sheet = new ExcelTableSheet("每日德扑币消耗", head, list, "第一页");
  		ExcelTableSheet sheet2 = new ExcelTableSheet("每日钻石消耗汇总", head1, list, "第二页");
  		
  		
  		XSSFWorkbook xs = ExcelUtil.getXSSFWorkbook(sheet,sheet2);
  		
  	    renderNewExcel(xs, "统计表");
	}
      private List<Map<String, String>> getList2() {
  		String[] head = {"日期","联盟局","修改昵称","延时道具","MTT门票","俱乐部推送","俱乐部改名","汇总"};
  		List<Map<String, String>> table = new ArrayList<Map<String, String>>();
  		Map<String, String> map = new HashMap<String, String>();
  		map.put(head[0], "1999-08-25");
  		map.put(head[1], "12");
  		map.put(head[2], "333");
  		map.put(head[3], "6623");
  		map.put(head[4], "4534");
  		map.put(head[5], "987");
  		map.put(head[6], "112");
  		map.put(head[7], "233");
  		Map<String, String> map1 = new HashMap<String, String>();
  		map.put(head[0], "1999-08-25");
  		map.put(head[1], "12");
  		map.put(head[2], "333");
  		map.put(head[3], "6623");
  		map.put(head[4], "4534");
  		map.put(head[5], "987");
  		map.put(head[6], "112");
  		map.put(head[7], "233");
  		
  		
  		table.add(map);
  		table.add(map1);
  		return null;
  	}

  	private List<Map<String, String>> getList() {
  		String[] head = { "日期", "服务费", "互动道具", "魔法表情","翻翻看","弹幕","扑克机","加勒比","牛牛—一粒大米","八八碰—一粒大米","打赏牌谱","德扑币报名MTT","汇总"};
  		List<Map<String, String>> table1 = new ArrayList<Map<String, String>>();
  		Map<String, String> map = new HashMap<String, String>();
  		map.put(head[0], "2017-10-1");
  		map.put(head[1], "111");
  		map.put(head[2], "222");
  		map.put(head[3], "1232");
  		map.put(head[4], "1323244");
  		map.put(head[5], "4545");
  		map.put(head[6], "6666");
  		map.put(head[7], "3356753");
  		map.put(head[8], "3352223");
  		map.put(head[9], "333");
  		map.put(head[10], "334533");
  		map.put(head[11], "335213");
  		map.put(head[12], "3333");
  		

  		Map<String, String> map2 = new HashMap<String, String>();

  		map2.put(head[0], "2017-10-1");
  		map2.put(head[1], "111");
  		map2.put(head[2], "222");
  		map2.put(head[3], "1232");
  		map2.put(head[4], "1323244");
  		map2.put(head[5], "4545");
  		map2.put(head[6], "6666");
  		map2.put(head[7], "3356753");
  		map2.put(head[8], "3352223");
  		map2.put(head[9], "333");
  		map2.put(head[10], "334533");
  		map2.put(head[11], "335213");
  		map2.put(head[12], "3333");
  		
  		
  		Map<String, String> map1 = new HashMap<String, String>();

  		map1.put(head[0], "2017-10-1");
  		map1.put(head[1], "111");
  		map1.put(head[2], "222");
  		map1.put(head[3], "1232");
  		map1.put(head[4], "1323244");
  		map1.put(head[5], "4545");
  		map1.put(head[6], "6666");
  		map1.put(head[7], "3356753");
  		map1.put(head[8], "3352223");
  		map1.put(head[9], "333");
  		map1.put(head[10], "334533");
  		map1.put(head[11], "335213");
  		map1.put(head[12], "3333");
  		
  		
  		Map<String, String> map3 = new HashMap<String, String>();

  		map3.put(head[0], "2017-10-1");
  		map3.put(head[1], "111");
  		map3.put(head[2], "222");
  		map3.put(head[3], "1232");
  		map3.put(head[4], "1323244");
  		map3.put(head[5], "4545");
  		map3.put(head[6], "6666");
  		map3.put(head[7], "3356753");
  		map3.put(head[8], "3352223");
  		map3.put(head[9], "333");
  		map3.put(head[10], "334533");
  		map3.put(head[11], "335213");
  		map3.put(head[12], "3333");
  		
  		
  		Map<String, String> map4= new HashMap<String, String>();

  	
  		map4.put(head[1], "111");
  		map4.put(head[2], "222");
  		map4.put(head[3], "1232");
  		map4.put(head[4], "1323244");
  		map4.put(head[5], "4545");
  		map4.put(head[6], "6666");
  		map4.put(head[7], "3356753");
  		map4.put(head[8], "3352223");
  		map4.put(head[9], "333");
  		map4.put(head[10], "334533");
  		map4.put(head[11], "335213");
  	
  		
  		
  		Map<String, String> map5 = new HashMap<String, String>();

  		map5.put(head[0], "2017-10-1");
  		map5.put(head[1], "111");
  		map5.put(head[2], "222");
  	
  		map5.put(head[4], "1323244");
  		map5.put(head[5], "4545");
  		
  		map5.put(head[7], "3356753");
  		map5.put(head[8], "3352223");
  	
  		map5.put(head[10], "334533");
  		map5.put(head[11], "335213");
  		map5.put(head[12], "3333");

  		table1.add(map);
  		table1.add(map2);
  		table1.add(map3);
  		table1.add(map4);
  		table1.add(map5);
  		
  		return table1;
  	}
      
     
}
