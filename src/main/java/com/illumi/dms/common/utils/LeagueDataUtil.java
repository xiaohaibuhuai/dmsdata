package com.illumi.dms.common.utils;

import java.util.List;

import com.illumi.dms.common.Consts;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class LeagueDataUtil {
    
    /*
     * commonBean对象存数据，Num1存大陆人数，Num存非大陆人数
     * 
     * 
     */
    public static CommonBean getTotalNumByArea(int leagueId,long beginDate,long endDate){
        CommonBean commonBean = new CommonBean();
        List<Record>  list = Db.use(Consts.DB_POKER2).findByCache("getLeagueOntablesUserNumByLeagueidAndDate", beginDate+""+endDate,SqlKit.sql("stat.task.getLeagueOntablesUserNumByLeagueidAndDate"), beginDate,endDate);
        int CNNum = 0;
        int OtherNum = 0;
        
        Boolean flag1 = false;
        Boolean flag2 = false;
        for(Record rd:list){
            if(rd.getLong("leagueid") == leagueId){
                flag1 = true;
                flag2 = true;
                if(JudgeAddress(rd.getInt("uuid"), StringUtil.timeStampToDateNormal(beginDate, "yyyy-MM-dd"))){
                    CNNum += rd.getLong("num").intValue();
                }else{
                    OtherNum += rd.getLong("num").intValue();
                }
            }else{
                flag2 = false;
            }
            if(flag1 && !flag2){
                break;
            }
        }
        commonBean.setNum1(CNNum);
        commonBean.setNum2(OtherNum);
        return commonBean;
    }
    
    /*
     * 中国大陆返回true，否则false
     */
    public static boolean JudgeAddress(int uuid ,String date){
        Boolean flag = false;
        List<Record>  list = Db.findByCache("getUserAddressByDate", uuid+date, SqlKit.sql("stat.userdata.getUserAddressByDate"), uuid,date);
        if(list != null && list.size()>0 && list.get(0).getStr("country").equals("CN")){
            if(list.get(0).getStr("province").contains("香港") || list.get(0).getStr("province").contains("澳门") || list.get(0).getStr("province").contains("台湾") ){
                
            }else{
                flag = true;
            }
        }
        return flag;
    }
    

}
