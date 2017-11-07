package com.illumi.oms.data.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.model.ChartInfo;
import com.illumi.oms.data.utils.ELKUtils;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
@ControllerBind(controllerKey = "/data/index", viewPath = UrlConfig.DATA)
public class DataIndexController extends EasyuiController<Record>{

	
	public void diamondChange() {
		String target="diamone_change";  //钻石变化
		long time = -30*60*1000;
		String timeformat="30m";
		List<ChartInfo> chartlistLog = ELKUtils.getLogChartChangeInfo(target, time,timeformat);
		List<ChartInfo> chartlistTask =ELKUtils.getTaskChartChangeInfo(target, time,timeformat);
		Map<String,ChartInfo> map = new HashMap<>();
		map.put("log", chartlistLog.get(1));
		map.put("task", chartlistTask.get(1));
		renderGson(map);
	}
	
	public void coinChange() {
		String target="money_change";  //钻石变化
		long time = -30*60*1000;
		String timeformat="30m";
		List<ChartInfo> chartlistLog = ELKUtils.getLogChartChangeInfo(target, time,timeformat);
		List<ChartInfo> chartlistTask =ELKUtils.getTaskChartChangeInfo(target, time,timeformat);
		Map<String,ChartInfo> map = new HashMap<>();
		map.put("log", chartlistLog.get(1));
		map.put("task", chartlistTask.get(1));
		renderGson(map);
	}
}
