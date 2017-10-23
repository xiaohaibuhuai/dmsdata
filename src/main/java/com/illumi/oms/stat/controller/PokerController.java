package com.illumi.oms.stat.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;

import com.illumi.oms.common.Consts;
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


@ControllerBind(controllerKey = "/stat/poker" ,viewPath=UrlConfig.STAT)
public class PokerController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(PokerController.class);
	
	public void list()
	{
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",牌局管理普通列表查询(/stat/poker/list),请求参数/"+getParasLog());
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		//DataGrid<Record> dg = RecordUtil.listBySqlDataGrid(Consts.DB_POKER, getSql(), getDataGrid(), getParas());
		DataGrid<Record> dg = getDataGrid();
		Object[] paras = getParas();
		String sql = getSql();
		long total = Db.use(Consts.DB_POKER2).queryLong(RecordUtil.getCountSql2(sql), paras);
		sql = RecordUtil.getLimitSort(sql,dg);
		List<Record> rdList = Db.use(Consts.DB_POKER2).find(sql,paras);
		for(Record rd:rdList){
			Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("createuser"));
			/***
			 * 如果t_user_baseinfo的用户不存在了，此处user为null，则user.getStr("nickname")报空指针
			 * 添加判断判断条件
			 * update by zhangpeng 201600921
			 */
			if(user!=null){
				rd.set("nickname", user.getStr("nickname"));
				rd.set("showid", user.getStr("showid"));
			}else{
				rd.set("nickname", "");
			}
//			rd.set("nickname", user.getStr("nickname"));
		}
	   
		dg.rows = rdList;
		dg.total = (int)total;	
		
		
		renderJson(dg);
	}
	
	public void pokerInfo(){
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",牌局管理牌局信息查询(/stat/poker/pokerInfo),请求参数/"+getParaToInt("id"));
		int roomid = getParaToInt("id");
		Record rd = Db.use(Consts.DB_POKER2).findFirst(SqlKit.sql("stat.poker.getPokerById"),roomid);
		Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("createuser"));
		if(user != null){
			rd.set("nickname", user.getStr("nickname"));
			rd.set("showid", user.getStr("showid"));
		}
		BigDecimal total = Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.poker.getPokerTotal"), roomid);
		BigDecimal totalBuys = Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.poker.getPokerBuysTotal"), roomid);
		
		BigDecimal totalSystem = Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.poker.getPokerSystemTotal"), roomid);
		if(totalSystem!=null && total != null && !totalSystem.equals(0)){
			total = total.add(totalSystem);
		}
		
		rd.set("total", total);
		rd.set("totalBuys", totalBuys);
		rd.set("totalSystem", totalSystem);
		setAttr("rd", rd);
	}
	
	public void memberlist(){
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user.getStr("account")+"/"+user.getName()+",牌局管理牌局玩家信息查询(/stat/poker/memberlist),请求参数/"+getParaToInt("roomid"));
		int roomid = getParaToInt("roomid");
		DataGrid<Record> dg = getDataGrid();
		String sql = RecordUtil.getLimitSort(SqlKit.sql("stat.poker.getMemberlist"),dg);
		List<Record> rdList = Db.use(Consts.DB_POKER2).find(sql,roomid);
		for(Record rd:rdList){
			Record userplayer = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("uuid"));
			if(userplayer!=null){
				rd.set("nickname", userplayer.getStr("nickname"));
				rd.set("showid", userplayer.getStr("showid"));
			}else{
				rd.set("nickname", "");
			}
		}
	    long total = (Db.use(Consts.DB_POKER2).queryLong(RecordUtil.getCountSql(SqlKit.sql("stat.poker.getMemberlist")), roomid));
		dg.rows = rdList;
		dg.total = (int)total;	
		renderJson(dg);
		
		//renderJson(RecordUtil.listByDataGrid(Consts.DB_POKER, "stat.poker.getMemberlist", dg, roomid));
	}
	
	private String getSql(){
		String sql = SqlKit.sql("stat.poker.getPokerList");
		if(!StrKit.isBlank(getPara("roomid"))){
			sql += " and roomid = ? ";
		}
		if(!StrKit.isBlank(getPara("roomname"))){
			sql += " and roomname like ? ";
		}
		if(!StrKit.isBlank(getPara("createuser"))){
			sql += " and createuser = ?";
		}
		if(!StrKit.isBlank(getPara("dateStart"))){
			sql += " and createtime >= ? ";
		}
		if(!StrKit.isBlank(getPara("dateEnd"))){
			sql += " and createtime <=? ";
		}
		return sql;
	}
	
	
	private Object[] getParas(){
		
		List<Object> list = new ArrayList<Object>();
		if(!StrKit.isBlank(getPara("roomid"))){
			list.add(getParaToInt("roomid"));
		}
		if(!StrKit.isBlank(getPara("roomname"))){
			list.add("%"+getPara("roomname")+"%");
		}
		if(!StrKit.isBlank(getPara("createuser"))){
			list.add(getPara("createuser"));
		}
		if(!StrKit.isBlank(getPara("dateStart"))){
			list.add(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateStart")+ " 00:00:00").getMillis());
		}
		if(!StrKit.isBlank(getPara("dateEnd"))){
			list.add(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateEnd")+" 23:59:59").getMillis());
		}

		return list.toArray();
	}
	
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("roomid"))){
    		sb.append(",牌局ID:"+getPara("roomid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("romename"))){
    		sb.append(",牌局名字:"+getPara("romename"));
    	}
    	
    	if(!StringUtil.isNullOrEmpty(getPara("createuser"))){
    		sb.append(",创建者UUID:"+getPara("createuser"));
    	}
    	
    	if(!StringUtil.isNullOrEmpty(getPara("dateStart"))){
    		sb.append(",结束时间起:"+getPara("dateStart"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("dateEnd"))){
    		sb.append(",结束时间止:"+getPara("dateEnd"));
    	}
    
    	if(sb != null && sb.length()>0){
    		paralog = sb.substring(1);
    	}
    	return paralog;
    }
	
}
