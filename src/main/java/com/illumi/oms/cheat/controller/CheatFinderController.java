package com.illumi.oms.cheat.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.illumi.oms.cheat.model.Json.CheatGroup;
import com.illumi.oms.cheat.model.Json.CheatInfo;
import com.illumi.oms.cheat.model.Json.CheatResponse;
import com.illumi.oms.cheat.service.CheatFinderService;
import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.system.model.User;
import com.jayqqaa12.jbase.jfinal.ext.ShiroExt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/cheatfinder", viewPath = UrlConfig.DATA)
public class CheatFinderController extends EasyuiController<Record>{
	private static final Logger logger = Logger.getLogger(CheatFinderController.class);
	
	public void findCheatByIds() {
		logger.info(StringUtil.report(this.keepModel(this.getClass())));
	    String ids = getPara("ids");    
	    User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
	    logger.debug("CheatFinder | " + user.getInt("uuid") + " | " + getSession().getId() + " | " + ids + " | ");
	    if (StrKit.isBlank(ids)) {
	      renderJson("1");
	      logger.warn("CheatFinderRequest ids is empty | " + user.getInt("uuid") + " | " + getSession().getId());
	      return;
	      
	    }
	    String[] idArr = ids.split(",");
	    String response = CheatFinderService.findCheatByIds(user.getInt("uuid"), idArr);
	    logger.info("查伙牌response: "+response);
	   // String response="{\"cheatInfo\":[{\"user_01\":\"73848\",\"user_02\":\"73836\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73848\",\"user_02\":\"10260\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73848\",\"user_02\":\"203270\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73848\",\"user_02\":\"341659\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73848\",\"user_02\":\"341604\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73836\",\"user_02\":\"10260\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73836\",\"user_02\":\"203270\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73836\",\"user_02\":\"341659\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"73836\",\"user_02\":\"341604\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"10260\",\"user_02\":\"203270\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"10260\",\"user_02\":\"341659\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"10260\",\"user_02\":\"341604\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"203270\",\"user_02\":\"341659\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"203270\",\"user_02\":\"341604\",\"score\":\"84\",\"times_sames_in_month\":\"10\"},{\"user_01\":\"341659\",\"user_02\":\"341604\",\"score\":\"84\",\"times_sames_in_month\":\"10\"}],\"cheatGroup\":[{\"groups\":1,\"uuid\":\"73848,73836,10260,203270,341659,341604\"}],\"playTimes\":[{\"uuid\":\"73848\",\"play_times\":\"50\",\"record_times\":\"3\"},{\"uuid\":\"73836\",\"play_times\":\"50\",\"record_times\":\"22\"},{\"uuid\":\"10260\",\"play_times\":\"50\",\"record_times\":\"48\"},{\"uuid\":\"203270\",\"play_times\":\"50\",\"record_times\":\"3\"},{\"uuid\":\"341659\",\"play_times\":\"50\",\"record_times\":\"3\"},{\"uuid\":\"73836\",\"play_times\":\"40\",\"record_times\":\"22\"},{\"uuid\":\"10260\",\"play_times\":\"40\",\"record_times\":\"48\"},{\"uuid\":\"203270\",\"play_times\":\"40\",\"record_times\":\"3\"},{\"uuid\":\"341659\",\"play_times\":\"40\",\"record_times\":\"3\"},{\"uuid\":\"341604\",\"play_times\":\"40\",\"record_times\":\"3\"}]} ";
	    List<Record> userInfo = new ArrayList<Record>();
	    for (String id : idArr) {
//	      Record userTemp = Db.use("pokerdb").findFirst(PropKit.use("sql.txt").get("getUserByUuid"), id);
	      Record userTemp = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), id);
	      if (userTemp != null) {
	        userInfo.add(userTemp);  
	      }
	    }
	    if (StrKit.isBlank(response)) {
	      renderJson("2");
	      logger.warn("CheatFinderResponse | " + user.getInt("uuid")  + " | " + getSession().getId());
	      return;
	    }
        
	    Gson json = new Gson();
	    try {
	      CheatResponse resp = json.fromJson(response, CheatResponse.class); 
	      logger.debug("CheatFinder | " + user.getInt("uuid")  + " | " + getSession().getId() + " | Parse Cheat Reponse Complete");
	      resp.setUserInfo(userInfo);

	      resp.setStartTime(System.currentTimeMillis());
	      resp.setEndTime(System.currentTimeMillis() - 86400 * 1000 * 60l);
          
	      renderJson(resp.getIntroducation());
	    } catch (Exception e) {
	      logger.warn("Err042 | " + user.getInt("uuid")  + " | Error parse Json : " + response + " | " + getSession().getId()
	          + " | Error message is " + e.getMessage());
	      e.printStackTrace();
	    }
	  }
	
}
