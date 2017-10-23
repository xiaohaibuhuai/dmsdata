package com.illumi.oms.service;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import com.illumi.oms.common.Consts;
import com.illumi.oms.common.utils.CommonBean;
import com.illumi.oms.common.utils.LeagueDataUtil;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.model.LeagueData;
import com.jayqqaa12.jbase.jfinal.ext.model.Db;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author zhangp
 *
 */
public class SyncLeagueDataService {
    private static final Logger log = Logger.getLogger(SyncLeagueDataService.class);
    /**
     * 跑联盟数据
     * @author zhangp
     */
    public void getLeagueData() {
        
        try{
            for(int a = 2;a<=14 ; a++){
                List<Record>  leaguelist  = new ArrayList<Record>();
                List<Record>  leagueUserlist  = new ArrayList<Record>();
                List<Record>  leagueGamelist  = new ArrayList<Record>();
                //查出所有联盟，联盟成员数，联盟牌局数，统一按照联盟ID从小到大排序。按照此逻辑去获取数据
                leaguelist  = Db.use(Consts.DB_POKER).find(SqlKit.sql("stat.task.getAllLeagueInfo"));
                leagueUserlist  = Db.use(Consts.DB_POKER).find(SqlKit.sql("stat.task.getLeagueTotalUsers"));
                leagueGamelist  = Db.use(Consts.DB_POKER2).find(SqlKit.sql("stat.task.getLeagueTotalGames"),DateTime.now().minusDays(a).withMillisOfDay(0).getMillis(),DateTime.now().minusDays(a-1).withMillisOfDay(0).getMillis());
                System.out.println(SqlKit.sql("stat.task.getLeagueTotalGames")+DateTime.now().minusDays(a).withMillisOfDay(0).getMillis()+"&&&"+DateTime.now().minusDays(a-1).withMillisOfDay(0).getMillis());
                System.out.println("@@@@a="+a+";leaguelist的大小:"+leaguelist.size()+";leagueUserlist的大小:"+leagueUserlist.size()+";leagueGamelist的大小:"+leagueGamelist.size());
//                for(Record rd:leagueUserlist){
//                    System.out.println(rd.get("leagueid")+"&&"+rd.get("peoplenum"));
//                }
//                for(Record rd:leagueGamelist){
//                    System.out.println(rd.get("leagueid")+"&&"+rd.get("ontablenum")+"&&"+rd.get("gamenum"));
//                }
                int userFlag = 0;
                int gameFlag = 0;
                for(int i=0;i < leaguelist.size(); i++){
                    Record rd = leaguelist.get(i);
                    LeagueData leagueData = new LeagueData();
                    //基本信息设置
                    leagueData.set("leagueid", rd.get("leagueid"));
                    leagueData.set("leaguename", StringUtil.filterEmoji(rd.getStr("leaguename")));
                    leagueData.set("clubid", rd.get("leaguelord"));
                    leagueData.set("clubname", StringUtil.filterEmoji(rd.getStr("clubname")));
                    leagueData.set("maxmembers", rd.get("maxmembers"));
                    //群主登录IP
                    Record udrd = Db.findFirst(SqlKit.sql("stat.player.getIpInfoByUuid"),rd.getInt("createUser"));
                    if(udrd != null){
                        String address = "";
                        if(udrd.getStr("country").equals("CN")){
                            address +="中国";
                        }else{
                            address +=udrd.getStr("country");
                        }
//                        address += udrd.getStr("province")+udrd.getStr("city");
                        leagueData.set("ipaddress", address);
                    }
                    //总人数，牌局数，上桌人次数
                    if(userFlag < leagueUserlist.size()){
                        System.out.println(rd.getLong("leagueid") + " !!!!! " +leagueUserlist.get(userFlag).getLong("leagueid"));
                        boolean userTag = false;
                        if(rd.getLong("leagueid").longValue() == leagueUserlist.get(userFlag).getLong("leagueid").longValue()){
                            userTag = true;
                            System.out.println("1－－ID相等"+userFlag);
                            userFlag = userFlag + 1;
                            System.out.println("1－－ID相等"+userFlag);
                        }else{
                            if(rd.getLong("leagueid").longValue() > leagueUserlist.get(userFlag).getLong("leagueid").longValue()){
                                for(int j = userFlag+1;j<leagueUserlist.size(); ){
                                    if(rd.getLong("leagueid").longValue() == leagueUserlist.get(j).getLong("leagueid").longValue()){
                                        userTag = true;
                                        userFlag = j + 1;
                                        System.out.println("2－－ID相等"+userFlag+";"+j);
                                        break;
                                    }else if(rd.getLong("leagueid").longValue() > leagueUserlist.get(j).getLong("leagueid").longValue()){
                                        userFlag = j + 1;
                                        j++;
                                    }else{
                                        userFlag = j;
                                        break;
                                    }
                                }
                            }
                        }
                        if(userTag){
                            System.out.println("user is true ####"+leagueData.get("leagueid")+"##"+leagueUserlist.get(userFlag-1).get("leagueid")+" ## "+leagueUserlist.get(userFlag-1).get("peoplenum"));
                            leagueData.set("peoplenum", leagueUserlist.get(userFlag-1).getLong("peoplenum"));
                        }
                    }
                    //牌局数，上桌人次数
                    if(gameFlag < leagueGamelist.size()){
                        boolean gameTag = false;
                        if(rd.getLong("leagueid").longValue() == leagueGamelist.get(gameFlag).getLong("leagueid").longValue()){
                            gameTag = true;
                            gameFlag = gameFlag +1;
                        }else{
                            if(rd.getLong("leagueid").longValue() > leagueGamelist.get(gameFlag).getLong("leagueid").longValue()){
                                for(int m = gameFlag+1;m<leagueGamelist.size(); ){
                                    if(rd.getLong("leagueid").longValue() == leagueGamelist.get(m).getLong("leagueid").longValue()){
                                        gameTag = true;
                                        gameFlag = m + 1;
                                        break;
                                    }else if(rd.getLong("leagueid").longValue() > leagueGamelist.get(m).getLong("leagueid").longValue()){
                                        gameFlag = m + 1;
                                        m++;
                                    }else{
                                        gameFlag = m;
                                        break;
                                    }
                                }
                            }
                        }
                        if(gameTag){
                            System.out.println("user is true ####"+leagueData.get("leagueid")+"##"+leagueGamelist.get(gameFlag-1).get("leagueid")+" ## "+leagueGamelist.get(gameFlag-1).get("ontablenum")+" ## "+leagueGamelist.get(gameFlag-1).get("gamenum"));
                            leagueData.set("ontablenum", leagueGamelist.get(gameFlag-1).getLong("ontablenum"));
                            leagueData.set("gamenum", leagueGamelist.get(gameFlag-1).getLong("gamenum"));
                        }
                    }
                    //记录时间
                    leagueData.set("targetDate",DateTime.now().minusDays(a).toString("yyyy-MM-dd"));
                    leagueData.saveAndCreateDate();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    /*
     * 恢复14天数据
     *
     *@date 2017-08-07
     */
    
    public void RestoreLeagueData() {
        try{
            List<LeagueData> list = LeagueData.dao.find("select * from t_league_daily_snapshot where ontablenum > 0 and targetDate BETWEEN ? AND ? ", DateTime.now().minusDays(21).toString("yyyy-MM-dd"), DateTime.now().toString("yyyy-MM-dd"));
            if(list != null){
                System.out.println("恢复行数"+list.size());
                for(LeagueData ld:list){
                    //记录 德州局数 奥马哈局数  6+局数,以及 大盲注和
                    Record record = Db.use(Consts.DB_POKER3).findFirst(SqlKit.sql("stat.task.getLeagueGameNumsAndBlindsByDateAndLeagueid"),ld.get("leagueid"),ld.getDate("targetDate").getTime(),StringUtil.getSpecifiedDayAfter(ld.getDate("targetDate")).getTime());
                    if(record != null){
                        ld.set("NLHE", record.getBigDecimal("NLHE") != null ? record.getBigDecimal("NLHE").intValue() : 0);
                        ld.set("PLO", record.getBigDecimal("PLO") != null ? record.getBigDecimal("PLO").intValue() : 0);
                        ld.set("SIXPLUS", record.getBigDecimal("SIXPLUS") != null ? record.getBigDecimal("SIXPLUS").intValue() : 0);
                        ld.set("bigblind", record.getBigDecimal("bigblind") != null ? record.getBigDecimal("bigblind").intValue() : 0);
                        ld.set("gamenum", ld.getInt("NLHE")+ld.getInt("PLO")+ld.getInt("SIXPLUS"));
                    }
                    //记录 用户地址信息
                    CommonBean  commonBean = LeagueDataUtil.getTotalNumByArea(ld.getInt("leagueid"),ld.getDate("targetDate").getTime(),StringUtil.getSpecifiedDayAfter(ld.getDate("targetDate")).getTime());
                    if(commonBean != null){
                        ld.set("mainLandNum", commonBean.getNum1());
                        ld.set("otherLandNum", commonBean.getNum2());
                        ld.set("ontablenum", ld.getInt("mainLandNum")+ld.getInt("otherLandNum"));
                    }
                    ld.update();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
