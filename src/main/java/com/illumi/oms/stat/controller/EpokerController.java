package com.illumi.oms.stat.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.ResultInfo;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.service.RecordUtil;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/epoker" ,viewPath=UrlConfig.STAT)
public class EpokerController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(EpokerController.class);
	
	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",错误牌局列表(/stat/epoker/list),请求参数/"+getParasLog());
		long beginstamp = System.currentTimeMillis();
		DataGrid<Record> dg = RecordUtil.listByDataGrid(Consts.DB_POKER3, "stat.epoker.getEpokerlist", getDataGrid(), getParas());
		long responseTime = System.currentTimeMillis() - beginstamp;
		log.info("getEpokerlist responseTime : " + responseTime);
		renderJson(dg);
	}
	
	public void count(){
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",错误牌局数据统计(/stat/epoker/count),请求参数/"+getParasLog());
		ResultInfo rf = new ResultInfo();
		rf.setCode(200);
		try{
			Object[] paras = getParas();
			Record rd = Db.use(Consts.DB_POKER3).findFirst(SqlKit.sql("stat.epoker.getCountPoker"), paras);
			long etotal = Db.use(Consts.DB_POKER3).queryLong(SqlKit.sql("stat.epoker.getCountEPoker"), paras);
			rd.set("etotal", etotal);
			rf.setObj(rd);
		}catch(Exception e){
			e.printStackTrace();
			rf.setCode(500);
			rf.setMsg("查找异常");
		}
		renderJson(rf);
	}
	
	private Object[] getParas(){
		
		DateTime dateStart = null;
		DateTime dateEnd = null;
		
		if(StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTime.now().minusDays(1).withMillisOfDay(0);
			dateEnd = DateTime.now().withMillisOfDay(0);
			
		}else if(StrKit.isBlank(getPara("dateStart"))&&!StrKit.isBlank(getPara("dateEnd"))){
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateEnd")+" 23:59:59");
			dateStart = dateEnd.minusDays(1).withMillisOfDay(0);
					
		}else if(!StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateStart")+ " 00:00:00");
			dateEnd = DateTime.now();
		}else{
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateStart")+ " 00:00:00");
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateEnd")+" 23:59:59");
		}
		

		Object[] obj = {dateStart.getMillis(),dateEnd.getMillis()};
		return obj;
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("dateStart"))){
    		sb.append(",开始时间起:"+getPara("dateStart"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateEnd"))){
    		sb.append(",开始时间止:"+getPara("dateEnd"));
    	}
    
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }
	
}
