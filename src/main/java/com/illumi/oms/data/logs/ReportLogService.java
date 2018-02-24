package com.illumi.oms.data.logs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Created by admin on 2018/2/24.
 */

public class ReportLogService {
    Log   mLogReport;
    private static ReportLogService instance;

    public ReportLogService()
    {
        //读取配置文件
        mLogReport  =   LogFactory.getLog("report");

    }

    //单例模式
    public static ReportLogService getInstance() {
        if (instance == null) {
            synchronized(ReportLogService.class){
                instance = new ReportLogService();
            }
        }
        return instance;
    }

    public void error(String content){
        mLogReport.error(content);

    }

}
