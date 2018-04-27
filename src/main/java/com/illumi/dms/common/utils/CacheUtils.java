package com.illumi.dms.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.illumi.dms.common.Consts;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

public class CacheUtils {
    private static CacheUtils instance;
    
    private CacheUtils(){
        
    }
    
    //单例模式
    public static CacheUtils getInstance() {  
        if (instance == null) { 
            synchronized(CacheUtils.class){
                instance = new CacheUtils();
            }
        }  
        return instance;  
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        //-----------通过MTT比赛ID获取MTT该局数据－－－－－－－－－－－－
        if(cacheName.equals("getMttDataByRoomid")){
            List<Record> list = CacheKit.get(cacheName, key);
            if(list == null){
                List<Record> newlist = new ArrayList<Record>();
                list = get("getAllRankByRoomid", key);
                if(list != null){
                    for(Record rd:list){
                        // 用户普通局数据和MTT总数据
                        Record curRecord = get("getMttDataByUUID", rd.get("uuid"));
                        if(curRecord != null){
                            //该局MTT信息
                            curRecord.set("mttrank", rd.get("mttrank"));
                            curRecord.set("nickname", rd.get("nickname"));
                            //该局MTT信息重进信息
                            Record entrynum = get("getEntryNumByUuidAndRoomid", rd.getInt("uuid")+"-"+rd.get("roomid"));
                            if(entrynum != null){
                                curRecord.set("entrynum", entrynum.getLong("signnum")+entrynum.getLong("entrynum")-entrynum.getLong("nosignnum"));
                            }
                            newlist.add(curRecord);
                        }
                    }
                }
                CacheKit.put(cacheName, key , newlist); 
                list = newlist;
            }
            return (T) list;
        }
        
        //-----------通过MTT比赛ID获取MTT所有参赛人员(有分页)－－－－－－－－－－－－
        if(cacheName.equals("getRankByRoomid")){
////            DataGrid<Record> dg = CacheKit.get("getRankByRoomid", getPara("roomid")+"-"+getDataGrid().page+"+"+getDataGrid().total);
//            DataGrid<Record> dg = CacheKit.get(cacheName, key);
//            if(dg == null){
//                dg = RecordUtil.listByDataGrid(Consts.DB_POKER2,SqlKit.sql("mtt.poker.getRankByRoomid"),getDataGrid(), getDetailForm());
//                CacheKit.put("getRankByRoomid", key, dg); 
//            }  
//            
        }
        //-----------通过MTT比赛ID获取MTT所有参赛人员－－－－－－－－－－－－
        if(cacheName.equals("getAllRankByRoomid")){
            List<Record> list = CacheKit.get(cacheName, key);
            if(list == null){
                list = Db.use(Consts.DB_POKER2).find(SqlKit.sql("mtt.poker.getAllRankByRoomid"),key);
                CacheKit.put(cacheName, key , list);
            }
            return (T) list;
        }
        
        //-----------通过UUID获取该用户MTT数据－－－－－－－－－－－－
        if(cacheName.equals("getMttDataByUUID")){
            Record curRecord = CacheKit.get("getMttDataByUUID", key);
            if(curRecord == null){
                curRecord = new Record();
                curRecord.set("uuid",key);
                //玩家基本获奖信息
                Record user = Db.use(Consts.DB_POKER).findFirst(SqlKit.sql("stat.player.getPlayerByUuid"), key);
                if(user!=null){
                    curRecord.set("showid", user.getStr("showid"));
                    //玩家MTT参赛信息
                    Record mttrd = Db.use(Consts.DB_POKER2).findFirst(SqlKit.sql("mtt.poker.countMTTNumByUUID"), key);
                    curRecord.set("totalnum", mttrd.get("totalnum"));
                    curRecord.set("totalprizenum", StringUtil.divisionLTWO(mttrd.getLong("totalprizenum"), mttrd.getLong("totalnum")));
                    //玩家MTT重进信息
                    Record entrytotalnum = Db.use(Consts.DB_STAT).findFirst(SqlKit.sql("mtt.stat.getAllEntryNumByUuid"), key);
                    curRecord.set("entrytotalnum", entrytotalnum.getLong("signnum")+entrytotalnum.getLong("entrynum")-entrytotalnum.getLong("nosignnum"));
                    //玩家MTT普通局盲注信息
                    List<Record> rdlist =  Db.use(Consts.DB_POKER2).find(SqlKit.sql("stat.player.getBlindDataByUuidAndDate"),get30DayByUUID((int)key));
                    if(rdlist != null && rdlist.size() >0){
                        long totalnum = 0;
                        long totallevel = 0;
                        long smallBlindNum = 0;
                        long middleBlindNum = 0;
                        long bigBlindNum = 0;
                        for(Record brd:rdlist){
                            totalnum += brd.getLong("num");
                            totallevel += brd.getInt("smallblind")*2*brd.getLong("num");
                            if(brd.getInt("smallblind") <=5){
                                smallBlindNum += brd.getLong("num");
                            }else if(brd.getInt("smallblind") >=50){
                                bigBlindNum += brd.getLong("num");
                            }else{
                                middleBlindNum += brd.getLong("num");
                            }
                        }
                        Record mostRecord = rdlist.get(0);
                        curRecord.set("mostBind", mostRecord.get("num")+"("+mostRecord.getInt("smallblind")+"/"+mostRecord.getInt("smallblind")*2+")");
                        curRecord.set("totalnumcommon", totalnum);
                        curRecord.set("averageBigBlind", StringUtil.division(totallevel, totalnum));
                        curRecord.set("smallBlindNum", smallBlindNum);
                        curRecord.set("middleBlindNum", middleBlindNum);
                        curRecord.set("bigBlindNum", bigBlindNum);
                    }
                    //玩家MTT普通局盈利信息
                    curRecord.set("totalBonus", Db.use(Consts.DB_POKER2).queryBigDecimal(SqlKit.sql("stat.player.getCommonTotalBonus"),get30DayByUUID((int)key)));
                }
                CacheKit.put("getMttDataByUUID", key, curRecord);
            }
            return (T) curRecord;
        }
        
        //-----------通过MTT比赛ID和用户UUID获取MTT该局该玩家重进信息数据－－－－－－－－－－－－
        if(cacheName.equals("getEntryNumByUuidAndRoomid")){
            //rd.getInt("uuid")+"-"+rd.get("roomid")
            //该局MTT信息重进信息
            Record entrynum = CacheKit.get(cacheName,key);
            if(entrynum == null){
                //2017.07.20更新MTT后，排名信息表可直接获取重进次数，当前比赛ID23316635
                if(Integer.parseInt(String.valueOf(key).split("-")[1])>=23316635){
                    entrynum = Db.use(Consts.DB_POKER2).findFirst(SqlKit.sql("mtt.stat.getNewEntryNumByUuidAndRoomid"),String.valueOf(key).split("-")[0],String.valueOf(key).split("-")[1]);
                    entrynum.set("entrynum", entrynum.getInt("reentrynum").longValue());
                    entrynum.set("signnum", 1l);
                    entrynum.set("nosignnum", 0l);
                }else{
                    entrynum = Db.use(Consts.DB_STAT).findFirst(SqlKit.sql("mtt.stat.getEntryNumByUuidAndRoomid"),String.valueOf(key).split("-")[0],String.valueOf(key).split("-")[1]);
                }
                CacheKit.put("getEntryNumByUuidAndRoomid", key , entrynum);
            }
            return (T) entrynum;
        }
        return null ;
    }
    
    private static Object[] get30DayByUUID(int uuid){
        DateTime dateStart = DateTime.now().minusDays(30).withMillisOfDay(0);;
        DateTime dateEnd =  DateTime.now();
        Object[] obj = {uuid,dateStart.getMillis(),dateEnd.getMillis()};
        return obj;
    }
    
    
}
