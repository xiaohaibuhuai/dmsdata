package com.illumi.dms.system.controller;

import com.illumi.dms.common.Consts;
import com.illumi.dms.common.UrlConfig;
import com.illumi.dms.common.utils.StringUtil;
import com.illumi.dms.system.model.Log;
import com.illumi.dms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;


@CacheName(value = "/system/log")
@ControllerBind(controllerKey = "/system/log" ,viewPath=UrlConfig.SYSTEM)
public class LogController extends EasyuiController<Log> 
{

	private static final Logger log = Logger.getLogger(LogController.class);
//	@Before(value = { CacheInterceptor.class })
	public void getVisitCount(){
		renderGson(Log.dao.getVisitCount());
		
	}
	
	public void list()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",日志管理日志查看(/system/log/list),请求参数/"+ getParasLog());
		renderJson( Log.dao.listByDataGrid(getDataGrid(), getFrom(Log.dao.tableName)));
	}
	
	public void excel()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",日志管理导出excel(/system/log/excel)");
		renderExcel(Log.dao.list(getFrom(Log.dao.tableName)),"log.xls",new String[]{"uid","id", "用户","事件","来源","日期","ip"});
	
	}

	public void chart(){
		renderGson(Log.dao.chart(getFrom(null)));
	}
	
	
	
	
	@Before(value = { EvictInterceptor.class })
	public void delete()
	{
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",日志管理删除记录(/system/log/delete),请求参数/"+getPara("id"));
		renderJsonResult( Log.dao.deleteById(getPara("id")));
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("operation"))){
    		sb.append(",事件类型:"+getPara("operation"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateStart"))){
    		sb.append(",日期起:"+getPara("dateStart"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateEnd"))){
    		sb.append(",日期止:"+getPara("dateEnd"));
    	}
    	
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }

}
