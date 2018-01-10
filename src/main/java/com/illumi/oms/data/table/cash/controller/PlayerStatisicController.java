package com.illumi.oms.data.table.cash.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.DateUtils;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/cash/playerstatistic", viewPath = UrlConfig.DATA_TAB_CASH)
public class PlayerStatisicController extends EasyuiController<Record>{


	//各牌局类型14日玩家统计
	public void getGamePlayer() {
		long dateEnd = DateUtils.getCurrentZeroTime();
		long dateStart = DateUtils.changeHour(dateEnd,-14*24);
		//2 查数据库
		List<Record>  gameValidInfo= Db.use(Consts.DB_POKERDATA).find(SqlKit.sql("data.reportForms.getGameInfoByDate"),new Object[]{dateStart,dateEnd});
		
		//Collections.reverse(gameValidInfo); 倒序
		
		DataGrid<Record> data = new DataGrid<Record>();
		data.setRows(gameValidInfo);
		
		renderJson(data);               		
	}
	//普通局盲注活跃人数
	public void getBlindPlayer4Normal() {
		DataGrid<Record> data = getBlindDateByType(1);
		renderJson(data);
	}
	
	
	//普通局保险局盲注活跃人数
    public void getBlindPlayer4Normalins() {
       	DataGrid<Record> data = getBlindDateByType(3);
		renderJson(data);	
	}
    
    //奥马哈盲注活跃人数
  	public void getBlindPlayer4Omaha() {
  		DataGrid<Record> data = getBlindDateByType(5);
		renderJson(data);
  	}
  	
    //奥马哈盲注活跃人数
  	public void getBlindPlayer4Omahains() {
  		DataGrid<Record> data = getBlindDateByType(6);
		renderJson(data);
  	}
  	
  	
  	
  	private DataGrid<Record> getBlindDateByType(int type) {
  		long dateEnd = DateUtils.getCurrentZeroTime();
		long dateStart = DateUtils.changeHour(dateEnd,-14*24);
		// 2 查数据库
		List<Record>  gameInfo= Db.use(Consts.DB_POKERDATA).find(SqlKit.sql("data.reportForms.getBlindInfoByType"),new Object[]{type,dateStart,dateEnd});
		
		//Collections.reverse(gameInfo);  倒叙 
		DataGrid<Record> data = new DataGrid<Record>();
		data.setRows(gameInfo);
		return data;
	}
}
