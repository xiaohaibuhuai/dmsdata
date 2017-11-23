package com.illumi.oms.data.table.dailyreport.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/dailyreport/rechargedaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class RechargeDailyController extends ExcelController {

	
	public void download() throws FileNotFoundException, IOException {
		
        String dateStart = getPara("h_dateStart");
        String dateEnd = getPara("h_dateEnd");
        String type = getPara("h_type");
        
        System.out.println(dateStart+"||"+dateEnd+"||"+type);
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
		
	    renderNewExcel(xs, "统计表");
	}

	
}
