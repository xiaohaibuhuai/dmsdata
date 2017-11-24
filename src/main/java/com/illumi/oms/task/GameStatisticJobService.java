package com.illumi.oms.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.illumi.oms.common.Consts;
import com.illumi.oms.data.model.BlindSnapShotDate;
import com.illumi.oms.data.model.GameSnapShotDate;
import com.illumi.oms.data.utils.DateUtils;
import com.jayqqaa12.jbase.jfinal.ext.model.Db;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

public class GameStatisticJobService implements Job{

	private static final Logger log = Logger.getLogger(GameStatisticJobService.class);
	private  String TYPE_GAME="g";
	private  String TYPE_PLAYER="p";
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("记录每日开局快照任务开始...");
		 //获取昨天时间
         //long zeroTime = 1508256000000l;
         long zeroTime = DateUtils.getCurrentZeroTime();
		 long startTime = DateUtils.changeHour(zeroTime, -24);
		 //记录每日局数统计快照
         statGameStatistic(startTime,zeroTime);
        //定时任务循环
        //defineExcuteByDay(startTime,zeroTime,30);
        
        
		log.info("记录每日开局快照任务结束...");
		
	}
	
	private void statGameStatistic(long startTime,long zeroTime) {
		try {
			 //牌局表
			 gameDataHandle(startTime,zeroTime);
			 
			 //盲注表
			 blindDataHandle(startTime,zeroTime);	
		} catch (Exception e) {
			log.error("定时任务统计联盟数据出错：", e);
		}
		
	}

	private void gameDataHandle(long startTime, long zeroTime) {
		//t_game_daily_snapshot 快照表
		  //1 获取昨天整天 开局数统计
		 List<Record>  gameTypeList  = new ArrayList<Record>();
		  //2 获取昨天整天 有效开局统计
		 List<Record>  gameTypeVaildList  = new ArrayList<Record>();
		  //3 获取昨天SNG 开局统计
		 Long  gameSNGCount  = null;
		  //4 获取昨天SNG 有效开局统计
		 Long  gameSNGVaildCount  = null;
		 //5 获取昨天 开局活跃玩家统计    (all)   buystacks>0
		 List<Record>  playerList  = new ArrayList<Record>();
		 //6 获取昨天 SNG 活跃玩家统计    (all)
		 Long  playerSNGCount  = null;
		 
		 gameTypeList  = Db.use(Consts.DB_POKER2).find(SqlKit.sql("data.billSnap.getGameTypeList"),new Object[] {startTime,zeroTime});
		
		 gameTypeVaildList = Db.use(Consts.DB_POKER2).find(SqlKit.sql("data.billSnap.getGameTypeVaildList"),new Object[] {startTime,zeroTime});
		
		 gameSNGCount = Db.use(Consts.DB_POKER2).queryLong(SqlKit.sql("data.billSnap.getGameSNGCount"),new Object[] {startTime,zeroTime});
		
		 gameSNGVaildCount = Db.use(Consts.DB_POKER2).queryLong(SqlKit.sql("data.billSnap.getGameSNGVaildCount"),new Object[] {startTime,zeroTime});
		
		 playerList = Db.use(Consts.DB_POKER2).find(SqlKit.sql("data.billSnap.getPlayerList"),new Object[] {startTime,zeroTime});
		 playerSNGCount = Db.use(Consts.DB_POKER2).queryLong(SqlKit.sql("data.billSnap.getplayerSNGCount"),new Object[] {startTime,zeroTime});
		 //1 封装普通局   isvalid=0   (包含人数)
		 GameSnapShotDate gameSnapShotDate = new GameSnapShotDate();
		 //是否有效
		 gameSnapShotDate.set("isvalid", 0); //0 全部  1 有效
		 //时间
		 gameSnapShotDate.set("targetdate",startTime);
		 //封装各种局统计数量
		 setDate2GameSnapShot(gameSnapShotDate,gameTypeList,TYPE_GAME);
		 //封装SNG局数统计
		 gameSnapShotDate.set("g_sng", gameSNGCount);
		 //封装各种局活跃人数统计
		 setDate2GameSnapShot(gameSnapShotDate,playerList,TYPE_PLAYER);
		 //封装SNG局活跃人数
		 gameSnapShotDate.set("p_sng", playerSNGCount);

		 //2封装有效局数 isvalid=1   ( 不包含人数)
		 GameSnapShotDate gameValidSnapShotDate = new GameSnapShotDate();
		 //是否有效
		 gameValidSnapShotDate.set("isvalid", 1); //0 全部  1 有效
		 //时间
		 gameValidSnapShotDate.set("targetdate",startTime);
		 //封装各种局统计数量  有效
		 setDate2GameSnapShot(gameValidSnapShotDate,gameTypeVaildList,TYPE_GAME);
		 //封装SNG有效数
		 gameValidSnapShotDate.set("g_sng", gameSNGVaildCount);
		 
		 boolean isuccess1 = gameSnapShotDate.saveAndCreateDate();
		 boolean isuccess2 = gameValidSnapShotDate.saveAndCreateDate();
		 
		 log.info("t_game_daily_snapshot[isvalid 0 :"+isuccess1+"|| isValid 1:"+isuccess2+"]");

	}
	
	
	
	//盲注
	private void blindDataHandle(long startTime, long zeroTime) {
		 //1 普通局   盲注数 和 活跃人数
		 List<Record>  blindNumList1  = new ArrayList<Record>();
		 List<Record>  playerblindNumList1  = new ArrayList<Record>();
		 String sql = SqlKit.sql("data.billSnap.getBlindNumByType");
		 String playsql = SqlKit.sql("data.billSnap.getBlindPlayerNumByType");
		 int target = 1;
		 blindNumList1 = Db.use(Consts.DB_POKER2).find(sql,new Object[] {startTime,zeroTime,target});
		 playerblindNumList1 = Db.use(Consts.DB_POKER2).find(playsql,new Object[] {startTime,zeroTime,target});
		 //2 普通保险局
		 List<Record>  blindNumList3  = new ArrayList<Record>();
		 List<Record>  playerblindNumList3  = new ArrayList<Record>();
		 target = 3;
		 blindNumList3 = Db.use(Consts.DB_POKER2).find(sql,new Object[] {startTime,zeroTime,target});
		 playerblindNumList3 = Db.use(Consts.DB_POKER2).find(playsql,new Object[] {startTime,zeroTime,target});
		 //3 奥马哈
		 List<Record>  blindNumList5  = new ArrayList<Record>();
		 List<Record>  playerblindNumList5  = new ArrayList<Record>();
		 target = 5;
		 blindNumList5 = Db.use(Consts.DB_POKER2).find(sql,new Object[] {startTime,zeroTime,target});
		 playerblindNumList5 = Db.use(Consts.DB_POKER2).find(playsql,new Object[] {startTime,zeroTime,target});
		 //4 奥马哈保险局
		 List<Record>  blindNumList6  = new ArrayList<Record>();
		 List<Record>  playerblindNumList6  = new ArrayList<Record>();
		 target = 6;
		 blindNumList6 = Db.use(Consts.DB_POKER2).find(sql,new Object[] {startTime,zeroTime,target});
		 playerblindNumList6 = Db.use(Consts.DB_POKER2).find(playsql,new Object[] {startTime,zeroTime,target});
		 
		 
		 
		 //封装数据
		 BlindSnapShotDate blindNormal = new BlindSnapShotDate();
		 BlindSnapShotDate blindNormalVaild = new BlindSnapShotDate();
		 BlindSnapShotDate blindOmaha = new BlindSnapShotDate();
		 BlindSnapShotDate blindOmahaVaild = new BlindSnapShotDate();
		 
		 //普通局
	
		 blindNormal.set("targetdate", startTime);
		 blindNormal.set("gameroomtype", 1);
		 setDate2blindSnapShot(blindNormal,blindNumList1,TYPE_GAME);
		 setDate2blindSnapShot(blindNormal,playerblindNumList1,TYPE_PLAYER);
		 
		 boolean isuccess1 = blindNormal.saveAndCreateDate();
		 log.info("blindNormal:"+isuccess1);
		 
		 //普通保险局
	
		 blindNormalVaild.set("targetdate", startTime);
		 blindNormalVaild.set("gameroomtype", 3);
		 setDate2blindSnapShot(blindNormalVaild,blindNumList3,TYPE_GAME);
		 setDate2blindSnapShot(blindNormalVaild,playerblindNumList3,TYPE_PLAYER);
		 
		 boolean isuccess3 = blindNormalVaild.saveAndCreateDate();
		 
		 
		 
		 //奥马哈

		 blindOmaha.set("targetdate", startTime);
		 
		 blindOmaha.set("gameroomtype", 5);
		 setDate2blindSnapShot(blindOmaha,blindNumList5,TYPE_GAME);
		 setDate2blindSnapShot(blindOmaha,playerblindNumList5,TYPE_PLAYER);
		 
		 boolean isuccess5 = blindOmaha.saveAndCreateDate();
		 
		 
		 
		 //奥马哈保险局
		
		 blindOmahaVaild.set("targetdate", startTime);
		 blindOmahaVaild.set("gameroomtype", 6);
		 setDate2blindSnapShot(blindOmahaVaild,blindNumList6,TYPE_GAME);
		 setDate2blindSnapShot(blindOmahaVaild,playerblindNumList6,TYPE_PLAYER);
		 
		 boolean isuccess6 = blindOmahaVaild.saveAndCreateDate();
		 log.info("t_blinds_daily_snapshot["+"blindNormal:"+isuccess1+"|| blindNormalVaild:"+isuccess3+"|| blindOmaha:"+isuccess5+"|| blindOmahaVaild:"+isuccess6+"]");
	}

	private void setDate2blindSnapShot(BlindSnapShotDate blind, List<Record> list,String type) {
		for(Record record:list) {
			 Integer  blindType = record.getInt("bigblind");
			 String resultype = paserBlindType(blindType,type);
			 Long num = record.getLong("num");
			 if(resultype!=null&&num!=null) {
				 blind.set(resultype, num);
			 }
			// log.error("获取blind数据出错");
		}
		
	}
	
	/**  
	 * @param gameSnapShotDate
	 * @param list
	 */
	private void setDate2GameSnapShot(GameSnapShotDate gameSnapShotDate, List<Record> list,String type) {
		for(Record record:list) {
			 Integer  gameroomtype = record.getInt("gameroomtype");
			 String resultype = paserGameroomtype(gameroomtype,type);
			 Long num = record.getLong("num");
			 if(resultype!=null&&num!=null) {
			 gameSnapShotDate.set(resultype, num);
			 }
			// log.error("获取数据出错");
		 }
		
	}
	
	// 盲注类型解析
	private String paserBlindType(Integer blindNum,String type) {
		switch (blindNum) {
		case 2:  return type+"_b2";
		case 4:  return type+"_b4";
		case 10:  return type+"_b10";
		case 20:  return type+"_b20";
		case 40:  return type+"_b40";
		case 50:  return type+"_b50";
		case 100:  return type+"_b100";
		case 200:  return type+"_b200";
		case 400:  return type+"_b400";
		case 600:  return type+"_b600";
		case 1000:  return type+"_b1000";
		case 2000:  return type+"_b2000";
		default:
			return null;
		}
	}


	//p牌局类型 解析
	private String paserGameroomtype(Integer num,String type) {
		if(num==1) {
			return type+"_normal";
		}else if(num==3) {
			return type+"_normalins";
		}else if(num==4) {
			return type+"_six";
		}else if(num==5) {
			return type+"_omaha";
		}else if(num==6) {
			return type+"_omahains";
		}
		return null;
	}
	
	
	private void defineExcuteByDay(long startTime, long zeroTime, int num) {
//        // 往前N天数据封装
//        for(int i=0;i<num;i++) {
//        	statGameStatistic(startTime,zeroTime);
//        zeroTime = startTime;
//        startTime = DateUtils.changeHour(startTime, -24);
//        }
        
        //1 找到30天之前的时间
        
        for(int i=0;i<num;i++) {
        	     zeroTime = startTime;
             startTime = DateUtils.changeHour(startTime, -24);
        }
        
        
        for(int i=0;i<=num;i++) {
        	statGameStatistic(startTime,zeroTime);
        	zeroTime = DateUtils.changeHour(startTime, +24);
        startTime = zeroTime;
        }
	}

}
