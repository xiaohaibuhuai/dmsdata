package com.illumi.oms.stat.controller;

import java.util.Date;
import java.util.List;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.UserWhite;
import com.illumi.oms.service.RecordUtil;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/playertrace" ,viewPath=UrlConfig.STAT)
public class PalyerTraceController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(PalyerTraceController.class);
	public void list()
	{
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",关注玩家管理列表查询(/stat/player/list),请求参数/"+getParasLog());
		DataGrid<Record> dg = RecordUtil.listByDataGrid(SqlKit.sql("stat.player.getPlayerList"), getDataGrid(), getForm());
		for(Record rd:dg.getRows()){
			Record udrd = Db.findFirst(SqlKit.sql("stat.player.getIpInfoByUuid"),rd.getInt("uuid"));
			if(udrd != null){
				String address = "";
				if(udrd.getStr("country").equals("CN")){
					address +="中国";
				}else{
					address +=udrd.getStr("country");
				}
				address += udrd.getStr("province")+udrd.getStr("city");
				rd.set("address", address);
				rd.set("ip", udrd.getStr("ip"));
			}
		}
		renderJson(dg);
//		renderJson(RecordUtil.listByDataGrid(SqlKit.sql("stat.player.getPlayerList"), getDataGrid(), getForm()));
	}
	
	//关注
	public void start()
	{
		boolean flag = false;
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",关注玩家(/stat/playertrace/start),请求参数/"+getParaToInt("id"));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.player.insertTraceUuid"),getParaToInt("id"), user.getInt("uuid"));
		if(row == 1){
			flag = true;
		}
		renderJsonResult(flag);
	}
	//取消关注
	public void end()
	{
		boolean flag = false;
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",取消关注玩家(/stat/playertrace/end),请求参数/"+getParaToInt("id"));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.player.deleteTraceUuid"),getParaToInt("id"), user.getInt("uuid"));
		if(row == 1){
			flag = true;
		}
		renderJsonResult(flag);
	}
	
	private Form getForm(){
		Form f = getFrom("");
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		if(StringUtil.isBlank(getPara("uuid"))){
			f.setFromParm("uuid", getUUIDlistbyUuid(user.getInt("uuid")));
			f.inValue.add("uuid");
		}else if(StringUtil.isNotBlank(getUUIDbyUuid(user.getInt("uuid"),getPara("uuid")))){
			f.setFromParm("uuid", getPara("uuid"));
			f.integerValue.add("uuid");
		}else{
			f.setFromParm("uuid", 0+"");
			f.inValue.add("uuid");
		}
		
		f.setFromParm("nickname", getPara("nickname"));
		f.setFromParm("phonenumber", getPara("phonenumber"));
		f.fuzzySerach.add("nickname");
		f.integerValue.add("phonenumber");
		
		return f;
	}

	private String getUUIDlistbyUuid(int uuid){
		List<Record> list = Db.find(SqlKit.sql("stat.player.getTraceUuidList"),uuid);
		String strSql = "";
		for(Record rd:list){
			strSql += "'"+ rd.getInt("uuid")+"',";
		}
		if(strSql.endsWith(",")){
			strSql = strSql.substring(0, strSql.length()-1);
		}
		if(StringUtil.isBlank(strSql)){
			strSql = 0+"";
		}
		return strSql;
	}
	
	private String getUUIDbyUuid(int opertor,String uuid){
		List<Record> list = Db.find(SqlKit.sql("stat.player.getTraceUuid"),opertor,uuid);
		String strSql = "";
		for(Record rd:list){
			strSql += "'"+ rd.getInt("uuid")+"',";
		}
		if(strSql.endsWith(",")){
			strSql = strSql.substring(0, strSql.length()-1);
		}
		return strSql;
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("uuid"))){
    		sb.append(",UUID:"+getPara("uuid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("phonenumber"))){
    		sb.append(",手机:"+getPara("phonenumber"));
    	}
    	
    	if(!StringUtil.isNullOrEmpty(getPara("nickname"))){
    		sb.append(",昵称:"+getPara("nickname"));
    	}
    
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }
	
}