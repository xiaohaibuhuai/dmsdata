package com.illumi.oms.system.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart
{
	public List<String> categories = new ArrayList<String>();
	
	public List<Object> series = new ArrayList<Object>();
	
	
	public void setSeriesDate(String name ,String type,List  data){
		
		Map<String ,Object> datas = new HashMap<String ,Object>();
		
		datas.put("name", name);
		datas.put("type", type);
		datas.put("data", data);
		series.add( datas);
	}
}