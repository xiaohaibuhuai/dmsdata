package com.illumi.oms.common.utils;

import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.service.EmailService;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.log.Logger;

/**
 * IP工具类
 * 
 * 
 */
public class IpUtil {
	private static final Logger log = Logger.getLogger(IpUtil.class);
	/**
	 * 获取登录用户的IP地址
	 * 
	 * 对手机无效
	 * 
	 * @param request
	 * @return
	 */
	private static String baiduAk = ConfigKit.getStr("baidu.key");
	
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	public static String getIpByMobile(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip != null && (ip.equals("127.0.0.1") || ip.startsWith("192")))
			ip = getIp(request);

		return ip;
	}


	public static String getIpAddr() {

		String ip = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("province") + " ";
		info += obj.getString("city") + " ";
		info += obj.getString("isp");

		return info;
	}

	public static String getIpCity() {
		String ip = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=111.206.14.135");
		String info = "";
		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("city");

		return info;
	}

	public static String getIpCity(String ipStr) {
		String ip = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ipStr);
		String info = "";
		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("city");

		return info;
	}
	public static JSONObject getIp(String ip) {
		String result = new Http().get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);
		JSONObject obj;
		try{
			obj = (JSONObject) JSON.parse(result);
		}catch(Exception e){
			log.error("sina iplookup failure :"+e.getMessage()+" |ip:"+ip);
			obj = null;
		}
		return obj;
	}
	public static String getIpCityByBaidu(String ipStr) {
		String ip = new Http().get("http://api.map.baidu.com/location/ip?ak=CEfb36fa38af476ec5d4703bb7aa1344&ip=" + ipStr);
		String info = "";
		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getJSONObject("content").getString("address");
		return info;
	}
	
	public static JSONObject getIpJsonByBaidu(String ipStr) {
		String ip = new Http().get("http://api.map.baidu.com/location/ip?ak="+baiduAk+"&ip=" + ipStr);
		JSONObject obj;
		try{
			obj = (JSONObject) JSON.parse(ip);
			if(obj.getLong("status") !=1 && obj.getLong("status") !=0 && obj.getLong("status") != 2){
				log.info("baidu ip error code"+ obj.getLong("status")+ ";IP" + ipStr);
				//ConfigKit.getStr("tp.report.email")
				EmailService.sendThirdPartyReport(ConfigKit.getStr("server.alert.mail"), ConfigKit.getStr("tp.report.devemail"),DateTime.now().toString("yyyy-MM-dd")+" IP库 分析异常,百度地图API调用失败，","baidu ip error code"+ obj.getLong("status")+ ";IP" + ipStr);
			}
		}catch(Exception e){
			log.error("baidu iplookup failure :"+e.getMessage()+" |ip:"+ip);
			obj = null;
		}
		return obj;
	}

	public static String getWeather(String location) {

		String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location
				+ "&output=json&ak=640f3985a6437dad8135dae98d775a09";
		String ip = new Http().get(url);

		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);

		if ("success".equals(obj.getString("status"))) {
			JSONObject data = obj.getJSONArray("results").getJSONObject(0);

			data = data.getJSONArray("weather_data").getJSONObject(0);
			info += info += location + "天气  ";
			info += data.getString("weather") + " ";
			info += data.getString("wind") + " ";
			info += data.getString("temperature") + " ";
		}

		return info;
	}

	public static long strToLong(String strip) {
		long[] ip = new long[4];
		int position1 = strip.indexOf(".");
		int position2 = strip.indexOf(".", position1 + 1);
		int position3 = strip.indexOf(".", position2 + 1);
		ip[0] = Long.parseLong(strip.substring(0, position1));
		ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strip.substring(position3 + 1));
		return ((ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]);
	}
	
	public static void main(String[] args) {
        System.out.println(getIpCityByBaidu("134.196.84.42"));
    }

}
