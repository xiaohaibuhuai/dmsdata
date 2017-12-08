package com.illumi.oms.data.table.weekreport.controller;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.ExcelController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;

@ControllerBind(controllerKey = "/data/table/weekreport/clubweek", viewPath = UrlConfig.DATA_TAB_WEEKREPORT)
public class ClubWeekController extends ExcelController{

	private static final Logger log = Logger.getLogger(ClubWeekController.class);
	
	public void clubLevel() {
		String dataStart = getPara("dataStart");
		String dataEnd = getPara("dataEnd");
		System.out.println(dataStart);
		
		System.out.println(dataEnd);
	}
	
	
	
	
	public void clubCheat() {
		String dataStart = getPara("dataStart");
		String dataEnd = getPara("dataEnd");
		System.out.println(dataStart);
		
		System.out.println(dataEnd);
	}
	
	public void excelDown() {
		String dataStart = getPara("dataStart");
		String dataEnd = getPara("dataEnd");
		System.out.println(dataStart);
		System.out.println(dataEnd);
	}
}
