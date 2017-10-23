package com.illumi.oms.stat.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.MoveClubUserInfo;
import com.illumi.oms.plugin.spring.IocInterceptor;
import com.illumi.oms.service.EmailService;
import com.illumi.oms.service.RecordUtil;
import com.illumi.oms.system.model.Chart;
import com.illumi.oms.system.model.User;
import com.illumi.oms.system.validator.ClubAddValidator;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.jbase.util.DateUtil;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/club" ,viewPath=UrlConfig.STAT)
public class ClubController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(ClubController.class);
	Log oLog = LogFactory.getLog("operation");
	public void list()
	{
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		DataGrid<Record> dg = RecordUtil.listByDataGrid(SqlKit.sql("stat.club.getClubList"), getDataGrid(), getForm());
//		for(Record rd:dg.rows){
//				rd.set("clublocation", Consts.locationMap.get(rd.getStr("clublocation")));
//		}
		renderJson(dg);
	}
	
	
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("clubid", getPara("clubid"));
		f.setFromParm("clubname", getPara("clubname"));
		f.fuzzySerach.add("clubname");
		f.integerValue.add("clubid");
		return f;
	}
	
	public void clubInfo(){
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int clubid = getParaToInt("id");
		Record rd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.club.getClubByClubid"),clubid);
		long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.player.getClubCurNum"), clubid);
		rd.set("myflag", getClubidbyUuid(user.getInt("uuid"), clubid));
		rd.set("curNum", curNum);
//		rd.set("clublocation", Consts.locationMap.get(rd.getStr("clublocation")));
		setAttr("rd", rd);
	}

	public void memberlist(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int clubid = getParaToInt("clubid");
		DataGrid<Record> dg = RecordUtil.listByDataGrid(Consts.DB_POKER, "stat.club.getMemberListByClubid", getDataGrid(), clubid);
		for(Record rd:dg.rows){
		    Record udrd = Db.findFirst(SqlKit.sql("stat.player.getIpInfoByUuid"),rd.getInt("memberuuid"));
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
	}
	public void leaguelist(){
		User user1 = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(user1.getStr("account")+"/"+user1.getName()+",俱乐部管理俱乐部详情他的联盟(/stat/club/leaguelist),请求参数/"+getParaToInt("clubid"));
		int clubid = getParaToInt("clubid");
		List<Record> rdList = Db.use(Consts.DB_POKER).find(SqlKit.sql("stat.club.getLeagueForClub"),clubid);
		for(Record rd:rdList){
			long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.league.getLeagueCurNum"), rd.getLong("leagueid"));
			rd.set("members", curNum);
		}
		DataGrid<Record> dg = getDataGrid();
		dg.rows = rdList;
		renderJson(dg);
	}
	public void toplist(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int clubid = getParaToInt("clubid");
		
		DataGrid<Record> dg = getDataGrid();
		List<Record> memberList = Db.use(Consts.DB_POKER).find(SqlKit.sql("stat.club.getMemberlistByClubid"), clubid);
		for(Record rd:memberList){
			BigDecimal bonus =  Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.club.getToplistByClubid"), getParas2(rd.getInt("uuid")));
			rd.set("bonus", bonus==null?0:bonus);
		}
	
		dg.rows = memberList;
		renderJson(dg);
		//renderJson(RecordUtil.listByDataGrid(Consts.DB_POKER, "stat.club.getToplistByClubid", dg, getParas()));
	}

	public void pokerlist(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Object[] paras = getParas();
		//牌局列表
		DataGrid<Record> dg = getDataGrid();
		String sql = RecordUtil.getLimitSort(SqlKit.sql("stat.club.getPokerListForClub"),dg);
		List<Record> rdList = Db.use(Consts.DB_POKER2).find(sql,paras);
		for(Record rd:rdList){
			Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("createuser"));
			rd.set("nickname", user.getStr("nickname"));
			rd.set("showid", user.getStr("showid"));
		}
	    long total = (Db.use(Consts.DB_POKER2).queryLong(RecordUtil.getCountSql(SqlKit.sql("stat.club.getPokerListForClub")), paras));
		dg.rows = rdList;
		dg.total = (int)total;	

		renderJson(dg);
	}
	
	public void memberchart(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int clubid = getParaToInt("clubid");
	
		Record dd = Db.use("pokerdb").findFirst(SqlKit.sql("stat.club.getMemberVipTypeByClubid"),clubid);
		Chart chart = new Chart();
		List<Object[]> series = new ArrayList<Object[]>();

		
		Object[] aa = {"蓝卡",dd.getLong("blue")};
		Object[] aa1 = {"黄金卡",dd.getLong("gold")};
		Object[] aa2 = {"白金卡",dd.getLong("platinum")};
		Object[] aa3 = {"黑卡",dd.getLong("black")};
		
		series.add(aa);
		series.add(aa1);
		series.add(aa2);
		series.add(aa3);


		chart.setSeriesDate("Member VIP  share", "pie",series);
		
		renderGson(chart);
	}
	
	public void blindchart(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Object[] paras = getParas();
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.club.getDistinctBigblind"),paras);
		List<Record> list = Db.use("pokerdb2").find(SqlKit.sql("stat.club.getCountBigblind"),paras);
		renderGson(getChart(dd, list,"盲注","bigblind"));
		
	}

	public void gtimechart(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Object[] paras = getParas();
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.club.getDistinctGtime"),paras);
		List<Record> list = Db.use("pokerdb2").find(SqlKit.sql("stat.club.getCountGtime"),paras);
		renderGson(getChart(dd, list,"时长","gtime"));
		
	}

	private Chart getChart(List<Record> typelist, List<Record> datalist,String chartName,String columnName) {
		Set<String> set = new LinkedHashSet<String>();
		Chart chart = new Chart();
		for(Record rd:datalist){
			set.add(DateUtil.format(rd.getDate("date"),"yyyy-MM-dd"));
		}
		
		for(Record rd:typelist){
			
			List<Long> series = new ArrayList<Long>();
					    
			Iterator<String> it=set.iterator();
	        while(it.hasNext()){
	        	long num = 0;
	        	String dateStr = it.next();
	        	for(Record temp:datalist){

	        		if(rd.get("type").toString().equals(temp.get(columnName).toString())&&dateStr.equals(DateUtil.format(temp.getDate("date"),"yyyy-MM-dd"))){
	        			num = temp.getLong("num");
	        			break;
	        		}
	        	}
	        	series.add(num);

	        }
			
			chart.setSeriesDate(chartName+rd.get("type").toString(), "column",series);
		}
		
		chart.categories = new ArrayList<String>(set);
		return chart;
	}
	
	//判断该俱乐部是否被关注
	private int getClubidbyUuid(int opertor,int clubid){
		int strSql = 0;
		List<Record> list = Db.find(SqlKit.sql("stat.club.getTraceClubid"),opertor,clubid);
		
		if(list!=null && list.size()>0){
			strSql = 1;
		}
		return strSql;
	}
	
	private Object[] getParas(){
		
		int clubid = getParaToInt("clubid");
		DateTime dateStart = null;
		DateTime dateEnd = null;
		
		if(StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTime.now().minusDays(29).withMillisOfDay(0);
			dateEnd = DateTime.now();
			
		}else if(StrKit.isBlank(getPara("dateStart"))&&!StrKit.isBlank(getPara("dateEnd"))){
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateEnd")+" 23:59:59");
			dateStart = dateEnd.minusDays(29).withMillisOfDay(0);
					
		}else if(!StrKit.isBlank(getPara("dateStart"))&&StrKit.isBlank(getPara("dateEnd"))){
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateStart")+ " 00:00:00");
			dateEnd = DateTime.now();
		}else{
			dateStart = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateStart")+ " 00:00:00");
			dateEnd = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(getPara("dateEnd")+" 23:59:59");
		}
		Object[] obj = {clubid,dateStart.getMillis(),dateEnd.getMillis()};
		return obj;
	}

	private Object[] getParas2(int uuid){
		
		int clubid = getParaToInt("clubid");
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
		Object[] obj = {clubid,uuid,dateStart.getMillis(),dateEnd.getMillis()};
		return obj;
	}

}
