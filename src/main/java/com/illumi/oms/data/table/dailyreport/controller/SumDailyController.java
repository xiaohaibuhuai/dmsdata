package com.illumi.oms.data.table.dailyreport.controller;

import com.illumi.oms.common.UrlConfig;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/dailyreport/sumdaily", viewPath = UrlConfig.DATA_TAB_DAILYREPORT)
public class SumDailyController extends EasyuiController<Record>{

	
}
