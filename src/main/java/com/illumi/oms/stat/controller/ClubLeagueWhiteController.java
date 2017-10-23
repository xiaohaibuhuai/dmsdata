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


@ControllerBind(controllerKey = "/stat/clubleaguewhite" ,viewPath=UrlConfig.STAT)
public class ClubLeagueWhiteController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(ClubLeagueWhiteController.class);
	
	public void list()
	{
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		DataGrid<Record> dg = RecordUtil.listByDataGridByWhere(SqlKit.sql("stat.club.getClubLeagueWhiteList"), getDataGrid(), getForm());
		renderJson(dg);
	}
	
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("law.clubid", getPara("clubid"));
		f.inValue.add("law.clubid");
		f.setFromParm("ci.clubname", getPara("clubname"));
		f.fuzzySerach.add("ci.clubname");
		return f;
	}
	
	//关注
	public void start()
	{
		boolean flag = false;
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",关注俱乐部(/stat/clubtrace/start),请求参数/"+getParaToInt("id"));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.club.insertTraceClubid"),getParaToInt("id"), user.getInt("uuid"));
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
		log.info(user.getStr("account")+"/"+user.getName()+",取消关注俱乐部(/stat/clubtrace/end),请求参数/"+getParaToInt("id"));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.club.deleteTraceClubid"),getParaToInt("id"), user.getInt("uuid"));
		if(row == 1){
			flag = true;
		}
		renderJsonResult(flag);
	}
	
	public void add()
	{
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		String  clubid = getPara("clubid");
		Integer flag = 0;
		try{
			Record crd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.club.getClubByClubid"),clubid);
			if(crd == null){
				flag = 2;//俱乐部不存在
			}else{
				Record temp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.club.getClubLeagueWhiteByClubid"),clubid);
				if(temp!=null){
					flag = 4;//俱乐部已经在白名单中
				}else{
					flag = Db.use("prodb").update(SqlKit.sql("stat.club.addClubLeagueWhiteByClubid"),clubid);
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
			flag = 5;//添加用户异常
		}
		renderJson(flag);
	}
	
	
}
