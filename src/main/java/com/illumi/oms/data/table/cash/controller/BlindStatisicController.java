package com.illumi.oms.data.table.cash.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.illumi.oms.common.Consts;
import com.illumi.oms.common.UrlConfig;
import com.illumi.oms.data.utils.DateUtils;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jayqqaa12.model.easyui.DataGrid;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/data/table/cash/blindstatistic", viewPath = UrlConfig.DATA_TAB_CASH)
public class BlindStatisicController extends EasyuiController<Record>{

    private static Map<Integer,List<Record>> map = new HashMap<>();
    private static Map<String,Map<String,Map<String,String>>> chartMap = null; 
    private static Long time = null; //过期时间
    private static final Logger log = Logger.getLogger(BlindStatisicController.class);
    
    //热力图
	public void sum() {
		//获取当天的零点时间
		 long dateEnd = DateUtils.getCurrentZeroTime();
		if(chartMap!=null&&!isOverTimeMap(dateEnd)) {
			log.info("从chartMap中获取数据||"+formatTime(time));
			renderJson(chartMap);
		}else {	
			List<Record> list1 = getBlindDateByType(1); 
			List<Record> list3 = getBlindDateByType(3);
			List<Record> list5 = getBlindDateByType(5);
			List<Record> list6 = getBlindDateByType(6);
			Map<String,Map<String,String>> typeMap1 = new HashMap<>();
			setDate2TypeMap(typeMap1,list1);
			Map<String,Map<String,String>> typeMap3 = new HashMap<>();
			setDate2TypeMap(typeMap3,list3);
			Map<String,Map<String,String>> typeMap5 = new HashMap<>();
			setDate2TypeMap(typeMap5,list5);
			Map<String,Map<String,String>> typeMap6 = new HashMap<>();
			setDate2TypeMap(typeMap6,list6);
		    Map<String,Map<String,Map<String,String>>> blindMap = new HashMap<>();
		    blindMap.put("普通局", typeMap1);
		    blindMap.put("普通保险局", typeMap3);
		    blindMap.put("奥马哈局", typeMap5);
		    blindMap.put("奥马哈保险局", typeMap6);
		    
		    chartMap=blindMap; //更新chartMap
		    time=dateEnd;//更新时间
		    renderJson(chartMap);
		}
		
	}
	//普通局
	public void getBlindNormal() {
		List<Record> list = getBlindDateByType(1);
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}
	
	//普通保险局
	public void getBlindNormalins() {
		List<Record> list = getBlindDateByType(3);
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}
	
	//奥马哈局
	public void getBlindOmaha() {
		List<Record> list = getBlindDateByType(5);
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}
	//奥马哈保险局
	public void getBlindOmahains() {
		List<Record> list = getBlindDateByType(6);
		DataGrid<Record> data = new DataGrid<>();
		data.setRows(list);
		renderJson(data);
	}


	/**
	 * 会有并发访问问题, map会覆盖,之后访问走缓存
	 * @param type
	 * @return
     */
	private List<Record> getBlindDateByType(int type) {
		long dateEnd = DateUtils.getCurrentZeroTime();

		List<Record> gameInfo = null;
		if(map.containsKey(type)&&!isOverTimeMap(dateEnd)) {
		 gameInfo = map.get(type);
		 log.info("从map中获取数据||"+"牌局类型:"+type+"||"+formatTime(time));
		}else {
			// 2 查数据库   一次查询
			// 定时任务3.45    判断是否已经跑完定时任务
			if(System.currentTimeMillis()<=DateUtils.changeHour(dateEnd,+5)){
				gameInfo = map.get(type);
				log.info("从map中获取数据||"+"牌局类型:"+type+"||"+formatTime(time));
			}else{
				long dateStart = DateUtils.changeHour(dateEnd,-14*24);
				Integer [] arrys = {1,3,5,6};
				for(int i=0;i<arrys.length;i++){
					gameInfo= Db.use(Consts.DB_POKERDATA).find(SqlKit.sql("data.reportForms.getBlindInfoByType"),new Object[]{arrys[i],dateStart,dateEnd});
					//倒序
					//Collections.reverse(gameInfo);
					map.put(arrys[i], gameInfo);
					//设置查询时间
				}
				time=dateEnd;
			}

		}
		return gameInfo;
	}
	
	/**
	 * 判断map是否过期  过期返回true
	 * @param dateEnd
	 * @return
	 */
	private boolean isOverTimeMap(long dateEnd) {
		if(dateEnd==time) {
			return false;
		}
		return true;
	}
	
	
	
	private String formatTime(Long time) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(time);
	}
	
	
	private void setDate2TypeMap(Map<String, Map<String, String>> typeMap, List<Record> list) {
		for(Record r:list) {
			Map<String,String> dayMap = new HashMap<>();
			//不能为空值 图表出错
			dayMap.put("1/2",paseResult(r,"g_b2"));
			dayMap.put("2/4",paseResult(r,"g_b4"));
			dayMap.put("5/10",paseResult(r,"g_b10"));
			dayMap.put("10/20",paseResult(r,"g_b20"));
			dayMap.put("20/40",paseResult(r,"g_b40"));
			dayMap.put("25/50",paseResult(r,"g_b50"));
			dayMap.put("50/100",paseResult(r,"g_b100"));
			dayMap.put("100/200",paseResult(r,"g_b200"));
			dayMap.put("200/400",paseResult(r,"g_b400"));
			dayMap.put("300/600",paseResult(r,"g_b600"));
			dayMap.put("500/1000",paseResult(r,"g_b1000"));
			dayMap.put("1000/2000",paseResult(r,"g_b2000"));
			String stime = formatTime(r.getLong("targetdate"));
			typeMap.put(stime, dayMap);
		}
		
	}
	
	
	
	/**
	 * 图表值为null 返回 “0” 
	 * @param r
	 * @param target
	 * @return
	 */
	private String paseResult(Record r, String target) {
		Integer result = r.getInt(target);
		if(result==null) {
			return "0";
		}
		return result+"";
	}
	
}
