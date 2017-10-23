package com.illumi.oms.stat.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
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
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/stat/league" ,viewPath=UrlConfig.STAT)
public class LeagueController extends EasyuiController<Record>
{
	private static final Logger log = Logger.getLogger(LeagueController.class);
	
	public void list()
	{
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		DataGrid<Record> dg = RecordUtil.listByDataGridByWhereAndFrom(SqlKit.sql("stat.league.getLeagueList"), getDataGrid(), getForm());
		for(Record rd:dg.rows){
//			long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.league.getLeagueCurNum"), rd.getLong("leagueid"));
//			rd.set("members", curNum);
			List<Record> bnRD = Db.use(Consts.DB_POKER2).findByCache("getPokerNumForLeague", getKey(rd.getLong("leagueid"),7), SqlKit.sql("stat.league.getPokerNumForLeague"), getParas(rd.getLong("leagueid"),7));
			if(bnRD != null && bnRD.size() > 0){
				rd.set("billNum", bnRD.get(0).getLong("billnum"));
			}
//			long billNum = Db.use(Consts.DB_POKER2).queryLong(SqlKit.sql("stat.league.getPokerNumForLeague"), getParas(rd.getLong("leagueid"),7));
//			rd.set("billNum", billNum);
		}
		renderJson(dg);
	}
	private Form getForm(){
		Form f = getFrom("");
		f.setFromParm("tli.leaguename", getPara("leaguename"));
		f.fuzzySerach.add("tli.leaguename");
		if(getPara("clubid")!=null&&!"".equals(getPara("clubid"))){
			f.setFromParm("tli.leagueid", "select leagueid from t_league_member_info where memberclubid = "+ getPara("clubid"));
			f.inValue.add("tli.leagueid");
		}else{
			f.setFromParm("tli.leagueid", getPara("leagueid"));
			f.integerValue.add("tli.leagueid");
		}
		
		return f;
	}
	
	public void leagueInfo(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int leagueid = getParaToInt("id");
		Record rd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.league.getLeagueByLeagueid"),leagueid);
		long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.league.getLeagueCurNum"), leagueid);
		rd.set("curNum", curNum);
		setAttr("rd", rd);
	}

	public void memberlist(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int leagueid = getParaToInt("leagueid");
		DataGrid<Record> dg = getDataGrid();
		dg = RecordUtil.listByDataGrid(Consts.DB_POKER, "stat.league.getMemberListByLeagueid", dg, leagueid);
		for(Record rd:dg.rows){
			long curNum = Db.use(Consts.DB_POKER).queryLong(SqlKit.sql("stat.player.getClubCurNum"), rd.getInt("clubid"));
			rd.set("members", curNum);
		}
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
		String sql = RecordUtil.getLimitSort(SqlKit.sql("stat.league.getPokerListForLeague"),dg);
		List<Record> rdList = Db.use(Consts.DB_POKER2).find(sql,paras);
		for(Record rd:rdList){
			Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), rd.getInt("createuser"));
			rd.set("nickname", user.getStr("nickname"));
		}
	    long total = (Db.use(Consts.DB_POKER2).queryLong(RecordUtil.getCountSql(SqlKit.sql("stat.league.getPokerListForLeague")), paras));
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
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.league.getDistinctBigblindForLeague"),paras);
		List<Record> list = Db.use("pokerdb2").find(SqlKit.sql("stat.league.getCountBigblindForLeague"),paras);
		renderGson(getChart(dd, list,"盲注","bigblind"));
		
	}

	public void gtimechart(){
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		Object[] paras = getParas();
		List<Record> dd = Db.use("pokerdb2").find(SqlKit.sql("stat.league.getDistinctGtimeForLeague"),paras);
		List<Record> list = Db.use("pokerdb2").find(SqlKit.sql("stat.league.getCountGtimeForLeague"),paras);
		renderGson(getChart(dd, list,"时长","gtime"));
	}
	
	//添加靓号
//	@Before(value = {ClubAddValidator.class})
//	public void add()
//	{
//		log.info(StringUtil.report(this.keepModel(this.getClass())));
//		Integer flag = 0;
//		try{
//			Record crd = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.club.getClubByClubid"),getPara("clubid"));
//			if(crd != null){
//				flag = 2;//俱乐部ID已经被占用
//			}else{
//				Record temp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByPhoneAndUuid"),getPara("strid"),getPara("uuid"));
//				if(temp==null){
//					flag = 3;//俱乐部创建者信息错误
//				}else{
//					int row_clubInfo = flag = Db.use("prodb").update(SqlKit.sql("stat.club.addClubInfo"),getPara("clubid"),getPara("clubname"),getPara("uuid"),getPara("maxmembers"));
//					if(row_clubInfo == 1){
//						int row_clubMemberInfo = Db.use("prodb").update(SqlKit.sql("stat.club.addClubMemberInfo"),getPara("clubid"),getPara("uuid"));
//						if(row_clubMemberInfo == 1){
//							flag = 1;
//						}else{
//							//俱乐部创建失败，请联系管理员
//							flag = 5;
//						}
//					}
//				}
//			}
//		}catch(Exception e){
//			log.error(e.getMessage());
//			flag = 4;//添加俱乐部靓号异常
//		}
//		renderJson(flag);
//	}

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
	
//	//判断该俱乐部是否被关注
//	private int getClubidbyUuid(int opertor,int clubid){
//		int strSql = 0;
//		List<Record> list = Db.find(SqlKit.sql("stat.club.getTraceClubid"),opertor,clubid);
//		
//		if(list!=null && list.size()>0){
//			strSql = 1;
//		}
//		return strSql;
//	}
	
	private Object[] getParas(){
		
		int clubid = getParaToInt("leagueid");
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
	
	private Object[] getParas(long id,int days) {
		DateTime dateStart = null;
		DateTime dateEnd = null;
		dateStart = DateTime.now().minusDays(days).withMillisOfDay(0);
		dateEnd = DateTime.now();
		Object[] obj = { id, dateStart.getMillis(), dateEnd.getMillis() };
		return obj;
	}
	private String getKey(long id,int days) {
		return days+"days"+id;
	}
	private String getParasLog(){
    	StringBuffer sb = new StringBuffer();
    	String paralog = "";
    	
    	if(!StringUtil.isNullOrEmpty(getPara("clubid"))){
    		sb.append(",俱乐部ID:"+getPara("clubid"));
    	}
    	if(!StringUtil.isNullOrEmpty(getPara("clubname"))){
    		sb.append(",俱乐部名字:"+getPara("clubname"));
    	}
    	
    	if(!StringUtil.isNullOrEmpty(getPara("nickname"))){
    		sb.append(",创建者昵称:"+getPara("nickname"));
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
