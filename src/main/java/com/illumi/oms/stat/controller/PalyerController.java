package com.illumi.oms.stat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.ResultInfo;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.plugin.spring.IocInterceptor;
import com.illumi.oms.service.RecordUtil;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/player" ,viewPath=UrlConfig.STAT)
public class PalyerController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(PalyerController.class);
	Log oLog = LogFactory.getLog("operation");
	public void list()
	{
//		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
//		log.info(user1.getStr("account")+"/"+user1.getName()+",玩家管理列表查询(/stat/player/list),请求参数/"+getParasLog());
		log.info(StringUtil.report(this.keepModel(this.getClass())));
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
			if(rd.getStr("pwd")==null || "".equals(rd.getStr("pwd")) || rd.getStr("pwd").equals("null")){
				rd.set("state", 1);
			}else{
				rd.set("state", 0);
				if(rd.getStr("pwd").length()>7){
					rd.set("pwd", rd.getStr("pwd").substring(0, 3)+"*******");
				}else if(rd.getStr("pwd").equals("1")){
					rd.set("pwd", "-");
				}
			}
		}
		renderJson(dg);
//		renderJson(RecordUtil.listByDataGrid(SqlKit.sql("stat.player.getPlayerList"), getDataGrid(), getForm()));
	}
	
	
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("uuid", getPara("uuid"));
		f.setFromParm("showid", getPara("showid"));
		f.setFromParm("nickname", getPara("nickname"));
		f.setFromParm("phonenumber", getPara("phonenumber"));
		f.setFromParm("countrycode", getPara("countrycode"));
		f.setFromParm("idtype", getPara("idtype"));
		f.fuzzySerach.add("nickname");
		f.integerValue.add("uuid");
		f.integerValue.add("phonenumber");
		return f;
	}
	
	public void playerInfo(){
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
//		log.info(user1.getStr("account")+"/"+user1.getName()+",玩家管理玩家查询(/stat/player/playerInfo),请求参数/"+getParaToInt("id"));
		log.info(StringUtil.report(this.keepModel(this.getClass())));

		int uuid = getParaToInt("id");
		Record rd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"),uuid);
		
		//加IP信息
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
		
		rd.set("myflag", getUUIDbyUuid(user1.getInt("uuid"),uuid));
		
		setAttr("rd", rd);
		
	}

	public void clublist(){
//		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
//		log.info(user1.getStr("account")+"/"+user1.getName()+",玩家管理玩家查询俱乐部查询(/stat/player/clublist),请求参数/"+getParaToInt("uuid"));
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int uuid = getParaToInt("uuid");
		
		List<Record> rdList = Db.use(Consts.DB_POKER).find(SqlKit.sql("stat.player.getClubForPlayer"),uuid);
		for(Record rd:rdList){
			long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.player.getClubCurNum"), rd.getInt("clubid"));
			rd.set("curNum", curNum);
//			rd.set("clublocation", Consts.locationMap.get(rd.getStr("clublocation")));
		}
		DataGrid<Record> dg = getDataGrid();
		dg.rows = rdList;
		
		renderJson(dg);
		
		
	}
	
	public void friendlist(){
//		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
//		log.info(user1.getStr("account")+"/"+user1.getName()+",玩家管理玩家查询好友查询(/stat/player/friendlist),请求参数/"+getParaToInt("uuid"));
	    log.info(StringUtil.report(this.keepModel(this.getClass())));
		int uuid = getParaToInt("uuid");
		DataGrid<Record> dg = getDataGrid();
		renderJson(RecordUtil.listByDataGrid(Consts.DB_POKER, "stat.player.getFriendForPlayer", dg, uuid));
		
		
	}
	
	public void commonlist(){
//		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
//		log.info(user1.getStr("account")+"/"+user1.getName()+",玩家管理玩家查询普通局查询(/stat/player/commonlist),请求参数/"+getParasLog());
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Object[] paras = getParas();
		//牌局列表
		DataGrid<Record> dg = getDataGrid();
		String sql = RecordUtil.getLimitSort(SqlKit.sql("stat.player.getCommonForPlayer"),dg);
		List<Record> rdList = Db.use(Consts.DB_POKER2).find(sql,paras);
		for(Record rd:rdList){
			Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("createuser"));
			rd.set("nickname", user.getStr("nickname"));
			rd.set("showid", user.getStr("showid"));
		}
	    long total = (Db.use(Consts.DB_POKER2).queryLong(RecordUtil.getCountSql(SqlKit.sql("stat.player.getCommonForPlayer")), paras));
		dg.rows = rdList;
		dg.total = (int)total;	

		renderJson(dg);
		
	}
	
	public void countCommon(){
		
		ResultInfo rf = new ResultInfo();
		rf.setCode(200);
		try{
			Map<String,Object> map = new HashMap<String,Object> ();
			Object[] paras = getParas();
			//统计牌局数
			List<Record> rdlist =  Db.use(Consts.DB_POKER2).find(SqlKit.sql("stat.player.getCommonTotalRoomType"),paras);
			for(Record rd:rdlist){
				if(rd.get("createroomtype")!=null){
					if(rd.getInt("createroomtype")==Consts.POKEY_TYPE_GROUP){
						map.put("totalGroup",rd.getLong("total"));
					}
					if(rd.getInt("createroomtype")==Consts.POKEY_TYPE_CLUB){
						map.put("totalClub",rd.getLong("total"));
					}
					if(rd.getInt("createroomtype")==Consts.POKEY_TYPE_FAST){
						map.put("totalFast",rd.getLong("total"));
					}
				}
			}
			//统计盈亏
			map.put("totalBonus", Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.player.getCommonTotalBonus"),paras));
			rf.setObj(map);
		}catch(Exception e){
			e.printStackTrace();
			rf.setCode(500);
			rf.setMsg("查找异常");
		}
		
		renderJson(rf);
	}
	
	
	public void bonuschart(){
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.player.getCommonBonusChart"),getParas());
		Chart chart = new Chart();
		List<Long> series = new ArrayList<Long>();
		for (Record event : dd)
		{
			chart.categories.add(DateUtil.format(event.getDate("date"),"MM-dd"));
			series.add(event.getBigDecimal("num").longValue());
		}
		chart.setSeriesDate("每日盈亏", "spline",series);
		renderGson(chart);
	}
	
	
	public void pokerchart(){
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.player.getCommonPokerChart"),getParas());
		Chart chart = new Chart();
		List<Long> series = new ArrayList<Long>();
		List<Long> series2 = new ArrayList<Long>();
		List<Long> series3 = new ArrayList<Long>();
		for (Record event : dd)
		{
			chart.categories.add(DateUtil.format(event.getDate("date"),"MM-dd"));
			series.add(event.getLong("grp"));
		}
		for (Record event : dd)
		{
			series2.add(event.getLong("club"));
		}
		
		for (Record event : dd)
		{
			series3.add(event.getLong("fst"));
		}
		chart.setSeriesDate("圈子局", "column",series);
		chart.setSeriesDate("俱乐部局", "column",series2);
		chart.setSeriesDate("快速局", "column",series3);
		
		renderGson(chart);
	}
	
	private Object[] getParas(){
		
		int uuid = getParaToInt("uuid");
		DateTime dateStart = null;
		DateTime dateEnd = null;
		
		if(StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTime.now().minusDays(29).withMillisOfDay(0);
			dateEnd = DateTime.now();
			
		}else if(StrKit.isBlank(getPara("dateStart"))&&!StrKit.isBlank(getPara("dateEnd"))){
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(getPara("dateEnd"));
			dateStart = dateEnd.minusDays(29).withMillisOfDay(0);
					
		}else if(!StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(getPara("dateStart"));
			dateEnd = DateTime.now();
		}else{
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(getPara("dateStart"));
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(getPara("dateEnd"));
		}
		

		Object[] obj = {uuid,dateStart.getMillis(),dateEnd.getMillis()};
		return obj;
	}
	
	private int getUUIDbyUuid(int opertor,int uuid){
		int strSql = 0;
		List<Record> list = Db.find(SqlKit.sql("stat.player.getTraceUuid"),opertor,uuid);
		
		if(list!=null && list.size()>0){
			strSql = 1;
		}
		return strSql;
	}
	
	public void setPwd() {
        log.info(StringUtil.report(this.keepModel(this.getClass())));
        User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
        int state = 0;
        try {
            Record toUser = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), getParaToInt("uuid"));
            if(toUser.getStr("pwd")==null || "".equals(toUser.getStr("pwd")) || toUser.getStr("pwd").equals("null")){
                int row = Db.use("prodb").update(SqlKit.sql("stat.player.resetUserPwd"),getPara("pwd").trim(), getParaToInt("uuid"));
                if(row != 1){
                    state = 2;
                }else{
                    //重置用户密码设置日志 1101 ＋ 操作人UUID ＋ uuid ＋ 设置的MD5
                    oLog.error(" 1101 " + user.getInt("uuid") + " " + getPara("pwd") + " " +getParaToInt("uuid")); 
                }
            }else{
                //用户密码不为空，无法重置
                state = 1;
            }
        } catch (Throwable e) {
            // 服务异常
            state = 3;
            e.printStackTrace();
        }
        renderText("{\"code\":" + state + "}");
    }
	
    public void ticketlist() {
        log.info(StringUtil.report(this.keepModel(this.getClass())));
        DataGrid<Record> dg = RecordUtil.listByDataGridByWhereAndFrom(
                SqlKit.sql("mtt.ticket.getRemainNumByUuid"), getDataGrid(), getticketForm());
        for (Record rd : dg.rows) {
            rd.set("expirationtime",
                    StringUtil.timeStampToDateNormal(rd.getLong("expirationtime")));
            rd.set("belonguuid", getPara("uuid"));
        }
        renderJson(dg);
    }

    private Form getticketForm() {
        Form f = getFrom("");
        f.setFromParm("a.belonguuid", getPara("uuid"));
        return f;
    }
    
	
}
