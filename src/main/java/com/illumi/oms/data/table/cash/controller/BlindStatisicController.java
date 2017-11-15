package com.illumi.oms.data.table.cash.controller;

import java.util.Collections;
import java.util.List;

import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.DateUtils;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/cash/blindstatistic", viewPath = UrlConfig.DATA_TAB_CASH)
public class BlindStatisicController extends EasyuiController<Record>{

	
	//普通局
	public void getBlindNormal() {
		DataGrid<Record> data = getBlindDateByType(1);
		renderJson(data);
	}
	
	//普通保险局
	public void getBlindNormalins() {
		DataGrid<Record> data = getBlindDateByType(3);
		renderJson(data);
	}
	
	//奥马哈局
	public void getBlindOmaha() {
		DataGrid<Record> data = getBlindDateByType(5);
		renderJson(data);
	}
	//奥马哈保险局
	public void getBlindOmahains() {
		DataGrid<Record> data = getBlindDateByType(6);
		renderJson(data);
	}
	
	
	
	private DataGrid<Record> getBlindDateByType(int type) {
		//long dateEnd = DateUtils.getZeroTime(new Date().getTime());
		long dateEnd = 1508256000000l;
		long dateStart = DateUtils.changeHour(dateEnd,-14*24);
		// 2 查数据库
		List<Record>  gameInfo= Db.find(SqlKit.sql("data.reportForms.getBlindInfoByType"),new Object[]{type,dateStart,dateEnd});
		//倒序
		Collections.reverse(gameInfo); 
		
		DataGrid<Record> data = new DataGrid<Record>();
		data.setRows(gameInfo);
		return data;
	}
	
}
