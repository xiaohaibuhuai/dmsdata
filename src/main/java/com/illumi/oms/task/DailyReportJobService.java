package com.illumi.oms.task;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.client.Response;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.illumi.oms.common.Consts;
import com.illumi.oms.data.model.SnapShot.DiamondRechargeSnapShotDate;
import com.illumi.oms.data.model.SnapShot.DiamondSnapShotDate;
import com.illumi.oms.data.model.SnapShot.MoneySnapShotDate;
import com.illumi.oms.data.model.SnapShot.RechargeSnapShotDate;
import com.illumi.oms.data.utils.ArithUtils;
import com.illumi.oms.data.utils.DateUtils;
import com.illumi.oms.data.utils.ELKUtils;
import com.illumi.oms.data.utils.LogMapperUtils;
import com.jayqqaa12.jbase.jfinal.ext.model.EasyuiModel;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

//每日日报快照
public class DailyReportJobService implements Job{
	private static final Logger log = Logger.getLogger(DailyReportJobService.class);
	private List<Record> uuids = null;
	private String[] rechargeMapperArr= {"101","102","201","202","301","302","303","401","402","403","sum"};
//	private Map<String,String> diamondMapperMap = null;
//	private Map<String,String> moneyMapperMap = null;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		 log.info("记录每日日报快照任务开始...");
		//1 执行recharge充值日报
		 long zeroTime = DateUtils.changeHour(DateUtils.getCurrentZeroTime(), -24);
		 startJob(zeroTime);
		 
		//2 执行循环任务
		//defineExcuteByDay(zeroTime, 30);
		
		 log.info("记录每日开局快照任务结束...");
	}
	
	
	
	
	public void startJob(long zeroTime) {
	   
		//0 查一天 起始时间和结束时间一致
		try {
		//1 执行recharge充值日报
		rechargeJob(zeroTime);
		//2 执行diamond
		diamondJob(zeroTime,zeroTime);
		//3 执行德扑币
		moneyJob(zeroTime,zeroTime);
		
		//4执行德扑币充值日报
		rechargeDiamondJob(zeroTime);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	





	private void rechargeDiamondJob(long zeroTime) {
		String termsFiled = "ChannelId";
		String sumFiled  = "diamond_change_no";
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String method = "GET";
		String url = urlhead+new SimpleDateFormat("yyyy-MM").format(zeroTime)+urlend;
		Response fullData = ResponseFullData(zeroTime, zeroTime, termsFiled, sumFiled,url,method);
		//全部
		Map<Long, Map<String, Long>> fullMap = ELKUtils.paseDailyResponse(fullData, "money");
		//填充数据
		DiamondRechargeSnapShotDate all = fillDiamondRechargeData(fullMap,0,DiamondRechargeSnapShotDate.class);
		
		
		//国外
		Response abroadData = ResponseAbroadData(zeroTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> abroadMap = ELKUtils.paseDailyResponse(abroadData, "money");
		DiamondRechargeSnapShotDate abroad = null;
		if(abroadMap.values().size()!=0) {
		 abroad = fillDiamondRechargeData(abroadMap,2,DiamondRechargeSnapShotDate.class);
		}else {
			abroad = new DiamondRechargeSnapShotDate();
			abroad.set("targetdate", all.get("targetdate"));
			abroad.set("type", 2);
			for(String s : rechargeMapperArr) {	
				abroad.set(s,0);
			}
		}
		//国内
		DiamondRechargeSnapShotDate  domestic = new DiamondRechargeSnapShotDate();
		domestic.set("targetdate", all.get("targetdate"));
		domestic.set("type", 1);
		for(String s : rechargeMapperArr) {	
			String str1 = all.get(s)+"";
			double a = Double.parseDouble(str1.equals("null")?"0.0":str1);
			String str2 = abroad.get(s)+"";
			double b = Double.parseDouble(str2.equals("null")?"0.0":str2);
			domestic.set(s, ArithUtils.sub(a, b));
		}
		
		
		boolean isuccess1 = all.saveAndCreateDate();
		boolean isuccess2 = abroad.saveAndCreateDate();
		boolean isuccess3 = domestic.saveAndCreateDate();		
		log.info("t_Recharge_daily_snapshot[type 0 :"+isuccess1+"|| type 1:"+isuccess2+"||type 2 "+isuccess3+"]"+"日期:"+DateUtils.getDateFormat4Day().format(zeroTime));
		
		
	}




	private void rechargeJob(long zeroTime) {
		String termsFiled = "ChannelId";
		String sumFiled  = "cach_earn_no";
		String urlhead = "ilumi_payment_";
		String urlend = "/_search?size=30";
		String method = "GET";
		String url = urlhead+new SimpleDateFormat("yyyy-MM").format(zeroTime)+urlend;
		
	
		
		Response fullData = ResponseFullData(zeroTime, zeroTime, termsFiled, sumFiled,url,method);
		//全部
		Map<Long, Map<String, Long>> fullMap = ELKUtils.paseDailyResponse(fullData, "money");
		//填充数据
		RechargeSnapShotDate all = fillRechargeData(fullMap,0,RechargeSnapShotDate.class);
		
		
		//国外
		Response abroadData = ResponseAbroadData(zeroTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> abroadMap = ELKUtils.paseDailyResponse(abroadData, "money");
		RechargeSnapShotDate abroad = null;
		if(abroadMap.values().size()!=0) {
		 abroad = fillRechargeData(abroadMap,2,RechargeSnapShotDate.class);
		}else {
			abroad = new RechargeSnapShotDate();
			abroad.set("targetdate", all.get("targetdate"));
			abroad.set("type", 2);
			for(String s : rechargeMapperArr) {	
				abroad.set(s,0);
			}
		}
		//国内
		RechargeSnapShotDate  domestic = new RechargeSnapShotDate();
		domestic.set("targetdate", all.get("targetdate"));
		domestic.set("type", 1);
		for(String s : rechargeMapperArr) {	
			String str1 = all.get(s)+"";
			double a = Double.parseDouble(str1.equals("null")?"0.0":str1);
			String str2 = abroad.get(s)+"";
			double b = Double.parseDouble(str2.equals("null")?"0.0":str2);
			domestic.set(s, ArithUtils.sub(a, b));
		}
		
		
		boolean isuccess1 = all.saveAndCreateDate();
		boolean isuccess2 = abroad.saveAndCreateDate();
		boolean isuccess3 = domestic.saveAndCreateDate();		
		log.info("t_Recharge_daily_snapshot[type 0 :"+isuccess1+"|| type 1:"+isuccess2+"||type 2 "+isuccess3+"]"+"日期:"+DateUtils.getDateFormat4Day().format(zeroTime));
		
	}
	private void moneyJob(long startTime, long zeroTime) {
		String termsFiled = "action_name";
		String sumFiled  = "money_change_no";
		String urlend = "/_search?size=30";
		String urlhead[] = {"ilumi_transactionlog_","ilumi_minigame_"};
		String method = "GET";
	//	String url = ELKUtils.getUrl(startTime, urlhead, urlend, new SimpleDateFormat("yyyy-MM"));
		String url = urlhead[0]+new SimpleDateFormat("yyyy-MM").format(zeroTime)+","+urlhead[1]+new SimpleDateFormat("yyyy-MM").format(zeroTime)+urlend;
		
		Response fullData = ResponseFullData(startTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> fullMap = ELKUtils.paseDailyResponse(fullData, "money");
		MoneySnapShotDate all = fillMoneyData(fullMap,0);
		//国外
		Response abroadData = ResponseAbroadData(startTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> abroadMap = ELKUtils.paseDailyResponse(abroadData, "money");
		MoneySnapShotDate abroad = fillMoneyData(abroadMap,2);
		if(abroadMap.values().size()!=0) {
			 abroad = fillMoneyData(abroadMap,2);
			}else {
				abroad = new MoneySnapShotDate();
				abroad.set("targetdate", all.get("targetdate"));
				abroad.set("type", 2);
				for(String s :LogMapperUtils.getMoneyMapper().keySet()) {	
					abroad.set(s,0);
				}
			}
		//3国内
		//国内
		MoneySnapShotDate  domestic = new MoneySnapShotDate();
	    domestic.set("targetdate", all.get("targetdate"));
		domestic.set("type", 1);
		for(String s : LogMapperUtils.getMoneyMapper().keySet()) {	
			double a = Double.parseDouble(all.get(s)+"");
			String str = abroad.get(s)+"";
			double b = Double.parseDouble(str.equals("null")?"0.0":str);
			domestic.set(s, ArithUtils.sub(a, b));
		}	
		boolean isuccess1 = all.saveAndCreateDate();
		boolean isuccess2 = abroad.saveAndCreateDate();
		boolean isuccess3 = domestic.saveAndCreateDate();		
		log.info("t_money_daily_snapshot[type 0 :"+isuccess1+"|| type 1:"+isuccess2+"||type 2 "+isuccess3+"]"+"日期:"+DateUtils.getDateFormat4Day().format(zeroTime));
		
	}
	private void diamondJob(long startTime, long zeroTime) {
		String termsFiled = "action_name";
		String sumFiled  = "diamond_change_no";
		String urlend = "/_search?size=30";
		String urlhead[] = {"ilumi_transactionlog_","ilumi_minigame_"};
		String method = "GET";
		String url = urlhead[0]+new SimpleDateFormat("yyyy-MM").format(zeroTime)+","+urlhead[1]+new SimpleDateFormat("yyyy-MM").format(zeroTime)+urlend;
		
		
		Response fullData = ResponseFullData(startTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> fullMap = ELKUtils.paseDailyResponse(fullData, "money");
		DiamondSnapShotDate all = fillDiamondData(fullMap,0);
		
		//国外
		Response abroadData = ResponseAbroadData(startTime, zeroTime, termsFiled, sumFiled,url,method);
		Map<Long, Map<String, Long>> abroadMap = ELKUtils.paseDailyResponse(abroadData, "money");
		DiamondSnapShotDate abroad = fillDiamondData(abroadMap,2);
		if(abroadMap.values().size()!=0) {
			 abroad = fillDiamondData(abroadMap,2);
			}else {
				abroad = new DiamondSnapShotDate();
				abroad.set("targetdate", all.get("targetdate"));
				abroad.set("type", 2);
				for(String s :LogMapperUtils.getDiamondMapper().keySet()) {	
					abroad.set(s,0);
				}
			}
		//3国内
		//国内
		DiamondSnapShotDate  domestic = new DiamondSnapShotDate();
	    domestic.set("targetdate", all.get("targetdate"));
		domestic.set("type", 1);
		for(String s : LogMapperUtils.getDiamondMapper().keySet()) {	
			double a = Double.parseDouble(all.get(s)+"");
			String str = abroad.get(s)+"";
			double b = Double.parseDouble(str.equals("null")?"0.0":str);
			domestic.set(s, ArithUtils.sub(a, b));
		}
		boolean isuccess1 = all.saveAndCreateDate();
		boolean isuccess2 = abroad.saveAndCreateDate();
		boolean isuccess3 = domestic.saveAndCreateDate();		
		log.info("t_money_daily_snapshot[type 0 :"+isuccess1+"|| type 1:"+isuccess2+"||type 2 "+isuccess3+"]");
	}
	
	
	/**
	 * 每个字段处理方法不同
	 * @param dataMap
	 * @param type
	 * @return
	 */
	private DiamondSnapShotDate fillDiamondData(Map<Long, Map<String, Long>> dataMap, int type) {
		Map<String,String> mapper = LogMapperUtils.getDiamondMapper();
		DiamondSnapShotDate ds = new DiamondSnapShotDate();		
		//1设置类型
		ds.set("type", type);
		for(Entry<Long, Map<String, Long>> entry:dataMap.entrySet()) {
			//2 时间
			ds.set("targetdate", entry.getKey());
			Map<String, Long> value = entry.getValue();
			Double sum=0.0;
			for(Entry<String, String> e : mapper.entrySet()) {
				String data = e.getKey(); // 数据库字段
				String log = e.getValue(); //日志字段
				if(value.containsKey(log)) {
					// 消耗变换量  乘负一
					   ds.set(data, value.get(log)*-1);
					   sum = ArithUtils.add(sum,value.get(log)*-1);
				}else {
					ds.set(data,0);
				}

		}
			ds.set("sum", sum);
			return ds;
			
      }
		return ds;
	}
	
	
	
	 //钻石充值   Long
    private <T extends EasyuiModel>T fillDiamondRechargeData(Map<Long, Map<String, Long>> fullMap, Integer type,Class<T> clazz) {
	   T rs = null;
	try {
		rs = clazz.newInstance();
	} catch (Exception e) {
		e.printStackTrace();
	}
	   rs.set("type",type);
	   String[] arrs= {"101","102","201","202","301","302","303","401","402","403","sum"};
	   Long sum = 0l;
	   //只有一个map
	   for(Entry<Long, Map<String, Long>> e :fullMap.entrySet()) {
		   rs.set("targetdate", e.getKey());
		   Map<String, Long> value = e.getValue();
		   for(String s :arrs) {
			   if(value.containsKey(s)) {
				   long div = value.get(s);
				   rs.set(s, div);
				   sum=div+sum;
			   }else {
				   rs.set(s, 0);
			   }
			   
		   }
		   rs.set("sum",sum);
	       return  rs;   
	   } 
		return rs;
	}
	
	
    //充值
    private <T extends EasyuiModel>T fillRechargeData(Map<Long, Map<String, Long>> fullMap, Integer type,Class<T> clazz) {
	   T rs = null;
	try {
		rs = clazz.newInstance();
	} catch (Exception e) {
		e.printStackTrace();
	}
	   rs.set("type",type);
	   String[] arrs= {"101","102","201","202","301","302","303","401","402","403","sum"};
	   Double sum = 0.0;
	   //只有一个map
	   for(Entry<Long, Map<String, Long>> e :fullMap.entrySet()) {
		   rs.set("targetdate", e.getKey());
		   Map<String, Long> value = e.getValue();
		   for(String s :arrs) {
			   if(value.containsKey(s)) {
				   double div = ArithUtils.div(value.get(s).doubleValue(),100);
				   rs.set(s, div);
				   sum = ArithUtils.add(sum, div);
			   }else {
				   rs.set(s, 0);
			   }
			   
		   }
		   rs.set("sum",sum);
	       return  rs;   
	   } 
		return rs;
	}

    //德扑币
    private MoneySnapShotDate fillMoneyData(Map<Long, Map<String, Long>> dataMap,Integer type) {
		Map<String,String> mapper = LogMapperUtils.getMoneyMapper();
		MoneySnapShotDate ds = new MoneySnapShotDate();		
		//1设置类型
		ds.set("type", type);
		for(Entry<Long, Map<String, Long>> entry:dataMap.entrySet()) {
			//2 时间
			ds.set("targetdate", entry.getKey());
			
			Map<String, Long> value = entry.getValue();
			Double sum=0.0;
			for(Entry<String, String> e : mapper.entrySet()) {
				String data = e.getKey(); // 数据库字段
				String log = e.getValue(); //日志字段
				if(value.containsKey(log)) {
					   if(log.equals("玩家买入")) {
						   ds.set(data, value.get(log)*0.1*-1);
					   }else {
						   //消耗变化量 乘负一
						   ds.set(data, value.get(log)*-1);   
					   }
					   sum = ArithUtils.add(sum,value.get(log)*-1);
				}else {
					ds.set(data,0);
				}

		}
			ds.set("sum", sum);
			return ds;
			
      }
		return ds;
	}
	
	
	private Response ResponseFullData(Long dateStart, Long dateEnd,String termsFiled,String sumFiled,String url,String method) {
		String jsonString = getRequestJson(dateStart, dateEnd,termsFiled,sumFiled);
//		System.out.println(jsonString);
//		System.out.println(method);
//		System.out.println(url);
		Response response = ELKUtils.getData(jsonString, method, url);
		return response;
	}
	
	
	
	private Response ResponseAbroadData(Long dateStart, Long dateEnd,String termsFiled,String sumFiled,String url,String method) {
	
		List<Record> uuids = initUuid();
		//log.info("uuids Num:"+uuids.size());
		String jsonString = getRequestAbroadJson(dateStart,dateEnd,uuids,termsFiled,sumFiled);
		Response response = ELKUtils.getData(jsonString, method, url);
//		if (response == null) {
//			renderJson(IErrCodeEnum.DATE_NOT_FOUND);
//		}
		return response;
	}
	
	
	
	private List<Record> initUuid() {
		if(uuids==null) {
			uuids = Db.use(Consts.DB_POKER).find(SqlKit.sql("data.reportForms.getForeignUUID"));
			System.out.println("********访问数据库*********");
			
		}
		return uuids;
	}
	private String getRequestAbroadJson(Long dateStart,Long dateEnd,List<Record> arrayList,String termsFiled,String sumFiled) {
		String uuids="";
		for(int i =0,n=arrayList.size();i<n;i++) {
			String target = arrayList.get(i).getInt("uuid")+"";
			if(i==n-1) {
				uuids+="\""+target+"\"";
			}else {
				uuids+="\""+target+"\""+",";
			}
		}
		String json="{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + 
				"        {\"constant_score\": {\n" + 
				"          \"filter\": {\n" + 
				"            \"range\": {\n" + 
				"              \"@timestamp\": {\n" + 
				"                \"gte\": \""+DateUtils.getDateFormat4Day().format(dateStart)+"\",\n" + 
				"                \"lte\": \""+DateUtils.getDateFormat4Day().format(dateEnd)+"\",\n" + 
				"                \"time_zone\":\"+08:00\"\n" + 
				"              }\n" + 
				"            }\n" + 
				"          },\n" + 
				"          \"boost\": 1.2\n" + 
				"        }\n" + 
				"          \n" + 
				"        },{\n" + 
				"          \"constant_score\": {\n" + 
				"            \"filter\": {\n" + 
				"              \"terms\": {\n" + 
				"                \"Uuid\": [\n" + 
                "                  "+uuids+"\n" + 
				"                ]\n" + 
				"              }\n" + 
				"            },\n" + 
				"            \"boost\": 1.2\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"NAME\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"@timestamp\",\n" + 
				"        \"interval\": \"day\",\n" + 
				"          \"time_zone\":\"+08:00\"\n" + 
				"      }, \"aggs\": {   \n" + 
				"        \"sum\":{\n" + 
				"     \"terms\": {\n" + 
				"       \"field\": \""+termsFiled+"\",\n" + 
				"       \"show_term_doc_count_error\": true,\n" + 
				"       \"shard_size\": 30,\n" + 
				"       \"order\": {\n" + 
				"         \"money_sum\": \"desc\"\n" + 
				"       }\n" + 
				"      },\"aggs\": {\n" + 
				"        \"money_sum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \""+sumFiled+"\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }}\n" + 
				"    }\n" + 
				"  }\n" + 
				"  \n" + 
				"}";
		return json;
	}

	
    private String getRequestJson(Long stime, Long etime,String termsFiled,String sumFiled) {
	
		String json="{\n" + 
				"  \"query\": {\n" + 
				"    \"constant_score\": {\n" + 
				"      \"filter\": {\"range\": {\n" + 
				"        \"@timestamp\": {\n" + 
				"          \"gte\": \""+DateUtils.getDateFormat4Day().format(stime)+"\",\n" + 
				"          \"lte\": \""+DateUtils.getDateFormat4Day().format(etime)+"\",\n" + 
				"          \"time_zone\":\"+08:00\"\n" + 
				"\n" + 
				"        }\n" + 
				"      }}\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"NAME\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"@timestamp\",\n" + 
				"        \"interval\": \"day\",\n" + 
				"          \"time_zone\":\"+08:00\"\n" + 
				"      }, \"aggs\": {   \n" + 
				"        \"sum\":{\n" + 
				"     \"terms\": {\n" + 
				"       \"field\": \""+termsFiled+"\",\n" + 
				"       \"show_term_doc_count_error\": true,\n" + 
				"       \"shard_size\": 30,\n" + 
				"       \"order\": {\n" + 
				"         \"money_sum\": \"desc\"\n" + 
				"       }\n" + 
				"      },\"aggs\": {\n" + 
				"        \"money_sum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \""+sumFiled+"\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }}\n" + 
				"    }\n" + 
				"  }\n" + 
				"  \n" + 
				"}";
  
		return json;
}

    /*
    private Map<String, String> initDiamondMapper() {
		if(diamondMapperMap==null) {
			diamondMapperMap = new HashMap<String,String>();
			// 110001 -- 联盟局设置.          alliance   null
			diamondMapperMap.put("alliance", "联盟局");
			// 450002 -- 修改昵称:            changename  null
			diamondMapperMap.put("changename", "修改昵称");
			//430003 -- 延时道具              delayprops  null
			diamondMapperMap.put("delayprops", "延时道具");
			//460001  --  Mtt门票             ticketmtt   null
			diamondMapperMap.put("ticketmtt", "MTT门票");
			//450004 -- 俱乐部推送             clubpush    null
			diamondMapperMap.put("clubpush", "俱乐部推送");
			//450005 -- 俱乐部修改名称         changecname  null
			diamondMapperMap.put("changecname", "俱乐部改名");
		}
		return diamondMapperMap;
	}
    
    public Map<String,String> initMoneyMapper(){
       if(moneyMapperMap==null) {
    	   moneyMapperMap = new HashMap<String,String>();
		//110000   玩家买入*0.1          服务费
    	   moneyMapperMap.put("service", "玩家买入");
		//430001    表情id      1 魔法表情   2互动道具   null
    	   moneyMapperMap.put("Interprops", "互动道具");
    	   moneyMapperMap.put("magic", "魔法表情");
		//430004 翻翻看   
    	   moneyMapperMap.put("lookover", "翻翻看");
		//450003 -- 发送弹幕   null
    	   moneyMapperMap.put("barrage", "弹幕");
		//500000  扑克机
    	   moneyMapperMap.put("pokermachine", "扑克机游戏");
		//500001   加勒比
    	   moneyMapperMap.put("caribbean", "加勒比游戏");	
	    //500002    红黑大战   数据库和日志都无		
	    //510000  游戏代码    1  牛牛  2 八八碰
    	   moneyMapperMap.put("cow", "牛牛_一粒大米");
    	   moneyMapperMap.put("eight", "八八碰_一粒大米");	
	    //800002  打赏牌谱
    	   moneyMapperMap.put("rewardpoker", "打赏牌谱");
		//120001 德扑币报名
    	   moneyMapperMap.put("mttsign", "德扑币报名MTT");
       }
       return moneyMapperMap;
    	
    }
    */
    
    /**
     * 
     * @param zeroTime
     * @param num
     */
    public void defineExcuteByDay(long zeroTime, int num) {
    	
    	  for(int i=0;i<num-1;i++) {
    		  zeroTime = DateUtils.changeHour(zeroTime, -24);
       }
   
      
      for(int i=0;i<num;i++) {
   	    startJob(zeroTime);
   	    zeroTime = DateUtils.changeHour(zeroTime, +24);
     }
    } 
}
