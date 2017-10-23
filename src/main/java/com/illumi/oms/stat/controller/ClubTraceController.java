package com.illumi.oms.stat.controller;

import java.util.List;

import org.joda.time.DateTime;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
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

@ControllerBind(controllerKey = "/stat/clubtrace", viewPath = UrlConfig.STAT)
public class ClubTraceController extends EasyuiController<Record> {
	private static final Logger log = Logger.getLogger(ClubTraceController.class);

	public void list() {
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		DataGrid<Record> dg = RecordUtil.listByDataGrid(SqlKit.sql("stat.club.getClubList"), getDataGrid(), getForm());
		for (Record rd : dg.rows) {
			
			List<Record> bnRD = Db.use(Consts.DB_POKER2).findByCache("getPokerNumForClub", getKey(rd.getInt("clubid"),7,30), SqlKit.sql("stat.club.getPokerNumForClub"), getParas(rd.getInt("clubid"), 7,30));
			if(bnRD != null && bnRD.size() > 0){
				rd.set("weekNum",bnRD.get(0).getLong("weeknum"));
				rd.set("monthNum",bnRD.get(0).getLong("monthnum")+bnRD.get(0).getLong("weeknum"));
			}
			
//			Record numRecord = Db.use("pokerdb2").findFirst(SqlKit.sql("stat.club.getPokerNumForClub"),getParas(rd.getInt("clubid"), 7,30));
//			rd.set("weekNum",numRecord.getLong("weeknum"));
//			rd.set("monthNum",numRecord.getLong("monthnum")+numRecord.getLong("weeknum"));
			
//			rd.set("clublocation", Consts.locationMap.get(rd.getStr("clublocation")));
		}
		renderJson(dg);
	}

	private Form getForm() {
		Form f = getFrom("");
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		if (StringUtil.isBlank(getPara("clubid"))) {
			f.setFromParm("clubid", getClublistbyUuid(user.getInt("uuid")));
			f.inValue.add("clubid");
		} else if (StringUtil.isNotBlank(getClubidbyUuid(user.getInt("uuid"), getPara("clubid")))) {
			f.setFromParm("clubid", getPara("clubid"));
			f.integerValue.add("clubid");
		} else {
			f.setFromParm("clubid", 0 + "");
			f.inValue.add("clubid");
		}
		f.setFromParm("clubname", getPara("clubname"));
		f.fuzzySerach.add("clubname");
		return f;
	}

	// 关注
	public void start() {
		boolean flag = false;
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.club.insertTraceClubid"), getParaToInt("id"), user.getInt("uuid"));
		if (row == 1) {
			flag = true;
		}
		renderJsonResult(flag);
	}

	// 取消关注
	public void end() {
		boolean flag = false;
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		log.info(StringUtil.report(this.keepModel(this.getClass())));
		int row = 0;
		row = Db.update(SqlKit.sql("stat.club.deleteTraceClubid"), getParaToInt("id"), user.getInt("uuid"));
		if (row == 1) {
			flag = true;
		}
		renderJsonResult(flag);
	}

	private String getClublistbyUuid(int uuid) {
		List<Record> list = Db.find(SqlKit.sql("stat.club.getTraceClubidList"), uuid);
		String strSql = "";
		for (Record rd : list) {
			strSql += "'" + rd.getInt("clubid") + "',";
		}
		if (strSql.endsWith(",")) {
			strSql = strSql.substring(0, strSql.length() - 1);
		}
		if (StringUtil.isBlank(strSql)) {
			strSql = 0 + "";
		}
		return strSql;
	}
	private String getKey(long id,int days,int days2) {
		return days+""+days2+"days"+id;
	}
	private String getClubidbyUuid(int opertor, String clubid) {
		List<Record> list = Db.find(SqlKit.sql("stat.club.getTraceClubid"), opertor, clubid);
		String strSql = "";
		for (Record rd : list) {
			strSql += "'" + rd.getInt("clubid") + "',";
		}
		if (strSql.endsWith(",")) {
			strSql = strSql.substring(0, strSql.length() - 1);
		}
		return strSql;
	}

	private Object[] getParas(int clubid,int days,int bigdays) {
		DateTime dateStart = null;
		DateTime dateBigStart = null;
		DateTime dateEnd = null;
		dateStart = DateTime.now().minusDays(days).withMillisOfDay(0);
		dateBigStart = DateTime.now().minusDays(bigdays).withMillisOfDay(0);
		dateEnd = DateTime.now();
		Object[] obj = { clubid, dateStart.getMillis(), dateEnd.getMillis(),clubid, dateBigStart.getMillis(), dateStart.getMillis() };
		return obj;
	}
}
